package application.vue;

import application.model.bdd.pojos.Carte;
import application.model.models.carteDeTransport.produits.abonnement.IAbonnement;
import application.model.models.carteDeTransport.produits.ticket.ITicket;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        var in = this.input("Mail : ");
        Matcher matcher = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE)
                .matcher(in);
         if (!matcher.find()){

             this.erreur("Veuillez rentrer un mail");
             this.inputMail();
         }

        return in;

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
        this.info(titre);
        clear();
        System.out.println("################# "+  titre.toUpperCase() +" #################");

    }
    public int pageMenu(String nom, String prenom){
        this.titre("ESPACE CLIENT : "+prenom + " "+nom);
        return this.checkboxes(" Faites un choix : ",
                List.of("Voir ma carte",
                        "Acheter un ticket",
                        "Souscrire un abonnement",
                        "Se désinscrire",
                        "Se déconnecter"
                ));
    }

    private int checkboxes(String question, List<String> listeChoix){
        int reponse = -1;
        do {
            System.out.println(question);
            System.out.println("------------------------------------");
            for(int i =1 ; i <= listeChoix.size(); i++){
                System.out.println(i + " - "+ listeChoix.get(i-1));
            }
            System.out.println("------------------------------------");
            var in = this.scanner.nextLine();

            try{
                reponse = Integer.parseInt(in);
            }catch (NumberFormatException ex){
                this.erreur("Veuillez choisir un nombre entre "+ 1 +" et "+listeChoix.size());
            }


        }while(reponse == -1);
        return reponse;
    }

    private String input(String question){
        System.out.println(question);
        var in =  this.scanner.nextLine();
        if(in.equals("")){
            this.erreur("Veuillez remplir correctement le champ");
            this.input(question);
        }
        return in;

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

        var in = this.input("Votre numero de carte ( 0 = retour) : ");

        Matcher matcher = Pattern.compile("[0-9A-F]{24}", Pattern.CASE_INSENSITIVE) // hexa de 24 char
                .matcher(in);
        if (!matcher.find() && !in.equals("0")){

            this.erreur("La carte est invalide");
            in = this.inputValiderCarte();
        }

        return in;




    }


    public int pageAchatTicket(float prix1Voyage, float prix10Voyages) {

        this.titre("Acheter des tickets");
        return this.checkboxes("Faites un choix : ",List.of(
                "Acheter ticket 1 voyage à " +prix1Voyage+"€",
                "Acheter ticket 10 voyages à "+prix10Voyages+"€",
                "Retour"

        ));
    }

    public void fermer() {

        this.titre("Au revoir et à bientôt !");
        this.pageIntroduction();
        System.exit(0); // 0 = code pour quitter l'app sans erreurs
    }

    public void inputEnter() {
        System.out.println("Appuyer sur <Entrer> pour continuer...");
        this.scanner.nextLine();

    }

    public int pageSouscription( float prix1Mois, float prix1An ) {

        this.titre("Souscrire à un abonnement");
        return this.checkboxes("Faites un choix : ",List.of(
                "S'abonner pendant 1 Mois à "+prix1Mois+"€",
                "S'abonner pendant 1 Année à "+prix1An+"€",
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
    public static String dateToString(LocalDate date){
        return date.getDayOfMonth() + "/"+date.getMonthValue()+"/"+date.getYear();
    }

    public void pageInfosCarte(String idCarte,LocalDate finabo, int nbVoyage) {
        this.titre("ma carte");
        System.out.println("Numero de votre carte : "+ idCarte);
        if(finabo != null){
            System.out.println("Fin de l'abonnement : "+ dateToString(finabo));
        }else {
            System.out.println("Pas d'abonnement");
        }

        System.out.println("Nombre de voyage restant : " + nbVoyage);
        this.inputEnter();
    }



    //bonus
    public void pinponpinpon(){
        this.info("Vous avez essayé de frauder");
        this.inputEnter();
        this.clear();

        System.out.println(
                "              _____________________\n" +
                        "               (<$$$$$$>#####<::::::>)\n" +
                        "            _/~~~~~~~~~~~~~~~~~~~~~~~~~\\\n" +
                        "          /~                             ~\\\n" +
                        "        .~                                 ~\n" +
                        "    ()\\/_____                           _____\\/()\n" +
                        "   .-''      ~~~~~~~~~~~~~~~~~~~~~~~~~~~     ``-.\n" +
                        ".-~              __________________              ~-.\n" +
                        "`~~/~~~~~~~~~~~~TTTTTTTTTTTTTTTTTTTT~~~~~~~~~~~~\\~~'\n" +
                        "| | | #### #### || | | | [] | | | || #### #### | | |\n" +
                        ";__\\|___________|++++++++++++++++++|___________|/__;\n" +
                        " (~~====___________________________________====~~~)\n" +
                        "  \\------_____________[CHIP 911]__________-------/\n" +
                        "     |      ||         ~~~~~~~~       ||      |\n" +
                        "      \\_____/                          \\_____/"
        );
        this.inputEnter();

    }
}
