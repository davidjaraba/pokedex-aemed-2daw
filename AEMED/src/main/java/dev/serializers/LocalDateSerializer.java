package dev.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.time.LocalDate;

/**
 * Clase para adapatar LocalDate a Json
 */
public class LocalDateSerializer implements JsonSerializer<LocalDate> {

    /**
     * Funcion para convertir un objeto LocalDate al Json
     * @param src el objeto a convertir
     * @param typeOfSrc el tipo de objeto
     * @param context
     * @return JsonElement
     */
    @Override
    public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(src.toString());
    }
}
