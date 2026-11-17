const fs = require('fs');
const path = require('path');

const src = path.join(__dirname, 'dist', 'kasatouristique-frontend', 'browser');
const dst = path.join(__dirname, 'dist', 'kasatouristique-frontend');

if (fs.existsSync(src)) {
  try {
    fs.cpSync(src, dst, { recursive: true });
    console.log('✅ Synchronisation de dist/browser vers dist réussie !');
  } catch (err) {
    console.error('Erreur synchronisation dist:', err);
  }
}
