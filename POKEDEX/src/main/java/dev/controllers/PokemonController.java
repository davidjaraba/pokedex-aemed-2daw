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

    public static PokemonController getInstance() throws IOException, SQLException {
        if (instance == null) {
            PokemonService pokemonService = new PokemonService(DatabaseManager.getInstance());
            instance = new PokemonController(pokemonService);
        }
        return instance;
    }


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

    public List<String> getLast5PokemonNames() {
        Stream<Pokemon> pokemons = pokemonService.getPokemons().stream();
        return pokemons
                .sorted(Comparator.comparing(Pokemon::getId).reversed())
                .limit(5)
                .map(Pokemon::getName)
                .collect(Collectors.toList());
    }

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

    public List<Pokemon> getWaterOrElectricPokemons() {
        Stream<Pokemon> pokemons = pokemonService.getPokemons().stream();
        return pokemons
                .filter(pokemon -> pokemon.getType().contains("Water") || pokemon.getType().contains("Electric"))
                .collect(Collectors.toList());
    }

    public Pokemon getMostWeaknessPokemon() {
        Stream<Pokemon> pokemons = pokemonService.getPokemons().stream();
        return pokemons
                .max(Comparator.comparingInt(pokemon -> pokemon.getWeaknesses().size()))
                .orElse(null);
    }

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

    public Pokemon getTallestPokemon() {
        Stream<Pokemon> pokemons = pokemonService.getPokemons().stream();
        return pokemons
                .max(Comparator.comparingDouble(Pokemon::getHeight))
                .orElse(null);
    }

    public double getAverageWeight() {
        Stream<Pokemon> pokemons = pokemonService.getPokemons().stream();
        return pokemons
                .mapToDouble(Pokemon::getWeight)
                .average()
                .orElse(0);
    }

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

    public double getAverageWeaknessCount() {
        Stream<Pokemon> pokemons = pokemonService.getPokemons().stream();
        return pokemons
                .mapToInt(p -> p.getWeaknesses().size())
                .average()
                .orElse(0);
    }

    public Map<Integer, List<Pokemon>> groupedByEvolutions (){

        Stream<Pokemon> pokemons = pokemonService.getPokemons().stream();
        Map<Integer, List<Pokemon>> groupedPokemons = new HashMap<>();

        groupedPokemons = pokemons.collect(Collectors.groupingBy(e-> e.getNext_evolution().size()));

        return groupedPokemons;

    }



    public void exportToCSV() {
        pokemonService.exportToCSV();
    }

    public Map<String, Long> groupedByWeakness(){

        Stream<Pokemon> pokemons = pokemonService.getPokemons().stream();

        Map<String, Long> groupedPokemons;

        groupedPokemons = pokemons.map(Pokemon::getWeaknesses).flatMap(List::stream).collect(Collectors.groupingBy(e -> e, Collectors.counting()));

        return groupedPokemons;

    }



    public double getPokemonAverageHeight(){

        Stream<Pokemon> pokemons = pokemonService.getPokemons().stream();

        return pokemons.mapToDouble(Pokemon::getHeight).average().orElse(0);

    }


    public Pokemon getPokemonWithLongestName (){

        Stream<Pokemon> pokemons = pokemonService.getPokemons().stream();

        return pokemons.max(Comparator.comparing(pk -> {
            String name = pk.getName();
            return name.length();
        })).orElse(null);

    }


    public Pokemon getHeaviestPokemon(){

        Stream<Pokemon> pokemons = pokemonService.getPokemons().stream();

        return pokemons.max(Comparator.comparingDouble(Pokemon::getWeight)).orElse(null);

    }


    public long getNumberOfPokemonsOnlyOneWeakness(){

        Stream<Pokemon> pokemons = pokemonService.getPokemons().stream();

        return pokemons.filter(e-> e.getWeaknesses().size() == 1).count();

    }



    public List<String> getPokemonsByType(String type){

        Stream<Pokemon> pokemons = pokemonService.getPokemons().stream();

        return pokemons.filter(e -> e.getType().contains(type)).map(Pokemon::getName).toList();

    }



    public List<String> tenFirstPokemons(){

        return pokemonService.getPokemons().stream().limit(10).map(Pokemon::getName).toList();

    }

    public List<Pokemon> getPokemonsFromCSV(){
        return pokemonService.getCSV().orElseThrow();
    }

    public List<Pokemon> importCsvToDB() throws SQLException, IOException {

        List<Pokemon> readedPokemons = getPokemonsFromCSV();

        DatabaseManager dbManager = DatabaseManager.getInstance();

        readedPokemons.stream().forEach(pokemonService::insertPokemon);

        return readedPokemons;

    }

    public List<Pokemon> readAllPokemonsFromDB() throws SQLException, IOException {

        return pokemonService.findAll();

    }

    public Pokemon getPokemonFromDBByName(String name) throws SQLException, IOException {
        return pokemonService.findPokemonByName(name).orElseThrow();
    }

    public Optional<Pokemon> getPokemonByName(String name) throws SQLException, IOException {
        Stream<Pokemon> pokemons = pokemonService.getPokemons().stream();
        return pokemons.filter(pokemon -> pokemon.getName().equals(name)).findFirst();
    }

}
