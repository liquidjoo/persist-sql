package camp.nextstep.edu.persistsql;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SelectQueryFactory {

    public String createQuery(Class<?> clazz) {
        StringBuilder query = new StringBuilder();
        query.append(selectClause(clazz));
        query.append(fromClause(clazz));
        query.append(whereClause(clazz));

        return query.toString().trim();
    }

    private String selectClause(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(Entity.class)) {
            throw new IllegalStateException(clazz.getName() + " class는 Entity가 아닙니다.");
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("select ");

        String columnNames = String.join(", ", getEntityFieldNames(clazz));
        stringBuilder.append(columnNames);

        return stringBuilder.toString();
    }

    private String fromClause(Class<?> clazz) {
        if (isExistTableWithName(clazz)) {
            Table table = clazz.getAnnotation(Table.class);
            return " from " + table.name();
        }

        return " from " + clazz.getSimpleName();
    }

    private boolean isExistTableWithName(Class<?> clazz) {
        if (clazz.isAnnotationPresent(Table.class)) {
            Table table = clazz.getAnnotation(Table.class);
            return table.name() != null && !table.name().isBlank();
        }

        return false;
    }

    private String whereClause(Class<?> clazz) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" where ");

        var answer = getEntityFieldNames(clazz).stream()
                .map(columnName -> columnName + " = ? ")
                .collect(Collectors.joining("AND "));
        stringBuilder.append(answer);

        return stringBuilder.toString();
    }

    private List<String> getEntityFieldNames(Class<?> clazz) {
        List<String> fieldNames = Arrays.stream(clazz.getDeclaredFields())
                .filter(it -> it.isAnnotationPresent(Id.class) || it.isAnnotationPresent(Column.class))
                .map(this::extractColumnName)
                .collect(Collectors.toList());

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
