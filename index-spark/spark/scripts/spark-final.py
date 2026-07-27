from pyspark.sql import SparkSession
from pyspark.sql.functions import window,from_json, explode, col, cast, concat, struct,lit, to_json, substring, length, substring_index, ltrim, size, split, collect_list, collect_set
from pyspark.sql.types import  StructType, StructField, StringType, LongType, DoubleType, IntegerType, ArrayType
from datetime import datetime
import time

def dfConsoleShow(df):
    query = df.writeStream.format("console").start()
    time.sleep(1) # sleep 10 seconds
    query.stop()

def reprint(message):
    print("###################################")
    print(message)
    print("###################################")


def myMicroBatchFunc(df,batch_id):
    print("df.collect()")
    print(df.collect())

    group_df=df.withWatermark("timestamp", "2 seconds")\
               .groupBy(window("timestamp", "2 seconds"),"key","moment")\
               .pivot("indexName")\
               .agg(collect_set("last"))\
               .withColumn('ProcessedTime',lit(str(datetime.now())))
    reprint("group_df")    
    group_df.printSchema()
    #dfConsoleShow(group_df) cannot be executed in microbatch


    cNames=group_df.select(group_df.colRegex("`index.+`")).columns
    print(cNames)

    #def addLastStruc(df,cn):
    #    return df.withColumnRenamed(cn,"last").withColumn(cn,struct("last")).drop("last")


    for cn in cNames:
        group_df=group_df.withColumn(cn,explode(cn)).transform(lambda df,cn: df.withColumnRenamed(cn,"last").withColumn(cn,struct("last")).drop("last"),cn)

    reprint("group_df")
    group_df.printSchema()
    
    result_df=group_df.withColumn("value",struct(col("key").alias("key"),\
                                               col("moment").alias("moment"),
                                               raw_df.colRegex("`index.+`")))\
                      .withColumn("key",lit(datetime.now().strftime("%Y-%m-%d-%H:%M:%S"))) 
    reprint("result_df")
    result_df.printSchema()
    #dfConsoleShow(result_df) cannot be executed in microbatch

    # copie du flux dans un topic de sortie apres readaptation
    result_df.select(col("key").cast("string").alias("key"),to_json(col("value")).cast("string").alias("value"))\
        .write\
        .format("kafka") \
        .option("kafka.bootstrap.servers", "kafka-cluster:9092") \
        .option("topic", "output_index") \
        .mode('append')\
        .save()
    pass



print("Load App Config")
# Spark session & context
session = SparkSession \
        .builder \
        .appName("index_transformer") \
        .config("spark.sql.debug.maxToStringFields", "100") \
        .config("spark.sql.streaming.stateStore.stateSchemaCheck","false")\
        .config("spark.driver.extraClassPath", "/home/jovyan/.ivy2/jars/*.jar") \
        .getOrCreate()

print("Load Kafka Topic Config")
kafka_df = session.readStream \
    .format("kafka") \
    .option("kafka.bootstrap.servers", "kafka-cluster:9092") \
    .option("subscribe", "mongodb.index") \
    .option("startingOffsets", "earliest") \
    .option("failOnDataLoss","false") \
    .option("spark.streaming.kafka.maxRatePerPartition", "50") \
    .load()

# failOnDataLoss: pour pouvoir retraiter la fil et ne pas avoir de blocage sur l'index si il a ete changé

kafka_df.printSchema()    
print("define schema")
# build based on https://medium.com/@jaya.aiyappan/reading-json-message-from-kafka-topic-and-process-using-spark-structured-streaming-and-write-it-a7fee670c159


schema = StructType([
    StructField("_id", StringType()),
    StructField("key", StringType()),
    StructField("moment", StringType()),
    StructField("val", StructType([
        StructField("first", StructType([
            StructField("value", DoubleType()),
            StructField("timestamp", DoubleType())
        ])),
        StructField("last", StructType([
            StructField("value", DoubleType()),
            StructField("timestamp", DoubleType())
        ]))
    ]))
])


print("Exec request")

#dfConsoleShow(kafka_df)

cast_df=kafka_df.select("timestamp",kafka_df.value.cast(StringType()))
#                .withColumn("newValue",substring('value',7,10000))\
#                .drop("value")


cast_df.printSchema()
#dfConsoleShow(cast_df)

#schema_df=cast_df.select("timestamp",from_json(col("newValue").cast("string"),schema, {"mode" : "FAILFAST"}).alias("value"))
schema_df=cast_df.select("timestamp",from_json(col("value"),schema).alias("value"))
reprint("schema_df")      
schema_df.printSchema()
#dfConsoleShow(schema_df)


key_split_df=schema_df.withColumn("key", concat(split(col("value.key"),'[|]').getItem(0),lit("|"),split(col("value.key"),'[|]').getItem(3)))\
                      .withColumn("indexName", split(col("value.key"),'[|]').getItem(1))
reprint("key_split_df")      
key_split_df.printSchema()
#dfConsoleShow(key_split_df)


raw_df=key_split_df.selectExpr("timestamp",'key','indexName','value.moment','value.val.last')
reprint("raw_df")    
raw_df.printSchema()
#dfConsoleShow(raw_df)


raw_df.writeStream\
      .trigger(processingTime='100 seconds')\
      .option("checkpointLocation", "/tmp/checkpoint") \
      .foreachBatch(myMicroBatchFunc)\
      .start()\
      .awaitTermination()


############################################################################## end of script


#fullSchema = StructType([
#    StructField("_id", StructType([
#        StructField("_data", StringType())
#    ])),
#    StructField("operationType", StringType()),
#    StructField("clusterTime", StructType([
#        StructField("$timestamp", StructType([
#            StructField("t", IntegerType()),
#            StructField("i", IntegerType())
#        ])),
#    ])),
#    StructField("fullDocument", StructType([
#        StructField("_id", StructType([
#            StructField("$oid", StringType())
#        ])),
#        StructField("key", StringType()),
#        StructField("moment", StringType()),
#        StructField("val", StructType([
#            StructField("first", StructType([
#                StructField("value", DoubleType()),
#                StructField("timestamp", DoubleType())
#            ])),
#            StructField("last", StructType([
#                StructField("value", DoubleType()),
#                StructField("timestamp", DoubleType())
#            ]))
#        ]))
#    ])),
#    StructField("ns", StructType([
#        StructField("db", StringType()),
#        StructField("coll", StringType())
#    ])),
#    StructField("documentKey", StructType([
#        StructField("_id", StructType([
#            StructField("$oid", StringType())
#        ])),
#    ]))
#])


# cast necessaire car c'est du json et recopie en l'etat dans le topic
#kafka_df.selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)") \
#    .writeStream \
#    .format("kafka") \
#    .option("kafka.bootstrap.servers", "kafka-cluster:9092") \
#    .option("checkpointLocation", "/tmp/checkpoint") \
#    .option("topic", "output_index") \
#    .start()\
#    .awaitTermination()


# copie du flux dans un fichier local
#result_df.select(result_df.key,to_json("value").alias("value")).selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)") \
#        .writeStream \
#        .format("csv")\
#        .option("path","/tmp/kafka_data")\
#        .option("checkpointLocation", "/tmp/checkpoint")\
#        .outputMode("append")\
#        .queryName("Flattened Invoice Writter")\
#        .start()\
#        .awaitTermination()


#result_df.selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)") \
#               .writeStream \
#               .format("console") \
#               .start() \
#               .awaitTermination()