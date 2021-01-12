package application.controlleur;

import application.model.DTO.*;
import application.model.facade.IFacade;
import application.model.models.exceptions.MailDejaUtiliseException;
import application.model.models.utilisateur.IUtilisateur;
import application.vue.VueModeTerminal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Controlleur {
    private IFacade facade;
    private VueModeTerminal vue;

    private long idConnected;


    public Controlleur() {
        this.facade = IFacade.creerFacade();
        this.vue = new VueModeTerminal();
        this.introduction();
    }

    public void introduction(){
        this.vue.pageIntroduction();
        this.accueil();
    }
    public void accueil(){
        int reponse = this.vue.pageAccueil();
        if (reponse == 1 ){
            this.validerCarte();
        } else if (reponse == 2){
            this.connexion();
        } else if (reponse == 3 ){
            this.inscription();
        } else if (reponse == 4){
            this.vue.fermer();
        }else{
            accueil();
        }
    }

    public void connexion(){
        this.vue.titre("connexion");
        String mail  = this.vue.inputMail();
        String mdp = this.vue.inputMdp();
        if (mail.equals("") || mdp.equals("")){
            this.vue.erreur("Veuillez bien remplir les champs");
            connexion();
        }else{
            this.idConnected = facade.connecter(new UserConnexionDTO(mail, mdp));

            if(this.idConnected != -1 ){
                this.vue.info("Vous êtes connecté !");
                this.menuEspaceClient();
            }else {
                this.vue.erreur("Votre mail ou mot de passe est incorrect");
                this.connexion();
            }
        }

    }
    public void inscription() {
        this.vue.titre("inscription");
        String nom = this.vue.inputNom();
        String prenom = this.vue.inputPrenom();
        String mail = this.vue.inputMail();
        String mdp = this.vue.inputMdp();


        try {
            long id = this.facade.inscrire(new UserInscriptionDTO(nom, prenom, mail, mdp));
            if (id != -1) {
                this.vue.info("Bienvenue "+prenom+", vous êtes inscrit !");
                this.idConnected = this.facade.connecter(new UserConnexionDTO(mail, mdp));
                var idCarte = this.facade.getIdCarteByIdTitu(idConnected).toHexString();
                this.vue.info("Voici votre nouvelle carte : "+ idCarte);
                this.vue.info("Vous pouvez dès maintenant acheter un ticket ou souscrire à un abonnement");
                this.vue.inputEnter();
                this.vue.info("Redirection vers votre espace...");
                this.menuEspaceClient();

            } else {
                this.inscription();
            }
        } catch (MailDejaUtiliseException e) {
            this.vue.erreur(mail + " est déjà utilisé. Veuillez recommencer");
            this.inscription();
        }




    }

    public void menuEspaceClient(){
        IUtilisateur userConnected = this.facade.getUser(idConnected);

        var reponse = this.vue.pageMenu(userConnected.getPrenom(), userConnected.getNom());

        if(reponse == 1){
            this.infosCarte();
        } else if (reponse == 2){
            this.achatTicket();
        }else if(reponse == 3){
            this.souscriptionAbonnment();
        }else if(reponse == 4){
            this.desinscription();
        } else if(reponse == 5){
            this.idConnected = -1;
            this.vue.info("Vous avez été déconnecté");
            this.accueil();
        }else this.menuEspaceClient();

    }

    private void infosCarte() {
        var idCarte =  this.facade.getIdCarteByIdTitu(idConnected).toHexString();
        var nbVoyage = this.facade.getNbVoyage(idConnected);
        var finAbo = this.facade.getFinAbonnement(idCarte);

        this.vue.pageInfosCarte(
               idCarte,
                finAbo,
                nbVoyage
        );

        this.menuEspaceClient();
    }

    public void desinscription(){
        var rep = this.vue.confirmer();
        if (rep == 1 ){

                String mail = vue.inputMail();
                String mdp = vue.inputMdp();
                this.facade.desinscrire(new UserDesinscriptionDTO(mail, mdp));
            this.vue.info("Vous avez êtes désinscrit");
            this.accueil();

        }else{
            this.menuEspaceClient();
        }
    }
    public void souscriptionAbonnment(){
        var reponse = this.vue.pageSouscription(this.facade.getPrix1Mois(),this.facade.getPrix1An());
        if (reponse == 1){

            this.facade.souscrireAbonnement1Mois(idConnected);
            this.confirmationSouscription();
        }else if(reponse == 2){
            this.facade.souscrireAbonnement1An(idConnected);
            this.confirmationSouscription();
        }else if(reponse == 3){
            this.menuEspaceClient();
        } else {
            this.souscriptionAbonnment();
        }
    }

    public void achatTicket(){
        var reponse = this.vue.pageAchatTicket(this.facade.getPrix1Voyage(),this.facade.getPrix10Voyages());
        if (reponse == 1){

            this.facade.commmander1Voyage(idConnected);
            this.confirmationCommande();
        }else if(reponse == 2){
            this.facade.commmander10Voyages(idConnected);
            this.confirmationCommande();
        }else if(reponse == 3){
            this.menuEspaceClient();
        }else {
            this.achatTicket();
        }


    }

    public void confirmationCommande(){
        this.vue.info("Vous avez maintenant: "+ this.facade.getNbVoyage(idConnected) + " voyages");
        this.vue.inputEnter();
        this.achatTicket();
    }

    public void confirmationSouscription(){
        this.vue.pageListeAbonnement(this.facade.getAbonnements(idConnected));
        this.vue.inputEnter();
        this.souscriptionAbonnment();
    }

    public void validerCarte(){

        String rep = this.vue.inputValiderCarte();

        if(rep.equals("0")){
            this.accueil();
        }else{
            boolean isValide = facade.validerTitre(rep);
            if (isValide){
                int nbVoyage = facade.getNbVoyageByIdCarte(rep);
                LocalDate finabo = facade.getFinAbonnement(rep);
                LocalDateTime today  = LocalDateTime.now();
                this.vue.info("Votre titre est valide vous pouvez passer ["+ today.getHour() +":"+today.getMinute() +"]");
                if(finabo != null){
                    this.vue.info("         Fin Abonnement : "+ VueModeTerminal.dateToString(finabo)) ;
                }else{
                    this.vue.info("         Voyages restants : "+nbVoyage);
                }

                this.vue.inputEnter();
                this.accueil();
            }else {
                this.vue.erreur("Votre titre n'a pas de voyage disponible");
                this.validerCarte();
            }

        }


    }


    public void historiqueAchat(){

    }

}
