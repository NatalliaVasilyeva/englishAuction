package main.java.by.epam.javaweb.vasilyevanatali.englishauction.dao;

import main.java.by.epam.javaweb.vasilyevanatali.englishauction.connection.ConnectionPool;
import main.java.by.epam.javaweb.vasilyevanatali.englishauction.connection.ProxyConnection;
import main.java.by.epam.javaweb.vasilyevanatali.englishauction.connection.exception.ConnectionPoolException;
import main.java.by.epam.javaweb.vasilyevanatali.englishauction.dao.exception.DaoException;
import main.java.by.epam.javaweb.vasilyevanatali.englishauction.entity.Bean;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public abstract class AbstractDao<T extends Bean> implements BaseDao<T> {
    private static final Logger LOGGER = LogManager.getLogger(AbstractDao.class);

    protected final ConnectionPool connectionPool = ConnectionPool.getInstance();

    protected abstract void prepareStatementForCreate(PreparedStatement statement, T object) throws SQLException;

    protected void prepareStatementForDelete(PreparedStatement statement, T object) throws SQLException {
        statement.setInt(1, object.getId());
        statement.executeUpdate();
    }

    protected void prepareStatementForDeleteById(PreparedStatement statement, int id) throws SQLException {
        statement.setInt(1, id);
        statement.executeUpdate();
    }

    protected abstract void prepareStatementForUpdate(PreparedStatement statement, T object) throws SQLException;

    protected abstract T prepareStatementForGetById(PreparedStatement statement) throws SQLException, DaoException;

    protected abstract List<T> prepareStatementForGetAll(PreparedStatement statement) throws SQLException, DaoException;

    public abstract String getSelectQueryById();

    public abstract String getSelectAllQuery();

    public abstract String getCreateQuery();

    public abstract String getUpdateQuery();

    public abstract String getDeleteQuery();

    public abstract String getDeleteByIdQuery();

    @Override
    public Optional<T> findById(int id) throws DaoException, SQLException {
        Optional<T> optional = Optional.empty();
        try (ProxyConnection proxyConnection = connectionPool.getConnection();
             PreparedStatement preparedStatement = proxyConnection.prepareStatement(getSelectQueryById())) {
            preparedStatement.setInt(1, id);
            return Optional.of(prepareStatementForGetById(preparedStatement));
        } catch (ConnectionPoolException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<T> findAll() throws SQLException, DaoException {
        try (ProxyConnection proxyConnection = connectionPool.getConnection();
             PreparedStatement preparedStatement = proxyConnection.prepareStatement(getSelectAllQuery())) {
            return prepareStatementForGetAll(preparedStatement);
        } catch (ConnectionPoolException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void create(T object) throws SQLException, DaoException {
        try (ProxyConnection proxyConnection = connectionPool.getConnection();
             PreparedStatement preparedStatement = proxyConnection.prepareStatement(getCreateQuery())) {
            prepareStatementForCreate(preparedStatement, object);
        } catch (ConnectionPoolException e) {
            throw new DaoException(e);
        }
    }


    @Override
    public void deleteById(int id) throws SQLException, DaoException {
        try (ProxyConnection proxyConnection = connectionPool.getConnection();
             PreparedStatement preparedStatement = proxyConnection.prepareStatement(getDeleteByIdQuery())) {
            prepareStatementForDeleteById(preparedStatement, id);
        } catch (ConnectionPoolException e) {
            throw new DaoException(e);
        }

    }

    @Override
    public void delete(T object) throws SQLException, DaoException {
        try (ProxyConnection proxyConnection = connectionPool.getConnection();
             PreparedStatement preparedStatement = proxyConnection.prepareStatement(getDeleteQuery())) {
            prepareStatementForDelete(preparedStatement, object);
        } catch (ConnectionPoolException e) {
            throw new DaoException(e);
        }
    }

    @Override

    public void update(T object) throws SQLException, DaoException {
        try (ProxyConnection proxyConnection = connectionPool.getConnection();
             PreparedStatement preparedStatement = proxyConnection.prepareStatement(getUpdateQuery())) {
            prepareStatementForUpdate(preparedStatement, object);
        } catch (ConnectionPoolException e) {
            throw new DaoException(e);
        }
    }
    public void startTransaction() throws DaoException {
        try {
            connectionPool.getConnection().setAutoCommit(false);
            LOGGER.debug( "start Transaction");
        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException(e);
        }
    }

    public void finishTransaction() throws DaoException {
        try {
            connectionPool.getConnection().setAutoCommit(true);
            LOGGER.debug("finish Transaction");
        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException(e);
        }
    }

    public void rollbackTransaction() throws DaoException {
        try {
            connectionPool.getConnection().rollback();
            LOGGER.debug( "rollback Transaction");
        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException(e);
        }
    }

}
