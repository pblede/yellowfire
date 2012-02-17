@REM "Create JMS resources"
CALL %GLASSFISH_HOME%\bin\asadmin --port 4949 create-connector-connection-pool --raname jmsra --connectiondefinition javax.jms.ConnectionFactory yellowfire.jms
CALL %GLASSFISH_HOME%\bin\asadmin --port 4949 create-connector-resource --poolname yellowfire.jms ConnectionFactory
CALL %GLASSFISH_HOME%\bin\asadmin --port 4949 create-jms-resource --restype javax.jms.Queue --property Name=yellowfire.queue.solr yellowfire/queue/solr
CALL %GLASSFISH_HOME%\bin\asadmin --port 4949 create-jms-resource --restype javax.jms.Queue --property Name=yellowfire.queue.notification yellowfire/queue/notification