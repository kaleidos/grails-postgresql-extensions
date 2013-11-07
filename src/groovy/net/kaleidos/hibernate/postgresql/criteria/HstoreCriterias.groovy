package net.kaleidos.hibernate.postgresql.criteria

import grails.orm.HibernateCriteriaBuilder
import net.kaleidos.hibernate.criterion.hstore.PgHstoreValueFunction
import net.kaleidos.hibernate.criterion.hstore.PgHstoreOperatorExpression

class HstoreCriterias {
    public HstoreCriterias() {
        addPgHstoreContainsKey()
        addPgHstoreContains()
        addPgHstoreIsContained()
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
    public void addPgHstoreContains() {
        HibernateCriteriaBuilder.metaClass.pgHstoreContains = { String propertyName, Map<String,String> values ->
            if (!validateSimpleExpression()) {
                throwRuntimeException(new IllegalArgumentException("Call to [pgHstoreContains] with propertyName [" +
                        propertyName + "] and value [" + propertyValue + "] not allowed here."))
            }
            propertyName = calculatePropertyName(propertyName)
            return addToCriteria(new PgHstoreOperatorExpression(propertyName, values, "@>"))
        }
    }

    public void addPgHstoreIsContained() {
        HibernateCriteriaBuilder.metaClass.pgHstoreIsContained = { String propertyName, Map<String,String> values ->
            if (!validateSimpleExpression()) {
                throwRuntimeException(new IllegalArgumentException("Call to [pgHstoreIsContained] with propertyName [" +
                        propertyName + "] and value [" + propertyValue + "] not allowed here."))
            }
            propertyName = calculatePropertyName(propertyName)
            return addToCriteria(new PgHstoreOperatorExpression(propertyName, values, "<@"))
        }
    }
}
