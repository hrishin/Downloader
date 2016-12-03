# Downloader Services

This is CLI unitity to downlaod files from various sources type 

 * http
  * https
   * ftp
    * sftp

### Build

with test

Note: test suit has 1 integration test which require access to file system. Please make sure the source code location
has write file perimission 
```
mvn clean test compile assembly:single
```


without test
```
mvn clean compile assembly:single -Dmaven.test.skip=true
```

### Run

```
java -jar downloader-0.0.1.jar -dl <download path> -s "<downlaod sources in CSV form>"
```
sample
```
java -jar downloader-0.0.1.jar -dl /Users/hrishikeshshinde/downloadtest -s
"https://s3.ap-south-1.amazonaws.com/hriships/HrishikeshShinde_resume.pdf,
ftp://anonymous:myemailname@ftp.hq.nasa.gov/pub/astrophysics/FTP_Instructions.txt"
```
Note: For FTP if reources are protected with username/password, please use the correct URl symatics accodingly (as shown
in above example) otherwise resource would not get download

Note: For -s swicth, value CSV string must be wrapped in double quotes
```
java -jar downloader-0.0.1.jar -h
```

### Fetures
1.Extensible to support different protocols

2.Downlaod big files

3.Auto cleanup of partial data in the final location in any case.

4.Print downlaod progress and status

5.Retrive completed task one by one 

