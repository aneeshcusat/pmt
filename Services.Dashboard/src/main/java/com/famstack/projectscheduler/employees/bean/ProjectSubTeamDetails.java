package com.famstack.projectscheduler.employees.bean;

import java.util.HashSet;
import java.util.Set;

public class ProjectSubTeamDetails {

	private int subTeamId;
	private int teamId;
	private String name;
	private String code;
	private String poId;
	private Set<ClientDetails> clientItems;

	public int getSubTeamId() {
		return subTeamId;
	}

	public void setSubTeamId(int subTeamId) {
		this.subTeamId = subTeamId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getPoId() {
		return poId;
	}

	public void setPoId(String poId) {
		this.poId = poId;
	}

	public Set<ClientDetails> getClientItems() {
		if (clientItems == null) {
			clientItems = new HashSet<>();
		}
		return clientItems;
	}

	public void setClientItems(Set<ClientDetails> clientItems) {
		this.clientItems = clientItems;
	}

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

}
