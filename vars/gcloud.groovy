def call(java.util.LinkedHashMap config, org.jenkinsci.plugins.workflow.cps.CpsClosure2 block) {
  def cwd = pwd()
  sh '(rm -Rf .gcloud || true) && mkdir .gcloud'
  try {
    withEnv([
      'CLOUDSDK_CONFIG=' + cwd + '/.gcloud', 
      'BOTO_CONFIG=' + cwd + '/.gcloud/boto.cfg', 
      'GOOGLE_APPLICATION_CREDENTIALS=' + cwd + '/.gcloud/serviceaccount.json',
      'DOCKER_CONFIG=' + cwd + '/.gcloud/docker.config'
    ]) {
      // Copy the service account JSON file to our Google Cloud config directory (so it doesn't
      // get deleted once withCredentials goes out of scope).
      if (config["serviceAccountCredential"] != null) {
        withCredentials([file(credentialsId: config["serviceAccountCredential"], variable: 'GOOGLE_APPLICATION_CREDENTIALS_TMP')]) {
          sh 'cp "$GOOGLE_APPLICATION_CREDENTIALS_TMP" "$GOOGLE_APPLICATION_CREDENTIALS"'
        }
      } else if (config["serviceAccountPath"] != null) {
        withEnv(['GOOGLE_APPLICATION_CREDENTIALS_TMP=' + config["serviceAccountPath"]]) {
          sh 'cp "$GOOGLE_APPLICATION_CREDENTIALS_TMP" "$GOOGLE_APPLICATION_CREDENTIALS"'
        }
      }

      // Set up service account authentication in the temporary Google Cloud config path.
      sh 'gcloud config set pass_credentials_to_gsutil false'
      sh 'echo "$GOOGLE_APPLICATION_CREDENTIALS" | gsutil config -e -o "$CLOUDSDK_CONFIG/boto.cfg"'
      sh 'gcloud auth activate-service-account --key-file="$GOOGLE_APPLICATION_CREDENTIALS"'
      sh 'gcloud auth print-access-token | docker login -u oauth2accesstoken --password-stdin https://gcr.io'

      // Invoke the closure block that the user wants to execute, with all the appropriate
      // environment variables set.
      block()
    }
  } finally {
    dir(cwd) {
      sh 'rm -Rf .gcloud'
    }
  }
}
