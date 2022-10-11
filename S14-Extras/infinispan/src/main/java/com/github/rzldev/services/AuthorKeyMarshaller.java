package com.github.rzldev.services;

import com.github.rzldev.schemas.AuthorKey;
import org.infinispan.protostream.MessageMarshaller;

import java.io.IOException;

public class AuthorKeyMarshaller implements MessageMarshaller<AuthorKey> {
    @Override
    public AuthorKey readFrom(ProtoStreamReader reader) throws IOException {
        long id = reader.readLong("id");
        return new AuthorKey(id);
    }

    @Override
    public void writeTo(ProtoStreamWriter writer, AuthorKey authorKey) throws IOException {
        writer.writeLong("id", authorKey.getId());
    }

    @Override
    public Class<? extends AuthorKey> getJavaClass() {
        return AuthorKey.class;
    }

    @Override
    public String getTypeName() {
        return "book_samples.AuthorKey";
    }
}
