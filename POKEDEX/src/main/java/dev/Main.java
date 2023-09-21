package dev;

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
        Map<String, List<Pokemon>> agrupadosPorTipo = pokeController.groupedByType();
        System.out.println("Agrupados por tipo: ");
        agrupadosPorTipo.forEach((tipo, list) -> System.out.println(tipo + ": " + list.size()));
    }
}
