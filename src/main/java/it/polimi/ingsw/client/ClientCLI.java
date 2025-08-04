package it.polimi.ingsw.client;

import it.polimi.ingsw.client.CLI.CLIcontroller;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import static it.polimi.ingsw.common.Constants.*;

/**
 * This class represents a client using a Command Line Interface (CLI) for communication.
 * It extends the Thread class to allow concurrent execution.
 * @author Gabriele Marra, Marco Mariotti, Andrea Gollo, Francesco Foresti
 */
public class ClientCLI extends Thread {
    private String hostName;
    private int portNumber;
    private MessageHandler messageHandler;

    /**
     * Selects the socket connection and initializes the necessary objects.
     */
    private void selectSocket() {
        CLIcontroller cliController = new CLIcontroller();
        ClientSocket clientSocket = new ClientSocket(hostName, portNumber, cliController);
        try {
            clientSocket.startingPoint();
            this.messageHandler = clientSocket.getMessageHandler();
            cliController.setMessageHandler(messageHandler);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Selects the RMI connection and initializes the necessary objects.
     */
    private void selectRMI() {
        ClientRMI clientRMI = new ClientRMI(hostName, portNumber);
        try {
            clientRMI.startingRMI();
            this.messageHandler = new MessageHandler(clientRMI.getServerClientHandler());
        } catch (RemoteException | NotBoundException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Prompts the user to enter the server's IP address.
     * Validates the input and returns the IP address.
     *
     * @return The server's IP address.
     */
    private String getIp() {
        boolean valid = false;
        while (!valid) {
            System.out.println("Enter Server's IP Address or press Enter to use the standard one");
            String ip = CLIcontroller.getInput();
            if (ip != null && ip.contains(".")) {
                String[] splittedIp = ip.split("\\.");
                boolean validNumber = true;
                if (splittedIp.length == 4) {
                    for (int i = 0; i < splittedIp.length && validNumber; i++) {
                        try {
                            int number = Integer.parseInt(splittedIp[i]);
                            if (number > 999 || number < 0) {
                                validNumber = false;
                            }
                        } catch (Exception e) {
                            validNumber = false;
                        }
                    }
                    if(validNumber){
                        return ip;
                    } else {
                        System.err.println("IP address must consist of 4 parts with numbers between 0 and 999");
                    }
                }
            } else if (ip == null || ip.isEmpty()) {
                return "127.0.0.1";
            } else {
                System.err.println("IP address must consist of 4 parts with numbers between 0 and 999");
            }
        }
        return null;
    }

    /**
     * Prompts the user to enter the port number.
     * Validates the input and returns the port number.
     *
     * @param in The input indicating the connection type (1 for socket, 2 for RMI).
     * @return The port number.
     */
    private int getPort(String in) {
        while (true) {
            try {
                System.out.println("Enter port number or press Enter to use the standard one");
                String input = CLIcontroller.getInput();
                if (input == null || input.isEmpty()) {
                    if (in.equals("1")) {
                        return SOCKETPORT;
                    } else {
                        return RMIPORT;
                    }
                }
                int port = Integer.parseInt(input);
                if (port >= 1024 && port <= 65535) {
                    return port;
                }
            } catch (Exception ignored) {
            }
            System.err.println("Invalid port number");
        }
    }

    /**
     * Starts the execution of the client.
     * Prompts the user to select the connection type (socket or RMI).
     */
    @Override
    public void run() {
        String input;
        boolean flag = Boolean.TRUE;

        while (flag) {
            System.out.println("Select connection type you wish to use:\n1) Socket\n2) RMI");
            input = CLIcontroller.getInput();
            do {
                try {
                    Thread.sleep(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (input == null);
            if (input.equals("1")) {
                flag = false;
                hostName = getIp();
                portNumber = getPort(input);
                selectSocket();
            } else if (input.equals("2")) {
                flag = false;
                hostName = getIp();
                portNumber = getPort(input);
                selectRMI();
            } else {
                System.err.println("Invalid input. Select a number between 1 and 2\n");
                input = null;
            }
        }
    }
}
