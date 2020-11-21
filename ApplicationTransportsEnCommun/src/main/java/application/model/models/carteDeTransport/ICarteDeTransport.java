package application.model.models.carteDeTransport;

import application.model.models.carteDeTransport.produits.abonnement.IAbonnement;
import application.model.models.carteDeTransport.portefeuille.IPortefeuilleDeTicket;

public interface ICarteDeTransport {

    static ICarteDeTransport creerCarteDeTransport(long id, long idTitulaire, IPortefeuilleDeTicket portefeuilleDeTicket){
        return new CarteDeTransportImpl(id, idTitulaire, portefeuilleDeTicket);
    }

    long getIdTitulaire();
    long getId();
    IAbonnement getAbonnement();
    IPortefeuilleDeTicket getPortefeuilleDeTicket();

    boolean verificationDuTitre(); // Retourne true si l'abonnement est valide ou si il y a 1 ticket, sinon false.

}
