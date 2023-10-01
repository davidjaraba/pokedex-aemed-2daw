package dev.database;

import dev.database.models.SqlCommand;
import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.*;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Properties;

/**
 * Clase que establece la conexion con la BD y ejecuta consultas
 */
public class DatabaseManager {

    private Connection connection;

    private static DatabaseManager instance;

    private final String username;
    private final String password;
    private final String connectionString;

    /**
     * Funcion para conectar con la BD
     * @throws SQLException
     * @throws IOException
     */
    private void connect() throws SQLException, IOException {
        connection = DriverManager.getConnection(connectionString, username, password);
    }

    /**
     * Funcion para cerrar la conexion
     */
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión");
        }
    }

    private DatabaseManager() throws IOException, SQLException {
        Properties appProps = new Properties();
        appProps.load(ClassLoader.getSystemResourceAsStream("application.properties"));
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
        }
        username = appProps.getProperty("db.username");
        password = appProps.getProperty("db.password");
        String filePath = Paths.get(appProps.getProperty("db.filepath")).toAbsolutePath().toString();
        connectionString = ("jdbc:h2:" + filePath).trim();
        String initFileName = appProps.getProperty("db.initScript");
        InputStream initStream = ClassLoader.getSystemResourceAsStream(initFileName);
        Reader reader = new BufferedReader(new InputStreamReader(initStream));
        connect();
        ScriptRunner sr = new ScriptRunner(connection);
        sr.runScript(reader);
        close();
    }

    /**
     * Funcion para obtener la instancia unica
     * @return
     * @throws SQLException
     * @throws IOException
     */
    public static DatabaseManager getInstance() throws SQLException, IOException {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    /**
     * Funcion que recibe un SQLCommand y lo convierte a un PreparedStatement
     * @param sqlCommand
     * @return
     * @throws SQLException
     */
    private PreparedStatement prepareStatement(SqlCommand sqlCommand) throws SQLException {

        PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand.getCommand());

        for (int i = 0; i < sqlCommand.getParams().size(); i++) {
            preparedStatement.setObject(i + 1, sqlCommand.getParams().get(i));
        }

        return preparedStatement;
    }

    /**
     * Funcion para ejecutar una query a la BD
     * @param sqlCommand
     * @return
     * @throws SQLException
     * @throws IOException
     */
    public ResultSet executeQuery(SqlCommand sqlCommand) throws SQLException, IOException {
        connect();
        PreparedStatement preparedStatement = prepareStatement(sqlCommand);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;

    }

    /**
     * Funcion para ejecutar un update a la BD
     * @param sqlCommand
     * @return
     * @throws SQLException
     * @throws IOException
     */
    public int executeUpdate(SqlCommand sqlCommand) throws SQLException, IOException {
        connect();
        PreparedStatement preparedStatement = prepareStatement(sqlCommand);
        int affectedRows = preparedStatement.executeUpdate();
        return affectedRows;

    }


}
