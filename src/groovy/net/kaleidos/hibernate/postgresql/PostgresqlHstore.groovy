package net.kaleidos.hibernate.postgresql

import grails.orm.HibernateCriteriaBuilder


import org.hibernate.criterion.Restrictions

class PostgresqlHstore {

    public PostgresqlHstore() {
        //addContainsOperator()
    }

    // private void addContainsOperator() {
    //     /**
    //      * Creates a "contains in native array" Criterion based on the specified property name and value
    //      * @param propertyName The property name
    //      * @param propertyValue The property value
    //      * @return A Criterion instance
    //      */
    //     HibernateCriteriaBuilder.metaClass.pgArrayContains = { String propertyName, Object propertyValue ->
    //         if (!validateSimpleExpression()) {
    //             throwRuntimeException(new IllegalArgumentException("Call to [pgArrayContains] with propertyName [" +
    //                     propertyName + "] and value [" + propertyValue + "] not allowed here."))
    //         }

    //         propertyName = calculatePropertyName(propertyName)
    //         propertyValue = calculatePropertyValue(propertyValue)

    //         return addToCriteria(new PgArrayExpression(propertyName, propertyValue, "@>"))
    //     }
    // }

    // private void addIsContainedByOperator() {
    //     /**
    //      * Creates a "is contained by in native array" Criterion based on the specified property name and value
    //      * @param propertyName The property name
    //      * @param propertyValue The property value
    //      * @return A Criterion instance
    //      */
    //     HibernateCriteriaBuilder.metaClass.pgArrayIsContainedBy = { String propertyName, Object propertyValue ->
    //         if (!validateSimpleExpression()) {
    //             throwRuntimeException(new IllegalArgumentException("Call to [pgArrayIsContainedBy] with propertyName [" +
    //                     propertyName + "] and value [" + propertyValue + "] not allowed here."))
    //         }

    //         propertyName = calculatePropertyName(propertyName)
    //         propertyValue = calculatePropertyValue(propertyValue)

    //         return addToCriteria(new PgArrayExpression(propertyName, propertyValue, "<@"))
    //     }
    // }

    // private void addOverlapsOperator() {
    //     /**
    //      * Creates a "overlap in native array" Criterion based on the specified property name and value
    //      * @param propertyName The property name
    //      * @param propertyValue The property value
    //      * @return A Criterion instance
    //      */
    //     HibernateCriteriaBuilder.metaClass.pgArrayOverlaps = { String propertyName, Object propertyValue ->
    //         if (!validateSimpleExpression()) {
    //             throwRuntimeException(new IllegalArgumentException("Call to [pgOverlap] with propertyName [" +
    //                     propertyName + "] and value [" + propertyValue + "] not allowed here."))
    //         }

    //         propertyName = calculatePropertyName(propertyName)
    //         propertyValue = calculatePropertyValue(propertyValue)

    //         return addToCriteria(new PgArrayExpression(propertyName, propertyValue, "&&"))
    //     }
    // }

    // private void addIsEmptyOperator() {
    //     /**
    //      * Creates an "is empty array" Criterion based on the specified property name
    //      * @param propertyName The property name
    //      * @return A Criterion instance
    //      */
    //     HibernateCriteriaBuilder.metaClass.pgArrayIsEmpty = { String propertyName ->
    //         if (!validateSimpleExpression()) {
    //             throwRuntimeException(new IllegalArgumentException("Call to [pgArrayIsEmpty] with propertyName [" +
    //                     propertyName + "] not allowed here."))
    //         }

    //         propertyName = calculatePropertyName(propertyName)

    //         return addToCriteria(new PgEmptinessExpression(propertyName, "="))
    //     }
    // }

    // private void addIsNotEmptyOperator() {
    //     /**
    //      * Creates an "is empty array" Criterion based on the specified property name
    //      * @param propertyName The property name
    //      * @return A Criterion instance
    //      */
    //     HibernateCriteriaBuilder.metaClass.pgArrayIsNotEmpty = { String propertyName ->
    //         if (!validateSimpleExpression()) {
    //             throwRuntimeException(new IllegalArgumentException("Call to [pgArrayIsNotEmpty] with propertyName [" +
    //                     propertyName + "] not allowed here."))
    //         }

    //         propertyName = calculatePropertyName(propertyName)

    //         return addToCriteria(new PgEmptinessExpression(propertyName, "<>"))
    //     }
    // }


}