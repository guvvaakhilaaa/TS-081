/**
 * EcoImpact Carbon Sequestration Module
 * Scientific carbon storage estimation, molecular ratio calculations (44/12), and GHG Protocol compliance
 */
const Sequestration = {
  init() {
    this.bindEvents();
    this.calculate(); // Initial default calculation
  },

  bindEvents() {
    const calcBtn = document.getElementById('btnCalculateSequestration');
    if (calcBtn) {
      calcBtn.addEventListener('click', () => this.calculate());
    }

    const methodSelect = document.getElementById('seqMethodType');
    if (methodSelect) {
      methodSelect.addEventListener('change', () => {
        this.updateMethodHelp();
        this.calculate();
      });
    }

    const qtyInput = document.getElementById('seqQuantity');
    if (qtyInput) {
      qtyInput.addEventListener('input', () => this.calculate());
    }
  },

  updateMethodHelp() {
    const method = document.getElementById('seqMethodType')?.value || 'BIOLOGICAL_TREE';
    const label = document.getElementById('lblSeqQuantity');
    const help = document.getElementById('helpSeqQuantity');

    if (method === 'BIOLOGICAL_TREE') {
      if (label) label.textContent = 'Number of Native Trees Planted';
      if (help) help.textContent = 'Urban/temperate trees planted (assumes average 21.77 kg CO₂/tree/year absorption).';
    } else if (method === 'SOIL_ORGANIC') {
      if (label) label.textContent = 'Soil Compost Added (Metric Tons)';
      if (help) help.textContent = 'High-humus organic compost stabilizing soil organic carbon.';
    } else if (method === 'BLUE_CARBON') {
      if (label) label.textContent = 'Restored Coastal Wetland Area (Hectares)';
      if (help) help.textContent = 'Mangrove or tidal marsh sediment anaerobic carbon entrapment (~3.75 t CO₂/ha/yr).';
    } else {
      if (label) label.textContent = 'Direct Air Captured CO₂ (kg)';
      if (help) help.textContent = 'Industrial sorbent direct air capture with basalt mineralization.';
    }
  },

  async calculate() {
    const method = document.getElementById('seqMethodType')?.value || 'BIOLOGICAL_TREE';
    const quantity = parseFloat(document.getElementById('seqQuantity')?.value) || 25;
    const treeAge = parseFloat(document.getElementById('seqTreeAge')?.value) || 5;

    try {
      const res = await API.calculateSequestration({
        methodType: method,
        quantity,
        treeAgeYears: treeAge,
        carbonFraction: 0.5
      });
      this.renderResults(res);
    } catch (err) {
      console.error(err);
    }
  },

  renderResults(res) {
    if (!res) return;

    const co2KgEl = document.getElementById('seqResCo2Kg');
    const co2TonnesEl = document.getElementById('seqResCo2Tonnes');
    const carbonKgEl = document.getElementById('seqResCarbonKg');
    const uncertEl = document.getElementById('seqResUncertainty');
    const formulaEl = document.getElementById('seqResFormula');
    const notesEl = document.getElementById('seqResNotes');

    if (co2KgEl) co2KgEl.textContent = Number(res.co2StoredKg).toFixed(1) + ' kg CO₂';
    if (co2TonnesEl) co2TonnesEl.textContent = Number(res.co2StoredTonnes).toFixed(3) + ' tonnes';
    if (carbonKgEl) carbonKgEl.textContent = Number(res.carbonStoredKg).toFixed(1) + ' kg C';
    if (uncertEl) uncertEl.textContent = '± ' + Number(res.uncertaintyPercentage).toFixed(0) + '%';
    if (formulaEl) formulaEl.textContent = res.scientificFormula || '';
    if (notesEl) notesEl.textContent = res.methodologyExplanation || '';
  }
};
