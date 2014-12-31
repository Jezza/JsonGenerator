package me.jezza.common.core;

import me.jezza.common.core.interfaces.IJsonData;

import java.io.File;

public class JsonGenerator {

    static {
        createFolder("./output/");
        createFolder("./output/assets/");
    }

    public static void generateJson(String modID, IJsonData jsonData) {
        createFolder("./output/assets/" + modID);
        jsonData.createData(new File("./output/assets/" + modID));

    }

    private static boolean createFolder(String folderPath) {
        File folder = new File(folderPath);
        if (!folder.exists())
            return folder.mkdir();
        return folder.exists();
    }

}
