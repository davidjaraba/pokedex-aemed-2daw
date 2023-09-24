package dev.services;

import dev.database.models.AemetRecord;
import dev.repository.AemetRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
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

}
