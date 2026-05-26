-- =====================================================
--  Données de test HackHub
--  Ordre : comptes -> entités métier -> hackathons -> équipes -> liaisons
-- =====================================================

-- Extension de hachage des mots de passe (BCrypt)
CREATE SCHEMA IF NOT EXISTS ext;
CREATE EXTENSION IF NOT EXISTS pgcrypto WITH SCHEMA ext;

-- Réinitialise toutes les tables
TRUNCATE compte, membrestaff, recompense, epreuve, mentor, organisateurhackathon,
         juge, partenaire, hackathon, equipe, participant,
         avoir, contenir, noter, sponsoriser
RESTART IDENTITY CASCADE;


-- =======
-- Comptes (mot de passe = pseudo, haché en BCrypt)
-- =======
INSERT INTO compte (id_compte, pseudo, empreinte_mdp, email, role_admin) VALUES
( 1, 'admin',   ext.crypt('HackHub@2026', ext.gen_salt('bf')), 'admin@mail.com',   TRUE ),
( 2, 'max',     ext.crypt('HackHub@2026', ext.gen_salt('bf')), 'max@mail.com',     FALSE ),
( 3, 'mika',    ext.crypt('HackHub@2026', ext.gen_salt('bf')), 'mika@mail.com',    FALSE ),
( 4, 'tom',     ext.crypt('HackHub@2026', ext.gen_salt('bf')), 'tom@mail.com',     FALSE ),
( 5, 'eva',     ext.crypt('HackHub@2026', ext.gen_salt('bf')), 'eva@mail.com',     FALSE ),
( 6, 'marie',   ext.crypt('HackHub@2026', ext.gen_salt('bf')), 'marie@mail.com',   FALSE ),
( 7, 'paul',    ext.crypt('HackHub@2026', ext.gen_salt('bf')), 'paul@mail.com',    FALSE ),
( 8, 'sophie',  ext.crypt('HackHub@2026', ext.gen_salt('bf')), 'sophie@mail.com',  FALSE ),
( 9, 'marc',    ext.crypt('HackHub@2026', ext.gen_salt('bf')), 'marc@mail.com',    FALSE ),
(10, 'lina',    ext.crypt('HackHub@2026', ext.gen_salt('bf')), 'lina@mail.com',    FALSE ),
(11, 'techcorp',ext.crypt('HackHub@2026', ext.gen_salt('bf')), 'contact@techcorp.com', FALSE ),
(12, 'datasoft',ext.crypt('HackHub@2026', ext.gen_salt('bf')), 'contact@datasoft.com', FALSE ),
(13, 'alex',    ext.crypt('HackHub@2026', ext.gen_salt('bf')), 'alex@mail.com',    FALSE ),
(14, 'nina',    ext.crypt('HackHub@2026', ext.gen_salt('bf')), 'nina@mail.com',    FALSE );

-- =======
-- Membres du staff
-- =======
INSERT INTO membrestaff (id_memstaf, nom_menstaf, adres_mail, age) VALUES
(1, 'Julie Accueil',   'julie@staff.com',   28),
(2, 'Karim Logistique','karim@staff.com',   35),
(3, 'Emma Technique',  'emma@staff.com',    24);

-- =======
-- Récompenses
-- =======
INSERT INTO recompense (id_recompense, nom_recompense, type_recompense) VALUES
(1, '1er Prix',            'Trophée + 5000€'),
(2, '2e Prix',            'Trophée + 2000€'),
(3, 'Prix Coup de Cœur',  'Bon d''achat 500€'),
(4, 'Prix Innovation',    'Mentorat + visibilité');

-- =======
-- Épreuves (FK -> recompense)
-- =======
INSERT INTO epreuve (id_epreuve, nom_epreuve, nbr_max_equipe, id_recompense) VALUES
(1, 'Défi IA',           10, 1),
(2, 'Hack Web',           8, 2),
(3, 'Challenge Mobile',   6, 3),
(4, 'Data Challenge',     5, 4),
(5, 'Cybersécurité',      5, 1);

-- =======
-- Mentors (FK -> compte)
-- =======
INSERT INTO mentor (id_mentor, nom_mentor, adresse_mail_mentor, specialite, id_compte) VALUES
(1, 'Alex Mentor', 'alex@mail.com', 'Intelligence Artificielle', 13),
(2, 'Nina Guide',  'nina@mail.com', 'UX / Design',               14);

-- =======
-- Organisateurs (FK -> compte)
-- =======
INSERT INTO organisateurhackathon (id_organisateur, nom_org, ad_mail_org, id_compte) VALUES
(1, 'Marie Organisatrice', 'marie@mail.com', 6),
(2, 'Paul Event',          'paul@mail.com',  7);

-- =======
-- Juges (FK -> compte)
-- =======
INSERT INTO juge (id_juge, nom_juge, adresse_mail_juge, specialite, id_compte) VALUES
(1, 'Sophie Tech', 'sophie@mail.com', 'Intelligence Artificielle', 8),
(2, 'Marc Innov',  'marc@mail.com',   'Développement Web',         9),
(3, 'Lina Data',   'lina@mail.com',   'Data Science',             10);

-- =======
-- Partenaires (FK -> compte)
-- =======
INSERT INTO partenaire (id_partenaire, nom_partenaire, contact_partenaire, id_compte) VALUES
(1, 'TechCorp', 'contact@techcorp.com', 11),
(2, 'DataSoft', 'contact@datasoft.com', 12);

-- =======
-- Hackathons (FK -> organisateur)
-- =======
INSERT INTO hackathon (id_hack, nom_hack, theme_hach, heure_deb, date_deb, date_fin, lieu_hack, contact, id_organisateur) VALUES
(1, 'HackInnov 2026', 'Intelligence Artificielle', '09:00', '2026-06-15', '2026-06-17', 'Paris',    'marie@mail.com', 1),
(2, 'WebFest',        'Développement Web',         '10:00', '2026-07-01', '2026-07-02', 'Lyon',     'marie@mail.com', 1),
(3, 'DataChallenge',  'Data & Cloud',              '08:30', '2026-09-10', '2026-09-12', 'Toulouse', 'paul@mail.com',  2);

-- =======
-- Équipes (FK -> hackathon, epreuve)
-- =======
INSERT INTO equipe (id_equipe, nom_equipe, nom_chef, code_equipe, id_hack, id_epreuve) VALUES
(1, 'Les Neurones',  'Max Dupont',  'NEUR01', 1, 1),
(2, 'CodeBreakers',  'Mika Leroy',  'CODE02', 1, 1),
(3, 'PixelPerfect',  'Tom Bernard', 'PIXL03', 2, 2),
(4, 'DataWizards',   'Eva Roux',    'DATA04', 3, 4),
(5, 'SecureForce',   'Sam Blanc',   'SECF05', 1, 5),
(6, 'MobileFirst',   'Lou Faure',   'MOBF06', 2, 3);

-- =======
-- Participants (FK -> equipe, compte nullable)
-- =======
INSERT INTO participant (id_participant, nom_participant, etablissement_participant, mail_participant, id_equipe, id_compte) VALUES
( 1, 'Max Dupont',  'ENSIL',     'max@mail.com',   1, 2),
( 2, 'Alice Martin','INSA',      'alice@mail.com', 1, NULL),
( 3, 'Bob Durand',  'ENSIL',     'bob@mail.com',   1, NULL),
( 4, 'Mika Leroy',  'UTC',       'mika@mail.com',  2, 3),
( 5, 'Chloé Petit', 'UTC',       'chloe@mail.com', 2, NULL),
( 6, 'Tom Bernard', 'EPITECH',   'tom@mail.com',   3, 4),
( 7, 'Dan Moreau',  'EPITECH',   'dan@mail.com',   3, NULL),
( 8, 'Eva Roux',    'Polytech',  'eva@mail.com',   4, 5),
( 9, 'Sam Blanc',   'ENSEEIHT',  'sam@mail.com',   5, NULL),
(10, 'Lou Faure',   'ENSEEIHT',  'lou@mail.com',   6, NULL);

-- =======
-- Avoir : staff <-> hackathon
-- =======
INSERT INTO avoir (id_hack, id_memstaf) VALUES
(1, 1), (1, 2), (2, 2), (3, 3);

-- =======
-- Contenir : épreuves <-> hackathon
-- =======
INSERT INTO contenir (id_hack, id_epreuve) VALUES
(1, 1), (1, 5), (2, 2), (2, 3), (3, 4);

-- =======
-- Noter : notes des juges aux équipes
-- =======
INSERT INTO noter (id_equipe, id_juge, note) VALUES
(1, 1, 17.50), (1, 2, 16.00),
(2, 1, 15.00),
(3, 2, 18.00),
(4, 3, 14.50),
(5, 1, 13.00);

-- =======
-- Sponsoriser : partenaires <-> hackathon
-- =======
INSERT INTO sponsoriser (id_hack, id_partenaire) VALUES
(1, 1), (1, 2), (2, 1), (3, 2);

-- =======
-- Relance les compteurs d'identité au-dessus des ID insérés
-- =======
ALTER TABLE compte               ALTER COLUMN id_compte      RESTART WITH 100;
ALTER TABLE membrestaff          ALTER COLUMN id_memstaf     RESTART WITH 100;
ALTER TABLE recompense           ALTER COLUMN id_recompense  RESTART WITH 100;
ALTER TABLE epreuve              ALTER COLUMN id_epreuve     RESTART WITH 100;
ALTER TABLE mentor               ALTER COLUMN id_mentor      RESTART WITH 100;
ALTER TABLE organisateurhackathon ALTER COLUMN id_organisateur RESTART WITH 100;
ALTER TABLE juge                 ALTER COLUMN id_juge        RESTART WITH 100;
ALTER TABLE partenaire           ALTER COLUMN id_partenaire  RESTART WITH 100;
ALTER TABLE hackathon            ALTER COLUMN id_hack        RESTART WITH 100;
ALTER TABLE equipe               ALTER COLUMN id_equipe      RESTART WITH 100;
ALTER TABLE participant          ALTER COLUMN id_participant RESTART WITH 100;
