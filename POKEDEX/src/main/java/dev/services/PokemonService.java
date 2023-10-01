package dev.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.reflect.TypeToken;
import dev.db.DatabaseManager;
import dev.models.Pokedex;
import dev.models.Pokemon;
import dev.models.SqlCommand;
import dev.utils.PokemonUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class PokemonService {
    private Pokedex pokedex;

    private final DatabaseManager dbManager;

    /**
     * Constructor de la clase PokemonService
     *
     * @param dbManager Objeto de la clase DatabaseManager
     * @throws FileNotFoundException Excepción que se lanza si no se encuentra el fichero pokemon.json
     */
    public PokemonService(DatabaseManager dbManager) throws FileNotFoundException {
        this.dbManager = dbManager;
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
        try (Reader reader = new InputStreamReader(new FileInputStream(pokemonJsonFile))) {
            this.pokedex = gson.fromJson(reader, new TypeToken<Pokedex>() {
            }.getType());
            System.out.println("Pokedex loaded! There are: " + getPokemons().size());
        } catch (Exception e) {
            System.out.println("Error loading Pokedex!");
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Método que devuelve la lista de solo lectura de pokemons
     *
     * @return Lista de pokemons (solo lectura)
     */
    public List<Pokemon> getPokemons() {
        return Collections.unmodifiableList(pokedex.getPokemon());
    }

    /**
     * Método que exporta la lista de pokemons a un fichero pokemon.csv en la carpeta data
     */
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

    /**
     * Método que importa la lista de pokemons desde un fichero pokemon.csv en la carpeta data
     */
    public Optional<List<Pokemon>> getCSV() {

        String dir = getDataDir();
        String pokemonCsvFile = dir + File.separator + "pokemon.csv";
        Path filePath = Paths.get(pokemonCsvFile);
        try {
            return Optional.of(Files.readAllLines(filePath).stream().map(line -> {
                String[] fields = line.split(String.valueOf(Pokemon.CSV_SEPARATOR));
                Pokemon pokemon = new Pokemon();
                pokemon.setId(Integer.parseInt(fields[0]));
                pokemon.setNum(fields[1]);
                pokemon.setName(fields[2]);
                pokemon.setHeight(Double.parseDouble(fields[3]));
                pokemon.setWeight(Double.parseDouble(fields[4]));
                return pokemon;
            }).toList());

        } catch (Exception e) {
            System.out.println("Error exportando a csv!");
            System.out.println("Error: " + e.getMessage());
        }
        return Optional.empty();
    }


    /**
     * Método que inserta un pokemon en la base de datos
     *
     * @param pokemon Pokemon a insertar
     * @return Pokemon insertado
     */
    public Optional<Pokemon> insertPokemon(Pokemon pokemon) {

        try {
            SqlCommand sqlCommand = new SqlCommand("INSERT INTO pokemon (id, num, name, height, weight) VALUES (?, ?, ?, ?, ?)");
            sqlCommand.addParam(pokemon.getId());
            sqlCommand.addParam(pokemon.getNum());
            sqlCommand.addParam(pokemon.getName());
            sqlCommand.addParam(pokemon.getHeight());
            sqlCommand.addParam(pokemon.getWeight());
            dbManager.executeUpdate(sqlCommand);
            return Optional.of(pokemon);
        } catch (Exception e) {
            System.out.println("Error insertando pokemon!");
            System.out.println("Error: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Método que busca un pokemon por su nombre de la base de datos
     *
     * @param name Nombre del pokemon a buscar
     * @return Pokemon encontrado
     */
    public Optional<Pokemon> findPokemonByName(String name) {

        try {
            SqlCommand sqlCommand = new SqlCommand("SELECT * FROM pokemon WHERE lower(name) = ?");
            sqlCommand.addParam(name.toLowerCase());
            ResultSet res = dbManager.executeQuery(sqlCommand);
            if (res.next()) {
                Pokemon pokemon = new Pokemon();
                pokemon.setId(res.getInt("id"));
                pokemon.setNum(res.getString("num"));
                pokemon.setName(res.getString("name"));
                pokemon.setHeight(res.getDouble("height"));
                pokemon.setWeight(res.getDouble("weight"));
                return Optional.of(pokemon);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();

    }

    /**
     * Método que obtiene todos los pokemons de la base de datos
     *
     * @return Pokemon encontrado
     */
    public List<Pokemon> findAll() {

        SqlCommand sqlCommand = new SqlCommand("SELECT * FROM pokemon");

        List<Pokemon> pokemons = new ArrayList<>();

        try {
            ResultSet res = dbManager.executeQuery(sqlCommand);
            while (res.next()) {

                Pokemon pokemon = new Pokemon();
                pokemon.setId(res.getInt("id"));
                pokemon.setNum(res.getString("num"));
                pokemon.setName(res.getString("name"));
                pokemon.setHeight(res.getDouble("height"));
                pokemon.setWeight(res.getDouble("weight"));

                pokemons.add(pokemon);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return pokemons;

    }


}
