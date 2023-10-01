package dev.repository;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Interfaz que implementa los metodos CRUD
 * @param <T>
 * @param <PK>
 */
public interface ICrudRepository<T, PK> {

        /**
         * Guarda un objeto en la base de datos
         * @param t
         * @return T
         * @throws SQLException
         * @throws IOException
         */
        T save(T t) throws SQLException, IOException;

        /**
         * Busca un objeto por su id
         * @param pk
         * @return T
         * @throws SQLException
         * @throws IOException
         */
        T findById(PK pk) throws SQLException, IOException;

        /**
         * Devuelve todos los objetos de la base de datos
         * @return
         * @throws SQLException
         * @throws IOException
         */
        List<T> findAll() throws SQLException, IOException;

        /**
         * Actualiza un objeto de la base de datos
         * @param t
         * @return T
         * @throws SQLException
         * @throws IOException
         */
        T update(T t) throws SQLException, IOException;

        /**
         * Borra un objeto de la base de datos por su id
         * @param pk
         * @throws SQLException
         * @throws IOException
         */
        void deleteById(PK pk) throws SQLException, IOException;
}
