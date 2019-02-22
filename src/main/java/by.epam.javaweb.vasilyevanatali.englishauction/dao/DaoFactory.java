package main.java.by.epam.javaweb.vasilyevanatali.englishauction.dao;

import main.java.by.epam.javaweb.vasilyevanatali.englishauction.dao.impl.AuctionDao;
import main.java.by.epam.javaweb.vasilyevanatali.englishauction.dao.impl.BidDao;
import main.java.by.epam.javaweb.vasilyevanatali.englishauction.dao.impl.LotDao;
import main.java.by.epam.javaweb.vasilyevanatali.englishauction.dao.impl.UserDao;

public class DaoFactory {

    private final static DaoFactory INSTANCE = new DaoFactory();

    private DaoFactory() {
    }
    private static DaoFactory getInstance(){
        return INSTANCE;
    }

    private static AbstractDao getAuctionDao(){
        return AuctionDao.getInstance();
    }

    private static AbstractDao getBidDao(){
        return BidDao.getInstance();
    }

    private static AbstractDao getLotDao(){
        return LotDao.getInstance();
    }

    private static AbstractDao getUserDao(){
        return UserDao.getInstance();
    }
}
