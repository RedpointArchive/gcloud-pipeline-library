# Jenkins gcloud pipeline library

This repository contains a `gcloud` pipeline step that you can use to load service account credentials in a temporary environment for your build jobs. Instead of configuring gcloud on each agent, it effectively lets you load service account credentials from Jenkins and use those for invocations of `gcloud` and other commands.

## Usage

You can use this step like so:

```groovy
library "gcloud-pipeline-library"

node {
  gcloud(serviceAccountCredential: 'my-credential-name') {
    sh 'gcloud ...'
  }
}
```

or alternatively:

```groovy
library "gcloud-pipeline-library"

node {
  gcloud(serviceAccountPath: 'path/to/service/account.json') {
    sh 'gcloud ...'
  }
}
```

## Setup

You need to add this library to your Jenkins instance first. Under **Manage Jenkins -> Configure System**, scroll down until you find the **Global Pipeline Libraries** section, then add this repository like so:

![Setup Instructions](./setup.png)

## License

```
MIT License

Copyright (c) 2018 Redpoint Games

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```