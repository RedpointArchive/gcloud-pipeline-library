def call(java.util.LinkedHashMap config, org.jenkinsci.plugins.workflow.cps.CpsClosure2 block) {
  def cwd = pwd()
  sh 'mkdir .gcloud'
  try {
    withEnv(['GCLOUD_CONFIG=' + cwd + '/.gcloud', 'BOTO_CONFIG=' + cwd + '/.gcloud/boto.cfg']) {
      // Copy the service account JSON file to our Google Cloud config directory (so it doesn't
      // get deleted once withCredentials goes out of scope).
      if (config["serviceAccountCredential"] != null) {
        withCredentials([file(credentialsId: config["serviceAccountCredential"], variable: 'GOOGLE_APPLICATION_CREDENTIALS')]) {
          sh 'cp "$GOOGLE_APPLICATION_CREDENTIALS" "$GCLOUD_CONFIG/serviceaccount.json"'
        }
      } else if (config["serviceAccountPath"] != null) {
        withEnv(['GOOGLE_APPLICATION_CREDENTIALS=' + config["serviceAccountPath"]]) {
          sh 'cp "$GOOGLE_APPLICATION_CREDENTIALS" "$GCLOUD_CONFIG/serviceaccount.json"'
        }
      }

      // Set up service account authentication in the temporary Google Cloud config path.
      sh 'gcloud config set pass_credentials_to_gsutil false'
      sh 'echo "$GCLOUD_CONFIG/serviceaccount.json" | gsutil config -e -o "$GCLOUD_CONFIG/boto.cfg"'
      sh 'gcloud auth activate-service-account --key-file="$GCLOUD_CONFIG/serviceaccount.json"'

      // Invoke the closure block that the user wants to execute, with the GCLOUD_CONFIG
      // and BOTO_CONFIG environment variables set.
      block()
    }
  } finally {
    dir(cwd) {
      sh 'rm -Rf .gcloud'
    }
  }
}