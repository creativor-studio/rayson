@echo off
setlocal EnableDelayedExpansion
set CURRENT_DIR=%cd%
cd ..
set PATENT_PATH=%cd%
@rem maven target path
set TARGET_PATH=%PATENT_PATH%\target
set CLASS_PATH=%TARGET_PATH%\classes\

for /R %TARGET_PATH%\dependency\ %%i in (*.jar) do @set CLASS_PATH=!CLASS_PATH!;%%i


@rem load jprofiler agent
@rem set PROFILER_OPTION="-agentpath:C:\Program Files\jprofiler10\bin\windows-x64\jprofilerti.dll"

java %PROFILER_OPTION% -cp %CLASS_PATH%  org.rayson.tools.profiler.Console %*
cd %CURRENT_DIR%
