package application.model.models.carteDeTransport;

import application.model.models.carteDeTransport.abonnement.IAbonnement;
import application.model.models.carteDeTransport.ticket.portefeuille.IPortefeuilleDeTicket;

public interface ICarteDeTransport {

    static ICarteDeTransport creerCarteDeTransport(long id, long idTitulaire, IAbonnement abonnement, IPortefeuilleDeTicket portefeuilleDeTicket, boolean valide){
        return new CarteDeTransportImpl(id, idTitulaire, abonnement, portefeuilleDeTicket, valide);
    }

    long getIdTitulaire();
    long getId();
    IAbonnement getAbonnement();
    IPortefeuilleDeTicket getPortefeuilleDeTicket();

    boolean verificationDuTitre(); // Retourne true si l'abonnement est valide ou si il y a 1 ticket, sinon false.



}
