package sys.applications;
import sys.Config;
import sys.Core.*;
import sys.State;
import sys.models.IMenu;
import sys.models.Scenario;

import java.util.Scanner;
import java.lang.Math;

public class CommandLineMenu implements IMenu {

    public void clear() {
        for(int i=0; i<50; i++) System.out.println();
    }

    public void ScreenSH() {  
        System.out.println("MENU' PRINCIPALE");
        System.out.println("#");
        System.out.println("#"); 
    }

    public void ScreenFI() {  
        System.out.println("CONFIGURAZIONE INIZIALE");
        System.out.println("#");
        System.out.println("#");   
    }

    public void ScreenSET() {  
        System.out.println("MENU' OPZIONI");
        System.out.println("#");
        System.out.println("#");
        System.out.println("Per settare i paramtetri inziali digita:");
        System.out.println("1 - Popolazione inziale");
        System.out.println("2 - Durata malattia");
        System.out.println("3 - Risorse inziali");
        System.out.println("4 - Costo del tampone");
        System.out.println("5 - Infettivita'");
        System.out.println("6 - Sintomaticita'");
        System.out.println("7 - Letalita'");
        System.out.println("8 - Eta' massima");
        System.out.println("9 - Eta' media");
        System.out.println("10 - Larghezza e Altezza dello spazio grafico della simulazione");
        System.out.println("0 - Per uscire");
    }

    @Override
    public int show() { 
        Scanner Input = new Scanner(System.in);

        clear();
        ScreenSH();
        System.out.println("Per far partire la simulazione digitare 0. Per configurare le impostazioni inziali digitare 2:");
        int action = Integer.parseInt(Input.nextLine()); 
        while(action != 0 && action != 2) {
            clear();
            ScreenSH();
            System.out.println("Parametro non valido. Reinserirlo:");
            action = Integer.parseInt(Input.nextLine());  
        }
        return action; 
    }

    @Override
    public void firstInput(Config config) {
        Scanner Input = new Scanner(System.in);

        clear();
        ScreenFI();
        // Popolazione iniziale
        System.out.println("Inserire il numero della popolazione iniziale (max 100000):");
        int populationNumber = Integer.parseInt(Input.nextLine());
        while(populationNumber > 100000) {
            clear();
            ScreenFI();
            System.out.println("Numero troppo grande. Reinserirlo:");
            populationNumber = Integer.parseInt(Input.nextLine());  
        }
        config.populationNumber = populationNumber;
        // Durata della malattia
        clear();
        ScreenFI();
        System.out.println("Inserire la durata della malattia (max 90 giorni):");
        int diseaseDuration = Integer.parseInt(Input.nextLine());
        while(diseaseDuration > 90) {
            clear();
            ScreenFI();
            System.out.println("Numero troppo grande. Reinserirlo:");
            diseaseDuration = Integer.parseInt(Input.nextLine());  
        }
        config.diseaseDuration = diseaseDuration;
        // Risorse iniziali
        clear();
        ScreenFI();
        System.out.println("Inserire il numero delle risorse iniziali:");
        System.out.println("(deve essere minore di "+(config.populationNumber*config.diseaseDuration)+")");
        int initialResource = Integer.parseInt(Input.nextLine());
        while(initialResource >= config.populationNumber * config.diseaseDuration) {
            clear();
            ScreenFI();
            System.out.println("Numero troppo grande. Reinserirlo:");
            System.out.println("(deve essere minore di "+(config.populationNumber*config.diseaseDuration)+")");
            initialResource = Integer.parseInt(Input.nextLine());  
        }
        config.initialResources = initialResource;
        // Costo del tampone
        clear();
        ScreenFI();
        System.out.println("Inserire il costo del tampone:");
        System.out.println("(deve essere maggiore di "+(config.initialResources/(config.populationNumber*10))+")");
        int swabsCost = Integer.parseInt(Input.nextLine());
        while(swabsCost <= config.initialResources/(config.populationNumber * 10)) {
            clear();
            ScreenFI();
            System.out.println("Numero troppo piccolo. Reinserirlo:");
            System.out.println("(deve essere maggiore di "+(config.initialResources/(config.populationNumber*10))+")");
            swabsCost = Integer.parseInt(Input.nextLine());  
        }
        config.swabsCost = swabsCost;
        // Infettività
        clear();
        ScreenFI();
        System.out.println("Inserire la percentuale di infettivita' :");
        System.out.println("(deve essere maggiore di 0)");
        Double infectivity = Double.parseDouble(Input.nextLine()); 
        while(infectivity <= 0.0 || infectivity > 100.0) {
            clear();
            ScreenFI();
            System.out.println("Percentuale non valida. Reinserirla:");
            System.out.println("(deve essere maggiore di 0)");
            infectivity = Double.parseDouble(Input.nextLine()); 
        }
        config.infectivity= infectivity;
        // Sintomaticità
        clear();
        ScreenFI();
        System.out.println("Inserire la percentuale di sintomaticita' :");
        System.out.println("(deve essere maggiore di 0)");
        Double sintomaticity = Double.parseDouble(Input.nextLine()); 
        while(sintomaticity <= 0.0 || sintomaticity > 100.0) {
            clear();
            ScreenFI();
            System.out.println("Percentuale non valida. Reinserirla:");
            System.out.println("(deve essere maggiore di 0)");
            sintomaticity = Double.parseDouble(Input.nextLine());   
        }
        config.sintomaticity = sintomaticity;
        // Letalità
        clear();
        ScreenFI();
        System.out.println("Inserire la percentuale di letalita' :");
        System.out.println("(deve essere maggiore di 0)");
        Double letality = Double.parseDouble(Input.nextLine()); 
        while(letality <= 0.0 || letality > 100.0) {
            clear();
            ScreenFI();
            System.out.println("Percentuale non valida. Reinserirla:");
            System.out.println("(deve essere maggiore di 0)");
            letality = Double.parseDouble(Input.nextLine());  
        }
        config.letality = letality;
        // Età massima
        clear();
        ScreenFI();
        System.out.println("Inserire l'eta' massima (compresa tra 50 e 100) :");
        int maxAge = Integer.parseInt(Input.nextLine());
        while(maxAge < 50 || maxAge > 100) {
            clear();
            ScreenFI();
            System.out.println("Eta' non valida. Reinserirla (compresa tra 50 e 100):");
            maxAge = Integer.parseInt(Input.nextLine());  
        }
        config.maxAge = maxAge;
        // Età media
        clear();
        ScreenFI();
        System.out.println("Inserire l'eta' media (compresa tra 20 e 80) :");
        int ageAverage = Integer.parseInt(Input.nextLine());
        while(ageAverage < 20 || ageAverage > 80) {
            clear();
            ScreenFI();
            System.out.println("Eta' non valida. Reinserirla (compresa tra 20 e 80):");
            ageAverage = Integer.parseInt(Input.nextLine());  
        }
        config.ageAverage = ageAverage;
        
        // Altezza e Larghezza
        clear();
        ScreenFI();
        System.out.println("Inserire l'altezza e la larghezza dello spazio grafico dove verrà visualizzata la simulazione");
        System.out.println("(Valore consigliati: "+(int)Math.sqrt(config.populationNumber*2)+")");
        System.out.println("Il prodotto devve essere compreso tra "+(config.populationNumber*10)+" e "+(config.populationNumber/10)+")");
        System.out.print("Larghezza: "); int width = Integer.parseInt(Input.nextLine());
        System.out.print("Altezza: "); int heigth = Integer.parseInt(Input.nextLine());
        while((width*heigth > config.populationNumber*10)||(width*heigth < config.populationNumber/10)) {
            clear();
            ScreenFI();
            System.out.println("Parametri non validi. Reinserirli:");
            System.out.println("(Valore consigliati: "+(int)Math.sqrt(config.populationNumber*2)+")");
            System.out.println("Il prodotto delle due grandezze deve essere compreso tra "+(config.populationNumber*10)+" e "+(config.populationNumber/10)+")");
            System.out.print("Larghezza: "); width = Integer.parseInt(Input.nextLine());
            System.out.print("Altezza: "); heigth = Integer.parseInt(Input.nextLine());

        }
        // config.size[0] = width;  config.size[1] = heigth;   TOGLIERE IL COMMENTO   
    }

    @Override
    public int settings(Config config) { 
        Scanner Input = new Scanner(System.in);

        clear();
        ScreenSET();
        int action = -1;

        while(action!=0) {
            clear();
            ScreenSET();
            action = Integer.parseInt(Input.nextLine());
            switch(action) {


                case(1):
                clear();
                ScreenSET();
                // Popolazione iniziale
                System.out.println("Inserire il numero della popolazione iniziale (max 100000):");
                int populationNumber = Integer.parseInt(Input.nextLine());
                while(populationNumber > 100000) {
                    clear();
                    ScreenSET();
                    System.out.println("Numero troppo grande. Reinserirlo:");
                    populationNumber = Integer.parseInt(Input.nextLine());  
                }
                config.populationNumber = populationNumber;
                // settando un nuovo valore per la popolazione iniziale controllo se 
                // il valore delle risorse inziali sia ancora buono
                if(config.initialResources > config.populationNumber * config.diseaseDuration) {
                    clear();
                    ScreenSET();
                    System.out.println("ATTENZIONE! il numero delle risorse iniziali non è più valido:");
                    System.out.println("Inserire il numero delle risorse iniziali:");
                    System.out.println("(deve essere minore di "+(config.populationNumber*config.diseaseDuration)+")");
                    int initialResource = Integer.parseInt(Input.nextLine());
                    while(initialResource >= config.populationNumber * config.diseaseDuration) {
                        clear();
                        ScreenSET();
                        System.out.println("Numero troppo grande. Reinserirlo:");
                        System.out.println("(deve essere minore di "+(config.populationNumber*config.diseaseDuration)+")");
                        initialResource = Integer.parseInt(Input.nextLine());  
                    }
                    config.initialResources = initialResource;
                }
                // controllo se il valore del costo del tampone sia ancora buono
                if(config.swabsCost > config.initialResources/(config.populationNumber * 10)) {
                    clear();
                    ScreenSET();
                    System.out.println("ATTENZIONE! il costo del tampone non è più valido:");
                    System.out.println("Inserire il costo del tampone:");
                    System.out.println("(deve essere maggiore di "+(config.initialResources/(config.populationNumber*10))+")");
                    int swabsCost = Integer.parseInt(Input.nextLine());
                    while(swabsCost <= config.initialResources/(config.populationNumber * 10)) {
                        clear();
                        ScreenSET();
                        System.out.println("Numero troppo piccolo. Reinserirlo:");
                        System.out.println("(deve essere maggiore di "+(config.initialResources/(config.populationNumber*10))+")");
                        swabsCost = Integer.parseInt(Input.nextLine());  
                    }
                    config.swabsCost = swabsCost;
                }
                // e controllo se i valori di altezza e larghezza siano ancora validi
/* TOGLIERE IL COMMENTO
                if((config.size[0]*config.size[1] > config.populationNumber*10)||(config.size[0]*config.size[1] < config.populationNumber/10)) {
                    clear();
                    ScreenSET();
                    System.out.println("ATTENZIONE! I valori di altezza e larghezza non sono piu' validi");
                    System.out.println("Inserire l'altezza e la larghezza dello spazio grafico dove verrà visualizzata la simulazione");
                    System.out.println("(Valore consigliati: "+(int)Math.sqrt(config.populationNumber*2)+")");
                    System.out.println("Il prodotto devve essere compreso tra "+(config.populationNumber*10)+" e "+(config.populationNumber/10)+")");
                    System.out.print("Larghezza: "); int width = Integer.parseInt(Input.nextLine());
                    System.out.print("Altezza: "); int heigth = Integer.parseInt(Input.nextLine());
                    while((width*heigth > config.populationNumber*10)||(width*heigth < config.populationNumber/10)) {
                        clear();
                        ScreenSET();
                        System.out.println("Parametri non validi. Reinserirli:");
                        System.out.println("(Valore consigliati: "+(int)Math.sqrt(config.populationNumber*2)+")");
                        System.out.println("Il prodotto delle due grandezze deve essere compreso tra "+(config.populationNumber*10)+" e "+(config.populationNumber/10)+")");
                        System.out.print("Larghezza: "); width = Integer.parseInt(Input.nextLine());
                        System.out.print("Altezza: "); heigth = Integer.parseInt(Input.nextLine());

                    }
                    // config.size[0] = width;  config.size[1] = heigth;      
                }*/
                break;
            

                case(2):
                // Durata della malattia
                clear();
                ScreenSET();
                System.out.println("Inserire la durata della malattia (max 90 giorni):");
                int diseaseDuration = Integer.parseInt(Input.nextLine());
                while(diseaseDuration > 90) {
                    clear();
                    ScreenSET();
                    System.out.println("Numero troppo grande. Reinserirlo:");
                    diseaseDuration = Integer.parseInt(Input.nextLine());  
                }
                config.diseaseDuration = diseaseDuration;
                // cambiando la durata della malattia controllo se il numero delle risorse iniziali
                // sia ancora buono
                if(config.initialResources > config.populationNumber * config.diseaseDuration) {
                    clear();
                    ScreenSET();
                    System.out.println("ATTENZIONE! il numero delle risorse iniziali non è più valido:");
                    System.out.println("Inserire il numero delle risorse iniziali:");
                    System.out.println("(deve essere minore di "+(config.populationNumber*config.diseaseDuration)+")");
                    int initialResource = Integer.parseInt(Input.nextLine());
                    while(initialResource >= config.populationNumber * config.diseaseDuration) {
                        clear();
                        ScreenSET();
                        System.out.println("Numero troppo grande. Reinserirlo:");
                        System.out.println("(deve essere minore di "+(config.populationNumber*config.diseaseDuration)+")");
                        initialResource = Integer.parseInt(Input.nextLine());  
                    }
                    config.initialResources = initialResource;
                }
                break;


                case(3):
                // Risorse iniziali
                clear();
                ScreenSET();
                System.out.println("Inserire il numero delle risorse iniziali:");
                System.out.println("(deve essere minore di "+(config.populationNumber*config.diseaseDuration)+")");
                int initialResource = Integer.parseInt(Input.nextLine());
                while(initialResource >= config.populationNumber * config.diseaseDuration) {
                    clear();
                    ScreenSET();
                    System.out.println("Numero troppo grande. Reinserirlo:");
                    System.out.println("(deve essere minore di "+(config.populationNumber*config.diseaseDuration)+")");
                    initialResource = Integer.parseInt(Input.nextLine());  
                }
                config.initialResources = initialResource;
                // cambiando il numero delle risorse iniziali controllo se il costo del tampone sia ancora buono
                if(config.swabsCost > config.initialResources/(config.populationNumber * 10)) {
                    clear();
                    ScreenSET();
                    System.out.println("ATTENZIONE! il costo del tampone non è più valido:");
                    System.out.println("Inserire il costo del tampone:");
                    System.out.println("(deve essere maggiore di "+(config.initialResources/(config.populationNumber*10))+")");
                    int swabsCost = Integer.parseInt(Input.nextLine());
                    while(swabsCost <= config.initialResources/(config.populationNumber * 10)) {
                        clear();
                        ScreenSET();
                        System.out.println("Numero troppo piccolo. Reinserirlo:");
                        System.out.println("(deve essere maggiore di "+(config.initialResources/(config.populationNumber*10))+")");
                        swabsCost = Integer.parseInt(Input.nextLine());  
                    }
                    config.swabsCost = swabsCost;
                }
                break;


                case(4):
                // Costo del tampone
                clear();
                ScreenSET();
                System.out.println("Inserire il costo del tampone:");
                System.out.println("(deve essere maggiore di "+(config.initialResources/(config.populationNumber*10))+")");
                int swabsCost = Integer.parseInt(Input.nextLine());
                while(swabsCost <= config.initialResources/(config.populationNumber * 10)) {
                    clear();
                    ScreenSET();
                    System.out.println("Numero troppo piccolo. Reinserirlo:");
                    System.out.println("(deve essere maggiore di "+(config.initialResources/(config.populationNumber*10))+")");
                    swabsCost = Integer.parseInt(Input.nextLine());  
                }
                config.swabsCost = swabsCost;
                break;


                case(5):
                // Infettività
                clear();
                ScreenSET();
                System.out.println("Inserire la percentuale di infettivita' :");
                System.out.println("(deve essere maggiore di 0)");
                Double infectivity = Double.parseDouble(Input.nextLine());
                while(infectivity <= 0.0 || infectivity > 100.0) {
                    clear();
                    ScreenSET();
                    System.out.println("Percentuale non valida. Reinserirla:");
                    System.out.println("(deve essere maggiore di 0)");
                    infectivity = Double.parseDouble(Input.nextLine()); 
                }
                config.infectivity= infectivity;
                break;


                case(6):
                // Sintomaticità
                clear();
                ScreenSET();
                System.out.println("Inserire la percentuale di sintomaticita' :");
                System.out.println("(deve essere maggiore di 0)");
                Double sintomaticity = Double.parseDouble(Input.nextLine()); 
                while(sintomaticity <= 0.0 || sintomaticity > 100.0) {
                    clear();
                    ScreenSET();
                    System.out.println("Percentuale non valida. Reinserirla:");
                    System.out.println("(deve essere maggiore di 0)");
                    sintomaticity = Double.parseDouble(Input.nextLine());  
                }
                config.sintomaticity = sintomaticity;
                break;


                case(7):
                // Letalità
                clear();
                ScreenSET();
                System.out.println("Inserire la percentuale di letalita' :");
                System.out.println("(deve essere maggiore di 0)");
                Double letality = Double.parseDouble(Input.nextLine()); 
                while(letality <= 0.0 || letality > 100.0) {
                    clear();
                    ScreenSET();
                    System.out.println("Percentuale non valida. Reinserirla:");
                    System.out.println("(deve essere maggiore di 0)");
                    letality = Double.parseDouble(Input.nextLine());  
                }
                config.letality = letality;
                break;


                case(8):
                // Età massima
                clear();
                ScreenSET();
                System.out.println("Inserire l'eta' massima (compresa tra 50 e 100): ");
                int maxAge = Integer.parseInt(Input.nextLine());
                while(maxAge < 50 || maxAge > 100) {
                    clear();
                    ScreenSET();
                    System.out.println("Eta' non valida. Reinserirla (compresa tra 50 e 100): ");
                    maxAge = Integer.parseInt(Input.nextLine());  
                }
                config.maxAge = maxAge;
                break;


                case(9):
                // Età minima
                clear();
                ScreenSET();
                System.out.println("Inserire l'eta' media (compresa tra 20 e 80) :");
                int ageAverage = Integer.parseInt(Input.nextLine());
                while(ageAverage < 20 || ageAverage > 80) {
                    clear();
                    ScreenSET();
                    System.out.println("Eta' non valida. Reinserirla (compresa tra 20 e 80):");
                    ageAverage = Integer.parseInt(Input.nextLine());  
                }
                config.ageAverage = ageAverage;
                break;


                /*case(10):
                clear(); //TOGLIERE IL COMMENTO
                ScreenSET();
                System.out.println("Inserire l'altezza e la larghezza dello spazio grafico dove verrà visualizzata la simulazione");
                System.out.println("(Valore consigliati: "+(int)Math.sqrt(config.populationNumber*2)+")");
                System.out.println("Il prodotto devve essere compreso tra "+(config.populationNumber*10)+" e "+(config.populationNumber/10)+")");
                System.out.print("Larghezza: "); int width = Integer.parseInt(Input.nextLine());
                System.out.print("Altezza: "); int heigth = Integer.parseInt(Input.nextLine());
                while((width*heigth > config.populationNumber*10)||(width*heigth < config.populationNumber/10)) {
                    clear();
                    ScreenSET();
                    System.out.println("Parametri non validi. Reinserirli:");
                    System.out.println("(Valore consigliati: "+(int)Math.sqrt(config.populationNumber*2)+")");
                    System.out.println("Il prodotto delle due grandezze deve essere compreso tra "+(config.populationNumber*10)+" e "+(config.populationNumber/10)+")");
                    System.out.print("Larghezza: "); width = Integer.parseInt(Input.nextLine());
                    System.out.print("Altezza: "); heigth = Integer.parseInt(Input.nextLine());
                }
                // config.size[0] = width;  config.size[1] = heigth;
                break;
                */


                case(0):
                break;
            }
        }
        return 1; 
    }

    @Override
    public void feedback(State state){
        System.out.println("GIORNO " + state.currentDay);

        if(state.unoPatientFound) System.out.println("Paziente zero trovato");
        else System.out.println("Paziente zero non ancora trovato");

        System.out.println("Popolazione sana " + state.getHealthyNumber());
        System.out.println("Popolazione guarita " + state.getCuredNumber());


        System.out.print("Popolazione infettata " + state.totalInfected.get(state.totalInfected.size()-1));
        System.out.println(" (" + state.dailyInfected.get(state.dailyInfected.size()-1) +" in più rispetto a ieri)");


        System.out.println("Morti " + state.getDeathsNumber());
        System.out.println("Risorse disponibili " + state.resources);
    }

    @Override
    public void finalFeedback(State state) {
        System.out.println("GIORNI PASSATI " + state.currentDay);
        System.out.println("Popolazione sana " + state.getHealthyNumber());
        System.out.println("Popolazione guarita " + state.getCuredNumber());
        System.out.println("Popolazione infettata " + state.totalInfected.get(state.totalInfected.size()-1));
        System.out.println("Morti " + state.getDeathsNumber());  
        System.out.println("Risorse rimaste " + state.resources);
    }

    @NotImplemented
    @Override
    public Scenario selectScenario() {
        return null;
    }

}
