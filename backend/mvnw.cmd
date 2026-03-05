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

@REM Set the current directory to the location of this script
set WRAPPER_DIR=%~dp0

@REM Determine the project base directory
set MAVEN_PROJECTBASEDIR=%WRAPPER_DIR%

set WRAPPER_JAR="%WRAPPER_DIR%.mvn\wrapper\maven-wrapper.jar"
set WRAPPER_PROPERTIES="%WRAPPER_DIR%.mvn\wrapper\maven-wrapper.properties"

@REM Download maven-wrapper.jar if not present
if not exist %WRAPPER_JAR% (
    echo Downloading Maven Wrapper...
    
    @REM Read distributionUrl from maven-wrapper.properties
    for /f "usebackq tokens=1,2 delims==" %%a in (%WRAPPER_PROPERTIES%) do (
        if "%%a"=="wrapperUrl" set WRAPPER_URL=%%b
    )
    
    powershell -Command "(New-Object System.Net.WebClient).DownloadFile('%WRAPPER_URL%', '%WRAPPER_JAR:'=%')"
)

@REM Determine Maven home from wrapper properties
set MAVEN_HOME=
for /f "usebackq tokens=1,2 delims==" %%a in (%WRAPPER_PROPERTIES%) do (
    if "%%a"=="distributionUrl" set DISTRIBUTION_URL=%%b
)

@REM Extract Maven version from URL for directory naming
for %%i in (%DISTRIBUTION_URL%) do set MAVEN_DIST_NAME=%%~ni

set MAVEN_HOME_DIR=%USERPROFILE%\.m2\wrapper\dists\%MAVEN_DIST_NAME%

if not exist "%MAVEN_HOME_DIR%\bin\mvn.cmd" (
    echo Maven not found, downloading...
    
    set MAVEN_ZIP=%TEMP%\%MAVEN_DIST_NAME%.zip
    
    powershell -Command "(New-Object System.Net.WebClient).DownloadFile('%DISTRIBUTION_URL%', '%MAVEN_ZIP%')"
    
    if not exist "%USERPROFILE%\.m2\wrapper\dists" mkdir "%USERPROFILE%\.m2\wrapper\dists"
    
    powershell -Command "Expand-Archive -Path '%MAVEN_ZIP%' -DestinationPath '%USERPROFILE%\.m2\wrapper\dists' -Force"
    
    del "%MAVEN_ZIP%" 2>nul
)

set MAVEN_CMD="%MAVEN_HOME_DIR%\bin\mvn.cmd"

@REM Execute Maven
%MAVEN_CMD% %*
