package dev.services;

import dev.database.DatabaseManager;
import dev.database.models.AemetRecord;
import dev.database.models.SqlCommand;
import dev.repository.AemetRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.Map;
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
                    LocalDate date = LocalDate.parse(dateString, ofPattern(dateFormat));
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

    public record ProvinceDateGroup(String province, LocalDate date) {
        @Override
        public String toString() {
            return "[" +
                    "province='" + province + '\'' +
                    ", date=" + date +
                    ']';
        }
    }

    public record PrecipitationDateGroup(Double precipitation, LocalDate date) {
        @Override
        public String toString() {
            return "[" +
                    "precipitation='" + precipitation + '\'' +
                    ", date=" + date +
                    ']';
        }
    }

    public Map<LocalDate, String> getMaxTempByDate() throws SQLException, IOException {

        Map<LocalDate, String> maxTempByDate = new HashMap<>();

        DatabaseManager dbMan = DatabaseManager.getInstance();
        SqlCommand sqlCGetDates = new SqlCommand("SELECT DISTINCT DATE FROM AEMET");
        ResultSet resultSet = dbMan.executeQuery(sqlCGetDates);

        while (resultSet.next()) {
            LocalDate date = resultSet.getDate("DATE").toLocalDate();
            SqlCommand sqlCGetMaxTemp = new SqlCommand("SELECT * FROM AEMET WHERE DATE = ? AND MAX_TEMP = (SELECT MAX(MAX_TEMP) FROM AEMET WHERE DATE = ?)");
            sqlCGetMaxTemp.addParam(date);
            sqlCGetMaxTemp.addParam(date);
            ResultSet resultSet1 = dbMan.executeQuery(sqlCGetMaxTemp);
            AemetRecord.fromResultSet(resultSet1).stream().findFirst().map(a -> maxTempByDate.put(a.getDate(), a.getCity()));
        }

        return maxTempByDate;

    }

    public Map<LocalDate, String> getMinTempByDate() throws SQLException, IOException {

        Map<LocalDate, String> maxTempByDate = new HashMap<>();

        DatabaseManager dbMan = DatabaseManager.getInstance();
        SqlCommand sqlCGetDates = new SqlCommand("SELECT DISTINCT DATE FROM AEMET");
        ResultSet resultSet = dbMan.executeQuery(sqlCGetDates);

        while (resultSet.next()) {
            LocalDate date = resultSet.getDate("DATE").toLocalDate();
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

        return records.stream().collect(Collectors.groupingBy(AemetRecord::getDate, Collectors.maxBy((a, b)-> (int)(a.getPrecipitation() - b.getPrecipitation())))).entrySet().stream().map(entry ->{
            double prec = 0;
            String city = "";
            LocalDate date = LocalDate.now();
            if (entry.getValue().isPresent()){
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

}
