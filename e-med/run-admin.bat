@echo off
echo Compiling e-MEDpharma Admin Dashboard...

cd /d "c:\Users\nares\OneDrive\Desktop\e-med-pharma\e-med"

echo Compiling Java files...
javac -cp ".;mysql-connector-j-9.4.0.jar" src/com/emedpharma/admin/*.java src/com/emedpharma/dao/*.java src/com/emedpharma/common/*.java

if %ERRORLEVEL% EQU 0 (
    echo Compilation successful!
    echo Starting Admin Dashboard...
    java -cp ".;mysql-connector-j-9.4.0.jar;src" com.emedpharma.common.MainApplication
) else (
    echo Compilation failed!
    pause
)