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

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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



    @Test
    public void getPokemonHeightAverageTest (){

        Pokemon pokemon = new Pokemon();
        pokemon.setHeight(15);
        Pokemon pokemon2 = new Pokemon();
        pokemon2.setHeight(5);
        Pokemon pokemon3 = new Pokemon();
        pokemon3.setHeight(40);


        Mockito.when(pokemonService.getPokemons()).thenReturn(Arrays.asList(pokemon, pokemon2, pokemon3));

        double average = pokemonController.getPokemonAverageHeight();


        assertEquals(20, average);

    }
    @Test
    public void getPokemonWithLongestNameTest (){

        Pokemon pokemon = new Pokemon();
        pokemon.setName("zzzzzzzz");
        Pokemon pokemon2 = new Pokemon();
        pokemon2.setName("ssddsdfg");
        Pokemon pokemon3 = new Pokemon();
        pokemon3.setName("fffff");

        Mockito.when(pokemonService.getPokemons()).thenReturn(Arrays.asList(pokemon,pokemon2,pokemon3));

        Pokemon res = pokemonController.getPokemonWithLongestName();

        assertEquals(res.getName(), "zzzzzzzz");

    }

    @Test
    public void heaviestPokemonTest(){

        Pokemon pokemon = new Pokemon();
        pokemon.setId(1);
        pokemon.setWeight(100);
        pokemon.setName("Pokemon 1");
        Pokemon pokemon2 = new Pokemon();
        pokemon2.setId(2);
        pokemon2.setWeight(0.2);
        pokemon2.setName("Pokemon 2");
        Pokemon pokemon3 = new Pokemon();
        pokemon3.setId(3);
        pokemon3.setWeight(20);
        pokemon3.setName("Pokemon 3");

        Mockito.when(pokemonService.getPokemons()).thenReturn(Arrays.asList(pokemon,pokemon2,pokemon3));

        Pokemon res = pokemonController.getHeaviestPokemon();

        assertEquals(res, pokemon);

    }
    @Test
    public void getNumberOfPokemonsOnlyOneWeaknessTest(){

        Pokemon pokemon = new Pokemon();
        pokemon.setWeaknesses(Arrays.asList("Fire", "Water"));
        Pokemon pokemon2 = new Pokemon();
        pokemon2.setWeaknesses(List.of("Fire"));
        Pokemon pokemon3 = new Pokemon();
        pokemon3.setWeaknesses(List.of("Fire","Ground"));
        Pokemon pokemon4 = new Pokemon();
        pokemon4.setWeaknesses(List.of("Poison"));


        Mockito.when(pokemonService.getPokemons()).thenReturn(Arrays.asList(pokemon, pokemon2, pokemon3,pokemon4));

        long result = pokemonController.getNumberOfPokemonsOnlyOneWeakness();

        assertEquals(2, result);

    }

    @Test
    public void getPokemonsByTypeTestEmpty(){

        Pokemon pokemon = new Pokemon();
        pokemon.setId(1);
        pokemon.setNum("001");
        pokemon.setName("Charmander");
        pokemon.setType(List.of("Rock"));
        Pokemon pokemon2 = new Pokemon();
        pokemon2.setId(2);
        pokemon2.setNum("002");
        pokemon2.setName("Charmeleon");
        pokemon2.setType(List.of("Poison"));
        Pokemon pokemon3 = new Pokemon();
        pokemon3.setId(3);
        pokemon3.setNum("003");
        pokemon3.setName("Charizard");
        pokemon3.setType(List.of("Flying"));

        Mockito.when(pokemonService.getPokemons()).thenReturn(Arrays.asList(pokemon, pokemon2, pokemon3));

        List<String> firePokems = pokemonController.getPokemonsByType("Fire");

        assertEquals(0, firePokems.size());

    }

    @Test
    public void getPokemonsByTypeTestNotEmpty(){

        Pokemon pokemon = new Pokemon();
        pokemon.setId(1);
        pokemon.setNum("001");
        pokemon.setName("Charmander");
        pokemon.setType(List.of("Fire"));
        Pokemon pokemon2 = new Pokemon();
        pokemon2.setId(2);
        pokemon2.setNum("002");
        pokemon2.setName("Charmeleon");
        pokemon2.setType(List.of("Fire"));
        Pokemon pokemon3 = new Pokemon();
        pokemon3.setId(3);
        pokemon3.setNum("003");
        pokemon3.setName("Charizard");
        pokemon3.setType(List.of("Flying"));

        Mockito.when(pokemonService.getPokemons()).thenReturn(Arrays.asList(pokemon, pokemon2, pokemon3));

        List<String> firePokems = pokemonController.getPokemonsByType("Fire");

        assertEquals(2, firePokems.size());

    }
    @Test
    public void tenFirstPokemonsTestWithTen(){

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
        Pokemon pokemon8 = new Pokemon();
        pokemon8.setId(8);
        pokemon8.setName("Pokemon 8");
        Pokemon pokemon9 = new Pokemon();
        pokemon9.setId(9);
        pokemon9.setName("Pokemon 9");
        Pokemon pokemon10 = new Pokemon();
        pokemon10.setId(10);
        pokemon10.setName("Pokemon 10");
        Pokemon pokemon11 = new Pokemon();
        pokemon11.setId(11);
        pokemon11.setName("Pokemon 11");

        Mockito.when(pokemonService.getPokemons()).thenReturn(Arrays.asList(pokemon, pokemon2, pokemon3, pokemon4, pokemon5, pokemon6, pokemon7, pokemon8, pokemon9, pokemon10, pokemon11));

        List<String> tenFirstPokemons = pokemonController.tenFirstPokemons();

        assertAll(
                () -> assertEquals(10, tenFirstPokemons.size()),
                () -> assertEquals("Pokemon 1", tenFirstPokemons.get(0)),
                () -> assertEquals("Pokemon 2", tenFirstPokemons.get(1)),
                () -> assertEquals("Pokemon 3", tenFirstPokemons.get(2)),
                () -> assertEquals("Pokemon 4", tenFirstPokemons.get(3)),
                () -> assertEquals("Pokemon 5", tenFirstPokemons.get(4)),
                () -> assertEquals("Pokemon 6", tenFirstPokemons.get(5)),
                () -> assertEquals("Pokemon 7", tenFirstPokemons.get(6)),
                () -> assertEquals("Pokemon 8", tenFirstPokemons.get(7)),
                () -> assertEquals("Pokemon 9", tenFirstPokemons.get(8)),
                () -> assertEquals("Pokemon 10", tenFirstPokemons.get(9))
        );

    }

    @Test
    public void getPokemonByNameTest() throws SQLException, IOException {
        Pokemon pokemon = new Pokemon();
        pokemon.setName("Charmander");
        Pokemon pokemon2 = new Pokemon();
        pokemon2.setName("Pikachu");
        Pokemon pokemon3 = new Pokemon();
        pokemon3.setName("Squirtle");
        Pokemon pokemon4 = new Pokemon();
        pokemon4.setName("Bulbasaur");
        Mockito.when(pokemonService.getPokemons()).thenReturn(Arrays.asList(pokemon, pokemon2, pokemon3, pokemon4));
        Optional<Pokemon> pokemonByName = pokemonController.getPokemonByName("Pikachu");
        assertAll(
                () -> assertTrue(pokemonByName.isPresent()),
                () -> assertEquals("Pikachu", pokemonByName.get().getName())
        );
    }

    @Test
    public void getPokemonByNameWithNoResultTest() throws SQLException, IOException {
        Pokemon pokemon = new Pokemon();
        pokemon.setName("Charmander");
        Pokemon pokemon2 = new Pokemon();
        pokemon2.setName("Pikachu");
        Pokemon pokemon3 = new Pokemon();
        pokemon3.setName("Squirtle");
        Pokemon pokemon4 = new Pokemon();
        pokemon4.setName("Bulbasaur");
        Mockito.when(pokemonService.getPokemons()).thenReturn(Arrays.asList(pokemon, pokemon2, pokemon3, pokemon4));
        Optional<Pokemon> pokemonByName = pokemonController.getPokemonByName("Evee");
        assertAll(
                () -> assertFalse(pokemonByName.isPresent())
        );
    }

    @Test
    public void getPokemonWithLessEvolution(){
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
        Pokemon pokemonWithLessEvolution = pokemonController.getPokemonWithLessEvolutions();
        assertEquals("Evee", pokemonWithLessEvolution.getName());
    }

}