@REM "Deploying Solr"
CALL %GLASSFISH_HOME%\bin\asadmin --port 4949 create-system-properties solr.data.dir=C\:\\SDE\\solr\\data:solr.solr.home=C\:\\SDE\\solr
CALL %GLASSFISH_HOME%\bin\asadmin --port 4949 deploy --force true --contextroot solr --upload true apache-solr-3.2.0.war