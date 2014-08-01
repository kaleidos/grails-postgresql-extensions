package net.kaleidos.hibernate.postgresql.criteria

import grails.orm.HibernateCriteriaBuilder
import net.kaleidos.hibernate.criterion.json.PgJsonExpression

class JsonCriterias {

    public JsonCriterias() {
        addHasFieldValueOperator()
    }

    private void addHasFieldValueOperator() {
        /**
         * Creates a "json has field value" Criterion based on the specified property name and value
         * @param propertyName The property name (json field)
         * @param jsonAttribute The json attribute
         * @param propertyValue The property value
         * @return A Criterion instance
         */
        HibernateCriteriaBuilder.metaClass.pgJsonHasFieldValue = { String propertyName, String jsonAttribute, Object propertyValue ->
            if (!validateSimpleExpression()) {
                throwRuntimeException(new IllegalArgumentException("Call to [pgJsonHasFieldValue] with propertyName [" +
                    propertyName + "], jsonAttribute [" + jsonAttribute + "] and value [" + propertyValue + "] not allowed here."))
            }

            propertyName = calculatePropertyName(propertyName)
            propertyValue = calculatePropertyValue(propertyValue)

            return addToCriteria(new PgJsonExpression(propertyName, jsonAttribute, propertyValue, "="))
        }
    }
}