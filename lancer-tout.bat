@echo off
chcp 65001 >nul
set "ROOT=%~dp0"
set "MVN=C:\Program Files\JetBrains\IntelliJ IDEA 2026.2\plugins\maven-plugin\lib\maven3\bin\mvn.cmd"

if not exist "%MVN%" (
    set "MVN=mvn"
)

echo ============================================================================
echo Demarrage de l'architecture Microservices - Patrimoine Ziguinchor (TP1)
echo ============================================================================

start "ms-patrimoine (8081)" cmd /s /k "cd /d "%ROOT%ms-patrimoine" && "%MVN%" spring-boot:run"
timeout /t 2 /nobreak >nul
start "ms-circuit (8082)" cmd /s /k "cd /d "%ROOT%ms-circuit" && "%MVN%" spring-boot:run"
timeout /t 2 /nobreak >nul
start "ms-evenement (8083)" cmd /s /k "cd /d "%ROOT%ms-evenement" && "%MVN%" spring-boot:run"
timeout /t 2 /nobreak >nul
start "ms-utilisateur (8085)" cmd /s /k "cd /d "%ROOT%ms-utilisateur" && "%MVN%" spring-boot:run"
timeout /t 2 /nobreak >nul
start "ms-paiement (8086)" cmd /s /k "cd /d "%ROOT%ms-paiement" && "%MVN%" spring-boot:run"
timeout /t 2 /nobreak >nul
start "ms-notification (8087)" cmd /s /k "cd /d "%ROOT%ms-notification" && "%MVN%" spring-boot:run"
timeout /t 2 /nobreak >nul
start "ms-reservation (8084)" cmd /s /k "cd /d "%ROOT%ms-reservation" && "%MVN%" spring-boot:run"
timeout /t 2 /nobreak >nul
start "patrimoine-ziguinchor WEB (8080)" cmd /s /k "cd /d "%ROOT%patrimoine-ziguinchor" && "%MVN%" spring-boot:run"

echo.
echo Les 8 fenetres ont ete lancees ! Patientez 20 a 30 secondes puis ouvrez :
echo   - Application Web : http://localhost:8080
echo   - Tableau des Microservices : http://localhost:8080/microservices
echo.
pause
