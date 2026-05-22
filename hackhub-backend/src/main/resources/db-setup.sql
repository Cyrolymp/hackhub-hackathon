-- ============================================================
--  HackHub — Script de création de la base de données
--  À exécuter dans DBeaver sur votre serveur PostgreSQL
-- ============================================================

-- 1. Créer la base de données (exécuter en tant que superuser)
CREATE DATABASE hackhub_db
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'fr_FR.UTF-8'
    LC_CTYPE = 'fr_FR.UTF-8'
    TEMPLATE = template0;

-- Si les locales fr_FR ne sont pas disponibles, utiliser :
-- LC_COLLATE = 'en_US.UTF-8'
-- LC_CTYPE = 'en_US.UTF-8'

-- ============================================================
-- 2. Se connecter à hackhub_db, puis exécuter le reste
-- ============================================================

-- NB : Spring Boot avec ddl-auto=update créera automatiquement
-- les tables au premier démarrage. Ce script crée la structure
-- manuellement pour référence et pour insérer les données initiales.

-- ENUM simulés en PostgreSQL
-- (Hibernate les gère comme VARCHAR, pas besoin de CREATE TYPE)

-- Table users
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    prenom VARCHAR(100) NOT NULL,
    nom VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'PARTICIPANT'
        CHECK (role IN ('PARTICIPANT','ORGANISATEUR','JUGE','ADMIN')),
    specialite VARCHAR(150),
    bio VARCHAR(1000),
    actif BOOLEAN NOT NULL DEFAULT TRUE,
    equipe_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table hackathons
CREATE TABLE IF NOT EXISTS hackathons (
    id BIGSERIAL PRIMARY KEY,
    titre VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    categorie VARCHAR(100) NOT NULL,
    lieu VARCHAR(255) NOT NULL,
    date_debut DATE NOT NULL,
    date_fin DATE NOT NULL,
    date_limite_inscription DATE NOT NULL,
    places_max INTEGER NOT NULL,
    taille_equipe_max INTEGER,
    prix_total NUMERIC(12,2),
    description_prix VARCHAR(500),
    technologies VARCHAR(500),
    statut VARCHAR(20) NOT NULL DEFAULT 'BROUILLON'
        CHECK (statut IN ('BROUILLON','OUVERT','EN_COURS','TERMINE','ANNULE')),
    organisateur_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_hackathon_organisateur FOREIGN KEY (organisateur_id) REFERENCES users(id)
);

-- Table equipes
CREATE TABLE IF NOT EXISTS equipes (
    id BIGSERIAL PRIMARY KEY,
    nom VARCHAR(150) NOT NULL,
    code VARCHAR(10) NOT NULL UNIQUE,
    specialite VARCHAR(150),
    taille_max INTEGER DEFAULT 4,
    description VARCHAR(500),
    chef_id BIGINT NOT NULL,
    hackathon_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_equipe_chef FOREIGN KEY (chef_id) REFERENCES users(id),
    CONSTRAINT fk_equipe_hackathon FOREIGN KEY (hackathon_id) REFERENCES hackathons(id) ON DELETE SET NULL
);

-- Clé étrangère users -> equipes (ajout après création des deux tables)
ALTER TABLE users
    ADD CONSTRAINT fk_user_equipe FOREIGN KEY (equipe_id) REFERENCES equipes(id) ON DELETE SET NULL;

-- Index pour les performances
CREATE INDEX IF NOT EXISTS idx_users_email    ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_role     ON users(role);
CREATE INDEX IF NOT EXISTS idx_hack_statut    ON hackathons(statut);
CREATE INDEX IF NOT EXISTS idx_hack_org       ON hackathons(organisateur_id);
CREATE INDEX IF NOT EXISTS idx_equipe_code    ON equipes(code);
CREATE INDEX IF NOT EXISTS idx_equipe_hack    ON equipes(hackathon_id);

-- ============================================================
-- 3. Données initiales
-- ============================================================

-- Compte ADMIN par défaut
-- Mot de passe : Admin@HackHub2025!  (BCrypt hash ci-dessous)
INSERT INTO users (prenom, nom, email, password, role, actif)
VALUES (
    'Admin',
    'HackHub',
    'admin@hackhub.io',
    '$2a$12$LqfB5rOkF/zs5Y3rJfN8xO5b1K4oKh3qU8DmJp2vY9CQE1FI4N1uu',
    'ADMIN',
    TRUE
) ON CONFLICT (email) DO NOTHING;

-- Organisateur de démo
-- Mot de passe : Orga@2025!
INSERT INTO users (prenom, nom, email, password, role, specialite, actif)
VALUES (
    'Jean',
    'Organisateur',
    'orga@hackhub.io',
    '$2a$12$3XhRzL7sOkqNhWVEoHo4sOKVv3I9bIK1FzFkQJmVXg2R5t9y2KVKK',
    'ORGANISATEUR',
    'TechCorp France',
    TRUE
) ON CONFLICT (email) DO NOTHING;

-- Participant de démo
-- Mot de passe : User@2025!
INSERT INTO users (prenom, nom, email, password, role, specialite, actif)
VALUES (
    'Marie',
    'Dupont',
    'marie@hackhub.io',
    '$2a$12$5z3JkT9OmVn4XQ1CgRp8LOuGj2F8sNkI9DpXzT0bL3oX7u5m3KRSK',
    'PARTICIPANT',
    'Développement Frontend',
    TRUE
) ON CONFLICT (email) DO NOTHING;

-- ============================================================
-- 4. Vérification
-- ============================================================
SELECT id, prenom, nom, email, role, actif FROM users ORDER BY id;

-- ============================================================
-- MEMO COMPTES DE DÉMO
-- ============================================================
-- Admin    : admin@hackhub.io      / Admin@HackHub2025!
-- Orga     : orga@hackhub.io       / Orga@2025!
-- Participant: marie@hackhub.io    / User@2025!
--
-- IMPORTANT : Changez ces mots de passe en production !
-- Les hashes BCrypt ci-dessus sont générés avec strength=12
-- ============================================================
