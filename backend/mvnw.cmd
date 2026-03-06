@REM ----------------------------------------------------------------------------
@REM Licensed to the Apache Software Foundation (ASF) under one
@REM or more contributor license agreements.  See the NOTICE file
@REM distributed with this work for additional information
@REM regarding copyright ownership.  The ASF licenses this file
@REM to you under the Apache License, Version 2.0 (the
@REM "License"); you may not use this file except in compliance
@REM with the License.  You may obtain a copy of the License at
@REM
@REM    http://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing,
@REM software distributed under the License is distributed on an
@REM "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
@REM KIND, either express or implied.  See the License for the
@REM specific language governing permissions and limitations
@REM under the License.
@REM ----------------------------------------------------------------------------

@REM Begin all REM://
@echo off
setlocal

set WRAPPER_DIR=%~dp0
set MAVEN_PROJECTBASEDIR=%WRAPPER_DIR%

set WRAPPER_PROPERTIES=%WRAPPER_DIR%.mvn\wrapper\maven-wrapper.properties
set WRAPPER_JAR=%WRAPPER_DIR%.mvn\wrapper\maven-wrapper.jar

@REM --- Read properties ---
set DISTRIBUTION_URL=
set WRAPPER_URL=
for /f "usebackq tokens=1,* delims==" %%a in ("%WRAPPER_PROPERTIES%") do (
    if "%%a"=="distributionUrl" set "DISTRIBUTION_URL=%%b"
    if "%%a"=="wrapperUrl" set "WRAPPER_URL=%%b"
)

@REM --- Download maven-wrapper.jar if not present ---
if not exist "%WRAPPER_JAR%" (
    echo Downloading Maven Wrapper JAR from %WRAPPER_URL% ...
    powershell -Command "[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; (New-Object System.Net.WebClient).DownloadFile('%WRAPPER_URL%', '%WRAPPER_JAR%')"
    if errorlevel 1 (
        echo ERROR: Could not download maven-wrapper.jar
        exit /b 1
    )
)

@REM --- Extract Maven version name from URL ---
@REM e.g. https://.../apache-maven-3.9.9-bin.zip -> apache-maven-3.9.9
for %%i in ("%DISTRIBUTION_URL%") do set "MAVEN_ZIP_NAME=%%~ni"
@REM Remove "-bin" suffix if present
set "MAVEN_DIST_NAME=%MAVEN_ZIP_NAME:-bin=%"

set "MAVEN_HOME_DIR=%USERPROFILE%\.m2\wrapper\dists\%MAVEN_DIST_NAME%"

@REM --- Download and extract Maven if not present ---
if not exist "%MAVEN_HOME_DIR%\bin\mvn.cmd" (
    echo Maven not found at %MAVEN_HOME_DIR%, downloading from %DISTRIBUTION_URL% ...

    set "MAVEN_ZIP=%TEMP%\%MAVEN_ZIP_NAME%.zip"

    powershell -Command "[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; (New-Object System.Net.WebClient).DownloadFile('%DISTRIBUTION_URL%', '%MAVEN_ZIP%')"
    if errorlevel 1 (
        echo ERROR: Could not download Maven distribution
        exit /b 1
    )

    if not exist "%USERPROFILE%\.m2\wrapper\dists" mkdir "%USERPROFILE%\.m2\wrapper\dists"

    echo Extracting Maven...
    powershell -Command "Expand-Archive -Path '%MAVEN_ZIP%' -DestinationPath '%USERPROFILE%\.m2\wrapper\dists' -Force"
    if errorlevel 1 (
        echo ERROR: Could not extract Maven distribution
        exit /b 1
    )

    del "%MAVEN_ZIP%" 2>nul
)

@REM --- Set JAVA_HOME if not set ---
if "%JAVA_HOME%"=="" (
    echo WARNING: JAVA_HOME is not set.
)

@REM --- Execute Maven ---
"%MAVEN_HOME_DIR%\bin\mvn.cmd" %*

endlocal
