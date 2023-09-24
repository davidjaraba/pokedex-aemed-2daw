package dev.controllers;

import dev.services.AemetService;

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
}
