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

	/** The session factory. */
	private SessionFactory sessionFactory;

	/**
	 * Sets the session factory.
	 *
	 * @param sessionFactory
	 *            the new session factory
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**
	 * Gets the session factory.
	 *
	 * @return the session factory
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/**
	 * Save item.
	 *
	 * @param saveItem
	 *            the save item
	 */
	public abstract void saveItem(Object saveItem);

	/**
	 * Update item.
	 *
	 * @param updateItem
	 *            the update item
	 */
	public abstract void updateItem(Object updateItem);

	/**
	 * Delete item.
	 *
	 * @param deleteItem
	 *            the delete item
	 */
	public abstract void deleteItem(Object deleteItem);

	/**
	 * Save or update item.
	 *
	 * @param updateItem
	 *            the update item
	 */
	public abstract void saveOrUpdateItem(Object updateItem);

	/**
	 * Gets the item by id.
	 *
	 * @param id
	 *            the id
	 * @param className
	 *            the class name
	 * @return the item by id
	 */
	public abstract Object getItemById(int id, Class<?> className);

	/**
	 * Gets the all items.
	 *
	 * @param itemName
	 *            the item name
	 * @return the all items
	 */
	public abstract List<?> getAllItems(String itemName);

	/**
	 * Execute query.
	 *
	 * @param hqlQuery
	 *            the hql query
	 * @param dataMap
	 *            the data map
	 * @return the list
	 */
	public abstract List<?> executeQuery(String hqlQuery, Map<String, String> dataMap);

}
