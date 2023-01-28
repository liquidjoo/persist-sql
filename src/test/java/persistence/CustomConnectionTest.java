package persistence;

import org.junit.jupiter.api.Test;
import persistence.database.H2;

class CustomConnectionTest {

    @Test
    void name() {

        H2 h2Connection = CustomConnection.getH2Connection();
        h2Connection.executeByQuery("실행할 쿼리");
    }
}
