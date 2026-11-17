@echo off
chcp 65001 >nul
echo ============================================================================
echo Synchronisation et Push vers GitHub : Adjasarr05/kasatouristique-microservices
echo ============================================================================
cd /d "%~dp0"

if not exist ".git" (
    echo Initialisation du depot Git local...
    git init -b main
    git remote add origin https://github.com/Adjasarr05/kasatouristique-microservices.git
)

echo Verification de la branche main...
git branch -M main

echo Ajout de tous les fichiers (Dockerfiles, Compose, Angular, CORS, etc.)...
git add .

echo Creation du commit...
git commit -m "Dockerisation complete des microservices, ajout Frontend Angular et configuration CORS"

echo.
echo Envoi vers GitHub...
git push -u origin main

if %errorlevel% neq 0 (
    echo.
    echo Tentative d'alignement avec le depot distant...
    git pull origin main --rebase --allow-unrelated-histories
    git push -u origin main
)

echo.
echo ============================================================================
echo Termine ! Actualisez votre page GitHub dans votre navigateur.
echo ============================================================================
pause
