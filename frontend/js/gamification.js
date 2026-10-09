/**
 * EcoImpact Gamification Engine (Algorithm 7)
 * Player stats, 5 Eco Ranks, XP Progress, Quests, Badges, and Leaderboard
 */
const Gamification = {
  profile: null,

  async init() {
    await this.loadProfile();
    await this.loadLeaderboard();
  },

  async loadProfile() {
    try {
      const data = await API.getGamificationProfile();
      this.profile = data;
      this.renderPlayerStats(data);
      this.renderMissions(data.missions || []);
      this.renderBadges(data.badges || []);

      // Synchronize Green Planet visualization
      if (typeof Planet !== 'undefined' && Planet.setStage) {
        Planet.setStage(data.greenPlanetStage || 1);
      }
    } catch (err) {
      console.error('Failed to load gamification profile', err);
    }
  },

  renderPlayerStats(p) {
    if (!p) return;

    // Topbar and Global Sync
    const updateText = (id, val) => {
      const el = document.getElementById(id);
      if (el) el.textContent = val;
    };

    updateText('topbarUserRank', p.ecoRank || 'Seedling');
    updateText('topbarUserXp', p.ecoXp || 0);
    updateText('topbarDailyStreak', p.dailyStreak || 1);

    updateText('playerRankName', p.ecoRank || 'Seedling');
    updateText('playerLevelNumber', 'Level ' + (p.level || 1));
    updateText('playerTotalXp', p.ecoXp || 0);
    updateText('playerNextLevelXp', p.nextLevelXp || 350);
    updateText('playerDailyStreak', (p.dailyStreak || 1) + ' Days');
    updateText('playerCompletedMissions', p.completedMissionsCount || 0);
    updateText('playerUnlockedBadges', p.unlockedBadgesCount || 0);

    const xpBar = document.getElementById('playerXpProgressFill');
    if (xpBar) {
      xpBar.style.width = (p.xpProgressPercentage || 20) + '%';
    }
    updateText('playerXpProgressLabel', (p.xpProgressPercentage || 20) + '% to Next Level');
  },

  renderMissions(missions) {
    const container = document.getElementById('missionsListContainer');
    if (!container) return;

    container.innerHTML = missions.map(m => {
      const isClaimed = m.claimStatus === 'CLAIMED';
      return `
        <div class="mission-card ${isClaimed ? 'badge-newly-unlocked' : ''}">
          <div class="mission-left">
            <div class="mission-icon">
              ${this.getMissionEmoji(m.category)}
            </div>
            <div class="mission-details">
              <h4>${m.title}</h4>
              <p>${m.description}</p>
              <div style="display: flex; gap: 8px; margin-top: 4px;">
                <span class="badge-positive" style="font-size: 11px; padding: 2px 6px;">+${m.xpReward} Eco XP</span>
                <span class="rec-badge-cat" style="font-size: 11px; padding: 2px 6px;">${m.category}</span>
                <span style="font-size: 11px; color: var(--text-light); font-weight: 600;">${m.frequencyType}</span>
              </div>
            </div>
          </div>
          <div>
            ${!isClaimed ? `
              <button class="btn btn-primary btn-sm" onclick="Gamification.handleCompleteMission(${m.missionId}, ${m.xpReward})">
                Complete & Claim
              </button>
            ` : `
              <span style="color: #16A34A; font-weight: 800; font-size: 13px;">✓ Claimed</span>
            `}
          </div>
        </div>
      `;
    }).join('');
  },

  async handleCompleteMission(id, xpReward) {
    try {
      const res = await API.completeMission(id);
      if (res.success) {
        App.triggerXpGain(xpReward, 'Mission Accomplished');
        App.showToast(`Mission completed! +${xpReward} XP awarded!`, 'success');
        await this.loadProfile();
      } else {
        App.showToast(res.message || 'Mission already completed.', 'warning');
      }
    } catch (err) {
      console.error(err);
    }
  },

  renderBadges(badges) {
    const container = document.getElementById('achievementsListContainer');
    if (!container) return;

    container.innerHTML = badges.map(b => `
      <div class="badge-item ${b.unlocked ? '' : 'locked'}">
        <div class="badge-icon-wrap">
          ${this.getBadgeEmoji(b.badgeKey)}
        </div>
        <h4 class="badge-title">${b.title}</h4>
        <p class="badge-desc">${b.description}</p>
        <span class="badge-xp">+${b.xpBonus} XP Bonus</span>
        ${b.unlocked ? `
          <div style="font-size: 11px; color: #16A34A; font-weight: 800; margin-top: 6px;">✓ Unlocked</div>
        ` : `
          <div style="font-size: 11px; color: #94A3B8; font-weight: 700; margin-top: 6px;">🔒 Locked</div>
        `}
      </div>
    `).join('');
  },

  async loadLeaderboard() {
    const container = document.getElementById('leaderboardTableBody');
    if (!container) return;

    try {
      const entries = await API.getLeaderboard();
      container.innerHTML = entries.map(e => `
        <tr ${e.userId === API.userId ? 'style="background: #F0FDF4; font-weight: 700;"' : ''}>
          <td class="leaderboard-rank ${this.getRankClass(e.rank)}">${this.getRankBadge(e.rank)}</td>
          <td><strong>${e.fullName}</strong> ${e.userId === API.userId ? '(You)' : ''}</td>
          <td><span class="rec-badge-cat">${e.ecoRank}</span></td>
          <td>Level ${e.level}</td>
          <td><strong style="color: var(--primary-dark);">${e.ecoXp} XP</strong></td>
          <td>${e.country || 'India'}</td>
        </tr>
      `).join('');
    } catch (err) {
      console.error(err);
    }
  },

  getMissionEmoji(category) {
    switch (category) {
      case 'TRANSPORTATION': return '🚲';
      case 'RESIDENTIAL': return '⚡';
      case 'FOOD': return '🥗';
      case 'WASTE': return '♻️';
      case 'ASSESSMENT': return '📊';
      default: return '🌱';
    }
  },

  getBadgeEmoji(key) {
    switch (key) {
      case 'FIRST_FOOTPRINT': return '👣';
      case 'ENERGY_SAVER': return '💡';
      case 'ECO_COMMUTER': return '🚴';
      case 'WASTE_WARRIOR': return '🗑️';
      case 'SEVEN_DAY_STREAK': return '🔥';
      case 'CARBON_CHAMPION': return '👑';
      default: return '🏆';
    }
  },

  getRankClass(rank) {
    if (rank === 1) return 'rank-top-1';
    if (rank === 2) return 'rank-top-2';
    if (rank === 3) return 'rank-top-3';
    return '';
  },

  getRankBadge(rank) {
    if (rank === 1) return '🥇 1';
    if (rank === 2) return '🥈 2';
    if (rank === 3) return '🥉 3';
    return `#${rank}`;
  }
};
