package com.famstack.projectscheduler.dataaccess.delegator;

import org.hibernate.Session;

@FunctionalInterface
public interface UpdateAction<T> {

    /**
     * Update order.
     *
     * @param orderItem the order item
     * @return the t
     */
    T updateOrder(T orderItem, Session session);

}
