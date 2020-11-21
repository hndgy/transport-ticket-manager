package application.model.models.carteDeTransport;

import application.model.models.carteDeTransport.produits.abonnement.IAbonnement;
import application.model.models.carteDeTransport.portefeuille.IPortefeuilleDeTicket;

public class CarteDeTransportImpl extends AbstractCarteDeTransport {

    public CarteDeTransportImpl(long id, long idTitulaire, IPortefeuilleDeTicket portefeuilleDeTicket) {
        super(id, idTitulaire, portefeuilleDeTicket);
    }
}
