package camp.nextstep.edu.persistsql.sql.dml;

import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class CustomSelect {

    private final CustomSelectQueryBuilder customSelectQueryBuilder = new CustomSelectQueryBuilder();

    public String toStatementString(Class<?> clazz) {
        return customSelectQueryBuilder.translatorSelectQuery(clazz);
    }

    private static class CustomSelectQueryBuilder {

        private Map<String, String> columns = new LinkedHashMap<>();

        public String translatorSelectQuery(Class<?> clazz) {
            validateEntityAnnotation(clazz);
            StringBuilder query = new StringBuilder();
            query.append(selectClause(clazz));
            query.append(fromClause(clazz));
            query.append(whereClause(clazz));
            return query.toString();
        }

        private String selectClause(Class<?> clazz) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("select ");
            String columns = String.join(", ", getColumns(clazz).keySet());

            return stringBuilder.append(columns).toString();
        }

        private String fromClause(Class<?> clazz) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(" from ");
            if (clazz.isAnnotationPresent(Table.class)) {
                Table annotation = clazz.getAnnotation(Table.class);
                stringBuilder.append(annotation.name());
                return stringBuilder.toString();
            }

            stringBuilder.append(clazz.getSimpleName());
            return stringBuilder.toString();
        }

        private String whereClause(Class<?> clazz) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(" where ");
            stringBuilder.append(getIdFields(clazz).getName());
            stringBuilder.append(" = ");
            stringBuilder.append(" ?");
            return stringBuilder.toString();
        }

        private String whereClause(String selectQuery, Class<?> clazz) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(selectQuery);
            stringBuilder.append(" where ");
            stringBuilder.append(" = ");
            stringBuilder.append(" ?");
            return stringBuilder.toString();
        }

        private Field getIdFields(Class<?> clazz) {
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                if (declaredField.isAnnotationPresent(Id.class)) {
                    return declaredField;
                }
            }
            throw new IllegalArgumentException();
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
