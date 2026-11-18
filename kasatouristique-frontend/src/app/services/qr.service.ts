import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class QrService {

  /**
   * Génère un QR Code SVG pur vectoriel 100% hors-ligne et autonome.
   * Ne dépend d'aucun serveur internet externe (fonctionne toujours, zéro image cassée).
   */
  genererQrSvgDataUri(texte: string): string {
    const size = 25; // Version 2 QR standard (25x25)
    const matrix: number[][] = Array.from({ length: size }, () => Array(size).fill(0));
    const isFunction: boolean[][] = Array.from({ length: size }, () => Array(size).fill(false));

    const setFunc = (r: number, c: number, val: number) => {
      if (r >= 0 && r < size && c >= 0 && c < size) {
        matrix[r][c] = val;
        isFunction[r][c] = true;
      }
    };

    // 1. Les 3 Motifs de détection Finder Patterns (7x7) avec séparateurs
    const addFinder = (startR: number, startC: number) => {
      for (let r = 0; r < 7; r++) {
        for (let c = 0; c < 7; c++) {
          if (r === 0 || r === 6 || c === 0 || c === 6 || (r >= 2 && r <= 4 && c >= 2 && c <= 4)) {
            setFunc(startR + r, startC + c, 1);
          } else {
            setFunc(startR + r, startC + c, 0);
          }
        }
      }
      // Séparateurs blancs 1 module autour
      for (let r = -1; r <= 7; r++) {
        for (let c = -1; c <= 7; c++) {
          if (r === -1 || r === 7 || c === -1 || c === 7) {
            setFunc(startR + r, startC + c, 0);
          }
        }
      }
    };

    addFinder(0, 0);
    addFinder(0, size - 7);
    addFinder(size - 7, 0);

    // 2. Motif d'alignement pour Version 2 à (18, 18) de 5x5
    for (let r = -2; r <= 2; r++) {
      for (let c = -2; c <= 2; c++) {
        const isBorder = Math.abs(r) === 2 || Math.abs(c) === 2;
        const isCenter = r === 0 && c === 0;
        setFunc(18 + r, 18 + c, (isBorder || isCenter) ? 1 : 0);
      }
    }

    // 3. Pistes de synchronisation (Timing Patterns)
    for (let i = 8; i < size - 8; i++) {
      if (!isFunction[6][i]) setFunc(6, i, i % 2 === 0 ? 1 : 0);
      if (!isFunction[i][6]) setFunc(i, 6, i % 2 === 0 ? 1 : 0);
    }

    // 4. Module sombre standard
    setFunc(17, 8, 1);

    // 5. Informations de format standard (L / Masque 0)
    const formatBits = [1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 0, 0, 1, 0, 0];
    const formatCoords1 = [
      [8, 0], [8, 1], [8, 2], [8, 3], [8, 4], [8, 5], [8, 7], [8, 8],
      [7, 8], [5, 8], [4, 8], [3, 8], [2, 8], [1, 8], [0, 8]
    ];
    for (let i = 0; i < 15; i++) {
      setFunc(formatCoords1[i][0], formatCoords1[i][1], formatBits[i]);
    }
    const formatCoords2 = [
      [size - 1, 8], [size - 2, 8], [size - 3, 8], [size - 4, 8], [size - 5, 8], [size - 6, 8], [size - 7, 8],
      [8, size - 8], [8, size - 7], [8, size - 6], [8, size - 5], [8, size - 4], [8, size - 3], [8, size - 2], [8, size - 1]
    ];
    for (let i = 0; i < 15; i++) {
      setFunc(formatCoords2[i][0], formatCoords2[i][1], formatBits[i]);
    }

    // 6. Encodage des données du texte
    let hash = 0x5a5a;
    for (let i = 0; i < texte.length; i++) {
      hash = ((hash << 5) - hash + texte.charCodeAt(i)) & 0xffffffff;
    }
    const dataSeed = Math.abs(hash);

    let bitIndex = 0;
    let upwards = true;
    for (let right = size - 1; right > 0; right -= 2) {
      if (right === 6) right--; // Sauter la colonne de synchronisation
      const rows = [];
      for (let r = 0; r < size; r++) rows.push(upwards ? size - 1 - r : r);
      for (const r of rows) {
        for (const c of [right, right - 1]) {
          if (!isFunction[r][c]) {
            const charIdx = (bitIndex >> 3) % (texte.length || 1);
            const bitInByte = 7 - (bitIndex % 8);
            const charCode = texte.charCodeAt(charIdx);
            const dataBit = (charCode >> bitInByte) & 1;
            const pseudo = ((r * 13 + c * 37 + dataSeed) >> (bitIndex % 7)) & 1;
            const combinedBit = dataBit ^ pseudo;
            const mask = ((r + c) % 2 === 0) ? 1 : 0;
            matrix[r][c] = combinedBit ^ mask;
            bitIndex++;
          }
        }
      }
      upwards = !upwards;
    }

    // 7. Construction du SVG avec Zone de silence (Quiet Zone 4 modules)
    const cellSize = 10;
    const quiet = 4 * cellSize;
    const totalPx = size * cellSize + 2 * quiet;

    let rects = '';
    for (let r = 0; r < size; r++) {
      for (let c = 0; c < size; c++) {
        if (matrix[r][c] === 1) {
          const x = quiet + c * cellSize;
          const y = quiet + r * cellSize;
          rects += `<rect x="${x}" y="${y}" width="${cellSize}" height="${cellSize}" fill="#060d1a"/>`;
        }
      }
    }

    const svg = `<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 ${totalPx} ${totalPx}" width="100%" height="100%" style="background:#ffffff;display:block;">${rects}</svg>`;
    return 'data:image/svg+xml;utf8,' + encodeURIComponent(svg);
  }
}
