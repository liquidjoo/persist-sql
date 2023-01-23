package camp.nextstep.edu.persistsql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.util.Arrays;

public class DeleteQueryFactory {

    public String createQuery(Class<?> clazz) {
        StringBuilder query = new StringBuilder();
        query.append(tableClause(clazz));
        query.append(whereClause(clazz));

        return query.toString().trim();
    }

    private String tableClause(Class<?> clazz) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("delete from ");
        stringBuilder.append(getTableName(clazz));

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
