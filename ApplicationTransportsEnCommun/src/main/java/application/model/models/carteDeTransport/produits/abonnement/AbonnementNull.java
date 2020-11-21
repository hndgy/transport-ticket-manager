package application.model.models.carteDeTransport.produits.abonnement;

import java.time.LocalDate;

public class AbonnementNull extends AbstractAbonnement{


    private static AbonnementNull instance;
    private AbonnementNull() {
        super(-1 , null, null, 0);
    }

    @Override
    public boolean estValide() {
        return false;
    }

    public static AbonnementNull getInstance(){
        return instance == null ? new AbonnementNull() : instance;
    }

}
