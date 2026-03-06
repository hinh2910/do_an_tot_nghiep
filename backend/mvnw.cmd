@REM ----------------------------------------------------------------------------
@REM Maven Wrapper script for Windows
@REM ----------------------------------------------------------------------------
@echo off
setlocal EnableDelayedExpansion

set "WRAPPER_DIR=%~dp0"
set "WRAPPER_PROPERTIES=%WRAPPER_DIR%.mvn\wrapper\maven-wrapper.properties"
set "WRAPPER_JAR=%WRAPPER_DIR%.mvn\wrapper\maven-wrapper.jar"

@REM --- Read properties ---
set "DISTRIBUTION_URL="
set "WRAPPER_URL="
for /f "usebackq tokens=1,* delims==" %%a in ("%WRAPPER_PROPERTIES%") do (
    if "%%a"=="distributionUrl" set "DISTRIBUTION_URL=%%b"
    if "%%a"=="wrapperUrl" set "WRAPPER_URL=%%b"
)

@REM --- Download maven-wrapper.jar if not present ---
if not exist "%WRAPPER_JAR%" (
    echo Downloading Maven Wrapper JAR from !WRAPPER_URL! ...
    powershell -Command "[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri '!WRAPPER_URL!' -OutFile '!WRAPPER_JAR!' -UseBasicParsing"
    if errorlevel 1 (
        echo ERROR: Could not download maven-wrapper.jar
        exit /b 1
    )
)

@REM --- Extract Maven dist name from URL ---
for %%i in ("%DISTRIBUTION_URL%") do set "MAVEN_ZIP_NAME=%%~ni"
set "MAVEN_DIST_NAME=!MAVEN_ZIP_NAME:-bin=!"
set "MAVEN_HOME_DIR=%USERPROFILE%\.m2\wrapper\dists\!MAVEN_DIST_NAME!"
set "MAVEN_ZIP=%TEMP%\!MAVEN_ZIP_NAME!.zip"

@REM --- Download and extract Maven if not present ---
if not exist "!MAVEN_HOME_DIR!\bin\mvn.cmd" (
    echo Maven not found at !MAVEN_HOME_DIR!, downloading...
    echo Downloading from !DISTRIBUTION_URL! ...
    echo This may take a minute...

    powershell -Command "[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; $ProgressPreference = 'SilentlyContinue'; Invoke-WebRequest -Uri '!DISTRIBUTION_URL!' -OutFile '!MAVEN_ZIP!' -UseBasicParsing"
    if errorlevel 1 (
        echo ERROR: Could not download Maven distribution
        echo Try downloading manually from: !DISTRIBUTION_URL!
        exit /b 1
    )

    if not exist "%USERPROFILE%\.m2\wrapper\dists" mkdir "%USERPROFILE%\.m2\wrapper\dists"

    echo Extracting Maven...
    powershell -Command "Expand-Archive -Path '!MAVEN_ZIP!' -DestinationPath '%USERPROFILE%\.m2\wrapper\dists' -Force"
    if errorlevel 1 (
        echo ERROR: Could not extract Maven distribution
        exit /b 1
    )

    del "!MAVEN_ZIP!" 2>nul
    echo Maven installed successfully.
)

@REM --- Execute Maven ---
"!MAVEN_HOME_DIR!\bin\mvn.cmd" %*

endlocal
