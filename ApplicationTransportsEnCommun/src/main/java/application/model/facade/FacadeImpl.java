package application.model.facade;

import application.model.DTO.*;
import application.model.bdd.FakeDB;
import application.model.models.carteDeTransport.ICarteDeTransport;
import application.model.models.carteDeTransport.portefeuille.IPortefeuilleDeTicket;
import application.model.models.carteDeTransport.portefeuille.PortefeuilleDeTicketImpl;
import application.model.models.carteDeTransport.produits.abonnement.IAbonnement;
import application.model.models.carteDeTransport.produits.ticket.ITicket;
import application.model.models.utilisateur.IUtilisateur;

import javax.swing.text.DefaultEditorKit;
import java.util.List;
import java.util.Map;

public class FacadeImpl implements IFacade {



    FakeDB db;

    FacadeImpl(){
        this.db = new FakeDB();
    }

    @Override
    public long inscrire(UserInscriptionDTO userInscriptionDTO) {

        return db.ajouterUser(userInscriptionDTO.getNom(), userInscriptionDTO.getPrenom(), userInscriptionDTO.getMail(), userInscriptionDTO.getMotDePasse());

    }

    @Override
    public boolean desinscrire(UserDesinscriptionDTO userDesinscriptionDTO) {
        return false;
    }

    @Override
    public long connecter(UserConnexionDTO userConnexionDTO) {
        return db.checkUser(userConnexionDTO.getMail(), userConnexionDTO.getMdp()) ;
    }

    @Override
    public boolean deconnecter(long idUser) {
        return false;
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
