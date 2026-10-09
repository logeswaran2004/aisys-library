@echo off
echo ==========================================
echo AISYS Library - Offline Update Utility
echo ==========================================
echo Backing up current version...
copy ..\..\backend\target\aisys-library-backend-1.0.0-SNAPSHOT.jar ..\rollback\aisys-library-backend-PREVIOUS.jar
echo.
echo Applying offline update...
rem In a real scenario, this would copy the new patch file. For the prototype, we simulate the swap.
copy update-package.jar ..\..\backend\target\aisys-library-backend-1.0.0-SNAPSHOT.jar
echo.
echo Update applied successfully! Please restart the application service.
pause