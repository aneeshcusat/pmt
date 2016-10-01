package com.famstack.projectscheduler.dataaccess;

import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.famstack.projectscheduler.datatransferobject.ProjectCommentItem;
import com.famstack.projectscheduler.datatransferobject.ProjectItem;
import com.famstack.projectscheduler.datatransferobject.UserItem;

public class FamstackDataAccessObjectManager extends BaseFamstackDataAccessObjectManager {

	@SuppressWarnings("unchecked")
	public UserItem getUser1(String userId) {
		Session session = getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		Query<UserItem> query = session.createQuery("from UserItem where userId = :id");
		query.setParameter("id", userId);
		UserItem userItem = null;
		try {
			userItem = query.getSingleResult();
		} catch (NoResultException ne) {
			logError(ne.getMessage(), ne);
		}
		tx.commit();
		session.close();
		return userItem;
	}

	@Override
	public void saveItem(Object saveItem) {
		Session session = getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		session.save(saveItem);
		tx.commit();
		session.close();

	}

	@Override
	public void updateItem(Object updateItem) {
		Session session = getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		session.update(updateItem);
		tx.commit();
		session.close();
	}

	@Override
	public void deleteItem(Object deleteItem) {
		Session session = getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		session.delete(deleteItem);
		tx.commit();
		session.close();
	}

	@Override
	public void saveOrUpdateItem(Object updateItem) {
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
	public List<?> executeQuery(String hqlQuery, Map<String, String> dataMap) {
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

	public ProjectItem getProjectById(int id) {
		ProjectItem projectItem = null;
		Session session = getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		projectItem = session.get(ProjectItem.class, id);
		tx.commit();
		session.close();
		return projectItem;
	}

	// ------------ Comments -------------//

	public ProjectCommentItem getCommentById(int id) {
		ProjectCommentItem projectCommentItem = null;
		Session session = getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		projectCommentItem = session.get(ProjectCommentItem.class, id);
		tx.commit();
		session.close();
		return projectCommentItem;
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<ProjectCommentItem> getProjectComments(int projectId) {
		Session session = getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		Query<ProjectCommentItem> query = session.createQuery("from ProjectCommentItem where projectItem = :projectId");
		query.setParameter("projectId", projectId);
		List<ProjectCommentItem> projectComments = null;
		try {
			projectComments = query.list();
		} catch (NoResultException ne) {
			logError(ne.getMessage(), ne);
		}
		tx.commit();
		session.close();
		return projectComments;
	}
}
