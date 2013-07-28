package net.kaleidos.hibernate;

import net.kaleidos.hibernate.usertype.HstoreType;
import net.kaleidos.hibernate.usertype.IntegerArrayType;
import net.kaleidos.hibernate.usertype.LongArrayType;
import net.kaleidos.hibernate.usertype.StringArrayType;
import net.kaleidos.hibernate.usertype.IdentityEnumArrayType;

import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.PostgreSQLDialect;
import org.hibernate.id.PersistentIdentifierGenerator;
import org.hibernate.id.SequenceGenerator;
import org.hibernate.type.Type;

import java.sql.Types;
import java.util.Properties;

public class PostgresqlExtensionsDialect extends PostgreSQLDialect {

    /**
     * Register postgresql types
     */
    public PostgresqlExtensionsDialect() {
        super();
        registerColumnType(Types.ARRAY, "array");
        registerColumnType(LongArrayType.SQLTYPE, "int8[]");
        registerColumnType(IntegerArrayType.SQLTYPE, "int[]");
        registerColumnType(IdentityEnumArrayType.SQLTYPE, "int[]");
        registerColumnType(StringArrayType.SQLTYPE, "varchar[]");
        registerColumnType(HstoreType.SQLTYPE, "hstore");
    }

    /**
     * Get the native identifier generator class.
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
            super.configure(type, params, dialect);
        }
    }
}
