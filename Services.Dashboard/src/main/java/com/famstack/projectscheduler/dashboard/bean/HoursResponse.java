package com.famstack.projectscheduler.dashboard.bean;

import java.util.Map;

public class HoursResponse {
	Map<String, Double> hoursBySkill;
	Map<String, Double> hoursByMonth;
	public Map<String, Double> getHoursBySkill() {
		return hoursBySkill;
	}
	public void setHoursBySkill(Map<String, Double> hoursBySkill) {
		this.hoursBySkill = hoursBySkill;
	}
	public Map<String, Double> getHoursByMonth() {
		return hoursByMonth;
	}
	public void setHoursByMonth(Map<String, Double> hoursByMonth) {
		this.hoursByMonth = hoursByMonth;
	}
}
