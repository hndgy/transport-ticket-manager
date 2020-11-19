package application.model.facade;

import application.model.DTO.UserConnexionDTO;
import application.model.DTO.UserDesinscriptionDTO;
import application.model.DTO.UserInscriptionDTO;

public interface IFacade {

    default IFacade creerFacade(){
        return null; //new FacadeImpl();
    }
    void inscrire(UserInscriptionDTO userInscriptionDTO);

    void desinscrire(UserDesinscriptionDTO userDesinscriptionDTO);

    void connecter(UserConnexionDTO userConnexionDTO);

    void deconnecter();

    void souscrireUnAbonnement();

    void commanderTitre();

    void validerTitre();


}
