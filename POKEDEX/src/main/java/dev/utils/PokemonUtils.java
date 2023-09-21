package dev.utils;


import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import dev.models.NextEvolution;
import dev.models.Pokemon;
import dev.models.PrevEvolution;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PokemonUtils {
    private PokemonUtils() {
    }

    public static JsonDeserializer<Pokemon> getJsonDeserializer() {
        return  new JsonDeserializer<Pokemon>() {
            @Override
            public Pokemon deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                if (!(json instanceof JsonObject jsonObject))
                    throw new JsonParseException("Pokemon is not a JSON object");
                Pokemon pokemon = new Pokemon();
                int id = jsonObject.get("id").getAsInt();
                pokemon.setId(id);
                String num = jsonObject.get("num").getAsString();
                pokemon.setNum(num);
                String name = jsonObject.get("name").getAsString();
                pokemon.setName(name);
                String img = jsonObject.get("img").getAsString();
                pokemon.setImg(img);
                List<String> type = context.deserialize(jsonObject.get("type"), new TypeToken<List<String>>() {
                }.getType());
                pokemon.setType(type);
                String height = jsonObject.get("height").getAsString();
                String heightNumber = height.split(" ")[0];
                pokemon.setHeight(Double.parseDouble(heightNumber));
                String weight = jsonObject.get("weight").getAsString();
                String weightNumber = weight.split(" ")[0];
                pokemon.setWeight(Double.parseDouble(weightNumber));
                String candy = jsonObject.get("candy").getAsString();
                pokemon.setCandy(candy);
                JsonElement candyCountElement = jsonObject.get("candy_count");
                if (candyCountElement != null) {
                    int candyCount = candyCountElement.getAsInt();
                    pokemon.setCandy_count(candyCount);
                }
                String egg = jsonObject.get("egg").getAsString();
                pokemon.setEgg(egg);
                double spawnChance = jsonObject.get("spawn_chance").getAsDouble();
                pokemon.setSpawn_chance(spawnChance);
                double avgSpawns = jsonObject.get("avg_spawns").getAsDouble();
                pokemon.setAvg_spawns(avgSpawns);
                String spawnTime = jsonObject.get("spawn_time").getAsString();
                pokemon.setSpawn_time(spawnTime);
                List<Double> multipliers = context.deserialize(jsonObject.get("multipliers"), new TypeToken<List<Double>>() {
                }.getType());
                pokemon.setMultipliers(multipliers);
                List<String> weaknesses = context.deserialize(jsonObject.get("weaknesses"), new TypeToken<List<String>>() {
                }.getType());
                pokemon.setWeaknesses(weaknesses);
                List<NextEvolution> nextEvolutions = new ArrayList<>();
                JsonElement nextEvolution = jsonObject.get("next_evolution");
                if (nextEvolution != null) {
                    nextEvolutions = context.deserialize(nextEvolution, new TypeToken<List<NextEvolution>>() {
                    }.getType());
                }
                List<PrevEvolution> prevEvolutions =new ArrayList<>();
                JsonElement prevEvolution = jsonObject.get("prev_evolution");
                if (prevEvolution != null) {
                    prevEvolutions = context.deserialize(prevEvolution, new TypeToken<List<PrevEvolution>>() {
                    }.getType());
                }
                pokemon.setPrev_evolution(prevEvolutions);
                return pokemon;
            }
        };
    }
}
