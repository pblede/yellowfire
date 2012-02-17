CALL 01-create-domain.bat
CALL 02-copy-libraries.bat
CALL 03-start-domain.bat
CALL 04-create-jdbc-resource.bat
CALL 05-create-mail-resource.bat
CALL 07-create-jms-resource.bat
CALL 06-create-auth-realm.bat

CALL 10-stop-domain.bat
CALL 03-start-domain.bat

CALL 08-deploy-solr.bat
CALL 09-deploy-yellowfire.bat

CALL 10-stop-domain.bat
CALL 03-start-domain.bat