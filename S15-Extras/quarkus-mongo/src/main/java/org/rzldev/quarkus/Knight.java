package org.rzldev.quarkus;

import io.quarkus.mongodb.panache.PanacheMongoEntity;

public class Knight extends PanacheMongoEntity {

    public String name;
    public String sword;

    public Knight() {
    }

    public Knight(String name, String sword) {
        this.name = name;
        this.sword = sword;
    }
}
