package kz.kaznu.lucene.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import kz.kaznu.lucene.model.Message;
import org.apache.lucene.document.Document;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Helper {
    private static final Gson gson = new GsonBuilder().create();
    private static final Type listType = new TypeToken<List<Message>>() {}.getType();

    /**
     * Read documents from the provided file.
     * Usual tutorial file is located in resources/tutorial.json
     *
     * @param pathToFile path to JSON file
     * @return list of documents
     * @throws FileNotFoundException
     */
    public static List<Document> readDocumentsFromFile(final String pathToFile) throws FileNotFoundException {
        final File file = new File(pathToFile);
        final FileReader fileReader = new FileReader(file);
        final JsonReader reader = new JsonReader(fileReader);
        final List<Message> messages = gson.fromJson(reader, listType);
        final List<Document> documents = messages.stream()
                .map(Message::convertToDocument)
                .collect(Collectors.toList());
        return documents;
    }
}
