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
    duree_minutes INT NOT NULL CHECK (duree_minutes > 0),
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

-- ======================================================
-- INSERTS (cohérents avec le modèle)
-- ======================================================

INSERT INTO aeroport(nom,ville,pays,code_iata,code_icao) VALUES ('Ivato International Airport','Antananarivo','Madagascar','TNR','FMMI');
INSERT INTO aeroport(nom,ville,pays,code_iata,code_icao) VALUES ('Fascene Airport','Nosy Be','Madagascar','NOS','FMNN');
INSERT INTO aeroport(nom,ville,pays,code_iata,code_icao) VALUES ('Arrachart Airport','Antsiranana','Madagascar','DIE','FMNA');
INSERT INTO aeroport(nom,ville,pays,code_iata,code_icao) VALUES ('Toliara Airport','Toliara','Madagascar','TLE','FMST');
INSERT INTO aeroport(nom,ville,pays,code_iata,code_icao) VALUES ('Marillac Airport','Fort Dauphin','Madagascar','FTU','FMSD');

INSERT INTO compagnie(nom,pays,code_iata,code_icao) VALUES ('Air Madagascar','Madagascar','MD','MDG');
INSERT INTO compagnie(nom,pays,code_iata,code_icao) VALUES ('Tsaradia','Madagascar','TZ','TSD');
INSERT INTO compagnie(nom,pays,code_iata,code_icao) VALUES ('Madagascar Airlines','Madagascar','MA','MDA');
INSERT INTO compagnie(nom,pays,code_iata,code_icao) VALUES ('Ewa Air','Comores','ZW','EWR');

INSERT INTO avion(id_compagnie,modele,capacite,numero_immatriculation) VALUES (1,'ATR 72',70,'5R-MJG');
INSERT INTO avion(id_compagnie,modele,capacite,numero_immatriculation) VALUES (2,'ATR 42',48,'5R-TSA');
INSERT INTO avion(id_compagnie,modele,capacite,numero_immatriculation) VALUES (3,'Boeing 737',140,'5R-MDL');
INSERT INTO avion(id_compagnie,modele,capacite,numero_immatriculation) VALUES (1,'Dash 8 Q400',78,'5R-DQ4');
INSERT INTO avion(id_compagnie,modele,capacite,numero_immatriculation) VALUES (4,'Embraer 190',100,'D2-EWA');

INSERT INTO vol(id_compagnie,id_aeroport_depart,id_aeroport_arrivee,duree_minutes) VALUES (1,1,2,90);
INSERT INTO vol(id_compagnie,id_aeroport_depart,id_aeroport_arrivee,duree_minutes) VALUES (2,1,2,85);
INSERT INTO vol(id_compagnie,id_aeroport_depart,id_aeroport_arrivee,duree_minutes) VALUES (1,1,3,120);

INSERT INTO vol_instance(id_vol,id_avion,date_depart,date_arrivee) VALUES (1,1,'2026-01-12 12:00','2026-01-12 13:30');
INSERT INTO vol_instance(id_vol,id_avion,date_depart,date_arrivee) VALUES (2,2,'2026-01-12 12:00','2026-01-12 13:25');

INSERT INTO prix_vol(id_vol,classe,prix) VALUES (1,'ECONOMY',350000);
INSERT INTO prix_vol(id_vol,classe,prix) VALUES (1,'BUSINESS',750000);
INSERT INTO prix_vol(id_vol,classe,prix) VALUES (2,'ECONOMY',330000);
INSERT INTO prix_vol(id_vol,classe,prix) VALUES (2,'BUSINESS',700000);
INSERT INTO prix_vol(id_vol,classe,prix) VALUES (3,'ECONOMY',280000);
