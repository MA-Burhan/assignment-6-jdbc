package se.lexicon.amin.data;

import se.lexicon.amin.model.City;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CityDaoJDBC implements CityDao {

    private static final String TABLE_CITY = "city";
    private static final String COLUMN_CITY_ID = "id";
    private static final String COLUMN_CITY_NAME = "name";
    private static final String COLUMN_CITY_COUNTRYCODE = "countrycode";
    private static final String COLUMN_CITY_DISTRICT = "district";
    private static final String COLUMN_CITY_POPULATION = "population";

    private static final String QUERY_CITY_TABLE = "SELECT * FROM " + TABLE_CITY + " ";
    private static final String FIND_BY_ID = "WHERE " + COLUMN_CITY_ID + " = ?";
    private static final String FIND_BY_COUNTRY_CODE = "WHERE " + COLUMN_CITY_COUNTRYCODE + " = ?";
    private static final String FIND_BY_NAME = "WHERE " + COLUMN_CITY_NAME + " = ?";

    private static final String ADD = "INSERT INTO " + TABLE_CITY + " (" +
                                    COLUMN_CITY_NAME + ", " +
                                    COLUMN_CITY_COUNTRYCODE + ", " +
                                    COLUMN_CITY_DISTRICT + ", " +
                                    COLUMN_CITY_POPULATION +
                                    ") VALUES(?, ?, ?, ?)";

    private static final String UPDATE = "UPDATE " + TABLE_CITY + " SET " +
                                        COLUMN_CITY_NAME + " = ?, " +
                                        COLUMN_CITY_COUNTRYCODE + " = ?, " +
                                        COLUMN_CITY_DISTRICT + " = ?, " +
                                        COLUMN_CITY_POPULATION + " = ? " +
                                        "WHERE " + COLUMN_CITY_ID + " = ?";

    private static final String DELETE = "DELETE FROM " + TABLE_CITY + " WHERE " + COLUMN_CITY_ID + " = ?";


    private City createCityObject(ResultSet resultSet) throws SQLException {
        return new City(
                resultSet.getInt(COLUMN_CITY_ID),
                resultSet.getString(COLUMN_CITY_NAME),
                resultSet.getString(COLUMN_CITY_COUNTRYCODE),
                resultSet.getString(COLUMN_CITY_DISTRICT),
                resultSet.getInt(COLUMN_CITY_POPULATION)
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
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(QUERY_CITY_TABLE)
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
