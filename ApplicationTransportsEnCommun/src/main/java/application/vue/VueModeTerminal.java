package application.vue;

import application.model.models.carteDeTransport.produits.abonnement.IAbonnement;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.Scanner;

public class VueModeTerminal {

    private Scanner scanner;

    public VueModeTerminal(){
        this.scanner = new Scanner(System.in);
    }


    public void pageIntroduction(){
        System.out.println(
                "___________   _______________________________________^__\n" +
                        " ___   ___ |||  ___   ___   ___    ___ ___  |   __  ,----\\\n" +
                        "|   | |   |||| |   | |   | |   |  |   |   | |  |  | |_____\\\n" +
                        "|___| |___|||| |___| |___| |___|  | O | O | |  |  |        \\\n" +
                        "           |||  TRANSPORT MIAGE   |___|___| |  |__|         )\n" +
                        "___________|||______________________________|______________/\n" +
                        "           |||                                        /--------\n" +
                        "-----------'''---------------------------------------'"
        );
    }

    public int pageAccueil(){
        this.titre("BIENVENUE");
        return checkboxes("Faites un choix",
                List.of(
                        "Valider son titre",
                        "Accéder à son espace personnel",
                        "Inscrivez-vous",
                        "Quitter"
                )
        );

    }

    public String inputMail(){
        return this.input("Mail : ");
    }
    public String inputMdp(){
        return this.input("Mot de passe : ");
    }
    public String inputNom(){
        return this.input("Nom : ");
    }
    public String inputPrenom(){
        return this.input("Prenom : ");
    }

    public void titre(String titre){
        clear();
        System.out.println("################# "+  titre.toUpperCase() +" #################");

    }
    public int pageMenu(){
        this.titre("ESPACE CLIENT");
        return this.checkboxes(" Faites un choix : ",
                List.of(
                        "Acheter un ticket",
                        "Souscrire un abonnement",
                        "Se désinscrire",
                        "Se déconnecter"
                ));
    }

    private int checkboxes(String question, List<String> listeChoix){
        int reponse = -1;
        do {
            System.out.println("        "+question);
            for(int i =1 ; i <= listeChoix.size(); i++){
                System.out.println("        "+i + " - "+ listeChoix.get(i-1));
            }

            var in = this.scanner.nextLine();

            System.out.println(in);
            try{
                reponse = Integer.parseInt(in);
            }catch (NumberFormatException ex){
                this.erreur("Veuillez en rentrer un nombre");
            }


        }while(reponse == -1);
        return reponse;
    }

    private String input(String question){
        System.out.println(question);
        return this.scanner.nextLine();
    }

    public void erreur(String message) {
        System.err.println(message);
    }
    public void info(String message){
        System.out.println( " > " + message);
    }

    public int confirmer(){
        return this.checkboxes("Etes-vous sûr de vouloir faire cela ?", List.of("oui", "non"));
    }

    public void clear(){
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for(int clear = 0; clear < 50; clear++)
        {
            System.out.println("\b") ;
        }

    }

    public String inputValiderCarte() {
        this.titre("valider votre carte");
        return this.input("Votre numero de carte : ");
    }


    public int pageAchatTicket(float prix1Voyage, float prix10Voyages) {

        this.titre("Acheter des tickets");
        return this.checkboxes("Faites un choix : ",List.of(
                "Acheter ticket 1 voyage "+prix1Voyage+"€",
                "Acheter ticket 10 voyages "+prix10Voyages+"€",
                "Retour"

        ));
    }

    public void fermer() {

        this.titre("Au revoir et à bientôt !");
        this.pageIntroduction();
        System.exit(0); // 0 = code pour quitter l'app sans erreurs
    }

    public void inputEnter() {
        this.input("<Entrer> pour continuer");
    }

    public int pageSouscription( float prix1Mois, float prix1An ) {

        this.titre("Souscrire à un abonnement");
        return this.checkboxes("Faites un choix : ",List.of(
                "1 Mois "+prix1Mois+"€",
                "1 Année "+prix1An+"€",
                "Retour"

        ));
    }

    public void pageListeAbonnement(List<IAbonnement> abonnements) {
        System.out.println("ID       DEBUT          FIN   ");
        System.out.println("------------------------------");
        abonnements.forEach(
                a -> {
                    System.out.println( a.getId()+"     "+ dateToString(a.getDateDebut())+"   "+dateToString(a.getDateFin()));
                }
        );
        System.out.println("------------------------------");

    }

    /**
     * LocalDate -> "dd/mm/yyyy"
     * @param date
     * @return
     */
    private String dateToString(LocalDate date){
        return date.getDayOfMonth() + "/"+date.getMonth()+"/"+date.getYear();
    }
}
