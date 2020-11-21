package application.model.models.carteDeTransport;

import application.model.models.carteDeTransport.produits.abonnement.IAbonnement;
import application.model.models.carteDeTransport.portefeuille.IPortefeuilleDeTicket;

public abstract class AbstractCarteDeTransport implements ICarteDeTransport{

    private long id;
    private long idTitulaire;
    private IAbonnement abonnement;
    private IPortefeuilleDeTicket portefeuilleDeTicket;

    public AbstractCarteDeTransport(long id, long idTitulaire, IPortefeuilleDeTicket portefeuilleDeTicket) {
        this.id = id;
        this.idTitulaire = idTitulaire;
        this.abonnement = IAbonnement.ABONNEMENT_NULL;
        this.portefeuilleDeTicket = portefeuilleDeTicket;
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
        if(this.abonnement.estValide()){
            return true;
        }else return this.portefeuilleDeTicket.depenserVoyage();
    }
}
