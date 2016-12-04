# Downloader Services

This is CLI utility tool to download files from various sources type 

  * http
  * https
  * ftp
  * sftp


### Build
Dev. Environment:

1) Java DK 1.8 +

2) Maven 3.0 +


with test
```
mvn clean compile test assembly:single
```

without test
```
mvn clean compile assembly:single -Dmaven.test.skip=true
```

### Usage

```
java -jar downloader-0.0.1.jar -dl <download path> -s "<download sources in CSV form>"
```
sample
```
java -jar downloader-0.0.1.jar -dl /Users/hrishikeshshinde/downloadtest -s
"https://s3.ap-south-1.amazonaws.com/hriships/HrishikeshShinde_resume.pdf,
ftp://anonymous:myemailname@ftp.hq.nasa.gov/pub/astrophysics/FTP_Instructions.txt"
```
Note: 
* For FTP if reources are protected with username/password, please use the correct URl symatics accodingly (as shown
in above example) otherwise resource would not get download
* For -s swicth, value CSV string must be wrapped in double quotes

For help
```
java -jar downloader-0.0.1.jar -h
```

### Fetures
1.Extensible to support different protocols

2.Download big files

3.Auto cleanup of partial data in the final location in any case.

4.Print download progress and status

5.Concurent downloads and pop completed task one by one rather waiting for all task to finish

