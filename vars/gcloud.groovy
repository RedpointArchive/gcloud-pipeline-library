def call(java.util.LinkedHashMap config, org.jenkinsci.plugins.workflow.cps.CpsClosure2 block) {
  def cwd = pwd()
  sh 'mkdir .gcloud'
  try {
    withEnv(['GCLOUD_CONFIG=' + cwd + '/.gcloud']) {
      if (config["serviceAccountCredential"] != null) {
        withCredentials([file(credentialsId: config["serviceAccountCredential"], variable: 'GOOGLE_APPLICATION_CREDENTIALS')]) {
          sh ('gcloud auth activate-service-account --key-file="$GOOGLE_APPLICATION_CREDENTIALS"')
          block()
        }
      } else if (config["serviceAccountPath"] != null) {
        sh ('gcloud auth activate-service-account --key-file="' + config["serviceAccountPath"] + '"')
        block()
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