@echo off
REM Script pour exécuter 00_script.sql sur PostgreSQL

REM Définir l'utilisateur et le mot de passe
SET PGPASSWORD=postgres
SET PSQL=psql
SET USER=postgres
SET SQLFILE=src\bd\00_script.sql

REM Vérifie si le fichier SQL existe
IF NOT EXIST "%SQLFILE%" (
    echo Le fichier %SQLFILE% est introuvable !
    pause
    exit /b
)

REM Exécute le fichier SQL avec psql
%PSQL% -U %USER% -f "%SQLFILE%"

REM Fin
echo Script terminé.
pause
