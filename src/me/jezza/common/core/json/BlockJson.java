package me.jezza.common.core.json;

import com.google.gson.JsonObject;

import java.io.File;

public class BlockJson extends JsonDataAbstract {

    public BlockJson(String modID, String name) {
        super(modID, name);
    }

    @Override
    public void createData(File path) {
        File tempPath = new File(path, "blockstates");
        createFolder(tempPath);
        createFile(tempPath = new File(tempPath, name + ".json"));
        writeJson(tempPath, createJsonBlockStates());

        createFolder(path = new File(path, "models"));

        tempPath = new File(path, "block");
        createFolder(tempPath);
        createFile(tempPath = new File(tempPath, name + ".json"));
        writeJson(tempPath, createJsonBlock());

        tempPath = new File(path, "item");
        createFolder(tempPath);
        createFile(tempPath = new File(tempPath, name + ".json"));
        writeJson(tempPath, createJsonItem());

    }

    public JsonObject createJsonBlockStates() {
        JsonObject root = new JsonObject();

        JsonObject variants = new JsonObject();

        JsonObject normal = new JsonObject();

        normal.addProperty("model", modID + ":" + name);

        variants.add("normal", normal);

        root.add("variants", variants);
        return root;
    }

    public JsonObject createJsonBlock() {
        JsonObject root = new JsonObject();

        root.addProperty("parent", "block/cube_all");

        JsonObject textures = new JsonObject();

        textures.addProperty("all", modID + ":blocks/" + name);

        root.add("textures", textures);
        return root;
    }

    public JsonObject createJsonItem() {
        JsonObject root = new JsonObject();
        root.addProperty("parent", modID + ":block/" + name);

        JsonObject display = new JsonObject();

        JsonObject thirdPerson = new JsonObject();

        int[] rotations = new int[]{10, -45, 170};
        thirdPerson.add("rotation", createArrayWith(rotations));

        float[] translations = new float[]{0F, 1.5F, -2.75F};
        thirdPerson.add("translation", createArrayWith(translations));

        float[] scales = new float[]{0.375F, 0.375F, 0.375F};
        thirdPerson.add("scale", createArrayWith(scales));

        display.add("thirdperson", thirdPerson);

        root.add("display", display);
        return root;
    }
}
