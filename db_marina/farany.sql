-- 1. Règles pour profils
INSERT INTO Regle (nb_jour_duree_pret_max, nb_livre_preter_max, nb_prolengement_pret_max, nb_jour_prolongement_max) VALUES 
(7, 2, 3, 7),
(9, 3, 5, 9),
(12, 4, 7, 12);

-- 2. États des exemplaires
INSERT INTO Etat (nom) VALUES 
('disponible'),
('en pret');

-- 3. Types d’emprunt
INSERT INTO Type (nom) VALUES 
('Maison'),
('Sur place');

-- 4. Rôles
INSERT INTO Role (nom) VALUES 
('bibliothequaire'),
('adherent'),
('admin');

-- 5. Profils liés aux règles (id_regle)
INSERT INTO Profil (nom, id_regle) VALUES 
('etudiant', 1),
('professeur', 2),
('autre', 3);

-- 6. Utilisateurs
INSERT INTO Utilisateur (username, mdp, id_role) VALUES
('biblio', 'mdp', 1),
('ETU001', 'mdp', 2),
('ETU002', 'mdp', 2),
('ETU003', 'mdp', 2),
('ENS001', 'mdp', 2),
('ENS002', 'mdp', 2),
('ENS003', 'mdp', 2),
('PROF001', 'mdp', 2),
('PROF002', 'mdp', 2);

-- 7. Adhérents
INSERT INTO Adherent (nom, prenom, date_de_naissance, id_utilisateur, id_profil) VALUES
('Amine Bensaid', 'ETU001', '1990-05-15', 2, 1),
('Sarah El Khattabi', 'ETU002', '1990-05-15', 3, 1),
('Youssef Moujahid', 'ETU003', '1990-05-15', 4, 1),
('Nadia Benali', 'ENS001', '1990-05-15', 5, 2),
('Karim Haddadi', 'ENS002', '1990-05-15', 6, 2),
('Salima Touhami', 'ENS003', '1990-05-15', 7, 2),
('Rachid El Mansouri', 'PROF001', '1990-05-15', 8, 3),
('Amina Zerouali', 'PROF002', '1990-05-15', 9, 3);

-- 8. Statut d’adhésion
INSERT INTO statut_Adherent (id_adherent, nom, date_debut, date_fin) VALUES
(1, 'Actif', '2025-02-01', '2025-07-24'),
-- (2, 'Actif', '2025-02-01', '2025-07-01'),
(3, 'Actif', '2025-04-01', '2025-12-01'),
(4, 'Actif', '2025-07-01', '2026-07-01'),
-- (5, 'Actif', '2025-08-01', '2026-05-01'),
(6, 'Actif', '2025-07-01', '2026-06-01'),
(7, 'Actif', '2025-06-01', '2025-12-01');
-- (8, 'Actif', '2024-10-01', '2025-06-01');

-- 9. Livres
INSERT INTO Livre (titre, auteur) VALUES
('Les Miserables', 'Victor Hugo'),
('L''Etranger', 'Albert Camus'),
('Harry Potter a l''ecole des sorciers', 'J.K.Rowling');

-- 10. Exemplaires
INSERT INTO Exemplaire (code, id_livre) VALUES
('MIS001', 1),
('MIS002', 1),
('MIS003', 1),
('ETR001', 2),
('ETR002', 2),
('HAR001', 3);

-- 11. État des exemplaires
INSERT INTO Etat_Exemplaire (date_modif, id_exemplaire, id_etat) VALUES
('2001-01-01 00:00:00', 1, 1), 
('2001-01-01 00:00:00', 2, 1), 
('2001-01-01 00:00:00', 3, 1), 
('2001-01-01 00:00:00', 4, 1), 
('2001-01-01 00:00:00', 5, 1), 
('2001-01-01 00:00:00', 6, 1);

-- 12. Statuts généraux
INSERT INTO Statut (nom) VALUES 
('en attente'),
('valider');

-- 13. Jours fériés et dimanches
INSERT INTO Jour_Ferie (description, date_jf) VALUES
('Dimanche', '2025-07-13'),
('Dimanche', '2025-07-20'),
('Dimanche', '2025-07-27'),
('Dimanche', '2025-08-03'),
('Dimanche', '2025-08-10'),
('Dimanche', '2025-08-17'),
('Jour férié', '2025-07-19'),
('Jour férié', '2025-07-26');

-- 14. Comportement des jours fériés
INSERT INTO Regle_Jour_Ferie (comportement_, date_modif) VALUES
(1, NOW());

-- 15. Pénalités
INSERT INTO Penalite (nb_jour_de_penalite) VALUES 
(10),
(9),
(8);

-- 16. Pénalités par profil
INSERT INTO Penalite_Profil (date_modif, id_penalite, id_profil) VALUES
(NOW(), 1, 1),
(NOW(), 2, 2),
(NOW(), 3, 3);