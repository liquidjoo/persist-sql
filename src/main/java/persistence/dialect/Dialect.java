package persistence.dialect;

import org.hibernate.MappingException;

import java.util.HashMap;
import java.util.Map;

public abstract class Dialect {

    private final Map<Integer, String> defaults = new HashMap<Integer, String>();

    protected void registerColumnType(int code, String name) {
        defaults.put( code, name );
    }

    public String get(final int typeCode) throws MappingException {
        final Integer integer = Integer.valueOf( typeCode );
        final String result = defaults.get( integer );
        if ( result == null ) {
            throw new MappingException( "No Dialect mapping for JDBC type: " + typeCode );
        }
        return result;
    }
}
