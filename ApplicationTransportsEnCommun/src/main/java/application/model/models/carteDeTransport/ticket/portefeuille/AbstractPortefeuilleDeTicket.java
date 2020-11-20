package application.model.models.carteDeTransport.ticket.portefeuille;

import application.model.models.carteDeTransport.ticket.ITicket;

import java.util.List;
import java.util.Map;
import java.util.Queue;

public abstract class AbstractPortefeuilleDeTicket implements IPortefeuilleDeTicket {

    private List<ITicket> tickets;
    private long id;


    public AbstractPortefeuilleDeTicket(List<ITicket> tickets, long id) {
        this.tickets = tickets;
        this.id = id;
    }


    @Override
    public long getId() {
        return this.id;
    }
    @Override
    public int getNbVoyagesRestant() {
        return this.tickets.size();
    }

    @Override
    public void depenserTicket() {
        this.tickets.get(0).;

    }
    @Override
    public void ajouterTicket(ITicket ticket){
        this.tickets.add(ticket);
    }
}
