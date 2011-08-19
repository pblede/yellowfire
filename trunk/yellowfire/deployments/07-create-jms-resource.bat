@REM "Create JMS resources"
CALL %GLASSFISH_HOME%\bin\asadmin --port 4949 create-connector-connection-pool --raname jmsra --connectiondefinition javax.jms.ConnectionFactory --transactionsupport XATransaction yellowfire.jms.cf
CALL %GLASSFISH_HOME%\bin\asadmin --port 4949 create-connector-resource --poolname yellowfire.jms.cf yellowfire.jms.cf
CALL %GLASSFISH_HOME%\bin\asadmin --port 4949 create-jms-resource --restype javax.jms.Queue --property Name=yellowfire.jms.queue.solr yellowfire.jms.queue.solr
CALL %GLASSFISH_HOME%\bin\asadmin --port 4949 create-jms-resource --restype javax.jms.Queue --property Name=yellowfire.jms.queue.notification yellowfire.jms.queue.notification