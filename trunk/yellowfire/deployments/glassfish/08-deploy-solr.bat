@REM "Deploying Solr"
CALL %GLASSFISH_HOME%\bin\asadmin --port 4949 create-system-properties solr.data.dir=C\:\\SDE\\servers\\solr\\data:solr.solr.home=C\:\\SDE\\servers\\solr
CALL %GLASSFISH_HOME%\bin\asadmin --port 4949 deploy --force true --contextroot solr --upload true .\\applications\\apache-solr-3.4.0.war