package application.model.bdd;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MySQLBddConnection {

    private Connection connection;

    private static final String USER_TABLE = "`utilisateur`";

    private static final String CONNECTION_STRING = "jdbc:mysql://localhost/db?user=admin&password=password";

    private static MySQLBddConnection instance;

    private void handleSqlError(SQLException ex){
        System.out.println("SQLException: " + ex.getMessage());
        System.out.println("SQLState: " + ex.getSQLState());
        System.out.println("VendorError: " + ex.getErrorCode());
    }

    public static MySQLBddConnection getInstance(){
        return instance == null ? new MySQLBddConnection() : instance;
    }

    private MySQLBddConnection(){

        try {
            this.connection = DriverManager.getConnection(CONNECTION_STRING);

        } catch (SQLException ex) {
            this.handleSqlError(ex);
        }
    }



    public long createUser(String nom, String prenom, String mail, String mdp){

        long res = -1;
        try{

            String sqlQuery =
                    "INSERT INTO "+USER_TABLE+"(`id`,`nom`,`prenom`,`mail`,`mdp`) " +
                    "VALUES (null,'" + nom + "','"+prenom +"','"+mail+"','"+mdp+"')";

            PreparedStatement statement = this.connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            var nbrow  = statement.executeUpdate();
            if(nbrow > 0){
                ResultSet rs = statement.getGeneratedKeys();
                if(rs.next())
                    res = rs.getLong(1);
            }

        } catch (SQLException ex){
           this.handleSqlError(ex);
        }
        return res;
    }


    public boolean checkMail(String mail){
        try{


            Statement statement = this.connection.createStatement();
            String sqlQuery = "SELECT mail from "+USER_TABLE+ " WHERE mail = '"+ mail + "'";
            var resultSet = statement.executeQuery(sqlQuery); // true si l'insertion se passe bien

            if (resultSet.next() ){
                // si il n'y pas de ligne avec le meme mail
                return false;

            }

        } catch (SQLException ex){
            this.handleSqlError(ex);
        }
        return true;
    }


    public long getUserByMailAndMdp(String mail, String mdp){

         long res = -1;
        try{
            Statement statement = this.connection.createStatement();
            String sqlQuery = "SELECT id from "+USER_TABLE+ " WHERE mail = '"+ mail + "' AND mdp = '" + mdp+"'";
            var resultSet = statement.executeQuery(sqlQuery); // true si l'insertion se passe bien

            while(resultSet.next()){
                res = resultSet.getInt("id");
            }

        } catch (SQLException ex){
            this.handleSqlError(ex);
        }
        return res;
    }


    public boolean deleteUser(String mail, String mdp){

        try{


            Statement statement = this.connection.createStatement();
            String sqlQuery = "DELETE from "+USER_TABLE+ " WHERE mail = '"+ mail + "' AND mdp = '"+mdp+"'";
            statement.execute(sqlQuery); // true si le delete se passe bien
            return true;

        } catch (SQLException ex){
            this.handleSqlError(ex);
        }
        return false;
    }
}
