on de 3 livres
INSERT INTO
    Livre (titre, auteur)
VALUES
    ('Les Miserables', 'Victor Hugo'),
    ('L etranger', 'Albert Camus'),
    ('Harry Potter a l ecole des sorciers', 'J.K Rowling');

INSERT INTO
    Exemplaire (code, id_livre)
VALUES
    ('MIS001', 1),
    ('MIS002', 1),
    ('MIS003', 1),
    ('ETR001', 2),
    ('ETR002', 2),
    ('HAR001', 3);

INSERT INTO
    Etat_Exemplaire (date_modif, id_exemplaire, id_etat)
VALUES
    (NOW (), 1, 1),
    (NOW (), 2, 1),
    (NOW (), 3, 1),
    (NOW (), 4, 1),
    (NOW (), 5, 1),
    (NOW (), 6, 1);



INSERT INTO
    Etat (nom)
VALUES
    ('disponible'),
    ('en pret');


INSERT INTO
    Type (nom)
VALUES
    ('Maison'),
    ('Sur place');

INSERT INTO
    Role (nom)
VALUES
    ('bibliothequaire'),
    ('adherent'),
    ('admin');

INSERT INTO
    Utilisateur (username, mdp, id_role)
VALUES
    ('ETU001', 'mdp', 2), 
    ('ETU002', 'mdp', 2), 
    ('ETU003', 'mdp', 2);
    ('ENS001', 'mdp', 2), 
    ('ENS002', 'mdp', 2),
    ('ENS003', 'mdp', 2),
    ('PROF001', 'mdp', 2),
    ('PROF002', 'mdp', 2);

INSERT INTO
    Utilisateur (username, mdp, id_role)
VALUES
    ('biblio', 'mdp', 1), 
    ('admin', 'mdp', 3);



INSERT INTO
    Profil (nom, id_regle)
VALUES
    ('etudiant', 1),
    ('professeur', 2),
    ('autre', 3);
    
INSERT INTO
    Adherent (
        nom,
        prenom,
        date_de_naissance,
        id_utilisateur,
        id_profil
    )
VALUES
    ('ETU001', 'Amine Bensaïd', '1990-05-15', 2, 1),
    ('ETU002', 'Sarah El Khattabi', '1985-09-20', 4, 1),
    ('ETU003', 'Youssef Moujahid', '1985-09-20', 4, 1),
    ('ENS001', 'Nadia Benali', '1985-09-20', 4, 2),
    ('ENS002', 'Karim Haddadi', '1985-09-20', 4, 2),
    ('ENS003', 'Salima Touhami', '1985-09-20', 4, 2),
    ('PROF001', 'Rachid El Mansouri', '1985-09-20', 7, 3),
    ('PROF002', 'Amina Zerouali', '1985-09-20', 8, 3);


INSERT INTO statut_Adherent (id_adherent, nom, date_debut, date_fin) VALUES
(1, 'Actif', '2025-02-01', '2025-07-24'),
(2, 'Inactif', '2025-02-01', '2025-07-01'),
(3, 'Actif', '2025-04-01','2025-12-01'),
(4, 'Actif', '2025-07-01', '2026-07-01'),
(5, 'Inactif', '2025-08-01', '2026-05-01'),
(6, 'Actif', '2025-07-01', '2026-06-01'),
(7, 'Actif', '2025-06-01', '2025-12-01'),
(8, 'Inactif', '2024-10-01', '2025-06-01');

INSERT INTO
    Regle (
        nb_jour_duree_pret_max,
        nb_livre_preter_max,
        nb_prolengement_pret_max,
        nb_jour_prolongement_max
    )
VALUES
    (7,7,3,3 ),
    (9,9,5,5 ),
    (12,12,7,7);


INSERT INTO
    Statut (nom)
VALUES
    ('en attente'),
    ('valider');


INSERT INTO
    Jour_Ferie (description, date_jf)
VALUES
    ('Ferie', '2025-07-13'),
    ('Ferie', '2025-07-20'),
    ('Ferie', '2025-07-27'),
    ('Ferie', '2025-08-03'),
    ('Ferie', '2025-08-10'),
    ('Ferie', '2025-08-17'),
    ('Ferie', '2025-07-26'),
    ('Ferie', '2025-07-19');

INSERT INTO
    Regle_Jour_Ferie (comportement_, date_modif) --ement = 1 si apres date jour ferier quon doit rendre , = 0 si avant date
VALUES
    (1, NOW ());




INSERT INTO
    Penalite (nb_jour_de_penalite)
VALUES
    (10),  
    (9),  
    (8);

INSERT INTO
    Penalite_Profil (date_modif, id_penalite, id_profil)
VALUES
    (NOW (), 1, 1),
    (NOW (), 2, 2),
    (NOW (), 3, 3);

INSERT INTO
    Abonnement (mois, annee, tarif)
VALUES
    (12, 2025, 10000),
    (11, 2025, 10000),
    (10, 2025, 10000),
    (9, 2025, 10000),
    (8, 2025, 10000),
    (7, 2025, 10000),
    (6, 2025, 10000),
    (5, 2025, 10000),
    (4, 2025, 10000),
    (3, 2025, 10000),
    (2, 2025, 10000),
    (1, 2025, 10000);




--er EX-0007 et EX-0008 pour l'adhérent 'Rakoto Jean' (id_adherent = 1)
INSERT INTO Pret (date_debut, date_fin, id_exemplaire, id_adherent, id_type)
VALUES
('2025-07-01', '2025-07-10', 7, 1, 1), ', '2025-07-12', 8, 1, 2), ', '2025-07-13', 9, 1, 1), ', '2025-07-14', 1, 1, 2), ', '2025-07-15', 2, 2, 1), ', '2025-07-16', 3, 2, 2), ', '2025-07-17', 4, 3, 1), ', '2025-07-18', 5, 3, 2), ', '2025-07-19', 6, 3, 1); O Date_Prevue_Rendu (id_pret, date_prevue) VALUES
(1, '2025-07-10 00:00:00'),
(2, '2025-07-11 00:00:00'),
(3, '2025-07-14 00:00:00'),
(4, '2025-07-13 00:00:00'),
(5, '2025-07-17 00:00:00'),
(6, '2025-07-18 00:00:00'),
(7, '2025-07-17 00:00:00'),
(8, '2025-07-20 00:00:00'),
(9, '2025-07-19 00:00:00');




INSERT INTO regle_jour_apres_rendu (nombre_jour) VALUES (10);  

