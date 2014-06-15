package net.kaleidos.hibernate.postgresql.criteria

import grails.orm.HibernateCriteriaBuilder
import net.kaleidos.hibernate.criterion.array.PgArrayExpression
import net.kaleidos.hibernate.criterion.array.PgEmptinessExpression

class ArrayCriterias {

    public ArrayCriterias() {
        addContainsOperator()
        addIsContainedByOperator()
        addOverlapsOperator()
        addIsEmptyOperator()
        addIsNotEmptyOperator()
        addIsEmptyOrContainsOperator()
        addEqualsOperator()
        addNotEqualsOperator()
    }

    private void addContainsOperator() {
        /**
         * Creates a "contains in native array" Criterion based on the specified property name and value
         * @param propertyName The property name
         * @param propertyValue The property value
         * @return A Criterion instance
         */
        HibernateCriteriaBuilder.metaClass.pgArrayContains = { String propertyName, Object propertyValue ->
            if (!validateSimpleExpression()) {
                throwRuntimeException(new IllegalArgumentException("Call to [pgArrayContains] with propertyName [" +
                        propertyName + "] and value [" + propertyValue + "] not allowed here."))
            }

            propertyName = calculatePropertyName(propertyName)
            propertyValue = calculatePropertyValue(propertyValue)

            return addToCriteria(new PgArrayExpression(propertyName, propertyValue, "@>"))
        }
    }

    private void addIsContainedByOperator() {
        /**
         * Creates a "is contained by in native array" Criterion based on the specified property name and value
         * @param propertyName The property name
         * @param propertyValue The property value
         * @return A Criterion instance
         */
        HibernateCriteriaBuilder.metaClass.pgArrayIsContainedBy = { String propertyName, Object propertyValue ->
            if (!validateSimpleExpression()) {
                throwRuntimeException(new IllegalArgumentException("Call to [pgArrayIsContainedBy] with propertyName [" +
                        propertyName + "] and value [" + propertyValue + "] not allowed here."))
            }

            propertyName = calculatePropertyName(propertyName)
            propertyValue = calculatePropertyValue(propertyValue)

            return addToCriteria(new PgArrayExpression(propertyName, propertyValue, "<@"))
        }
    }

    private void addOverlapsOperator() {
        /**
         * Creates a "overlap in native array" Criterion based on the specified property name and value
         * @param propertyName The property name
         * @param propertyValue The property value
         * @return A Criterion instance
         */
        HibernateCriteriaBuilder.metaClass.pgArrayOverlaps = { String propertyName, Object propertyValue ->
            if (!validateSimpleExpression()) {
                throwRuntimeException(new IllegalArgumentException("Call to [pgOverlap] with propertyName [" +
                        propertyName + "] and value [" + propertyValue + "] not allowed here."))
            }

            propertyName = calculatePropertyName(propertyName)
            propertyValue = calculatePropertyValue(propertyValue)

            return addToCriteria(new PgArrayExpression(propertyName, propertyValue, "&&"))
        }
    }

    private void addIsEmptyOperator() {
        /**
         * Creates an "is empty array" Criterion based on the specified property name
         * @param propertyName The property name
         * @return A Criterion instance
         */
        HibernateCriteriaBuilder.metaClass.pgArrayIsEmpty = { String propertyName ->
            if (!validateSimpleExpression()) {
                throwRuntimeException(new IllegalArgumentException("Call to [pgArrayIsEmpty] with propertyName [" +
                        propertyName + "] not allowed here."))
            }

            propertyName = calculatePropertyName(propertyName)

            return addToCriteria(new PgEmptinessExpression(propertyName, "="))
        }
    }

    private void addIsNotEmptyOperator() {
        /**
         * Creates an "is empty array" Criterion based on the specified property name
         * @param propertyName The property name
         * @return A Criterion instance
         */
        HibernateCriteriaBuilder.metaClass.pgArrayIsNotEmpty = { String propertyName ->
            if (!validateSimpleExpression()) {
                throwRuntimeException(new IllegalArgumentException("Call to [pgArrayIsNotEmpty] with propertyName [" +
                        propertyName + "] not allowed here."))
            }

            propertyName = calculatePropertyName(propertyName)

            return addToCriteria(new PgEmptinessExpression(propertyName, "<>"))
        }
    }

    private void addIsEmptyOrContainsOperator() {
        /**
         * Creates a "contains in native array" or "is empty native array" Criterion based on the specified property name and value
         * If the propertyValue is empty, the 'contains' operator is used and if the propertyValue is not empty, the 'isEmpty'
         * operator is used.
         *
         * @param propertyName The property name
         * @param propertyValue The property value
         * @return A Criterion instance
         */
        HibernateCriteriaBuilder.metaClass.pgArrayIsEmptyOrContains = { String propertyName, Object propertyValue ->
            if (!validateSimpleExpression()) {
                throwRuntimeException(new IllegalArgumentException("Call to [pgArrayIsEmptyOrContains] with propertyName [" +
                        propertyName + "] and value [" + propertyValue + "] not allowed here."))
            }

            propertyName = calculatePropertyName(propertyName)
            propertyValue = calculatePropertyValue(propertyValue)

            if (propertyValue) {
                return addToCriteria(new PgArrayExpression(propertyName, propertyValue, "@>"))
            } else {
                return addToCriteria(new PgEmptinessExpression(propertyName, "="))
            }
        }
    }

    private void addEqualsOperator() {
        /**
         * Creates a "equals in native array" Criterion based on the specified property name and value
         * @param propertyName The property name
         * @param propertyValue The property value
         * @return A Criterion instance
         */
        HibernateCriteriaBuilder.metaClass.pgArrayEquals = { String propertyName, Object propertyValue ->
            if (!validateSimpleExpression()) {
                throwRuntimeException(new IllegalArgumentException("Call to [pgArrayEquals] with propertyName [" +
                        propertyName + "] and value [" + propertyValue + "] not allowed here."))
            }

            propertyName = calculatePropertyName(propertyName)
            propertyValue = calculatePropertyValue(propertyValue)

            return addToCriteria(new PgArrayExpression(propertyName, propertyValue, "="))
        }
    }

    private void addNotEqualsOperator() {
        /**
         * Creates a "not equals in native array" Criterion based on the specified property name and value
         * @param propertyName The property name
         * @param propertyValue The property value
         * @return A Criterion instance
         */
        HibernateCriteriaBuilder.metaClass.pgArrayNotEquals = { String propertyName, Object propertyValue ->
            if (!validateSimpleExpression()) {
                throwRuntimeException(new IllegalArgumentException("Call to [pgArrayNotEquals] with propertyName [" +
                        propertyName + "] and value [" + propertyValue + "] not allowed here."))
            }

            propertyName = calculatePropertyName(propertyName)
            propertyValue = calculatePropertyValue(propertyValue)

            return addToCriteria(new PgArrayExpression(propertyName, propertyValue, "<>"))
        }
    }
}
