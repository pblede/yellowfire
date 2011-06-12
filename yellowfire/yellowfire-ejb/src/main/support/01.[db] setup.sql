create database race;
go
USE [master]
GO
CREATE LOGIN [race] WITH PASSWORD=N'race', DEFAULT_DATABASE=[race], CHECK_EXPIRATION=OFF, CHECK_POLICY=OFF
GO
USE [race]
GO
CREATE USER [race] FOR LOGIN [race]
GO
USE [race]
GO
ALTER USER [race] WITH DEFAULT_SCHEMA=[rce]
GO
USE [race]
GO
CREATE SCHEMA [rce] AUTHORIZATION [race]
GO
USE [race]
GO
EXEC sp_addrolemember N'db_owner', N'race'
GO