package net.kaleidos.hibernate.postgresql.criteria
import grails.orm.HibernateCriteriaBuilder
import net.kaleidos.hibernate.criterion.hstore.PgHstoreILikeValueFunction
import net.kaleidos.hibernate.criterion.hstore.PgHstoreOperatorExpression
import net.kaleidos.hibernate.criterion.hstore.PgHstoreValueFunction

class HstoreCriterias {
    HstoreCriterias() {
        addPgHstoreContainsKey()
        addPgHstoreContains()
        addPgHstoreIsContained()
        addPgHstoreILikeValueFunction()
    }

    void addPgHstoreContainsKey() {
        HibernateCriteriaBuilder.metaClass.pgHstoreContainsKey = { String propertyName, propertyValue ->
            if (!validateSimpleExpression()) {
                throwRuntimeException(new IllegalArgumentException("Call to [pgHstoreContains] with propertyName [" +
                        propertyName + "] and value [" + propertyValue + "] not allowed here."))
            }
            propertyName = calculatePropertyName(propertyName)
            propertyValue = calculatePropertyValue(propertyValue)

            return addToCriteria(new PgHstoreValueFunction(propertyName, propertyValue, "exist"))
        }
    }

    void addPgHstoreContains() {
        HibernateCriteriaBuilder.metaClass.pgHstoreContains = { String propertyName, Map<String,String> values ->
            if (!validateSimpleExpression()) {
                throwRuntimeException(new IllegalArgumentException("Call to [pgHstoreContains] with propertyName [" +
                        propertyName + "] and value [" + propertyValue + "] not allowed here."))
            }
            propertyName = calculatePropertyName(propertyName)
            return addToCriteria(new PgHstoreOperatorExpression(propertyName, values, "@>"))
        }
    }

    void addPgHstoreIsContained() {
        HibernateCriteriaBuilder.metaClass.pgHstoreIsContained = { String propertyName, Map<String,String> values ->
            if (!validateSimpleExpression()) {
                throwRuntimeException(new IllegalArgumentException("Call to [pgHstoreIsContained] with propertyName [" +
                        propertyName + "] and value [" + propertyValue + "] not allowed here."))
            }
            propertyName = calculatePropertyName(propertyName)
            return addToCriteria(new PgHstoreOperatorExpression(propertyName, values, "<@"))
        }
    }

    void addPgHstoreILikeValueFunction() {
        HibernateCriteriaBuilder.metaClass.pgHstoreILikeValue = { String propertyName, propertyValue ->
            if (!validateSimpleExpression()) {
                throwRuntimeException(new IllegalArgumentException("Call to [pgHstoreILikeValue] with propertyName [" +
                        propertyName + "] and value [" + propertyValue + "] not allowed here."))
            }
            propertyName = calculatePropertyName(propertyName)
            propertyValue = calculatePropertyValue(propertyValue)

            return addToCriteria(new PgHstoreILikeValueFunction(propertyName, propertyValue))
        }
    }
}
