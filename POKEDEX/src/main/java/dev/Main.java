package dev;

import dev.models.Pokemon;
import dev.controllers.PokemonController;

import java.util.List;
import java.util.Map;


public class Main {

    public static void main(String[] args) {
        System.out.println("Hello Pokedex!");
        var pokeController = PokemonController.getInstance();
        Map<String, List<Pokemon>> agrupadosPorTipo = pokeController.groupedByType();
        System.out.println("Agrupados por tipo: ");
        agrupadosPorTipo.forEach((tipo, list) -> System.out.println(tipo + ": " + list.size()));
        System.out.println("Debilidad más común: ");
        System.out.println(pokeController.getMostCommonWeakness());
        System.out.println("Últimos 5 pokemons: ");
        pokeController.getLast5PokemonNames().forEach(System.out::println);
        System.out.println("Evolución de Charmander: ");
        System.out.println(pokeController.getNextEvolution("Charmander"));
        System.out.println("Pokémons eléctricos o de agua: ");
        System.out.println(pokeController.getWaterOrElectricPokemons().stream().map(Pokemon::getName).toList());
        System.out.println("Pokémon con más debilidades: ");
        System.out.println(pokeController.getMostWeaknessPokemon());
        System.out.println("Pokémons sin evolución de tipo fuego: ");
        System.out.println(pokeController.getPokemonWithNoFireEvolution().stream().map(Pokemon::getName).toList());
        System.out.println("Pokémon más alto: ");
        System.out.println(pokeController.getTallestPokemon());
        System.out.println("Media de peso de pokémons: ");
        System.out.println(pokeController.getAverageWeight());
        System.out.println("Media de evoluciones de pokémons: ");
        System.out.println(pokeController.getAverageEvolutionCount());
    }
}
