package org.pat4kord.DAO.manipulationSQL;

import org.pat4kord.DAO.Country;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SelectAllCountry extends MappingSqlQuery<Country> {

    private static final String QUERY_FIND_ALL
            = "SELECT " +
            "`code`, `name`, `continent`, `surfaceArea`, `population`, `capital` " +
            "FROM " +
            "`country`";

    public SelectAllCountry(DataSource ds) {
        super(ds, QUERY_FIND_ALL);
    }

    @Override
    protected Country mapRow(ResultSet resultSet, int i) throws SQLException {
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
