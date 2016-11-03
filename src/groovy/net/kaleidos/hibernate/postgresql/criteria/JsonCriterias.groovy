package net.kaleidos.hibernate.postgresql.criteria

import grails.orm.HibernateCriteriaBuilder
import net.kaleidos.hibernate.criterion.json.PgJsonExpression

class JsonCriterias {

    JsonCriterias() {
        addHasFieldValueOperator()
        addGenericFieldValueOperator()
    }

    private void addHasFieldValueOperator() {
        /**
         * Creates a "json has field value" Criterion based on the specified property name and value
         * @param propertyName The property name (json field)
         * @param jsonAttribute The json attribute
         * @param propertyValue The property value
         * @return A Criterion instance
         */
        HibernateCriteriaBuilder.metaClass.pgJsonHasFieldValue = { String propertyName, String jsonAttribute, propertyValue ->
            if (!validateSimpleExpression()) {
                throwRuntimeException(new IllegalArgumentException("Call to [pgJsonHasFieldValue] with propertyName [" +
                    propertyName + "], jsonAttribute [" + jsonAttribute + "] and value [" + propertyValue + "] not allowed here."))
            }

            propertyName = calculatePropertyName(propertyName)
            propertyValue = calculatePropertyValue(propertyValue)

            return addToCriteria(new PgJsonExpression(propertyName, '->>', jsonAttribute, "=", propertyValue as String))
        }
    }

     private void addGenericFieldValueOperator() {
        /**
         * Creates a "json <condition> on field value" Criterion based on the specified property name and value
         * @param propertyName The property name (json field)
         * @param jsonAttribute The json attribute
         * @param jsonOp The json operator (->>, #>, ...)
         * @param propertyValue The property value
         * @param sqlOp The sql operator (=, <>, ilike, ...)
         * @return A Criterion instance
         */
         HibernateCriteriaBuilder.metaClass.pgJson = { String propertyName, String jsonOp, String jsonAttribute, String sqlOp, propertyValue->
            if (!validateSimpleExpression()) {
                throwRuntimeException(new IllegalArgumentException("Call to [pgJson] with propertyName [" +
                    propertyName + "], json operator [" + jsonOp + "], jsonAttribute [" + jsonAttribute + "], sql operator [" + sqlOp + "] and value [" + propertyValue + "] not allowed here."))
            }

            propertyName = calculatePropertyName(propertyName)
            propertyValue = calculatePropertyValue(propertyValue)

            return addToCriteria(new PgJsonExpression(propertyName, jsonOp, jsonAttribute, sqlOp, propertyValue as String))
        }
    }

}
