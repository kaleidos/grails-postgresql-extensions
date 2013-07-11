package net.kaleidos.hibernate.postgresql

import grails.orm.HibernateCriteriaBuilder
import net.kaleidos.hibernate.criterion.PgContainsExpression
import net.kaleidos.hibernate.criterion.PgIsContainedByExpression
import org.hibernate.criterion.Restrictions

class PostgresqlArrays {

    public PostgresqlArrays() {
        addContainsOperator()
    }

    private void addContainsOperator() {

        /**
         * Apply a "pgContains" constraint to the named property
         * @param propertyName
         * @param value value
         * @return Criterion
         */
        org.hibernate.criterion.Restrictions.metaClass.'static'.pgContains = { String propertyName, Object value ->
            return new PgContainsExpression(propertyName, value)
        }

        /**
         * Apply a "pgIsContainedBy" constraint to the named property
         * @param propertyName
         * @param value value
         * @return Criterion
         */
        org.hibernate.criterion.Restrictions.metaClass.'static'.pgIsContainedBy = { String propertyName, Object value ->
            return new PgIsContainedByExpression(propertyName, value)
        }

        /**
         * Creates a "contains in native array" Criterion based on the specified property name and value
         * @param propertyName The property name
         * @param propertyValue The property value
         * @return A Criterion instance
         */
        HibernateCriteriaBuilder.metaClass.pgContains = { String propertyName, Object propertyValue ->
            if (!validateSimpleExpression()) {
                throwRuntimeException(new IllegalArgumentException("Call to [pgContains] with propertyName [" +
                        propertyName + "] and value [" + propertyValue + "] not allowed here."));
            }

            propertyName = calculatePropertyName(propertyName);
            propertyValue = calculatePropertyValue(propertyValue);

            return addToCriteria(Restrictions.pgContains(propertyName, propertyValue));
        }
        
        /**
         * Creates a "is contained by in native array" Criterion based on the specified property name and value
         * @param propertyName The property name
         * @param propertyValue The property value
         * @return A Criterion instance
         */
        HibernateCriteriaBuilder.metaClass.pgIsContainedBy = { String propertyName, Object propertyValue ->
            if (!validateSimpleExpression()) {
                throwRuntimeException(new IllegalArgumentException("Call to [pgIsContainedBy] with propertyName [" +
                        propertyName + "] and value [" + propertyValue + "] not allowed here."));
            }

            propertyName = calculatePropertyName(propertyName);
            propertyValue = calculatePropertyValue(propertyValue);

            return addToCriteria(Restrictions.pgIsContainedBy(propertyName, propertyValue));
        }
    }

}