package dev;

import dev.models.Pokemon;
import dev.controllers.PokemonController;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


public class Main {

    public static void main(String[] args) {
        try {
            System.out.println("Hello Pokedex!");
            var pokeController = PokemonController.getInstance();
            System.out.println("Obtener datos de pikachu: "+pokeController.getPokemonByName("Pikachu"));
            Map<String, List<Pokemon>> agrupadosPorTipo = pokeController.groupedByType();
            System.out.println("Agrupados por tipo: ");
            agrupadosPorTipo.forEach((tipo, list) -> System.out.println(tipo + ": " + list.size()));
            System.out.println("Debilidad más común: ");
            System.out.println(pokeController.getMostCommonWeakness());
            System.out.println("Últimos 5 pokemons: ");
            pokeController.getLast5PokemonNames().forEach(System.out::println);
            System.out.println("Evolución de Charmander: ");
            System.out.println(pokeController.getNextEvolution("Charmander"));
            System.out.println("Agrupados pokemons por numero de evoluciones");
            pokeController.groupedByEvolutions().forEach((numEvoluciones, list) -> System.out.println(numEvoluciones + ": " + list.size()));
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
            System.out.println("Media de debilidades de pokémons: ");
            System.out.println(pokeController.getAverageWeaknessCount());
            pokeController.exportToCSV();
            System.out.println("Pokemons agrupados por debilidad: ");
            pokeController.groupedByWeakness().forEach((a, b) -> {

                System.out.println(a + " => " + b);

            });
            System.out.println("Media de altura de los pokemons: "+pokeController.getPokemonAverageHeight()+" m");
            System.out.println("Pokemon con el nombre más largo");
            System.out.println(pokeController.getPokemonWithLongestName().getName());
            System.out.println("Pokemon más pesado: "+pokeController.getHeaviestPokemon());
            System.out.println("Pokemons con una sola debilidad: "+pokeController.getNumberOfPokemonsOnlyOneWeakness());
            System.out.println("Pokemons tipo fire");
            pokeController.getPokemonsByType("Fire").forEach(System.out::println);
            System.out.println("10 primeros");
            pokeController.tenFirstPokemons().forEach(System.out::println);
            System.out.println("Pokemon con menos evoluciones");
            System.out.println(pokeController.getPokemonWithLessEvolutions());
            System.out.println("Pokemons almacenados en el CSV");
            pokeController.getPokemonsFromCSV().forEach(System.out::println);
            System.out.println("Importando CSV a BD");
            pokeController.importCsvToDB();
            System.out.println("Pokemons almacenados en la BD");
            pokeController.readAllPokemonsFromDB().forEach(System.out::println);
            System.out.println("Obteniendo datos de pikachu");
            System.out.println(pokeController.getPokemonFromDBByName("Pikachu"));
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
