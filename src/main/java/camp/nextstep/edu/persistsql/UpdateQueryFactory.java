package camp.nextstep.edu.persistsql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UpdateQueryFactory {

    public String createQuery(Class<?> clazz) {
        StringBuilder query = new StringBuilder();
        query.append(updateClause(clazz));
        query.append(setClause(clazz));
        query.append(whereClause(clazz));

        return query.toString().trim();
    }

    private String updateClause(Class<?> clazz) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("update ");
        stringBuilder.append(getTableName(clazz));
        stringBuilder.append(" ");

        return stringBuilder.toString();
    }

    private String getTableName(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(Table.class)) {
            return clazz.getSimpleName();
        }

        Table table = clazz.getAnnotation(Table.class);
        if (table.name() == null || table.name().isBlank()) {
            return clazz.getSimpleName();
        }

        return table.name();
    }

    private String setClause(Class<?> clazz) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("set ");

        String values = getEntityFields(clazz).entrySet()
                .stream()
                .map(entry -> entry.getKey() + " = " + entry.getValue())
                .collect(Collectors.joining(", "));
        stringBuilder.append(values);

        return stringBuilder.toString();
    }

    private Map<String, String> getEntityFields(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(it -> it.isAnnotationPresent(Id.class) || it.isAnnotationPresent(Column.class))
                .map(this::extractColumnName)
                .collect(Collectors.toMap(Function.identity(), it -> "?", (x, y) -> y, LinkedHashMap::new));
    }

    private String whereClause(Class<?> clazz) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" where ");

        String idColumnName = extractColumnName(getIdField(clazz));
        stringBuilder.append(idColumnName + " = ");
        stringBuilder.append("? ");

        return stringBuilder.toString();
    }

    private Field getIdField(Class<?> clazz) {
        Field[] declaredFields = clazz.getDeclaredFields();
        return Arrays.stream(declaredFields)
                .filter(filed -> filed.isAnnotationPresent(Id.class))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Id 필드가 존재하지 않습니다."));
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
