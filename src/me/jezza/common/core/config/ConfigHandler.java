package me.jezza.common.core.config;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import me.jezza.App;
import me.jezza.common.core.interfaces.IJsonHandler;
import me.jezza.common.core.interfaces.IJsonHandlerWindow;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.lang.reflect.Type;
import java.util.Collection;

public class ConfigHandler {

    public static final Type jsonArrayType = new TypeToken<JsonArray>() {
    }.getType();

    private static File configFile = new File("./user.cfg");

    public static void save() {
        App.LOG.info("Saving...");
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        JsonArray jsonArray = new JsonArray();
        Collection<IJsonHandler> handlers = App.HANDLERS;
        for (IJsonHandler handler : handlers) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("objectClazz", handler.getClass().getCanonicalName());
            handler.toJsonObject(jsonObject);
            jsonArray.add(jsonObject);
        }

        try {
            try (OutputStream os = new FileOutputStream(configFile)) {
                IOUtils.write(jsonArray.toString(), os);
            }
        } catch (Exception e) {
            App.LOG.error("Failed to save to config.", e);
        }
    }

    public static void load() {
        App.LOG.info("Loading...");
        if (!configFile.exists()) {
            App.LOG.info("Failed to locate Config File: {}", configFile);
            return;
        }

        String jsonString = "";
        try {
            try (InputStream is = new FileInputStream(configFile)) {
                jsonString = IOUtils.toString(is);
            }
        } catch (Exception e) {
            App.LOG.error("Failed to save to config.", e);
        }

        if (!jsonString.isEmpty()) {
            JsonArray jsonArray = new Gson().fromJson(jsonString, jsonArrayType);
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonElement jsonElement = jsonArray.get(i);
                if (!jsonElement.isJsonObject())
                    continue;
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                if (!jsonObject.has("objectClazz"))
                    continue;
                String objectClazz = jsonObject.get("objectClazz").getAsString();
                Class<?> clazz = null;
                try {
                    clazz = Class.forName(objectClazz);
                } catch (ClassNotFoundException e) {
                    App.LOG.error("Failed to find class {}", objectClazz);
                    continue;
                }
                if (clazz == null || !IJsonHandler.class.isAssignableFrom(clazz))
                    continue;

                if (IJsonHandlerWindow.class.isAssignableFrom(clazz)) {
                    IJsonHandlerWindow jsonHandler = create(clazz);
                    if (jsonHandler == null)
                        continue;
                    jsonHandler.getMainInstance().fromJsonObject(jsonObject);
                } else {
                    IJsonHandler jsonHandler = create(clazz);
                    if (jsonHandler == null)
                        continue;
                    jsonHandler.fromJsonObject(jsonObject);
                }

            }
        }
    }

    private static <T extends IJsonHandler> T create(Class<?> clazz) {
        try {
            return (T) clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            App.LOG.error("Failed to create instance. {}", clazz);
        }
        return null;
    }
}