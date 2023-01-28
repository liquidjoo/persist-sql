package persistence.sql.ddl;

import persistence.dialect.H2Dialect;
import org.hibernate.type.descriptor.sql.JdbcTypeJavaClassMappings;

public class CustomType {

    private final JdbcTypeJavaClassMappings jdbcTypeJavaClassMappings = JdbcTypeJavaClassMappings.INSTANCE;
    private final H2Dialect h2Dialect = new H2Dialect();

    protected String getTypeName(Class<?> clazz) {
        int code = jdbcTypeJavaClassMappings.determineJdbcTypeCodeForJavaClass(clazz);
        return h2Dialect.get(code);
    }
}
