package dev;

import dev.controllers.AemetController;
import dev.database.DatabaseManager;
import dev.repository.AemetRepository;
import dev.services.AemetService;

import java.io.IOException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, IOException {
        DatabaseManager databaseManager = DatabaseManager.getInstance();
        AemetRepository repository = new AemetRepository(databaseManager);
        AemetService service = new AemetService(repository);
        AemetController controller = new AemetController(service);
        controller.importCsv();


        System.out.println("Temperatura máxima por provincia y día: ");
        System.out.println(controller.getMaxTempGroupedByProvinceDay());

        System.out.println("Temperatura media por provincia y día: ");
        System.out.println(controller.getAvgTempGroupedByProvinceDay());

        System.out.println("Precipitación media por provincia y día: ");
        System.out.println(controller.getAvgPrecipitationGroupedByProvinceDay());

    }
}