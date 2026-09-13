package ru.otus.java.pro.mapper;

import java.lang.reflect.Field;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {
    private final EntityClassMetaData<?> entityClassMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData<?> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() {
        return "select %s from %s".formatted(
                joinFieldNames(entityClassMetaData.getAllFields()),
                entityClassMetaData.getName());
    }

    @Override
    public String getSelectByIdSql() {
        return "%s where %s = ?".formatted(
                getSelectAllSql(),
                entityClassMetaData.getIdField().getName());
    }

    @Override
    public String getInsertSql() {
        var fields = entityClassMetaData.getFieldsWithoutId();
        var fieldNames = joinFieldNames(fields);
        var placeholders = fields.stream()
                .map(field -> "?")
                .collect(Collectors.joining(", "));

        return "insert into %s(%s) values (%s)".formatted(
                entityClassMetaData.getName(),
                fieldNames,
                placeholders);
    }

    @Override
    public String getUpdateSql() {
        var assignments = entityClassMetaData.getFieldsWithoutId().stream()
                .map(field -> field.getName() + " = ?")
                .collect(Collectors.joining(", "));

        return "update %s set %s where %s = ?".formatted(
                entityClassMetaData.getName(),
                assignments,
                entityClassMetaData.getIdField().getName());
    }

    private String joinFieldNames(Iterable<Field> fields) {
        var result = new StringBuilder();
        for (var field : fields) {
            if (!result.isEmpty()) {
                result.append(", ");
            }
            result.append(field.getName());
        }
        return result.toString();
    }
}
