@REM "Delete the mail resource"
CALL %GLASSFISH_HOME%\bin\asadmin --port 4949 delete-javamail-resource mail/yellowfire.mail

@REM "Create the mail resource"
CALL %GLASSFISH_HOME%\bin\asadmin --port 4949 create-javamail-resource --mailhost smtp.gmail.com --mailuser mp.ashworth@gmail.com --fromaddress mp.ashworth@gmail.com --property mail.smtp.starttls.enable=true:mail.smtp.password=delphi18*:mail.smtp.auth=true:mail.smtp.user=mp.ashworth@gmail.com --debug true --enabled true mail/yellowfire.mail


	