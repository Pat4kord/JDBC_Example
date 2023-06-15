package org.pat4kord.DAO.manipulationSQL;

import org.pat4kord.DAO.Country;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class SelectByNameCountry extends MappingSqlQuery<Country> {

    private static String FIND_BY_NAME
            = "SELECT\n" +
            "\t`code`, \n" +
            "    `name`, \n" +
            "    `continent`, \n" +
            "    `surfaceArea`, \n" +
            "    `population`, \n" +
            "    `capital`\n" +
            "FROM \n" +
            "\tworld.country\n" +
            "WHERE \n" +
            "\tcountry.Name = :name";

    public SelectByNameCountry(DataSource ds) {
        super(ds, FIND_BY_NAME);
        super.declareParameter(new SqlParameter("name", Types.VARCHAR));
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
