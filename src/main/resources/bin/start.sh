ENV=$1
if [ $ENV == '' ]
then 
$ENV = "dev";
fi
java -Xms256m -Xmx512m -server -Xloggc:../logs/gc.log -verbose:gc -XX:+PrintGCDetails -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=../logs -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=9098 -Dcom.sun.management.jmxremote.ssl=FALSE -Dcom.sun.management.jmxremote.authenticate=FALSE -Dlogging.config=../config/log4j2.xml -Dspring.config.location=../config/application.yml -jar ../lib/alfresco-utilities-1.0.0.jar  com.alfresco.utilities.app.PlusyoursoftechUtilitiesApplication
