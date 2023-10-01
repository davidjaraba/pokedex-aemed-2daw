package dev.db;

import dev.models.SqlCommand;
import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.*;
import java.sql.*;
import java.util.Properties;

/**
 * Clase que se encarga de la conexion a la base de datos
 */
public class DatabaseManager {

    private Connection connection;

    private static DatabaseManager instance;


    /**
     * Método que se encarga de abrir una conexión a la base de datos y ejecutar el script de inicialización
     *
     * @throws SQLException Si hay un error en la conexión
     * @throws IOException  Si hay un error al leer el archivo de propiedades
     */
    public void connect() throws SQLException, IOException {

        Properties appProps = new Properties();
        appProps.load(ClassLoader.getSystemResourceAsStream("application.properties"));

        String username = appProps.getProperty("db.username");
        String password = appProps.getProperty("db.password");
        String name = appProps.getProperty("db.name");
        String url = "jdbc:h2:mem:" + name + ";DB_CLOSE_DELAY=-1";

        connection = DriverManager.getConnection(url, username, password);

        System.out.println("Ejecutando SQL");

        InputStream initStream = ClassLoader.getSystemResourceAsStream(appProps.getProperty("db.initScript"));
        Reader reader = new BufferedReader(new InputStreamReader(initStream));
        ScriptRunner sr = new ScriptRunner(connection);
        sr.runScript(reader);

    }

    private DatabaseManager() throws IOException, SQLException {

        connect();

    }

    /**
     * Método que se encarga de obtener la instancia de la clase
     *
     * @return La instancia de la clase
     * @throws SQLException Si hay un error en la conexión
     * @throws IOException  Si hay un error al leer el archivo de propiedades
     */
    public static DatabaseManager getInstance() throws SQLException, IOException {

        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;

    }

    private PreparedStatement prepareStatement(SqlCommand sqlCommand) throws SQLException {

        PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand.getCommand());

        for (int i = 0; i < sqlCommand.getParams().size(); i++) {
            preparedStatement.setObject(i + 1, sqlCommand.getParams().get(i));
        }

        return preparedStatement;
    }

    /**
     * Método que se encarga de ejecutar una consulta a la base de datos
     *
     * @param sqlCommand El comando a ejecutar con sus parámetros
     * @return El resultado de la consulta
     * @throws SQLException Si hay un error en la conexión
     */
    public ResultSet executeQuery(SqlCommand sqlCommand) throws SQLException {

        PreparedStatement preparedStatement = prepareStatement(sqlCommand);

        return preparedStatement.executeQuery();

    }

    /**
     * Método que se encarga de ejecutar una actualización a la base de datos
     *
     * @param sqlCommand El comando a ejecutar con sus parámetros
     * @return El número de filas afectadas
     * @throws SQLException Si hay un error en la conexión
     */
    public int executeUpdate(SqlCommand sqlCommand) throws SQLException {

        PreparedStatement preparedStatement = prepareStatement(sqlCommand);

        return preparedStatement.executeUpdate();

    }


}
