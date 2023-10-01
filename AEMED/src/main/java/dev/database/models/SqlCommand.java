package dev.database.models;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class SqlCommand {

    private final String command;
    private final List<Object> params = new ArrayList<>();

    /**
     * Agregar parametros a la consulta
     * @param param
     */
    public void addParam(Object param) {
        params.add(param);
    }


}
