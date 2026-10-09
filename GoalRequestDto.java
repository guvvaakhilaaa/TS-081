package com.ecoimpact.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class GoalRequestDto {
    private Long userId = 1L;
    private String title = "Climate Action Plan";
    private BigDecimal targetPercentage = new BigDecimal("20.00");
    private Integer durationMonths = 6;
    private List<Long> selectedRecommendationIds = new ArrayList<>();

    public GoalRequestDto() {}

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public BigDecimal getTargetPercentage() { return targetPercentage; }
    public void setTargetPercentage(BigDecimal targetPercentage) { this.targetPercentage = targetPercentage; }

    public Integer getDurationMonths() { return durationMonths; }
    public void setDurationMonths(Integer durationMonths) { this.durationMonths = durationMonths; }

    public List<Long> getSelectedRecommendationIds() { return selectedRecommendationIds; }
    public void setSelectedRecommendationIds(List<Long> selectedRecommendationIds) { this.selectedRecommendationIds = selectedRecommendationIds; }
}
