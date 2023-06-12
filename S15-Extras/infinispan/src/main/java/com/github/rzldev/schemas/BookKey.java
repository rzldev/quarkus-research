package com.github.rzldev.schemas;

import org.infinispan.protostream.annotations.ProtoFactory;
import org.infinispan.protostream.annotations.ProtoField;

public class BookKey implements MessageKey {

    private final long id;
    private final long authorId;

    @ProtoFactory
    public BookKey(long id, long authorId) {
        this.id = id;
        this.authorId = authorId;
    }

    @ProtoField(number = 1, required = true)
    public long getId() {
        return id;
    }

    @ProtoField(number = 2, required = true)
    public long getAuthorId() {
        return authorId;
    }
}
