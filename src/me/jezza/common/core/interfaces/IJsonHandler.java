package me.jezza.common.core.interfaces;

import com.google.gson.JsonObject;

public interface IJsonHandler {

    public void toJsonObject(JsonObject jsonObject);

    public void fromJsonObject(JsonObject jsonObject);

}
