package com.ecoimpact.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class HotspotResultDto {
    private String primaryHotspot;
    private BigDecimal primaryHotspotPercentage;
    private BigDecimal primaryHotspotKg;
    private String secondaryHotspot;
    private BigDecimal secondaryHotspotPercentage;
    private List<RankedCategoryDto> rankedCategories = new ArrayList<>();
    private List<String> activityHotspots = new ArrayList<>();
    private List<String> reductionOpportunities = new ArrayList<>();

    public static class RankedCategoryDto {
        private String category;
        private BigDecimal emissionsKg;
        private BigDecimal percentage;
        private Integer rank;

        public RankedCategoryDto() {}

        public RankedCategoryDto(String category, BigDecimal emissionsKg, BigDecimal percentage, Integer rank) {
            this.category = category;
            this.emissionsKg = emissionsKg;
            this.percentage = percentage;
            this.rank = rank;
        }

        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }

        public BigDecimal getEmissionsKg() { return emissionsKg; }
        public void setEmissionsKg(BigDecimal emissionsKg) { this.emissionsKg = emissionsKg; }

        public BigDecimal getPercentage() { return percentage; }
        public void setPercentage(BigDecimal percentage) { this.percentage = percentage; }

        public Integer getRank() { return rank; }
        public void setRank(Integer rank) { this.rank = rank; }
    }

    public HotspotResultDto() {}

    public String getPrimaryHotspot() { return primaryHotspot; }
    public void setPrimaryHotspot(String primaryHotspot) { this.primaryHotspot = primaryHotspot; }

    public BigDecimal getPrimaryHotspotPercentage() { return primaryHotspotPercentage; }
    public void setPrimaryHotspotPercentage(BigDecimal primaryHotspotPercentage) { this.primaryHotspotPercentage = primaryHotspotPercentage; }

    public BigDecimal getPrimaryHotspotKg() { return primaryHotspotKg; }
    public void setPrimaryHotspotKg(BigDecimal primaryHotspotKg) { this.primaryHotspotKg = primaryHotspotKg; }

    public String getSecondaryHotspot() { return secondaryHotspot; }
    public void setSecondaryHotspot(String secondaryHotspot) { this.secondaryHotspot = secondaryHotspot; }

    public BigDecimal getSecondaryHotspotPercentage() { return secondaryHotspotPercentage; }
    public void setSecondaryHotspotPercentage(BigDecimal secondaryHotspotPercentage) { this.secondaryHotspotPercentage = secondaryHotspotPercentage; }

    public List<RankedCategoryDto> getRankedCategories() { return rankedCategories; }
    public void setRankedCategories(List<RankedCategoryDto> rankedCategories) { this.rankedCategories = rankedCategories; }

    public List<String> getActivityHotspots() { return activityHotspots; }
    public void setActivityHotspots(List<String> activityHotspots) { this.activityHotspots = activityHotspots; }

    public List<String> getReductionOpportunities() { return reductionOpportunities; }
    public void setReductionOpportunities(List<String> reductionOpportunities) { this.reductionOpportunities = reductionOpportunities; }
}
