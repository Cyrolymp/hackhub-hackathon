// ============================================================
//  HackHub — dashboard.js
// ============================================================

const HACKATHONS = [
  {
    id: 1,
    title: 'HackIA 2025 — Paris',
    org: 'TechCorp France',
    categorie: 'IA & Machine Learning',
    color: 'c-purple',
    date_debut: '14 Fév 2025',
    date_fin: '16 Fév 2025',
    lieu: 'Paris, France',
    places: 200,
    inscrits: 148,
    prix: '10 000 €',
    statut: 'ouvert',
    desc: 'Relevez des défis liés à l\'intelligence artificielle et au machine learning. Construisez des solutions innovantes en 48h et présentez-les à un jury d\'experts.',
    tags: ['Python', 'TensorFlow', 'Computer Vision'],
  },
  {
    id: 2,
    title: 'CryptoHack Lyon',
    org: 'DecentLab',
    categorie: 'Web3 & Blockchain',
    color: 'c-teal',
    date_debut: '28 Fév 2025',
    date_fin: '02 Mar 2025',
    lieu: 'Lyon, France',
    places: 120,
    inscrits: 87,
    prix: '7 500 €',
    statut: 'ouvert',
    desc: 'Explorez le monde de la blockchain, des smart contracts et de la finance décentralisée. Créez des applications Web3 révolutionnaires en équipe.',
    tags: ['Solidity', 'Ethereum', 'DeFi'],
  },
  {
    id: 3,
    title: 'EcoHack Bordeaux',
    org: 'GreenStartup',
    categorie: 'Green Tech',
    color: 'c-amber',
    date_debut: '07 Mar 2025',
    date_fin: '09 Mar 2025',
    lieu: 'Bordeaux, France',
    places: 80,
    inscrits: 80,
    prix: '5 000 €',
    statut: 'ouvert',
    desc: 'Hackathon dédié aux solutions technologiques pour l\'environnement. IoT, data environnementale, apps mobiles — tout est permis pour sauver la planète.',
    tags: ['IoT', 'Data', 'Mobile'],
  },
  {
    id: 4,
    title: 'DevSecOps Challenge',
    org: 'CyberFrance',
    categorie: 'Cybersécurité',
    color: 'c-pink',
    date_debut: '21 Mar 2025',
    date_fin: '22 Mar 2025',
    lieu: 'Marseille, France',
    places: 60,
    inscrits: 23,
    prix: '8 000 €',
    statut: 'ouvert',
    desc: 'Affrontez des challenges de sécurité informatique réels : CTF, pentest, forensics et durcissement d\'infrastructure. Pour les passionnés de cybersécurité.',
    tags: ['CTF', 'Pentest', 'Linux'],
  },
  {
    id: 5,
    title: 'HealthTech Hackathon',
    org: 'MedInnovate',
    categorie: 'Santé & Tech',
    color: 'c-blue',
    date_debut: '10 Jan 2025',
    date_fin: '12 Jan 2025',
    lieu: 'Toulouse, France',
    places: 100,
    inscrits: 100,
    prix: '12 000 €',
    statut: 'en-cours',
    desc: 'Créez des solutions numériques pour améliorer le secteur de la santé : applications pour patients, outils de diagnostic IA, gestion hospitalière.',
    tags: ['IA Médicale', 'React', 'API Santé'],
  },
  {
    id: 6,
    title: 'GameJam Nantes',
    org: 'GameDev FR',
    categorie: 'Game Development',
    color: 'c-red',
    date_debut: '05 Déc 2024',
    date_fin: '07 Déc 2024',
    lieu: 'Nantes, France',
    places: 150,
    inscrits: 150,
    prix: '3 000 €',
    statut: 'termine',
    desc: 'Créez un jeu vidéo complet en 48h à partir d\'un thème imposé. Unity, Godot, Pygame... tous les moteurs sont acceptés !',
    tags: ['Unity', 'Godot', 'Pixel Art'],
  },
  {
    id: 7,
    title: 'EdTech Sprint',
    org: 'LearnLab',
    categorie: 'Éducation & Tech',
    color: 'c-purple',
    date_debut: '04 Avr 2025',
    date_fin: '06 Avr 2025',
    lieu: 'Lille, France',
    places: 90,
    inscrits: 12,
    prix: '6 000 €',
    statut: 'ouvert',
    desc: 'Repensez l\'éducation avec la technologie ! Plateformes d\'apprentissage adaptatif, outils pour enseignants, gamification pédagogique — libérez votre créativité.',
    tags: ['EdTech', 'UX/UI', 'IA'],
  },
  {
    id: 8,
    title: 'FinHack Paris',
    org: 'BNP Paribas x Station F',
    categorie: 'FinTech',
    color: 'c-teal',
    date_debut: '18 Avr 2025',
    date_fin: '19 Avr 2025',
    lieu: 'Paris, France',
    places: 200,
    inscrits: 67,
    prix: '15 000 €',
    statut: 'ouvert',
    desc: 'Le plus grand hackathon FinTech de France. Paiements, open banking, gestion de patrimoine IA — innovez dans la finance de demain avec le soutien de BNP Paribas.',
    tags: ['Open Banking', 'API', 'Paiement'],
  },
];

let currentFilter = 'tous';
let currentSearch = '';

// ---- Init ----
document.addEventListener('DOMContentLoaded', () => {
  loadUserSession();
  renderHackathons();
  setupSidebarNav();
  loadTeamState();
  loadProfileData();
  generateDashCode();
});

// ---- Load user session ----
function loadUserSession() {
  const session = JSON.parse(sessionStorage.getItem('hh_session') || '{}');
  const name = session.prenom ? `${session.prenom} ${session.nom || ''}`.trim() : 'Participant';
  const initial = name.charAt(0).toUpperCase();

  const el = (id) => document.getElementById(id);
  if (el('sidebar-name'))    el('sidebar-name').textContent = name;
  if (el('sidebar-avatar'))  el('sidebar-avatar').textContent = initial;
  if (el('profile-avatar-big')) el('profile-avatar-big').textContent = initial;
  if (el('profile-fullname'))   el('profile-fullname').textContent = name;
  if (el('profile-email'))      el('profile-email').textContent = session.email || '';
  if (el('profile-specialite')) el('profile-specialite').textContent = session.specialite || 'Participant';
  if (el('pf-prenom'))   el('pf-prenom').value = session.prenom || '';
  if (el('pf-nom'))      el('pf-nom').value    = session.nom    || '';
  if (el('pf-email'))    el('pf-email').value  = session.email  || '';
}

// ---- Render hackathons ----
function renderHackathons() {
  const grid = document.getElementById('hackathon-grid');
  if (!grid) return;

  const filtered = HACKATHONS.filter(h => {
    const matchFilter = currentFilter === 'tous' || h.statut === currentFilter;
    const matchSearch = h.title.toLowerCase().includes(currentSearch.toLowerCase())
      || h.org.toLowerCase().includes(currentSearch.toLowerCase())
      || h.categorie.toLowerCase().includes(currentSearch.toLowerCase());
    return matchFilter && matchSearch;
  });

  if (filtered.length === 0) {
    grid.innerHTML = `<div class="col-12 text-center" style="padding:3rem;color:var(--text-muted);">
      <i class="bi bi-search" style="font-size:2rem;display:block;margin-bottom:0.75rem;opacity:0.4;"></i>
      Aucun hackathon trouvé pour cette recherche.
    </div>`;
    return;
  }

  grid.innerHTML = filtered.map(h => {
    const pct = Math.round((h.inscrits / h.places) * 100);
    const statusHtml = {
      ouvert:   `<span class="status-badge status-ouvert"><i class="bi bi-circle-fill" style="font-size:0.5rem;"></i> Inscriptions ouvertes</span>`,
      'en-cours': `<span class="status-badge status-en-cours"><i class="bi bi-play-fill"></i> En cours</span>`,
      termine:  `<span class="status-badge status-termine"><i class="bi bi-check-circle-fill"></i> Terminé</span>`,
    }[h.statut] || '';

    const btnLabel  = h.statut === 'ouvert' ? "S'inscrire" : h.statut === 'en-cours' ? 'Suivre' : 'Voir les résultats';
    const btnClass  = h.statut === 'termine' ? 'disabled' : '';
    const btnAction = h.statut !== 'termine' ? `onclick="openHackDetail(${h.id})"` : '';

    const tags = h.tags.map(t => `<span class="meta-pill">${t}</span>`).join('');

    return `
    <div class="col-md-6 col-lg-4" data-statut="${h.statut}">
      <div class="hack-card-dash ${h.color}">
        <div class="d-flex justify-content-between align-items-start">
          <span class="hack-tag">${h.categorie}</span>
          ${statusHtml}
        </div>
        <h6>${h.title}</h6>
        <p class="hack-org"><i class="bi bi-building me-1"></i>${h.org}</p>
        <p class="hack-desc">${h.desc}</p>
        <div class="hack-card-meta">
          <span class="meta-pill"><i class="bi bi-calendar3"></i> ${h.date_debut}</span>
          <span class="meta-pill"><i class="bi bi-geo-alt"></i> ${h.lieu}</span>
          <span class="meta-pill"><i class="bi bi-trophy"></i> ${h.prix}</span>
        </div>
        <div class="hack-card-meta">${tags}</div>
        <!-- Progress bar places -->
        <div>
          <div style="display:flex;justify-content:space-between;font-size:0.7rem;color:var(--text-muted);margin-bottom:4px;">
            <span>${h.inscrits} inscrits</span><span>${h.places} places</span>
          </div>
          <div style="height:4px;background:rgba(255,255,255,0.08);border-radius:2px;overflow:hidden;">
            <div style="height:100%;width:${pct}%;background:${pct >= 100 ? '#ef4444' : 'var(--primary)'};border-radius:2px;"></div>
          </div>
        </div>
        <button class="btn-s-inscrire ${btnClass}" ${btnAction}>${btnLabel}</button>
      </div>
    </div>`;
  }).join('');
}

// ---- Filter ----
function filterBy(type, btn) {
  currentFilter = type;
  document.querySelectorAll('.filter-btn').forEach(b => b.classList.remove('active'));
  btn.classList.add('active');
  renderHackathons();
}

function filterHackathons() {
  currentSearch = document.getElementById('search-input').value;
  renderHackathons();
}

// ---- Hackathon detail modal ----
function openHackDetail(id) {
  const h = HACKATHONS.find(x => x.id === id);
  if (!h) return;

  const colorMap = { 'c-purple': 'var(--primary)', 'c-teal': 'var(--accent)', 'c-amber': '#fbbf24', 'c-pink': '#f472b6', 'c-blue': '#38bdf8', 'c-red': '#f87171' };
  const color = colorMap[h.color] || 'var(--primary)';
  const pct = Math.round((h.inscrits / h.places) * 100);
  const tags = h.tags.map(t => `<span class="meta-pill">${t}</span>`).join('');

  document.getElementById('hack-detail-content').innerHTML = `
    <div class="hack-detail-banner" style="background:${color};"></div>
    <span class="hack-tag" style="margin-bottom:0.75rem;display:inline-block;">${h.categorie}</span>
    <h4 class="hack-detail-title">${h.title}</h4>
    <p class="hack-detail-org"><i class="bi bi-building me-1"></i>${h.org}</p>
    <div class="hack-detail-meta">
      <span class="meta-pill"><i class="bi bi-calendar3"></i> ${h.date_debut} → ${h.date_fin}</span>
      <span class="meta-pill"><i class="bi bi-geo-alt"></i> ${h.lieu}</span>
      <span class="meta-pill"><i class="bi bi-people"></i> ${h.inscrits}/${h.places} places</span>
    </div>
    <p class="hack-detail-desc">${h.desc}</p>
    <div class="prize-box">
      <i class="bi bi-trophy-fill"></i>
      <div><div class="prize-label">Prix à gagner</div><div class="prize-amount">${h.prix}</div></div>
    </div>
    <div class="hack-card-meta mb-3">${tags}</div>
    <div style="margin-bottom:1rem;">
      <div style="display:flex;justify-content:space-between;font-size:0.72rem;color:var(--text-muted);margin-bottom:5px;">
        <span>Places disponibles</span><span>${h.places - h.inscrits} restantes</span>
      </div>
      <div style="height:6px;background:rgba(255,255,255,0.08);border-radius:3px;overflow:hidden;">
        <div style="height:100%;width:${pct}%;background:${pct >= 100 ? '#ef4444' : color};border-radius:3px;"></div>
      </div>
    </div>
    <button class="btn-submit" onclick="inscrireHackathon(${h.id})">
      <i class="bi bi-rocket-takeoff-fill me-2"></i>S'inscrire au hackathon
    </button>
  `;
  openModal('modal-hack-detail');
}

function inscrireHackathon(id) {
  const h = HACKATHONS.find(x => x.id === id);
  closeModal('modal-hack-detail');
  alert(`✅ Inscription à "${h.title}" enregistrée !\n\nVous recevrez une confirmation par email.`);
}

// ---- Sidebar nav ----
function setupSidebarNav() {
  document.querySelectorAll('.sidebar-link').forEach(link => {
    link.addEventListener('click', function(e) {
      e.preventDefault();
      const section = this.dataset.section;
      document.querySelectorAll('.sidebar-link').forEach(l => l.classList.remove('active'));
      this.classList.add('active');
      document.querySelectorAll('.content-section').forEach(s => s.classList.remove('active'));
      document.getElementById(`section-${section}`)?.classList.add('active');

      const titles = { hackathons: ['Hackathons disponibles', 'Trouvez votre prochain défi'], equipe: ['Mon Équipe', 'Gérez vos coéquipiers'], profil: ['Mon Profil', 'Vos informations personnelles'], ressources: ['Ressources', 'Guides, outils et formations'] };
      document.getElementById('page-title').textContent = titles[section]?.[0] || '';
      document.getElementById('page-sub').textContent   = titles[section]?.[1] || '';

      // Mobile: close sidebar after nav
      if (window.innerWidth < 768) {
        document.getElementById('sidebar').classList.remove('open');
      }
    });
  });
}

// ---- Sidebar toggle ----
function toggleSidebar() {
  const sidebar = document.getElementById('sidebar');
  const main = document.querySelector('.main-content');
  if (window.innerWidth >= 768) {
    sidebar.classList.toggle('collapsed');
    main.classList.toggle('expanded');
  } else {
    sidebar.classList.toggle('open');
  }
}

// ---- Team state ----
function loadTeamState() {
  const team = JSON.parse(sessionStorage.getItem('hh_team') || 'null');
  const user = JSON.parse(sessionStorage.getItem('hh_session') || '{}');

  const statTeam = document.getElementById('stat-team');
  if (team) {
    if (statTeam) statTeam.textContent = team.name || team.code;
    document.getElementById('no-team-state').style.display = 'none';
    document.getElementById('team-info-state').style.display = 'block';
    document.getElementById('team-display-name').textContent = team.name || 'Mon équipe';
    document.getElementById('team-display-code').textContent = team.code;

    const membersList = document.getElementById('team-members-list');
    const memberName = `${user.prenom || 'Moi'} ${user.nom || ''}`.trim();
    membersList.innerHTML = `
      <div class="member-row">
        <div class="member-av" style="background:var(--accent);color:#0B0E1A;">${memberName.charAt(0)}</div>
        <div><div class="member-name">${memberName} <span style="font-size:0.7rem;color:var(--accent);">(Vous)</span></div><div class="member-spec">${user.specialite || 'Participant'}</div></div>
        <div class="ms-auto"><span class="team-role-badge" style="font-size:0.7rem;">${team.role === 'chef' ? 'Chef d\'équipe' : 'Membre'}</span></div>
      </div>
      <div class="member-row" style="opacity:0.4;">
        <div class="member-av" style="background:var(--dark);border:1px dashed var(--border);color:var(--text-muted);"><i class="bi bi-plus"></i></div>
        <div><div class="member-name" style="color:var(--text-muted);">Place disponible</div><div class="member-spec">En attente d'un membre</div></div>
      </div>`;
  } else {
    if (statTeam) statTeam.textContent = 'Aucune';
  }
}

// ---- Profile ----
function loadProfileData() {
  const session = JSON.parse(sessionStorage.getItem('hh_session') || '{}');
  const spec = document.getElementById('pf-specialite');
  if (spec && session.specialite) {
    for (let o of spec.options) { if (o.value === session.specialite) { o.selected = true; break; } }
  }
}

function saveProfile() {
  const session = JSON.parse(sessionStorage.getItem('hh_session') || '{}');
  session.prenom     = document.getElementById('pf-prenom').value.trim();
  session.nom        = document.getElementById('pf-nom').value.trim();
  session.email      = document.getElementById('pf-email').value.trim();
  session.specialite = document.getElementById('pf-specialite').value;
  sessionStorage.setItem('hh_session', JSON.stringify(session));
  localStorage.setItem('hh_user', JSON.stringify(session));
  loadUserSession();
  alert('Profil mis à jour avec succès !');
}

// ---- Team modals (dashboard) ----
function openTeamModal(type) {
  openModal(type === 'create' ? 'dash-modal-create' : 'dash-modal-join');
}

function generateDashCode() {
  const el = document.getElementById('d-teamCode');
  if (!el) return;
  const chars = 'ABCDEFGHJKLMNPQRSTUVWXYZ23456789';
  let code = 'TEAM-';
  for (let i = 0; i < 4; i++) code += chars[Math.floor(Math.random() * chars.length)];
  el.value = code;
}

function submitDashCreateTeam() {
  const team = document.getElementById('d-teamName').value.trim();
  const code = document.getElementById('d-teamCode').value.trim();
  if (!team || !code) { alert('Veuillez remplir les champs obligatoires.'); return; }
  const teamData = { name: team, code, specialite: document.getElementById('d-teamSpeciality').value, size: document.getElementById('d-teamSize').value, role: 'chef' };
  sessionStorage.setItem('hh_team', JSON.stringify(teamData));
  closeModal('dash-modal-create');
  loadTeamState();
  // Switch to equipe section
  document.querySelector('[data-section="equipe"]').click();
}

function submitDashJoinTeam() {
  const code = document.getElementById('d-joinCode').value.trim();
  if (!code) { alert('Entrez un code d\'équipe.'); return; }
  const teamData = { code, role: 'membre' };
  sessionStorage.setItem('hh_team', JSON.stringify(teamData));
  closeModal('dash-modal-join');
  loadTeamState();
  document.querySelector('[data-section="equipe"]').click();
}
