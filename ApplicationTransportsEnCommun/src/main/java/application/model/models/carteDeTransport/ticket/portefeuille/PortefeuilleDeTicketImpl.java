package application.model.models.carteDeTransport.ticket.portefeuille;


import application.model.models.carteDeTransport.ticket.ITicket;

import java.util.ArrayList;
import java.util.List;

public class PortefeuilleDeTicketImpl extends AbstractPortefeuilleDeTicket {


    public PortefeuilleDeTicketImpl(long id) {
        super(new ArrayList<>(), id);
    }



    @Override
    public void depenserTicket(long idTicket) {

    }

    @Override
    public void ajouterTicket(ITicket ticket) {

    }
}
