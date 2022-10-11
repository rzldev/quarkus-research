package com.github.rzldev.services;

import com.github.rzldev.schemas.Author;
import com.github.rzldev.schemas.Book;
import org.infinispan.protostream.MessageMarshaller;

import java.io.IOException;
import java.math.BigDecimal;

public class BookMarshaller implements MessageMarshaller<Book> {
    @Override
    public Book readFrom(ProtoStreamReader reader) throws IOException {
        long id = reader.readLong("id");
        String title = reader.readString("title");
        String description = reader.readString("description");
        int publicationYear = reader.readInt("publicationYear");
        Author author = reader.readObject("author", Author.class);
        String price = reader.readString("price");
        return new Book(id, title, description, publicationYear, author.getId(), price);
    }

    @Override
    public void writeTo(ProtoStreamWriter writer, Book book) throws IOException {
        writer.writeLong("id", book.getId());
        writer.writeString("title", book.getTitle());
        writer.writeString("description", book.getDescription());
        writer.writeInt("publicationYear", book.getPublicationYear());
        writer.writeObject("author", book.getAuthorId(), Author.class);
        writer.writeString("price", book.getPrice());
    }

    @Override
    public Class<? extends Book> getJavaClass() {
        return Book.class;
    }

    @Override
    public String getTypeName() {
        return "book_samples.Book";
    }
}
