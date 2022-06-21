package mapper;

import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;

import java.util.List;

public interface RowMapper<T> {
    T mapFrom(Row row);
    List<T> mapListFrom(RowSet<Row> resultSet);
}
