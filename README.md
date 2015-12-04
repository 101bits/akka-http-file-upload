File Uploader  [![Build Status](https://travis-ci.org/101bits/akka-http-file-upload.svg)](https://travis-ci.org/101bits/akka-http-file-upload)
--

The purpose of the project is to learn building a simple HTTP REST API that uploads a file as `application/octet-stream`.
In Akka-Http there is existing [FileUploadDirectives](http://doc.akka.io/docs/akka-stream-and-http-experimental/2.0-M2/scala/http/routing-dsl/directives/file-upload-directives/index.html#fileuploaddirectives) but they send data as `MultiPart.FormData` which means that data/file is uploaded from a `HTML` form.

The API in this exercise is build so that any `API` client can connect to the Server endpoint and upload the data.

During this time I had a good learning time which is tracked via [Akka Issue](https://github.com/akka/akka/issues/19092).

How to run?
--
* Clone this project
* Make sure project compiles and test pass
```
sbt clean compile test package
```
* Run the file upload server
```
sbt run
```

You should see something similar to following
```
[info] Loading project definition from /Users/harit/IdeaProjects/akka-http-file-upload/project
[info] Set current project to akka-http-file-upload (in build file:/Users/harit/IdeaProjects/akka-http-file-upload/)
[info] Running FileUploadServer 
FileUpload server is running at http://localhost:8080
Press RETURN to stop ...

```
* Hit the API
```
$ curl  -v -X POST -H 'Content-Type: application/octet-stream' --data-binary @Demo_log_004.log http://localhost:8080/upload 
*   Trying 127.0.0.1...
* Connected to localhost (127.0.0.1) port 8080 (#0)
> POST /upload HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.43.0
> Accept: */*
> Content-Type: application/octet-stream
> Content-Length: 63359314
> Expect: 100-continue
> 
< HTTP/1.1 100 Continue
< Server: akka-http/2.4-SNAPSHOT
< Date: Fri, 04 Dec 2015 22:36:58 GMT
* We are completely uploaded and fine
< HTTP/1.1 200 OK
< Server: akka-http/2.4-SNAPSHOT
< Date: Fri, 04 Dec 2015 22:36:58 GMT
< Content-Type: application/json
< Content-Length: 114
< 
{
  "path": "/var/folders/_2/q9xz_8ks73d0h6hq80c7439s8_x7qx/T/debug-7703357566406406583.zip",
  "size": 63359314
}
```

For Large Uploads
---
By default the content size can be maximum of `8m` which is defined under `akka.http.parsing.max-content-length = 8m` in `reference.conf`.

If you need to upload larger files, you need to override this value to what you need. For example, in this codebase, I have this property as 
```
akka.http.parsing.max-content-length = 1000m
```

inside `application.conf`. You may also change it to `infinite`. However, this change applies to all the `API` endpoints you have not just one.