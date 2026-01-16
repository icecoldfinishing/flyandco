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
    classe VARCHAR(20) NOT NULL CHECK (classe IN ('ECONOMY','PREMIUM','FIRST')),
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
    email VARCHAR(100) UNIQUE,
    est_enfant BOOLEAN NOT NULL DEFAULT FALSE
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
    classe VARCHAR(20) NOT NULL CHECK (classe IN ('ECONOMY','PREMIUM','FIRST')),
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

INSERT INTO passager (nom, prenom, date_naissance, email, est_enfant) VALUES
-- ================= ENFANTS (1 à 30) =================
('Nom1','Prenom1','2017-01-10','passager1@mail.com',TRUE),
('Nom2','Prenom2','2017-02-10','passager2@mail.com',TRUE),
('Nom3','Prenom3','2017-03-10','passager3@mail.com',TRUE),
('Nom4','Prenom4','2017-04-10','passager4@mail.com',TRUE),
('Nom5','Prenom5','2017-05-10','passager5@mail.com',TRUE),
('Nom6','Prenom6','2017-06-10','passager6@mail.com',TRUE),
('Nom7','Prenom7','2017-07-10','passager7@mail.com',TRUE),
('Nom8','Prenom8','2017-08-10','passager8@mail.com',TRUE),
('Nom9','Prenom9','2017-09-10','passager9@mail.com',TRUE),
('Nom10','Prenom10','2017-10-10','passager10@mail.com',TRUE),
('Nom11','Prenom11','2016-01-10','passager11@mail.com',TRUE),
('Nom12','Prenom12','2016-02-10','passager12@mail.com',TRUE),
('Nom13','Prenom13','2016-03-10','passager13@mail.com',TRUE),
('Nom14','Prenom14','2016-04-10','passager14@mail.com',TRUE),
('Nom15','Prenom15','2016-05-10','passager15@mail.com',TRUE),
('Nom16','Prenom16','2016-06-10','passager16@mail.com',TRUE),
('Nom17','Prenom17','2016-07-10','passager17@mail.com',TRUE),
('Nom18','Prenom18','2016-08-10','passager18@mail.com',TRUE),
('Nom19','Prenom19','2016-09-10','passager19@mail.com',TRUE),
('Nom20','Prenom20','2016-10-10','passager20@mail.com',TRUE),
('Nom21','Prenom21','2015-01-10','passager21@mail.com',TRUE),
('Nom22','Prenom22','2015-02-10','passager22@mail.com',TRUE),
('Nom23','Prenom23','2015-03-10','passager23@mail.com',TRUE),
('Nom24','Prenom24','2015-04-10','passager24@mail.com',TRUE),
('Nom25','Prenom25','2015-05-10','passager25@mail.com',TRUE),
('Nom26','Prenom26','2015-06-10','passager26@mail.com',TRUE),
('Nom27','Prenom27','2015-07-10','passager27@mail.com',TRUE),
('Nom28','Prenom28','2015-08-10','passager28@mail.com',TRUE),
('Nom29','Prenom29','2015-09-10','passager29@mail.com',TRUE),
('Nom30','Prenom30','2015-10-10','passager30@mail.com',TRUE),

-- ================= ADULTES (31 à 100) =================
('Nom31','Prenom31','1980-01-10','passager31@mail.com',FALSE),
('Nom32','Prenom32','1981-02-10','passager32@mail.com',FALSE),
('Nom33','Prenom33','1982-03-10','passager33@mail.com',FALSE),
('Nom34','Prenom34','1983-04-10','passager34@mail.com',FALSE),
('Nom35','Prenom35','1984-05-10','passager35@mail.com',FALSE),
('Nom36','Prenom36','1985-06-10','passager36@mail.com',FALSE),
('Nom37','Prenom37','1986-07-10','passager37@mail.com',FALSE),
('Nom38','Prenom38','1987-08-10','passager38@mail.com',FALSE),
('Nom39','Prenom39','1988-09-10','passager39@mail.com',FALSE),
('Nom40','Prenom40','1989-10-10','passager40@mail.com',FALSE),
('Nom41','Prenom41','1990-01-10','passager41@mail.com',FALSE),
('Nom42','Prenom42','1991-02-10','passager42@mail.com',FALSE),
('Nom43','Prenom43','1992-03-10','passager43@mail.com',FALSE),
('Nom44','Prenom44','1993-04-10','passager44@mail.com',FALSE),
('Nom45','Prenom45','1994-05-10','passager45@mail.com',FALSE),
('Nom46','Prenom46','1995-06-10','passager46@mail.com',FALSE),
('Nom47','Prenom47','1996-07-10','passager47@mail.com',FALSE),
('Nom48','Prenom48','1997-08-10','passager48@mail.com',FALSE),
('Nom49','Prenom49','1998-09-10','passager49@mail.com',FALSE),
('Nom50','Prenom50','1999-10-10','passager50@mail.com',FALSE),

('Nom51','Prenom51','1980-01-10','passager51@mail.com',FALSE),
('Nom52','Prenom52','1981-02-10','passager52@mail.com',FALSE),
('Nom53','Prenom53','1982-03-10','passager53@mail.com',FALSE),
('Nom54','Prenom54','1983-04-10','passager54@mail.com',FALSE),
('Nom55','Prenom55','1984-05-10','passager55@mail.com',FALSE),
('Nom56','Prenom56','1985-06-10','passager56@mail.com',FALSE),
('Nom57','Prenom57','1986-07-10','passager57@mail.com',FALSE),
('Nom58','Prenom58','1987-08-10','passager58@mail.com',FALSE),
('Nom59','Prenom59','1988-09-10','passager59@mail.com',FALSE),
('Nom60','Prenom60','1989-10-10','passager60@mail.com',FALSE),

('Nom61','Prenom61','1990-01-10','passager61@mail.com',FALSE),
('Nom62','Prenom62','1991-02-10','passager62@mail.com',FALSE),
('Nom63','Prenom63','1992-03-10','passager63@mail.com',FALSE),
('Nom64','Prenom64','1993-04-10','passager64@mail.com',FALSE),
('Nom65','Prenom65','1994-05-10','passager65@mail.com',FALSE),
('Nom66','Prenom66','1995-06-10','passager66@mail.com',FALSE),
('Nom67','Prenom67','1996-07-10','passager67@mail.com',FALSE),
('Nom68','Prenom68','1997-08-10','passager68@mail.com',FALSE),
('Nom69','Prenom69','1998-09-10','passager69@mail.com',FALSE),
('Nom70','Prenom70','1999-10-10','passager70@mail.com',FALSE),

('Nom71','Prenom71','1980-01-10','passager71@mail.com',FALSE),
('Nom72','Prenom72','1981-02-10','passager72@mail.com',FALSE),
('Nom73','Prenom73','1982-03-10','passager73@mail.com',FALSE),
('Nom74','Prenom74','1983-04-10','passager74@mail.com',FALSE),
('Nom75','Prenom75','1984-05-10','passager75@mail.com',FALSE),
('Nom76','Prenom76','1985-06-10','passager76@mail.com',FALSE),
('Nom77','Prenom77','1986-07-10','passager77@mail.com',FALSE),
('Nom78','Prenom78','1987-08-10','passager78@mail.com',FALSE),
('Nom79','Prenom79','1988-09-10','passager79@mail.com',FALSE),
('Nom80','Prenom80','1989-10-10','passager80@mail.com',FALSE),

('Nom81','Prenom81','1990-01-10','passager81@mail.com',FALSE),
('Nom82','Prenom82','1991-02-10','passager82@mail.com',FALSE),
('Nom83','Prenom83','1992-03-10','passager83@mail.com',FALSE),
('Nom84','Prenom84','1993-04-10','passager84@mail.com',FALSE),
('Nom85','Prenom85','1994-05-10','passager85@mail.com',FALSE),
('Nom86','Prenom86','1995-06-10','passager86@mail.com',FALSE),
('Nom87','Prenom87','1996-07-10','passager87@mail.com',FALSE),
('Nom88','Prenom88','1997-08-10','passager88@mail.com',FALSE),
('Nom89','Prenom89','1998-09-10','passager89@mail.com',FALSE),
('Nom90','Prenom90','1999-10-10','passager90@mail.com',FALSE),

('Nom91','Prenom91','1980-01-10','passager91@mail.com',FALSE),
('Nom92','Prenom92','1981-02-10','passager92@mail.com',FALSE),
('Nom93','Prenom93','1982-03-10','passager93@mail.com',FALSE),
('Nom94','Prenom94','1983-04-10','passager94@mail.com',FALSE),
('Nom95','Prenom95','1984-05-10','passager95@mail.com',FALSE),
('Nom96','Prenom96','1985-06-10','passager96@mail.com',FALSE),
('Nom97','Prenom97','1986-07-10','passager97@mail.com',FALSE),
('Nom98','Prenom98','1987-08-10','passager98@mail.com',FALSE),
('Nom99','Prenom99','1988-09-10','passager99@mail.com',FALSE),
('Nom100','Prenom100','1989-10-10','passager100@mail.com',FALSE);
