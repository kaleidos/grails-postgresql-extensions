package net.kaleidos.hibernate.postgresql.criteria

import grails.orm.HibernateCriteriaBuilder
import net.kaleidos.hibernate.criterion.json.PgJsonExpression

class JsonCriterias {

    public JsonCriterias() {
        addEqualsOperator()
    }

    private void addEqualsOperator() {
        /**
         * Creates a "json equals" Criterion based on the specified property name and value
         * @param propertyName The property name (json field)
         * @param jsonAttribute The json attribute
         * @param propertyValue The property value
         * @return A Criterion instance
         */
        HibernateCriteriaBuilder.metaClass.pgJsonEquals = { String propertyName, String jsonAttribute, Object propertyValue ->
            if (!validateSimpleExpression()) {
                throwRuntimeException(new IllegalArgumentException("Call to [pgJsonEquals] with propertyName [" +
                    propertyName + "] and value [" + propertyValue + "] not allowed here."))
            }

            propertyName = calculatePropertyName(propertyName)
            propertyValue = calculatePropertyValue(propertyValue)

            return addToCriteria(new PgJsonExpression(propertyName, jsonAttribute, propertyValue, "="))
        }
    }
}