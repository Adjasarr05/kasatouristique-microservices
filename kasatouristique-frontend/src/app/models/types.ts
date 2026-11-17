export interface Categorie {
  id?: number;
  nom: string;
  description?: string;
  icone?: string;
}

export interface Avis {
  id?: number;
  auteur: string;
  note: number;
  commentaire: string;
  dateCreation?: string;
}

export interface Patrimoine {
  id?: number;
  nom: string;
  code?: string;
  description: string;
  historique?: string;
  adresse?: string;
  quartier?: string;
  capacite?: number;
  horairesOuverture?: string;
  imageUrl?: string;
  latitude?: number;
  longitude?: number;
  categorie?: Categorie;
  avis?: Avis[];
}

export interface Circuit {
  id?: number;
  nom: string;
  description: string;
  dureeHeures: number;
  distanceKm: number;
  difficulte: string;
  prix: number;
  actif: boolean;
  sitesIds?: number[];
}

export interface Evenement {
  id?: number;
  titre: string;
  description: string;
  dateDebut: string;
  dateFin?: string;
  lieu: string;
  capaciteMax: number;
  placesReservees: number;
  prix: number;
  statut: string;
  circuitId?: number;
}

export interface Reservation {
  id?: number;
  codeReservation?: string;
  utilisateurNom?: string;
  utilisateurEmail?: string;
  nombrePlaces: number;
  montantTotal?: number;
  statut?: string;
  qrCodeHash?: string;
  evenementId?: number;
  circuitId?: number;
}

export interface Utilisateur {
  id?: number;
  nom: string;
  email: string;
  motDePasse?: string;
  role?: string;
  actif?: boolean;
  telephone?: string;
  specialite?: string;
  languesParlees?: string;
}
