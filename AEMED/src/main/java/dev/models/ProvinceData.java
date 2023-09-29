package dev.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class ProvinceData {

    private double averageMaxTemp;
    private double averageMinTemp;

    private TempCityGroup maxTemp;
    private TempCityGroup minTemp;

    private PrecipitationCityGroup maxPrecipitation;
    private double averagePrecipitation;

    public record TempCityGroup(double temp, String city){

    }

    public record PrecipitationCityGroup(double prec, String city){

    }


}
