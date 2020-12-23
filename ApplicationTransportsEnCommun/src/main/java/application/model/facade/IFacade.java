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

    long commanderTitre(CommandeTitreDTO commandeTitreDTO);

    boolean validerTitre(long idCarte);


}
