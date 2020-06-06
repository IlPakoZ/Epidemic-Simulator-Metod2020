package sys.applications;
import sys.Config;
import sys.Core.*;
import sys.Simulation;
import sys.State;
import sys.models.IMenu;
import java.util.Scanner;
import java.io.IOException;

public class CommandLineMenu implements IMenu {

    public void clearScreen() {  
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor(); 
        } catch (InterruptedException e) { } 
        catch (IOException s) { }  
    }

    public void clearScreenFI() {  
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor(); 
        } catch (InterruptedException e) { } 
        catch (IOException s) { }  
        System.out.println("CONFIGURAZIONE INIZIALE");
        System.out.println("#");
        System.out.println("#");   
    }

    public void clearScreenSET() {  
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor(); 
        } catch (InterruptedException e) { } 
        catch (IOException s) { }  
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
        System.out.println("9 - Eta' minima");
        System.out.println("0 - Per uscire");
   
    }

    @Override
    public int show() { 
        Scanner Input = new Scanner(System.in);

        System.out.println("MENU' PRINCIPALE");
        System.out.println("#");
        System.out.println("#");
        System.out.println("Per far partire la simulazione digitare 0. Per configurare le impostazioni inziali digitare 2:");
        int action = Integer.parseInt(Input.nextLine()); 
        while(action != 0 && action != 2) {
            System.out.println("Parametro non valido. Reinserirlo:");
            action = Integer.parseInt(Input.nextLine());  
        }
        clearScreen();
        return action; 
    }

    @Override
    public void firstInput(Config config) {
        Scanner Input = new Scanner(System.in);

        clearScreenFI();
        // Popolazione iniziale
        System.out.println("Inserire il numero della popolazione iniziale (max 100000):");
        int populationNumber = Integer.parseInt(Input.nextLine());
        while(populationNumber > 100000) {
            clearScreenFI();
            System.out.println("Numero troppo grande. Reinserirlo:");
            populationNumber = Integer.parseInt(Input.nextLine());  
        }
        config.populationNumber = populationNumber;
        // Durata della malattia
        clearScreenFI();
        System.out.println("Inserire la durata della malattia (max 90 giorni):");
        int diseaseDuration = Integer.parseInt(Input.nextLine());
        while(diseaseDuration > 90) {
            clearScreenFI();
            System.out.println("Numero troppo grande. Reinserirlo:");
            diseaseDuration = Integer.parseInt(Input.nextLine());  
        }
        config.diseaseDuration = diseaseDuration;
        // Risorse iniziali
        clearScreenFI();
        System.out.println("Inserire il numero delle risorse iniziali:");
        System.out.println("(deve essere minore del prodotto fra popolazione e durata della malattia)");
        int initialResource = Integer.parseInt(Input.nextLine());
        while(initialResource > config.populationNumber * config.diseaseDuration) {
            clearScreenFI();
            System.out.println("Numero troppo grande. Reinserirlo:");
            System.out.println("(deve essere minore del prodotto fra popolazione e durata della malattia)");
            initialResource = Integer.parseInt(Input.nextLine());  
        }
        config.initialResources = initialResource;
        // Costo del tampone
        clearScreenFI();
        System.out.println("Inserire il costo del tampone:");
        System.out.println("(deve essere maggiore di risore/popolazione*10)");
        int swabsCost = Integer.parseInt(Input.nextLine());
        while(swabsCost < config.initialResources/(config.populationNumber * 10)) {
            clearScreenFI();
            System.out.println("Numero troppo piccolo. Reinserirlo:");
            System.out.println("(deve essere maggiore di risore/popolazione*10)");
            swabsCost = Integer.parseInt(Input.nextLine());  
        }
        config.swabsCost = swabsCost;
        // Infettività
        clearScreenFI();
        System.out.println("Inserire la percentuale di infettivita' :");
        System.out.println("(deve essere maggiore di 0)");
        Double infectivity = Double.parseDouble(Input.nextLine()); 
        while(infectivity <= 0.0 || infectivity > 100.0) {
            clearScreenFI();
            System.out.println("Percentuale non valida. Reinserirla:");
            System.out.println("(deve essere maggiore di 0)");
            infectivity = Double.parseDouble(Input.nextLine()); 
        }
        config.infectivity= infectivity;
        // Sintomaticità
        clearScreenFI();
        System.out.println("Inserire la percentuale di sintomaticita' :");
        System.out.println("(deve essere maggiore di 0)");
        Double sintomaticity = Double.parseDouble(Input.nextLine()); 
        while(sintomaticity <= 0.0 || sintomaticity > 100.0) {
            clearScreenFI();
            System.out.println("Percentuale non valida. Reinserirla:");
            System.out.println("(deve essere maggiore di 0)");
            sintomaticity = Double.parseDouble(Input.nextLine());   
        }
        config.sintomaticity = sintomaticity;
        // Letalità
        clearScreenFI();
        System.out.println("Inserire la percentuale di sintomaticita' :");
        System.out.println("(deve essere maggiore di 0)");
        Double letality = Double.parseDouble(Input.nextLine()); 
        while(letality <= 0.0 || letality > 100.0) {
            clearScreenFI();
            System.out.println("Percentuale non valida. Reinserirla:");
            System.out.println("(deve essere maggiore di 0)");
            letality = Double.parseDouble(Input.nextLine());  
        }
        config.letality = letality;
        // Età massima
        clearScreenFI();
        System.out.println("Inserire l'eta' massima (compresa tra 50 e 100) :");
        int maxAge = Integer.parseInt(Input.nextLine());
        while(maxAge < 50 || maxAge > 100) {
            clearScreenFI();
            System.out.println("Eta' non valida. Reinserirla (compresa tra 50 e 100):");
            maxAge = Integer.parseInt(Input.nextLine());  
        }
        config.maxAge = maxAge;
        // Età minima
        clearScreenFI();
        System.out.println("Inserire l'eta' minima (compresa tra 1 e 50) :");
        int meanAge = Integer.parseInt(Input.nextLine());
        while(meanAge < 1 || meanAge > 50) {
            clearScreenFI();
            System.out.println("Eta' non valida. Reinserirla (compresa tra 50 e 100):");
            meanAge = Integer.parseInt(Input.nextLine());  
        }
        config.meanAge = meanAge;
        // Età media
        config.ageAverage = (config.maxAge + config.meanAge)/2;
    }

    @Override
    public int settings(Config config) { 
        Scanner Input = new Scanner(System.in);

        clearScreenSET();
        int action = -1;

        while(action!=0) {
            clearScreenSET();
            action = Integer.parseInt(Input.nextLine());
            switch(action) {


                case(1):
                clearScreenSET();
                // Popolazione iniziale
                System.out.println("Inserire il numero della popolazione iniziale (max 100000):");
                int populationNumber = Integer.parseInt(Input.nextLine());
                while(populationNumber > 100000) {
                    clearScreenSET();
                    System.out.println("Numero troppo grande. Reinserirlo:");
                    populationNumber = Integer.parseInt(Input.nextLine());  
                }
                config.populationNumber = populationNumber;
                // settando un nuovo valore per la popolazione iniziale controllo se 
                // il valore delle risorse inziali sia ancora buono
                if(config.initialResources > config.populationNumber * config.diseaseDuration) {
                    clearScreenSET();
                    System.out.println("ATTENZIONE! il numero delle risorse iniziali non è più valido:");
                    System.out.println("Inserire il numero delle risorse iniziali:");
                    System.out.println("(deve essere minore del prodotto fra popolazione e durata della malattia)");
                    int initialResource = Integer.parseInt(Input.nextLine());
                    while(initialResource > config.populationNumber * config.diseaseDuration) {
                        clearScreenSET();
                        System.out.println("Numero troppo grande. Reinserirlo:");
                        System.out.println("(deve essere minore del prodotto fra popolazione e durata della malattia)");
                        initialResource = Integer.parseInt(Input.nextLine());  
                    }
                    config.initialResources = initialResource;
                }
                // e controllo se il valore del costo del tampone sia ancora buono
                if(config.swabsCost > config.initialResources/config.populationNumber * 10) {
                    clearScreenSET();
                    System.out.println("ATTENZIONE! il costo del tampone non è più valido:");
                    System.out.println("Inserire il costo del tampone:");
                    System.out.println("(deve essere maggiore di risore/popolazione*10)");
                    int swabsCost = Integer.parseInt(Input.nextLine());
                    while(swabsCost < config.initialResources/(config.populationNumber * 10)) {
                        clearScreenSET();
                        System.out.println("Numero troppo piccolo. Reinserirlo:");
                        System.out.println("(deve essere maggiore di risore/popolazione*10)");
                        swabsCost = Integer.parseInt(Input.nextLine());  
                    }
                    config.swabsCost = swabsCost;
                }
                break;


                case(2):
                // Durata della malattia
                clearScreenSET();
                System.out.println("Inserire la durata della malattia (max 90 giorni):");
                int diseaseDuration = Integer.parseInt(Input.nextLine());
                while(diseaseDuration > 90) {
                    clearScreenSET();
                    System.out.println("Numero troppo grande. Reinserirlo:");
                    diseaseDuration = Integer.parseInt(Input.nextLine());  
                }
                config.diseaseDuration = diseaseDuration;
                // cambiando la durata della malattia controllo se il numero delle risorse iniziali
                // sia ancora buono
                if(config.initialResources > config.populationNumber * config.diseaseDuration) {
                    clearScreenSET();
                    System.out.println("ATTENZIONE! il numero delle risorse iniziali non è più valido:");
                    System.out.println("Inserire il numero delle risorse iniziali:");
                    System.out.println("(deve essere minore del prodotto fra popolazione e durata della malattia)");
                    int initialResource = Integer.parseInt(Input.nextLine());
                    while(initialResource > config.populationNumber * config.diseaseDuration) {
                        clearScreenSET();
                        System.out.println("Numero troppo grande. Reinserirlo:");
                        System.out.println("(deve essere minore del prodotto fra popolazione e durata della malattia)");
                        initialResource = Integer.parseInt(Input.nextLine());  
                    }
                    config.initialResources = initialResource;
                }
                break;


                case(3):
                // Risorse iniziali
                clearScreenSET();
                System.out.println("Inserire il numero delle risorse iniziali:");
                System.out.println("(deve essere minore del prodotto fra popolazione e durata della malattia)");
                int initialResource = Integer.parseInt(Input.nextLine());
                while(initialResource > config.populationNumber * config.diseaseDuration) {
                    clearScreenSET();
                    System.out.println("Numero troppo grande. Reinserirlo:");
                    System.out.println("(deve essere minore del prodotto fra popolazione e durata della malattia)");
                    initialResource = Integer.parseInt(Input.nextLine());  
                }
                config.initialResources = initialResource;
                // cambiando il numero delle risorse iniziali controllo se il costo del tampone sia ancora buono
                if(config.swabsCost > config.initialResources/config.populationNumber * 10) {
                    clearScreenSET();
                    System.out.println("ATTENZIONE! il costo del tampone non è più valido:");
                    System.out.println("Inserire il costo del tampone:");
                    System.out.println("(deve essere maggiore di risore/popolazione*10)");
                    int swabsCost = Integer.parseInt(Input.nextLine());
                    while(swabsCost < config.initialResources/(config.populationNumber * 10)) {
                        clearScreenSET();
                        System.out.println("Numero troppo piccolo. Reinserirlo:");
                        System.out.println("(deve essere maggiore di risore/popolazione*10)");
                        swabsCost = Integer.parseInt(Input.nextLine());  
                    }
                    config.swabsCost = swabsCost;
                }
                break;


                case(4):
                // Costo del tampone
                clearScreenSET();
                System.out.println("Inserire il costo del tampone:");
                System.out.println("(deve essere maggiore di risore/popolazione*10)");
                int swabsCost = Integer.parseInt(Input.nextLine());
                while(swabsCost < config.initialResources/(config.populationNumber * 10)) {
                    clearScreenSET();
                    System.out.println("Numero troppo piccolo. Reinserirlo:");
                    System.out.println("(deve essere maggiore di risore/popolazione*10)");
                    swabsCost = Integer.parseInt(Input.nextLine());  
                }
                config.swabsCost = swabsCost;
                break;


                case(5):
                // Infettività
                clearScreenSET();
                System.out.println("Inserire la percentuale di infettivita' :");
                System.out.println("(deve essere maggiore di 0)");
                Double infectivity = Double.parseDouble(Input.nextLine());
                while(infectivity <= 0.0 || infectivity > 100.0) {
                    clearScreenSET();
                    System.out.println("Percentuale non valida. Reinserirla:");
                    System.out.println("(deve essere maggiore di 0)");
                    infectivity = Double.parseDouble(Input.nextLine()); 
                }
                config.infectivity= infectivity;
                break;


                case(6):
                // Sintomaticità
                clearScreenSET();
                System.out.println("Inserire la percentuale di sintomaticita' :");
                System.out.println("(deve essere maggiore di 0)");
                Double sintomaticity = Double.parseDouble(Input.nextLine()); 
                while(sintomaticity <= 0.0 || sintomaticity > 100.0) {
                    clearScreenSET();
                    System.out.println("Percentuale non valida. Reinserirla:");
                    System.out.println("(deve essere maggiore di 0)");
                    sintomaticity = Double.parseDouble(Input.nextLine());  
                }
                config.sintomaticity = sintomaticity;
                break;


                case(7):
                // Letalità
                clearScreenSET();
                System.out.println("Inserire la percentuale di sintomaticita' :");
                System.out.println("(deve essere maggiore di 0)");
                Double letality = Double.parseDouble(Input.nextLine()); 
                while(letality <= 0.0 || letality > 100.0) {
                    clearScreenSET();
                    System.out.println("Percentuale non valida. Reinserirla:");
                    System.out.println("(deve essere maggiore di 0)");
                    letality = Double.parseDouble(Input.nextLine());  
                }
                config.letality = letality;
                break;


                case(8):
                // Età massima
                clearScreenSET();
                System.out.println("Inserire l'eta' massima (compresa tra 50 e 100) :");
                int maxAge = Integer.parseInt(Input.nextLine());
                while(maxAge < 50 || maxAge > 100) {
                    clearScreenSET();
                    System.out.println("Eta' non valida. Reinserirla (compresa tra 50 e 100):");
                    maxAge = Integer.parseInt(Input.nextLine());  
                }
                config.maxAge = maxAge;
                config.ageAverage = (config.maxAge + config.meanAge)/2;
                break;


                case(9):
                // Età minima
                clearScreenSET();
                System.out.println("Inserire l'eta' minima (compresa tra 1 e 50) :");
                int meanAge = Integer.parseInt(Input.nextLine());
                while(meanAge < 1 || meanAge > 50) {
                    clearScreenSET();
                    System.out.println("Eta' non valida. Reinserirla (compresa tra 1 e 50):");
                    meanAge = Integer.parseInt(Input.nextLine());  
                }
                config.meanAge = meanAge;
                config.ageAverage = (config.maxAge + config.meanAge)/2;
                break;


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
        System.out.println("Popolazione infetta " + state.totalInfected.size());
        System.out.println("Risorse disponibili " + state.resources);
    }

    @Override
    public void finalFeedback(State state) {
        System.out.println("GIORNI PASSATI " + state.currentDay);
        System.out.println("Popolazione infettata " + state.totalInfected.size());
        System.out.println("Risorse rimaste " + state.resources);
    }



}
