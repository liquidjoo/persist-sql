package persistence.sql.ddl;

public class CustomTableGenerator {

    private final CustomCreateTableGenerator customCreateTableGenerator = new CustomCreateTableGenerator();

    public String createTable(Class<?> clazz) {
        return customCreateTableGenerator.createTable(clazz);
    }

}
