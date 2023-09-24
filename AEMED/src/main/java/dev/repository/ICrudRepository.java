package dev.repository;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface ICrudRepository<T, PK> {

        T save(T t) throws SQLException, IOException;

        T findById(PK pk) throws SQLException, IOException;

        List<T> findAll() throws SQLException, IOException;

        T update(T t) throws SQLException, IOException;

        void deleteById(PK pk) throws SQLException, IOException;
}
