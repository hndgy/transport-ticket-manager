package application.model.bdd;

import application.model.models.carteDeTransport.produits.abonnement.IAbonnement;
import application.model.models.carteDeTransport.produits.ticket.ITicket;
import application.model.models.utilisateur.IUtilisateur;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MySQLBddConnection {

    public static final String ABONNEMENT_MENSUEL = "abonnement_mensuel";
    public static final String ABONNEMENT_ANNUEL = "abonnement_annuel";
    // TABLES
    private static final String USER_TABLE = "`utilisateur`";
    private static final String TARIFS_TABLE = "`tarifs`";
    private static final String ABONNEMENT_TABLE = "`abonnement`";
    private static final String TICKET_TABLE = "`ticket`";
    // CHAINE DE CONNEXION
    private static final String CONNECTION_STRING =
            "jdbc:mysql://" + ConfigBdd.getConfig("mysql.host")
                    + "/" + ConfigBdd.getConfig("mysql.db") +
                    "?user=" + ConfigBdd.getConfig("mysql.user") +
                    "&password=" + ConfigBdd.getConfig("mysql.password");
    private static MySQLBddConnection instance;
    private Connection connection;

    public MySQLBddConnection() {

        try {
            this.connection = DriverManager.getConnection(CONNECTION_STRING);

        } catch (SQLException ex) {
            this.handleSqlError(ex);
        }
    }

    public static MySQLBddConnection getInstance() {
        return instance == null ? new MySQLBddConnection() : instance;
    }

    private void handleSqlError(SQLException ex) {
        System.out.println("SQLException: " + ex.getMessage());
        System.out.println("SQLState: " + ex.getSQLState());
        System.out.println("VendorError: " + ex.getErrorCode());
        ex.printStackTrace();
    }

    /**
     * Insert un utilisateur dans la base
     *
     * @param nom
     * @param prenom
     * @param mail
     * @param mdp
     * @return le dernier user crée dans la bdd
     */
    public long createUser(String nom, String prenom, String mail, String mdp) {

        long res = -1;
        try {

            String sqlQuery =
                    "INSERT INTO " + USER_TABLE + "(`id`,`nom`,`prenom`,`mail`,`mdp`) " +
                            "VALUES (null,'" + nom + "','" + prenom + "','" + mail + "','" + mdp + "')";

            PreparedStatement statement = this.connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            var nbrow = statement.executeUpdate();
            if (nbrow > 0) {
                ResultSet rs = statement.getGeneratedKeys();
                if (rs.next())
                    res = rs.getLong(1);
            }

        } catch (SQLException ex) {
            this.handleSqlError(ex);
        }
        return res;
    }


    /**
     * @return Tous les utilisateurs de la bdd
     */
    public List<IUtilisateur> getAllUser() {
        List<IUtilisateur> res = new ArrayList<>();
        try {
            Statement statement = this.connection.createStatement();
            String sqlQuery = "SELECT * from " + USER_TABLE;
            var resultSet = statement.executeQuery(sqlQuery); // true si l'insertion se passe bien
            while (resultSet.next()) {
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

        } catch (SQLException ex) {
            this.handleSqlError(ex);
        }
        return res;
    }

    /**
     * Verifie si un mail est déjà utilisé par un utilisateur ou non
     *
     * @param mail
     * @return false si il n'exite pas, true sinon
     */
    public boolean checkMail(String mail) {
        try {


            Statement statement = this.connection.createStatement();
            String sqlQuery = "SELECT mail from " + USER_TABLE + " WHERE mail = '" + mail + "'";
            var resultSet = statement.executeQuery(sqlQuery); // true si l'insertion se passe bien

            if (resultSet.next()) {
                // si il n'y pas de ligne avec le meme mail
                return false;

            }

        } catch (SQLException ex) {
            this.handleSqlError(ex);
        }
        return true;
    }


    /**
     * Recupere un utilisateur via son mail et son mdp
     *
     * @param mail
     * @param mdp
     * @return l'utilisateur
     */
    public IUtilisateur getUserByMailAndMdp(String mail, String mdp) {

        IUtilisateur res = null;
        try {
            Statement statement = this.connection.createStatement();
            String sqlQuery = "SELECT * from " + USER_TABLE + " WHERE mail = '" + mail + "' AND mdp = '" + mdp + "'";
            var resultSet = statement.executeQuery(sqlQuery); // true si l'insertion se passe bien

            if (resultSet.next()) {
                res = IUtilisateur.creerUtilisateur(
                        resultSet.getInt("id"),
                        resultSet.getString("prenom"),
                        resultSet.getString("nom"),
                        resultSet.getString("mail"),
                        resultSet.getString("mdp")
                );
            }

        } catch (SQLException ex) {
            this.handleSqlError(ex);
        }
        return res;
    }


    /**
     * Supprime un utilisateur de la bdd grâce à son mail et son mot de passe
     *
     * @param mail
     * @param mdp
     * @return true si l'utilisateur à bien été supprimé
     */
    public boolean deleteUser(String mail, String mdp) {

        try {
            Statement statement = this.connection.createStatement();
            String sqlQuery = "DELETE from " + USER_TABLE + " WHERE mail = '" + mail + "' AND mdp = '" + mdp + "'";
            statement.execute(sqlQuery); // true si le delete se passe bien
            return true;

        } catch (SQLException ex) {
            this.handleSqlError(ex);
        }
        return false;
    }


    /**
     * @param produit
     * @return le tarif d'un produit en particulier
     */
    /*
    Les produits :
        abonnement_mensuel
        abonnement_annuel
        ticket_10_voyages
        ticket_1_voyage
     */
    public float getTarif(String produit) {
        try {
            Statement statement = this.connection.createStatement();
            String sqlQuery = "SELECT prix from " + TARIFS_TABLE + " WHERE type_produit = '" + produit + "' and actif = 1";
            var resultSet = statement.executeQuery(sqlQuery); // true si le delete se passe bien
            if (resultSet.next()) {
                return resultSet.getFloat("prix");
            }

        } catch (SQLException ex) {


            this.handleSqlError(ex);
        }
        return .0f;
    }

    public float getPrix1Voyage() {
        return getTarif("ticket_1_voyage");
    }

    public float getPrix10Voyages() {
        return getTarif("ticket_10_voyages");
    }

    public float getPrix1MoisAbo() {
        return getTarif("abonnement_mensuel");
    }

    public float getPrix1AnAbo() {
        return getTarif("abonnement_annuel");
    }

    /**
     * @param produit
     * @return l'id tarif d'un produit
     */
    public int getIdTarif(String produit) {
        try {


            Statement statement = this.connection.createStatement();
            String sqlQuery = "SELECT id from " + TARIFS_TABLE + " WHERE type_produit = '" + produit + "' and actif = 1";
            var resultSet = statement.executeQuery(sqlQuery); // true si le delete se passe bien
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }

        } catch (SQLException ex) {


            this.handleSqlError(ex);
        }
        return -1;
    }


    /**
     * Fixe un prix pour un produit en particulier
     *
     * @param produit
     * @param prix
     */
    public void setTarif(String produit, float prix) {
        try {


            Statement statement = this.connection.createStatement();
            String sqlQuery1 = "UPDATE " + TARIFS_TABLE + " SET actif = 0 where type_produit = '" + produit + "' and actif = 1";
            statement.execute(sqlQuery1);

            String sqlQuery2 = "INSERT INTO " + TARIFS_TABLE + " VALUES(null, " + produit + "," + prix + ",1)";
            statement.execute(sqlQuery2);


        } catch (SQLException ex) {
            this.handleSqlError(ex);
        }

    }

    public void setPrixAbonnementMensuel(float prix) {
        setTarif("abonnement_mensuel", prix);
    }

    public void setPrixAbonnementAnnuel(float prix) {
        setTarif("abonnement_annuel", prix);
    }

    public void setPrixTicket1Voyage(float prix) {
        setTarif("ticket_1_voyage", prix);
    }

    public void setPrixTicket10Voyages(float prix) {
        setTarif("ticket_10_voyages", prix);
    }


    /**
     * Ajoute un abonnement mensuel pour un utilisateur
     *
     * @param userID
     * @return true si l'ajout de l'abonnement à bien été effectuer
     */
    public boolean abonnementMensuel(long userID) {
        try {
            Statement statement = this.connection.createStatement();
            IAbonnement lastAbonnement = getAbonnementByUser(userID)
                    .stream()
                    .max(Comparator.comparing(IAbonnement::getDateFin)).orElse(null);
            String sqlQuery = "";
            if (lastAbonnement != null && lastAbonnement.estValide()) {
                String x = lastAbonnement.getDateFin().toString();
                sqlQuery = "INSERT INTO " + ABONNEMENT_TABLE + " VALUES" + "(null,'" + lastAbonnement.getDateFin().toString() + "',DATE_ADD('" + lastAbonnement.getDateFin().toString() + "', INTERVAL 1 MONTH), " +
                        getIdTarif(ABONNEMENT_MENSUEL) + ","
                        + userID
                        + ")";
            } else {
                sqlQuery = "INSERT INTO " + ABONNEMENT_TABLE + " VALUES" + "(null,DATE(NOW()),DATE_ADD(DATE(NOW()), INTERVAL 1 MONTH), " +
                        getIdTarif(ABONNEMENT_MENSUEL) + ","
                        + userID
                        + ")";
            }

            statement.execute(sqlQuery);
            return true;
        } catch (SQLException throwables) {
            this.handleSqlError(throwables);
        }
        return false;
    }

    /**
     * Ajoute un abonnement annuel pour un utilisateur
     *
     * @param userID
     * @return true si l'ajout de l'abonnement à bien été effectué
     */
    public boolean abonnementAnnuel(long userID) {
        try {
            Statement statement = this.connection.createStatement();
            IAbonnement lastAbonnement = getAbonnementByUser(userID)
                    .stream()
                    .max(Comparator.comparing(IAbonnement::getDateFin)).orElse(null);
            String sqlQuery = "";
            if (lastAbonnement != null && lastAbonnement.estValide()) {
                sqlQuery = "INSERT INTO " + ABONNEMENT_TABLE + " VALUES" + "(null,'" + lastAbonnement.getDateFin().toString() + "',DATE_ADD('" + lastAbonnement.getDateFin().toString() + "', INTERVAL 1 YEAR), " +
                        getIdTarif(ABONNEMENT_ANNUEL) + ","
                        + userID
                        + ")";
            } else {
                sqlQuery = "INSERT INTO " + ABONNEMENT_TABLE + " VALUES" + "(null,DATE(NOW()),DATE_ADD(DATE(NOW()), INTERVAL 1 YEAR), " +
                        getIdTarif(ABONNEMENT_ANNUEL) + ","
                        + userID
                        + ")";
            }

            statement.execute(sqlQuery);
            return true;
        } catch (SQLException throwables) {
            this.handleSqlError(throwables);
        }
        return false;

    }

    /**
     * Ajoute un ticket à l'unité pour un utilisateur
     *
     * @param userID
     * @return true si l'ajout à bien été effectué
     */
    public boolean insertTicket1Voyage(long userID) {
        try {
            Statement statement = this.connection.createStatement();
            String sqlQuery =
                    "INSERT INTO " + TICKET_TABLE + " VALUES (" +
                            "null," +
                            getIdTarif("ticket_1_voyage") + ","
                            + 1 + ","
                            + userID + ")";

            statement.execute(sqlQuery);
            return true;
        } catch (SQLException throwables) {
            this.handleSqlError(throwables);
        }
        return false;
    }

    /**
     * Ajoute un carnet de 10 tickets pour un utilisateur
     *
     * @param userID
     * @return true si l'ajout à bien été effectué
     */
    public boolean insertTicket10Voyages(long userID) {
        try {
            Statement statement = this.connection.createStatement();
            String sqlQuery =
                    "INSERT INTO " + TICKET_TABLE + " VALUES" + "(" +
                            "null," +
                            getIdTarif("ticket_10_voyages") + ","
                            + 10 + ","
                            + userID + ")";
            statement.execute(sqlQuery);
            return true;
        } catch (SQLException throwables) {
            this.handleSqlError(throwables);
        }
        return false;
    }

    /**
     * @param userID
     * @return les tickets disponibles pour un utilisateur dans une liste
     */
    public List<ITicket> getTicketByUser(long userID) {
        List<ITicket> ticketList = new ArrayList<>();
        try {
            Statement statement = this.connection.createStatement();
            String sqlQuery =
                    "SELECT * FROM " + TICKET_TABLE + " where id_user=" + userID;
            var resultSet = statement.executeQuery(sqlQuery);
            resultSet.next();
            while (resultSet.next()) {

                int idTicket = resultSet.getInt("id");
                ResultSet rsPrix = this.connection.createStatement().executeQuery(
                        "SELECT prix FROM tarifs WHERE id = " + resultSet.getInt("id_tarif"));
                rsPrix.next();
                int prixTicket = rsPrix.getInt("prix");

                ResultSet rsType = this.connection.createStatement().executeQuery(
                        "SELECT type_produit FROM tarifs WHERE id = " + resultSet.getInt("id_tarif"));
                rsType.next();
                String typeVoyage = rsType.getString("type_produit");

                switch (typeVoyage) {
                    case "ticket_1_voyage":
                        ticketList.add(ITicket.creerTicket1Voyage(idTicket, prixTicket));
                    case "ticket_10_voyages":
                        ticketList.add(ITicket.creerTicket10Voyage(idTicket, prixTicket));
                }
            }
            return ticketList;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return ticketList;
    }


    /**
     * @param userID
     * @return les abonnements de l'utilisateur dans une liste
     */
    public List<IAbonnement> getAbonnementByUser(long userID) {
        List<IAbonnement> abonnementsList = new ArrayList<>();
        try {
            Statement statement = this.connection.createStatement();
            String sqlQuery =
                    "SELECT * FROM " + ABONNEMENT_TABLE + " where id_user=" + userID;
            var resSet = statement.executeQuery(sqlQuery);
            while (resSet.next()) {

                int idAbo = resSet.getInt("id");
                ResultSet resPrix = this.connection.createStatement().executeQuery(
                        "SELECT prix FROM tarifs WHERE id = " + resSet.getInt("id_tarif"));
                resPrix.next();
                int prixAbo = resPrix.getInt("prix");

                ResultSet resType = this.connection.createStatement().executeQuery(
                        "SELECT type_produit FROM tarifs WHERE id = " + resSet.getInt("id_tarif"));
                resType.next();
                String typeAbo = resType.getString("type_produit");
                LocalDate dateDebut = resSet.getDate("date_debut").toLocalDate();
                switch (typeAbo) {
                    case ABONNEMENT_MENSUEL:
                        abonnementsList.add(IAbonnement.creerAbonnementMensuel(idAbo, dateDebut, prixAbo));
                        break;
                    case ABONNEMENT_ANNUEL:
                        abonnementsList.add(IAbonnement.creerAbonnementAnnuel(idAbo, dateDebut, prixAbo));
                        break;
                }
            }
            return abonnementsList;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return abonnementsList;
    }


}
