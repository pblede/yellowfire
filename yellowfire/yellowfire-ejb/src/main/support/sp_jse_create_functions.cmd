sqlcmd -U race -P race -S %1 -d %2 -i "02.[struct] setup.sql"
sqlcmd -U race -P race -S %1 -d %2 -i "02.[view] setup.sql"
sqlcmd -U race -P race -S %1 -d %2 -i "03.[data] sample.sql"
sqlcmd -U race -P race -S %1 -d %2 -i "04.[data] sample.sql"
sqlcmd -U race -P race -S %1 -d %2 -i "05.[data] sample.sql"
sqlcmd -U race -P race -S %1 -d %2 -i "10.[data] sample.sql"
