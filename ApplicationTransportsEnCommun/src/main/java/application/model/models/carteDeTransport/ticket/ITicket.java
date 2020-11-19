package application.model.models.carteDeTransport.ticket;

public interface ITicket {

    static ITicket creerTicket1Voyage(){
        new Ticket1Voyage();
    }

    static  ITicket creerTicket10Voyage(){
        new Ticket10Voyages();
    }

    long getId();
}
