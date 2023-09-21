package dev.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.reflect.TypeToken;
import dev.models.Pokedex;
import dev.models.Pokemon;
import dev.utils.PokemonUtils;

import java.io.File;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class PokemonService {
    private Pokedex pokedex;

    public PokemonService() {
        loadPokedex();
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

    public List<Pokemon> getPokemons() {
        return Collections.unmodifiableList(pokedex.getPokemon());
    }

    private Gson getGson() {
        JsonDeserializer<Pokemon> pokemonJsonSerializer = PokemonUtils.getJsonDeserializer();
        return new GsonBuilder().setPrettyPrinting().registerTypeAdapter(Pokemon.class, pokemonJsonSerializer).create();
    }

}
