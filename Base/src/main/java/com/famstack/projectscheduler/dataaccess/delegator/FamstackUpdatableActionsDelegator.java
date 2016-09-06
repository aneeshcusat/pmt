package com.famstack.projectscheduler.dataaccess.delegator;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

import com.famstack.projectscheduler.BaseFamstackService;

/**
 * The Class DeliveryInterfaceUpdatableActionsDelegator.
 *
 * @param <T> the generic type
 * 
 * @author Aneeshkumar
 * @version 1.0
 */
@Component
public class FamstackUpdatableActionsDelegator<T> extends BaseFamstackService {

    /** The transaction delegator. */
    @Resource
    private TransactionDelegator transactionDelegator;

    /**
     * Save order.
     *
     * @param deliveryOrderItem the delivery order item
     * @param action the action
     * @param sessionFactory the session factory
     * @return the t
     */
    public T saveOrder(final Object saveItem, final SaveAction<T> action, final SessionFactory sessionFactory) {
        Session session = sessionFactory.openSession();
        return transactionDelegator.inTransaction(new TransactionableAction<T>() {
            @Override
            public T process(Session session) {
                synchronized (saveItem) {
                    T result = action.saveOrder((T) saveItem);
                    session.save(saveItem);
                    return result;
                }
            }
        }, session);
    }
    
}
