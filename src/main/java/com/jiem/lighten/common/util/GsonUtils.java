package com.jiem.lighten.common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.io.Reader;

/**
 * gon json 工具类型
 *
 * @author: haojunjie
 * @date: 2023-10-21 11:46
 */
public class GsonUtils {


    public static JsonObject parse(String json) {
        return JsonParser.parseString(json).getAsJsonObject();
    }

    public static JsonObject parse(Reader json) {
        return JsonParser.parseReader(json).getAsJsonObject();
    }

    public static JsonObject parse(JsonReader json) {
        return JsonParser.parseReader(json).getAsJsonObject();
    }


    public static Gson gson() {
        return new GsonBuilder().create();
    }

}
