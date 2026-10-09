/**
 * EcoImpact Calculator Module
 * Activity form submission, validation, and transparent carbon footprint calculation display
 */
const Calculator = {
  currentData: null,

  init() {
    this.bindEvents();
  },

  bindEvents() {
    const form = document.getElementById('activityForm');
    if (form) {
      form.addEventListener('submit', (e) => {
        e.preventDefault();
        this.handleCalculation();
      });
    }

    // Tab buttons inside the calculator form
    document.querySelectorAll('.calc-tab-btn').forEach(btn => {
      btn.addEventListener('click', (e) => {
        document.querySelectorAll('.calc-tab-btn').forEach(b => b.classList.remove('active'));
        document.querySelectorAll('.calc-tab-content').forEach(c => c.style.display = 'none');
        
        btn.classList.add('active');
        const targetId = btn.getAttribute('data-target');
        const target = document.getElementById(targetId);
        if (target) target.style.display = 'block';
      });
    });

    // Auto calculate fuel when distance or mileage changes
    const distInput = document.getElementById('calcDistanceKm');
    const mileageInput = document.getElementById('calcMileage');
    const fuelInput = document.getElementById('calcFuelLitres');

    const updateFuelEstimate = () => {
      const dist = parseFloat(distInput?.value) || 0;
      const mileage = parseFloat(mileageInput?.value) || 15;
      if (dist > 0 && mileage > 0 && fuelInput) {
        fuelInput.value = (dist / mileage).toFixed(2);
      }
    };

    if (distInput) distInput.addEventListener('input', updateFuelEstimate);
    if (mileageInput) mileageInput.addEventListener('input', updateFuelEstimate);
  },

  getFormData() {
    return {
      reportingPeriod: document.getElementById('calcReportingPeriod')?.value || 'MONTHLY',
      vehicleType: document.getElementById('calcVehicleType')?.value || 'PETROL_CAR',
      distanceKm: parseFloat(document.getElementById('calcDistanceKm')?.value) || 0,
      fuelType: document.getElementById('calcFuelType')?.value || 'PETROL',
      mileageKmPerLitre: parseFloat(document.getElementById('calcMileage')?.value) || 15.0,
      fuelConsumedLitres: parseFloat(document.getElementById('calcFuelLitres')?.value) || 0,
      publicTransportKm: parseFloat(document.getElementById('calcPublicTransitKm')?.value) || 0,
      flightKm: parseFloat(document.getElementById('calcFlightKm')?.value) || 0,
      evEnergyKwh: parseFloat(document.getElementById('calcEvEnergyKwh')?.value) || 0,
      
      electricityKwh: parseFloat(document.getElementById('calcElectricityKwh')?.value) || 0,
      gridRegion: document.getElementById('calcGridRegion')?.value || 'India National Grid',
      lpgCylindersOrKg: parseFloat(document.getElementById('calcLpgKg')?.value) || 0,
      naturalGasKwh: parseFloat(document.getElementById('calcNaturalGasKwh')?.value) || 0,

      dietType: document.getElementById('calcDietType')?.value || 'MEDIUM_MEAT_DIET',
      meatServingsPerWeek: parseInt(document.getElementById('calcMeatServings')?.value) || 4,
      dairyServingsPerWeek: parseInt(document.getElementById('calcDairyServings')?.value) || 7,
      foodWasteKg: parseFloat(document.getElementById('calcFoodWasteKg')?.value) || 0,

      clothingItemsBought: parseInt(document.getElementById('calcClothingItems')?.value) || 0,
      electronicsBought: parseInt(document.getElementById('calcElectronics')?.value) || 0,
      generalGoodsSpendUsd: parseFloat(document.getElementById('calcGoodsSpend')?.value) || 0,

      wasteGeneratedKg: parseFloat(document.getElementById('calcWasteKg')?.value) || 0,
      recyclingPercentage: parseFloat(document.getElementById('calcRecyclePct')?.value) || 0,
      compostingActive: document.getElementById('calcComposting')?.checked || false,

      isIndustrial: document.getElementById('calcIsIndustrial')?.checked || false,
      industrialEnergyKwh: parseFloat(document.getElementById('calcIndEnergy')?.value) || 0,
      industrialFuelLitres: parseFloat(document.getElementById('calcIndFuel')?.value) || 0
    };
  },

  async handleCalculation() {
    const data = this.getFormData();
    App.showToast('Calculating scientifically verified footprint...', 'info');

    try {
      const result = await API.calculateCarbon(data);
      this.currentData = result;
      this.renderResults(result);
      App.showToast('Footprint calculated successfully!', 'success');
      App.triggerXpGain(40, 'Logged Carbon Activity');

      // Refresh downstream modules
      Hotspots.loadHotspots();
      Coach.loadRecommendations();
      Gamification.loadProfile();
      Analytics.loadProgress();
    } catch (err) {
      console.error(err);
      App.showToast('Error during calculation. Please check inputs.', 'error');
    }
  },

  renderResults(res) {
    const summaryCard = document.getElementById('calculationResultSummary');
    if (summaryCard) summaryCard.style.display = 'block';

    const totalKgEl = document.getElementById('resTotalKg');
    if (totalKgEl) totalKgEl.textContent = Number(res.totalFootprintKg || 0).toFixed(2);

    const totalTonnesEl = document.getElementById('resTotalTonnes');
    if (totalTonnesEl) totalTonnesEl.textContent = Number(res.totalFootprintTonnes || 0).toFixed(4);

    const annualTonnesEl = document.getElementById('resAnnualTonnes');
    if (annualTonnesEl) annualTonnesEl.textContent = Number(res.annualProjectionTonnes || 0).toFixed(2);

    const largestCatEl = document.getElementById('resLargestCategory');
    if (largestCatEl) largestCatEl.textContent = res.largestCategory || 'N/A';

    // Render category cards with scientific formula explanations
    const listEl = document.getElementById('resCategoryBreakdownList');
    if (listEl && res.categories) {
      listEl.innerHTML = res.categories.map(cat => `
        <div class="card" style="margin-bottom: 12px; padding: 16px;">
          <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 8px;">
            <div style="display: flex; align-items: center; gap: 8px;">
              <span class="rec-badge-cat">${cat.category}</span>
              <strong style="font-size: 15px;">${Number(cat.emissionsKg).toFixed(2)} kg CO₂e</strong>
            </div>
            <span style="font-weight: 800; font-size: 14px; color: var(--primary-dark);">${Number(cat.percentageContribution).toFixed(1)}%</span>
          </div>
          <div class="progress-bar-container" style="margin-bottom: 8px;">
            <div class="progress-bar-fill" style="width: ${cat.percentageContribution}%;"></div>
          </div>
          <div style="font-size: 11px; color: var(--text-muted); background: var(--bg-nature); padding: 8px; border-radius: 6px;">
            <div><strong>Applied Formula:</strong> ${cat.formulaApplied || 'Quantity × Emission Factor'}</div>
            <div style="margin-top: 2px;"><strong>Source Factor:</strong> ${cat.keyFactorDescription || 'IPCC / CEA / DEFRA Baseline'}</div>
          </div>
        </div>
      `).join('');
    }

    // Scroll smoothly to results
    if (summaryCard) {
      summaryCard.scrollIntoView({ behavior: 'smooth' });
    }
  }
};
