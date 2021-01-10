package application.model.bdd;

import application.model.models.utilisateur.IUtilisateur;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MySQLBddConnection {

    private Connection connection;

    // TABLES
    private static final String USER_TABLE = "`utilisateur`";
    private static final String TARIFS_TABLE = "`tarifs`";

    // CHAINE DE CONNEXION
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

    public MySQLBddConnection(){

        try {
            this.connection = DriverManager.getConnection(CONNECTION_STRING);

        } catch (SQLException ex) {
            this.handleSqlError(ex);
        }
    }


    /**
     * Insert un utilisateur dans la base
     * @param nom
     * @param prenom
     * @param mail
     * @param mdp
     * @return le dernier user créer dans la base
     */
    public long createUser(String nom, String prenom, String mail, String mdp){

        long res = -1;
        try{
           /*
           // hachage du mdp
           SecureRandom random = new SecureRandom();
            byte[]salt = new byte[16];
            random.nextBytes(salt);
            KeySpec spec = new PBEKeySpec(mdp.toCharArray(), salt, 65536, 128);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");*/
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
        }/* catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }*/
        return res;
    }


    /**
     *
     * @return Tous les utilisateurs de la bases
     */
    public List<IUtilisateur> getAllUser(){
        List<IUtilisateur> res = new ArrayList<>();
        try{



            Statement statement = this.connection.createStatement();
            String sqlQuery = "SELECT * from "+USER_TABLE;
            var resultSet = statement.executeQuery(sqlQuery); // true si l'insertion se passe bien

            while (resultSet.next()){
                // si il n'y pas de ligne avec le meme mail
                var user = IUtilisateur.creerUtilisateur(
                        resultSet.getInt("id"),
                        resultSet.getString("prenom"),
                        resultSet.getString("nom"),
                        resultSet.getString("mail"),
                        resultSet.getString("mdp")

                        );
                res.add(user);

            }

        } catch (SQLException ex){
            this.handleSqlError(ex);
        }
        return res;
    }

    /**
     * Verifie si un mail est déjà utilisé ou non
     * @param mail
     * @return false si il exite pas true sinon
     */
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


    /**
     * Recupere un utilisateur via son mail et son mdp
     * @param mail
     * @param mdp
     * @return
     */
    public IUtilisateur getUserByMailAndMdp(String mail, String mdp){

        IUtilisateur res = null;
        try{
            Statement statement = this.connection.createStatement();
            String sqlQuery = "SELECT * from "+USER_TABLE+ " WHERE mail = '"+ mail + "' AND mdp = '" + mdp+"'";
            var resultSet = statement.executeQuery(sqlQuery); // true si l'insertion se passe bien

            if(resultSet.next()){
                res = IUtilisateur.creerUtilisateur(
                        resultSet.getInt("id"),
                        resultSet.getString("prenom"),
                        resultSet.getString("nom"),
                        resultSet.getString("mail"),
                        resultSet.getString("mdp")
                        );
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

    /*
    Les produits :
        abonnement_mensuel
        abonnement_annuel
        ticket_10_voyages
        ticket_1_voyage
     */
    public float getTarif(String produit){
        try{


            Statement statement = this.connection.createStatement();
            String sqlQuery = "SELECT prix from "+TARIFS_TABLE+ " WHERE type_produit = '"+ produit + "'";
            var resultSet = statement.executeQuery(sqlQuery); // true si le delete se passe bien
            if (resultSet.next()){
                 return resultSet.getFloat("prix");
            }

        } catch (SQLException ex){
            this.handleSqlError(ex);
        }
        return .0f;
    }

    public boolean abonnementMensuel(long userID){
        try {
            Statement statement = this.connection.createStatement();
            String sqlQuery =
                    "INSERT INTO abonnement VALUES"+ "(null,DATE(NOW()),DATE_ADD(DATE(NOW()), INTERVAL 1 MONTH),"
                    +userID
                    +",1)";
            statement.execute(sqlQuery);
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public boolean abonnementAnnuel(long userID){
        try {
            Statement statement = this.connection.createStatement();
            String sqlQuery =
                    "INSERT INTO abonnement VALUES"+ "(null,DATE(NOW()),DATE_ADD(DATE(NOW()), INTERVAL 1 YEAR),"
                            +userID
                            +",2)";
            statement.execute(sqlQuery);
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;

    }

    public static void main(String[] args) {
        MySQLBddConnection bdd = new MySQLBddConnection();

        System.out.println(bdd.getTarif("ticket_1_voyage"));
    }
}
