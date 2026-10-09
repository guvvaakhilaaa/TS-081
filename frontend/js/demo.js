/**
 * EcoImpact 10-Step Hackathon Demonstration Workflow Controller
 * Guides evaluators and hackathon judges sequentially through the full end-to-end PBL workflow
 */
const Demo = {
  currentStep: 1,
  totalSteps: 10,

  steps: [
    {
      step: 1,
      title: "Step 1 of 10: Guided Activity Input",
      desc: "Review or enter baseline activity data across Transport, Energy, Food, Shopping, and Waste.",
      view: "calculator",
      action() {
        // Pre-fill realistic hackathon demo inputs
        const setVal = (id, v) => { const el = document.getElementById(id); if (el) el.value = v; };
        setVal('calcDistanceKm', 450);
        setVal('calcMileage', 15);
        setVal('calcFuelLitres', 30);
        setVal('calcPublicTransitKm', 60);
        setVal('calcElectricityKwh', 240);
        setVal('calcLpgKg', 14.2);
        setVal('calcDietType', 'MEDIUM_MEAT_DIET');
        setVal('calcFoodWasteKg', 3.5);
        setVal('calcClothingItems', 2);
        setVal('calcGoodsSpend', 45);
        setVal('calcWasteKg', 20);
        setVal('calcRecyclePct', 25);
      }
    },
    {
      step: 2,
      title: "Step 2 of 10: Carbon Footprint Calculation",
      desc: "Trigger the scientific carbon calculation engine. Notice transparent formulas and kg CO₂e units.",
      view: "calculator",
      action() {
        Calculator.handleCalculation();
      }
    },
    {
      step: 3,
      title: "Step 3 of 10: Emission Hotspot Detection (Algorithm 4)",
      desc: "Algorithm 4 ranks emission sources by calculated contribution and flags residential & food hotspots.",
      view: "hotspots",
      action() {
        Hotspots.loadHotspots();
      }
    },
    {
      step: 4,
      title: "Step 4 of 10: AI Eco Coach & Weighted Ranking (Algorithms 1 & 2)",
      desc: "View personalized recommendations scored by: (0.4×Impact) + (0.25×Feasibility) + (0.2×Pref) + (0.15×Afford).",
      view: "coach",
      action() {
        Coach.loadRecommendations();
      }
    },
    {
      step: 5,
      title: "Step 5 of 10: What-If Carbon Simulator (Algorithm 3)",
      desc: "Adjust lifestyle parameters (e.g. 2 days transit, 20% electricity saving) and see real-time delta.",
      view: "simulator",
      action() {
        const setVal = (id, v) => { const el = document.getElementById(id); if (el) el.value = v; };
        setVal('simTransitDays', 3);
        setVal('simTransportReduction', 35);
        setVal('simElectricityReduction', 25);
        Simulator.updateDisplayValues();
        Simulator.runSimulation();
      }
    },
    {
      step: 6,
      title: "Step 6 of 10: Baseline vs Proposed Comparison",
      desc: "Compare emissions reductions and review estimated financial utility savings ($/month).",
      view: "simulator",
      action() {
        // Highlight comparison cards
        const card = document.getElementById('simComparisonBars');
        if (card) card.scrollIntoView({ behavior: 'smooth' });
      }
    },
    {
      step: 7,
      title: "Step 7 of 10: 6-Month Reduction Plan & Milestones (Algorithm 5)",
      desc: "Activate a structured 6-month carbon reduction target and inspect monthly milestone checkpoints.",
      view: "goals",
      action() {
        Goals.handleCreateGoal();
      }
    },
    {
      step: 8,
      title: "Step 8 of 10: Green Quest & Gamified Missions (Algorithm 7)",
      desc: "Complete eligible climate missions and trigger interactive XP rewards without duplicate claims.",
      view: "missions",
      action() {
        Gamification.loadProfile();
      }
    },
    {
      step: 9,
      title: "Step 9 of 10: My Green Planet Evolution",
      desc: "Watch the interactive eco-island canvas dynamically evolve through Level 1 to Level 5.",
      view: "planet",
      action() {
        if (typeof Planet !== 'undefined') {
          Planet.setStage(3); // Elevate stage for presentation
        }
      }
    },
    {
      step: 10,
      title: "Step 10 of 10: Audit Progress & Export CSV Report",
      desc: "Review historical decarbonization trajectory and download carbon audit CSV report for evaluation.",
      view: "analytics",
      action() {
        Analytics.loadProgress();
      }
    }
  ],

  init() {
    this.bindEvents();
    this.updateBanner();
  },

  bindEvents() {
    const nextBtn = document.getElementById('btnDemoNext');
    const prevBtn = document.getElementById('btnDemoPrev');
    const resetBtn = document.getElementById('btnDemoReset');

    if (nextBtn) nextBtn.addEventListener('click', () => this.nextStep());
    if (prevBtn) prevBtn.addEventListener('click', () => this.prevStep());
    if (resetBtn) resetBtn.addEventListener('click', () => this.resetDemo());
  },

  nextStep() {
    if (this.currentStep < this.totalSteps) {
      this.currentStep++;
      this.executeCurrentStep();
    } else {
      App.showToast('🎉 Hackathon demonstration flow completed successfully!', 'success');
    }
  },

  prevStep() {
    if (this.currentStep > 1) {
      this.currentStep--;
      this.executeCurrentStep();
    }
  },

  executeCurrentStep() {
    const curr = this.steps[this.currentStep - 1];
    if (!curr) return;

    this.updateBanner();
    App.navigateTo(curr.view);
    if (typeof curr.action === 'function') {
      setTimeout(() => curr.action(), 150);
    }
  },

  updateBanner() {
    const curr = this.steps[this.currentStep - 1];
    if (!curr) return;

    const badgeEl = document.getElementById('demoBannerBadge');
    const textEl = document.getElementById('demoBannerText');

    if (badgeEl) badgeEl.textContent = `Step ${this.currentStep} / ${this.totalSteps}`;
    if (textEl) textEl.textContent = `${curr.title}: ${curr.desc}`;
  },

  async resetDemo() {
    App.showToast('Resetting demonstration environment...', 'info');
    try {
      await API.resetDemo();
      this.currentStep = 1;
      this.executeCurrentStep();
      await Gamification.loadProfile();
      App.showToast('Demo environment reset to standard initial benchmark state!', 'success');
    } catch (err) {
      console.error(err);
    }
  }
};
