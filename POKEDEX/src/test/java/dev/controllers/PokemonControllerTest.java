package dev.controllers;

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


}