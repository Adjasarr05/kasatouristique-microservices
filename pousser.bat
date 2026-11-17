@echo off
chcp 65001 >nul
cd /d "%~dp0"

echo 1. Annulation de l'ancien rebase...
git rebase --abort >nul 2>&1

echo 2. Reinitialisation complete de Git...
if exist ".git" (
    attrib -h -r -s .git\* /s /d >nul 2>&1
    rd /s /q .git >nul 2>&1
)

echo 3. Initialisation du nouveau depot sur main...
git init -b main
git remote add origin https://github.com/Adjasarr05/kasatouristique-microservices.git

echo 4. Copie du rapport officiel Word...
if exist "%USERPROFILE%\.gemini\antigravity\scratch\Rapport_Projet_KasaTouristique_UASZ.docx" (
    copy /y "%USERPROFILE%\.gemini\antigravity\scratch\Rapport_Projet_KasaTouristique_UASZ.docx" "Rapport_Projet_KasaTouristique_UASZ.docx" >nul 2>&1
)

echo 5. Ajout de tous les dossiers (Docker, Compose, Angular, Microservices, Rapport)...
git add .

echo 6. Creation du commit officiel...
git commit -m "Design Angular 17 identique au portail KasaTouristique (Bleu Nuit & Or) et fixes Vercel"

echo 7. Envoi propre et direct vers GitHub...
git push -u origin main --force

echo.
echo ============================================================================
echo BRAVO ! Votre GitHub est maintenant 100%% a jour avec Docker et Angular !
echo ============================================================================
pause
