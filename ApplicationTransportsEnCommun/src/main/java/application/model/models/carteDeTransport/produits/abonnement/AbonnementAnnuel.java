package application.model.models.carteDeTransport.produits.abonnement;


import java.time.LocalDate;

public class AbonnementAnnuel extends AbstractAbonnement {
    public AbonnementAnnuel(long id, LocalDate dateDebut, float prix) {
        super(id, dateDebut, dateDebut.plusYears(1), prix);
    }

    @Override
    public boolean estValide() {
        return getDateFin().isAfter(LocalDate.now());
    }


}
