package application.model.models.produits.abonnement;

import java.time.LocalDate;

public class AbonnementMensuel extends AbstractAbonnement {
    public AbonnementMensuel(long id, LocalDate dateDebut, float prix) {
        super(id, dateDebut, dateDebut.plusMonths(1), prix);
    }

    @Override
    public boolean estValide() { return LocalDate.now().isBefore(getDateFin()) ;}
    //public boolean estValide() { return getDateFin().isAfter(LocalDate.now()); }
}
