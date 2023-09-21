package dev;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.models.Pokemon;
import dev.controllers.PokemonController;

import java.util.List;
import java.util.Map;

/**
 * https://json2csharp.com/code-converters/json-to-pojo
 * https://www.jsonschema2pojo.org/
 * https://plugins.jetbrains.com/plugin/8634-robopojogenerator
 * https://marketplace.visualstudio.com/items?itemName=quicktype.quicktype
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("Hello Pokedex!");
        var pokeController = PokemonController.getInstance();
        Map<String, List<Pokemon>> agrupadosPorTipo = pokeController.getAgrupadosPorTipo();
    }

    private static void printPokemon(Pokemon pokemon) {
        System.out.println(pokemon);
    }

    private static void printPokemonJson(Pokemon pokemon) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gson.toJson(pokemon));
    }
}
