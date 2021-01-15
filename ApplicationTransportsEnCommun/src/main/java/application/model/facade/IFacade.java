package application.model.facade;

import application.model.DTO.*;
import application.model.models.produits.abonnement.IAbonnement;
import application.model.models.produits.ticket.ITicket;
import application.model.models.exceptions.MailDejaUtiliseException;
import application.model.models.utilisateur.IUtilisateur;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.List;

public interface IFacade {

    static IFacade creerFacade(){
        return  FacadeImpl.getInstance();
    }

    long inscrire(UserInscriptionDTO userInscriptionDTO) throws MailDejaUtiliseException;

    boolean desinscrire(UserDesinscriptionDTO userDesinscriptionDTO);

    long connecter(UserConnexionDTO userConnexionDTO);

    boolean deconnecter(long idUser);


    void souscrireAbonnement1Mois(long idUser);
    void souscrireAbonnement1An(long idUser);

    void commmander1Voyage(long idUser);
    void commmander10Voyages(long idUser);


    boolean validerTitre(String idCarte);

    List<ITicket> getTickets(long idUser);

    List<IAbonnement> getAbonnements(long idUser);

    boolean isConnected(long idUser);

    ObjectId getIdCarteByIdTitu(long idTitu);

    int getNbVoyage(long idTitu);

    int getNbVoyageByIdCarte(String idCarte);

    LocalDate getFinAbonnement(String idCarte);

    //ADMIN
    void setPrixAbonnementMensuel(float prix);
    void setPrixAbonnementAnnuel(float prix);
    void setPrixTicket1Voyage(float prix);
    void setPrixTicket10Voyages(float prix);

    float getPrix10Voyages();

    float getPrix1Voyage();
    float getPrix1Mois();
    float getPrix1An();

    IUtilisateur getUser(long idUser);


}
