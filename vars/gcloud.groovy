def call(java.util.LinkedHashMap config, org.jenkinsci.plugins.workflow.cps.CpsClosure2 block) {
  def cwd = pwd()
  sh 'mkdir .gcloud'
  try {
    withEnv(['GCLOUD_CONFIG=' + cwd + '/.gcloud', 'BOTO_CONFIG=' + cwd + '/.gcloud/boto.cfg']) {
      if (config["serviceAccountCredential"] != null) {
        withCredentials([file(credentialsId: config["serviceAccountCredential"], variable: 'GOOGLE_APPLICATION_CREDENTIALS')]) {
          sh 'gcloud config set pass_credentials_to_gsutil false'
          sh 'echo "$GOOGLE_APPLICATION_CREDENTIALS" | gsutil config -e -o "$(pwd)/.gcloud/boto.cfg"'
          sh 'gcloud auth activate-service-account --key-file="$GOOGLE_APPLICATION_CREDENTIALS"'
          block()
        }
      } else if (config["serviceAccountPath"] != null) {
        withEnv(['GOOGLE_APPLICATION_CREDENTIALS=' + config["serviceAccountPath"]]) {
          sh 'gcloud config set pass_credentials_to_gsutil false'
          sh 'echo "$GOOGLE_APPLICATION_CREDENTIALS" | gsutil config -e -o "$(pwd)/.gcloud/boto.cfg"'
          sh 'gcloud auth activate-service-account --key-file="$GOOGLE_APPLICATION_CREDENTIALS"'
          block()
        }
      } else {
        // No config options set.
        block()
      }
    }
  } finally {
    dir(cwd) {
      sh 'rm -Rf .gcloud'
    }
  }
}