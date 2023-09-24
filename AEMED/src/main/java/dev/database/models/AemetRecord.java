package dev.database.models;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Data
@Builder()
public class AemetRecord {
    private final UUID id;
    private final String city;
    private final String province;
    private final double maxTemp;
    private final double minTemp;
    private final LocalTime minTempTime;
    private final LocalTime maxTempTime;
    private final double precipitation;
    private final LocalDate date;

    public static AemetRecord fromCsv(String csv, LocalDate date) {
        List<String> values = Arrays.stream(csv.split(";")).map(String::trim).toList();
        String city = values.get(0);
        String province = values.get(1);
        double maxTemp = Double.parseDouble(values.get(2));
        String maxTempTimeString = values.get(3);
        if (maxTempTimeString.length() < 5) {
            maxTempTimeString = "0" + maxTempTimeString;
        }
        LocalTime maxTempTime = LocalTime.parse(maxTempTimeString);
        double minTemp = Double.parseDouble(values.get(4));
        String minTempTimeString = values.get(5);
        if (minTempTimeString.length() < 5) {
            minTempTimeString = "0" + minTempTimeString;
        }
        LocalTime minTempTime = LocalTime.parse(minTempTimeString);
        double precipitation = Double.parseDouble(values.get(6));
        return AemetRecord.builder()
                .id(UUID.randomUUID())
                .city(city)
                .province(province)
                .maxTemp(maxTemp)
                .maxTempTime(maxTempTime)
                .minTemp(minTemp)
                .minTempTime(minTempTime)
                .precipitation(precipitation)
                .date(date)
                .build();
    }
}
