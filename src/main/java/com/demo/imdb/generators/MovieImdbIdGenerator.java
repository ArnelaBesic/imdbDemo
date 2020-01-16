package com.demo.imdb.generators;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.stream.Stream;

public class MovieImdbIdGenerator implements IdentifierGenerator {

    private String prefix = "tt";

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object obj)
            throws HibernateException {
        String query = String.format("select %s from %s",
                session.getEntityPersister(obj.getClass().getName(), obj)
                        .getIdentifierPropertyName(),
                obj.getClass().getSimpleName());

        Stream ids = session.createQuery(query).stream();
        Long max = ids.map(id -> ((String) id).replace(prefix, ""))
                .mapToLong(value -> Long.parseLong((String) value))
                .max()
                .orElse(0L);
        return prefix + (max + 1);
    }
}