package dev.controllers;

import dev.models.NextEvolution;
import dev.models.Pokemon;
import dev.services.PokemonService;

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

    public static PokemonController getInstance() {
        if (instance == null) {
            PokemonService pokemonService = new PokemonService();
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
}
