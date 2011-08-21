@REM "Deploying Yellowfire"
CALL %GLASSFISH_HOME%\bin\asadmin --port 4949 create-system-properties yellowfire.solr.url=http\://localhost\:9081/solr
CALL %GLASSFISH_HOME%\bin\asadmin --port 4949 deploy --force true --contextroot yellowfire --upload true ..\yellowfire-ear\target\yellowfire-0.0.1.ear