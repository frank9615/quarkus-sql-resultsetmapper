package mapper;

import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import mapper.RowMapper;
import model.Utente;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UtenteMapper implements RowMapper<Utente> {

    @Override
    public Utente mapFrom(Row row) {

        return Utente.builder()
                .name(row.getString("name"))
                .surname(row.getString("surname"))
                .punti(row.getLong("punti"))
                .build();
    }

    @Override
    public List<Utente> mapListFrom(RowSet<Row> resultSet) {
        List<Utente> utenti = new ArrayList<>();
        resultSet.forEach(row -> {
            utenti.add(mapFrom(row));
        });
        return utenti;
    }
}
