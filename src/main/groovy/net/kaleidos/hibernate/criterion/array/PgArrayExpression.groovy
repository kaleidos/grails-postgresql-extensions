package net.kaleidos.hibernate.criterion.array

import groovy.transform.CompileStatic
import net.kaleidos.hibernate.usertype.ArrayType
import net.kaleidos.hibernate.utils.PgArrayUtils
import org.hibernate.Criteria
import org.hibernate.HibernateException
import org.hibernate.annotations.common.util.StringHelper
import org.hibernate.criterion.CriteriaQuery
import org.hibernate.criterion.Criterion
import org.hibernate.engine.spi.TypedValue
import org.hibernate.type.CustomType
import org.hibernate.type.Type

@CompileStatic
class PgArrayExpression implements Criterion {

    private static final long serialVersionUID = 2872183637309166619L

    private final String propertyName
    private final Object value
    private final String op

    protected PgArrayExpression(String propertyName, Object value, String op) {
        this.propertyName = propertyName
        this.value = value
        this.op = op
    }

    @Override
    String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
        ArrayType arrayType = checkAndGetArrayType(criteria, criteriaQuery)

        String postgresArrayType = PgArrayUtils.getNativeSqlType(arrayType.getTypeClass()) + "[]"

        return StringHelper.join(
            " and ",
            StringHelper.suffix(criteriaQuery.findColumns(propertyName, criteria), " ${op} CAST(? as ${postgresArrayType})")
        )
    }

    @Override
    TypedValue[] getTypedValues(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
        ArrayType arrayType = checkAndGetArrayType(criteria, criteriaQuery)

        Object[] arrValue
        if (arrayType.typeClass.isEnum()) {
            arrValue = PgArrayUtils.getValueAsArrayOfType(value, Integer, mapValueToEnumOrdinal())
        } else {
            arrValue = PgArrayUtils.getValueAsArrayOfType(value, arrayType.typeClass)
        }

        return criteriaQuery.getTypedValue(criteria, propertyName, arrValue) as TypedValue[]
    }

    private PgArrayUtils.MapFunction mapValueToEnumOrdinal() {
        { Object o ->
            try {
                return (o as Enum).ordinal()
            } catch (ClassCastException e) {
                throw new HibernateException("Unable to cast object ${o} to Enum", e)
            }
        } as PgArrayUtils.MapFunction
    }

    private ArrayType checkAndGetArrayType(Criteria criteria, CriteriaQuery criteriaQuery) {
        Type propertyType = criteriaQuery.getType(criteria, propertyName)

        if (!(propertyType instanceof CustomType) || !((propertyType as CustomType).userType instanceof ArrayType)) {
            throw new HibernateException("Property is not an instance of the postgres type ArrayType. Type is: ${propertyType.class}")
        }

        return (propertyType as CustomType).getUserType() as ArrayType
    }
}
