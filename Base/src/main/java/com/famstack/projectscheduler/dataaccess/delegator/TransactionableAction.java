package com.famstack.projectscheduler.dataaccess.delegator;

import org.hibernate.Session;

@FunctionalInterface
public interface TransactionableAction<T> {

    /**
     * Process.
     *
     * @return the t
     */
    T process(Session session);
}
