package com.github.rzldev.schemas;

import org.infinispan.protostream.annotations.ProtoDoc;
import org.infinispan.protostream.annotations.ProtoFactory;
import org.infinispan.protostream.annotations.ProtoField;

public class AuthorKey implements MessageKey {

    private final long id;

    @ProtoFactory
    public AuthorKey(long id) {
        this.id = id;
    }

    @ProtoField(number = 1, required = true)
    public long getId() {
        return id;
    }
}
