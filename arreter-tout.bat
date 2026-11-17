@echo off
chcp 65001 >nul
echo Arret des microservices sur les ports 8080 a 8087...
for %%P in (8080 8081 8082 8083 8084 8085 8086 8087) do (
    for /f "tokens=5" %%A in ('netstat -ano ^| findstr /R ":%%P .*LISTENING"') do (
        echo Arret du processus PID %%A sur le port %%P...
        taskkill /PID %%A /F >nul 2>&1
    )
)
echo Tous les services ont ete arretes.
pause
