package camp.nextstep.edu.persistsql;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class InsertQueryFactory {

    public <T> String createQuery(Class<T> clazz) {
        if (!clazz.isAnnotationPresent(Entity.class)) {
            throw new IllegalStateException(clazz.getSimpleName() + " class는 Entity가 아닙니다.");
        }

        StringBuilder query = new StringBuilder();
        query.append(tableClause(clazz));
        query.append(columnsClause(clazz));
        query.append(valueClause(clazz));

        return query.toString().trim();
    }

    private String tableClause(Class<?> clazz) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("insert into ");
        if (!clazz.isAnnotationPresent(Table.class)) {
            stringBuilder.append(clazz.getSimpleName());
            return stringBuilder.toString();
        }

        Table tableAnnotation = clazz.getAnnotation(Table.class);
        String tableName = tableAnnotation.name();
        if (tableName != null && !tableName.isBlank()) {
            stringBuilder.append(tableName);
            return stringBuilder.toString();
        }

        stringBuilder.append(clazz.getSimpleName());
        return stringBuilder.toString();
    }

    private String columnsClause(Class<?> clazz) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(");

        String columns = String.join(", ", getEntityFields(clazz).keySet());
        stringBuilder.append(columns);
        stringBuilder.append(") ");

        return stringBuilder.toString();
    }

    private String valueClause(Class<?> clazz) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("values (");

        String values = String.join(", ", getEntityFields(clazz).values());
        stringBuilder.append(values);

        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    private Map<String, String> getEntityFields(Class<?> clazz) {
        var fieldNames = Arrays.stream(clazz.getDeclaredFields())
                .filter(it -> it.isAnnotationPresent(Id.class) || it.isAnnotationPresent(Column.class))
                .map(this::extractColumnName)
                .collect(Collectors.toMap(Function.identity(), it -> "?", (x, y) -> y, LinkedHashMap::new));

        return fieldNames;
    }

    private String extractColumnName(Field field) {
        if (field.isAnnotationPresent(Column.class)) {
            Column column = field.getAnnotation(Column.class);
            String columnName = column.name();
            if (columnName != null && !columnName.isBlank()) {
                return columnName;
            }
        }

        return field.getName();
    }
}
