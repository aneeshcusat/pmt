package com.famstack.projectscheduler.dataaccess;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.famstack.projectscheduler.datatransferobject.FamstackBaseItem;

public class FamstackDataAccessObjectManager extends BaseFamstackDataAccessObjectManager {

	@Override
	public void saveItem(FamstackBaseItem saveItem) {
		saveItem.setCreatedDate(new Timestamp(new Date().getTime()));
		saveItem.setLastModifiedDate(new Timestamp(new Date().getTime()));
		Session session = getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		session.save(saveItem);
		tx.commit();
		session.close();
	}

	@Override
	public void updateItem(FamstackBaseItem updateItem) {
		updateItem.setLastModifiedDate(new Timestamp(new Date().getTime()));
		Session session = getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		session.update(updateItem);
		tx.commit();
		session.close();
	}

	@Override
	public void deleteItem(FamstackBaseItem deleteItem) {
		Session session = getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		session.delete(deleteItem);
		tx.commit();
		session.close();
	}

	@Override
	public void saveOrUpdateItem(FamstackBaseItem updateItem) {
		if (updateItem.getCreatedDate() == null) {
			updateItem.setCreatedDate(new Timestamp(new Date().getTime()));
		}
		updateItem.setLastModifiedDate(new Timestamp(new Date().getTime()));
		Session session = getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		session.saveOrUpdate(updateItem);
		tx.commit();
		session.close();
	}

	@Override
	public Object getItemById(int id, Class<?> className) {
		Object item = null;
		Session session = getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		item = session.get(className, id);
		tx.commit();
		session.close();
		return item;
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<?> getAllItems(String itemName) {
		Session session = getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		String hql = String.format("from %s", itemName);
		Query<?> query = session.createQuery(hql);
		List<?> itemList = query.list();
		tx.commit();
		session.close();
		return itemList;
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<?> executeQuery(String hqlQuery, Map<String, Object> dataMap) {
		Session session = getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		Query<?> query = session.createQuery(hqlQuery);
		logDebug("executeQuery :" + hqlQuery);
		if (dataMap != null) {
			logDebug("dataMap :" + dataMap.keySet());
			for (String paramName : dataMap.keySet()) {
				query.setParameter(paramName, dataMap.get(paramName));
			}
		}
		List<?> itemList = query.list();
		tx.commit();
		session.close();
		return itemList;
	}

	public long getCount(String hqlQuery, Map<String, Object> dataMap) {
		Session session = getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		Query<?> query = session.createQuery(hqlQuery);
		logDebug("executeQuery :" + hqlQuery);
		if (dataMap != null) {
			logDebug("dataMap :" + dataMap.keySet());
			for (String paramName : dataMap.keySet()) {
				query.setParameter(paramName, dataMap.get(paramName));
			}
		}
		long count = (long) query.getSingleResult();
		tx.commit();
		session.close();
		return count;
	}

	@Override
	public void executeUpdate(String hqlQuery, Map<String, Object> dataMap) {
		Session session = getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		Query query = session.createQuery(hqlQuery);
		if (dataMap != null) {
			logDebug("dataMap :" + dataMap.keySet());
			for (String paramName : dataMap.keySet()) {
				query.setParameter(paramName, dataMap.get(paramName));
			}
		}
		query.executeUpdate();
		tx.commit();
		session.close();
	}

}
