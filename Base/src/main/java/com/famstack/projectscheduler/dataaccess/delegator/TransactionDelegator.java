package com.famstack.projectscheduler.dataaccess.delegator;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionDelegator {

    /**
     * In transaction.
     *
     * @param <T> the generic type
     * @param action the action
     * @param session the session
     * @return the t
     */
    public <T> T inTransaction(TransactionableAction<T> action, Session session) {

        T result;
        Transaction tx = session.beginTransaction();
        result = action.process(session);
        tx.commit();
        session.close();
        return result;
    }

}
