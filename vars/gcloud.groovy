def call(String serviceAccountPath = null, String serviceAccountCredential = null) {
  return { block ->
    def cwd = pwd()
    sh 'mkdir .gcloud'
    try {
      withEnv(['GCLOUD_CONFIG=' + cwd + '/.gcloud']) {
        if (serviceAccountPath != null) {
          sh ('gcloud auth activate-service-account --key-file="' + serviceAccountPath + '"')
        }
        def wrapper = { nestedBlock -> nestedBlock() }
        if (serviceAccountCredential != null) {
          wrapper = { nestedBlock -> 
            withCredentials([file(credentialsId: serviceAccountCredential, variable: 'GOOGLE_APPLICATION_CREDENTIALS')]) {
              sh ('gcloud auth activate-service-account --key-file="$GOOGLE_APPLICATION_CREDENTIALS"')
              nestedBlock()
            }
          }
        }
        wrapper(block)
      }
    } finally {
      dir(cwd) {
        sh 'rm -Rf .gcloud'
      }
    }
  }
}