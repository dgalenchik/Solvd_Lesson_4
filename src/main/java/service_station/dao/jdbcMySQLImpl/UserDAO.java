package service_station.dao.jdbcMySQLImpl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service_station.dao.IUserDAO;
import service_station.dao.connectionPool.ConnectionPool;
import service_station.models.User;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class UserDAO implements IUserDAO {
    private static final Logger LOGGER = LogManager.getLogger(UserDAO.class);
    private static Properties p = new Properties();
    private User user = new User();
    private ConnectionPool connectionPool = ConnectionPool.getInstance();
    private Connection connection;
    private PreparedStatement pr = null;
    private ResultSet resultSet = null;
    private static String userName;
    private static String url;
    private static String password;

    static {
        try (FileInputStream f = new FileInputStream("src/main/resources/db.properties")) {
            p.load(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
        url = p.getProperty("db.url");
        userName = p.getProperty("db.username");
        password = p.getProperty("db.password");
    }

    @Override
    public User getEntityById(int id) {
        try {
            connection = connectionPool.retrieve();
            pr = connection.prepareStatement("Select * From Users where id=?");
            pr.setInt(1, id);
            pr.execute();
            resultSet = pr.getResultSet();
            while (resultSet.next()) {
                user.setId(resultSet.getInt("id"));
                user.setName(resultSet.getString("name"));
                user.setEmail(resultSet.getString("email"));
                user.setSurname(resultSet.getString("surname"));
            }

        } catch (SQLException e) {
            LOGGER.info(e);
        } finally {
            try {
                if (connection != null) connectionPool.putback(connection);
                if (pr != null) pr.close();
                if (resultSet != null) resultSet.close();
            } catch (SQLException e) {
                LOGGER.info(e);
            }
        }
        return user;
    }

    @Override
    public void saveEntity(User entity) {
        try {
            connection = connectionPool.retrieve();
            pr = connection.prepareStatement("Insert into users (name,surname,email) Values (?,?,?)");
            pr.setString(1, entity.getName());
            pr.setString(2, entity.getSurname());
            pr.setString(3, entity.getEmail());
            pr.executeUpdate();
        } catch (SQLException e) {
            LOGGER.info(e);
        } finally {
            try {
                if (connection != null) connectionPool.putback(connection);
                if (pr != null) pr.close();
            } catch (SQLException e) {
                LOGGER.info(e);
            }
        }
    }

    @Override
    public void updateEntity(User entity) {
        try {
            connection = connectionPool.retrieve();
            pr = connection.prepareStatement("Update users Set name=?,`surname`=?,`email`=? where id=?");
            pr.setString(1, entity.getName());
            pr.setString(2, entity.getSurname());
            pr.setString(3, entity.getEmail());
            pr.setInt(4, entity.getId());
            pr.executeUpdate();
        } catch (SQLException e) {
            LOGGER.info(e);
        } finally {
            try {
                if (connection != null) connectionPool.putback(connection);
                if (pr != null) pr.close();
            } catch (SQLException e) {
                LOGGER.info(e);
            }
        }
    }


    @Override
    public void removeEntity(User entity) {
        try {
            connection = connectionPool.retrieve();
            pr = connection.prepareStatement("Delete from users where id=?");
            pr.setInt(1, entity.getId());
            pr.execute();
        } catch (SQLException e) {
            LOGGER.info(e);
        } finally {
            try {
                if (connection != null) connectionPool.putback(connection);
                if (pr != null) pr.close();
            } catch (SQLException e) {
                LOGGER.info(e);
            }
        }
    }

    @Override
    public void generateUsers(String name, String surname, String email, int quantity) {
        try {
            connection = connectionPool.retrieve();
            pr = connection.prepareStatement("Insert into users (name,surname,email) Values (?,?,?)");
            for (int i = 0; i < quantity; i++) {
                pr.setString(1, name + "_" + i);
                pr.setString(2, surname + "_" + i);
                pr.setString(3, email + "_" + i);
                pr.executeUpdate();
            }
        } catch (SQLException e) {
            LOGGER.info(e);
        } finally {
            try {
                if (connection != null) connectionPool.putback(connection);
                if (pr != null) pr.close();
            } catch (SQLException e) {
                LOGGER.info(e);
            }
        }
    }

    @Override
    public void showAll() {
        try {
            connection = connectionPool.retrieve();
            pr = connection.prepareStatement("Select * From Users");
            pr.execute();
            resultSet = pr.getResultSet();
            while (resultSet.next()) {
                user.setId(resultSet.getInt("id"));
                user.setName(resultSet.getString("name"));
                user.setEmail(resultSet.getString("email"));
                user.setSurname(resultSet.getString("surname"));
                LOGGER.info(user);
            }

        } catch (SQLException e) {
            LOGGER.info(e);
        } finally {
            try {
                if (connection != null) connectionPool.putback(connection);
                if (pr != null) pr.close();
                if (resultSet != null) resultSet.close();
            } catch (SQLException e) {
                LOGGER.info(e);
            }
        }
    }
}
