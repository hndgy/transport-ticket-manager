package application.model.models.carteDeTransport.abonnement;


import java.time.LocalDate;
import java.util.Date;

public class AbonnementAnnuel extends AbstractAbonnement {
    public AbonnementAnnuel(long id, LocalDate dateDebut) {
        super(id, dateDebut, dateDebut.plusYears(1), 300);
    }


}
