package application.model.models.carteDeTransport.produits.ticket;

import application.model.models.exceptions.NombreDeVoyageEpuiseException;

public interface ITicket {

    static ITicket creerTicket1Voyage(long id, float prix){
        return new Ticket1Voyage(id,prix);
    }

    static ITicket creerTicket10Voyage(long id, float prix){
        return new Ticket10Voyages(id, prix);
    }

    long getId();
    float getPrix();
    int getNbVoyagesRestant();
    boolean depenserVoyage() ;


}
