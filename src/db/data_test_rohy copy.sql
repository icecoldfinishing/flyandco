INSERT INTO Pret (date_debut, date_fin, id_exemplaire, id_adherent, id_type) VALUES
-- id_adherent = 2
('2025-07-01', '2025-07-10', 1, 3, 1),
('2025-07-02', '2025-08-11', 2, 3, 2),
('2025-07-02', '2025-08-22', 2, 3, 2);


INSERT INTO Date_Prevue_Rendu (id_pret, date_prevue) VALUES
-- id_adherent 2 (pret id 10 à 19)
(16, '2025-07-10 00:00:00'),
(17, '2025-08-11 00:00:00'),
(18, '2025-08-22 00:00:00');


INSERT INTO Rendu (id_pret, date_du_rendu) VALUES
-- id_adherent 2 (id_pret 10 à 15)
(16, '2025-07-28 09:00:00'), 
(17, '2025-08-12 15:00:00'),
(18, '2025-08-20 15:00:00');

