def call(Closure block, String serviceAccountPath = null, String serviceAccountCredential = null) {
  def cwd = pwd()
  sh 'mkdir .gcloud'
  try {
    withEnv(['GCLOUD_CONFIG=' + cwd + '/.gcloud']) {
      if (serviceAccountCredential != null) {
        withCredentials([file(credentialsId: serviceAccountCredential, variable: 'GOOGLE_APPLICATION_CREDENTIALS')]) {
          sh ('gcloud auth activate-service-account --key-file="$GOOGLE_APPLICATION_CREDENTIALS"')
          block()
        }
      } else if (serviceAccountPath != null) {
        sh ('gcloud auth activate-service-account --key-file="' + serviceAccountPath + '"')
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