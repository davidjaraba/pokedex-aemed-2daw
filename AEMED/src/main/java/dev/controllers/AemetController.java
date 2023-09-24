package dev.controllers;

import dev.services.AemetService;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Map;

public class AemetController {
    private final AemetService service;

    public AemetController(AemetService service) {

        this.service = service;
    }

    public void importCsv() {
        try {
            service.importCsv();
        } catch (Exception e) {
            System.err.println("Error al importar los datos");
            e.printStackTrace();
        }
    }

    public Map<LocalDate, String> getMaxTempByDate() throws SQLException, IOException {
        return service.getMaxTempByDate();
    }

    public Map<LocalDate, String> getMinTempByDate() throws SQLException, IOException {
        return service.getMinTempByDate();
    }

}
