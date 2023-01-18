package camp.nextstep.edu.persistsql.sql.ddl;

import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CustomCreateTableGenerator {

    private final CustomType customType = new CustomType();

    public String createTable(Class<?> clazz) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("create table ");
        stringBuilder.append(getTableName(clazz));
        stringBuilder.append(getColumnsAndTypes(clazz));
        stringBuilder.append(getDbSystem());
        return stringBuilder.toString();
    }

    private String getDbSystem() {
        return "";
    }

    private String getTableName(Class<?> clazz) {
        if (clazz.isAnnotationPresent(Table.class)) {
            Table annotation = clazz.getAnnotation(Table.class);
            return annotation.name();
        }
        return clazz.getSimpleName();
    }

    private String getColumnsAndTypes(Class<?> clazz) {
        Field[] declaredFields = clazz.getDeclaredFields();
        List<Field> fields = Arrays.stream(declaredFields)
                .filter(field -> field.isAnnotationPresent(Id.class) || field.isAnnotationPresent(Column.class))
                .collect(Collectors.toList());

        List<String> types = new ArrayList<>();
        for (Field field : fields) {
            StringBuilder stringBuilder = new StringBuilder();
            if (field.isAnnotationPresent(Id.class)) {

                stringBuilder.append(getColumnNameBySingleQuote(field.getName()) + " " + customType.getTypeName(field.getType()));
                if (field.isAnnotationPresent(GeneratedValue.class)) {
                    GeneratedValue annotation = field.getAnnotation(GeneratedValue.class);
                    if (annotation.strategy() == GenerationType.AUTO) {
                        stringBuilder.append("AUTO_INCREMENT");
                    }
                }

                stringBuilder.append("PRIMARY KEY\n");
                types.add(stringBuilder.toString());

            } else if (field.isAnnotationPresent(Column.class)) {
                Column column = field.getAnnotation(Column.class);
                if (Objects.isNull(column)) {
                    stringBuilder.append(getColumnNameBySingleQuote(field.getName()) + " " + customType.getTypeName(field.getType()));
                    stringBuilder.append("PRIMARY KEY\n");
                    types.add(stringBuilder.toString());
                    continue;
                }

                if (StringUtils.hasText(column.name())) {
                    stringBuilder.append(getColumnNameBySingleQuote(column.name()) + " " + customType.getTypeName(field.getType()));
                    stringBuilder.append("PRIMARY KEY\n");
                    types.add(stringBuilder.toString());
                    continue;
                }

                stringBuilder.append(getColumnNameBySingleQuote(field.getName()) + " " + customType.getTypeName(field.getType()));
                stringBuilder.append("PRIMARY KEY\n");
                types.add(stringBuilder.toString());
            }
        }

        return types.stream()
                .collect(Collectors.joining(", ", " (", ")"));
    }

    private String getColumnNameBySingleQuote(String name) {
        return "'" + name + "'";
    }
}
