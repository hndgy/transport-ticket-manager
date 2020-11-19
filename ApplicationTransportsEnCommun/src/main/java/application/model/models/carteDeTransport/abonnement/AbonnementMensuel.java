package application.model.models.carteDeTransport.abonnement;

import java.time.LocalDate;

public class AbonnementMensuel extends AbstractAbonnement {
    public AbonnementMensuel(long id, LocalDate dateDebut) {
        super(id, dateDebut, dateDebut.plusMonths(1), 50.99f);
    }
}
