package application.model.facade;

import application.model.DTO.*;
import application.model.bdd.MongoDbConnection;
import application.model.bdd.MySQLBddConnection;

import application.model.bdd.pojos.Carte;
import application.model.models.carteDeTransport.CarteDeTransportImpl;
import application.model.models.exceptions.MailDejaUtiliseException;
import org.bson.types.ObjectId;

import java.util.HashMap;

import java.util.Map;

public class FacadeImpl implements IFacade {



    private Map<Long, Long> connectedUsers;
    private MySQLBddConnection mySQLBddConnection;
    private MongoDbConnection mongoDbConnection;

    private static FacadeImpl instance;
    public static FacadeImpl getInstance(){
        return instance == null ? new FacadeImpl() : instance;
    }
    private FacadeImpl(){
        this.mySQLBddConnection = MySQLBddConnection.getInstance();
        this.connectedUsers = new HashMap<>();
        this.mongoDbConnection =  MongoDbConnection.getMongoInstance();
    }

    @Override
    public long inscrire(UserInscriptionDTO userInscriptionDTO) throws MailDejaUtiliseException {
        boolean checkMail = mySQLBddConnection.checkMail(userInscriptionDTO.getMail());
        if(checkMail){
            long idTitu =  mySQLBddConnection.createUser(userInscriptionDTO.getNom(), userInscriptionDTO.getPrenom(), userInscriptionDTO.getMail(), userInscriptionDTO.getMotDePasse());
            mongoDbConnection.addCarteByTitu(idTitu);
            return idTitu;
        }
        else throw new MailDejaUtiliseException(userInscriptionDTO.getMail());

    }

    @Override
    public boolean desinscrire(UserDesinscriptionDTO userDesinscriptionDTO) {
        return this.mySQLBddConnection.deleteUser(userDesinscriptionDTO.getMail(), userDesinscriptionDTO.getMdp());

    }

    @Override
    public long connecter(UserConnexionDTO userConnexionDTO) {
        var user = mySQLBddConnection.getUserByMailAndMdp(userConnexionDTO.getMail(), userConnexionDTO.getMdp());
        if (user != -1 ){
            this.connectedUsers.put(user,user);
            return user;
        }
        return -1;

    }

    @Override
    public boolean deconnecter(long idUser) {
        var res = this.connectedUsers.remove(idUser);
        return true;
    }

    @Override
    public boolean souscrireUnAbonnement(SouscriptionDTO souscriptionDTO) {
        return false;
    }

    @Override
    public long commanderTitre(CommandeTitreDTO commandeTitreDTO) {
        return mongoDbConnection.updateNbVoyage(commandeTitreDTO.getIdCarte(),commandeTitreDTO.getNbTitre());
    }

    @Override
    public boolean validerTitre(long idCarte) {
        return false;
    }
}
