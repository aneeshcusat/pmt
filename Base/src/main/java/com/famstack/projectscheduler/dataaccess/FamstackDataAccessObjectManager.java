package com.famstack.projectscheduler.dataaccess;

import java.util.List;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.famstack.projectscheduler.datatransferobject.UserItem;

public class FamstackDataAccessObjectManager extends BaseFamstackDataAccessObjectManager {

	public UserItem getUserById(int id) {
		UserItem userItem = null;
		Session session = getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		userItem = session.get(UserItem.class, id);
		tx.commit();
		session.close();
		return userItem;
	}

	@SuppressWarnings("unchecked")
	public UserItem getUser(String userId) {
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

	@SuppressWarnings({ "deprecation", "unchecked" })
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
		session.saveOrUpdate(updateItem);
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

}
