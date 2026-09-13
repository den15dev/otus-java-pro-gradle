package ru.otus.java.pro.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    private final Class<T> clazz;
    private final Constructor<T> constructor;
    private final List<Field> allFields;
    private final Field idField;
    private final List<Field> fieldsWithoutId;

    public EntityClassMetaDataImpl(Class<T> clazz) {
        this.clazz = clazz;
        this.constructor = findConstructor(clazz);
        this.allFields = Arrays.asList(clazz.getDeclaredFields());
        this.idField = findIdField(allFields);
        this.fieldsWithoutId = allFields.stream()
            .filter(field -> field != idField)
            .toList();

        allFields.forEach(field -> field.setAccessible(true));
        constructor.setAccessible(true);
    }

    @Override
    public String getName() {
        return clazz.getSimpleName();
    }

    @Override
    public Constructor<T> getConstructor() {
        return constructor;
    }

    @Override
    public Field getIdField() {
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        return allFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return fieldsWithoutId;
    }

    private Constructor<T> findConstructor(Class<T> clazz) {
        try {
            return clazz.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(
                    "Класс %s должен иметь конструктор без параметров".formatted(clazz.getName()), e);
        }
    }

    private Field findIdField(List<Field> fields) {
        var idFields = fields.stream()
                .filter(field -> field.isAnnotationPresent(Id.class))
                .toList();

        if (idFields.size() != 1) {
            throw new IllegalArgumentException(
                    "Класс %s должен иметь только одно поле с аннотацией @Id".formatted(clazz.getName()));
        }

        return idFields.getFirst();
    }
}
