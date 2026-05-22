// ============================================================
//  HackHub — app.js  (partagé entre index.html et dashboard)
// ============================================================

// ---- Modal helpers ----
function openModal(id) {
  document.querySelectorAll('.modal-overlay').forEach(m => m.classList.remove('active'));
  document.getElementById(id).classList.add('active');
}
function closeModal(id) {
  document.getElementById(id).classList.remove('active');
}
function closeAll() {
  document.querySelectorAll('.modal-overlay').forEach(m => m.classList.remove('active'));
}
function goBack(from, to) {
  closeModal(from);
  openModal(to);
}

// Close on backdrop click
document.addEventListener('DOMContentLoaded', () => {
  document.querySelectorAll('.modal-overlay').forEach(overlay => {
    overlay.addEventListener('click', function (e) {
      if (e.target === this) this.classList.remove('active');
    });
  });
  generateCode();
});

// ---- Show/hide password ----
function togglePwd(inputId, btn) {
  const input = document.getElementById(inputId);
  const icon = btn.querySelector('i');
  if (input.type === 'password') {
    input.type = 'text';
    icon.className = 'bi bi-eye-slash';
  } else {
    input.type = 'password';
    icon.className = 'bi bi-eye';
  }
}

// ---- Password strength ----
function checkStrength() {
  const pwd = document.getElementById('reg-pwd')?.value || '';
  const fill = document.getElementById('pwd-strength-fill');
  const label = document.getElementById('pwd-strength-label');
  if (!fill || !label) return;
  let score = 0;
  if (pwd.length >= 8) score++;
  if (/[A-Z]/.test(pwd)) score++;
  if (/[0-9]/.test(pwd)) score++;
  if (/[^a-zA-Z0-9]/.test(pwd)) score++;
  const levels = [
    { pct: '0%',   color: '',          text: '' },
    { pct: '25%',  color: '#ef4444',   text: 'Très faible' },
    { pct: '50%',  color: '#f97316',   text: 'Faible' },
    { pct: '75%',  color: '#fbbf24',   text: 'Moyen' },
    { pct: '100%', color: '#00D4AA',   text: 'Fort ✓' },
  ];
  fill.style.width = levels[score].pct;
  fill.style.background = levels[score].color;
  label.textContent = levels[score].text;
  label.style.color = levels[score].color;
}

// ---- Auth tabs ----
function switchTab(tab) {
  document.getElementById('form-login').style.display    = tab === 'login' ? 'block' : 'none';
  document.getElementById('form-register').style.display = tab === 'register' ? 'block' : 'none';
  document.getElementById('tab-login').classList.toggle('active', tab === 'login');
  document.getElementById('tab-register').classList.toggle('active', tab === 'register');
}

// ---- Login Participant ----
function loginParticipant() {
  const email = document.getElementById('login-email').value.trim();
  const pwd   = document.getElementById('login-pwd').value.trim();
  if (!email || !pwd) { alert('Veuillez remplir tous les champs.'); return; }

  // Simulation : en prod, remplacer par un appel API
  const stored = JSON.parse(localStorage.getItem('hh_user') || '{}');
  if (stored.email === email && stored.pwd === pwd) {
    sessionStorage.setItem('hh_session', JSON.stringify(stored));
    closeAll();
    goToDashboard();
  } else {
    alert('Email ou mot de passe incorrect.');
  }
}

// ---- Register Participant ----
function registerParticipant() {
  const prenom = document.getElementById('reg-prenom').value.trim();
  const nom    = document.getElementById('reg-nom').value.trim();
  const email  = document.getElementById('reg-email').value.trim();
  const pwd    = document.getElementById('reg-pwd').value.trim();
  const pwd2   = document.getElementById('reg-pwd2').value.trim();
  const spec   = document.getElementById('reg-specialite').value;

  if (!prenom || !nom || !email || !pwd) { alert('Veuillez remplir les champs obligatoires (*).'); return; }
  if (pwd !== pwd2) { alert('Les mots de passe ne correspondent pas.'); return; }
  if (pwd.length < 8) { alert('Le mot de passe doit contenir au moins 8 caractères.'); return; }

  // Sauvegarde locale (à remplacer par API)
  const user = { prenom, nom, email, pwd, specialite: spec };
  localStorage.setItem('hh_user', JSON.stringify(user));
  sessionStorage.setItem('hh_session', JSON.stringify(user));

  closeAll();
  // Affiche le choix d'équipe
  if (document.getElementById('welcome-name')) {
    document.getElementById('welcome-name').textContent = prenom;
  }
  openModal('modal-team-choice');
}

// ---- Login Organisateur ----
function loginOrganisateur() {
  const email = document.getElementById('org-email').value.trim();
  const pwd   = document.getElementById('org-pwd').value.trim();
  if (!email || !pwd) { alert('Veuillez remplir tous les champs.'); return; }
  // Simulation
  alert('Connexion organisateur — à connecter avec le backend.\n\nEmail : ' + email);
}

// ---- Redirect to dashboard ----
function goToDashboard() {
  window.location.href = 'dashboard.html';
}

// ---- Logout ----
function logout() {
  sessionStorage.removeItem('hh_session');
  window.location.href = 'index.html';
}

// ---- Team helpers ----
function generateCode() {
  const el = document.getElementById('teamCode');
  if (!el) return;
  const chars = 'ABCDEFGHJKLMNPQRSTUVWXYZ23456789';
  let code = 'TEAM-';
  for (let i = 0; i < 4; i++) code += chars[Math.floor(Math.random() * chars.length)];
  el.value = code;
}

function openTeamForm(type) {
  closeAll();
  openModal(type === 'create' ? 'modal-create-team' : 'modal-join-team');
}

function submitCreateTeam() {
  const team = document.getElementById('teamName').value.trim();
  const code = document.getElementById('teamCode').value.trim();
  if (!team || !code) { alert('Veuillez remplir les champs obligatoires (*).'); return; }

  // Sauvegarde équipe
  const teamData = {
    name: team,
    code,
    specialite: document.getElementById('teamSpeciality').value,
    size: document.getElementById('teamSize').value,
    desc: document.getElementById('teamDesc')?.value || '',
    role: 'chef',
  };
  sessionStorage.setItem('hh_team', JSON.stringify(teamData));

  document.getElementById('success-title').textContent = `Équipe "${team}" créée !`;
  document.getElementById('success-msg').textContent = 'Partagez ce code avec vos coéquipiers pour qu\'ils vous rejoignent.';
  document.getElementById('success-code').textContent = code;
  closeAll();
  openModal('modal-success');
}

function submitJoinTeam() {
  const code = document.getElementById('joinCode').value.trim();
  if (!code) { alert('Entrez un code d\'équipe.'); return; }

  const teamData = { code, role: 'membre' };
  sessionStorage.setItem('hh_team', JSON.stringify(teamData));

  document.getElementById('success-title').textContent = 'Vous avez rejoint l\'équipe !';
  document.getElementById('success-msg').textContent = `Bienvenue dans l'équipe ${code} !`;
  document.getElementById('success-code').textContent = code;
  closeAll();
  openModal('modal-success');
}
