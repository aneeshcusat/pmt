package com.famstack.projectscheduler.dataaccess;

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
     * @param sessionFactory the new session factory
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
     * Save.
     *
     * @param deliveryOrderItem the delivery order item
     */
    public abstract void save(Object saveItem);

    /**
     * Update.
     *
     * @param deliveryOrderItem the delivery order item
     * @param newOrderState the new order state
     */
    public abstract void update(Object updateItem);
    
    public abstract void delete(Object deleteItem);


    
}
