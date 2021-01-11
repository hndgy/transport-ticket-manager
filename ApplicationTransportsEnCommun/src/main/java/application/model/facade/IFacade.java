package application.model.facade;

import application.model.DTO.*;
import application.model.models.carteDeTransport.produits.ticket.ITicket;
import application.model.models.exceptions.MailDejaUtiliseException;
import application.model.models.exceptions.NbTitreNonValide;

import java.util.Collection;
import java.util.List;

public interface IFacade {

    static IFacade creerFacade(){
        return  FacadeImpl.getInstance();
    }

    long inscrire(UserInscriptionDTO userInscriptionDTO) throws MailDejaUtiliseException;

    boolean desinscrire(UserDesinscriptionDTO userDesinscriptionDTO);

    long connecter(UserConnexionDTO userConnexionDTO);

    boolean deconnecter(long idUser);

    boolean souscrireUnAbonnement(SouscriptionDTO souscriptionDTO);

    void commanderTitre(CommandeTitreDTO commandeTitreDTO) throws NbTitreNonValide;

    boolean validerTitre(String idCarte);

    List<ITicket> getTickets(long idUser);

    boolean isConnected(long idUser);

    //ADMIN
    void setPrixAbonnementMensuel(float prix);
    void setPrixAbonnementAnnuel(float prix);
    void setPrixTicket1Voyage(float prix);
    void setPrixTicket10Voyages(float prix);

}
