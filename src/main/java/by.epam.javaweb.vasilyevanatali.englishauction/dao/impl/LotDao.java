package main.java.by.epam.javaweb.vasilyevanatali.englishauction.dao.impl;

import main.java.by.epam.javaweb.vasilyevanatali.englishauction.connection.ProxyConnection;
import main.java.by.epam.javaweb.vasilyevanatali.englishauction.connection.exception.ConnectionPoolException;
import main.java.by.epam.javaweb.vasilyevanatali.englishauction.dao.AbstractDao;
import main.java.by.epam.javaweb.vasilyevanatali.englishauction.dao.exception.DaoException;
import main.java.by.epam.javaweb.vasilyevanatali.englishauction.entity.Category;
import main.java.by.epam.javaweb.vasilyevanatali.englishauction.entity.Lot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;


public class LotDao extends AbstractDao<Lot> {
    private static final Logger LOGGER = LogManager.getLogger(LotDao.class);

    private static final  LotDao INSTANCE = new LotDao();

    private LotDao() {
    }

    public static LotDao getInstance(){
        return  INSTANCE;
    }

    @Override
    protected void prepareStatementForCreate(PreparedStatement statement, Lot lot) throws SQLException {
        commonData(statement, lot);
        statement.executeUpdate();
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Lot lot) throws SQLException {
        commonData(statement, lot);
        statement.setBoolean(8, lot.isPaid());
        statement.setInt(9, lot.getIsBlocked());
        int rowChangeNumber = statement.executeUpdate();
        LOGGER.debug("Lot by id:" + lot.getId() + (rowChangeNumber == 1 ? " " : " not") + " updated");
    }

    @Override
    protected Lot prepareStatementForGetById(PreparedStatement statement) throws SQLException, DaoException {
        ResultSet resultSet = statement.executeQuery();
        Lot lot = new Lot();
        while (resultSet.next()) {
            setLotParameters(lot, resultSet);
        }
        return lot;
    }

    @Override
    protected List<Lot> prepareStatementForGetAll(PreparedStatement statement) throws SQLException, DaoException {
        List<Lot> lots = new ArrayList<>();
        ResultSet resultSet = statement.executeQuery();
        lots = parseResultSet(resultSet, lots);
        return lots;
    }

    public void unApproveLot(int lotId) throws DaoException {
        LOGGER.debug("Unapproved lot:" + lotId);
        try (ProxyConnection proxyConnection = connectionPool.getConnection();
             PreparedStatement preparedStatement = proxyConnection.prepareStatement(getUnapproveLotQuery())) {
            preparedStatement.setInt(1, lotId); // `id`
            preparedStatement.executeUpdate();
        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException(e);
        }
        LOGGER.debug("Lot " + lotId + "was unapproved");

    }


    public void approveLot(int lotId) throws DaoException {
        LOGGER.debug("Unapproved lot:" + lotId);
        try (ProxyConnection proxyConnection = connectionPool.getConnection();
             PreparedStatement preparedStatement = proxyConnection.prepareStatement(getApproveLotQuery())) {
            preparedStatement.setInt(1, lotId); // `id`
            preparedStatement.executeUpdate();
        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException(e);
        }
        LOGGER.debug("Lot " + lotId + "was approved");

    }

    public List<Lot> takeProposedLotSet() throws DaoException {
        LOGGER.debug("Send request for proposed lot set.");
        List<Lot> setLots = new ArrayList<>();
        try (ProxyConnection proxyConnection = connectionPool.getConnection();
             PreparedStatement preparedStatement = proxyConnection.prepareStatement(getProposedLotSetQuery())) {
            ResultSet resultSet = preparedStatement.executeQuery();
            setLots = parseResultSet(resultSet, setLots);
        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException(e);
        }
        return setLots;
    }

    public String takeCategoryNameById(int categoryId) throws DaoException {
        String name = null;
        try (ProxyConnection proxyConnection = connectionPool.getConnection();
             PreparedStatement preparedStatement = proxyConnection.prepareStatement(getCategoryNameQuery())) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                name = resultSet.getString("category_name");
            }
        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException(e);
        }
        return name;
    }

    public List<Lot> takeUnfinishedLotList() throws DaoException {
        List<Lot> lots = new ArrayList<>();
        try (ProxyConnection proxyConnection = connectionPool.getConnection();
             PreparedStatement preparedStatement = proxyConnection.prepareStatement(getUnfinishedLotQuery())) {
            ResultSet resultSet = preparedStatement.executeQuery();
            lots = setLotParametersForTakeLotsList(lots, resultSet);
        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException(e);
        }
        return lots;
    }

    public List<Lot> takeLotsListBySellerId(int sellerId) throws DaoException {
        List<Lot> lots = new ArrayList<>();
        try (ProxyConnection proxyConnection = connectionPool.getConnection();
             PreparedStatement preparedStatement = proxyConnection.prepareStatement(getAllLotsBySellerIdQuery())) {
            ResultSet resultSet = preparedStatement.executeQuery();
            lots = setLotParametersForTakeLotsList(lots, resultSet);
        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException(e);
        }
        return lots;
    }

    public List<Lot> takeAllLotsLimit(int startIndex, int quantity) throws DaoException, SQLException, ConnectionPoolException {
        List<Lot> setLots = new ArrayList<>();

        try (ProxyConnection proxyConnection = connectionPool.getConnection();
             PreparedStatement preparedStatement = proxyConnection.prepareStatement(getSelectAllQueryLimit())) {
            preparedStatement.setInt(1, startIndex);
            preparedStatement.setInt(2, quantity);
            ResultSet resultSet = preparedStatement.executeQuery();

            setLots = parseResultSet(resultSet, setLots);
        }
        return setLots;
    }

    @Override
    public String getSelectQueryById() {
        return "SELECT lot_id, lot_name, description, lot_is_approved, lot_is_paid, category_name, auction_id, auction_start_time, auction_finish_time,  seller_id, start_price, min_step"
                + "     seller_email, seller_login, seller_is_blocked, bid_price, bid_id, bidder_id"
                + " FROM (SELECT LOT.id AS lot_id, LOT.name AS lot_name, LOT.description AS description,LOT.is_blocked AS lot_is_approved, LOT.is_paid AS lot_is_paid,"
                + "            LOT.category_name AS category_name, AUCTION.start_time AS auction_start_time, AUCTION.finish_time AS auction_finish_time,"
                + "            LOT.user_id as seller_id, LOT.start_price as start_price, "
                + "            SELLER.email AS seller_email, SELLER.login AS seller_login, SELLER.is_blocked AS seller_is_blocked, BID.bid_amount AS bid_price,"
                + "            BID.bid_id AS bid_id, BID.user_id AS bidder_id  FROM  auctionDB.lot LOT"
                + "    JOIN auctionDB.auction AUCTION ON LOT.auction_id = AUCTION.auction_id"
                + "    JOIN auctionDB.user SELLER ON LOT.user_id = SELLER.user_id"
                + "     JOIN auctionDB.category CATEGORY on LOT.category_id = CATEGORY.category_id"
                + "    JOIN auctionDB.bid BID ON LOT.lot_id = BID.lot_id WHERE LOT.lot_id = ? ORDER BY (CASE WHEN auction_type = 0 THEN bid_price END) DESC , "
                + "    (CASE WHEN auction_type = 1 THEN bid_price END) ASC LIMIT 1) as LOTT";
    }


    @Override
    public String getSelectAllQuery() {
        return "SELECT lot_id, lot_name, description, lot_is_approved, lot_is_paid, category_name, auction_id, auction_start_time, auction_finish_time,  seller_id, start_price, min_step"
                + "     seller_email, seller_login, seller_is_blocked, bid_price, bid_id, bidder_id"
                + " FROM (SELECT LOT.id AS lot_id, LOT.name AS lot_name, LOT.description AS description,LOT.is_blocked AS lot_is_approved, LOT.is_paid AS lot_is_paid,"
                + "            LOT.category_name AS category_name, AUCTION.start_time AS auction_start_time, AUCTION.finish_time AS auction_finish_time,"
                + "            LOT.user_id as seller_id, LOT.start_price as start_price, "
                + "            SELLER.email AS seller_email, SELLER.login AS seller_login, SELLER.is_blocked AS seller_is_blocked, BID.bid_amount AS bid_price,"
                + "            BID.bid_id AS bid_id, BID.user_id AS bidder_id  FROM  auctionDB.lot LOT"
                + "    JOIN auctionDB.auction AUCTION ON LOT.auction_id = AUCTION.auction_id"
                + "    JOIN auctionDB.user SELLER ON LOT.user_id = SELLER.user_id"
                + "     JOIN auctionDB.category CATEGORY on LOT.category_id = CATEGORY.category_id"
                + "    JOIN auctionDB.bid BID ON LOT.lot_id = BID.lot_id ORDER BY (CASE WHEN auction_type = 0 THEN bid_price END) DESC , "
                + "    (CASE WHEN auction_type = 1 THEN bid_price END) ASC LIMIT 1) as LOTT";
    }

    @Override
    public String getCreateQuery() {
        return "INSERT INTO auctionDB.lot(lot_name, description, category_id, auction_id, user_id, start_price, min_step, is_paid, is_blocked) VALUES (?, ?, ?, ?, ?, ?, ?, 0, 0);";
    }

    @Override
    public String getUpdateQuery() {
        return "UPDATE auctionDB.lot SET lot_name=?, description=?, category_id=?, auction_id=?, user_id=?, start_price=?, min_step=?, is_paid=?, is_blocked=? WHERE `lot_id`=?";
    }

    @Override
    public String getDeleteQuery() {
        return "DELETE FROM auctionDB.lot WHERE lot_id=?";
    }

    @Override
    public String getDeleteByIdQuery() {
        return "DELETE FROM auctionDB.lot WHERE lot_id=?";
    }

    public String getUnapproveLotQuery() {
        return "UPDATE auctionDB.lot SET is_blocked=0  WHERE lot.id= ?";
    }

    public String getApproveLotQuery() {
        return "UPDATE auctionDB.lot SET is_blocked=1  WHERE lot.id= ?";
    }

    public String getProposedLotSetQuery() {
        return "SELECT lot_id, lot_name, description, category_id, auction_id, user_id as seller_id, start_price, min_step, is_paid, is_blocked FROM auctionDB.lot  WHERE lot.is_blocked = 0;";
    }

    public String getCategoryNameQuery() {
        return "SELECT category_name FROM auctionDB.category WHERE category_id = ? ";
    }

    public String getUnfinishedLotQuery() {
        return "SELECT LOT.lot_id, LOT.lot_name, LOT.description, LOT.is_blocked, LOT.is_paid, CATEGORY.category_name AS category, AUCTION.start_time, AUCTION.finish_time, LOT.user_id, LOT.auction_id "
                + "FROM auctionDB.lot LOT JOIN auctionDB.category CATEGORY ON LOT.category_id = CATEGORY.category_id"
                + "    JOIN auctionDB.auction AUCTION ON LOT.auction_id = AUCTION.auction_id"
                + "    WHERE LOT.is_blocked=1 AND finish_time > now()";
    }

    public String getAllLotsBySellerIdQuery() {
        return "SELECT LOT.lot_id, LOT.lot_name, LOT.description, LOT.is_blocked, LOT.is_paid, CATEGORY.category_name AS category, AUCTION.start_time, AUCTION.finish_time, LOT.user_id, LOT.auction_id "
                + "FROM auctionDB.lot LOT JOIN auctionDB.category CATEGORY ON LOT.category_id = CATEGORY.category_id"
                + "    JOIN auctionDB.auction AUCTION ON LOT.auction_id = AUCTION.auction_id"
                + "    WHERE LOT.user_id=?";
    }

public String getSelectAllQueryLimit(){
        return "Select lot_id, lot_name, lot_description, lot_is_approved, lot_is_paid, auction_start_time, auction_finish_time, seller_id, seller_email, seller_login, seller_is_blocked, category_name" +
                " FROM( SELECT LOT.lot_id AS lot_id, LOT.lot_name AS lot_name, LOT.description AS lot_description, LOT.is_blocked AS lot_is_approved, " +
                "LOT.is_paid AS lot_is_paid, AUCTION.start_time AS auction_start_time, " +
                "AUCTION.finish_time AS auction_finish_time, LOT.user_id AS seller_id, SELLER.email AS seller_email, SELLER.login AS seller_login, SELLER.is_blocked AS seller_is_blocked," +
                " LOT.category_id AS category_name, CASE WHEN Auction.auction_type = 0 THEN MAX(BID.bid_amount) ELSE MIN(BID.bid_amount) END AS bid_price, " +
                "BID.bid_id AS bid_id, BID.user_id AS bidder_id FROM auctionDB.lot LOT JOIN auctionDB.auction AUCTION ON LOT.auction_id = AUCTION.auction_id JOIN auctionDB.user SELLER ON LOT.user_id = SELLER.user_id " +
                "JOIN auctionDB.bid BID ON LOT.lot_id = BID.lot_id GROUP BY lot_id LIMIT ?,? )";
}
    private void commonData(PreparedStatement statement, Lot lot) throws SQLException {
        statement.setString(1, lot.getName());
        statement.setString(2, lot.getDescription());
        statement.setInt(3, lot.getCategory().ordinal());
        statement.setInt(4, lot.getAuctionId());
        statement.setInt(5, lot.getSellerId());
        statement.setInt(6, lot.getStartPrice());
        statement.setInt(7, lot.getMinimumStep());
    }

    private void setLotParameters(Lot lot, ResultSet resultSet) throws SQLException {

        lot.setId(resultSet.getInt("lot_id"));
        lot.setName(resultSet.getString("lot_name"));
        lot.setDescription(resultSet.getString("description"));
        lot.setCategory(Category.valueOf(resultSet.getString("category_name")));
        lot.setAuctionId(resultSet.getInt("auction_id"));
        lot.setSellerId(resultSet.getInt("seller_id"));
        lot.setStartPrice(resultSet.getInt("start_price"));
        lot.setMinimumStep(resultSet.getInt("min_step"));
        lot.setPaid(resultSet.getBoolean("lot_is_paid"));
        lot.setIsBlocked(resultSet.getInt("lot_is_approved"));
        lot.setStartTime((LocalDateTime.ofInstant(resultSet.getTimestamp("auction_start_time").toInstant(), ZoneId.systemDefault())));
        lot.setFinishTime((LocalDateTime.ofInstant(resultSet.getTimestamp("auction_finish_time").toInstant(), ZoneId.systemDefault())));
        lot.setSellerEmail(resultSet.getString("seller_email"));
        lot.setSellerEmail(resultSet.getString("seller_login"));
        lot.setSellerIsBlocked(resultSet.getInt("seller_is_blocked"));
        lot.setBidPrice(resultSet.getInt("bid_price"));
        lot.setBidId(resultSet.getInt("bid_id"));
        lot.setBidderId(resultSet.getInt("bidder_id"));

    }

    private List<Lot> setLotParametersForTakeLotsList(List<Lot> lots, ResultSet resultSet) throws  SQLException {
        while (resultSet.next()) {
            Lot lot = new Lot();
            lot.setId(resultSet.getInt("lot_id"));
            lot.setName(resultSet.getString("lot_name"));
            lot.setDescription(resultSet.getString("description"));
            lot.setIsBlocked(resultSet.getInt("is_blocked"));
            lot.setPaid(resultSet.getBoolean("is_paid"));
            lot.setCategory(Category.valueOf(resultSet.getString("category-name")));
            lot.setStartTime((LocalDateTime.ofInstant(resultSet.getTimestamp("auction_start_time").toInstant(), ZoneId.systemDefault())));
            lot.setFinishTime((LocalDateTime.ofInstant(resultSet.getTimestamp("auction_finish_time").toInstant(), ZoneId.systemDefault())));
            lot.setSellerId(resultSet.getInt("seller_id"));
            lot.setAuctionId(resultSet.getInt("auction_id"));
            lots.add(lot);
        }
        return lots;
    }

    private List<Lot> parseResultSet(ResultSet resultSet, List<Lot> lots) throws DaoException {
        try {
            while (resultSet.next()) {
                Lot lot = new Lot();
                setLotParameters(lot, resultSet);
                lots.add(lot);
            }
        } catch (SQLException e) {
            throw new DaoException();
        }
        return lots;
    }
}
