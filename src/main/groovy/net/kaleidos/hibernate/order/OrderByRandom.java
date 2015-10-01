package net.kaleidos.hibernate.order;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Order;

public class OrderByRandom extends Order {

    private static final String ORDER_RANDOM = "random()";

    protected OrderByRandom() {
        super(ORDER_RANDOM, true);
    }

    public String toString() {
        return ORDER_RANDOM;
    }

    public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
        return ORDER_RANDOM;
    }

    public static Order byRandom() {
        return new OrderByRandom();
    }
}