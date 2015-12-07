package net.kaleidos.hibernate.postgresql.criteria

import grails.orm.HibernateCriteriaBuilder
import net.kaleidos.hibernate.criterion.json.PgJsonExpression
import net.kaleidos.hibernate.criterion.json.PgJsonbOperator

class JsonCriterias {

    JsonCriterias() {
        addHasFieldValueOperator()
        addPgJsonbContainsOperator()
        addPgJsonbIsContainedOperator()
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

            return addToCriteria(new PgJsonExpression(propertyName, jsonAttribute, propertyValue, "="))
        }
    }

    private void addPgJsonbContainsOperator() {
        /**
         * Creates a "json constains another json" Criterion based on the specified property name and value
         * @param propertyName The property name (jsonb field)
         * @param propertyValue The property value
         * @return A Criterion instance
         */
        HibernateCriteriaBuilder.metaClass.pgJsonbContains = { String propertyName, propertyValue ->
            if (!validateSimpleExpression()) {
                throwRuntimeException(new IllegalArgumentException("Call to [pgJsonContains] with propertyName [" +
                    propertyName + "] and value [" + propertyValue + "] not allowed here."))
            }

            propertyName = calculatePropertyName(propertyName)
            propertyValue = calculatePropertyValue(propertyValue)

            return addToCriteria(new PgJsonbOperator(propertyName, propertyValue, "@>"))
        }
    }


    private void addPgJsonbIsContainedOperator() {
        /**
         * Creates a "json is contained in another json" Criterion based on the specified property name and value
         * @param propertyName The property name (jsonb field)
         * @param propertyValue The property value
         * @return A Criterion instance
         */
        HibernateCriteriaBuilder.metaClass.pgJsonbIsContained = { String propertyName, propertyValue ->
            if (!validateSimpleExpression()) {
                throwRuntimeException(new IllegalArgumentException("Call to [pgJsonIsContained] with propertyName [" +
                    propertyName + "] and value [" + propertyValue + "] not allowed here."))
            }

            propertyName = calculatePropertyName(propertyName)
            propertyValue = calculatePropertyValue(propertyValue)

            return addToCriteria(new PgJsonbOperator(propertyName, propertyValue, "<@"))
        }
    }
}
