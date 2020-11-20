package application.model.models.carteDeTransport;

import application.model.models.carteDeTransport.abonnement.IAbonnement;
import application.model.models.carteDeTransport.ticket.portefeuille.IPortefeuilleDeTicket;

public abstract class AbstractCarteDeTransport implements ICarteDeTransport{

    private long id;
    private long idTitulaire;
    private IAbonnement abonnement;
    private IPortefeuilleDeTicket portefeuilleDeTicket;
    private boolean valide;

    public AbstractCarteDeTransport(long id, long idTitulaire, IAbonnement abonnement, IPortefeuilleDeTicket portefeuilleDeTicket, boolean valide) {
        this.id = id;
        this.idTitulaire = idTitulaire;
        this.abonnement = abonnement;
        this.portefeuilleDeTicket = portefeuilleDeTicket;
        this.valide = valide;
    }

    @Override
    public long getIdTitulaire() {
        return this.idTitulaire;
    }

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public IAbonnement getAbonnement() {
        return this.abonnement;
    }

    @Override
    public IPortefeuilleDeTicket getPortefeuilleDeTicket() {
        return this.portefeuilleDeTicket;
    }

    @Override
    public boolean verificationDuTitre() {
        return this.valide;
    }
}
