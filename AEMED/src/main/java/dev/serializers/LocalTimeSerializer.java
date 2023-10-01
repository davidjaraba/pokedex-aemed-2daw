package dev.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.time.LocalTime;

/**
 * Clase para adapatar LocalTime a Json
 */
public class LocalTimeSerializer implements JsonSerializer<LocalTime> {

    /**
     * Funcion para convertir un objeto LocalTime al Json
     * @param src el objeto a convertir
     * @param typeOfSrc el tipo de objeto
     * @param context
     * @return JsonElement
     */
    @Override
    public JsonElement serialize(LocalTime src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(src.toString());
    }
}
