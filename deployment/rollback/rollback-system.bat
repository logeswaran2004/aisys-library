@echo off
echo ==========================================
echo AISYS Library - Emergency Rollback Utility
echo ==========================================
echo Verifying previous version backup...
if exist aisys-library-backend-PREVIOUS.jar (
    echo Restoring previous version...
    copy aisys-library-backend-PREVIOUS.jar ..\..\backend\target\aisys-library-backend-1.0.0-SNAPSHOT.jar
    echo Rollback completed successfully.
) else (
    echo ERROR: No previous version found to roll back to.
)
pause