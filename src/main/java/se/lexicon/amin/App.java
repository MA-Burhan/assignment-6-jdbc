package se.lexicon.amin;

import se.lexicon.amin.data.CityDaoJDBC;
import se.lexicon.amin.data.Database;
import sun.security.provider.certpath.CertId;

import java.sql.SQLException;
import java.util.Optional;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws SQLException {

        Optional<String> op = Optional.empty();
        System.out.println(op.get());
    }
}
