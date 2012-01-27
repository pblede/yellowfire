@REM "Delete the connection pools"

CALL %GLASSFISH_HOME%\bin\asadmin --port 4949 delete-jdbc-connection-pool --cascade=true pool.yellowfire.online

@REM CALL %GLASSFISH_HOME%\bin\asadmin --port 4949 delete-jdbc-connection-pool --cascade=true pool.yellowfire.security
@REM CALL %GLASSFISH_HOME%\bin\asadmin --port 4949 delete-jdbc-connection-pool --cascade=true pool.training.online
@REM CALL %GLASSFISH_HOME%\bin\asadmin --port 4949 delete-jdbc-connection-pool --cascade=true pool.training.archive

@REM "Create the connection pools"

CALL %GLASSFISH_HOME%\bin\asadmin --port 4949 create-jdbc-connection-pool --datasourceclassname com.microsoft.sqlserver.jdbc.SQLServerDataSource --restype javax.sql.DataSource --steadypoolsize 1 --statementcachesize 50 --ping true --description "" --property databaseName=race:serverName=localhost\\SQLSERVER2008R2:user=race:password=race pool.yellowfire.online

@REM CALL %GLASSFISH_HOME%\bin\asadmin --port 4949 create-jdbc-connection-pool --datasourceclassname com.microsoft.sqlserver.jdbc.SQLServerDataSource --restype javax.sql.DataSource --steadypoolsize 1 --statementcachesize 50 --ping true --description "" --property databaseName=race:serverName=localhost\\SQLSERVER2008R2:user=race:password=race pool.yellowfire.security
@REM CALL %GLASSFISH_HOME%\bin\asadmin --port 4949 create-jdbc-connection-pool --datasourceclassname com.microsoft.sqlserver.jdbc.SQLServerDataSource --restype javax.sql.DataSource --steadypoolsize 1 --statementcachesize 50 --ping true --description "" --property databaseName=race:serverName=localhost\\SQLSERVER2008R2:user=training:password=training pool.training.online
@REM CALL %GLASSFISH_HOME%\bin\asadmin --port 4949 create-jdbc-connection-pool --datasourceclassname com.microsoft.sqlserver.jdbc.SQLServerDataSource --restype javax.sql.DataSource --steadypoolsize 1 --statementcachesize 50 --ping true --description "" --property databaseName=race:serverName=localhost\\SQLSERVER2008R2:user=archive:password=archive pool.training.archive

@REM "Create the jdbc resources"
CALL %GLASSFISH_HOME%\bin\asadmin --port 4949 create-jdbc-resource --connectionpoolid pool.yellowfire.online --enabled true yellowfire/ds

@REM CALL %GLASSFISH_HOME%\bin\asadmin --port 4949 create-jdbc-resource --connectionpoolid pool.yellowfire.security --enabled true yellowfire.security.ds
@REM CALL %GLASSFISH_HOME%\bin\asadmin --port 4949 create-jdbc-resource --connectionpoolid pool.training.online --enabled true yellowfire.training.online.ds
@REM CALL %GLASSFISH_HOME%\bin\asadmin --port 4949 create-jdbc-resource --connectionpoolid pool.training.archive --enabled true yellowfire.training.archive.ds
