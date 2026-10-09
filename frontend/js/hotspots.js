/**
 * EcoImpact Emission Hotspot Detection (Algorithm 4)
 * Visualizes ranked emission contributors, SVG Donut chart, and targeted opportunities
 */
const Hotspots = {
  data: null,

  async init() {
    await this.loadHotspots();
  },

  async loadHotspots() {
    try {
      const res = await API.getHotspots();
      this.data = res;
      this.render(res);
    } catch (err) {
      console.error('Failed to load hotspots', err);
    }
  },

  render(data) {
    if (!data) return;

    // Primary & Secondary Hotspots
    const primaryEl = document.getElementById('hotspotPrimaryName');
    const primaryPctEl = document.getElementById('hotspotPrimaryPct');
    const primaryKgEl = document.getElementById('hotspotPrimaryKg');
    if (primaryEl) primaryEl.textContent = data.primaryHotspot || 'N/A';
    if (primaryPctEl) primaryPctEl.textContent = Number(data.primaryHotspotPercentage || 0).toFixed(1) + '%';
    if (primaryKgEl) primaryKgEl.textContent = Number(data.primaryHotspotKg || 0).toFixed(2) + ' kg CO₂e';

    const secondaryEl = document.getElementById('hotspotSecondaryName');
    const secondaryPctEl = document.getElementById('hotspotSecondaryPct');
    if (secondaryEl) secondaryEl.textContent = data.secondaryHotspot || 'N/A';
    if (secondaryPctEl) secondaryPctEl.textContent = Number(data.secondaryHotspotPercentage || 0).toFixed(1) + '%';

    // Activity Hotspots List
    const actList = document.getElementById('hotspotActivityList');
    if (actList && data.activityHotspots) {
      actList.innerHTML = data.activityHotspots.map(item => `
        <li style="margin-bottom: 8px; display: flex; align-items: flex-start; gap: 8px;">
          <span style="color: #E11D48; font-weight: 800;">•</span>
          <span>${item}</span>
        </li>
      `).join('');
    }

    // Reduction Opportunities List
    const oppList = document.getElementById('hotspotOpportunitiesList');
    if (oppList && data.reductionOpportunities) {
      oppList.innerHTML = data.reductionOpportunities.map(item => `
        <li style="margin-bottom: 8px; display: flex; align-items: flex-start; gap: 8px;">
          <span style="color: #16A34A; font-weight: 800;">✓</span>
          <span>${item}</span>
        </li>
      `).join('');
    }

    // Ranked Categories Table
    const tableBody = document.getElementById('hotspotRankedTableBody');
    if (tableBody && data.rankedCategories) {
      tableBody.innerHTML = data.rankedCategories.map(cat => `
        <tr>
          <td style="font-weight: 800;">#${cat.rank}</td>
          <td><strong>${cat.category}</strong></td>
          <td>${Number(cat.emissionsKg).toFixed(2)} kg</td>
          <td>
            <div style="display: flex; align-items: center; gap: 8px;">
              <div class="progress-bar-container" style="width: 100px;">
                <div class="progress-bar-fill" style="width: ${cat.percentage}%;"></div>
              </div>
              <span style="font-weight: 700; font-size: 12px;">${Number(cat.percentage).toFixed(1)}%</span>
            </div>
          </td>
        </tr>
      `).join('');
    }

    // Render Clean SVG Donut Chart
    this.renderDonutChart(data.rankedCategories || []);
  },

  renderDonutChart(categories) {
    const container = document.getElementById('hotspotDonutContainer');
    if (!container || categories.length === 0) return;

    const colors = ['#22C55E', '#3B82F6', '#EAB308', '#EC4899', '#A855F7', '#64748B'];
    let cumulative = 0;

    const slices = categories.map((cat, idx) => {
      const pct = parseFloat(cat.percentage) || 0;
      const startAngle = (cumulative / 100) * 360;
      cumulative += pct;
      const endAngle = (cumulative / 100) * 360;
      return {
        category: cat.category,
        pct: pct,
        color: colors[idx % colors.length],
        startAngle,
        endAngle
      };
    });

    const size = 260;
    const center = size / 2;
    const radius = 90;
    const innerRadius = 55;

    let svgPaths = '';
    slices.forEach(slice => {
      if (slice.pct <= 0) return;
      const p = this.getDonutPath(center, center, radius, innerRadius, slice.startAngle, slice.endAngle);
      svgPaths += `<path d="${p}" fill="${slice.color}" stroke="#FFFFFF" stroke-width="2">
        <title>${slice.category}: ${slice.pct.toFixed(1)}%</title>
      </path>`;
    });

    const legendHtml = slices.map(s => `
      <div style="display: flex; align-items: center; gap: 6px; font-size: 11px; margin-bottom: 4px;">
        <span style="width: 10px; height: 10px; background: ${s.color}; border-radius: 2px;"></span>
        <span>${s.category} (${s.pct.toFixed(1)}%)</span>
      </div>
    `).join('');

    container.innerHTML = `
      <div style="display: flex; align-items: center; justify-content: center; gap: 20px; flex-wrap: wrap;">
        <svg width="${size}" height="${size}" viewBox="0 0 ${size} ${size}">
          ${svgPaths}
          <circle cx="${center}" cy="${center}" r="${innerRadius - 4}" fill="#FFFFFF"/>
          <text x="${center}" y="${center - 4}" text-anchor="middle" font-size="12" font-weight="700" fill="#64748B">HOTSPOTS</text>
          <text x="${center}" y="${center + 14}" text-anchor="middle" font-size="15" font-weight="800" fill="#23412B">RANKED</text>
        </svg>
        <div style="display: flex; flex-direction: column;">
          ${legendHtml}
        </div>
      </div>
    `;
  },

  getDonutPath(cx, cy, rOut, rIn, startAngle, endAngle) {
    if (endAngle - startAngle >= 359.9) {
      endAngle = 359.99;
    }
    const rad = Math.PI / 180;
    const x1 = cx + rOut * Math.cos((startAngle - 90) * rad);
    const y1 = cy + rOut * Math.sin((startAngle - 90) * rad);
    const x2 = cx + rOut * Math.cos((endAngle - 90) * rad);
    const y2 = cy + rOut * Math.sin((endAngle - 90) * rad);

    const x3 = cx + rIn * Math.cos((endAngle - 90) * rad);
    const y3 = cy + rIn * Math.sin((endAngle - 90) * rad);
    const x4 = cx + rIn * Math.cos((startAngle - 90) * rad);
    const y4 = cy + rIn * Math.sin((startAngle - 90) * rad);

    const largeArc = (endAngle - startAngle > 180) ? 1 : 0;

    return `M ${x1} ${y1} A ${rOut} ${rOut} 0 ${largeArc} 1 ${x2} ${y2} L ${x3} ${y3} A ${rIn} ${rIn} 0 ${largeArc} 0 ${x4} ${y4} Z`;
  }
};
