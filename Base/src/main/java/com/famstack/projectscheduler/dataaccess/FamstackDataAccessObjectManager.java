package com.famstack.projectscheduler.dataaccess;

import java.util.List;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.famstack.projectscheduler.datatransferobject.UserItem;

public class FamstackDataAccessObjectManager extends BaseFamstackDataAccessObjectManager {

    /**
     * Gets the user.
     *
     * @param userId the user id
     * @return the user
     */
    public UserItem getUser(String userId) {
        Session session = getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        Query query = session.createQuery("from UserItem where userId = :userId");
        query.setParameter("userId", userId);
        UserItem userItem = null;
        try {
            userItem = (UserItem) query.getSingleResult();
        }
        catch (NoResultException ne) {
            logError(ne.getMessage(), ne);
        }
        tx.commit();
        session.close();
        return userItem;
    }

    /**
     * Gets the all users.
     *
     * @return the all users
     */
    public List<UserItem> getAllUsers() {
        Session session = getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        Query query = session.createQuery("from UserItem");
        List<UserItem> userItemList = query.list();
        tx.commit();
        session.close();
        return userItemList;
    }

	@Override
	public void save(Object saveItem) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Object updateItem) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Object deleteItem) {
		// TODO Auto-generated method stub
		
	}

}
