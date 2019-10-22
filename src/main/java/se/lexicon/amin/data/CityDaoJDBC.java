package se.lexicon.amin.data;

import se.lexicon.amin.model.City;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CityDaoJDBC implements CityDao {

    private static final String QUERY_CITY_TABLE = "SELECT * FROM city ";
    private static final String FIND_BY_ID = "WHERE id = ?";
    private static final String FIND_BY_COUNTRY_CODE = "WHERE countrycode = ?";
    private static final String FIND_BY_NAME = "WHERE name = ?";

    private static final String ADD = "INSERT INTO city (name, countrycode, district, population) VALUES(?, ?, ?, ?)";

    private static final String UPDATE = "UPDATE city SET name = ?, countrycode = ?, district = ?, population = ? " +
            "WHERE person_id = ?";

    private static final String DELETE = "DELETE FROM city WHERE id = ?";


    private City createCityObject(ResultSet resultSet) throws SQLException {
        return new City(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("countrycode"),
                resultSet.getString("district"),
                resultSet.getInt("population")
        );
    }

    private PreparedStatement createFindById(Connection connection, int id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(QUERY_CITY_TABLE + FIND_BY_ID);
        statement.setInt(1, id);
        return statement;
    }

    @Override
    public Optional<City> findById(int id) {

        City result = null;

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = createFindById(connection, id);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                result = createCityObject(resultSet);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (result == null) {
            return Optional.empty();
        } else {
            return Optional.of(result);
        }
    }


    private PreparedStatement createFindByCountryCode(Connection connection, String countryCode) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(QUERY_CITY_TABLE + FIND_BY_COUNTRY_CODE);
        statement.setString(1, countryCode);
        return statement;
    }

    @Override
    public List<City> findByCountryCode(String countryCode) {

        List<City> result = new ArrayList<>();
        try (
                Connection connection = Database.getConnection();
                PreparedStatement statement = createFindByCountryCode(connection, countryCode);
                ResultSet resultSet = statement.executeQuery()
        ) {

            while (resultSet.next()) {
                result.add(createCityObject(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


    private PreparedStatement createFindByName(Connection connection, String name) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(QUERY_CITY_TABLE + FIND_BY_NAME);
        statement.setString(1, name);
        return statement;
    }

    @Override
    public List<City> findByName(String name) {

        List<City> result = new ArrayList<>();


        try (
                Connection connection = Database.getConnection();
                PreparedStatement statement = createFindByName(connection, name);
                ResultSet resultSet = statement.executeQuery()
        ) {

            while (resultSet.next()) {
                result.add(createCityObject(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


    @Override
    public List<City> findAll() {

        List<City> result = new ArrayList<>();
        try (
                Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(QUERY_CITY_TABLE);
                ResultSet resultSet = statement.executeQuery()
        ) {

            while (resultSet.next()) {
                result.add(createCityObject(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


    private PreparedStatement createAdd(Connection connection, City city) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(ADD, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, city.getName());
        statement.setString(2, city.getCountryCode());
        statement.setString(3, city.getDistrict());
        statement.setInt(4, city.getPopulation());
        return statement;
    }

//    @Override
//    public City add(City city) {
//        if (city.getId() != 0) {
//            return city;
//        }
//
//        Connection connection = null;
//        PreparedStatement statement = null;
//        ResultSet resultSet = null;
//
//        try {
//            connection = Database.getConnection();
//            statement = createAdd(connection, city);
//            statement.executeUpdate();
//            resultSet = statement.getGeneratedKeys();
//
//            int cityId = 0;
//            while (resultSet.next()) {
//                cityId = resultSet.getInt(1);
//            }
//
//
//            city = new City(cityId, city.getName(), city.getCountryCode(), city.getDistrict(), city.getPopulation());                      //personId taken from getGeneratedKeys()
//
//
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        } finally {
//            try {
//                if (resultSet != null) {
//                    resultSet.close();
//                }
//                if (statement != null) {
//                    statement.close();
//                }
//                if (connection != null) {
//                    connection.close();
//                }
//            } catch (SQLException ex) {
//                ex.printStackTrace();
//            }
//        }
//
//        return city;
//
//    }

    public City add(City city) {
        if (city.getId() != 0) {
            return city;
        }

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = createAdd(connection, city)
        ) {
            statement.executeUpdate();

            int cityId = 0;

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                while (resultSet.next()) {
                    cityId = resultSet.getInt(1);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            city = new City(cityId, city.getName(), city.getCountryCode(), city.getDistrict(), city.getPopulation());                      //personId taken from getGeneratedKeys()


        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return city;

    }



    private PreparedStatement createUpdate(Connection connection, City city) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(UPDATE);
        statement.setString(1, city.getName());
        statement.setString(2, city.getCountryCode());
        statement.setString(3, city.getDistrict());
        statement.setInt(4, city.getPopulation());
        statement.setInt(5, city.getId());
        return statement;
    }


    @Override
    public City update(City city) {
        if (city.getId() == 0) {
            throw new IllegalArgumentException("City is not yet persisted");
        }
        try (
                Connection connection = Database.getConnection();
                PreparedStatement statement = createUpdate(connection, city)

        ) {

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return city;
    }


    private PreparedStatement createDelete(Connection connection, City city) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(DELETE);
        statement.setInt(1, city.getId());
        return statement;
    }

    @Override
    public int delete(City city) {

        int numRows = 0;
        try (
                Connection connection = Database.getConnection();
                PreparedStatement statement = createDelete(connection, city)) {

            numRows = statement.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return numRows;
    }
}
