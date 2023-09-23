package dev.controllers;

import dev.models.NextEvolution;
import dev.models.Pokemon;
import dev.services.PokemonService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PokemonControllerTest {

    @InjectMocks
    PokemonController pokemonController;

    @Mock
    PokemonService pokemonService;

    @Test
    public void testAgruparPorTipos() {
        Pokemon pokemon = new Pokemon();
        pokemon.setType(Arrays.asList("Grass", "Poison"));
        Pokemon pokemon2 = new Pokemon();
        pokemon2.setType(List.of("Fire"));
        Pokemon pokemon3 = new Pokemon();
        pokemon3.setType(List.of("Fire"));

        Mockito.when(pokemonService.getPokemons()).thenReturn(Arrays.asList(pokemon, pokemon2, pokemon3));

        Map<String, List<Pokemon>> agrupadosPorTipo = pokemonController.groupedByType();

        assertEquals(3, agrupadosPorTipo.size());
    }

    @Test
    public void testMostCommonWeakness() {
        Pokemon pokemon = new Pokemon();
        pokemon.setWeaknesses(Arrays.asList("Fire", "Water"));
        Pokemon pokemon2 = new Pokemon();
        pokemon2.setWeaknesses(List.of("Fire"));
        Pokemon pokemon3 = new Pokemon();
        pokemon3.setWeaknesses(List.of("Fire"));

        Mockito.when(pokemonService.getPokemons()).thenReturn(Arrays.asList(pokemon, pokemon2, pokemon3));

        String mostCommonWeakness = pokemonController.getMostCommonWeakness();

        assertEquals("Fire", mostCommonWeakness);
    }

    @Test
    public void getLast5PokemonName() {
        Pokemon pokemon = new Pokemon();
        pokemon.setId(1);
        pokemon.setName("Pokemon 1");
        Pokemon pokemon2 = new Pokemon();
        pokemon2.setId(2);
        pokemon2.setName("Pokemon 2");
        Pokemon pokemon3 = new Pokemon();
        pokemon3.setId(3);
        pokemon3.setName("Pokemon 3");
        Pokemon pokemon4 = new Pokemon();
        pokemon4.setId(4);
        pokemon4.setName("Pokemon 4");
        Pokemon pokemon5 = new Pokemon();
        pokemon5.setId(5);
        pokemon5.setName("Pokemon 5");
        Pokemon pokemon6 = new Pokemon();
        pokemon6.setId(6);
        pokemon6.setName("Pokemon 6");
        Pokemon pokemon7 = new Pokemon();
        pokemon7.setId(7);
        pokemon7.setName("Pokemon 7");

        Mockito.when(pokemonService.getPokemons()).thenReturn(Arrays.asList(pokemon, pokemon2, pokemon3, pokemon4, pokemon5, pokemon6, pokemon7));

        List<String> last5PokemonName = pokemonController.getLast5PokemonNames();

        assertAll(
                () -> assertEquals(5, last5PokemonName.size()),
                () -> assertEquals("Pokemon 7", last5PokemonName.get(0)),
                () -> assertEquals("Pokemon 6", last5PokemonName.get(1)),
                () -> assertEquals("Pokemon 5", last5PokemonName.get(2)),
                () -> assertEquals("Pokemon 4", last5PokemonName.get(3)),
                () -> assertEquals("Pokemon 3", last5PokemonName.get(4))
        );
    }

    @Test
    public void getNextEvolutionTestWhenExists() {
        Pokemon pokemon = new Pokemon();
        pokemon.setId(1);
        pokemon.setNum("001");
        pokemon.setName("Charmander");
        NextEvolution nextEvolution = new NextEvolution();
        nextEvolution.setName("Charmeleon");
        nextEvolution.setNum("002");
        pokemon.setNext_evolution(List.of(nextEvolution));
        Pokemon pokemon2 = new Pokemon();
        pokemon2.setId(2);
        pokemon2.setNum("002");
        pokemon2.setName("Charmeleon");
        Mockito.when(pokemonService.getPokemons()).thenReturn(Arrays.asList(pokemon, pokemon2));
        Pokemon evolution = pokemonController.getNextEvolution("Charmander");
        assertEquals("Charmeleon", evolution.getName());
    }

    @Test
    public void getNextEvolutionTestWhenNotExists() {
        Pokemon evolution = pokemonController.getNextEvolution("Raichu");
        assertNull(evolution);
    }

    @Test
    public void getWaterOrElectricPokemonsTest() {
        Pokemon pokemon = new Pokemon();
        pokemon.setId(1);
        pokemon.setName("Charmander");
        pokemon.setType(List.of("Fire"));
        Pokemon pokemon2 = new Pokemon();
        pokemon2.setId(2);
        pokemon2.setName("Pikachu");
        pokemon2.setType(List.of("Electric"));
        Pokemon pokemon3 = new Pokemon();
        pokemon3.setId(3);
        pokemon3.setName("Squirtle");
        pokemon3.setType(List.of("Water"));
        Pokemon pokemon4 = new Pokemon();
        pokemon4.setId(4);
        pokemon4.setName("Bulbasaur");
        pokemon4.setType(List.of("Grass"));
        Mockito.when(pokemonService.getPokemons()).thenReturn(Arrays.asList(pokemon, pokemon2, pokemon3, pokemon4));
        List<Pokemon> waterOrElectricPokemons = pokemonController.getWaterOrElectricPokemons();
        assertAll(
                () -> assertEquals(2, waterOrElectricPokemons.size()),
                () -> assertEquals("Pikachu", waterOrElectricPokemons.get(0).getName()),
                () -> assertEquals("Squirtle", waterOrElectricPokemons.get(1).getName())
        );
    }

    @Test
    public void getMostWeaknessPokemon() {
        Pokemon pokemon = new Pokemon();
        pokemon.setId(1);
        pokemon.setName("Charmander");
        pokemon.setWeaknesses(List.of("Water"));
        Pokemon pokemon2 = new Pokemon();
        pokemon2.setId(2);
        pokemon2.setName("Pikachu");
        pokemon2.setWeaknesses(List.of("Fire"));
        Pokemon pokemon3 = new Pokemon();
        pokemon3.setId(3);
        pokemon3.setName("Squirtle");
        pokemon3.setWeaknesses(List.of("Fire"));
        Pokemon pokemon4 = new Pokemon();
        pokemon4.setId(4);
        pokemon4.setName("Bulbasaur");
        pokemon4.setWeaknesses(List.of("Fire"));
        Mockito.when(pokemonService.getPokemons()).thenReturn(Arrays.asList(pokemon, pokemon2, pokemon3, pokemon4));
        Pokemon mostWeaknessPokemon = pokemonController.getMostWeaknessPokemon();
        assertEquals("Charmander", mostWeaknessPokemon.getName());
    }

    @Test
    public void getTallestPokemonTest() {
        Pokemon pokemon = new Pokemon();
        pokemon.setId(1);
        pokemon.setName("Charmander");
        pokemon.setHeight(0.6);
        Pokemon pokemon2 = new Pokemon();
        pokemon2.setId(2);
        pokemon2.setName("Pikachu");
        pokemon2.setHeight(0.4);
        Pokemon pokemon3 = new Pokemon();
        pokemon3.setId(3);
        pokemon3.setName("Squirtle");
        pokemon3.setHeight(0.5);
        Pokemon pokemon4 = new Pokemon();
        pokemon4.setId(4);
        pokemon4.setName("Bulbasaur");
        pokemon4.setHeight(0.7);
        Mockito.when(pokemonService.getPokemons()).thenReturn(Arrays.asList(pokemon, pokemon2, pokemon3, pokemon4));
        Pokemon tallestPokemon = pokemonController.getTallestPokemon();
        assertEquals("Bulbasaur", tallestPokemon.getName());
    }

    @Test
    public void getAverageWeightTest() {
        Pokemon pokemon = new Pokemon();
        pokemon.setId(1);
        pokemon.setName("Charmander");
        pokemon.setWeight(0.6);
        Pokemon pokemon2 = new Pokemon();
        pokemon2.setId(2);
        pokemon2.setName("Pikachu");
        pokemon2.setWeight(0.4);
        Pokemon pokemon3 = new Pokemon();
        pokemon3.setId(3);
        pokemon3.setName("Squirtle");
        pokemon3.setWeight(0.5);
        Pokemon pokemon4 = new Pokemon();
        pokemon4.setId(4);
        pokemon4.setName("Bulbasaur");
        pokemon4.setWeight(0.7);
        Mockito.when(pokemonService.getPokemons()).thenReturn(Arrays.asList(pokemon, pokemon2, pokemon3, pokemon4));

        double averageWeight = pokemonController.getAverageWeight();
        assertEquals(0.55, averageWeight);
    }

    @Test
    public void getAverageEvolutions() {
        Pokemon pokemon = new Pokemon();
        pokemon.setId(1);
        pokemon.setName("Charmander");
        pokemon.setNum("001");
        NextEvolution nextEvolution = new NextEvolution();
        nextEvolution.setName("Charmeleon");
        nextEvolution.setNum("002");
        pokemon.setNext_evolution(List.of(nextEvolution));
        Pokemon pokemon2 = new Pokemon();
        pokemon2.setId(2);
        pokemon2.setNum("002");
        pokemon2.setName("Charmeleon");
        Pokemon pokemon3 = new Pokemon();
        pokemon3.setId(3);
        pokemon3.setName("Squirtle");
        pokemon3.setNum("003");
        NextEvolution nextEvolution2 = new NextEvolution();
        nextEvolution2.setName("Wartortle");
        nextEvolution2.setNum("004");
        pokemon3.setNext_evolution(List.of(nextEvolution2));
        Pokemon pokemon4 = new Pokemon();
        pokemon4.setId(4);
        pokemon4.setNum("004");
        pokemon4.setName("Wartortle");
        Mockito.when(pokemonService.getPokemons()).thenReturn(Arrays.asList(pokemon, pokemon2, pokemon3, pokemon4));

        double averageEvolutions = pokemonController.getAverageEvolutionCount();
        assertEquals(0.5, averageEvolutions);
    }

    @Test
    public void getAverageWeaknesses() {
        Pokemon pokemon = new Pokemon();
        pokemon.setId(1);
        pokemon.setName("Charmander");
        pokemon.setWeaknesses(List.of("Water"));
        Pokemon pokemon2 = new Pokemon();
        pokemon2.setId(2);
        pokemon2.setName("Pikachu");
        pokemon2.setWeaknesses(List.of("Fire"));
        Pokemon pokemon3 = new Pokemon();
        pokemon3.setId(3);
        pokemon3.setName("Squirtle");
        pokemon3.setWeaknesses(List.of("Fire"));
        Pokemon pokemon4 = new Pokemon();
        pokemon4.setId(4);
        pokemon4.setName("Bulbasaur");
        pokemon4.setWeaknesses(List.of("Fire"));
        Mockito.when(pokemonService.getPokemons()).thenReturn(Arrays.asList(pokemon, pokemon2, pokemon3, pokemon4));

        double averageWeaknesses = pokemonController.getAverageWeaknessCount();
        assertEquals(1.0, averageWeaknesses);
    }

    @Test
    public void groupedByEvolutionsTest() {
        Pokemon pokemon = new Pokemon();
        pokemon.setName("Charmander");
        NextEvolution nextEvolution = new NextEvolution();
        nextEvolution.setName("Charmeleon");
        pokemon.setNext_evolution(List.of(nextEvolution));
        
        Pokemon pokemon2 = new Pokemon();
        pokemon2.setName("Evee");
        pokemon2.setNext_evolution(List.of());

        Pokemon pokemon3 = new Pokemon();
        pokemon3.setName("Charizard");
        NextEvolution nextEvolution2 = new NextEvolution();
        nextEvolution2.setName("Vaporeon");
        NextEvolution nextEvolution3 = new NextEvolution();
        nextEvolution3.setName("Vaporeon2");
        pokemon3.setNext_evolution(List.of(nextEvolution2, nextEvolution3));

        Mockito.when(pokemonService.getPokemons()).thenReturn(Arrays.asList(pokemon, pokemon2, pokemon3));
        Map<Integer, List<Pokemon>> groupedByEvolutions = pokemonController.groupedByEvolutions();
        assertAll(
                () -> assertEquals(3, groupedByEvolutions.size()),
                () -> assertEquals(1, groupedByEvolutions.get(0).size()),
                () -> assertEquals(1, groupedByEvolutions.get(2).size())
        );
    }


    @Test
    public void groupedByWeaknessTest (){

        Pokemon pokemon = new Pokemon();
        pokemon.setWeaknesses(Arrays.asList("Fire", "Water"));
        Pokemon pokemon2 = new Pokemon();
        pokemon2.setWeaknesses(List.of("Fire"));
        Pokemon pokemon3 = new Pokemon();
        pokemon3.setWeaknesses(List.of("Electric", "Poison"));
        Pokemon pokemon4 = new Pokemon();
        pokemon4.setWeaknesses(List.of("Water"));
        Pokemon pokemon5 = new Pokemon();
        pokemon5.setWeaknesses(List.of("Ground", "Poison", "Fire"));


        Mockito.when(pokemonService.getPokemons()).thenReturn(Arrays.asList(pokemon, pokemon2, pokemon3, pokemon4, pokemon5));


        Map<String, Long> groupedByWeakness = pokemonController.groupedByWeakness();


        assertAll(
                () -> assertEquals(5, groupedByWeakness.size()),
                () -> assertEquals(3, groupedByWeakness.get("Fire")),
                () -> assertEquals(2, groupedByWeakness.get("Water")),
                () -> assertEquals(1, groupedByWeakness.get("Electric")),
                () -> assertNull(groupedByWeakness.get("Ghost"))
        );


    }



}