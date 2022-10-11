package com.github.rzldev.schemas;

import org.infinispan.protostream.annotations.ProtoDoc;
import org.infinispan.protostream.annotations.ProtoFactory;
import org.infinispan.protostream.annotations.ProtoField;

import java.util.Objects;

@ProtoDoc("@Indexed")
public class Author {
    private final long id;
    private final String name;
    private final String surname;

    @ProtoFactory
    public Author(long id, String name, String surname) {
        this.id = id;
        this.name = name;
        this.surname = surname;
    }

    @ProtoField(number = 1, required = true)
    public long getId() {
        return id;
    }

    @ProtoField(number = 2)
    @ProtoDoc("@Field(index=Index.YES, analyze = Analyze.YES, store = Store.YES)")
    public String getName() {
        return name;
    }

    @ProtoField(number = 3)
    @ProtoDoc("@Field(index=Index.YES, analyze = Analyze.YES, store = Store.YES)")
    public String getSurname() {
        return surname;
    }

    @Override
    public String toString() {
        return """
                Author {
                    id: '%s',
                    name: '%s',
                    surname: '%s'
                }
                """.formatted(id, name, surname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname);
    }
}
