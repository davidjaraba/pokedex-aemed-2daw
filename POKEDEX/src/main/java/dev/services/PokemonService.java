package dev.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.reflect.TypeToken;
import dev.models.Pokedex;
import dev.models.Pokemon;
import dev.utils.PokemonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class PokemonService {
    private Pokedex pokedex;

    public PokemonService() throws FileNotFoundException {
        loadPokedex();
    }

    private String getDataDir() {

        String userDir = System.getProperty("user.dir");
        Path currentRelativePath = Paths.get(userDir);
        String ruta = currentRelativePath.toAbsolutePath().toString();
        return ruta + File.separator + "data";
    }

    private void loadPokedex() throws FileNotFoundException {
        String dir = getDataDir();
        String pokemonJsonFile = dir + File.separator + "pokemon.json";
        Path filePath = Paths.get(pokemonJsonFile);
        if (!Files.exists(filePath)) {
            throw new FileNotFoundException("File not found: " + pokemonJsonFile);
        }
        Gson gson = getGson();
        try (Reader reader = Files.newBufferedReader(filePath)) {
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

    public void exportToCSV() {
        String dir = getDataDir();
        String pokemonCsvFile = dir + File.separator + "pokemon.csv";
        Path filePath = Paths.get(pokemonCsvFile);
        try {
            Files.deleteIfExists(filePath);
            List<String> lines = getPokemons().stream().map(Pokemon::toCSV).toList();
            Files.write(filePath, lines);
            System.out.println("Pokémons exportados en: " + pokemonCsvFile);
        } catch (Exception e) {
            System.out.println("Error exportando a csv!");
            System.out.println("Error: " + e.getMessage());
        }
    }

    private Gson getGson() {
        JsonDeserializer<Pokemon> pokemonJsonSerializer = PokemonUtils.getJsonDeserializer();
        return new GsonBuilder().setPrettyPrinting().registerTypeAdapter(Pokemon.class, pokemonJsonSerializer).create();
    }


}
