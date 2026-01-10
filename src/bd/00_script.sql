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
(2, 2, '2026-01-12 12:00', '2026-01-12 13:25');

-- =========================
-- PRIX PAR CLASSE
-- =========================
INSERT INTO prix_vol (id_vol, classe, prix)
VALUES
(1, 'ECONOMY', 350000),
(1, 'BUSINESS', 750000),
(2, 'ECONOMY', 330000),
(2, 'BUSINESS', 700000),
(3, 'ECONOMY', 280000);
