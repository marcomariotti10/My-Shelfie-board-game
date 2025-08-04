package it.polimi.ingsw.client;

import it.polimi.ingsw.client.CLI.CLIcontroller;
import it.polimi.ingsw.client.GUI.GUI;

public class ClientMain {

    /**
     * The entry point of the client application.
     * @author Andrea Gollo, Francesco Foresti
     */
    public static void main(String[] args) {
        String input;
        boolean flag = Boolean.TRUE;

        while(flag){
            System.out.println("Select the interface you wish to use:\n1) CLI\n2) GUI");
            input = CLIcontroller.getInput();
            do {
                try {
                    Thread.sleep(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (input == null);
            if(input.equals("1")){
                flag = Boolean.FALSE;
                ClientCLI clientCLI = new ClientCLI();
                clientCLI.start();
            }
            else if(input.equals("2")){
                flag = Boolean.FALSE;
                GUI.launch(GUI.class);
            }
            else{
                System.err.println("Your input is not valid, select a number between 1 and 2\n");
                input = null;
            }
        }
    }
}
