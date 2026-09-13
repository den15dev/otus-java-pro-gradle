package ru.otus.java.pro.mapper;

import ru.otus.java.pro.core.repository.DataTemplate;
import ru.otus.java.pro.core.repository.DataTemplateException;
import ru.otus.java.pro.core.repository.executor.DbExecutor;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/** Сохратяет объект в базу, читает объект из базы */
@SuppressWarnings("java:S1068")
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(
            DbExecutor dbExecutor,
            EntitySQLMetaData entitySQLMetaData,
            EntityClassMetaData<T> entityClassMetaData
    ) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(
                connection,
                entitySQLMetaData.getSelectByIdSql(),
                List.of(id),
                rs -> {
                    try {
                        return rs.next() ? createObject(rs) : null;

                    } catch (Exception e) {
                        throw new DataTemplateException(e);
                    }
                }
        );
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(
                        connection,
                        entitySQLMetaData.getSelectAllSql(),
                        Collections.emptyList(),
                        rs -> {
                            List<T> result = new ArrayList<>();
                            try {
                                while (rs.next()) {
                                    result.add(createObject(rs));
                                }
                                return result;

                            } catch (Exception e) {
                                throw new DataTemplateException(e);
                            }
                        }
                )
                .orElseGet(Collections::emptyList);
    }

    @Override
    public long insert(Connection connection, T object) {
        try {
            var params = getFieldValues(object, entityClassMetaData.getFieldsWithoutId());
            return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), params);

        } catch (IllegalAccessException e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T object) {
        try {
            var params = new ArrayList<>(getFieldValues(object, entityClassMetaData.getFieldsWithoutId()));
            params.add(entityClassMetaData.getIdField().get(object));
            dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), params);

        } catch (IllegalAccessException e) {
            throw new DataTemplateException(e);
        }
    }

    private T createObject(ResultSet resultSet) throws Exception {
        var object = entityClassMetaData.getConstructor().newInstance();

        for (var field : entityClassMetaData.getAllFields()) {
            setFieldValue(object, field, resultSet);
        }

        return object;
    }

    private void setFieldValue(T object, Field field, ResultSet resultSet)
            throws SQLException, IllegalAccessException {
        var value = resultSet.getObject(field.getName());
        field.set(object, value);
    }

    private List<Object> getFieldValues(T object, List<Field> fields) throws IllegalAccessException {
        var values = new ArrayList<>(fields.size());
        for (var field : fields) {
            values.add(field.get(object));
        }
        return values;
    }
}
