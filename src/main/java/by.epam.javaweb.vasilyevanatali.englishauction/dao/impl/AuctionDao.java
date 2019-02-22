package main.java.by.epam.javaweb.vasilyevanatali.englishauction.dao.impl;

import main.java.by.epam.javaweb.vasilyevanatali.englishauction.connection.ProxyConnection;
import main.java.by.epam.javaweb.vasilyevanatali.englishauction.connection.exception.ConnectionPoolException;
import main.java.by.epam.javaweb.vasilyevanatali.englishauction.dao.AbstractDao;
import main.java.by.epam.javaweb.vasilyevanatali.englishauction.dao.exception.DaoException;
import main.java.by.epam.javaweb.vasilyevanatali.englishauction.entity.Auction;
import main.java.by.epam.javaweb.vasilyevanatali.englishauction.entity.AuctionType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AuctionDao extends AbstractDao<Auction> {
    private static final Logger LOGGER = LogManager.getLogger(AuctionDao.class);
    private static final  AuctionDao INSTANCE = new AuctionDao();

    private AuctionDao() {
    }

    public static AuctionDao getInstance(){
        return  INSTANCE;
    }

    @Override
    protected void prepareStatementForCreate(PreparedStatement statement, Auction auction) throws SQLException {
        statement.setTimestamp(1, Timestamp.valueOf(auction.getStartTime()));
        statement.setTimestamp(2, Timestamp.valueOf(auction.getFinishTime()));
        statement.setString(3, auction.getAuctionType().getName());
        statement.setString(4, auction.getDescription());
        statement.executeUpdate();
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Auction auction) throws SQLException {

    }

    @Override
    protected Auction prepareStatementForGetById(PreparedStatement statement) throws SQLException, DaoException {
        ResultSet resultSet = statement.executeQuery();
        Auction auction = new Auction();
        while (resultSet.next()) {
            setAuctionParameters(auction, resultSet);
        }
        return auction;
    }

    @Override
    protected List<Auction> prepareStatementForGetAll(PreparedStatement statement) throws SQLException, DaoException {
        List<Auction> auctions = new ArrayList<>();
        ResultSet resultSet = statement.executeQuery();
        auctions = parseResultSet(resultSet, auctions);
        return auctions;
    }

    int getNumberOfUsersInStorage() throws DaoException {
        LOGGER.debug( "Request for active auctions count");
        Optional<Integer> optionalCount = Optional.empty();
        try (ProxyConnection proxyConnection = connectionPool.getConnection();
             PreparedStatement preparedStatement = proxyConnection.prepareStatement(getSelectCountAuctionsQuery())) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Integer count = resultSet.getInt(1);
                optionalCount = Optional.of(count);
                LOGGER.debug("Auctions count: " + count);
            }
        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException(e);
        }
        return optionalCount.orElseThrow(() -> new DaoException("Null auction count"));
    }


    @Override
    public String getSelectQueryById() {
        return "SELECT id, start_time, finish_time, auction_type, description FROM auctionDB.auction WHERE auction.id = ?;";
    }

    @Override
    public String getSelectAllQuery() {
        return "SELECT auction_id, start_time, finish_time, auction_type, description FROM auctionDB.auction WHERE finish_time > now();";
    }

    @Override
    public String getCreateQuery() {
        return "INSERT INTO auctionDB.auction (start_time, finish_time, auction_type, description) VALUES (?, ?, ?, ?);";
    }

    @Override
    public String getUpdateQuery() {
        return null;
    }

    @Override
    public String getDeleteQuery() {
        return null;
    }

    @Override
    public String getDeleteByIdQuery() {
        return null;
    }

    public String getSelectCountAuctionsQuery() {
        return "SELECT COUNT(auction_id) FROM auctionDB.auction WHERE finish_time > now();";
    }

    private void setAuctionParameters(Auction auction, ResultSet resultSet) throws DaoException, SQLException {

        auction.setId(resultSet.getInt("auction_id"));
        auction.setStartTime(LocalDateTime.ofInstant(resultSet.getTimestamp("start_time").toInstant(),
                ZoneId.systemDefault()));
        auction.setFinishTime(LocalDateTime.ofInstant(resultSet.getTimestamp("finish_time").toInstant(),
                ZoneId.systemDefault()));
        auction.setAuctionType(AuctionType.values()[resultSet.getInt("auction_type")]);
        auction.setDescription(resultSet.getString("description"));
    }

    private List<Auction> parseResultSet(ResultSet resultSet, List<Auction> auctions) throws DaoException {
        try {
            while (resultSet.next()) {
                Auction auction = new Auction();
                setAuctionParameters(auction, resultSet);
                auctions.add(auction);
            }
        } catch (SQLException e) {
            throw new DaoException();
        }
        return auctions;
    }
}
