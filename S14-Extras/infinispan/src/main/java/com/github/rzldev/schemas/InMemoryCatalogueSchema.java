package com.github.rzldev.schemas;

import org.infinispan.protostream.SerializationContextInitializer;
import org.infinispan.protostream.annotations.AutoProtoSchemaBuilder;
import org.infinispan.protostream.types.java.math.BigDecimalAdapter;

@AutoProtoSchemaBuilder(
        schemaPackageName = "book_samples",
        includeClasses = {Author.class, AuthorKey.class, Book.class, BookKey.class, BigDecimalAdapter.class}
)
public interface InMemoryCatalogueSchema extends SerializationContextInitializer {
}
