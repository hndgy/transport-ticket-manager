package application.model.facade;

import application.model.DTO.*;

public interface IFacade {

    static IFacade creerFacade(){
        return new FacadeImpl();
    }

    long inscrire(UserInscriptionDTO userInscriptionDTO);

    boolean desinscrire(UserDesinscriptionDTO userDesinscriptionDTO);

    long connecter(UserConnexionDTO userConnexionDTO);

    boolean deconnecter(long idUser);

    boolean souscrireUnAbonnement(SouscriptionDTO souscriptionDTO);

    boolean commanderTitre(CommandeTitreDTO commandeTitreDTO);

    boolean validerTitre(long idCarte);


}
