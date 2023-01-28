package persistence;

import persistence.database.H2;

public class CustomConnection {

    private static final H2 H_2_IN_MEMORY = new H2();

    public static H2 getH2Connection() {
        return H_2_IN_MEMORY;
    }


}
