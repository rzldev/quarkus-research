package com.github.rzldev.services;

import com.github.rzldev.schemas.Author;
import org.infinispan.protostream.MessageMarshaller;

import java.io.IOException;

public class AuthorMarshaller implements MessageMarshaller<Author> {
    @Override
    public Author readFrom(ProtoStreamReader reader) throws IOException {
        long id = reader.readLong("id");
        String name = reader.readString("name");
        String surname = reader.readString("surname");
        return new Author(id, name, surname);
    }

    @Override
    public void writeTo(ProtoStreamWriter writer, Author author) throws IOException {
        writer.writeLong("id", author.getId());
        writer.writeString("name", author.getName());
        writer.writeString("surname", author.getSurname());
    }

    @Override
    public Class<? extends Author> getJavaClass() {
        return Author.class;
    }

    @Override
    public String getTypeName() {
        return "book_samples.Author";
    }
}
