@echo off
echo Testing Order Flow...

cd /d "%~dp0"
javac -cp "..\mysql-connector-j-9.4.0.jar" VerifyOrderFlow.java
java -cp ".;..\mysql-connector-j-9.4.0.jar" VerifyOrderFlow

pause