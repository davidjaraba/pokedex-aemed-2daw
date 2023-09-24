package dev.database.models;

import lombok.Builder;
import lombok.Data;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

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

    public static List<AemetRecord> fromResultSet(ResultSet resultSet) throws SQLException {

        List<AemetRecord> res = new ArrayList<>();

        while(resultSet.next()){

            AemetRecord aemetRecord = AemetRecord.builder()
                    .id(UUID.fromString(resultSet.getString("id")))
                    .city(resultSet.getString("city"))
                    .province(resultSet.getString("province"))
                    .maxTemp(resultSet.getDouble("max_temp"))
                    .maxTempTime(resultSet.getTime("max_temp_time").toLocalTime())
                    .minTemp(resultSet.getDouble("min_temp"))
                    .minTempTime(resultSet.getTime("min_temp_time").toLocalTime())
                    .precipitation(resultSet.getDouble("precipitation"))
                    .date(resultSet.getDate("date").toLocalDate())
                    .build();


            res.add(aemetRecord);

        }

        return res;

    }


}
