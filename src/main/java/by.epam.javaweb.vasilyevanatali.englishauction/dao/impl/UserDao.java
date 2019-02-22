package main.java.by.epam.javaweb.vasilyevanatali.englishauction.dao.impl;


import main.java.by.epam.javaweb.vasilyevanatali.englishauction.connection.ProxyConnection;
import main.java.by.epam.javaweb.vasilyevanatali.englishauction.connection.exception.ConnectionPoolException;
import main.java.by.epam.javaweb.vasilyevanatali.englishauction.dao.AbstractDao;
import main.java.by.epam.javaweb.vasilyevanatali.englishauction.dao.exception.DaoException;
import main.java.by.epam.javaweb.vasilyevanatali.englishauction.entity.Role;
import main.java.by.epam.javaweb.vasilyevanatali.englishauction.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDao extends AbstractDao<User> {
    private static final Logger LOGGER = LogManager.getLogger(UserDao.class);
    private static final UserDao INSTANCE = new UserDao();

    private UserDao() {
    }

    public static UserDao getInstance() {
        return INSTANCE;
    }

    @Override
    protected void prepareStatementForCreate(PreparedStatement statement, User user) throws SQLException {
        statement.setString(1, user.getLogin());
        statement.setString(3, user.getFirstName());
        statement.setString(4, user.getLastName());
        statement.setString(5, user.getEmail());
        statement.setString(6, user.getPhoneNumber());

        statement.executeUpdate();
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, User user) throws SQLException {
        // 0 = ADMINISTRATOR
        statement.setInt(1, user.getRole().ordinal());
        statement.setString(2, user.getLogin());
        statement.setString(3, user.getEmail());
        statement.setString(4, user.getPhoneNumber());
        statement.setInt(5, user.getBalance());
        statement.setInt(6, user.getFrozenMoney());
        // 0 = blocked
        statement.setInt(7, user.getIsBlocked());
        statement.setInt(8, user.getId());
        int rowChangeNumber = statement.executeUpdate();
        LOGGER.debug("User by id:" + user.getId() + (rowChangeNumber == 1 ? " " : " not") + " updated");
    }

    @Override
    protected User prepareStatementForGetById(PreparedStatement statement) throws SQLException, DaoException {
        ResultSet resultSet = statement.executeQuery();
        User user = new User();
        while (resultSet.next()) {
//            user.setId(resultSet.getInt("id"));
//            // 0 = ADMINISTRATOR
//            user.setRole(Role.valueOf(resultSet.getString("role_id")));
//            user.setLogin(resultSet.getString("login"));
//            user.setFirstName(resultSet.getString("first_name"));
//            user.setLastName(resultSet.getString("last_name"));
//            user.setEmail(resultSet.getString("email"));
//            user.setPhoneNumber(resultSet.getString("phone"));
//            user.setBalance(resultSet.getInt("money"));
//            user.setFrozenMoney(resultSet.getInt("frozen_money"));
//            // 1 = blocked
//            user.setIsBlocked(resultSet.getInt("is_blocked"));
            setUserParameters(user, resultSet);
        }
        return user;
    }

    @Override
    protected List<User> prepareStatementForGetAll(PreparedStatement statement) throws SQLException, DaoException {
        List<User> users = new ArrayList<>();
        ResultSet resultSet = statement.executeQuery();
        users = parseResultSet(resultSet, users);
        return users;
    }

    public void updatePassword(int id, String password) throws DaoException {
        LOGGER.debug("Update password for user with id " + id);
        int rowChangeNumber = 0;

        try (ProxyConnection proxyConnection = connectionPool.getConnection();
             PreparedStatement preparedStatement = proxyConnection.prepareStatement(getUpdatePasswordQuery())) {
            preparedStatement.setString(1, password);
            preparedStatement.setInt(2, id);
            rowChangeNumber = preparedStatement.executeUpdate();
            if (rowChangeNumber == 1) {
                LOGGER.debug("Password changed for user with id: " + id);
            } else {
                LOGGER.error("Unable to update password for user with id:" + id);
            }
        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException(e);
        }
    }

    Optional<User> findUserByLoginAndPassword(String login, String password) throws DaoException, SQLException, ConnectionPoolException {
        LOGGER.debug("User DAO looking for user:" + login);
        Optional<User> optionalUser = Optional.empty();
        try (ProxyConnection proxyConnection = connectionPool.getConnection();
             PreparedStatement preparedStatement = proxyConnection.prepareStatement(getSelectQueryByLoginAndPassword())) {
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            LOGGER.trace("Request was sent.");

            optionalUser = Optional.of(parseResultForOneUser(resultSet));
            return optionalUser;
        }
    }

    List<User> findAllWithLimit(int offset, int numberOfRecords) throws DaoException {
        LOGGER.debug("Looking for all users with limit on page");
        List<User> users = new ArrayList<>();
        try (ProxyConnection proxyConnection = connectionPool.getConnection();
             PreparedStatement preparedStatement = proxyConnection.prepareStatement(getSelectQueryWithLimit())) {
            preparedStatement.setInt(1, offset);
            preparedStatement.setInt(2, numberOfRecords);
            ResultSet resultSet = preparedStatement.executeQuery();
            users = parseResultSet(resultSet, users);

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ConnectionPoolException e) {
            e.printStackTrace();
        }
        return users;
    }

    Optional<User> findUserByRole(String role) throws DaoException {
        LOGGER.debug("DAO - find user by role:" + role);
        Optional<User> optionalUser = Optional.empty();
        try (ProxyConnection proxyConnection = connectionPool.getConnection();
             PreparedStatement preparedStatement = proxyConnection.prepareStatement(getSelectQueryByRole())) {
            preparedStatement.setString(1, role);
            ResultSet resultSet = preparedStatement.executeQuery();
            LOGGER.trace("Request was sent.");
            optionalUser = Optional.of(parseResultForOneUser(resultSet));

        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException(e);
        }
        return optionalUser;
    }

    boolean checkLoginAndPassword(String login, String password) throws DaoException {
        try (ProxyConnection proxyConnection = connectionPool.getConnection();
             PreparedStatement prepareStatement = proxyConnection.prepareStatement(getSelectUserIDQueryByLoginPass())) {
            prepareStatement.setString(1, login);
            prepareStatement.setString(2, password);
            ResultSet resultSet = prepareStatement.executeQuery();
            return resultSet.next();
        } catch (ConnectionPoolException | SQLException e) {
            throw new DaoException(e);
        }
    }

    int getNumberOfUsersInStorage() throws DaoException {
        LOGGER.debug("Request for users count");
        Optional<Integer> optionalCount = Optional.empty();
        try (ProxyConnection proxyConnection = connectionPool.getConnection();
             PreparedStatement preparedStatement = proxyConnection.prepareStatement(getSelectCountUsersQuery())) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Integer count = resultSet.getInt(1);
                optionalCount = Optional.of(count);
                LOGGER.debug("Users count: " + count);
            }
        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException(e);
        }
        return optionalCount.orElseThrow(() -> new DaoException("Null users count"));
    }


    @Override
    public String getSelectQueryById() {
        return "SELECT user_id, role_id, login, first_name, last_name, email, phone, money, frozen_money, is_blocked FROM auctionDB.user WHERE user_id = ?;";
    }

    @Override
    public String getSelectAllQuery() {
        return "SELECT user_id, role_id, login, first_name, last_name, email, phone, money, frozen_money, is_blocked FROM auctionDB.user JOIN auctionDB.role ON user.role_id = role.role_id";
    }

    @Override
    public String getCreateQuery() {
        return "INSERT INTO auctionDB.user (role_id, login, hash, first_name, last_name, email, phone, money, frozen_money, is_blocked) VALUES (1, ?, hash(?) ?, ?, ?, ?, 0, 0, 0);";
    }

    @Override
    public String getUpdateQuery() {
        return "UPDATE auctionDB.user SET role_id=?, login =?, first_name=?, last_name=?, email=?, phone=?, money=?, frozen_money=?, is_blocked=? WHERE user_id=?;";
    }

    @Override
    public String getDeleteQuery() {
        return "DELETE FROM auctionDB.user where id_user = ?";
    }

    @Override
    public String getDeleteByIdQuery() {
        return "DELETE FROM auctionDB.user where id_user = ?";
    }

    public String getUpdatePasswordQuery() {
        return "UPDATE auctionDB.user SET hash = ? WHERE id=?";
    }

    public String getSelectQueryByLoginAndPassword() {
        return "SELECT user_id, role_id, login, first_name, last_name, email, phone, money, frozen_money, is_blocked FROM auctionDB.user WHERE login LIKE ? AND hash LIKE ?;";
    }

    public String getSelectQueryWithLimit() {
        return "SELECT user_id, role_id, login, first_name, last_name, email, phone, money, frozen_money, is_blocked FROM auctionDB.user JOIN auctionDB.role ON user.role_id = role.role_id limit ?, ?";
    }

    public String getSelectQueryByRole() {
        return "SELECT user_id, role_id, login, first_name, last_name, email, phone, money, frozen_money, is_blocked FROM auctionDB.user JOIN auctionDB.role ON user.role_id = role.role_id WHERE role_name=?";
    }

    public String getSelectUserIDQueryByLoginPass() {
        return "SELECT user_id FROM auctionDB.user WHERE login=? and password=?";
    }

    public String getSelectCountUsersQuery() {
        return "SELECT COUNT(user_id) FROM auctionDB.user";
    }

    private List<User> parseResultSet(ResultSet resultSet, List<User> users) throws DaoException {
        try {
            while (resultSet.next()) {
                User user = new User();
//                user.setId(resultSet.getInt("id"));
//                user.setRole(Role.valueOf(resultSet.getString("role_id")));
//                user.setLogin(resultSet.getString("login"));
//                user.setFirstName(resultSet.getString("first_name"));
//                user.setLastName(resultSet.getString("last_name"));
//                user.setEmail(resultSet.getString("email"));
//                user.setPhoneNumber(resultSet.getString("phone"));
//                user.setBalance(resultSet.getInt("money"));
//                user.setFrozenMoney(resultSet.getInt("frozen_money"));
//                user.setIsBlocked(resultSet.getInt("is_blocked"));
                setUserParameters(user, resultSet);
                users.add(user);
            }
        } catch (SQLException e) {
            throw new DaoException();
        }
        return users;
    }

    private User parseResultForOneUser(ResultSet resultSet) throws DaoException, SQLException {

        User user = new User();
        while (resultSet.next()) {
//            user.setId(resultSet.getInt("id"));
//            user.setRole(Role.valueOf(resultSet.getString("role_id")));
//            user.setLogin(resultSet.getString("login"));
//            user.setFirstName(resultSet.getString("first_name"));
//            user.setLastName(resultSet.getString("last_name"));
//            user.setEmail(resultSet.getString("email"));
//            user.setPhoneNumber(resultSet.getString("phone"));
//            user.setBalance(resultSet.getInt("money"));
//            user.setFrozenMoney(resultSet.getInt("frozen_money"));
//            user.setIsBlocked(resultSet.getInt("is_blocked"));
            setUserParameters(user, resultSet);
        }
        return user;
    }

    private void setUserParameters(User user, ResultSet resultSet) throws DaoException, SQLException {

        user.setId(resultSet.getInt("user_id"));
        user.setRole(Role.valueOf(resultSet.getString("role_id")));
        user.setLogin(resultSet.getString("login"));
        user.setFirstName(resultSet.getString("first_name"));
        user.setLastName(resultSet.getString("last_name"));
        user.setEmail(resultSet.getString("email"));
        user.setPhoneNumber(resultSet.getString("phone"));
        user.setBalance(resultSet.getInt("money"));
        user.setFrozenMoney(resultSet.getInt("frozen_money"));
        user.setIsBlocked(resultSet.getInt("is_blocked"));

    }
}



