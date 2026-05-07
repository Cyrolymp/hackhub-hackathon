Sub Create_Tables()

DoCmd.RunSQL "CREATE TABLE MembreStaff(" & _
   "id_memstaf COUNTER," & _
   "nom_menstaf VARCHAR(50) NOT NULL," & _
   "adres_mail VARCHAR(50) NOT NULL," & _
   "Age INT," & _
   "PRIMARY KEY(id_memstaf)" & _
");"   

DoCmd.RunSQL "CREATE TABLE Recompense(" & _
   "id_recompense COUNTER," & _
   "nom_recompense VARCHAR(50) NOT NULL," & _
   "type_recompense VARCHAR(50) NOT NULL," & _
   "PRIMARY KEY(id_recompense)" & _
");"   

DoCmd.RunSQL "CREATE TABLE Compte(" & _
   "id_compte INT," & _
   "fonction VARCHAR(50) NOT NULL," & _
   "pseudo VARCHAR(50) NOT NULL," & _
   "email VARCHAR(50) NOT NULL," & _
   "role_admin VARCHAR(50) NOT NULL," & _
   "empreinte_mdp VARCHAR(100) NOT NULL," & _
   "PRIMARY KEY(id_compte)," & _
   "UNIQUE(pseudo)," & _
   "UNIQUE(email)," & _
   "UNIQUE(empreinte_mdp)" & _
");"   

DoCmd.RunSQL "CREATE TABLE Mentor(" & _
   "id_mentor INT," & _
   "Nom_mentor VARCHAR(50) NOT NULL," & _
   "Adresse_mail_mentor VARCHAR(50) NOT NULL," & _
   "Specialite VARCHAR(50) NOT NULL," & _
   "id_compte INT," & _
   "PRIMARY KEY(id_mentor)," & _
   "UNIQUE(id_compte)," & _
   "FOREIGN KEY(id_compte) REFERENCES Compte(id_compte)" & _
");"   

DoCmd.RunSQL "CREATE TABLE OrganisateurHackathon(" & _
   "id_organisateur COUNTER," & _
   "nom_org VARCHAR(50) NOT NULL," & _
   "Ad_mail_org VARCHAR(50) NOT NULL," & _
   "id_compte INT NOT NULL," & _
   "PRIMARY KEY(id_organisateur)," & _
   "UNIQUE(id_compte)," & _
   "FOREIGN KEY(id_compte) REFERENCES Compte(id_compte)" & _
");"   

DoCmd.RunSQL "CREATE TABLE Hackathon(" & _
   "id_hack COUNTER," & _
   "nom_Hack VARCHAR(50) NOT NULL," & _
   "theme_Hach VARCHAR(50) NOT NULL," & _
   "heure_Deb TIME NOT NULL," & _
   "date_Deb DATE," & _
   "date_fin DATE," & _
   "lieu_hack VARCHAR(50) NOT NULL," & _
   "contact VARCHAR(50)," & _
   "id_organisateur INT NOT NULL," & _
   "PRIMARY KEY(id_hack)," & _
   "FOREIGN KEY(id_organisateur) REFERENCES OrganisateurHackathon(id_organisateur)" & _
");"   

DoCmd.RunSQL "CREATE TABLE Epreuve(" & _
   "id_Epreuve COUNTER," & _
   "nom_epreuve VARCHAR(50) NOT NULL," & _
   "nbr_max_Equipe INT NOT NULL," & _
   "id_recompense INT," & _
   "PRIMARY KEY(id_Epreuve)," & _
   "FOREIGN KEY(id_recompense) REFERENCES Recompense(id_recompense)" & _
");"   

DoCmd.RunSQL "CREATE TABLE Equipe(" & _
   "id_Equipe COUNTER," & _
   "nom_Equipe VARCHAR(50) NOT NULL," & _
   "nom_chef VARCHAR(50) NOT NULL," & _
   "code_Equipe VARCHAR(50)," & _
   "id_Epreuve INT NOT NULL," & _
   "id_hack INT NOT NULL," & _
   "PRIMARY KEY(id_Equipe)," & _
   "FOREIGN KEY(id_Epreuve) REFERENCES Epreuve(id_Epreuve)," & _
   "FOREIGN KEY(id_hack) REFERENCES Hackathon(id_hack)" & _
");"   

DoCmd.RunSQL "CREATE TABLE Participant(" & _
   "id_Participant COUNTER," & _
   "nom_participant VARCHAR(50)," & _
   "etablissement_Participant VARCHAR(50)," & _
   "mail_Participant VARCHAR(50) NOT NULL," & _
   "id_Equipe INT NOT NULL," & _
   "PRIMARY KEY(id_Participant)," & _
   "FOREIGN KEY(id_Equipe) REFERENCES Equipe(id_Equipe)" & _
");"   

DoCmd.RunSQL "CREATE TABLE Juge(" & _
   "id_Juge COUNTER," & _
   "nom_juge VARCHAR(50) NOT NULL," & _
   "Adresse_mail_juge VARCHAR(50) NOT NULL," & _
   "Specialite VARCHAR(50) NOT NULL," & _
   "id_compte INT NOT NULL," & _
   "PRIMARY KEY(id_Juge)," & _
   "UNIQUE(id_compte)," & _
   "FOREIGN KEY(id_compte) REFERENCES Compte(id_compte)" & _
");"   

DoCmd.RunSQL "CREATE TABLE Partenaire(" & _
   "id_Partenaire COUNTER," & _
   "nom_partenaire VARCHAR(50) NOT NULL," & _
   "Contact_partenaire VARCHAR(50) NOT NULL," & _
   "id_compte INT NOT NULL," & _
   "PRIMARY KEY(id_Partenaire)," & _
   "UNIQUE(id_compte)," & _
   "FOREIGN KEY(id_compte) REFERENCES Compte(id_compte)" & _
");"   

DoCmd.RunSQL "CREATE TABLE Possede(" & _
   "id_hack INT," & _
   "id_memstaf INT," & _
   "PRIMARY KEY(id_hack, id_memstaf)," & _
   "FOREIGN KEY(id_hack) REFERENCES Hackathon(id_hack)," & _
   "FOREIGN KEY(id_memstaf) REFERENCES MembreStaff(id_memstaf)" & _
");"   

DoCmd.RunSQL "CREATE TABLE Contenir(" & _
   "id_hack INT," & _
   "id_Epreuve INT," & _
   "PRIMARY KEY(id_hack, id_Epreuve)," & _
   "FOREIGN KEY(id_hack) REFERENCES Hackathon(id_hack)," & _
   "FOREIGN KEY(id_Epreuve) REFERENCES Epreuve(id_Epreuve)" & _
");"   

DoCmd.RunSQL "CREATE TABLE Noter(" & _
   "id_Equipe INT," & _
   "id_Juge INT," & _
   "note DOUBLE NOT NULL NUMERIC (5,2)," & _
   "PRIMARY KEY(id_Equipe, id_Juge)," & _
   "FOREIGN KEY(id_Equipe) REFERENCES Equipe(id_Equipe)," & _
   "FOREIGN KEY(id_Juge) REFERENCES Juge(id_Juge)" & _
");"   

DoCmd.RunSQL "CREATE TABLE Sponsoriser(" & _
   "id_hack INT," & _
   "id_Partenaire INT," & _
   "PRIMARY KEY(id_hack, id_Partenaire)," & _
   "FOREIGN KEY(id_hack) REFERENCES Hackathon(id_hack)," & _
   "FOREIGN KEY(id_Partenaire) REFERENCES Partenaire(id_Partenaire)" & _
");"   

End Sub