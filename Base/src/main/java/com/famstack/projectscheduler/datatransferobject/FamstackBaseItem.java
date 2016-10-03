package com.famstack.projectscheduler.datatransferobject;

import java.io.Serializable;
import java.sql.Timestamp;

public interface FamstackBaseItem extends Serializable {

	public void setCreatedDate(Timestamp createdDate);

	public void setLastModifiedDate(Timestamp createdDate);

	public Timestamp getCreatedDate();

}
