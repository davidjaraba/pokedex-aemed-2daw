package dev.models;


import lombok.Data;

import java.util.List;

@Data
public class Pokemon {

    public static final char CSV_SEPARATOR = ';';

    private int id;
    private String num;
    private String name;
    private String img;
    private List<String> type;
    private double height;
    private double weight;
    private String candy;
    private int candy_count;
    private String egg;
    private double spawn_chance;
    private double avg_spawns;
    private String spawn_time;
    private List<Double> multipliers;
    private List<String> weaknesses;
    private List<NextEvolution> next_evolution;
    private List<PrevEvolution> prev_evolution;

    @Override
    public String toString() {
        return "Pokemon{" +
                "id=" + id +
                ", num='" + num + '\'' +
                ", name='" + name + '\'' +
                ", img='" + img + '\'' +
                ", type=" + type +
                ", height='" + height + '\'' +
                ", weight='" + weight + '\'' +
                ", candy='" + candy + '\'' +
                ", candy_count=" + candy_count +
                ", egg='" + egg + '\'' +
                ", spawn_chance=" + spawn_chance +
                ", avg_spawns=" + avg_spawns +
                ", spawn_time='" + spawn_time + '\'' +
                ", multipliers=" + multipliers +
                ", weaknesses=" + weaknesses +
                ", next_evolution=" + next_evolution +
                ", prev_evolution=" + prev_evolution +
                '}';
    }

    public String toCSV() {
        return String.valueOf(id) + CSV_SEPARATOR +
                num + CSV_SEPARATOR +
                name + CSV_SEPARATOR +
                height + CSV_SEPARATOR +
                weight + CSV_SEPARATOR;
    }
}

