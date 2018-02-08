package com.famstack.projectscheduler.dataaccess;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.famstack.projectscheduler.datatransferobject.FamstackBaseItem;

public class FamstackDataAccessObjectManager extends BaseFamstackDataAccessObjectManager
{

    @Override
    public void saveItem(FamstackBaseItem saveItem)
    {
        saveItem.setCreatedDate(new Timestamp(new Date().getTime()));
        saveItem.setLastModifiedDate(new Timestamp(new Date().getTime()));
        if (saveItem.getUserGroupId() == null) {
            saveItem.setUserGroupId(getFamstackUserSessionConfiguration().getUserGroupId());
        }
        Session session = getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.save(saveItem);
        tx.commit();
        session.close();
    }

    @Override
    public void updateItem(FamstackBaseItem updateItem)
    {
        updateItem.setLastModifiedDate(new Timestamp(new Date().getTime()));
        Session session = getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.update(updateItem);
        tx.commit();
        session.close();
    }

    @Override
    public void deleteItem(FamstackBaseItem deleteItem)
    {
        Session session = getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.delete(deleteItem);
        tx.commit();
        session.close();
    }

    @Override
    public void saveOrUpdateItem(FamstackBaseItem updateItem)
    {
        if (updateItem.getCreatedDate() == null) {
            updateItem.setCreatedDate(new Timestamp(new Date().getTime()));
        }
        if (updateItem.getUserGroupId() == null) {
            updateItem.setUserGroupId(getFamstackUserSessionConfiguration().getUserGroupId());
        }
        updateItem.setLastModifiedDate(new Timestamp(new Date().getTime()));
        Session session = getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.saveOrUpdate(updateItem);
        tx.commit();
        session.close();
    }

    @Override
    public Object getItemById(int id, Class<?> className)
    {
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
    public List<?> getAllItems(String itemName)
    {
        Session session = getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        String hql = String.format("from %s", itemName);
        hql += " and userGroupId = " + getFamstackUserSessionConfiguration().getUserGroupId();
        Query<?> query = session.createQuery(hql).setCacheable(true);
        List<?> itemList = query.list();
        tx.commit();
        session.close();
        return itemList;
    }

    @SuppressWarnings("deprecation")
    public List<?> getAllGroupItems(String itemName)
    {
        Session session = getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        String hql = String.format("from %s", itemName);
        Query<?> query = session.createQuery(hql).setCacheable(true);
        List<?> itemList = query.list();
        tx.commit();
        session.close();
        return itemList;
    }

    @Override
    public List<?> executeQuery(String hqlQuery, Map<String, Object> dataMap)
    {
        String userGroupId = getFamstackUserSessionConfiguration().getUserGroupId();
        return executeQueryWithGroupId(hqlQuery, dataMap, userGroupId);
    }

    @SuppressWarnings("deprecation")
    public List<?> executeQueryWithGroupId(String hqlQuery, Map<String, Object> dataMap, String userGroupId)
    {
        Session session = getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        hqlQuery += " and userGroupId = " + userGroupId;
        Query<?> query = session.createQuery(hqlQuery).setCacheable(true);
        logDebug("executeQuery :" + hqlQuery);
        if (dataMap != null) {
            logDebug("dataMap :" + dataMap.keySet());
            logDebug("dataMap values :" + dataMap.values());
            for (String paramName : dataMap.keySet()) {
                query.setParameter(paramName, dataMap.get(paramName));
            }
        }
        List<?> itemList = query.list();
        tx.commit();
        session.close();
        return itemList;
    }

    @SuppressWarnings("deprecation")
    public List<?> executeAllGroupQuery(String hqlQuery, Map<String, Object> dataMap)
    {
        Session session = getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        Query<?> query = session.createQuery(hqlQuery).setCacheable(true);
        logDebug("executeQuery :" + hqlQuery);
        if (dataMap != null) {
            logDebug("dataMap :" + dataMap.keySet());
            logDebug("dataMap values :" + dataMap.values());
            for (String paramName : dataMap.keySet()) {
                query.setParameter(paramName, dataMap.get(paramName));
            }
        }
        List<?> itemList = query.list();
        tx.commit();
        session.close();
        return itemList;
    }

    @SuppressWarnings("deprecation")
    @Override
    public List<Object[]> executeSQLQuery(String sqlQuery, Map<String, Object> dataMap)
    {

        sqlQuery =
            String.format(sqlQuery, " and user_grp_id = " + getFamstackUserSessionConfiguration().getUserGroupId());
        Session session = getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        SQLQuery query = session.createSQLQuery(sqlQuery);
        logDebug("executeSQLQuery :" + sqlQuery);
        if (dataMap != null) {
            logDebug("dataMap :" + dataMap.keySet());
            logDebug("dataMap values :" + dataMap.values());
            for (String paramName : dataMap.keySet()) {
                query.setParameter(paramName, dataMap.get(paramName));
            }
        }
        List<Object[]> itemList = query.list();
        tx.commit();
        session.close();
        return itemList;
    }

    public long getCount(String hqlQuery, Map<String, Object> dataMap)
    {
        Session session = getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        hqlQuery += " and userGroupId = " + getFamstackUserSessionConfiguration().getUserGroupId();
        Query<?> query = session.createQuery(hqlQuery).setCacheable(true);
        logDebug("executeQuery :" + hqlQuery);
        if (dataMap != null) {
            logDebug("dataMap :" + dataMap.keySet());
            logDebug("dataMap values :" + dataMap.values());
            for (String paramName : dataMap.keySet()) {
                query.setParameter(paramName, dataMap.get(paramName));
            }
        }
        Long count = (Long) query.getSingleResult();
        tx.commit();
        session.close();
        return count.longValue();
    }

    @Override
    public void executeUpdate(String hqlQuery, Map<String, Object> dataMap)
    {
        Session session = getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        Query query = session.createQuery(hqlQuery);
        if (dataMap != null) {
            logDebug("dataMap :" + dataMap.keySet());
            logDebug("dataMap values :" + dataMap.values());
            for (String paramName : dataMap.keySet()) {
                query.setParameter(paramName, dataMap.get(paramName));
            }
        }
        query.executeUpdate();
        tx.commit();
        session.close();
    }

    public void executeSQLUpdate(String sqlQuery, Map<String, Object> dataMap)
    {
        Session session = getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        Query query = session.createSQLQuery(sqlQuery);
        if (dataMap != null) {
            logDebug("dataMap :" + dataMap.keySet());
            logDebug("dataMap values :" + dataMap.values());
            for (String paramName : dataMap.keySet()) {
                query.setParameter(paramName, dataMap.get(paramName));
            }
        }
        query.executeUpdate();
        tx.commit();
        session.close();
    }
}
