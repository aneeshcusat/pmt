package com.famstack.projectscheduler.dataaccess;

import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;

import com.famstack.projectscheduler.BaseFamstackService;

/**
 * The Class BaseDeliveryInterfaceDataAccessObjectManager.
 * 
 * @author Aneeshkumar
 * @version 1.0
 */
public abstract class BaseFamstackDataAccessObjectManager extends BaseFamstackService {

	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public abstract void saveItem(Object saveItem);

	public abstract void updateItem(Object updateItem);

	public abstract void deleteItem(Object deleteItem);

	public abstract void saveOrUpdateItem(Object updateItem);

	public abstract Object getItemById(int id, Class<?> className);

	public abstract List<?> getAllItems(String itemName);

	public abstract List<?> executeQuery(String hqlQuery, Map<String, String> dataMap);

}
