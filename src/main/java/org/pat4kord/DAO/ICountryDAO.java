package org.pat4kord.DAO;

import java.util.List;

public interface ICountryDAO {
    List<Country> findAll();
    Country findNameByCode(String code);
    void insert(Country country);
    void delete(String code);
    List<Country> findAllWithDetail();
}
