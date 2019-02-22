package main.java.by.epam.javaweb.vasilyevanatali.englishauction.dao;

import main.java.by.epam.javaweb.vasilyevanatali.englishauction.dao.exception.DaoException;
import main.java.by.epam.javaweb.vasilyevanatali.englishauction.entity.Bean;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface BaseDao<T extends Bean> {
    List<T> findAll() throws SQLException, DaoException;
    Optional<T> findById(int id) throws DaoException, SQLException, InterruptedException;
    void deleteById(int id) throws SQLException, DaoException;
    void delete(T object) throws SQLException, DaoException;
    void create(T object) throws SQLException, DaoException;
    void update(T object) throws SQLException, DaoException;
}
