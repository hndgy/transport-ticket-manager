package application.model.bdd;


import application.model.models.carteDeTransport.ICarteDeTransport;
import application.model.models.carteDeTransport.portefeuille.IPortefeuilleDeTicket;
import application.model.models.carteDeTransport.produits.abonnement.IAbonnement;
import application.model.models.carteDeTransport.produits.ticket.ITicket;
import application.model.models.utilisateur.IUtilisateur;

import java.util.HashMap;
import java.util.Map;

public class FakeDB {

    //user
    Map<Long, IUtilisateur> utilisateurs;

    //cartes
    Map<Long, ICarteDeTransport> cartesDeTransport;
    Map<Long, IPortefeuilleDeTicket> portefeuilles;

    //produits
    Map<Long, IAbonnement> abonnements;
    Map<Long, ITicket> tickets;

    // relations
    Map<Long, Long> relationCartesUtilisateurs;
    Map<Long, Long> relationTicketsPortefeuilles;
    Map<Long, Long> relationPortefeuillesCartes;
    Map<Long, Long> relationAbonnementsCartes;


    private static long id_user = 0;
    private static long id_abonnement = 0;
    private static long id_ticket = 0;
    private static long id_carte = 0;
    private static long id_portefeuille = 0;

    public FakeDB(){
        this.utilisateurs  = new HashMap<>();
        this.cartesDeTransport = new HashMap<>();
        this.portefeuilles  = new HashMap<>();
        this.abonnements = new HashMap<>();
        this.tickets  = new HashMap<>();
        this.relationCartesUtilisateurs = new HashMap<>();
        this.relationTicketsPortefeuilles  = new HashMap<>();
        this.relationPortefeuillesCartes = new HashMap<>();
        this.relationAbonnementsCartes = new HashMap<>();
    }

    public long ajouterUser(String nom, String prenom, String mail, String mdp){
        id_user++;
        this.utilisateurs.put(id_user, IUtilisateur.creerUtilisateur(id_user,prenom,nom,mail,mdp));
        return id_user;
    }


    public long checkUser(String mail, String mdp){
        var id_res = -1;
        var users =  this.utilisateurs.entrySet();
        return users.stream().filter( (entry) -> mail.equals(entry.getValue().getMail()) && mdp.equals(entry.getValue().getMotDePasse())).findFirst().get().getKey();
    }





}
