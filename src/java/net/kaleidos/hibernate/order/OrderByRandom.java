package net.kaleidos.hibernate.order;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Order;

/**
 * Extends {@link org.hibernate.criterion.Order} to allow ordering by an SQL formula passed by the user.
 * Is simply appends the <code>sqlFormula</code> passed by the user to the resulting SQL query, without any verification.
 * @author Sorin Postelnicu
 * @since Jun 10, 2008
 */
public class OrderByRandom extends Order {

    private static final String ORDER_RANDOM = "random()";

    /**
     * Constructor for Order.
     * @param sqlFormula an SQL formula that will be appended to the resulting SQL query
     */
    protected OrderByRandom() {
        super(ORDER_RANDOM, true);
    }

    public String toString() {
        return ORDER_RANDOM;
    }

    public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
        return ORDER_RANDOM;
    }

    /**
     * Custom order
     *
     * @param sqlFormula an SQL formula that will be appended to the resulting SQL query
     * @return Order
     */
    public static Order byRandom() {
        return new OrderByRandom();
    }
}