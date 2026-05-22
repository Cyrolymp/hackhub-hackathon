-- =====================================================
--  HackHub — Script SQL PostgreSQL
--  À exécuter dans DBeaver pour créer la base de données
-- =====================================================

-- 1. Créer la base de données (exécuter séparément si besoin)
-- CREATE DATABASE hackhub_db;

-- 2. Se connecter à hackhub_db puis exécuter le reste

-- =====================================================
--  TABLES (Spring auto-crée via ddl-auto=update,
--  mais voici le script complet pour référence)
-- =====================================================

CREATE TABLE IF NOT EXISTS utilisateurs (
    id              BIGSERIAL PRIMARY KEY,
    prenom          VARCHAR(100) NOT NULL,
    nom             VARCHAR(100) NOT NULL,
    email           VARCHAR(255) NOT NULL UNIQUE,
    mot_de_passe    VARCHAR(255) NOT NULL,
    specialite      VARCHAR(200),
    bio             TEXT,
    role            VARCHAR(20)  NOT NULL DEFAULT 'PARTICIPANT',
    actif           BOOLEAN      NOT NULL DEFAULT TRUE,
    equipe_id       BIGINT,
    created_at      TIMESTAMP DEFAULT NOW(),
    updated_at      TIMESTAMP DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS equipes (
    id          BIGSERIAL PRIMARY KEY,
    nom         VARCHAR(200) NOT NULL,
    code        VARCHAR(10)  NOT NULL UNIQUE,
    specialite  VARCHAR(200),
    description TEXT,
    taille_max  INTEGER NOT NULL DEFAULT 4,
    chef_id     BIGINT  NOT NULL,
    created_at  TIMESTAMP DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS hackathons (
    id               BIGSERIAL PRIMARY KEY,
    titre            VARCHAR(300) NOT NULL,
    description      TEXT,
    categorie        VARCHAR(100) NOT NULL,
    lieu             VARCHAR(200),
    date_debut       TIMESTAMP    NOT NULL,
    date_fin         TIMESTAMP    NOT NULL,
    places_max       INTEGER      NOT NULL DEFAULT 100,
    prix_recompense  VARCHAR(100),
    statut           VARCHAR(20)  NOT NULL DEFAULT 'OUVERT',
    organisateur_id  BIGINT       NOT NULL,
    created_at       TIMESTAMP DEFAULT NOW(),
    updated_at       TIMESTAMP DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS hackathon_technologies (
    hackathon_id  BIGINT       NOT NULL,
    technologie   VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS inscriptions (
    id              BIGSERIAL PRIMARY KEY,
    equipe_id       BIGINT      NOT NULL,
    hackathon_id    BIGINT      NOT NULL,
    statut          VARCHAR(20) NOT NULL DEFAULT 'VALIDEE',
    commentaire     TEXT,
    date_inscription TIMESTAMP DEFAULT NOW(),
    updated_at       TIMESTAMP DEFAULT NOW(),
    UNIQUE (equipe_id, hackathon_id)
);

-- =====================================================
--  CONTRAINTES FK
-- =====================================================
ALTER TABLE utilisateurs
    ADD CONSTRAINT fk_utilisateurs_equipe
    FOREIGN KEY (equipe_id) REFERENCES equipes(id) ON DELETE SET NULL;

ALTER TABLE equipes
    ADD CONSTRAINT fk_equipes_chef
    FOREIGN KEY (chef_id) REFERENCES utilisateurs(id);

ALTER TABLE hackathons
    ADD CONSTRAINT fk_hackathons_organisateur
    FOREIGN KEY (organisateur_id) REFERENCES utilisateurs(id);

ALTER TABLE hackathon_technologies
    ADD CONSTRAINT fk_hack_tech_hackathon
    FOREIGN KEY (hackathon_id) REFERENCES hackathons(id) ON DELETE CASCADE;

ALTER TABLE inscriptions
    ADD CONSTRAINT fk_inscriptions_equipe
    FOREIGN KEY (equipe_id) REFERENCES equipes(id) ON DELETE CASCADE;

ALTER TABLE inscriptions
    ADD CONSTRAINT fk_inscriptions_hackathon
    FOREIGN KEY (hackathon_id) REFERENCES hackathons(id) ON DELETE CASCADE;

-- =====================================================
--  INDEX
-- =====================================================
CREATE INDEX IF NOT EXISTS idx_utilisateurs_email ON utilisateurs(email);
CREATE INDEX IF NOT EXISTS idx_utilisateurs_role  ON utilisateurs(role);
CREATE INDEX IF NOT EXISTS idx_equipes_code       ON equipes(code);
CREATE INDEX IF NOT EXISTS idx_hackathons_statut  ON hackathons(statut);
CREATE INDEX IF NOT EXISTS idx_inscriptions_hack  ON inscriptions(hackathon_id);
CREATE INDEX IF NOT EXISTS idx_inscriptions_equipe ON inscriptions(equipe_id);

-- =====================================================
--  DONNÉES INITIALES — Admin
-- =====================================================
-- Mot de passe admin : Admin1234! (BCrypt)
INSERT INTO utilisateurs (prenom, nom, email, mot_de_passe, role, actif)
VALUES (
    'Admin',
    'HackHub',
    'admin@hackhub.com',
    '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.',
    'ADMIN',
    TRUE
) ON CONFLICT (email) DO NOTHING;

SELECT 'Schema HackHub créé avec succès !' AS message;
