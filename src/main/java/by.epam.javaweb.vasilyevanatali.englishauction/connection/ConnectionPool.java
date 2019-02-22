package main.java.by.epam.javaweb.vasilyevanatali.englishauction.connection;


import main.java.by.epam.javaweb.vasilyevanatali.englishauction.connection.exception.ConnectionPoolException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;


public class ConnectionPool {

    private static final Logger LOGGER = LogManager.getLogger(ConnectionPool.class);
    private final int POOL_SIZE = ConnectionCreator.getPollSize();
    private ArrayBlockingQueue<ProxyConnection> freeConnections;
    private ArrayBlockingQueue<ProxyConnection> releaseConnections;
    private static ReentrantLock lock = new ReentrantLock();
    private static ConnectionPool instance;

    public static ConnectionPool getInstance(){
        if(instance == null){
            try {
                lock.lock();
                if(instance == null){
                    instance = new ConnectionPool();
                }
            } finally {
                lock.unlock();
            }
        }
        return instance;
    }

    private ConnectionPool()  {
        try {
            lock.lock();
            if(instance != null){
                throw new UnsupportedOperationException();
            }
            else {
               init();
            }
        }
        finally {
            lock.unlock();
        }
    }

    private void init() {
        freeConnections = new ArrayBlockingQueue<>(POOL_SIZE);
        releaseConnections = new ArrayBlockingQueue<>(POOL_SIZE);

        for (int i = 0; i < POOL_SIZE; i++) {
            try {
                ProxyConnection connection = new ProxyConnection(ConnectionCreator.createConnection());
                freeConnections.offer(connection);
            } catch (MissingResourceException e) {
                LOGGER.fatal("Exception during database initialization", e);
                throw new RuntimeException("Exception during database initialization", e);
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException("Driver is not found" + e.getMessage(), e);
            } catch (SQLException e) {
                try {
                    throw new ConnectionPoolException("Exception with create connection", e);
                } catch (ConnectionPoolException e1) {
                    e1.printStackTrace();
                }
            }

        }

    }

    public ProxyConnection getConnection() throws ConnectionPoolException {
        ProxyConnection connection = null;
        try {
            connection = freeConnections.take();
            releaseConnections.offer(connection);
        } catch (InterruptedException e) {
            LOGGER.error("Error getting connection ", e);
            throw new ConnectionPoolException("Problem with take connection from pool, e");

        }
        return connection;
    }


    public void closeConnection(ProxyConnection proxyConnection) {
        releaseConnections.remove(proxyConnection);
        if (freeConnections.offer(proxyConnection)){
            LOGGER.error("Connection successfully returned: ", proxyConnection.toString());
        }
    }

    public void closeConnectionsQueue(List<ProxyConnection> queue) throws SQLException {
        Iterator<ProxyConnection> iterator = queue.iterator();
        while (iterator.hasNext()) {
            ProxyConnection proxyConnection = iterator.next();
            if (!proxyConnection.getAutoCommit()) {
                proxyConnection.commit();
            }
            proxyConnection.realClose();
        }
    }

    public void destroyConnectionPool() {
        for (ProxyConnection proxyConnection:freeConnections) {
            proxyConnection.realClose();
        }
        for (ProxyConnection proxyConnection:releaseConnections) {
            proxyConnection.realClose();
        }

    }

}

