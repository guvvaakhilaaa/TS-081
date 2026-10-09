/**
 * EcoImpact What-If Carbon Simulator (Algorithm 3)
 * Dynamic interactive scenario testing with real-time recalculation and comparison
 */
const Simulator = {
  currentResult: null,

  init() {
    this.bindEvents();
    this.runSimulation(); // Initial calculation
  },

  bindEvents() {
    const inputs = [
      'simTransitDays', 'simTransportReduction', 'simElectricityReduction',
      'simDietType', 'simFoodWasteReduction', 'simRecyclePct', 'simComposting'
    ];

    inputs.forEach(id => {
      const el = document.getElementById(id);
      if (el) {
        el.addEventListener('input', () => {
          this.updateDisplayValues();
          this.runSimulation();
        });
      }
    });

    const sendToPlanBtn = document.getElementById('btnSendSimToPlan');
    if (sendToPlanBtn) {
      sendToPlanBtn.addEventListener('click', () => {
        if (this.currentResult) {
          Goals.prefillFromSimulation(this.currentResult);
          App.navigateTo('goals');
          App.showToast('Scenario ported into your reduction plan!', 'success');
        }
      });
    }
  },

  updateDisplayValues() {
    const setVal = (id, val, suffix = '') => {
      const el = document.getElementById(id);
      if (el) el.textContent = val + suffix;
    };

    setVal('valTransitDays', document.getElementById('simTransitDays')?.value || '2', ' days/wk');
    setVal('valTransportReduction', document.getElementById('simTransportReduction')?.value || '30', '%');
    setVal('valElectricityReduction', document.getElementById('simElectricityReduction')?.value || '20', '%');
    setVal('valFoodWasteReduction', document.getElementById('simFoodWasteReduction')?.value || '50', '%');
    setVal('valRecyclePct', document.getElementById('simRecyclePct')?.value || '60', '%');
  },

  getParams() {
    return {
      scenarioName: document.getElementById('simScenarioName')?.value || 'Proposed Lifestyle Shift',
      publicTransitDaysPerWeek: parseInt(document.getElementById('simTransitDays')?.value) || 2,
      transportReductionPercent: parseFloat(document.getElementById('simTransportReduction')?.value) || 30.0,
      electricityReductionPercent: parseFloat(document.getElementById('simElectricityReduction')?.value) || 20.0,
      proposedDietType: document.getElementById('simDietType')?.value || 'VEGETARIAN_DIET',
      foodWasteReductionPercent: parseFloat(document.getElementById('simFoodWasteReduction')?.value) || 50.0,
      proposedRecyclingPercent: parseFloat(document.getElementById('simRecyclePct')?.value) || 60.0,
      enableComposting: document.getElementById('simComposting')?.checked || true,
      shoppingReductionPercent: 20.0
    };
  },

  async runSimulation() {
    const params = this.getParams();
    try {
      const result = await API.runSimulation(params);
      this.currentResult = result;
      this.renderResults(result);
    } catch (err) {
      console.error('Simulation error', err);
    }
  },

  renderResults(res) {
    if (!res) return;

    // Metric Badges
    const baseKgEl = document.getElementById('simBaselineKg');
    const propKgEl = document.getElementById('simProposedKg');
    const redKgEl = document.getElementById('simReductionKg');
    const redPctEl = document.getElementById('simReductionPct');
    const annualTonnesEl = document.getElementById('simAnnualAvoidedTonnes');
    const savingsEl = document.getElementById('simMonthlySavings');
    const summaryTextEl = document.getElementById('simSummaryText');

    if (baseKgEl) baseKgEl.textContent = Number(res.baselineEmissionsKg).toFixed(1) + ' kg';
    if (propKgEl) propKgEl.textContent = Number(res.proposedEmissionsKg).toFixed(1) + ' kg';
    if (redKgEl) redKgEl.textContent = '-' + Number(res.reductionKg).toFixed(1) + ' kg';
    if (redPctEl) redPctEl.textContent = Number(res.reductionPercentage).toFixed(1) + '%';
    if (annualTonnesEl) annualTonnesEl.textContent = Number(res.annualAvoidedTonnes).toFixed(3) + ' tonnes/yr';
    if (savingsEl) savingsEl.textContent = '$' + Number(res.estimatedMonthlySavingsUsd).toFixed(2);
    if (summaryTextEl) summaryTextEl.textContent = res.simulationSummary || '';

    // Category comparison table/bars
    const container = document.getElementById('simComparisonBars');
    if (container && res.categoryComparisons) {
      container.innerHTML = res.categoryComparisons.map(cat => {
        const base = parseFloat(cat.baselineKg) || 0;
        const prop = parseFloat(cat.proposedKg) || 0;
        const red = parseFloat(cat.reductionKg) || 0;
        const pct = parseFloat(cat.percentageChange) || 0;

        return `
          <div style="margin-bottom: 14px; background: #F8FAF7; padding: 12px; border-radius: 8px; border: 1px solid var(--border-green);">
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 6px;">
              <strong>${cat.category}</strong>
              <span style="font-weight: 700; color: ${red > 0 ? '#16A34A' : '#64748B'}; font-size: 13px;">
                ${red > 0 ? `-${red.toFixed(1)} kg (${pct.toFixed(0)}% reduction)` : 'Unchanged'}
              </span>
            </div>
            <div style="display: flex; align-items: center; gap: 10px; font-size: 12px; color: var(--text-muted);">
              <span>Baseline: ${base.toFixed(1)} kg</span>
              <span>→</span>
              <strong style="color: var(--primary-dark);">Proposed: ${prop.toFixed(1)} kg</strong>
            </div>
          </div>
        `;
      }).join('');
    }
  }
};
