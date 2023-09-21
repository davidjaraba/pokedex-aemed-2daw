package dev.controllers;

import dev.models.Pokemon;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PokemonControllerTest {

    @Mock
    PokemonController pokemonController = PokemonController.getInstance();

    @Test
    public void testAgruparPorTipos() {
        Pokemon pokemon = new Pokemon();
        pokemon.setType(Arrays.asList("Grass", "Poison"));
        Pokemon pokemon2 = new Pokemon();
        pokemon2.setType(List.of("Fire"));
        Pokemon pokemon3 = new Pokemon();
        pokemon3.setType(List.of("Fire"));

        Mockito.when(pokemonController.getPokemons()).thenReturn(Arrays.asList(pokemon, pokemon2, pokemon3));

        Map<String, List<Pokemon>> agrupadosPorTipo = pokemonController.getAgrupadosPorTipo();

        assertEquals(3, agrupadosPorTipo.size());
    }


}