# Source Code Crawler - Step 6

In this demo application [Apache Hadoop](http://hadoop.apache.org/) MapReduce library is used.

## Hadoop in stand-alone mode

Make sure Java installed and JAVA_HOME environment variable set.

Download the latest binary from this [Download](https://hadoop.apache.org/releases.html) page.

Extract ```hadoop-*.tar.gz``` to ```/opt``` directory.

To make command lines shorter add ```/opt/hadoop-*/bin``` to PATH.

Go to this step of project directory.

Create ```input``` directory and copy source code files to it:

```
hdfs dfs -mkdir input
hdfs dfs -put {PATH_TO_PROJECT}/source-code-crawler-step1/src/main/java/org/interactiverobotics/source_code_crawler/step1/dummy/*.java input
```

Directory should contain following files:

+ Animal.java
+ .Animal.java.crc
+ Cat.java
+ .Cat.java.crc
+ Dog.java
+ .Dog.java.crc
+ Named.java
+ .Named.java.crc
+ Pet.java
+ .Pet.java.crc

Run the application:

```
./../gradlew :source-code-crawler-step6:run -PprogramArgs=input,output
```

Directory ```output``` should be created with following files:

+ part-r-00000
+ .part-r-00000.crc
+ _SUCCESS
+ ._SUCCESS.crc

```text``` command can be used to dump the result:

```
hdfs dfs -text output/part-r-00000
```

## Hadoop in cluster mode

Assuming configuration from /hadoop directory applied.

Go to http://SERVER:9870/explorer.html#/user/hdfs

Create ```input``` directory. Upload files to the directory.

Run the application:

```
./../gradlew :source-code-crawler-step6:run -PprogramArgs=hdfs://SERVER:9000/user/hdfs/input,hdfs://SERVER:9000/user/hdfs/output
```

Go to ```output``` directory and check the results.
