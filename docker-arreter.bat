@echo off
chcp 65001 >nul
echo ============================================================================
echo Arret des conteneurs Docker KasaTouristique
echo ============================================================================
cd /d "%~dp0"

where docker-compose >nul 2>nul
if %errorlevel% equ 0 (
    docker-compose down
) else (
    docker compose down
)

echo.
echo Tous les conteneurs sont arretes.
pause
