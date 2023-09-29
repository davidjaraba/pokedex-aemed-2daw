package dev.controllers;

import dev.models.ProvinceData;
import dev.services.AemetService;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
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

    public Map<AemetService.ProvinceDateGroup, Double> getMaxTempGroupedByProvinceDay() {
        try {
            return service.getMaxTempGroupedByProvinceDay();
        } catch (Exception e) {
            System.err.println("Error al obtener los datos");
            e.printStackTrace();
            return Map.of();
        }
    }

    public Map<AemetService.ProvinceDateGroup, Double> getAvgTempGroupedByProvinceDay() {
        try {
            return service.getAvgTempGroupedByProvinceDay();
        } catch (Exception e) {
            System.err.println("Error al obtener los datos");
            e.printStackTrace();
            return Map.of();
        }
    }

    public Map<AemetService.ProvinceDateGroup, Double> getAvgPrecipitationGroupedByProvinceDay() {
        try {
            return service.getAvgPrecipitationGroupedByProvinceDay();
        } catch (Exception e) {
            System.err.println("Error al obtener los datos");
            e.printStackTrace();
            return Map.of();
        }
    }


    public Map<LocalDate, String> getMaxTempByDate() throws SQLException, IOException {
        return service.getMaxTempByDate();
    }

    public Map<LocalDate, String> getMinTempByDate() throws SQLException, IOException {
        return service.getMinTempByDate();
    }

    public Map<AemetService.ProvinceDateGroup, Double> getMinTempGroupedByDateAndProvince() throws SQLException, IOException {
        return service.getMinTempGroupedByDateAndProvince();
    }

    public Map<AemetService.PrecipitationDateGroup, String> getMaxPrecipitationByDay() throws SQLException, IOException {
        return service.getMaxPrecipitationByDay();
    }

    public List<AemetService.ProvinceDateGroup> getPrecipitationGroupedByProvinceAndDate() throws SQLException, IOException {
        return service.getPrecipitationGroupedByProvinceAndDate();
    }

    public void exportToJson(String province) throws SQLException, IOException {
        service.exportToJson(province);
    }

    public String getMostRainPlace() throws SQLException, IOException {
        return service.getMostRainPlace();
    }

    public Map<LocalDate, ProvinceData> getDataByDateAtProvince(String province) throws SQLException, IOException {
        return service.getDataByDateAtProvince(province);
    }


}
