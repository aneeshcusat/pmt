package com.famstack.projectscheduler.contants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkillsUtils {

	public static List<String> getUserSkillList(){
		List<String> userSkills = new ArrayList<>();
		
		userSkills.add("Primary Research");
		userSkills.add("Secondary Research");
		userSkills.add("Data Analysis");
		userSkills.add("BI Tableau Power Bi etc");
		userSkills.add("Survey Programming");
		userSkills.add("Project Management");
		userSkills.add("Viz");
		userSkills.add("Social Media");
		userSkills.add("Data Engineering");
		userSkills.add("Digital-Web Analytics");
		userSkills.add("Applied AI-Advanced Data Sciences");
		userSkills.add("AI Engineering");
		userSkills.add("Product-Web Development");
		userSkills.add("Others");
		return userSkills;
	}
	
	private static Map<String,String> getUserSkillMapping(){
		Map<String,String> userSkillsMap = new HashMap<>();
		userSkillsMap.put("BI (Tableau, PowerBi etc.)", "BI Tableau Power Bi etc");
		userSkillsMap.put("Visualization", "Viz");
		userSkillsMap.put("Social Media Sensing", "Social Media");
		userSkillsMap.put("Digital / Web Analytics", "Digital-Web Analytics");
		userSkillsMap.put("Applied AI / Advanced Data Sciences", "Applied AI-Advanced Data Sciences");
		userSkillsMap.put("Product / Web Development", "Product-Web Development");
		return userSkillsMap;
	}
	
	public static String getUserSkillMapping(String skill) {
		String mappedSkill = getUserSkillMapping().get(skill);
		return mappedSkill == null ? skill : mappedSkill;
	}
}
