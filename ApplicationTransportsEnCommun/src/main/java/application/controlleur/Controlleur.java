package application.controlleur;

import application.model.DTO.*;
import application.model.facade.IFacade;
import application.model.models.exceptions.MailDejaUtiliseException;
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

        if (mail.equals("") || mdp.equals("") || nom.equals("") || prenom.equals("")) {
            this.vue.erreur("Veuillez bien remplir les champs");
            inscription();
        } else {
            try {
                long id = this.facade.inscrire(new UserInscriptionDTO(nom, prenom, mail, mdp));
                if (id != -1) {
                    this.vue.info("Vous êtes inscrit");
                    this.connexion();
                } else {
                    this.inscription();
                }
            } catch (MailDejaUtiliseException e) {
                this.vue.erreur(mail + " est déjà utilisé");
            }


        }

    }

    public void menuEspaceClient(){

        var reponse = this.vue.pageMenu();

        if (reponse == 1){
            this.achatTicket();
        }else if(reponse == 2){
            this.souscriptionAbonnment();
        }else if(reponse == 3){
            this.desinscription();
        } else if(reponse == 4){
            this.idConnected = -1;
            this.vue.info("Vous avez été déconnecté");
            this.accueil();
        }else this.menuEspaceClient();

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

        if (!rep.equals("") || rep.length() != 24){ // 24 = tailles des _id en base
            boolean isValide = facade.validerTitre(rep);
            if (isValide){
                int nbVoyage = facade.getNbVoyage(this.idConnected);
                LocalDate finabo = facade.getFinAbonnement(idConnected);
                this.vue.info("Votre titre est valide vous pouvez passer ["+ LocalDateTime.now() +"]");
                if(finabo != null){
                    this.vue.info("         Fin Abonnement : "+ finabo.getDayOfMonth()+"/"+finabo.getMonth()+"/"+finabo.getYear());
                }else{
                    this.vue.info("         Voyages restants : "+nbVoyage);
                }

                this.accueil();

            }else {
                this.vue.erreur("Votre titre n'est pas valide");
                this.validerCarte();
            }
        }else {
            this.validerCarte();
        }
    }


    public void historiqueAchat(){

    }

}
