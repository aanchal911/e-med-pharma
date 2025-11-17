@echo off
echo Starting e-MEDpharma Admin Dashboard with Database Connection...

cd /d "c:\Users\nares\OneDrive\Desktop\e-med-pharma\e-med"

echo Compiling Java files...
javac -cp ".;mysql-connector-j-9.4.0\mysql-connector-j-9.4.0.jar" src/com/emedpharma/admin/AdminDashboard.java

if %ERRORLEVEL% EQU 0 (
    echo Compilation successful!
    echo Starting Admin Dashboard with Database...
    java -cp ".;mysql-connector-j-9.4.0\mysql-connector-j-9.4.0.jar" TestAdmin
) else (
    echo Compilation failed!
    pause
)