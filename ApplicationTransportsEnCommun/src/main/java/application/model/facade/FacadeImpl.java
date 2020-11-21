package application.model.facade;

import application.model.DTO.*;
import application.model.bdd.FakeDB;
import application.model.bdd.MySQLBddConnection;
import application.model.models.carteDeTransport.ICarteDeTransport;
import application.model.models.carteDeTransport.portefeuille.IPortefeuilleDeTicket;
import application.model.models.carteDeTransport.portefeuille.PortefeuilleDeTicketImpl;
import application.model.models.carteDeTransport.produits.abonnement.IAbonnement;
import application.model.models.carteDeTransport.produits.ticket.ITicket;
import application.model.models.exceptions.MailDejaUtiliseException;
import application.model.models.utilisateur.IUtilisateur;

import javax.lang.model.type.NullType;
import javax.swing.text.DefaultEditorKit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FacadeImpl implements IFacade {



    private Map<Long, Long> connectedUsers;
    private MySQLBddConnection mySQLBddConnection;

    private static FacadeImpl instance;
    public static FacadeImpl getInstance(){
        return instance == null ? new FacadeImpl() : instance;
    }
    private FacadeImpl(){
        this.mySQLBddConnection = MySQLBddConnection.getInstance();
        this.connectedUsers = new HashMap<>();
    }

    @Override
    public long inscrire(UserInscriptionDTO userInscriptionDTO) throws MailDejaUtiliseException {
        boolean checkMail = mySQLBddConnection.checkMail(userInscriptionDTO.getMail());
        if(checkMail){
            return mySQLBddConnection.createUser(userInscriptionDTO.getNom(), userInscriptionDTO.getPrenom(), userInscriptionDTO.getMail(), userInscriptionDTO.getMotDePasse());
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
        this.connectedUsers.remove(idUser);
        return true;
    }

    @Override
    public boolean souscrireUnAbonnement(SouscriptionDTO souscriptionDTO) {
        return false;
    }

    @Override
    public boolean commanderTitre(CommandeTitreDTO commandeTitreDTO) {
        return false;
    }

    @Override
    public boolean validerTitre(long idCarte) {
        return false;
    }
}
