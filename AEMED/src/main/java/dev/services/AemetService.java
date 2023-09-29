package dev.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.database.DatabaseManager;
import dev.database.models.AemetRecord;
import dev.database.models.SqlCommand;
import dev.models.ProvinceData;
import dev.repository.AemetRepository;
import dev.serializers.LocalDateSerializer;
import dev.serializers.LocalTimeSerializer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.time.format.DateTimeFormatter.ofPattern;

public class AemetService {

    private final AemetRepository repository;

    public AemetService(AemetRepository repository) {
        this.repository = repository;
    }

    public void importCsv() throws IOException {
        Path dataPath = Paths.get("data").toAbsolutePath();
        try (Stream<Path> fileStream = Files.list(dataPath)) {
            List<Path> files = fileStream.filter(path -> path.toString().endsWith(".csv")).toList();
            List<AemetRecord> aemetRecords = files.stream().flatMap(file -> {
                try {
                    String dateString = file.getFileName().toString().substring(5, 13);
                    final String dateFormat = "yyyyMMdd";
                    java.time.LocalDate date = java.time.LocalDate.parse(dateString, ofPattern(dateFormat));
                    return Files.lines(file).map(line -> AemetRecord.fromCsv(line, date));
                } catch (IOException e) {
                    e.printStackTrace();
                    return Stream.empty();
                }
            }).toList();
            repository.saveAll(aemetRecords);
        } catch (SQLException e) {
            System.err.println("Error al importar los datos");
            e.printStackTrace();
        }
    }

    public Map<ProvinceDateGroup, Double> getMaxTempGroupedByProvinceDay() throws SQLException, IOException {
        List<AemetRecord> records = repository.findAll();
        Map<ProvinceDateGroup, Optional<AemetRecord>> map =
                records.stream()
                        .collect(Collectors.groupingBy(record -> new ProvinceDateGroup(record.getProvince(), record.getDate())
                                , Collectors.maxBy((a, b) -> (int) (a.getMaxTemp() - b.getMaxTemp()))));
        return map.entrySet().stream().map(entry -> {
                    double temp = 0;
                    if (entry.getValue().isPresent()) {
                        temp = entry.getValue().get().getMaxTemp();
                    }
                    return Map.entry(entry.getKey(), temp);
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public Map<ProvinceDateGroup, Double> getAvgTempGroupedByProvinceDay() throws SQLException, IOException {
        List<AemetRecord> records = repository.findAll();
        return records.stream()
                .collect(Collectors.groupingBy(record -> new ProvinceDateGroup(record.getProvince(), record.getDate())
                        , Collectors.averagingDouble(AemetRecord::getMaxTemp)));
    }

    public Map<ProvinceDateGroup, Double> getAvgPrecipitationGroupedByProvinceDay() throws SQLException, IOException {
        List<AemetRecord> records = repository.findAll();
        return records.stream()
                .collect(Collectors.groupingBy(record -> new ProvinceDateGroup(record.getProvince(), record.getDate())
                        , Collectors.averagingDouble(AemetRecord::getPrecipitation)));
    }

    public record ProvinceDateGroup(String province, java.time.LocalDate date) {
        @Override
        public String toString() {
            return "[" +
                    "province='" + province + '\'' +
                    ", date=" + date +
                    ']';
        }
    }

    public record PrecipitationDateGroup(Double precipitation, java.time.LocalDate date) {
        @Override
        public String toString() {
            return "[" +
                    "precipitation='" + precipitation + '\'' +
                    ", date=" + date +
                    ']';
        }
    }

    public Map<java.time.LocalDate, String> getMaxTempByDate() throws SQLException, IOException {

        Map<java.time.LocalDate, String> maxTempByDate = new HashMap<>();

        DatabaseManager dbMan = DatabaseManager.getInstance();
        SqlCommand sqlCGetDates = new SqlCommand("SELECT DISTINCT DATE FROM AEMET");
        ResultSet resultSet = dbMan.executeQuery(sqlCGetDates);

        while (resultSet.next()) {
            java.time.LocalDate date = resultSet.getDate("DATE").toLocalDate();
            SqlCommand sqlCGetMaxTemp = new SqlCommand("SELECT * FROM AEMET WHERE DATE = ? AND MAX_TEMP = (SELECT MAX(MAX_TEMP) FROM AEMET WHERE DATE = ?)");
            sqlCGetMaxTemp.addParam(date);
            sqlCGetMaxTemp.addParam(date);
            ResultSet resultSet1 = dbMan.executeQuery(sqlCGetMaxTemp);
            AemetRecord.fromResultSet(resultSet1).stream().findFirst().map(a -> maxTempByDate.put(a.getDate(), a.getCity()));
        }

        return maxTempByDate;

    }

    public Map<java.time.LocalDate, String> getMinTempByDate() throws SQLException, IOException {

        Map<java.time.LocalDate, String> maxTempByDate = new HashMap<>();

        DatabaseManager dbMan = DatabaseManager.getInstance();
        SqlCommand sqlCGetDates = new SqlCommand("SELECT DISTINCT DATE FROM AEMET");
        ResultSet resultSet = dbMan.executeQuery(sqlCGetDates);

        while (resultSet.next()) {
            java.time.LocalDate date = resultSet.getDate("DATE").toLocalDate();
            SqlCommand sqlCGetMaxTemp = new SqlCommand("SELECT * FROM AEMET WHERE DATE = ? AND MIN_TEMP = (SELECT MIN(MIN_TEMP) FROM AEMET WHERE DATE = ?)");
            sqlCGetMaxTemp.addParam(date);
            sqlCGetMaxTemp.addParam(date);
            ResultSet resultSet1 = dbMan.executeQuery(sqlCGetMaxTemp);
            AemetRecord.fromResultSet(resultSet1).stream().findFirst().map(a -> maxTempByDate.put(a.getDate(), a.getCity()));
        }

        return maxTempByDate;

    }

    public Map<ProvinceDateGroup, Double> getMinTempGroupedByDateAndProvince() throws SQLException, IOException {

        List<AemetRecord> records = repository.findAll();

        return records.stream().collect(Collectors.groupingBy(r -> new ProvinceDateGroup(r.getProvince(), r.getDate()),
                        Collectors.minBy((a, b) -> (int) (a.getMinTemp() - b.getMinTemp())))).entrySet().stream().map(entry -> {
                    double temp = 0;
                    if (entry.getValue().isPresent()) {
                        temp = entry.getValue().get().getMinTemp();
                    }
                    return Map.entry(entry.getKey(), temp);
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    }

    public Map<PrecipitationDateGroup, String> getMaxPrecipitationByDay() throws SQLException, IOException {

        List<AemetRecord> records = repository.findAll();

        return records.stream().collect(Collectors.groupingBy(AemetRecord::getDate, Collectors.maxBy((a, b) -> (int) (a.getPrecipitation() - b.getPrecipitation())))).entrySet().stream().map(entry -> {
            double prec = 0;
            String city = "";
            java.time.LocalDate date = java.time.LocalDate.now();
            if (entry.getValue().isPresent()) {
                prec = entry.getValue().get().getPrecipitation();
                city = entry.getValue().get().getCity();
                date = entry.getValue().get().getDate();
            }
            return Map.entry(new PrecipitationDateGroup(prec, date), city);
        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    }

    public List<ProvinceDateGroup> getPrecipitationGroupedByProvinceAndDate() throws SQLException, IOException {

        List<AemetRecord> records = repository.findAll();

        return records.stream().filter(pr -> pr.getPrecipitation() != 0).map(r -> new ProvinceDateGroup(r.getProvince(), r.getDate())).toList();

    }

    public void exportToJson(String province) throws SQLException, IOException {
        List<AemetRecord> records = repository.findAll();
        List<AemetRecord> recordsByProvince = records.stream().filter(r -> r.getProvince().equals(province)).toList();
        final Path filePath = Paths.get("data", province.toLowerCase() + ".json");
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(java.time.LocalDate.class, new LocalDateSerializer())
                .registerTypeAdapter(LocalTime.class, new LocalTimeSerializer())
                .create();
        String json = gson.toJson(recordsByProvince);
        Files.writeString(filePath, json);
    }

    public String getMostRainPlace() throws SQLException, IOException {
        List<AemetRecord> records = repository.findAll();
        Map<String, Double> map = records.stream().collect(Collectors.groupingBy(a -> a.getProvince() + " - " + a.getCity(), Collectors.summingDouble(AemetRecord::getPrecipitation)));
        return map.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
    }

    public Map<LocalDate, ProvinceData> getDataByDateAtProvince(String province) throws SQLException, IOException {

        List<AemetRecord> records = repository.findByProvince(province);


        return records.stream().collect(Collectors.groupingBy(AemetRecord::getDate)).entrySet().stream().map(
                (entry)->{

                    ProvinceData.TempCityGroup maxTemp = entry.getValue().stream().max(Comparator.comparingDouble(AemetRecord::getMaxTemp)).stream().findFirst().map(
                            a -> new ProvinceData.TempCityGroup(a.getMaxTemp(), a.getCity())).get();

                    ProvinceData.TempCityGroup minTemp = entry.getValue().stream().min(Comparator.comparingDouble(AemetRecord::getMinTemp)).stream().findFirst().map(
                            a -> new ProvinceData.TempCityGroup(a.getMinTemp(), a.getCity())).get();

                    double avgMaxTemp = entry.getValue().stream().map(AemetRecord::getMaxTemp).mapToDouble(Double::doubleValue).average().orElse(0.0);

                    double avgMinTemp = entry.getValue().stream().map(AemetRecord::getMinTemp).mapToDouble(Double::doubleValue).average().orElse(0.0);

                    ProvinceData.PrecipitationCityGroup maxPrecipitation = entry.getValue().stream().max(Comparator.comparingDouble(AemetRecord::getPrecipitation)).stream().findFirst().map(
                            a -> new ProvinceData.PrecipitationCityGroup(a.getPrecipitation(), a.getCity())).get();

                    double avgPrecipitation = entry.getValue().stream().map(AemetRecord::getPrecipitation).mapToDouble(Double::doubleValue).average().orElse(0.0);


                    return Map.entry(entry.getKey(), new ProvinceData(avgMaxTemp, avgMinTemp, maxTemp, minTemp, maxPrecipitation, avgPrecipitation));
                }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

}
