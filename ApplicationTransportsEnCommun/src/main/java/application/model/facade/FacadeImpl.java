package application.model.facade;

import application.model.DTO.*;
import application.model.bdd.MongoDbConnection;
import application.model.bdd.MySQLBddConnection;
import application.model.models.exceptions.MailDejaUtiliseException;
import application.model.models.exceptions.NbTitreNonValide;
import application.model.models.utilisateur.IUtilisateur;

import java.util.HashMap;
import java.util.Map;

public class FacadeImpl implements IFacade {


    private static FacadeImpl instance;
    private Map<Long, IUtilisateur> connectedUsers;
    private MySQLBddConnection mySQLBddConnection;
    private MongoDbConnection mongoDbConnection;

    private FacadeImpl() {
        this.mySQLBddConnection = MySQLBddConnection.getInstance();
        this.connectedUsers = new HashMap<>();
        this.mongoDbConnection = MongoDbConnection.getMongoInstance();
    }

    public static FacadeImpl getInstance() {
        return instance == null ? new FacadeImpl() : instance;
    }

    @Override
    public long inscrire(UserInscriptionDTO userInscriptionDTO) throws MailDejaUtiliseException {
        boolean checkMail = mySQLBddConnection.checkMail(userInscriptionDTO.getMail());
        if (checkMail) {
            long idTitu = mySQLBddConnection.createUser(userInscriptionDTO.getNom(), userInscriptionDTO.getPrenom(), userInscriptionDTO.getMail(), userInscriptionDTO.getMotDePasse());
            mongoDbConnection.addCarteByTitu(idTitu);
            return idTitu;
        } else throw new MailDejaUtiliseException(userInscriptionDTO.getMail());

    }

    @Override
    public boolean desinscrire(UserDesinscriptionDTO userDesinscriptionDTO) {
        long id  = mySQLBddConnection.getUserByMailAndMdp(userDesinscriptionDTO.getMail(), userDesinscriptionDTO.getMdp()).getId();
        if(isConnected(id)){
            this.deconnecter(id);
        }
        mongoDbConnection.removeCarteByTitu(this.mySQLBddConnection.getUserByMailAndMdp(userDesinscriptionDTO.getMail(), userDesinscriptionDTO.getMdp()).getId());
        return this.mySQLBddConnection.deleteUser(userDesinscriptionDTO.getMail(), userDesinscriptionDTO.getMdp());

    }

    @Override
    public long connecter(UserConnexionDTO userConnexionDTO) {
        var user = mySQLBddConnection.getUserByMailAndMdp(userConnexionDTO.getMail(), userConnexionDTO.getMdp());
        if (user != null) {
            this.connectedUsers.put(user.getId(), user);
            return user.getId();
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

    @Override
    public boolean validerTitre(String idCarte) {

        return mongoDbConnection.isValide(idCarte);
    }

    @Override
    public boolean isConnected(long idUser) {
        return this.connectedUsers.containsKey(idUser);
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
