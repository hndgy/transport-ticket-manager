package application.model.models.carteDeTransport.portefeuille;

import application.model.models.carteDeTransport.produits.ticket.ITicket;
import application.model.models.exceptions.NombreDeVoyageEpuiseException;

import java.util.List;

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
        int nbVoyages = 0;
        for (ITicket ticket : this.tickets){
            nbVoyages += ticket.getNbVoyagesRestant();
        }
        return nbVoyages;
    }

    @Override
    public boolean depenserVoyage() {
        for (ITicket ticket : this.tickets){
            if(ticket.getNbVoyagesRestant() > 0){
                ticket.depenserVoyage();
                return true;
            }
        }
        return false;
    }
    @Override
    public void ajouterTicket(ITicket ticket){
        this.tickets.add(ticket);
    }
}
