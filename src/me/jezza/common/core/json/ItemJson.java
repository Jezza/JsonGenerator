package me.jezza.common.core.json;

import com.google.gson.JsonObject;

import java.io.File;

public class ItemJson extends JsonDataAbstract {

    public ItemJson(String modID, String name) {
        super(modID, name);
    }

    @Override
    public void createData(File path) {
        createFolder(path = new File(path, "models"));
        createFolder(path = new File(path, "item"));

        createFile(path = new File(path, name + ".json"));

        writeJson(path, createJson());
    }

    private JsonObject createJson() {
        JsonObject root = new JsonObject();
        root.addProperty("parent", "builtin/generated");

        JsonObject textures = new JsonObject();
        textures.addProperty("layer0", modID + ":items/" + name);
        root.add("textures", textures);

        JsonObject display = new JsonObject();

        JsonObject thirdPerson = new JsonObject();

        int[] rotations = new int[]{-90, 0, 0};
        thirdPerson.add("rotation", createArrayWith(rotations));

        int[] translations = new int[]{0, 1, -3};
        thirdPerson.add("translation", createArrayWith(translations));

        float[] scales = new float[]{0.55F, 0.55F, 0.55F};
        thirdPerson.add("scale", createArrayWith(scales));

        JsonObject firstPerson = new JsonObject();

        rotations = new int[]{0, -135, 25};
        firstPerson.add("rotation", createArrayWith(rotations));

        translations = new int[]{0, 4, 2};
        firstPerson.add("translation", createArrayWith(translations));

        scales = new float[]{1.7F, 1.7F, 1.7F};
        firstPerson.add("scale", createArrayWith(scales));

        display.add("thirdperson", thirdPerson);
        display.add("firstperson", firstPerson);

        root.add("display", display);
        return root;
    }
}
