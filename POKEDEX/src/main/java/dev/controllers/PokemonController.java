package dev.controllers;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import dev.models.NextEvolution;
import dev.models.Pokedex;
import dev.models.Pokemon;
import dev.models.PrevEvolution;
import dev.utils.PokemonUtils;

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
        JsonDeserializer<Pokemon> pokemonJsonSerializer = PokemonUtils.getJsonDeserializer();
        return new GsonBuilder().setPrettyPrinting().registerTypeAdapter(Pokemon.class, pokemonJsonSerializer).create();
    }

    public List<Pokemon> getPokemons() {
        return Collections.unmodifiableList(pokedex.getPokemon());
    }

    public Pokemon getPokemon(int index) {
        return getPokemons().get(index);
    }
}
