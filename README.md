# Recommendation System. 

## Requirement 

* java 8 
* scala 2.11.8
* SBT 2.8.1

## Steps 

* Initializing Spark Session
* Importing Data
* Model parameter 
* Applying model to data
* Saving Clustered Data on HDFS


## Configuration
The program needs somme properties to be executed. These properties are saved in config/config.properties. 

`````
APP_NAME=  Spark Job NAME
PATH_ratingDF_HDFS= Path to csv file on HDFS 
PATH_movieID_HDFS=Path to csv file on HDFS 
rank= ALS Algorithm parameter
numIterations= ALS Algorithm parameter
lambda= ALS Algorithm parameter
alpha= ALS Algorithm parameter
block= ALS Algorithm parameter
seed= ALS Algorithm parameter
implicitPrefs= ALS Algorithm parameter
PATH_OUTPUT_HDFS= Path to stored data on HDFS
`````



## Runing on HADOOP cluster 
To build the project, run : 
````
> sbt package
> sbt assembly 
````
To build jar with dependencies, Run Assembly 

Then you can submit the job using spark-submit in the shell file:

````
> cd SR/lib/shell
> chmod +x spark-submit.sh
> ./spark-submit.sh
````
