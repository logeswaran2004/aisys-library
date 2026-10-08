@echo off
setlocal
REM AISYS RFID Library offline installer for isolated Windows LAN environments.
set SCRIPT_DIR=%~dp0
set BACKEND_ROOT=%SCRIPT_DIR%..\..
set CONF=%SCRIPT_DIR%offline-db.conf
echo Using configuration file: %CONF%
if not exist "%CONF%" (
  echo Missing offline-db.conf
  exit /b 1
)
echo Building backend with Maven for offline deployment...
call mvn -f "%BACKEND_ROOT%\pom.xml" -DskipTests package
if errorlevel 1 (
  echo Maven build failed.
  exit /b 1
)
echo Starting AISYS Library from the packaged jar...
java -jar "%BACKEND_ROOT%\target\aisys-library-backend-1.0.0-SNAPSHOT.jar"
endlocal
