package application.model.models.carteDeTransport;

import application.model.models.carteDeTransport.abonnement.IAbonnement;
import application.model.models.carteDeTransport.ticket.portefeuille.IPortefeuilleDeTicket;

public class CarteDeTransportImpl extends AbstractCarteDeTransport {

    public CarteDeTransportImpl(long id, long idTitulaire, IAbonnement abonnement, IPortefeuilleDeTicket portefeuilleDeTicket, boolean valide) {
        super(id, idTitulaire, abonnement, portefeuilleDeTicket, valide);
    }
}
