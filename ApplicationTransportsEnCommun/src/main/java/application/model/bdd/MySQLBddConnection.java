package application.model.bdd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLBddConnection {

    private Connection connection;

    private static final String USER_TABLE = "utilisateur";

    private static final String CONNECTION_STRING = "jdbc:mysql://localhost:3306/db?" +
            "user=admin&password=password";


    private void handleSqlError(SQLException ex){
        System.out.println("SQLException: " + ex.getMessage());
        System.out.println("SQLState: " + ex.getSQLState());
        System.out.println("VendorError: " + ex.getErrorCode());
    }
    public MySQLBddConnection(){
        try {
            this.connection = DriverManager.getConnection(CONNECTION_STRING);

        } catch (SQLException ex) {
            this.handleSqlError(ex);
        }
    }



    public boolean createUser(String nom, String prenom, String mail, String mdp){

        boolean res = false;
        try{
            Statement statement = this.connection.createStatement();
            String sqlQuery = "INSERT INTO `utilisateur` (`id`,`nom`,`prenom`,`mail`,`mdp`) VALUES (null," + nom + ","+prenom +","+mail+","+mdp+")";
            res = statement.execute(sqlQuery); // true si l'insertion se passe bien
        } catch (SQLException ex){
           this.handleSqlError(ex);
        }
        return res;
    }
}
