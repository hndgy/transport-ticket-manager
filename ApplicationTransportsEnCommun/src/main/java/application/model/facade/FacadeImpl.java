package application.model.facade;

import application.model.DTO.*;
import application.model.bdd.MongoDbConnection;
import application.model.bdd.MySQLBddConnection;
import application.model.bdd.pojos.Carte;
import application.model.models.carteDeTransport.produits.ticket.ITicket;
import application.model.models.exceptions.MailDejaUtiliseException;
import application.model.models.exceptions.NbTitreNonValide;
import application.model.models.utilisateur.IUtilisateur;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FacadeImpl implements IFacade {


    private static FacadeImpl instance;
    private Map<Long, IUtilisateur> connectedUsers;
    private MySQLBddConnection mySQLBddConnection;
    private MongoDbConnection mongoDbConnection;

    /**
     * Constructeur de FacadeImpl
     * Se connecte à la bdd Mongo et Mysql et créer une hashmap pour les utilisateurs connectés
     */
    private FacadeImpl() {
        this.mySQLBddConnection = MySQLBddConnection.getInstance();
        this.connectedUsers = new HashMap<>();
        this.mongoDbConnection = MongoDbConnection.getMongoInstance();
    }

    public static FacadeImpl getInstance() {
        return instance == null ? new FacadeImpl() : instance;
    }

    /**
     * Inscription d'un utilisateur dans la bdd MySql et création de sa carte dans la bdd Mongo
     * @param userInscriptionDTO
     * @return l'id du nouvel utilisateur
     * @throws MailDejaUtiliseException
     */
    @Override
    public long inscrire(UserInscriptionDTO userInscriptionDTO) throws MailDejaUtiliseException {
        boolean checkMail = mySQLBddConnection.checkMail(userInscriptionDTO.getMail());
        if (checkMail) {
            long idTitu = mySQLBddConnection.createUser(userInscriptionDTO.getNom(), userInscriptionDTO.getPrenom(), userInscriptionDTO.getMail(), userInscriptionDTO.getMotDePasse());
            mongoDbConnection.addCarteByTitu(idTitu);
            return idTitu;
        } else throw new MailDejaUtiliseException(userInscriptionDTO.getMail());

    }

    /**
     * Désinscrit l'utilisateur de la bdd MySql et supprime sa carte dans Mongo
     * @param userDesinscriptionDTO
     * @return true si l'utilisateur est bien désinscrit sinon false
     */
    @Override
    public boolean desinscrire(UserDesinscriptionDTO userDesinscriptionDTO) {
        long id  = mySQLBddConnection.getUserByMailAndMdp(userDesinscriptionDTO.getMail(), userDesinscriptionDTO.getMdp()).getId();
        if(isConnected(id)){
            this.deconnecter(id);
        }
        mongoDbConnection.removeCarteByTitu(this.mySQLBddConnection.getUserByMailAndMdp(userDesinscriptionDTO.getMail(), userDesinscriptionDTO.getMdp()).getId());
        return this.mySQLBddConnection.deleteUser(userDesinscriptionDTO.getMail(), userDesinscriptionDTO.getMdp());

    }

    /**
     * Connecte un utilisateur et l'ajoute dans la hashmap
     * @param userConnexionDTO
     * @return l'id de l'utilisateur connecté
     */
    @Override
    public long connecter(UserConnexionDTO userConnexionDTO) {
        var user = mySQLBddConnection.getUserByMailAndMdp(userConnexionDTO.getMail(), userConnexionDTO.getMdp());
        if (user != null) {
            this.connectedUsers.put(user.getId(), user);
            return user.getId();
        }
        return -1;

    }

    /**
     * Déconnecte l'utilisateur et le retire de la hashmap
     * @param idUser
     * @return true si l'utilisateur est bien déconnecté
     */
    @Override
    public boolean deconnecter(long idUser) {
        var res = this.connectedUsers.remove(idUser);
        return true;
    }

    /**
     * Permet à l'utilisateur de souscrire à un abonnement mensuel ou annuel et ajoute l'abonnement correspondant sur sa carte
     * via la bdd Mongo
     * @param souscriptionDTO
     * @return true si l'abonnement à bien été souscrit
     */
    @Override
    public boolean souscrireUnAbonnement(SouscriptionDTO souscriptionDTO) {

        switch (souscriptionDTO.getType()) {
            case "mensuel":
                mySQLBddConnection.abonnementMensuel(souscriptionDTO.getIdUser());
                mongoDbConnection.updateAbonnement(souscriptionDTO.getIdUser(), 1);
                return true;
            case "annuel":
                mySQLBddConnection.abonnementAnnuel(souscriptionDTO.getIdUser());
                mongoDbConnection.updateAbonnement(souscriptionDTO.getIdUser(), 12);
                return true;
        }
        return false;
    }

    /**
     * Permet de commander soit un ticket à l'unité ou un carnet de 10 tickets et l'incrémente sur la carte de l'utilisateur
     * @param commandeTitreDTO
     * @throws NbTitreNonValide
     */
    @Override
    public void commanderTitre(CommandeTitreDTO commandeTitreDTO) throws NbTitreNonValide {
        switch (commandeTitreDTO.getNbTitre()){
            case 1 :
                mySQLBddConnection.insertTicket1Voyage(commandeTitreDTO.getIdUser());
                break;
            case 10 :
                mySQLBddConnection.insertTicket10Voyages(commandeTitreDTO.getIdUser());
                break;
            default:
                throw new NbTitreNonValide();
        }
         mongoDbConnection.updateNbVoyage(commandeTitreDTO.getIdUser(), commandeTitreDTO.getNbTitre());
    }

    /**
     * Valide ou non la carte de l'utilisateur sous réserve d'un nombre suffisant de voyage ou d'un abonnement valide
     * @param idCarte
     * @return true si la carte est valide pour embarquer
     */
    @Override
    public boolean validerTitre(String idCarte) {

        return mongoDbConnection.isValide(idCarte);
    }


    /**
     * @param idUser
     * @return la liste des tickets posséder par un utilisateur
     */
    @Override
    public List<ITicket> getTickets(long idUser){
        return mySQLBddConnection.getTicket_byUser(idUser);
    }

    /**
     * Check si l'utilisateur est inclus dans la hashmap
     * @param idUser
     * @return true si l'utilisateur est connecté
     */
    @Override
    public boolean isConnected(long idUser) {
        return this.connectedUsers.containsKey(idUser);
    }

    /**
     * @param idTitu
     * @return l'id de la carte d'un utilisateur via son identifiant
     */
    @Override
    public ObjectId getIdCarteByIdTitu(long idTitu) {
        return mongoDbConnection.getIdCarteByIdTitu(idTitu);
    }

    /**
     * @param idTitu
     * @return le nombre de voyage disponible d'un utilisateur
     */
    @Override
    public int getNbVoyage(long idTitu) {
        return mongoDbConnection.getNbVoyage(idTitu);
    }

    /**
     * @param idTitu
     * @return la date de fin d'abonnement de la carte d'un utilisateur
     */
    @Override
    public LocalDate getFinAbonnement(long idTitu) {
        return mongoDbConnection.getFinAboByTitu(idTitu);
    }

    @Override
    public void setPrixAbonnementMensuel(float prix) {
       mySQLBddConnection.setPrixAbonnementMensuel(prix);
    }

    @Override
    public void setPrixAbonnementAnnuel(float prix) {
        mySQLBddConnection.setPrixAbonnementAnnuel(prix);
    }

    @Override
    public void setPrixTicket1Voyage(float prix) {
        mySQLBddConnection.setPrixTicket1Voyage(prix);
    }

    @Override
    public void setPrixTicket10Voyages(float prix) {
        mySQLBddConnection.setPrixTicket10Voyages(prix);
    }


}
