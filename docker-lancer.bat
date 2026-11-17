@echo off
chcp 65001 >nul
echo ============================================================================
echo Demarrage des Microservices KasaTouristique avec Docker Compose
echo ============================================================================
cd /d "%~dp0"

where docker-compose >nul 2>nul
if %errorlevel% equ 0 (
    docker-compose up -d --build
) else (
    docker compose up -d --build
)

echo.
echo Statut des conteneurs :
where docker-compose >nul 2>nul
if %errorlevel% equ 0 (
    docker-compose ps
) else (
    docker compose ps
)

echo.
echo URLs disponibles :
echo   - Frontend Angular (SPA)   : http://localhost:4200
echo   - Portail Web Thymeleaf    : http://localhost:8080
echo   - Dashboard Microservices  : http://localhost:8080/microservices
echo   - ms-patrimoine            : http://localhost:8081/api/patrimoines
echo   - ms-circuit               : http://localhost:8082/api/circuits
echo   - ms-evenement             : http://localhost:8083/api/evenements
echo   - ms-reservation           : http://localhost:8084/api/reservations
echo   - ms-utilisateur           : http://localhost:8085/api/utilisateurs
echo   - ms-paiement              : http://localhost:8086/api/paiements
echo   - ms-notification          : http://localhost:8087/api/notifications
echo.
pause
