def call(String serviceAccount = null, String serviceAccountSecret = null) {
  return { block ->
    def cwd = pwd()
    sh 'mkdir .gcloud'
    try {
      withEnv(['GCLOUD_CONFIG=' + cwd + '/.gcloud']) {
        if (serviceAccount != null) {
          sh ('gcloud auth activate-service-account --key-file="' + serviceAccount + '"')
        }
        def wrapper = { nestedBlock -> nestedBlock() }
        if (serviceAccountSecret != null) {
          wrapper = { nestedBlock -> 
            withCredentials([file(credentialsId: serviceAccountSecret, variable: 'GOOGLE_APPLICATION_CREDENTIALS')]) {
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