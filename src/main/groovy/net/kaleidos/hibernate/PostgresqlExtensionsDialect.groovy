package net.kaleidos.hibernate

import grails.util.Holders
import groovy.transform.CompileStatic
import net.kaleidos.hibernate.usertype.ArrayType
import net.kaleidos.hibernate.usertype.HstoreMapType
import net.kaleidos.hibernate.usertype.JsonMapType
import net.kaleidos.hibernate.usertype.JsonbMapType
import org.hibernate.dialect.Dialect
import org.hibernate.dialect.PostgreSQL9Dialect
import org.hibernate.id.SequenceGenerator
import org.hibernate.type.Type

import java.sql.Types

@CompileStatic
class PostgresqlExtensionsDialect extends PostgreSQL9Dialect {

    private static final String SEQUENCE_PER_TABLE = 'dataSource.postgresql.extensions.sequence_per_table'

    /**
     * Register postgresql types
     */
    PostgresqlExtensionsDialect() {
        super()
        registerColumnType(Types.ARRAY, 'array')
        registerColumnType(ArrayType.LONG_ARRAY, 'int8[]')
        registerColumnType(ArrayType.INTEGER_ARRAY, 'int[]')
        registerColumnType(ArrayType.ENUM_INTEGER_ARRAY, 'int[]')
        registerColumnType(ArrayType.STRING_ARRAY, 'varchar[]')
        registerColumnType(ArrayType.DOUBLE_ARRAY, 'float8[]')
        registerColumnType(ArrayType.FLOAT_ARRAY, 'float[]')
        registerColumnType(ArrayType.UUID_ARRAY, 'uuid[]')
        registerColumnType(HstoreMapType.SQLTYPE, 'hstore')
        registerColumnType(JsonMapType.SQLTYPE, 'json')
        registerColumnType(JsonbMapType.SQLTYPE, 'jsonb')
    }

    /**
     * Get the native identifier generator class.
     *
     * @return TableNameSequenceGenerator.
     */
    @Override
    Class<?> getNativeIdentifierGeneratorClass() {
        TableNameSequenceGenerator
    }

    /**
     * Creates a sequence per table instead of the default behavior of one sequence.
     * From <a href='http://www.hibernate.org/296.html'>http://www.hibernate.org/296.html</a>
     */
    static class TableNameSequenceGenerator extends SequenceGenerator {

        /**
         * {@inheritDoc}
         * If the parameters do not contain a {@link SequenceGenerator#SEQUENCE} name, we
         * assign one based on the table name.
         */
        @Override
        void configure(final Type type, final Properties params, final Dialect dialect) {

            Boolean sequencePerTable = Holders.config.getProperty(SEQUENCE_PER_TABLE, Boolean, true)

            if (sequencePerTable) {
                if (!params.getProperty(SEQUENCE)) {
                    String tableName = params.getProperty(TABLE)
                    String schemaName = params.getProperty('schemaName')
                    if (schemaName) {
                        params.setProperty(SCHEMA, schemaName)
                    }
                    if (tableName) {
                        params.setProperty(SEQUENCE, "seq_${tableName}")
                    }
                }
            }
            super.configure(type, params, dialect)
        }
    }
}
