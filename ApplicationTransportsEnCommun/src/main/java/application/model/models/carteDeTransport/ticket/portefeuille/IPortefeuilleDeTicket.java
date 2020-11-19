package application.model.models.carteDeTransport.ticket.portefeuille;

import application.model.models.carteDeTransport.ticket.ITicket;

import java.util.Collection;

public interface IPortefeuilleDeTicket {

    static IPortefeuilleDeTicket creerPortefeuilleDeTicket(){
        return new PortefeuilleDeTicketImpl();
    }

    long getId();
    void ajouterTicket(ITicket ticket);
    int getNbVoyagesRestant();
    void depenserTicket();

}
