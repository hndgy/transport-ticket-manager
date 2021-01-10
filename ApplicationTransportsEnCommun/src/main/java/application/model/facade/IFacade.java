package application.model.facade;

import application.model.DTO.*;
import application.model.models.exceptions.MailDejaUtiliseException;

public interface IFacade {

    static IFacade creerFacade(){
        return  FacadeImpl.getInstance();
    }

    long inscrire(UserInscriptionDTO userInscriptionDTO) throws MailDejaUtiliseException;

    boolean desinscrire(UserDesinscriptionDTO userDesinscriptionDTO);

    long connecter(UserConnexionDTO userConnexionDTO);

    boolean deconnecter(long idUser);

    boolean souscrireUnAbonnement(SouscriptionDTO souscriptionDTO);

    void commanderTitre(CommandeTitreDTO commandeTitreDTO);

    boolean validerTitre(String idCarte);

    boolean isConnected(long idUser);

    //ADMIN
    void setPrixAbonnementMensuel(float prix);
    void setPrixAbonnementAnnuel(float prix);
    void setPrixTicket1Voyage(float prix);
    void setPrixTicket10Voyages(float prix);

}
