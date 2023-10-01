package dev.controllers;

import dev.db.DatabaseManager;
import dev.models.NextEvolution;
import dev.models.Pokemon;
import dev.models.SqlCommand;
import dev.services.PokemonService;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PokemonController {
    private static PokemonController instance;
    private final PokemonService pokemonService;

    private PokemonController(PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }

    /**
     * Método que devuelve una instancia de la clase PokemonController
     *
     * @return La instancia de la clase
     * @throws IOException  Si hay un error al leer el archivo de propiedades
     * @throws SQLException Si hay un error en la conexión
     */
    public static PokemonController getInstance() throws IOException, SQLException {
        if (instance == null) {
            PokemonService pokemonService = new PokemonService(DatabaseManager.getInstance());
            instance = new PokemonController(pokemonService);
        }
        return instance;
    }


    /**
     * Método que se encarga de obtener todos los pokémons agrupados por tipo
     *
     * @return Un mapa con los pokémons agrupados por tipo
     */
    public Map<String, List<Pokemon>> groupedByType() {
        Stream<Pokemon> pokemons = pokemonService.getPokemons().stream();
        Map<String, List<Pokemon>> map = new HashMap<>();
        return pokemons.reduce(map, (accumulator, pokemon) -> {
            List<String> types = pokemon.getType();
            types.forEach(type -> {
                List<Pokemon> group = accumulator.computeIfAbsent(type, k -> new ArrayList<>());
                group.add(pokemon);
            });
            return accumulator;
        }, (map1, map2) -> {
            map1.putAll(map2);
            return map1;
        });
    }

    /**
     * Método que se encarga de obtener la debilidad más común entre todos los pokémons
     *
     * @return La debilidad más común entre todos los pokémons
     */
    public String getMostCommonWeakness() {
        Stream<Pokemon> pokemons = pokemonService.getPokemons().stream();
        Optional<Map.Entry<String, Long>> entry = pokemons
                .flatMap(s -> s.getWeaknesses().stream())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .max(Comparator.comparingLong(Map.Entry::getValue));
        return entry
                .orElse(Map.entry("No hay debilidades en la lista de pokemons", 0L))
                .getKey();

    }

    /**
     * Método que se encarga de obtener los nombres de los últimos 5 pokémons agregados
     *
     * @return Los nombres de los últimos 5 pokémons agregados
     */
    public List<String> getLast5PokemonNames() {
        Stream<Pokemon> pokemons = pokemonService.getPokemons().stream();
        return pokemons
                .sorted(Comparator.comparing(Pokemon::getId).reversed())
                .limit(5)
                .map(Pokemon::getName)
                .collect(Collectors.toList());
    }

    /**
     * Método que se encarga de obtener la siguiente evolución de un pokémon
     *
     * @param name El nombre del pokémon del que se quiere obtener la siguiente evolución
     * @return La siguiente evolución del pokémon
     */
    public Pokemon getNextEvolution(String name) {
        List<Pokemon> pokemonList = pokemonService.getPokemons();
        Stream<Pokemon> pokemons = pokemonList.stream();
        Optional<Pokemon> optional = pokemons
                .filter(pokemon -> pokemon.getName().equals(name))
                .findFirst();
        if (optional.isEmpty()) {
            return null;
        }
        Pokemon pokemon = optional.get();
        if (pokemon.getNext_evolution() == null || pokemon.getNext_evolution().size() < 1) {
            return null;
        }
        NextEvolution nextEvolution = pokemon.getNext_evolution().get(0);
        return pokemonList.stream()
                .filter(p -> p.getNum().equals(nextEvolution.getNum()))
                .findFirst()
                .orElse(null);

    }

    /**
     * Método que se encarga de obtener los pokémons de tipo eléctrico o agua
     *
     * @return Los pokémons de tipo eléctrico o agua
     */
    public List<Pokemon> getWaterOrElectricPokemons() {
        Stream<Pokemon> pokemons = pokemonService.getPokemons().stream();
        return pokemons
                .filter(pokemon -> pokemon.getType().contains("Water") || pokemon.getType().contains("Electric"))
                .collect(Collectors.toList());
    }

    /**
     * Método que se encarga de obtener el pokémon con más debilidades
     *
     * @return El pokémon con más debilidades
     */
    public Pokemon getMostWeaknessPokemon() {
        Stream<Pokemon> pokemons = pokemonService.getPokemons().stream();
        return pokemons
                .max(Comparator.comparingInt(pokemon -> pokemon.getWeaknesses().size()))
                .orElse(null);
    }

    /**
     * Método que se encarga de obtener los pokémons que no tienen evolución de tipo fuego
     *
     * @return Los pokémons que no tienen evolución de tipo fuego
     */
    public List<Pokemon> getPokemonWithNoFireEvolution() {
        Stream<Pokemon> pokemons = pokemonService.getPokemons().stream();
        return pokemons
                .filter(pokemon -> {
                    Stream<Pokemon> pokemonStream = pokemonService.getPokemons().stream();
                    List<String> evolutionNums = pokemon.getNext_evolution().stream().map(NextEvolution::getNum).toList();
                    return pokemonStream
                            .filter(p -> evolutionNums.contains(p.getNum()))
                            .noneMatch(p -> p.getType().contains("Fire"));
                })
                .toList();
    }

    /**
     * Método que se encarga de obtener el pokémon más alto
     *
     * @return El pokémon más alto
     */
    public Pokemon getTallestPokemon() {
        Stream<Pokemon> pokemons = pokemonService.getPokemons().stream();
        return pokemons
                .max(Comparator.comparingDouble(Pokemon::getHeight))
                .orElse(null);
    }

    /**
     * Método que se encarga de obtener la media de peso de todos los pokémons
     *
     * @return La media de peso de todos los pokémons
     */
    public double getAverageWeight() {
        Stream<Pokemon> pokemons = pokemonService.getPokemons().stream();
        return pokemons
                .mapToDouble(Pokemon::getWeight)
                .average()
                .orElse(0);
    }

    /**
     * Método que se encarga de obtener la media de evoluciones de todos los pokémons
     *
     * @return La media de evoluciones de todos los pokémons
     */
    public double getAverageEvolutionCount() {
        Stream<Pokemon> pokemons = pokemonService.getPokemons().stream();
        return pokemons
                .mapToInt(p -> {
                    if (p.getNext_evolution() == null) {
                        return 0;
                    }
                    return p.getNext_evolution().size();
                })
                .average()
                .orElse(0);
    }

    /**
     * Método que se encarga de obtener la media de debilidades de todos los pokémons
     *
     * @return La media de debilidades de todos los pokémons
     */
    public double getAverageWeaknessCount() {
        Stream<Pokemon> pokemons = pokemonService.getPokemons().stream();
        return pokemons
                .mapToInt(p -> p.getWeaknesses().size())
                .average()
                .orElse(0);
    }

    /**
     * Método que se encarga de obtener los pokémons agrupados por el número de evoluciones que tienen
     *
     * @return Los pokémons agrupados por el número de evoluciones que tienen
     */
    public Map<Integer, List<Pokemon>> groupedByEvolutions() {

        Stream<Pokemon> pokemons = pokemonService.getPokemons().stream();
        Map<Integer, List<Pokemon>> groupedPokemons = new HashMap<>();

        groupedPokemons = pokemons.collect(Collectors.groupingBy(e -> e.getNext_evolution().size()));

        return groupedPokemons;

    }

    /**
     * Método que se encarga de exportar los pokémons a un fichero CSV
     */
    public void exportToCSV() {
        pokemonService.exportToCSV();
    }

    /**
     * Método que se encarga de obtener los pokémons agrupados por debilidad
     *
     * @return Los pokémons agrupados por debilidad
     */
    public Map<String, Long> groupedByWeakness() {

        Stream<Pokemon> pokemons = pokemonService.getPokemons().stream();

        Map<String, Long> groupedPokemons;

        groupedPokemons = pokemons.map(Pokemon::getWeaknesses).flatMap(List::stream).collect(Collectors.groupingBy(e -> e, Collectors.counting()));

        return groupedPokemons;

    }


    /**
     * Método que se encarga de obtener la altura media de los pokémons
     *
     * @return La altura media de los pokémons
     */
    public double getPokemonAverageHeight() {

        Stream<Pokemon> pokemons = pokemonService.getPokemons().stream();

        return pokemons.mapToDouble(Pokemon::getHeight).average().orElse(0);

    }

    /**
     * Método que se encarga de obtener el pokémon con el nombre más largo
     *
     * @return El pokémon con el nombre más largo
     */
    public Pokemon getPokemonWithLongestName() {

        Stream<Pokemon> pokemons = pokemonService.getPokemons().stream();

        return pokemons.max(Comparator.comparing(pk -> {
            String name = pk.getName();
            return name.length();
        })).orElse(null);

    }

    /**
     * Método que se encarga de obtener el pokémon más pesado
     *
     * @return El pokémon más pesado
     */
    public Pokemon getHeaviestPokemon() {

        Stream<Pokemon> pokemons = pokemonService.getPokemons().stream();

        return pokemons.max(Comparator.comparingDouble(Pokemon::getWeight)).orElse(null);

    }


    /**
     * Método que se encarga de obtener el número de pokémons que tienen una debilidad
     *
     * @return El número de pokémons que tienen una debilidad
     */
    public long getNumberOfPokemonsOnlyOneWeakness() {

        Stream<Pokemon> pokemons = pokemonService.getPokemons().stream();

        return pokemons.filter(e -> e.getWeaknesses().size() == 1).count();

    }


    /**
     * Método que se encarga de obtener todos los pokémons de un tipo dado
     *
     * @param type Tipo de pokémon
     * @return Todos los pokémons de un tipo dado
     */
    public List<String> getPokemonsByType(String type) {

        Stream<Pokemon> pokemons = pokemonService.getPokemons().stream();

        return pokemons.filter(e -> e.getType().contains(type)).map(Pokemon::getName).toList();

    }


    /**
     * Método que se encarga de obtener los 10 primeros pokémons
     *
     * @return Los 10 primeros pokémons
     */
    public List<String> tenFirstPokemons() {

        return pokemonService.getPokemons().stream().limit(10).map(Pokemon::getName).toList();

    }

    /**
     * Método que se encarga de obtener los 10 últimos pokémons
     *
     * @return
     */
    public List<Pokemon> getPokemonsFromCSV() {
        return pokemonService.getCSV().orElseThrow();
    }

    /**
     * Método que se encarga de importar los pokémons de un fichero CSV a la base de datos
     *
     * @return Los pokémons importados
     * @throws SQLException Si hay algún error con la base de datos
     * @throws IOException  Si hay algún error con el fichero CSV
     */
    public List<Pokemon> importCsvToDB() throws SQLException, IOException {

        List<Pokemon> readedPokemons = getPokemonsFromCSV();

        DatabaseManager dbManager = DatabaseManager.getInstance();

        readedPokemons.stream().forEach(pokemonService::insertPokemon);

        return readedPokemons;

    }

    /**
     * Método que se encarga de leer todos los pokémons de la base de datos
     *
     * @return Todos los pokémons de la base de datos
     */
    public List<Pokemon> readAllPokemonsFromDB() {

        return pokemonService.findAll();

    }

    /**
     * Método que se encarga de leer un pokémon de la base de datos por su nombre
     *
     * @param name Nombre del pokémon
     * @return El pokémon con el nombre dado
     * @throws SQLException Si hay algún error con la base de datos
     * @throws IOException  Si hay algún error con el fichero CSV
     */
    public Pokemon getPokemonFromDBByName(String name) {
        return pokemonService.findPokemonByName(name).orElseThrow();
    }

    /**
     * Método que se encarga de leer un pokémon de la base de datos por su nombre
     *
     * @param name Nombre del pokémon
     * @return El pokémon con el nombre dado
     */
    public Optional<Pokemon> getPokemonByName(String name) {
        Stream<Pokemon> pokemons = pokemonService.getPokemons().stream();
        return pokemons.filter(pokemon -> pokemon.getName().equals(name)).findFirst();
    }

    /**
     * Método que se encarga de obtener el pokémon con menos evoluciones
     *
     * @return El pokémon con menos evoluciones
     */
    public Pokemon getPokemonWithLessEvolutions() {
        Stream<Pokemon> pokemons = pokemonService.getPokemons().stream();
        return pokemons.min(Comparator.comparingInt(pokemon -> pokemon.getNext_evolution().size())).orElse(null);
    }

}
