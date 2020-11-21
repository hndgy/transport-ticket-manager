package application.model.models.carteDeTransport.portefeuille;

import application.model.models.carteDeTransport.produits.ticket.ITicket;
import application.model.models.exceptions.NombreDeVoyageEpuiseException;

public interface IPortefeuilleDeTicket {

    static IPortefeuilleDeTicket creerPortefeuilleDeTicket(long id){
        return new PortefeuilleDeTicketImpl(id);
    }

    long getId();
    void ajouterTicket(ITicket ticket);
    int getNbVoyagesRestant();
    boolean depenserVoyage() ;

}
