package application.model.models.produits.abonnement;

import java.time.LocalDate;

public interface IAbonnement {

    static AbonnementAnnuel creerAbonnementAnnuel(long id, LocalDate dateDebut, float prix){
        return new AbonnementAnnuel(id,dateDebut, prix);
    }
    static AbonnementMensuel creerAbonnementMensuel(long id, LocalDate dateDebut, float prix){
        return new AbonnementMensuel(id,dateDebut, prix);
    }

    AbonnementNull ABONNEMENT_NULL = AbonnementNull.getInstance();

    long getId();
    LocalDate getDateDebut();
    LocalDate getDateFin();

    float getPrix();

    boolean estValide();

}
