package application.model.models.produits.ticket;

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
