package me.jezza.common.core.json;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import me.jezza.App;
import me.jezza.common.core.interfaces.IJsonData;
import org.apache.commons.io.IOUtils;

import java.io.*;

public abstract class JsonDataAbstract implements IJsonData {

    protected String modID;
    protected String name;

    public JsonDataAbstract(String modID, String name) {
        this.modID = modID;
        this.name = name;
    }

    public void writeJson(File file, JsonElement jsonElement) {
        try {
            try (OutputStream os = new FileOutputStream(file)) {
                IOUtils.write(toJson(jsonElement), os);
            }
        } catch (Exception e) {
            App.LOG.error("Failed to save to config.", e);
        }
    }

    public String toJson(JsonElement jsonElement) {
        StringWriter writer = new StringWriter();
        try {
            JsonWriter e = this.newJsonWriter(Streams.writerForAppendable(writer));
            new Gson().toJson(jsonElement, e);
        } catch (IOException var4) {
            throw new RuntimeException(var4);
        }
        return writer.toString();
    }

    private JsonWriter newJsonWriter(Writer writer) throws IOException {
        JsonWriter jsonWriter = new JsonWriter(writer);
        jsonWriter.setIndent("    ");
        return jsonWriter;
    }

    public boolean createFolder(String folderPath) {
        return createFolder(new File(folderPath));
    }

    public boolean createFolder(File folder) {
        if (!folder.exists())
            return folder.mkdir();
        return folder.exists();
    }

    public boolean createFile(String fileName) {
        return createFile(new File(fileName));
    }

    public boolean createFile(File file) {
        if (!file.exists())
            try {
                return file.createNewFile();
            } catch (IOException e) {
                App.LOG.error("Failed to create file: {}", file.getName());
            }
        return file.exists();
    }

    public JsonArray createArrayWith(int... ints) {
        JsonArray jsonArray = new JsonArray();
        for (int i = 0; i < ints.length; i++)
            jsonArray.add(new JsonPrimitive(ints[i]));
        return jsonArray;
    }

    public JsonArray createArrayWith(float... floats) {
        JsonArray jsonArray = new JsonArray();
        for (int i = 0; i < floats.length; i++)
            jsonArray.add(new JsonPrimitive(floats[i]));
        return jsonArray;
    }

}
