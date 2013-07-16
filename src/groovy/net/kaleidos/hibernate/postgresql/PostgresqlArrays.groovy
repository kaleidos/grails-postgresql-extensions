package net.kaleidos.hibernate.postgresql

import grails.orm.HibernateCriteriaBuilder
import net.kaleidos.hibernate.criterion.array.PgContainsExpression
import net.kaleidos.hibernate.criterion.array.PgIsContainedByExpression
import net.kaleidos.hibernate.criterion.array.PgIsEmptyExpression
import net.kaleidos.hibernate.criterion.array.PgOverlapsExpression

import org.hibernate.criterion.Restrictions

class PostgresqlArrays {

    public PostgresqlArrays() {
        addContainsOperator()
        addIsContainedByOperator()
        addOverlapsOperator()
        addIsEmptyOperator()
    }

    private void addContainsOperator() {
        /**
         * Apply a "pgArrayContains" constraint to the named property
         * @param propertyName
         * @param value value
         * @return Criterion
         */
        org.hibernate.criterion.Restrictions.metaClass.'static'.pgArrayContains = { String propertyName, Object value ->
            return new PgContainsExpression(propertyName, value)
        }

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

            return addToCriteria(Restrictions.pgArrayContains(propertyName, propertyValue))
        }
    }

    private void addIsContainedByOperator() {
        /**
         * Apply a "pgArrayIsContainedBy" constraint to the named property
         * @param propertyName
         * @param value value
         * @return Criterion
         */
        org.hibernate.criterion.Restrictions.metaClass.'static'.pgArrayIsContainedBy = { String propertyName, Object value ->
            return new PgIsContainedByExpression(propertyName, value)
        }

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

            return addToCriteria(Restrictions.pgArrayIsContainedBy(propertyName, propertyValue))
        }
    }

    private void addOverlapsOperator() {
        /**
         * Apply a "pgArrayOverlaps" constraint to the named property
         * @param propertyName
         * @param value value
         * @return Criterion
         */
        org.hibernate.criterion.Restrictions.metaClass.'static'.pgArrayOverlaps = { String propertyName, Object value ->
            return new PgOverlapsExpression(propertyName, value)
        }

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

            return addToCriteria(Restrictions.pgArrayOverlaps(propertyName, propertyValue))
        }
    }
    
    private void addIsEmptyOperator() {
        /**
         * Apply a "pgArrayOverlaps" constraint to the named property
         * @param propertyName
         * @param value value
         * @return Criterion
         */
        org.hibernate.criterion.Restrictions.metaClass.'static'.pgArrayIsEmpty = { String propertyName ->
            return new PgIsEmptyExpression(propertyName)
        }

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

            return addToCriteria(Restrictions.pgArrayIsEmpty(propertyName))
        }
    }


}