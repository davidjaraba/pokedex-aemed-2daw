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

    }
}