package application.model.models.carteDeTransport.portefeuille;


import application.model.models.carteDeTransport.produits.ticket.ITicket;

import java.util.ArrayList;

public class PortefeuilleDeTicketImpl extends AbstractPortefeuilleDeTicket {


    public PortefeuilleDeTicketImpl(long id) {
        super(new ArrayList<>(), id);
    }


    @Override
    public void ajouterTicket(ITicket ticket) {

    }
}
