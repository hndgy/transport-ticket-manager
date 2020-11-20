package application.model.models.carteDeTransport.ticket;

public interface ITicket {

    static ITicket creerTicket1Voyage(long id){
        return new Ticket1Voyage(id);
    }

    static  ITicket creerTicket10Voyage(long id){
        return new Ticket10Voyages(id);
    }

    long getId();
    float getPrix();
    int getNbVoyages();
}
