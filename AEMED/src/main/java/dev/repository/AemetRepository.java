package dev.repository;

import dev.database.DatabaseManager;
import dev.database.models.AemetRecord;
import dev.database.models.SqlCommand;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Implementa los metodos de la interfaz CRUD
 */
public class AemetRepository implements ICrudRepository<AemetRecord, UUID>, AutoCloseable {
    private final DatabaseManager databaseManager;

    public AemetRepository(DatabaseManager dbManager) {
        databaseManager = dbManager;
    }

    /**
     * Funcion para guardar un objeto en la base de datos
     * @param aemetRecord
     * @return
     * @throws SQLException
     * @throws IOException
     */
    @Override
    public AemetRecord save(AemetRecord aemetRecord) throws SQLException, IOException {
        String command = "INSERT INTO aemet (id, date, city, province, max_temp, min_temp, min_temp_time, max_temp_time, precipitation) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        SqlCommand sqlCommand = new SqlCommand(command);
        sqlCommand.addParam(aemetRecord.getId().toString());
        sqlCommand.addParam(aemetRecord.getDate());
        sqlCommand.addParam(aemetRecord.getCity());
        sqlCommand.addParam(aemetRecord.getProvince());
        sqlCommand.addParam(aemetRecord.getMaxTemp());
        sqlCommand.addParam(aemetRecord.getMinTemp());
        sqlCommand.addParam(aemetRecord.getMinTempTime());
        sqlCommand.addParam(aemetRecord.getMaxTempTime());
        sqlCommand.addParam(aemetRecord.getPrecipitation());
        databaseManager.executeUpdate(sqlCommand);
        return aemetRecord;
    }

    /**
     * Funcion para guardar una lista de objetos en la base de datos
     * @param records
     * @throws SQLException
     * @throws IOException
     */
    public void saveAll(List<AemetRecord> records) throws SQLException, IOException {
        StringBuilder sb = new StringBuilder();
        String values = "(?,?,?,?,?,?,?,?,?),";
        sb.append("INSERT INTO aemet VALUES ");
        sb.append(values.repeat(records.size()));
        int lastCommandIndex = sb.lastIndexOf(",");
        sb.replace(lastCommandIndex, lastCommandIndex + 1, ";");
        String command = sb.toString();
        SqlCommand sqlCommand = new SqlCommand(command);
        for (AemetRecord record : records) {
            sqlCommand.addParam(record.getId().toString());
            sqlCommand.addParam(record.getDate());
            sqlCommand.addParam(record.getCity());
            sqlCommand.addParam(record.getProvince());
            sqlCommand.addParam(record.getMaxTemp());
            sqlCommand.addParam(record.getMaxTempTime());
            sqlCommand.addParam(record.getMinTemp());
            sqlCommand.addParam(record.getMinTempTime());
            sqlCommand.addParam(record.getPrecipitation());
        }
        databaseManager.executeUpdate(sqlCommand);
    }

    /**
     * Funcion para buscar un objeto por su id
     * @param uuid
     * @return
     * @throws SQLException
     * @throws IOException
     */
    @Override
    public AemetRecord findById(UUID uuid) throws SQLException, IOException {
        String query = "SELECT * FROM aemet WHERE id = ?";
        SqlCommand sqlCommand = new SqlCommand(query);
        sqlCommand.addParam(uuid.toString());
        try (ResultSet resultSet = databaseManager.executeQuery(sqlCommand)) {
            if (resultSet.next()) {
                return AemetRecord.builder()
                        .id(UUID.fromString(resultSet.getString("id")))
                        .city(resultSet.getString("city"))
                        .province(resultSet.getString("province"))
                        .maxTemp(resultSet.getDouble("max_temp"))
                        .minTemp(resultSet.getDouble("min_temp"))
                        .minTempTime(resultSet.getTime("min_temp_time").toLocalTime())
                        .maxTempTime(resultSet.getTime("max_temp_time").toLocalTime())
                        .precipitation(resultSet.getDouble("precipitation"))
                        .build();
            }
        }
        return null;
    }

    /**
     * Funcion para devolver una lista con todos los objetos de la base de datos
     * @return
     * @throws SQLException
     * @throws IOException
     */
    @Override
    public List<AemetRecord> findAll() throws SQLException, IOException {
        String query = "SELECT * FROM aemet";
        SqlCommand sqlCommand = new SqlCommand(query);
        List<AemetRecord> aemetRecords;
        try (ResultSet resultSet = databaseManager.executeQuery(sqlCommand)) {
            aemetRecords = new ArrayList<>();

            while (resultSet.next()) {
                AemetRecord record = AemetRecord.builder()
                        .id(UUID.fromString(resultSet.getString("id")))
                        .date(resultSet.getDate("date").toLocalDate())
                        .city(resultSet.getString("city"))
                        .province(resultSet.getString("province"))
                        .maxTemp(resultSet.getDouble("max_temp"))
                        .minTemp(resultSet.getDouble("min_temp"))
                        .minTempTime(resultSet.getTime("min_temp_time").toLocalTime())
                        .maxTempTime(resultSet.getTime("max_temp_time").toLocalTime())
                        .precipitation(resultSet.getDouble("precipitation"))
                        .build();
                aemetRecords.add(record);
            }
        }
        return aemetRecords;
    }

    /**
     * Funcion para actualizar un objeto de la base de datos
     * @param aemetRecord
     * @return
     * @throws SQLException
     * @throws IOException
     */
    @Override
    public AemetRecord update(AemetRecord aemetRecord) throws SQLException, IOException {
        String command = "UPDATE aemet SET date = ?, SET city = ?, province = ?, max_temp = ?, min_temp = ?, min_temp_time = ?, max_temp_time = ?, precipitation = ? WHERE id = ?";
        SqlCommand sqlCommand = new SqlCommand(command);
        sqlCommand.addParam(aemetRecord.getDate());
        sqlCommand.addParam(aemetRecord.getCity());
        sqlCommand.addParam(aemetRecord.getProvince());
        sqlCommand.addParam(aemetRecord.getMaxTemp());
        sqlCommand.addParam(aemetRecord.getMinTemp());
        sqlCommand.addParam(aemetRecord.getMinTempTime());
        sqlCommand.addParam(aemetRecord.getMaxTempTime());
        sqlCommand.addParam(aemetRecord.getPrecipitation());
        sqlCommand.addParam(aemetRecord.getId().toString());
        databaseManager.executeUpdate(sqlCommand);
        return aemetRecord;
    }

    /**
     * Funcion para borrar un objeto de la base de datos por su id
     * @param uuid
     * @throws SQLException
     * @throws IOException
     */
    @Override
    public void deleteById(UUID uuid) throws SQLException, IOException {
        String command = "DELETE FROM aemet WHERE id = ?";
        SqlCommand sqlCommand = new SqlCommand(command);
        sqlCommand.addParam(uuid.toString());
        databaseManager.executeUpdate(sqlCommand);
    }

    /**
     * Funcion para buscar un objeto por la provincia
     * @param province
     * @return
     * @throws SQLException
     * @throws IOException
     */
    public List<AemetRecord> findByProvince(String province) throws SQLException, IOException{
        String command = "SELECT * FROM aemet WHERE province = ?";
        SqlCommand sqlCommand = new SqlCommand(command);
        sqlCommand.addParam(province);
        List<AemetRecord> aemetRecords = new ArrayList<>();
        try (ResultSet resultSet = databaseManager.executeQuery(sqlCommand)) {
            while (resultSet.next()) {
                AemetRecord record = AemetRecord.builder()
                        .id(UUID.fromString(resultSet.getString("id")))
                        .date(resultSet.getDate("date").toLocalDate())
                        .city(resultSet.getString("city"))
                        .province(resultSet.getString("province"))
                        .maxTemp(resultSet.getDouble("max_temp"))
                        .minTemp(resultSet.getDouble("min_temp"))
                        .minTempTime(resultSet.getTime("min_temp_time").toLocalTime())
                        .maxTempTime(resultSet.getTime("max_temp_time").toLocalTime())
                        .precipitation(resultSet.getDouble("precipitation"))
                        .build();
                aemetRecords.add(record);
            }
        }
        return aemetRecords;
    }

    /**
     * Funcion para cerrar la conexion con la base de datos
     * @throws Exception
     */
    @Override
    public void close() throws Exception {
        DatabaseManager.getInstance().close();
    }
}
