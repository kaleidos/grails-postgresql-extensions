package net.kaleidos.hibernate.postgresql.criteria

import grails.orm.HibernateCriteriaBuilder
import net.kaleidos.hibernate.criterion.hstore.PgHstoreValueFunction

class HstoreCriterias {
    public HstoreCriterias() {
        addPgHstoreContainsKey()
    }

    public void addPgHstoreContainsKey() {
        HibernateCriteriaBuilder.metaClass.pgHstoreContainsKey = { String propertyName, Object propertyValue ->
            if (!validateSimpleExpression()) {
                throwRuntimeException(new IllegalArgumentException("Call to [pgHstoreContains] with propertyName [" +
                        propertyName + "] and value [" + propertyValue + "] not allowed here."))
            }
            propertyName = calculatePropertyName(propertyName)
            propertyValue = calculatePropertyValue(propertyValue)

            return addToCriteria(new PgHstoreValueFunction(propertyName, propertyValue, "exist"))
        }
    }
}
