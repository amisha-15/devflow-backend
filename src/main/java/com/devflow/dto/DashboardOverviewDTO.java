package com.devflow.dto;

import java.util.Map;

public class DashboardOverviewDTO {
	
	private Map<String, Long> statusSummary;
	private Map<String, Long> prioritySummary;
	private Map<String, Long> assigneeSummary;
	
	public DashboardOverviewDTO(
			 Map<String, Long> statusSummary,
			 Map<String, Long> prioritySummary,
			 Map<String, Long> assigneeSummary ) {
		
		this.statusSummary = statusSummary;
		this.prioritySummary = prioritySummary;
		this.assigneeSummary = assigneeSummary;
	}
	
	public Map<String, Long> getStatusSummary(){
		return statusSummary;
	}
	
	 public Map<String, Long> getPrioritySummary() {
	        return prioritySummary;
	 }

	 public Map<String, Long> getAssigneeSummary() {
	        return assigneeSummary;
	 }
	
}
