package net.kaleidos.hibernate;

import grails.util.Holders;
import net.kaleidos.hibernate.usertype.ArrayType;
import net.kaleidos.hibernate.usertype.HstoreType;
import net.kaleidos.hibernate.usertype.JsonMapType;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.PostgreSQL81Dialect;
import org.hibernate.id.PersistentIdentifierGenerator;
import org.hibernate.id.SequenceGenerator;
import org.hibernate.type.Type;

import java.sql.Types;
import java.util.Properties;

public class PostgresqlExtensionsDialect extends PostgreSQL81Dialect {

    /**
     * Register postgresql types
     */
    public PostgresqlExtensionsDialect() {
        super();
        registerColumnType(Types.ARRAY, "array");
        registerColumnType(ArrayType.LONG_ARRAY, "int8[]");
        registerColumnType(ArrayType.INTEGER_ARRAY, "int[]");
        registerColumnType(ArrayType.ENUM_INTEGER_ARRAY, "int[]");
        registerColumnType(ArrayType.STRING_ARRAY, "varchar[]");
        registerColumnType(ArrayType.DOUBLE_ARRAY, "float8[]");
        registerColumnType(ArrayType.FLOAT_ARRAY, "float[]");
        registerColumnType(HstoreType.SQLTYPE, "hstore");
        registerColumnType(JsonMapType.SQLTYPE, "json");
    }

    /**
     * Get the native identifier generator class.
     *
     * @return TableNameSequenceGenerator.
     */
    @Override
    public Class<?> getNativeIdentifierGeneratorClass() {
        return TableNameSequenceGenerator.class;
    }

    /**
     * Creates a sequence per table instead of the default behavior of one sequence.
     * From <a href='http://www.hibernate.org/296.html'>http://www.hibernate.org/296.html</a>
     */
    public static class TableNameSequenceGenerator extends SequenceGenerator {

        /**
         * {@inheritDoc}
         * If the parameters do not contain a {@link SequenceGenerator#SEQUENCE} name, we
         * assign one based on the table name.
         */
        @Override
        public void configure(final Type type, final Properties params, final Dialect dialect) {

            Boolean sequencePerTable = (Boolean) Holders.getFlatConfig().get("dataSource.postgresql.extensions.sequence_per_table");

            if ((sequencePerTable == null) || sequencePerTable) {
                if (params.getProperty(SEQUENCE) == null || params.getProperty(SEQUENCE).length() == 0) {
                    String tableName = params.getProperty(PersistentIdentifierGenerator.TABLE);
                    String schemaName = params.getProperty("schemaName");
                    if (schemaName != null) {
                        params.setProperty(PersistentIdentifierGenerator.SCHEMA, schemaName);
                    }
                    if (tableName != null) {
                        params.setProperty(SEQUENCE, "seq_" + tableName);
                    }
                }
            }
            super.configure(type, params, dialect);
        }
    }
}