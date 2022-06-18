package mapper;

import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * @version 0.0.2
 * @author: Francesco Accardi
 */
@Slf4j
public class ResultSetMapper {

    private static <T> void setValues(Row row, List<String> columnsNames, Class<T> targetClass, Function<String, String> propertyFieldMapper, T instance) {
        if (row == null) {
            return;
        }
        List<Field> classDeclaredFields = Arrays.stream(targetClass.getDeclaredFields()).toList();
        columnsNames.forEach(columnName -> {
            classDeclaredFields.stream().filter(field -> field.getName().equals(propertyFieldMapper.apply(columnName))).findFirst().ifPresent(field -> {
                String propertyName = field.getName();
                String propertyNameCapitalize = propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
                try {
                    Method m = targetClass.getMethod("set" + propertyNameCapitalize, field.getType());
                    m.invoke(instance, row.get(field.getType(), columnName));
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            });
        });
    }

    public static <T> T mapFrom(RowSet<Row> rows, Class<T> targetClass, Function<String, String> propertyFieldMapper) {
        T instance;
        try {
            instance = targetClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        Row row = rows.iterator().next();
        setValues(row, rows.columnsNames(), targetClass, propertyFieldMapper, instance);
        return instance;
    }

    public static <T> List<T> mapListFrom(RowSet<Row> rows, Class<T> targetClass, Function<String, String> propertyFieldMapper) {
        List<T> instanceList = new ArrayList<>();
        rows.forEach(row -> {
            T instance;
            try {
                instance = targetClass.getDeclaredConstructor().newInstance();
                setValues(row, rows.columnsNames(), targetClass, propertyFieldMapper, instance);
                instanceList.add(instance);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });

        return instanceList;
    }

    public static <T> T mapFrom(RowSet<Row> rows, Class<T> targetClass) {
        return mapFrom(rows, targetClass, String::toString);
    }

    public static <T> List<T> mapListFrom(RowSet<Row> rows, Class<T> targetClass) {
        return mapListFrom(rows, targetClass, String::toString);
    }

}
