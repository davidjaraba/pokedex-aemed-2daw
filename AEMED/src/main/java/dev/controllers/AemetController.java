package dev.controllers;

import dev.models.ProvinceData;
import dev.services.AemetService;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Controlador que implementa los metodos de la clase services
 * @author David Jaraba y Jorge Benavente
 * @version 1.0
 */
public class AemetController {
    private final AemetService service;

    public AemetController(AemetService service) {

        this.service = service;
    }

    /**
     * Funcion para importar los datos de los csv a la BD
     * @throws IOException
     */
    public void importCsv() {
        try {
            service.importCsv();
        } catch (Exception e) {
            System.err.println("Error al importar los datos");
            e.printStackTrace();
        }
    }

    /**
     * Funcion para obtener la temperatura maxima de cada provincia agrupado por dia y provincia
     * @return Map<ProvinceDateGroup, Double>
     * @throws SQLException
     * @throws IOException
     */
    public Map<AemetService.ProvinceDateGroup, Double> getMaxTempGroupedByProvinceDay() {
        try {
            return service.getMaxTempGroupedByProvinceDay();
        } catch (Exception e) {
            System.err.println("Error al obtener los datos");
            e.printStackTrace();
            return Map.of();
        }
    }

    /**
     * Funcion para obtener la media de temperatura agrupada por dia y provincia
     * @return Map<ProvinceDateGroup, Double>
     * @throws SQLException
     * @throws IOException
     */
    public Map<AemetService.ProvinceDateGroup, Double> getAvgTempGroupedByProvinceDay() {
        try {
            return service.getAvgTempGroupedByProvinceDay();
        } catch (Exception e) {
            System.err.println("Error al obtener los datos");
            e.printStackTrace();
            return Map.of();
        }
    }

    /**
     * Funcion para obtener la media de precipitaciones agrupada por dia y provincia
     * @return Map<ProvinceDateGroup, Double>
     * @throws SQLException
     * @throws IOException
     */
    public Map<AemetService.ProvinceDateGroup, Double> getAvgPrecipitationGroupedByProvinceDay() {
        try {
            return service.getAvgPrecipitationGroupedByProvinceDay();
        } catch (Exception e) {
            System.err.println("Error al obtener los datos");
            e.printStackTrace();
            return Map.of();
        }
    }

    /**
     * Funcion para obtener la temperatura maxima por cada dia
     * @return Map<java.time.LocalDate, String>
     * @throws SQLException
     * @throws IOException
     */
    public Map<LocalDate, String> getMaxTempByDate() throws SQLException, IOException {
        return service.getMaxTempByDate();
    }

    /**
     * Funcion para obtener la temperatura minima por cada dia
     * @return Map<java.time.LocalDate, String>
     * @throws SQLException
     * @throws IOException
     */
    public Map<LocalDate, String> getMinTempByDate() throws SQLException, IOException {
        return service.getMinTempByDate();
    }

    /**
     * Funcion para obtener la temperatura minima agrupada por dia y provincia
     * @return Map<ProvinceDateGroup, Double>
     * @throws SQLException
     * @throws IOException
     */
    public Map<AemetService.ProvinceDateGroup, Double> getMinTempGroupedByDateAndProvince() throws SQLException, IOException {
        return service.getMinTempGroupedByDateAndProvince();
    }

    /**
     * Funcion para obtener la precipitacion maxima agrupada por precipitacion y dia
     * @return Map<PrecipitationDateGroup, String>
     * @throws SQLException
     * @throws IOException
     */
    public Map<AemetService.PrecipitationDateGroup, String> getMaxPrecipitationByDay() throws SQLException, IOException {
        return service.getMaxPrecipitationByDay();
    }

    /**
     * Funcion para obtener donde ha llovido agrupado por provincia y dia
     * @return List<ProvinceDateGroup>
     * @throws SQLException
     * @throws IOException
     */
    public List<AemetService.ProvinceDateGroup> getPrecipitationGroupedByProvinceAndDate() throws SQLException, IOException {
        return service.getPrecipitationGroupedByProvinceAndDate();
    }

    /**
     * Funcion para exportar los datos de una provincia concreta a json
     * @param province
     * @throws SQLException
     * @throws IOException
     */
    public void exportToJson(String province) throws SQLException, IOException {
        service.exportToJson(province);
    }

    /**
     * Funcion para obtener donde ha sido el lugar en que mas ha llovido
     * @return String
     * @throws SQLException
     * @throws IOException
     */
    public String getMostRainPlace() throws SQLException, IOException {
        return service.getMostRainPlace();
    }

    /**
     * Funcion para obtener datos expecificos y medias de una provincia dada
     * @param province
     * @return Map<LocalDate, ProvinceData>
     * @throws SQLException
     * @throws IOException
     */
    public Map<LocalDate, ProvinceData> getDataByDateAtProvince(String province) throws SQLException, IOException {
        return service.getDataByDateAtProvince(province);
    }

}
