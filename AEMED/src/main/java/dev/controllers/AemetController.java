package dev.controllers;

import dev.services.AemetService;

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

    public Map<AemetService.MaxTempGroupedByProvinceDay, Double> getMaxTempGroupedByProvinceDay() {
        try {
            return service.getMaxTempGroupedByProvinceDay();
        } catch (Exception e) {
            System.err.println("Error al obtener los datos");
            e.printStackTrace();
            return Map.of();
        }
    }
}
