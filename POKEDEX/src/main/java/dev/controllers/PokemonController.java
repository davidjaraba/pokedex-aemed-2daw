package dev.controllers;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import dev.models.NextEvolution;
import dev.models.Pokedex;
import dev.models.Pokemon;
import dev.models.PrevEvolution;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PokemonController {
    private static PokemonController instance;
    private Pokedex pokedex;

    private PokemonController() {
        loadPokedex();
    }

    public static PokemonController getInstance() {
        if (instance == null) {
            instance = new PokemonController();
        }
        return instance;
    }

    private void loadPokedex() {
        String userDir = System.getProperty("user.dir");
        Path currentRelativePath = Paths.get(userDir);
        String ruta = currentRelativePath.toAbsolutePath().toString();
        String dir = ruta + File.separator + "data";
        String pokemonJsonFile = dir + File.separator + "pokemon.json";
        Gson gson = getGson();
        try (Reader reader = Files.newBufferedReader(Paths.get(pokemonJsonFile))) {
            this.pokedex = gson.fromJson(reader, new TypeToken<Pokedex>() {
            }.getType());
            System.out.println("Pokedex loaded! There are: " + getPokemons().size());
        } catch (Exception e) {
            System.out.println("Error loading Pokedex!");
            System.out.println("Error: " + e.getMessage());
        }
    }

    private Gson getGson() {
        JsonDeserializer<Pokemon> pokemonJsonSerializer = new JsonDeserializer<Pokemon>() {
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
        return new GsonBuilder().setPrettyPrinting().registerTypeAdapter(Pokemon.class, pokemonJsonSerializer).create();
    }

    public List<Pokemon> getPokemons() {
        return Collections.unmodifiableList(pokedex.getPokemon());
    }

    public Pokemon getPokemon(int index) {
        return getPokemons().get(index);
    }
}
