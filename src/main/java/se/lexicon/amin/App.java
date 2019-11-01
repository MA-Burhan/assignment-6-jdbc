package se.lexicon.amin;


import se.lexicon.amin.data.CityDaoJDBC;
import se.lexicon.amin.data.Database;
import se.lexicon.amin.model.City;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;


public class App 
{
    public static void main( String[] args ) throws SQLException {


       // Database.getConnection();
       // CityDaoJDBC test = new CityDaoJDBC();

        //findById - OK

//        Optional<City> result = test.findById(10);
//
//        City city = result.get();
//
//        System.out.println(city);

        //findByCountryCode - OK

//        List<City> cityList = test.findByCountryCode("AFG");

//        for (City city : cityList) {
//            System.out.println(city);
//        }

        //findByName - OK

//        List<City> cityList = test.findByName("San Fernando");
//
//        for (City city : cityList) {
//            System.out.println(city);
//        }

        //findAll - OK

//        List<City> cityList = test.findAll();
//
//        for (City city : cityList) {
//            System.out.println(city);
//        }


        //add - OK
//        City newCity = new City("Amins stad", "AFG", "Amins distrikt", 123213);
//
//        City returnedCity = test.add(newCity);
//
//        System.out.println(returnedCity);



        //update - OK

//        City city = test.findById(4082).get();
//
//        city.setName("Amins stad 2");
//
//        City returnedCity = test.update(city);
//
//        System.out.println(returnedCity);



        //delete - OK

//        City city = test.findById(4082).get();
//
//        int numrows = test.delete(city);
//
//        System.out.println(numrows);








    }
}
