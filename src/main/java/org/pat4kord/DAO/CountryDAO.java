package org.pat4kord.DAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("CountryDAO")
public class CountryDAO implements ICountryDAO{
    private static Logger logger = LoggerFactory.getLogger(CountryDAO.class);
    private JdbcTemplate jdbcTemplate;
    private String QUERY_FIND_ALL
            = "SELECT " +
            "`code`, `name`, `continent`, `surfaceArea`, `population`, `capital` " +
            "FROM " +
            "`country`";

    private String QUERY_FINDE_BY_CODE
            = "SELECT " +
            "`code`, `name`, `continent`, `surfaceArea`, `population`, `capital` " +
            "FROM " +
            "`country` " +
            "WHERE " +
            "`code` = ?";
    private String QUERY_INSERT
            = "INSERT INTO " +
            "`country` " +
            "(`Code`, `Name`, `Continent`, `Region`, `SurfaceArea`, `Population`)" +
            " VALUES " +
            "(?,?,?,?,?,?);";

    private String QUERY_DELETE
            = "DELETE FROM " +
            "`country` " +
            "WHERE " +
            "(`Code` = ?);";

    private String QUERY_ALL_DETAILED =
            "SELECT \n" +
                    "\t`city`.`ID` as `cityId`,\n" +
                    "    `city`.`Name` as `cityName`,\n" +
                    "    `city`.`CountryCode` as `countryCode`,\n" +
                    "    `city`.`District` as `cityDistrict`,\n" +
                    "    `city`.`Population` as `cityPopulation`,\n" +
                    "    `country`.Name as `countryName`,\n" +
                    "    `country`.`Continent` as `continent`,\n" +
                    "    `country`.`SurfaceArea` as `surfaceArea`,\n" +
                    "    `country`.`Population` as `countryPopulation`,\n" +
                    "    `country`.`Capital` as `capital`\n" +
                    "FROM \n" +
                    "\t`world`.`city`\n" +
                    "\tLEFT JOIN `world`.`country` ON `city`.`CountryCode` = `country`.`Code`";

    static {
        try{
            logger.info("Checking for driver availability");
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (ClassNotFoundException exception) {
            logger.error("Problem loading DB dDriver", exception);
        }
    }
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Connection getConnection() throws SQLException {
        logger.info("Try get connection");
        return jdbcTemplate.getDataSource().getConnection();

        /* Вариант через DriverManager
         return DriverManager.getConnection("jdbc:mysql://localhost:3306/world?useSSL=true",
                "root", "2COf4Dtt_3");
        */
    }

    private void closeConnection(Connection connection){
        logger.info("Try close connection");
        if (connection == null){
            return;
        }

        try {
            connection.close();
        }catch (SQLException exception){
            logger.error("Problem closing connection to the database!", exception);
        }
    }

    private static final class CountryMapper implements RowMapper<Country> {
        public Country mapRow(ResultSet resultSet, int i) throws SQLException {
            Country country = new Country();

            country.setCode(resultSet.getString("code"));
            country.setName(resultSet.getString("name"));
            country.setContinent(resultSet.getString("continent"));
            country.setSurfaceArea(resultSet.getFloat("surfaceArea"));
            country.setPopulation(resultSet.getInt("population"));
            country.setCapital(resultSet.getInt("capital"));

            return country;
        }
    }

    private static final class CountryDetailExtractor implements ResultSetExtractor<List<Country>> {
        public List<Country> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
            Map<String, Country> resultMap = new HashMap<String, Country>();

            Country country;
            while (resultSet.next()) {
                String countryCode = resultSet.getString("countryCode");
                country = resultMap.get(countryCode);

                if (country == null){
                    country = new Country();
                    country.setCode(resultSet.getString("countryCode"));
                    country.setName(resultSet.getString("countryName"));
                    country.setContinent(resultSet.getString("continent"));
                    country.setSurfaceArea(resultSet.getFloat("surfaceArea"));
                    country.setPopulation(resultSet.getInt("countryPopulation"));
                    country.setCapital(resultSet.getInt("capital"));
                    resultMap.put(countryCode, country);
                }

                Long cityId = resultSet.getLong("cityId");
                if (cityId > 0){
                    City city = new City();
                    city.setId(cityId);
                    city.setName(resultSet.getString("cityName"));
                    city.setCountryCode(resultSet.getString("countryCode"));
                    city.setDistrict(resultSet.getString("cityDistrict"));
                    city.setPopulation(resultSet.getInt("cityPopulation"));
                    country.addCity(city);
                }
            }

            return new ArrayList<Country>(resultMap.values());
        }
    }

    public List<Country> findAll() {
        logger.info("Execution method 'find all'");
        List<Country> countries = new ArrayList<Country>();

        Connection connection = null;
        try{
            connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(QUERY_FIND_ALL);
            ResultSet set = statement.executeQuery();

            while (set.next()){
                Country country = new Country();
                country.setCode(set.getString("code"));
                country.setName(set.getString("name"));
                country.setContinent(set.getString("continent"));
                country.setSurfaceArea(set.getFloat("surfaceArea"));
                country.setPopulation(set.getInt("population"));
                country.setCapital(set.getInt("capital"));
                countries.add(country);
            }

            statement.close();
        }catch (SQLException exception){
            logger.error("Problem when executing SELECT from country!", exception);
        }finally {
            this.closeConnection(connection);
        }

        return countries;
    }

    public Country findNameByCode(String code) {
        return jdbcTemplate.queryForObject(QUERY_FINDE_BY_CODE, new CountryMapper(), code);
    }

    public void insert(Country country) {
        Connection connection = null;

        try {
            connection = this.getConnection();
            PreparedStatement statement
                    = connection.prepareStatement(QUERY_INSERT, Statement.NO_GENERATED_KEYS);

            statement.setString(1, country.getCode());
            statement.setString(2, country.getName());
            statement.setString(3, country.getContinent());
            statement.setString(4, country.getRegion());
            statement.setFloat(5, country.getSurfaceArea());
            statement.setInt(6, country.getPopulation());
            statement.execute();

            statement.close();
        }catch (SQLException exception){
            logger.error("Problem when executing INSERT in country!", exception);
        }finally {
            this.closeConnection(connection);
        }
    }

    public void delete(String code) {
        Connection connection = null;

        try {
            connection = this.getConnection();
            PreparedStatement statement = connection.prepareStatement(QUERY_DELETE);

            statement.setString(1, code);
            statement.execute();

            statement.close();
        }catch (SQLException exception){
            logger.error("Problem when executing DELETE in country!", exception);
        }finally {
            this.closeConnection(connection);
        }
    }

    public List<Country> findAllWithDetail() {
        return jdbcTemplate.query(QUERY_ALL_DETAILED, new CountryDetailExtractor());
    }

}
