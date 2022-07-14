package org.rzldev.quarkus;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import io.quarkus.panache.common.Sort;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class KnightRepository implements PanacheMongoRepository<Knight> {

    public Knight findByName(String name) {
        return find("name", name).firstResult();
    }

    public List<Knight> findOrderName() {
        return listAll(Sort.by("name"));
    }

}
