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
        System.out.println("Pokemon con el nombre más largo");
        System.out.println(pokeController.getPokemonWithLongestName().getName());
    }
}
