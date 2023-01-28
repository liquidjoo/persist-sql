package persistence.dialect;

import java.sql.Types;

public class H2Dialect extends Dialect{

    public H2Dialect() {
        registerColumnType( Types.BOOLEAN, "boolean" );
        registerColumnType( Types.BIGINT, "bigint" );
        registerColumnType( Types.BINARY, "binary" );
        registerColumnType( Types.BIT, "boolean" );
        registerColumnType( Types.CHAR, "char" );
        registerColumnType( Types.DATE, "date" );
        registerColumnType( Types.DECIMAL, "decimal" );
        registerColumnType( Types.DOUBLE, "double" );
        registerColumnType( Types.FLOAT, "float" );
        registerColumnType( Types.INTEGER, "integer" );
        registerColumnType( Types.LONGVARBINARY, "longvarbinary" );
        // H2 does define "longvarchar", but it is a simple alias to "varchar"
        registerColumnType( Types.LONGVARCHAR, String.format( "varchar(%d)", Integer.MAX_VALUE ) );
        registerColumnType( Types.REAL, "real" );
        registerColumnType( Types.SMALLINT, "smallint" );
        registerColumnType( Types.TINYINT, "tinyint" );
        registerColumnType( Types.TIME, "time" );
        registerColumnType( Types.TIMESTAMP, "timestamp" );
        registerColumnType( Types.VARCHAR, "varchar" );
        registerColumnType( Types.BLOB, "blob" );
        registerColumnType( Types.CLOB, "clob" );
    }
}
