package application.model.models.carteDeTransport.abonnement;

import java.time.LocalDate;

public interface IAbonnement {

    static IAbonnement creerAbonnementAnnuel(long id,LocalDate dateDebut){
        return new AbonnementAnnuel(id,dateDebut);
    }
    static IAbonnement creerAbonnementMensuel(long id,LocalDate dateDebut){
        return new AbonnementMensuel(id,dateDebut);
    }

    long getId();
    LocalDate getDateDebut();
    LocalDate getDateFin();

    float getPrix();


}
