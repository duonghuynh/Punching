@echo off
if not "%JAVA_HOME%" == "" goto gotJdkHome
if not "%JRE_HOME%" == "" goto gotJreHome

:gotJdkHome
echo java: "%JAVA_HOME%"
set "JAVA_FOLDER=%JAVA_HOME%"
goto copyComm

:gotJreHome
echo java: "%JRE_HOME%"
set "JAVA_FOLDER=%JRE_HOME%"
goto copyComm

:copyComm
echo JAVA_FOLDER = "%JAVA_FOLDER%"
echo Copy from ForJRE to %JAVA_FOLDER%
xcopy ForJRE\bin "%JAVA_FOLDER%\bin" /Y
xcopy ForJRE\lib "%JAVA_FOLDER%\lib" /Y
xcopy ForJRE\lib\ext "%JAVA_FOLDER%\lib\ext" /Y

pause