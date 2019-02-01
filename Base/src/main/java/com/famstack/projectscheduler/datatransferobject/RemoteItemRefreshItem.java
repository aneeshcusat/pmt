package com.famstack.projectscheduler.datatransferobject;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * The Class RemoteItemRefreshItem.
 */
@Entity
@Table(name = "remote_refresh_info", uniqueConstraints = {@UniqueConstraint(columnNames = {"id"})})
public class RemoteItemRefreshItem implements FamstackBaseItem
{

    /**
	 * 
	 */
    private static final long serialVersionUID = 5145320086419804408L;

    /** The id. */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_Sequence")
    @SequenceGenerator(name = "id_Sequence", sequenceName = "ID_SEQ")
    private int id;

    /** The instance identifier. */
    @Column(name = "instance_identifier")
    private String instanceIdentifier;
    
    /** The service name. */
    @Column(name = "service_name")
    private String serviceName;
    
    /** The self update. */
    @Column(name = "self_update")
    private Boolean selfUpdate;

    /** The created date. */
    @Column(name = "created_date")
    private Timestamp createdDate;

    /** The last modified date. */
    @Column(name = "last_modified_date")
    private Timestamp lastModifiedDate;

	/* (non-Javadoc)
	 * @see com.famstack.projectscheduler.datatransferobject.FamstackBaseItem#setCreatedDate(java.sql.Timestamp)
	 */
	@Override
	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate =createdDate; 	
	}

	/* (non-Javadoc)
	 * @see com.famstack.projectscheduler.datatransferobject.FamstackBaseItem#setLastModifiedDate(java.sql.Timestamp)
	 */
	@Override
	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate =lastModifiedDate; 
	}

	/* (non-Javadoc)
	 * @see com.famstack.projectscheduler.datatransferobject.FamstackBaseItem#getCreatedDate()
	 */
	@Override
	public Timestamp getCreatedDate() {
		return createdDate;
	}

	/* (non-Javadoc)
	 * @see com.famstack.projectscheduler.datatransferobject.FamstackBaseItem#setUserGroupId(java.lang.String)
	 */
	@Override
	public void setUserGroupId(String groupId) {
		
	}

	/* (non-Javadoc)
	 * @see com.famstack.projectscheduler.datatransferobject.FamstackBaseItem#getUserGroupId()
	 */
	@Override
	public String getUserGroupId() {
		return null;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Gets the instance identifier.
	 *
	 * @return the instance identifier
	 */
	public String getInstanceIdentifier() {
		return instanceIdentifier;
	}

	/**
	 * Sets the instance identifier.
	 *
	 * @param instanceIdentifier the new instance identifier
	 */
	public void setInstanceIdentifier(String instanceIdentifier) {
		this.instanceIdentifier = instanceIdentifier;
	}

	/**
	 * Gets the service name.
	 *
	 * @return the service name
	 */
	public String getServiceName() {
		return serviceName;
	}

	/**
	 * Sets the service name.
	 *
	 * @param serviceName the new service name
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	/**
	 * Gets the self update.
	 *
	 * @return the self update
	 */
	public Boolean getSelfUpdate() {
		return selfUpdate;
	}

	/**
	 * Sets the self update.
	 *
	 * @param selfUpdate the new self update
	 */
	public void setSelfUpdate(Boolean selfUpdate) {
		this.selfUpdate = selfUpdate;
	}

	/**
	 * Gets the last modified date.
	 *
	 * @return the last modified date
	 */
	public Timestamp getLastModifiedDate() {
		return lastModifiedDate;
	}
	
	@Override
	public String toString() {
		return "ServiceName " +  serviceName +", Instance Name "+ instanceIdentifier + ", Selft status " + selfUpdate +", last updated time "+  lastModifiedDate;
	}

}