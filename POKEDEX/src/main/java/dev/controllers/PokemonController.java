package dev.controllers;

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
}
