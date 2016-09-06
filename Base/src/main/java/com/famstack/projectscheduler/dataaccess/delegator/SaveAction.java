package com.famstack.projectscheduler.dataaccess.delegator;

@FunctionalInterface
public interface SaveAction<T> {

    /**
     * Save order.
     *
     * @param order the order
     * @return the t
     */
    T saveOrder(T order);
}
