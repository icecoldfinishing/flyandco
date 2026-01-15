DROP DATABASE IF EXISTS pg10;
CREATE DATABASE pg10;
\c pg10;

-- =========================
-- UTILISATEUR (ADMIN)
-- =========================
CREATE TABLE utilisateur (
    id_utilisateur SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    mot_de_passe VARCHAR(100) NOT NULL,
    role VARCHAR(20) DEFAULT 'admin'
);

-- =========================
-- COMPAGNIE
-- =========================
CREATE TABLE compagnie (
    id_compagnie SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    pays VARCHAR(50),
    code_iata CHAR(2) UNIQUE,
    code_icao CHAR(3) UNIQUE
);

-- =========================
-- AEROPORT
-- =========================
CREATE TABLE aeroport (
    id_aeroport SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    ville VARCHAR(50),
    pays VARCHAR(50),
    code_iata CHAR(3) UNIQUE NOT NULL,
    code_icao CHAR(4) UNIQUE
);

-- =========================
-- AVION
-- =========================
CREATE TABLE avion (
    id_avion SERIAL PRIMARY KEY,
    id_compagnie INT NOT NULL,
    modele VARCHAR(50) NOT NULL,
    capacite INT NOT NULL CHECK (capacite > 0),
    numero_immatriculation VARCHAR(20) UNIQUE NOT NULL,
    FOREIGN KEY (id_compagnie) REFERENCES compagnie(id_compagnie)
);

-- =========================
-- VOL (trajet théorique)
-- =========================
CREATE TABLE vol (
    id_vol SERIAL PRIMARY KEY,
    id_compagnie INT NOT NULL,
    id_aeroport_depart INT NOT NULL,
    id_aeroport_arrivee INT NOT NULL,
    FOREIGN KEY (id_compagnie) REFERENCES compagnie(id_compagnie),
    FOREIGN KEY (id_aeroport_depart) REFERENCES aeroport(id_aeroport),
    FOREIGN KEY (id_aeroport_arrivee) REFERENCES aeroport(id_aeroport),
    CHECK (id_aeroport_depart <> id_aeroport_arrivee)
);

-- =========================
-- VOL INSTANCE (date réelle)
-- =========================
CREATE TABLE vol_instance (
    id_vol_instance SERIAL PRIMARY KEY,
    id_vol INT NOT NULL,
    id_avion INT NOT NULL,
    date_depart TIMESTAMP NOT NULL,
    date_arrivee TIMESTAMP NOT NULL,
    statut VARCHAR(20) DEFAULT 'PREVU',
    FOREIGN KEY (id_vol) REFERENCES vol(id_vol),
    FOREIGN KEY (id_avion) REFERENCES avion(id_avion)
);

-- =========================
-- PRIX PAR CLASSE
-- =========================
CREATE TABLE prix_vol (
    id_prix SERIAL PRIMARY KEY,
    id_vol INT NOT NULL,
    classe VARCHAR(20) NOT NULL CHECK (classe IN ('ECONOMY','BUSINESS','FIRST')),
    prix NUMERIC(10,2) NOT NULL CHECK (prix > 0),
    date_maj TIMESTAMP DEFAULT NOW(),
    FOREIGN KEY (id_vol) REFERENCES vol(id_vol),
    UNIQUE (id_vol, classe)
);

-- =========================
-- PASSAGER
-- =========================
CREATE TABLE passager (
    id_passager SERIAL PRIMARY KEY,
    nom VARCHAR(50) NOT NULL,
    prenom VARCHAR(50) NOT NULL,
    date_naissance DATE,
    email VARCHAR(100) UNIQUE
);

-- =========================
-- RESERVATION
-- =========================
CREATE TABLE reservation (
    id_reservation SERIAL PRIMARY KEY,
    id_passager INT NOT NULL,
    id_vol_instance INT NOT NULL,
    id_prix INT NOT NULL,
    siege VARCHAR(5),
    date_reservation TIMESTAMP DEFAULT NOW(),
    statut VARCHAR(20) DEFAULT 'CONFIRMEE',
    FOREIGN KEY (id_passager) REFERENCES passager(id_passager),
    FOREIGN KEY (id_vol_instance) REFERENCES vol_instance(id_vol_instance),
    FOREIGN KEY (id_prix) REFERENCES prix_vol(id_prix),
    UNIQUE (id_passager, id_vol_instance)
);

-- =========================
-- PILOTE
-- =========================
CREATE TABLE pilote (
    id_pilote SERIAL PRIMARY KEY,
    id_compagnie INT NOT NULL,
    nom VARCHAR(50) NOT NULL,
    prenom VARCHAR(50) NOT NULL,
    licence_num VARCHAR(20) UNIQUE NOT NULL,
    experience INT DEFAULT 0,
    FOREIGN KEY (id_compagnie) REFERENCES compagnie(id_compagnie)
);

-- =========================
-- EQUIPAGE
-- =========================
CREATE TABLE equipage (
    id_equipage SERIAL PRIMARY KEY,
    id_vol_instance INT NOT NULL,
    id_pilote INT NOT NULL,
    role VARCHAR(50) DEFAULT 'PILOTE',
    FOREIGN KEY (id_vol_instance) REFERENCES vol_instance(id_vol_instance),
    FOREIGN KEY (id_pilote) REFERENCES pilote(id_pilote),
    UNIQUE (id_vol_instance, id_pilote)
);


-- =========================
-- MODE PAIEMENT
-- =========================
CREATE TABLE moyen_paiement (
    id_moyen_paiement SERIAL PRIMARY KEY,
    libelle VARCHAR(50) NOT NULL UNIQUE
);

-- =========================
-- PAIEMENT
-- =========================
CREATE TABLE paiement (
    id_paiement SERIAL PRIMARY KEY,
    id_reservation INT NOT NULL,
    id_moyen_paiement INT NOT NULL,
    montant NUMERIC(10,2) NOT NULL,
    date_paiement TIMESTAMP DEFAULT NOW(),
    statut VARCHAR(20) DEFAULT 'OK',
    FOREIGN KEY (id_reservation) REFERENCES reservation(id_reservation),
    FOREIGN KEY (id_moyen_paiement) REFERENCES moyen_paiement(id_moyen_paiement)
);

-- =========================
-- SIEGE
-- =========================
CREATE TABLE siege (
    id_siege SERIAL PRIMARY KEY,
    id_vol INT NOT NULL,    
    numero VARCHAR(5) NOT NULL,           
    classe VARCHAR(20) NOT NULL CHECK (classe IN ('ECONOMY','BUSINESS','FIRST')),
    FOREIGN KEY (id_vol) REFERENCES vol(id_vol)   
);


CREATE TABLE siege_vol (
    id_siege_vol SERIAL PRIMARY KEY,
    id_vol_instance INT NOT NULL,
    id_siege INT NOT NULL,
    statut VARCHAR(20) DEFAULT 'LIBRE',
    FOREIGN KEY (id_vol_instance) REFERENCES vol_instance(id_vol_instance),
    FOREIGN KEY (id_siege) REFERENCES siege(id_siege)
);

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
(2, 'ECONOMY', 330000),
(2, 'FIRST', 1200000),
(3, 'FIRST', 1200000),
(3, 'ECONOMY', 700000);


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
-- FIRST CLASS pour le vol 1 (1A → 5B = 10 sièges)
-- =========================
INSERT INTO siege (id_vol, numero, classe) VALUES
(1,'1A','FIRST'),(1,'1B','FIRST'),
(1,'2A','FIRST'),(1,'2B','FIRST'),
(1,'3A','FIRST'),(1,'3B','FIRST'),
(1,'4A','FIRST'),(1,'4B','FIRST'),
(1,'5A','FIRST'),(1,'5B','FIRST');

-- =========================
-- ECONOMY pour le vol 1 (6A → 35B = 60 sièges)
-- =========================
INSERT INTO siege (id_vol, numero, classe) VALUES
(1,'6A','ECONOMY'),(1,'6B','ECONOMY'),
(1,'7A','ECONOMY'),(1,'7B','ECONOMY'),
(1,'8A','ECONOMY'),(1,'8B','ECONOMY'),
(1,'9A','ECONOMY'),(1,'9B','ECONOMY'),
(1,'10A','ECONOMY'),(1,'10B','ECONOMY'),
(1,'11A','ECONOMY'),(1,'11B','ECONOMY'),
(1,'12A','ECONOMY'),(1,'12B','ECONOMY'),
(1,'13A','ECONOMY'),(1,'13B','ECONOMY'),
(1,'14A','ECONOMY'),(1,'14B','ECONOMY'),
(1,'15A','ECONOMY'),(1,'15B','ECONOMY'),
(1,'16A','ECONOMY'),(1,'16B','ECONOMY'),
(1,'17A','ECONOMY'),(1,'17B','ECONOMY'),
(1,'18A','ECONOMY'),(1,'18B','ECONOMY'),
(1,'19A','ECONOMY'),(1,'19B','ECONOMY'),
(1,'20A','ECONOMY'),(1,'20B','ECONOMY'),
(1,'21A','ECONOMY'),(1,'21B','ECONOMY'),
(1,'22A','ECONOMY'),(1,'22B','ECONOMY'),
(1,'23A','ECONOMY'),(1,'23B','ECONOMY'),
(1,'24A','ECONOMY'),(1,'24B','ECONOMY'),
(1,'25A','ECONOMY'),(1,'25B','ECONOMY'),
(1,'26A','ECONOMY'),(1,'26B','ECONOMY'),
(1,'27A','ECONOMY'),(1,'27B','ECONOMY'),
(1,'28A','ECONOMY'),(1,'28B','ECONOMY'),
(1,'29A','ECONOMY'),(1,'29B','ECONOMY'),
(1,'30A','ECONOMY'),(1,'30B','ECONOMY'),
(1,'31A','ECONOMY'),(1,'31B','ECONOMY'),
(1,'32A','ECONOMY'),(1,'32B','ECONOMY'),
(1,'33A','ECONOMY'),(1,'33B','ECONOMY'),
(1,'34A','ECONOMY'),(1,'34B','ECONOMY'),
(1,'35A','ECONOMY'),(1,'35B','ECONOMY');

-- =========================
-- FIRST CLASS pour le vol 2 (1A → 4B = 8 sièges)
-- =========================
INSERT INTO siege (id_vol, numero, classe) VALUES
(2,'1A','FIRST'),(2,'1B','FIRST'),
(2,'2A','FIRST'),(2,'2B','FIRST'),
(2,'3A','FIRST'),(2,'3B','FIRST'),
(2,'4A','FIRST'),(2,'4B','FIRST');

-- =========================
-- ECONOMY pour le vol 2 (5A → 24B = 40 sièges)
-- =========================
INSERT INTO siege (id_vol, numero, classe) VALUES
(2,'5A','ECONOMY'),(2,'5B','ECONOMY'),
(2,'6A','ECONOMY'),(2,'6B','ECONOMY'),
(2,'7A','ECONOMY'),(2,'7B','ECONOMY'),
(2,'8A','ECONOMY'),(2,'8B','ECONOMY'),
(2,'9A','ECONOMY'),(2,'9B','ECONOMY'),
(2,'10A','ECONOMY'),(2,'10B','ECONOMY'),
(2,'11A','ECONOMY'),(2,'11B','ECONOMY'),
(2,'12A','ECONOMY'),(2,'12B','ECONOMY'),
(2,'13A','ECONOMY'),(2,'13B','ECONOMY'),
(2,'14A','ECONOMY'),(2,'14B','ECONOMY'),
(2,'15A','ECONOMY'),(2,'15B','ECONOMY'),
(2,'16A','ECONOMY'),(2,'16B','ECONOMY'),
(2,'17A','ECONOMY'),(2,'17B','ECONOMY'),
(2,'18A','ECONOMY'),(2,'18B','ECONOMY'),
(2,'19A','ECONOMY'),(2,'19B','ECONOMY'),
(2,'20A','ECONOMY'),(2,'20B','ECONOMY'),
(2,'21A','ECONOMY'),(2,'21B','ECONOMY'),
(2,'22A','ECONOMY'),(2,'22B','ECONOMY'),
(2,'23A','ECONOMY'),(2,'23B','ECONOMY'),
(2,'24A','ECONOMY'),(2,'24B','ECONOMY');

-- =========================
-- FIRST CLASS pour le vol 3 (1A → 10B = 20 sièges)
-- =========================
INSERT INTO siege (id_vol, numero, classe) VALUES
(3,'1A','FIRST'),(3,'1B','FIRST'),
(3,'2A','FIRST'),(3,'2B','FIRST'),
(3,'3A','FIRST'),(3,'3B','FIRST'),
(3,'4A','FIRST'),(3,'4B','FIRST'),
(3,'5A','FIRST'),(3,'5B','FIRST'),
(3,'6A','FIRST'),(3,'6B','FIRST'),
(3,'7A','FIRST'),(3,'7B','FIRST'),
(3,'8A','FIRST'),(3,'8B','FIRST'),
(3,'9A','FIRST'),(3,'9B','FIRST'),
(3,'10A','FIRST'),(3,'10B','FIRST');


-- =========================
-- ECONOMY pour le vol 3 (11A → 70B = 120 sièges)
-- =========================
INSERT INTO siege (id_vol, numero, classe) VALUES
(3,'11A','ECONOMY'),(3,'11B','ECONOMY'),
(3,'12A','ECONOMY'),(3,'12B','ECONOMY'),
(3,'13A','ECONOMY'),(3,'13B','ECONOMY'),
(3,'14A','ECONOMY'),(3,'14B','ECONOMY'),
(3,'15A','ECONOMY'),(3,'15B','ECONOMY'),
(3,'16A','ECONOMY'),(3,'16B','ECONOMY'),
(3,'17A','ECONOMY'),(3,'17B','ECONOMY'),
(3,'18A','ECONOMY'),(3,'18B','ECONOMY'),
(3,'19A','ECONOMY'),(3,'19B','ECONOMY'),
(3,'20A','ECONOMY'),(3,'20B','ECONOMY'),
(3,'21A','ECONOMY'),(3,'21B','ECONOMY'),
(3,'22A','ECONOMY'),(3,'22B','ECONOMY'),
(3,'23A','ECONOMY'),(3,'23B','ECONOMY'),
(3,'24A','ECONOMY'),(3,'24B','ECONOMY'),
(3,'25A','ECONOMY'),(3,'25B','ECONOMY'),
(3,'26A','ECONOMY'),(3,'26B','ECONOMY'),
(3,'27A','ECONOMY'),(3,'27B','ECONOMY'),
(3,'28A','ECONOMY'),(3,'28B','ECONOMY'),
(3,'29A','ECONOMY'),(3,'29B','ECONOMY'),
(3,'30A','ECONOMY'),(3,'30B','ECONOMY'),
(3,'31A','ECONOMY'),(3,'31B','ECONOMY'),
(3,'32A','ECONOMY'),(3,'32B','ECONOMY'),
(3,'33A','ECONOMY'),(3,'33B','ECONOMY'),
(3,'34A','ECONOMY'),(3,'34B','ECONOMY'),
(3,'35A','ECONOMY'),(3,'35B','ECONOMY'),
(3,'36A','ECONOMY'),(3,'36B','ECONOMY'),
(3,'37A','ECONOMY'),(3,'37B','ECONOMY'),
(3,'38A','ECONOMY'),(3,'38B','ECONOMY'),
(3,'39A','ECONOMY'),(3,'39B','ECONOMY'),
(3,'40A','ECONOMY'),(3,'40B','ECONOMY'),
(3,'41A','ECONOMY'),(3,'41B','ECONOMY'),
(3,'42A','ECONOMY'),(3,'42B','ECONOMY'),
(3,'43A','ECONOMY'),(3,'43B','ECONOMY'),
(3,'44A','ECONOMY'),(3,'44B','ECONOMY'),
(3,'45A','ECONOMY'),(3,'45B','ECONOMY'),
(3,'46A','ECONOMY'),(3,'46B','ECONOMY'),
(3,'47A','ECONOMY'),(3,'47B','ECONOMY'),
(3,'48A','ECONOMY'),(3,'48B','ECONOMY'),
(3,'49A','ECONOMY'),(3,'49B','ECONOMY'),
(3,'50A','ECONOMY'),(3,'50B','ECONOMY'),
(3,'51A','ECONOMY'),(3,'51B','ECONOMY'),
(3,'52A','ECONOMY'),(3,'52B','ECONOMY'),
(3,'53A','ECONOMY'),(3,'53B','ECONOMY'),
(3,'54A','ECONOMY'),(3,'54B','ECONOMY'),
(3,'55A','ECONOMY'),(3,'55B','ECONOMY'),
(3,'56A','ECONOMY'),(3,'56B','ECONOMY'),
(3,'57A','ECONOMY'),(3,'57B','ECONOMY'),
(3,'58A','ECONOMY'),(3,'58B','ECONOMY'),
(3,'59A','ECONOMY'),(3,'59B','ECONOMY'),
(3,'60A','ECONOMY'),(3,'60B','ECONOMY'),
(3,'61A','ECONOMY'),(3,'61B','ECONOMY'),
(3,'62A','ECONOMY'),(3,'62B','ECONOMY'),
(3,'63A','ECONOMY'),(3,'63B','ECONOMY'),
(3,'64A','ECONOMY'),(3,'64B','ECONOMY'),
(3,'65A','ECONOMY'),(3,'65B','ECONOMY'),
(3,'66A','ECONOMY'),(3,'66B','ECONOMY'),
(3,'67A','ECONOMY'),(3,'67B','ECONOMY'),
(3,'68A','ECONOMY'),(3,'68B','ECONOMY'),
(3,'69A','ECONOMY'),(3,'69B','ECONOMY'),
(3,'70A','ECONOMY'),(3,'70B','ECONOMY');
