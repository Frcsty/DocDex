package me.piggypiglet.docdex.documentation.index.population.implementations.flatfile;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.piggypiglet.docdex.config.Javadoc;
import me.piggypiglet.docdex.documentation.index.objects.DocumentedObjectKey;
import me.piggypiglet.docdex.documentation.index.population.IndexPopulator;
import me.piggypiglet.docdex.documentation.index.population.implementations.flatfile.adaptation.ObjectMapAdapter;
import me.piggypiglet.docdex.documentation.objects.DocumentedObject;
import me.piggypiglet.docdex.documentation.objects.adaptation.creation.FieldMetadataCreator;
import me.piggypiglet.docdex.documentation.objects.adaptation.creation.MethodMetadataCreator;
import me.piggypiglet.docdex.documentation.objects.adaptation.creation.TypeMetadataCreator;
import me.piggypiglet.docdex.documentation.objects.detail.field.FieldMetadata;
import me.piggypiglet.docdex.documentation.objects.detail.method.MethodMetadata;
import me.piggypiglet.docdex.documentation.objects.type.TypeMetadata;
import me.piggypiglet.docdex.file.utils.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;

import static me.piggypiglet.docdex.documentation.index.population.implementations.flatfile.adaptation.ObjectMapAdapter.DESERIALIZED_TYPE;

// ------------------------------
// Copyright (c) PiggyPiglet 2020
// https://www.piggypiglet.me
// ------------------------------
public final class FlatFilePopulator implements IndexPopulator {
    private static final Logger LOGGER = LoggerFactory.getLogger("FlatFilePopulator");
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(TypeMetadata.class, new TypeMetadataCreator())
            .registerTypeAdapter(MethodMetadata.class, new MethodMetadataCreator())
            .registerTypeAdapter(FieldMetadata.class, new FieldMetadataCreator())
            .registerTypeAdapter(DESERIALIZED_TYPE, new ObjectMapAdapter())
            .create();

    @Override
    public boolean shouldPopulate(final @NotNull Javadoc javadoc) {
        return new File("docs", String.join("-", javadoc.getNames()) + ".json").exists();
    }

    @NotNull
    @Override
    public Map<DocumentedObjectKey, DocumentedObject> provideObjects(@NotNull final Javadoc javadoc) {
        final String fileName = String.join("-", javadoc.getNames()) + ".json";
        final Path file = Paths.get("docs", fileName);

        LOGGER.info("Loading pre-built index from {}", fileName);

        try {
            final Map<DocumentedObjectKey, DocumentedObject> data = GSON.fromJson(FileUtils.readFile(file), DESERIALIZED_TYPE);
            LOGGER.info("Finished loading {}", fileName);
            return data;
        } catch (IOException exception) {
            LOGGER.error("Something went wrong when loading " + fileName, exception);
        }

        return Collections.emptyMap();
    }
}
