/**
 * EcoImpact Green Planet Canvas Visualizer (Algorithm 7 Visual Progression)
 * Interactive evolving eco-island from Level 1 (Seedling Islet) to Level 5 (Harmonious Biosphere)
 */
const Planet = {
  canvas: null,
  ctx: null,
  stage: 2, // 1 to 5
  animationFrameId: null,
  particles: [],
  angle: 0,

  stageTitles: [
    "Stage 1: Seedling Islet (Initial clean plot with sapling)",
    "Stage 2: Sprouting Meadow (Lush grass, wild flora & clear stream)",
    "Stage 3: Biodiverse Canopy (Flowering groves & vibrant wildlife)",
    "Stage 4: Thriving Eco-Sanctuary (Flourishing forests & clean wind power)",
    "Stage 5: Harmonious Biosphere (Restored planetary equilibrium)"
  ],

  init() {
    this.canvas = document.getElementById('planetCanvas');
    if (!this.canvas) return;

    this.ctx = this.canvas.getContext('2d');
    this.resizeCanvas();
    window.addEventListener('resize', () => this.resizeCanvas());

    this.initParticles();
    this.startAnimation();

    // Interactive click to sprout floating leaves
    this.canvas.addEventListener('click', (e) => {
      const rect = this.canvas.getBoundingClientRect();
      const x = e.clientX - rect.left;
      const y = e.clientY - rect.top;
      this.spawnSprout(x, y);
    });
  },

  resizeCanvas() {
    if (!this.canvas) return;
    this.canvas.width = this.canvas.parentElement.clientWidth || 500;
    this.canvas.height = this.canvas.parentElement.clientHeight || 360;
  },

  setStage(newStage) {
    this.stage = Math.min(5, Math.max(1, newStage));
    const titleEl = document.getElementById('planetStageLabel');
    const badgeEl = document.getElementById('planetStageBadge');
    if (titleEl) titleEl.textContent = this.stageTitles[this.stage - 1];
    if (badgeEl) badgeEl.textContent = 'Stage ' + this.stage + ' / 5';
    this.initParticles();
  },

  initParticles() {
    this.particles = [];
    const count = this.stage * 12;
    for (let i = 0; i < count; i++) {
      this.particles.push({
        x: Math.random() * (this.canvas?.width || 500),
        y: Math.random() * (this.canvas?.height || 360),
        size: Math.random() * 3 + 2,
        speedY: -(Math.random() * 0.8 + 0.3),
        speedX: (Math.random() - 0.5) * 0.5,
        color: this.stage >= 3 ? (Math.random() > 0.5 ? '#86EFAC' : '#FDE047') : '#A7F3D0',
        alpha: Math.random() * 0.7 + 0.3
      });
    }
  },

  spawnSprout(x, y) {
    for (let i = 0; i < 8; i++) {
      this.particles.push({
        x, y,
        size: Math.random() * 4 + 3,
        speedY: -(Math.random() * 2 + 1),
        speedX: (Math.random() - 0.5) * 2,
        color: '#22C55E',
        alpha: 1.0
      });
    }
    App.showToast('🌿 Sprouted life in your Green Biosphere!', 'info');
  },

  startAnimation() {
    const render = () => {
      this.draw();
      this.animationFrameId = requestAnimationFrame(render);
    };
    render();
  },

  draw() {
    if (!this.ctx || !this.canvas) return;
    const ctx = this.ctx;
    const w = this.canvas.width;
    const h = this.canvas.height;

    ctx.clearRect(0, 0, w, h);

    // Subtle atmospheric halo
    this.angle += 0.015;
    const floatingY = Math.sin(this.angle) * 8;
    const centerX = w / 2;
    const centerY = h / 2 + floatingY + 20;

    // 1. Water Ripple Ring
    ctx.save();
    ctx.beginPath();
    ctx.ellipse(centerX, centerY + 50, 160 + this.stage * 12, 60, 0, 0, Math.PI * 2);
    ctx.fillStyle = 'rgba(186, 230, 253, 0.45)';
    ctx.fill();
    ctx.restore();

    // 2. Isometric Floating Eco-Island Base (Earth & Grass)
    // Lower Earth Layer (Rocky soil)
    ctx.save();
    ctx.beginPath();
    ctx.ellipse(centerX, centerY + 30, 140, 50, 0, 0, Math.PI * 2);
    ctx.fillStyle = '#854D0E'; // Earth brown
    ctx.fill();

    // Island Top Layer (Lush Green Grass)
    ctx.beginPath();
    ctx.ellipse(centerX, centerY, 135, 45, 0, 0, Math.PI * 2);
    const grassGrad = ctx.createLinearGradient(centerX - 100, centerY - 40, centerX + 100, centerY + 40);
    grassGrad.addColorStop(0, '#86EFAC');
    grassGrad.addColorStop(1, '#22C55E');
    ctx.fillStyle = grassGrad;
    ctx.fill();
    ctx.lineWidth = 3;
    ctx.strokeStyle = '#15803D';
    ctx.stroke();
    ctx.restore();

    // 3. Stage-Specific Flora and Features
    // Central feature: Tree / Grove
    this.drawTrees(ctx, centerX, centerY);

    // Stage 4 & 5 Clean Energy Wind Turbines
    if (this.stage >= 4) {
      this.drawWindTurbine(ctx, centerX + 80, centerY - 25, this.angle * 2);
      this.drawWindTurbine(ctx, centerX - 85, centerY - 15, this.angle * 2.2);
    }

    // Stage 5 Rainbow / Biosphere Harmony Arc
    if (this.stage >= 5) {
      ctx.save();
      ctx.beginPath();
      ctx.arc(centerX, centerY - 20, 140, Math.PI, 0, false);
      ctx.lineWidth = 6;
      ctx.strokeStyle = 'rgba(253, 224, 71, 0.4)';
      ctx.stroke();
      ctx.restore();
    }

    // 4. Floating Spores & Sparkles Particles
    ctx.save();
    this.particles.forEach(p => {
      ctx.beginPath();
      ctx.arc(p.x, p.y, p.size, 0, Math.PI * 2);
      ctx.fillStyle = p.color;
      ctx.globalAlpha = p.alpha;
      ctx.fill();

      p.y += p.speedY;
      p.x += p.speedX;

      if (p.y < 0) {
        p.y = h;
        p.x = Math.random() * w;
      }
    });
    ctx.restore();
  },

  drawTrees(ctx, cx, cy) {
    ctx.save();

    // Stage 1: Single small sapling
    if (this.stage === 1) {
      // Trunk
      ctx.fillStyle = '#78350F';
      ctx.fillRect(cx - 3, cy - 35, 6, 35);
      // Foliage
      ctx.beginPath();
      ctx.arc(cx, cy - 45, 18, 0, Math.PI * 2);
      ctx.fillStyle = '#22C55E';
      ctx.fill();
    }
    // Stage 2: Sturdy Tree with flowers
    else if (this.stage === 2) {
      ctx.fillStyle = '#78350F';
      ctx.fillRect(cx - 5, cy - 50, 10, 50);
      ctx.beginPath();
      ctx.arc(cx, cy - 65, 30, 0, Math.PI * 2);
      ctx.fillStyle = '#16A34A';
      ctx.fill();

      // Bush left
      ctx.beginPath();
      ctx.arc(cx - 40, cy - 10, 15, 0, Math.PI * 2);
      ctx.fillStyle = '#4ADE80';
      ctx.fill();
    }
    // Stage 3 to 5: Lush forest grove with fruit & flowers
    else {
      // Center Grand Tree
      ctx.fillStyle = '#581C87';
      ctx.fillRect(cx - 8, cy - 70, 16, 70);
      ctx.beginPath();
      ctx.arc(cx, cy - 90, 42, 0, Math.PI * 2);
      ctx.fillStyle = '#15803D';
      ctx.fill();

      // Left Tree
      ctx.fillRect(cx - 50, cy - 45, 10, 45);
      ctx.beginPath();
      ctx.arc(cx - 50, cy - 60, 24, 0, Math.PI * 2);
      ctx.fillStyle = '#22C55E';
      ctx.fill();

      // Right Tree
      ctx.fillRect(cx + 45, cy - 40, 10, 40);
      ctx.beginPath();
      ctx.arc(cx + 45, cy - 55, 22, 0, Math.PI * 2);
      ctx.fillStyle = '#4ADE80';
      ctx.fill();

      // Flowers / Fruit Dots
      const flowerColors = ['#F43F5E', '#FBBF24', '#A855F7'];
      for (let i = 0; i < 9; i++) {
        const fx = cx - 30 + (i % 3) * 30;
        const fy = cy - 105 + Math.floor(i / 3) * 15;
        ctx.beginPath();
        ctx.arc(fx, fy, 4, 0, Math.PI * 2);
        ctx.fillStyle = flowerColors[i % flowerColors.length];
        ctx.fill();
      }
    }
    ctx.restore();
  },

  drawWindTurbine(ctx, x, y, rot) {
    ctx.save();
    // Tower
    ctx.strokeStyle = '#FFFFFF';
    ctx.lineWidth = 3;
    ctx.beginPath();
    ctx.moveTo(x, y);
    ctx.lineTo(x, y - 55);
    ctx.stroke();

    // Hub
    ctx.beginPath();
    ctx.arc(x, y - 55, 4, 0, Math.PI * 2);
    ctx.fillStyle = '#E2E8F0';
    ctx.fill();

    // Blades
    for (let b = 0; b < 3; b++) {
      const bAngle = rot + (b * Math.PI * 2) / 3;
      const bx = x + Math.cos(bAngle) * 22;
      const by = y - 55 + Math.sin(bAngle) * 22;
      ctx.beginPath();
      ctx.moveTo(x, y - 55);
      ctx.lineTo(bx, by);
      ctx.lineWidth = 2;
      ctx.strokeStyle = '#FFFFFF';
      ctx.stroke();
    }
    ctx.restore();
  }
};
