package persistence.sql.dml;

import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class CustomInsert {

    private final CustomInsertQueryBuilder customInsertQueryBuilder = new CustomInsertQueryBuilder();

    public String toStatementString(Class<?> clazz) {
        return customInsertQueryBuilder.translatorInsertQuery(clazz);
    }

    private static class CustomInsertQueryBuilder {

        private Map<String, String> columns = new LinkedHashMap<>();

        public String translatorInsertQuery(Class<?> clazz) {
            validateEntityAnnotation(clazz);
            StringBuilder query = new StringBuilder();
            query.append(insertClause());
            query.append(tableClause(clazz));
            query.append(columnsClause(clazz));
            query.append(valueClause(clazz));
            return query.toString();
        }

        private String insertClause() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("insert ");
            return stringBuilder.toString();
        }

        private String tableClause(Class<?> clazz) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("into ");
            if (clazz.isAnnotationPresent(Table.class)) {
                Table annotation = clazz.getAnnotation(Table.class);
                stringBuilder.append(annotation.name());
                return stringBuilder.toString();
            }

            stringBuilder.append(clazz.getSimpleName());
            return stringBuilder.toString();
        }

        private String columnsClause(Class<?> clazz) {
            return getColumns(clazz).keySet().stream().collect(Collectors.joining(", ", " (", ")"));
        }

        private String valueClause(Class<?> clazz) {
            String values = getColumns(clazz).values().stream().collect(Collectors.joining(", ", " (", ")"));

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(" values ");
            stringBuilder.append(values);
            return stringBuilder.toString();
        }

        private Map<String, String> getColumns(Class<?> clazz) {
            Field[] declaredFields = clazz.getDeclaredFields();
            List<Field> fields = Arrays.stream(declaredFields)
                    .filter(field -> field.isAnnotationPresent(Id.class) || field.isAnnotationPresent(Column.class))
                    .collect(Collectors.toList());

            for (Field field : fields) {
                if (field.isAnnotationPresent(Id.class)) {
                    Column column = field.getAnnotation(Column.class);
                    if (Objects.isNull(column)) {
                        columns.put(field.getName(), "?");
                        continue;
                    }

                    if (StringUtils.hasText(column.name())) {
                        columns.put(column.name(), "?");
                        continue;
                    }

                    columns.put(field.getName(), "?");
                    continue;
                }


                Column column = field.getAnnotation(Column.class);
                if (StringUtils.hasText(column.name())) {
                    columns.put(column.name(), "?");
                    continue;
                }

                columns.put(field.getName(), "?");
            }

            return columns;
        }

        private void validateEntityAnnotation(Class<?> clazz) {
            if (!clazz.isAnnotationPresent(Entity.class)) {
                throw new IllegalArgumentException();
            }
        }


    }
}
