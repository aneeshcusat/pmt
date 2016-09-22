package com.famstack.projectscheduler.datatransferobject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "project_activity_info", uniqueConstraints = { @UniqueConstraint(columnNames = { "id" }) })
public class ProjectActivityItem {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_Sequence")
	@SequenceGenerator(name = "id_Sequence", sequenceName = "ID_SEQ")
	private int id;

	@ManyToOne
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	private ProjectItem projectItem;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ProjectItem getProjectItem() {
		return projectItem;
	}

	public void setProjectItem(ProjectItem projectItem) {
		this.projectItem = projectItem;
	}

}
