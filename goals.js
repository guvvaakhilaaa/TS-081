/**
 * EcoImpact Goal Planning Module (Algorithm 5)
 * Sets measurable reduction targets, generates progressive milestones, and tracks progress
 */
const Goals = {
  currentPlan: null,

  init() {
    this.bindEvents();
    this.loadGoalsAndPlans();
  },

  bindEvents() {
    const form = document.getElementById('goalForm');
    if (form) {
      form.addEventListener('submit', (e) => {
        e.preventDefault();
        this.handleCreateGoal();
      });
    }

    const targetPctSlider = document.getElementById('goalTargetPct');
    if (targetPctSlider) {
      targetPctSlider.addEventListener('input', () => {
        const valEl = document.getElementById('valGoalTargetPct');
        if (valEl) valEl.textContent = targetPctSlider.value + '%';
        this.updateLiveCalculations();
      });
    }
  },

  prefillFromSimulation(simResult) {
    const slider = document.getElementById('goalTargetPct');
    const valEl = document.getElementById('valGoalTargetPct');
    if (slider && simResult.reductionPercentage) {
      const pct = Math.min(50, Math.max(5, Math.round(simResult.reductionPercentage)));
      slider.value = pct;
      if (valEl) valEl.textContent = pct + '%';
      this.updateLiveCalculations();
    }
  },

  updateLiveCalculations() {
    const baseline = 542.76; // Default baseline kg
    const pct = parseFloat(document.getElementById('goalTargetPct')?.value) || 20;
    const targetEmissions = baseline * (1 - pct / 100);
    const reductionKg = baseline - targetEmissions;

    const baseEl = document.getElementById('goalCalcBaseline');
    const targetEl = document.getElementById('goalCalcTarget');
    const redEl = document.getElementById('goalCalcReduction');

    if (baseEl) baseEl.textContent = baseline.toFixed(1) + ' kg';
    if (targetEl) targetEl.textContent = targetEmissions.toFixed(1) + ' kg';
    if (redEl) redEl.textContent = reductionKg.toFixed(1) + ' kg';
  },

  async handleCreateGoal() {
    const targetPct = parseFloat(document.getElementById('goalTargetPct')?.value) || 20;
    const durationMonths = parseInt(document.getElementById('goalDurationMonths')?.value) || 6;
    const title = document.getElementById('goalTitle')?.value || `Reduce Carbon Footprint by ${targetPct}%`;

    try {
      App.showToast('Generating personalized reduction roadmap...', 'info');
      const res = await API.createGoal({
        title,
        targetPercentage: targetPct,
        durationMonths
      });
      this.currentPlan = res;
      this.renderPlan(res);
      App.showToast('Reduction Plan activated! First milestone unlocked.', 'success');
      App.triggerXpGain(75, 'Activated Reduction Roadmap');
      Gamification.loadProfile();
    } catch (err) {
      console.error(err);
    }
  },

  async loadGoalsAndPlans() {
    this.updateLiveCalculations();
    try {
      const plans = await API.getPlans();
      if (plans && plans.length > 0) {
        // Render latest active plan milestones
        const latest = plans[0];
        const res = await API.createGoal({
          title: latest.planTitle,
          targetPercentage: latest.targetReductionPercentage,
          durationMonths: latest.durationMonths
        });
        this.renderPlan(res);
      }
    } catch (err) {
      console.error(err);
    }
  },

  renderPlan(data) {
    if (!data) return;

    const planContainer = document.getElementById('activePlanContainer');
    if (planContainer) planContainer.style.display = 'block';

    const titleEl = document.getElementById('activePlanTitle');
    const targetPctEl = document.getElementById('activePlanTargetPct');
    const targetEmissionsEl = document.getElementById('activePlanTargetEmissions');
    const reductionAmountEl = document.getElementById('activePlanReductionAmount');

    if (titleEl) titleEl.textContent = data.plan?.planTitle || 'Active Climate Reduction Plan';
    if (targetPctEl) targetPctEl.textContent = (data.targetPercentage || 20) + '%';
    if (targetEmissionsEl) targetEmissionsEl.textContent = Number(data.targetEmissionsKg || 0).toFixed(1) + ' kg CO₂e';
    if (reductionAmountEl) reductionAmountEl.textContent = '-' + Number(data.targetKgReduction || 0).toFixed(1) + ' kg CO₂e';

    // Render 6-Month Milestones Roadmap
    const milestonesEl = document.getElementById('planMilestonesRoadmap');
    if (milestonesEl && data.milestones) {
      milestonesEl.innerHTML = data.milestones.map((m, idx) => `
        <div style="display: flex; gap: 16px; margin-bottom: 18px; position: relative;">
          <div style="display: flex; flex-direction: column; align-items: center;">
            <div style="width: 32px; height: 32px; border-radius: 50%; background: ${idx === 0 ? 'var(--primary-dark)' : 'var(--soft-mint)'}; color: ${idx === 0 ? '#FFFFFF' : 'var(--primary-dark)'}; display: flex; align-items: center; justify-content: center; font-weight: 800; font-size: 13px; z-index: 2;">
              M${m.monthNumber}
            </div>
            ${idx < data.milestones.length - 1 ? `
              <div style="width: 2px; flex: 1; background: var(--border-green); margin-top: 4px;"></div>
            ` : ''}
          </div>
          <div style="flex: 1; background: #FFFFFF; border: 1px solid var(--border-green); border-radius: var(--radius-md); padding: 14px; box-shadow: var(--shadow-sm);">
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 4px;">
              <strong style="color: var(--primary-dark); font-size: 14px;">Month ${m.monthNumber} Target: -${Number(m.cumulativeKgReductionTarget).toFixed(1)} kg (${Number(m.cumulativePercentageTarget).toFixed(1)}%)</strong>
              <span class="nav-badge" style="background: ${idx === 0 ? '#DCFCE7' : '#F1F5F9'}; color: #23412B;">
                ${idx === 0 ? 'Current Phase' : 'Upcoming'}
              </span>
            </div>
            <p style="font-size: 12px; color: var(--text-muted);">${m.strategicFocus}</p>
          </div>
        </div>
      `).join('');
    }
  }
};
