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


-- Inscrit un participant (ou retourne l'id existant si le mail est déjà connu)
CREATE OR REPLACE FUNCTION fn_register_user(
    p_nom           VARCHAR(50),
    p_etablissement VARCHAR(50),
    p_mail          VARCHAR(50),
    p_id_equipe     BIGINT
)
RETURNS BIGINT AS $$
DECLARE
    v_id BIGINT;
BEGIN
    SELECT id_Participant INTO v_id
    FROM Participant
    WHERE mail_Participant = p_mail;
    IF v_id IS NULL THEN
        INSERT INTO Participant (nom_participant, etablissement_Participant, mail_Participant, id_Equipe)
        VALUES (p_nom, p_etablissement, p_mail, p_id_equipe)
        RETURNING id_Participant INTO v_id;
    END IF;
    RETURN v_id;
END;
$$ LANGUAGE plpgsql;


-- Insère ou met à jour la note d'une équipe attribuée par un juge
CREATE OR REPLACE FUNCTION fn_upsert_score(
    p_id_equipe BIGINT,
    p_id_juge   BIGINT,
    p_note      NUMERIC(5,2)
)
RETURNS VOID AS $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM Equipe WHERE id_Equipe = p_id_equipe) THEN
        RAISE EXCEPTION 'Équipe % introuvable', p_id_equipe;
    END IF;
    IF NOT EXISTS (SELECT 1 FROM Juge WHERE id_Juge = p_id_juge) THEN
        RAISE EXCEPTION 'Juge % introuvable', p_id_juge;
    END IF;
    INSERT INTO Noter (id_Equipe, id_Juge, note)
    VALUES (p_id_equipe, p_id_juge, p_note)
    ON CONFLICT (id_Equipe, id_Juge)
    DO UPDATE SET note = EXCLUDED.note;
END;
$$ LANGUAGE plpgsql;


-- Triggers (à ajouter en S2)