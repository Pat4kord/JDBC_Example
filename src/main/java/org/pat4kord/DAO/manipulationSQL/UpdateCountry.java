package org.pat4kord.DAO.manipulationSQL;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlUpdate;

import javax.sql.DataSource;
import java.sql.Types;

public class UpdateCountry extends SqlUpdate {

    private static String QUERY_UPDATE =
            "UPDATE \n" +
                    "\t`world`.`country` \n" +
                    "SET \n" +
                    "\t`Population` = :population, \n" +
                    "    `LifeExpectancy` = :lifeExpectancy \n" +
                    "WHERE \n" +
                    "\t`Code` = :countryCode";

    public UpdateCountry(DataSource ds) {
        super(ds, QUERY_UPDATE);
        super.declareParameter(new SqlParameter("population", Types.INTEGER));
        super.declareParameter(new SqlParameter("lifeExpectancy", Types.FLOAT));
        super.declareParameter(new SqlParameter("countryCode", Types.CHAR));
    }
}
