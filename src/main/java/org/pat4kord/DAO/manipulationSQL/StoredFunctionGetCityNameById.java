package org.pat4kord.DAO.manipulationSQL;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlFunction;

import javax.sql.DataSource;
import java.sql.Types;

public class StoredFunctionGetCityNameById extends SqlFunction<String> {
    private static String QUERY = "SELECT getCityNameById(?)";

    public StoredFunctionGetCityNameById(DataSource ds) {
        super(ds, QUERY);
        super.declareParameter(new SqlParameter(Types.INTEGER));
        super.compile();
    }
}
