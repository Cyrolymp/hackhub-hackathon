
-- Supprime tous les triggers du schema

DO
$code$
   DECLARE
      r RECORD;
   BEGIN
      FOR r IN
         SELECT 'DROP TRIGGER ' || trigger_name || ' ON ' || event_object_table AS sql
			FROM information_schema.triggers t
            WHERE trigger_schema = CURRENT_SCHEMA
            GROUP BY event_object_table, trigger_name
            LOOP
               EXECUTE r.sql;
            END LOOP;
   END;
$code$;


-- Supprime toutes les fonctions du schema

DO
$code$
DECLARE
	r RECORD;
BEGIN
	FOR r IN
		SELECT 'DROP FUNCTION ' || ns.nspname || '.' || proname
            || '(' || oidvectortypes(proargtypes) || ')' AS sql
		FROM pg_proc INNER JOIN pg_namespace ns ON (pg_proc.pronamespace = ns.oid)
		WHERE ns.nspname = current_schema
	LOOP
		EXECUTE r.sql;
	END LOOP;
END;
$code$;


-- Fonctions

CREATE OR REPLACE FUNCTION fn_register_user(
    p_nom           VARCHAR(50),
    p_etablissement VARCHAR(50),
    p_mail          VARCHAR(50)
)
RETURNS INT AS $$
DECLARE
    v_id INT;
BEGIN
    -- Vérifie si l'étudiant existe déjà
    SELECT id_etudiant INTO v_id
    FROM Etudiant
    WHERE addresse_mail_etudiant = p_mail;

    -- S'il n'existe pas, on l'insère
    IF v_id IS NULL THEN
        INSERT INTO Etudiant (Nom_etudiant, Etablissement_etudiant, addresse_mail_etudiant)
        VALUES (p_nom, p_etablissement, p_mail)
        RETURNING id_etudiant INTO v_id;
    END IF;

    RETURN v_id;
END;
$$ LANGUAGE plpgsql;

--utilisation : SELECT fn_register_user('Alice Dupont', 'ENSIL', 'alice@ensil.fr');

CREATE OR REPLACE FUNCTION fn_upsert_score(
    p_id_equipe INT,
    p_id_juge   INT,
    p_note      NUMERIC(5,2)
)
RETURNS VOID AS $$
BEGIN
    -- Vérifie que l'équipe existe
    IF NOT EXISTS (SELECT 1 FROM equipe WHERE id_equipe = p_id_equipe) THEN
        RAISE EXCEPTION 'Équipe % introuvable', p_id_equipe;
    END IF;

    -- Vérifie que le juge existe
    IF NOT EXISTS (SELECT 1 FROM Juge WHERE id_juge = p_id_juge) THEN
        RAISE EXCEPTION 'Juge % introuvable', p_id_juge;
    END IF;

    -- Upsert dans la table Noter
    INSERT INTO Noter (id_equipe, id_juge, note)
    VALUES (p_id_equipe, p_id_juge, p_note)
    ON CONFLICT (id_equipe, id_juge)
    DO UPDATE SET note = EXCLUDED.note;
END;
$$ LANGUAGE plpgsql;

--utilisation: SELECT fn_upsert_score(1, 2, 17.5);

--Ajoute de la column note dans la table Noter

--ALTER TABLE Noter ADD COLUMN note NUMERIC(5,2);



-- Triggers
