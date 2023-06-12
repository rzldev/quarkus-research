package com.github.rzldev.schemas;

import org.infinispan.protostream.annotations.ProtoDoc;
import org.infinispan.protostream.annotations.ProtoFactory;
import org.infinispan.protostream.annotations.ProtoField;

import java.math.BigDecimal;
import java.util.Objects;

@ProtoDoc("@Indexed")
public class Book {

    private final long id;
    private final String title;
    private final String description;
    private final int publicationYear;
    private final long authorId;
    private final String price;

    @ProtoFactory
    public Book(long id, String title, String description, int publicationYear, long authorId, String price) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.publicationYear = publicationYear;
        this.authorId = authorId;
        this.price = price;
    }

    @ProtoField(number = 1, required = true)
    public long getId() {
        return id;
    }

    @ProtoField(number = 2)
    @ProtoDoc("@Field(index=Index.YES, analyze = Analyze.YES, store = Store.YES)")
    public String getTitle() {
        return title;
    }

    @ProtoField(number = 3)
    @ProtoDoc("@Field(index=Index.YES, analyze = Analyze.YES, store = Store.YES)")
    public String getDescription() {
        return description;
    }

    @ProtoField(number = 4, defaultValue = "2000")
    @ProtoDoc("@Field(index=Index.YES, analyze = Analyze.YES, store = Store.YES)")
    public int getPublicationYear() {
        return publicationYear;
    }

    @ProtoField(number = 5, defaultValue = "0")
    @ProtoDoc("@Field(index=Index.YES, analyze = Analyze.YES, store = Store.YES)")
    public long getAuthorId() {
        return authorId;
    }

    @ProtoField(number = 6)
    @ProtoDoc("@Field(index=Index.YES, analyze = Analyze.YES, store = Store.YES)")
    public String getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return """
                Book {
                    id: '%s',
                    title: '%s',
                    description: '%s',
                    publicationYear: '%s',
                    authorId: '%s',
                    price: '%s'
                }
                """.formatted(id, title, description, publicationYear, authorId, price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, publicationYear, authorId, price);
    }
}
