/**
 * EcoImpact API Client
 * Manages communication with Java Spring Boot REST backend with transparent fallback
 */
const API = {
  baseUrl: '/api',
  userId: 1,

  async request(endpoint, options = {}) {
    const defaultHeaders = {
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    };

    try {
      const response = await fetch(`${this.baseUrl}${endpoint}`, {
        ...options,
        headers: { ...defaultHeaders, ...options.headers }
      });

      if (!response.ok) {
        throw new Error(`HTTP Error ${response.status}: ${response.statusText}`);
      }

      return await response.json();
    } catch (err) {
      console.warn(`API call failed for ${endpoint}:`, err.message);
      // Fallback to local simulated data if backend is starting or offline
      return this.handleFallback(endpoint, options);
    }
  },

  // Carbon Assessment & Calculation
  async calculateCarbon(activityData) {
    return this.request('/carbon/calculate', {
      method: 'POST',
      body: JSON.stringify({ ...activityData, userId: this.userId })
    });
  },

  async getAssessments() {
    return this.request(`/carbon/assessments?userId=${this.userId}`);
  },

  async getFactors() {
    return this.request('/factors');
  },

  // Emission Hotspots
  async getHotspots() {
    return this.request(`/emissions/hotspots?userId=${this.userId}`);
  },

  // Recommendations
  async getRecommendations() {
    return this.request(`/recommendations?userId=${this.userId}`);
  },

  async updateRecommendationStatus(recId, status) {
    return this.request(`/recommendations/${recId}/status`, {
      method: 'POST',
      body: JSON.stringify({ status })
    });
  },

  // What-If Simulation
  async runSimulation(simulationData) {
    return this.request('/simulations', {
      method: 'POST',
      body: JSON.stringify({ ...simulationData, userId: this.userId })
    });
  },

  // Goals & Reduction Plans
  async createGoal(goalData) {
    return this.request('/goals', {
      method: 'POST',
      body: JSON.stringify({ ...goalData, userId: this.userId })
    });
  },

  async getGoals() {
    return this.request(`/goals?userId=${this.userId}`);
  },

  async getPlans() {
    return this.request(`/plans?userId=${this.userId}`);
  },

  // Progress Analytics
  async getProgress() {
    return this.request(`/progress?userId=${this.userId}`);
  },

  // Gamification & Quest
  async getGamificationProfile() {
    return this.request(`/gamification/profile?userId=${this.userId}`);
  },

  async completeMission(missionId) {
    return this.request(`/missions/${missionId}/complete?userId=${this.userId}`, {
      method: 'POST'
    });
  },

  async getLeaderboard() {
    return this.request('/leaderboard');
  },

  // Sequestration
  async calculateSequestration(seqData) {
    return this.request('/sequestration/calculate', {
      method: 'POST',
      body: JSON.stringify({ ...seqData, userId: this.userId })
    });
  },

  // ML Archetype Persona
  async getMlPersona() {
    return this.request(`/ml/cluster?userId=${this.userId}`);
  },

  // Demo Reset
  async resetDemo() {
    return this.request(`/demo/reset?userId=${this.userId}`, {
      method: 'POST'
    });
  },

  // Smart in-memory fallback for smooth offline demo execution
  handleFallback(endpoint, options) {
    if (endpoint.includes('/gamification/profile')) {
      return {
        userId: 1,
        username: 'aarav_eco',
        fullName: 'Aarav Sharma',
        ecoRank: 'Sprout',
        level: 2,
        ecoXp: 185,
        nextLevelXp: 350,
        xpProgressToNextLevel: 35,
        xpProgressPercentage: 20,
        dailyStreak: 4,
        greenPlanetStage: 2,
        greenPlanetTitle: 'Stage 2: Sprouting Meadow (Lush grass & wild flora)',
        completedMissionsCount: 3,
        unlockedBadgesCount: 2,
        missions: [
          { missionId: 1, title: 'Switch to Public Transit or Metro', description: 'Take bus or metro for 15 km this week.', category: 'TRANSPORTATION', xpReward: 50, completed: true, claimStatus: 'CLAIMED', iconName: 'bus' },
          { missionId: 2, title: 'Unplug Phantom Power & Idle Devices', description: 'Switch off idle standby chargers.', category: 'RESIDENTIAL', xpReward: 30, completed: true, claimStatus: 'CLAIMED', iconName: 'power' },
          { missionId: 3, title: 'Meat-Free Green Monday', description: 'Enjoy a plant-based meal today.', category: 'FOOD', xpReward: 40, completed: false, claimStatus: 'PENDING', iconName: 'leaf' },
          { missionId: 4, title: 'Zero Food Waste Day', description: 'Finish prepared meals and discard zero edible food.', category: 'FOOD', xpReward: 35, completed: false, claimStatus: 'PENDING', iconName: 'utensils' },
          { missionId: 5, title: 'Waste Segregation & Composting', description: 'Segregate dry recyclables and initiate kitchen composting.', category: 'WASTE', xpReward: 45, completed: false, claimStatus: 'PENDING', iconName: 'recycle' },
          { missionId: 6, title: 'Conduct Monthly Carbon Review', description: 'Review your calculated carbon assessment.', category: 'ASSESSMENT', xpReward: 60, completed: true, claimStatus: 'CLAIMED', iconName: 'calculator' }
        ],
        badges: [
          { achievementId: 1, badgeKey: 'FIRST_FOOTPRINT', title: 'First Footprint Pioneer', description: 'Completed your very first carbon footprint calculation.', xpBonus: 50, iconName: 'footprints', unlocked: true },
          { achievementId: 2, badgeKey: 'ENERGY_SAVER', title: 'Watt Whisperer', description: 'Logged an energy-saving efficiency action.', xpBonus: 75, iconName: 'zap', unlocked: true },
          { achievementId: 3, badgeKey: 'ECO_COMMUTER', title: 'Green Commuter', description: 'Logged 50+ km of public transport transit.', xpBonus: 80, iconName: 'bike', unlocked: false },
          { achievementId: 4, badgeKey: 'WASTE_WARRIOR', title: 'Zero-Waste Champion', description: 'Maintained 80%+ waste recycling.', xpBonus: 70, iconName: 'trash-2', unlocked: false },
          { achievementId: 5, badgeKey: 'SEVEN_DAY_STREAK', title: 'Eco Devotee (7-Day Streak)', description: 'Maintained an unbroken daily action streak.', xpBonus: 120, iconName: 'flame', unlocked: false },
          { achievementId: 6, badgeKey: 'CARBON_CHAMPION', title: 'Planet Guardian Hero', description: 'Achieved a verified 15%+ carbon reduction.', xpBonus: 200, iconName: 'award', unlocked: false }
        ],
        recentXpTransactions: [
          { id: 1, amount: 50, description: 'Unlocked badge: First Footprint Pioneer', createdAt: '2026-10-09' },
          { id: 2, amount: 75, description: 'Unlocked badge: Watt Whisperer', createdAt: '2026-10-09' },
          { id: 3, amount: 60, description: 'Completed monthly carbon assessment review', createdAt: '2026-10-09' }
        ]
      };
    }

    if (endpoint.includes('/emissions/hotspots')) {
      return {
        primaryHotspot: 'RESIDENTIAL',
        primaryHotspotPercentage: 44.07,
        primaryHotspotKg: 239.17,
        secondaryHotspot: 'FOOD',
        secondaryHotspotPercentage: 32.57,
        rankedCategories: [
          { category: 'RESIDENTIAL', emissionsKg: 239.17, percentage: 44.07, rank: 1 },
          { category: 'FOOD', emissionsKg: 176.75, percentage: 32.57, rank: 2 },
          { category: 'TRANSPORTATION', emissionsKg: 74.64, percentage: 13.75, rank: 3 },
          { category: 'SHOPPING', emissionsKg: 45.75, percentage: 8.43, rank: 4 },
          { category: 'WASTE', emissionsKg: 6.45, percentage: 1.18, rank: 5 }
        ],
        activityHotspots: [
          'Grid electricity & domestic heating contribute 44.07% of footprint.',
          'Dietary protein lifecycle & household food waste make up 32.57%.'
        ],
        reductionOpportunities: [
          'Solar rooftop or high-efficiency star-rated inverter appliances.',
          'Transition to plant-rich meals and zero-waste grocery planning.'
        ]
      };
    }

    if (endpoint.includes('/leaderboard')) {
      return [
        { rank: 1, userId: 101, username: 'neha_climate', fullName: 'Neha Patel', country: 'India', ecoRank: 'Planet Protector', level: 5, ecoXp: 1240 },
        { rank: 2, userId: 102, username: 'vikram_green', fullName: 'Vikram Mehta', country: 'India', ecoRank: 'Eco Hero', level: 4, ecoXp: 860 },
        { rank: 3, userId: 1, username: 'aarav_eco', fullName: 'Aarav Sharma', country: 'India', ecoRank: 'Sprout', level: 2, ecoXp: 185 },
        { rank: 4, userId: 104, username: 'priya_earth', fullName: 'Priya Iyer', country: 'India', ecoRank: 'Seedling', level: 1, ecoXp: 110 }
      ];
    }

    return { success: true };
  }
};
