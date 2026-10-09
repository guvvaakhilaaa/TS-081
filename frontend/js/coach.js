/**
 * EcoImpact AI Eco Coach Module (Algorithms 1 & 2)
 * Rule-Based Recommendation Engine & Weighted Scoring Visualization
 */
const Coach = {
  recommendations: [],

  async init() {
    this.bindEvents();
    await this.loadRecommendations();
  },

  bindEvents() {
    const filterSelect = document.getElementById('coachCategoryFilter');
    if (filterSelect) {
      filterSelect.addEventListener('change', () => this.filterAndRender());
    }

    const refreshBtn = document.getElementById('btnRefreshRecommendations');
    if (refreshBtn) {
      refreshBtn.addEventListener('click', async () => {
        App.showToast('Re-running weighted recommendation engine...', 'info');
        const res = await API.request('/recommendations/generate?userId=' + API.userId, { method: 'POST' });
        this.recommendations = res || [];
        this.filterAndRender();
        App.showToast('Recommendations freshly generated!', 'success');
      });
    }
  },

  async loadRecommendations() {
    try {
      const res = await API.getRecommendations();
      this.recommendations = res || [];
      this.filterAndRender();
    } catch (err) {
      console.error('Failed to load recommendations', err);
    }
  },

  filterAndRender() {
    const filter = document.getElementById('coachCategoryFilter')?.value || 'ALL';
    let filtered = this.recommendations;
    if (filter !== 'ALL') {
      filtered = filtered.filter(r => r.category === filter);
    }
    this.renderCards(filtered);
  },

  renderCards(items) {
    const container = document.getElementById('recommendationsList');
    if (!container) return;

    if (!items || items.length === 0) {
      container.innerHTML = `
        <div class="card" style="text-align: center; padding: 40px;">
          <div style="font-size: 36px; margin-bottom: 10px;">🌱</div>
          <h3>No recommendations available yet</h3>
          <p style="color: var(--text-muted); margin-top: 6px;">Calculate your carbon footprint first so the AI coach can analyze your emission hotspots!</p>
          <button class="btn btn-primary" style="margin-top: 16px;" onclick="App.navigateTo('calculator')">Go to Calculator</button>
        </div>
      `;
      return;
    }

    container.innerHTML = items.map(rec => {
      const isAccepted = rec.status === 'ACCEPTED';
      const isCompleted = rec.status === 'COMPLETED';

      return `
        <div class="rec-card ${isCompleted ? 'badge-newly-unlocked' : ''}">
          <div class="rec-card-top">
            <div class="rec-meta">
              <span class="rec-badge-cat">${rec.category}</span>
              <span class="rec-badge-score">Scoring Index: ${(rec.rankingScore * 100).toFixed(0)} / 100</span>
              <span style="font-size: 11px; font-weight: 700; color: ${this.getDifficultyColor(rec.difficultyLevel)}">
                Difficulty: ${rec.difficultyLevel}
              </span>
            </div>
            <div>
              <span class="nav-badge" style="background: ${this.getStatusBg(rec.status)}; color: #23412B;">
                ${rec.status || 'SUGGESTED'}
              </span>
            </div>
          </div>

          <h3 class="rec-title">${rec.title}</h3>
          <p class="rec-description">${rec.description}</p>

          <div style="font-size: 12px; background: #F0FDF4; border-left: 3px solid #22C55E; padding: 8px 12px; margin-bottom: 12px; border-radius: 4px;">
            <strong>Why Recommended:</strong> ${rec.reasoning}
          </div>

          <div class="rec-metrics-row">
            <div class="rec-metric-item">
              <div class="rec-metric-value">${Number(rec.estimatedReductionKg).toFixed(1)} kg</div>
              <div class="rec-metric-label">Estimated CO₂e Saved</div>
            </div>
            <div class="rec-metric-item">
              <div class="rec-metric-value">$${Number(rec.potentialCostSavingsUsd).toFixed(2)}</div>
              <div class="rec-metric-label">Est. Monthly Savings</div>
            </div>
            <div class="rec-metric-item">
              <div class="rec-metric-value">${rec.implementationTime || '1 week'}</div>
              <div class="rec-metric-label">Time to Implement</div>
            </div>
          </div>

          ${rec.actionSteps ? `
            <div class="rec-action-steps">
              <strong>Action Checklist:</strong>
              ${rec.actionSteps}
            </div>
          ` : ''}

          <div class="rec-footer">
            <div style="font-size: 11px; color: var(--text-light);">
              Formula: (0.40×Impact) + (0.25×Feasibility) + (0.20×Pref) + (0.15×Afford)
            </div>
            <div style="display: flex; gap: 8px;">
              ${!isCompleted ? `
                <button class="btn btn-primary btn-sm" onclick="Coach.markComplete(${rec.id})">
                  ✓ Mark Completed (+60 XP)
                </button>
              ` : `
                <span style="color: #16A34A; font-weight: 700; font-size: 13px;">✓ Action Completed</span>
              `}
              ${!isAccepted && !isCompleted ? `
                <button class="btn btn-secondary btn-sm" onclick="Coach.accept(${rec.id})">
                  Add to Reduction Plan
                </button>
              ` : ''}
            </div>
          </div>
        </div>
      `;
    }).join('');
  },

  async accept(id) {
    try {
      await API.updateRecommendationStatus(id, 'ACCEPTED');
      App.showToast('Added action to your reduction roadmap!', 'success');
      this.loadRecommendations();
    } catch (err) {
      console.error(err);
    }
  },

  async markComplete(id) {
    try {
      await API.updateRecommendationStatus(id, 'COMPLETED');
      App.triggerXpGain(60, 'Completed Eco Coach Action');
      App.showToast('Congratulations! Marked action complete and earned +60 Eco XP.', 'success');
      this.loadRecommendations();
      Gamification.loadProfile();
    } catch (err) {
      console.error(err);
    }
  },

  getDifficultyColor(diff) {
    if (diff === 'LOW') return '#15803D';
    if (diff === 'HIGH') return '#B91C1C';
    return '#B45309';
  },

  getStatusBg(status) {
    if (status === 'COMPLETED') return '#86EFAC';
    if (status === 'ACCEPTED') return '#BAE6FD';
    return '#E8F5E9';
  }
};
