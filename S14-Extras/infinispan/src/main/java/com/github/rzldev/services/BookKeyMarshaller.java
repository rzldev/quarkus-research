package com.github.rzldev.services;

import com.github.rzldev.schemas.BookKey;
import org.infinispan.protostream.MessageMarshaller;

import java.io.IOException;

public class BookKeyMarshaller implements MessageMarshaller<BookKey> {
    @Override
    public BookKey readFrom(ProtoStreamReader reader) throws IOException {
        long id = reader.readLong("id");
        long authorId = reader.readLong("authorId");
        return new BookKey(id, authorId);
    }

    @Override
    public void writeTo(ProtoStreamWriter writer, BookKey bookKey) throws IOException {
        writer.writeLong("id", bookKey.getId());
    }

    @Override
    public Class<? extends BookKey> getJavaClass() {
        return BookKey.class;
    }

    @Override
    public String getTypeName() {
        return "book_samples.BookKey";
    }
}
