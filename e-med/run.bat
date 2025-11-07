@echo off
echo Starting e-MEDpharma Application...
echo.

cd src

echo Compiling Java files...
javac -cp ".;*" com/emedpharma/gui/*.java

if %errorlevel% neq 0 (
    echo Compilation failed!
    pause
    exit /b 1
)

echo Compilation successful!
echo.
echo Launching e-MEDpharma...
java com.emedpharma.gui.MainApplication

pause