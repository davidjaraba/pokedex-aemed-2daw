package dev;

import dev.controllers.AemetController;
import dev.database.DatabaseManager;
import dev.models.ProvinceData;
import dev.repository.AemetRepository;
import dev.services.AemetService;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Map;


/**
 * @author David Jaraba y Jorge Benavente
 * @version 1.0
 */
public class Main {
    public static void main(String[] args) throws SQLException, IOException {
        DatabaseManager databaseManager = DatabaseManager.getInstance();
        AemetRepository repository = new AemetRepository(databaseManager);
        AemetService service = new AemetService(repository);
        AemetController controller = new AemetController(service);
        controller.importCsv();

        System.out.println("Donde se dio la temperatur maxima cada dia: ");
        controller.getMaxTempByDate().forEach((date,city)->{
            System.out.println(date);
            System.out.println(city);
        });

        System.out.println("Donde se dio la temperatur minima cada dia: ");
        controller.getMinTempByDate().forEach((date,city)->{
            System.out.println(date);
            System.out.println(city);
        });


        System.out.println("Temperatura máxima por provincia y día: ");
        System.out.println(controller.getMaxTempGroupedByProvinceDay());

        System.out.println("Temperatura media por provincia y día: ");
        System.out.println(controller.getAvgTempGroupedByProvinceDay());

        System.out.println("Precipitación media por provincia y día: ");
        System.out.println(controller.getAvgPrecipitationGroupedByProvinceDay());

        System.out.println("Temperatura mínima por provincia y día: ");
        System.out.println(controller.getMinTempGroupedByDateAndProvince());

        System.out.println("Precipitacion maxima por dias y donde se dio: ");
        controller.getMaxPrecipitationByDay().forEach((datePrecGr,city)->{
            System.out.println(datePrecGr);
            System.out.println("Se dio en -> "+city);
        });

        System.out.println("Lugares donde ha llovido agrupados por provincia y dia: ");
        controller.getPrecipitationGroupedByProvinceAndDate().forEach(System.out::println);

        System.out.println("Datos de Madrid: ");
        Map<LocalDate, ProvinceData> pd = controller.getDataByDateAtProvince("Madrid");
        pd.forEach((date,provinceData)->{
            System.out.println(date);
            System.out.println(provinceData);
        });


        System.out.println("Exportando a JSON datos de la provincia Madrid");
        controller.exportToJson("Madrid");
        System.out.println("Lugar donde más ha llovido: " + controller.getMostRainPlace());

    }
}