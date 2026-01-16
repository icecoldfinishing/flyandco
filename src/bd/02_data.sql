

-- =========================
-- UTILISATEUR
-- =========================
INSERT INTO utilisateur (username, mot_de_passe, role)
VALUES
('admin', 'admin', 'admin');

-- =========================
-- AEROPORTS
-- =========================
INSERT INTO aeroport (nom, ville, pays, code_iata, code_icao)
VALUES
('Ivato International Airport', 'Antananarivo', 'Madagascar', 'TNR', 'FMMI'),
('Fascene Airport', 'Nosy Be', 'Madagascar', 'NOS', 'FMNN'),
('Arrachart Airport', 'Antsiranana', 'Madagascar', 'DIE', 'FMNA'),
('Toliara Airport', 'Toliara', 'Madagascar', 'TLE', 'FMST'),
('Marillac Airport', 'Fort Dauphin', 'Madagascar', 'FTU', 'FMSD');

-- =========================
-- COMPAGNIES
-- =========================
INSERT INTO compagnie (nom, pays, code_iata, code_icao)
VALUES
('Air Madagascar', 'Madagascar', 'MD', 'MDG'),
('Tsaradia', 'Madagascar', 'TZ', 'TSD'),
('Madagascar Airlines', 'Madagascar', 'MA', 'MDA'),
('Ewa Air', 'Comores', 'ZW', 'EWR');

-- =========================
-- AVIONS
-- =========================
INSERT INTO avion (id_compagnie, modele, capacite, numero_immatriculation)
VALUES
(1, 'ATR 72', 70, '5R-MJG'),
(2, 'ATR 42', 48, '5R-TSA'),
(3, 'Boeing 737', 140, '5R-MDL'),
(1, 'Dash 8 Q400', 78, '5R-DQ4'),
(4, 'Embraer 190', 100, 'D2-EWA');

-- =========================
-- VOLS (trajets)
-- =========================
INSERT INTO vol (id_compagnie, id_aeroport_depart, id_aeroport_arrivee)
VALUES
(1, 1, 2),
(2, 1, 2),
(1, 1, 3);

-- =========================
-- VOL INSTANCES
-- =========================
INSERT INTO vol_instance (id_vol, id_avion, date_depart, date_arrivee)
VALUES
(1, 1, '2026-01-12 12:00', '2026-01-12 13:30'),
(2, 2, '2026-01-12 12:00', '2026-01-12 13:25'),
(3, 3, '2026-01-15 02:00', '2026-01-15 08:25');

-- =========================
-- PRIX PAR CLASSE
-- =========================
INSERT INTO prix_vol (id_vol, classe, prix)
VALUES
(1, 'ECONOMY', 700000),
(1, 'FIRST', 1200000),
(1, 'PREMIUM', 1000000);



-- =========================
-- INSERTION DES MODES DE PAIEMENT
-- =========================
INSERT INTO moyen_paiement (libelle)
VALUES
('CB'),        -- Carte Bancaire
('Virement'),  -- Virement bancaire
('Paypal'),    -- Paiement en ligne via Paypal
('Espèces'),   -- Paiement en espèces
('Chèque');    -- Paiement par chèque

-- CREATION DE LA VUE SIMPLIFIEE
CREATE OR REPLACE VIEW view_chiffre_affaire AS
SELECT 
    c.id_compagnie,
    c.nom AS nom_compagnie,
    a.id_avion,
    a.modele AS avion_modele,
    a.numero_immatriculation,
    v.id_vol,
    vi.id_vol_instance,
    vi.date_depart,
    vi.date_arrivee,
    py.id_paiement,
    py.montant AS montant_paye,
    py.date_paiement,
    py.statut AS statut_paiement
FROM paiement py
JOIN reservation r ON py.id_reservation = r.id_reservation
JOIN vol_instance vi ON r.id_vol_instance = vi.id_vol_instance
JOIN vol v ON vi.id_vol = v.id_vol
JOIN avion a ON vi.id_avion = a.id_avion
JOIN compagnie c ON a.id_compagnie = c.id_compagnie
ORDER BY c.id_compagnie, vi.date_depart, v.id_vol;

-- =========================
-- FIRST CLASS pour le vol 1 (30 sièges)
-- =========================
INSERT INTO siege (id_vol, numero, classe) VALUES
(1,'1A','FIRST'), (1,'1B','FIRST'),
(1,'2A','FIRST'), (1,'2B','FIRST'),
(1,'3A','FIRST'), (1,'3B','FIRST'),
(1,'4A','FIRST'), (1,'4B','FIRST'),
(1,'5A','FIRST'), (1,'5B','FIRST'),
(1,'6A','FIRST'), (1,'6B','FIRST'),
(1,'7A','FIRST'), (1,'7B','FIRST'),
(1,'8A','FIRST'), (1,'8B','FIRST'),
(1,'9A','FIRST'), (1,'9B','FIRST'),
(1,'10A','FIRST'), (1,'10B','FIRST'),
(1,'11A','FIRST'), (1,'11B','FIRST'),
(1,'12A','FIRST'), (1,'12B','FIRST'),
(1,'13A','FIRST'), (1,'13B','FIRST'),
(1,'14A','FIRST'), (1,'14B','FIRST'),
(1,'15A','FIRST'), (1,'15B','FIRST');

-- =========================
-- PREMIUM CLASS pour le vol 1 (40 sièges)
-- =========================
INSERT INTO siege (id_vol, numero, classe) VALUES
(1,'16A','PREMIUM'), (1,'16B','PREMIUM'),
(1,'17A','PREMIUM'), (1,'17B','PREMIUM'),
(1,'18A','PREMIUM'), (1,'18B','PREMIUM'),
(1,'19A','PREMIUM'), (1,'19B','PREMIUM'),
(1,'20A','PREMIUM'), (1,'20B','PREMIUM'),
(1,'21A','PREMIUM'), (1,'21B','PREMIUM'),
(1,'22A','PREMIUM'), (1,'22B','PREMIUM'),
(1,'23A','PREMIUM'), (1,'23B','PREMIUM'),
(1,'24A','PREMIUM'), (1,'24B','PREMIUM'),
(1,'25A','PREMIUM'), (1,'25B','PREMIUM'),
(1,'26A','PREMIUM'), (1,'26B','PREMIUM'),
(1,'27A','PREMIUM'), (1,'27B','PREMIUM'),
(1,'28A','PREMIUM'), (1,'28B','PREMIUM'),
(1,'29A','PREMIUM'), (1,'29B','PREMIUM'),
(1,'30A','PREMIUM'), (1,'30B','PREMIUM'),
(1,'31A','PREMIUM'), (1,'31B','PREMIUM'),
(1,'32A','PREMIUM'), (1,'32B','PREMIUM'),
(1,'33A','PREMIUM'), (1,'33B','PREMIUM'),
(1,'34A','PREMIUM'), (1,'34B','PREMIUM'),
(1,'35A','PREMIUM'), (1,'35B','PREMIUM');

-- =========================
-- ECONOMY CLASS pour le vol 1 (50 sièges)
-- =========================
INSERT INTO siege (id_vol, numero, classe) VALUES
(1,'36A','ECONOMY'), (1,'36B','ECONOMY'),
(1,'37A','ECONOMY'), (1,'37B','ECONOMY'),
(1,'38A','ECONOMY'), (1,'38B','ECONOMY'),
(1,'39A','ECONOMY'), (1,'39B','ECONOMY'),
(1,'40A','ECONOMY'), (1,'40B','ECONOMY'),
(1,'41A','ECONOMY'), (1,'41B','ECONOMY'),
(1,'42A','ECONOMY'), (1,'42B','ECONOMY'),
(1,'43A','ECONOMY'), (1,'43B','ECONOMY'),
(1,'44A','ECONOMY'), (1,'44B','ECONOMY'),
(1,'45A','ECONOMY'), (1,'45B','ECONOMY'),
(1,'46A','ECONOMY'), (1,'46B','ECONOMY'),
(1,'47A','ECONOMY'), (1,'47B','ECONOMY'),
(1,'48A','ECONOMY'), (1,'48B','ECONOMY'),
(1,'49A','ECONOMY'), (1,'49B','ECONOMY'),
(1,'50A','ECONOMY'), (1,'50B','ECONOMY'),
(1,'51A','ECONOMY'), (1,'51B','ECONOMY'),
(1,'52A','ECONOMY'), (1,'52B','ECONOMY'),
(1,'53A','ECONOMY'), (1,'53B','ECONOMY'),
(1,'54A','ECONOMY'), (1,'54B','ECONOMY'),
(1,'55A','ECONOMY'), (1,'55B','ECONOMY'),
(1,'56A','ECONOMY'), (1,'56B','ECONOMY'),
(1,'57A','ECONOMY'), (1,'57B','ECONOMY'),
(1,'58A','ECONOMY'), (1,'58B','ECONOMY'),
(1,'59A','ECONOMY'), (1,'59B','ECONOMY'),
(1,'60A','ECONOMY'), (1,'60B','ECONOMY');

