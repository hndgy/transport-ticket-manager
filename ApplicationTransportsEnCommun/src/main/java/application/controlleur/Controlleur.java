package application.controlleur;

import application.model.DTO.UserConnexionDTO;
import application.model.DTO.UserDesinscriptionDTO;
import application.model.DTO.UserInscriptionDTO;
import application.model.facade.IFacade;
import application.model.models.exceptions.MailDejaUtiliseException;
import application.vue.VueModeTerminal;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
        }

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

    }

    public void achatTicket(){

    }

    public void confirmationCommande(){

    }

    public void validerCarte(){

        String rep = this.vue.inputValiderCarte();

        if (!rep.equals("") || rep.length() != 24){
            boolean isValide = facade.validerTitre(rep);
            if (isValide){
                this.vue.info("Votre titre est valide vous pouvez passer ["+ LocalDateTime.now() +"]");
                // Affficher aussi le nb de voyage restant
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
