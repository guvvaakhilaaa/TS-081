/**
 * EcoImpact Core Application
 * State orchestration, view routing, responsive sidebar, notifications, and XP floaters
 */
const App = {
  currentView: 'dashboard',

  init() {
    this.bindNavigation();
    this.bindMobileMenu();

    // Initialize all modular subsystems
    if (typeof Calculator !== 'undefined') Calculator.init();
    if (typeof Hotspots !== 'undefined') Hotspots.init();
    if (typeof Coach !== 'undefined') Coach.init();
    if (typeof Simulator !== 'undefined') Simulator.init();
    if (typeof Goals !== 'undefined') Goals.init();
    if (typeof Sequestration !== 'undefined') Sequestration.init();
    if (typeof Gamification !== 'undefined') Gamification.init();
    if (typeof Planet !== 'undefined') Planet.init();
    if (typeof Analytics !== 'undefined') Analytics.init();
    if (typeof Demo !== 'undefined') Demo.init();

    // Check URL hash for routing or default to landing
    const initialView = window.location.hash ? window.location.hash.substring(1) : 'landing';
    this.navigateTo(initialView);
  },

  bindNavigation() {
    document.querySelectorAll('[data-view]').forEach(item => {
      item.addEventListener('click', (e) => {
        e.preventDefault();
        const targetView = item.getAttribute('data-view');
        this.navigateTo(targetView);
      });
    });
  },

  bindMobileMenu() {
    const menuBtn = document.getElementById('mobileMenuBtn');
    const sidebar = document.querySelector('.sidebar');
    if (menuBtn && sidebar) {
      menuBtn.addEventListener('click', () => {
        sidebar.classList.toggle('mobile-open');
      });
    }
  },

  navigateTo(viewName) {
    this.currentView = viewName;
    window.location.hash = viewName;

    // Update active state in sidebar
    document.querySelectorAll('.nav-item').forEach(link => {
      if (link.getAttribute('data-view') === viewName) {
        link.classList.add('active');
      } else {
        link.classList.remove('active');
      }
    });

    // Toggle view sections
    document.querySelectorAll('.view-section').forEach(section => {
      if (section.id === `view-${viewName}`) {
        section.classList.add('active');
      } else {
        section.classList.remove('active');
      }
    });

    // Close mobile menu if open
    const sidebar = document.querySelector('.sidebar');
    if (sidebar) sidebar.classList.remove('mobile-open');

    // Update topbar title
    this.updatePageTitle(viewName);

    // Refresh specific view if needed
    if (viewName === 'planet' && typeof Planet !== 'undefined') {
      setTimeout(() => {
        Planet.resizeCanvas();
        Planet.draw();
      }, 100);
    }
  },

  updatePageTitle(view) {
    const titles = {
      landing: { title: "EcoImpact Overview", subtitle: "Understand Your Impact. Reduce Your Carbon. Shape a Greener Future." },
      dashboard: { title: "Gaming Dashboard — EcoQuest", subtitle: "Play Green. Live Clean. Protect Our Planet." },
      calculator: { title: "Activity Input & Carbon Calculator", subtitle: "Measure emissions with transparent scientific factors" },
      hotspots: { title: "Emission Source Hotspots", subtitle: "Algorithm 4: Ranked contribution & opportunity detection" },
      coach: { title: "AI Eco Coach", subtitle: "Algorithms 1 & 2: Rule-based personalized weighted recommendations" },
      simulator: { title: "What-If Carbon Simulator", subtitle: "Algorithm 3: Real-time scenario recalculation and savings" },
      goals: { title: "Reduction Plan & Goals", subtitle: "Algorithm 5: 6-month measurable roadmap & milestones" },
      sequestration: { title: "Carbon Sequestration Module", subtitle: "Biomass, trees, soil & technological carbon storage (44/12 ratio)" },
      missions: { title: "Green Quest Missions", subtitle: "Daily & weekly climate actions with deterministic XP rewards" },
      planet: { title: "My Green Planet", subtitle: "Interactive biosphere evolving from Seedling to Harmonious Biosphere" },
      achievements: { title: "Achievements & Badges", subtitle: "Unlock eco-badges as you hit climate milestones" },
      leaderboard: { title: "Community Eco-Leaderboard", subtitle: "Peer rankings and sustainable climate action" },
      analytics: { title: "Progress & Trend Analytics", subtitle: "Algorithm 6: Historical comparisons & carbon audit CSV" },
      reports: { title: "Executive Environmental Reports", subtitle: "Comprehensive greenhouse gas accounting summary" },
      profile: { title: "User Profile & Preferences", subtitle: "Custom settings, lifestyle defaults, and Demo Mode Reset" },
      onboarding: { title: "Welcome to EcoImpact", subtitle: "Get started with baseline sustainability setup" }
    };

    const info = titles[view] || { title: "EcoImpact", subtitle: "Gamified Carbon Reduction for Climate Action" };
    const h2 = document.getElementById('pageTitleText');
    const p = document.getElementById('pageSubtitleText');
    if (h2) h2.textContent = info.title;
    if (p) p.textContent = info.subtitle;
  },

  triggerXpGain(amount, reason = '') {
    // Sound or visual floater
    const floater = document.createElement('div');
    floater.className = 'xp-floater';
    floater.innerHTML = `<span>+${amount} XP</span> <span style="font-size: 16px;">🌱</span>`;
    document.body.appendChild(floater);

    setTimeout(() => {
      floater.remove();
    }, 1600);
  },

  showToast(message, type = 'success') {
    let container = document.getElementById('toastContainer');
    if (!container) {
      container = document.createElement('div');
      container.id = 'toastContainer';
      container.className = 'toast-container';
      document.body.appendChild(container);
    }

    const toast = document.createElement('div');
    toast.className = 'toast';
    const icon = type === 'success' ? '✓' : (type === 'error' ? '✕' : 'ℹ');
    toast.innerHTML = `<strong style="color: var(--primary-dark); font-size: 15px;">${icon}</strong> <span>${message}</span>`;
    container.appendChild(toast);

    setTimeout(() => {
      toast.style.opacity = '0';
      toast.style.transition = 'opacity 0.3s ease';
      setTimeout(() => toast.remove(), 300);
    }, 3500);
  }
};

// Auto-boot application on DOM ready
document.addEventListener('DOMContentLoaded', () => {
  App.init();
});
