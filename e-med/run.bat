@echo off
echo Starting e-MEDpharma Application...
echo.

echo Compiling Java files...
javac -cp ".;mysql-connector-j-9.4.0\mysql-connector-j-9.4.0.jar" -d classes src\com\emedpharma\common\*.java src\com\emedpharma\customer\*.java src\com\emedpharma\vendor\*.java src\com\emedpharma\model\*.java src\com\emedpharma\dao\*.java src\com\emedpharma\service\*.java

if %errorlevel% neq 0 (
    echo Compilation failed!
    pause
    exit /b 1
)

echo Compilation successful!
echo Starting application...
echo.

java -cp ".;mysql-connector-j-9.4.0\mysql-connector-j-9.4.0.jar;classes" com.emedpharma.common.MainApplication

echo.
echo Application closed.
pause