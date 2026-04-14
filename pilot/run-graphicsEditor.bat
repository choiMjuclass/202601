@echo off
setlocal

set "SCRIPT_DIR=%~dp0"
pushd "%SCRIPT_DIR%" >nul

if exist "target\classes\org\graphicsEditor\GMain.class" (
    java -cp "target\classes" org.graphicsEditor.GMain
) else (
    mvn -q compile exec:java
)

set "EXIT_CODE=%ERRORLEVEL%"
popd >nul
exit /b %EXIT_CODE%
