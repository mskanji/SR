#!/bin/sh
spark-submit --class Netflix.SR.Run  --master yarn-cluster --files /home/moez/SR/Config/config.properties /home/moez/SR/lib/jars/sr_2.11-0.1.jar



