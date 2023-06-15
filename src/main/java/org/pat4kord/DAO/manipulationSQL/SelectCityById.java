package org.pat4kord.DAO.manipulationSQL;

import org.pat4kord.DAO.City;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class SelectCityById extends MappingSqlQuery<City> {
    private static String QUERY =
            "SELECT \n" +
                    "\tID as `id`,\n" +
                    "\tName as `name`, \n" +
                    "\tCountryCode as `countryCode`,  \n" +
                    "\tDistrict as `district`,\n" +
                    "\tPopulation as `population`\n" +
                    "FROM \n" +
                    "\t`world`.`city`\n" +
                    "WHERE \n" +
                    "\tid = :id";
    public SelectCityById(DataSource ds) {
        super(ds, QUERY);
        super.declareParameter(new SqlParameter("id", Types.INTEGER));
    }

    @Override
    protected City mapRow(ResultSet resultSet, int i) throws SQLException {
        City city = new City();
        city.setId(resultSet.getLong("id"));
        city.setName(resultSet.getString("name"));
        city.setCountryCode(resultSet.getString("countryCode"));
        city.setDistrict(resultSet.getString("district"));
        city.setPopulation(resultSet.getInt("population"));
        return city;
    }
}
