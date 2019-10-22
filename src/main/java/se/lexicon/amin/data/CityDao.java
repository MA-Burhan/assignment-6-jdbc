package se.lexicon.amin.data;

import se.lexicon.amin.model.City;

import java.util.List;
import java.util.Optional;

public interface CityDao {

    Optional<City> findById(int id);

    List<City> findByCountryCode(String countryCode);

    List<City> findByName(String name);

    List<City> findAll();

    City add(City city);

    City update(City city);

    int delete(City city);
}
