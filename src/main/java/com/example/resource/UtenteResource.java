package com.example.resource;

import mapper.ResultSetMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.smallrye.mutiny.Uni;
import lombok.extern.slf4j.Slf4j;
import model.Utente;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import io.vertx.mutiny.mysqlclient.MySQLPool;

import java.util.List;

@Slf4j
@Path("test")
public class UtenteResource {

    @Inject
    MySQLPool client;


    @GET
    public Uni<String> getName() {
        return client.query("SELECT * FROM utenti")
                .execute()
                .onItem()
                .call(rows -> {
                    List<Utente> u = ResultSetMapper.mapListFrom(rows, Utente.class);
                    log.info(u.toString());
                    return Uni.createFrom().nullItem();
                }).onItem().transformToUni(x -> {
                    return Uni.createFrom().item("ok");
                });
    }
}
