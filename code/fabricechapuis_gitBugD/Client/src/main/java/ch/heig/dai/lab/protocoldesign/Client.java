package ch.heig.dai.lab.protocoldesign;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Scanner;

public class Client {
    final String SERVER_ADDRESS = "1.2.3.4";
    final int SERVER_PORT = 1234;
    Socket socket;
    OutputStream os;
    OutputStreamWriter OsWriter;
    BufferedReader in;
    BufferedWriter out;
    

    public static void main(String[] args) {
        // Create a new client and run it
        Client client = new Client();
        client.connect();
        client.run();
        client.disconnect();

    }
    private void connect() {
        try {
            this.socket = new Socket("localhost", this.SERVER_PORT);
            this.os = this.socket.getOutputStream();
            this.OsWriter = new OutputStreamWriter(this.os, StandardCharsets.UTF_8);
            this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.out = new BufferedWriter(new OutputStreamWriter(this.os));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void disconnect() {
        try {
        this.socket.close();
        this.os.close();
        this.in.close();
        this.OsWriter.close();

        } catch (IOException e) {
        e.printStackTrace();
        }
    }

    private boolean sendMessage(String message) {
        try {
            this.out.write(message + "\n");
            this.out.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String getMessage() {
        try {
            String finalMessage = "";
            while (true) {
                String msg = this.in.readLine().trim();
                msg += "\n";
                String[] words = msg.split(" ");

                if (words[words.length - 1].equals("END\n")){
                    words[words.length - 1] = "\n";
                    finalMessage += String.join(" ", words);
                    return finalMessage;
                } else {
                    finalMessage += msg;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void initiateConversation() {
            // Getting welcome message
            String response = getMessage();
            System.out.print(response);

    }
    private void run() {
            initiateConversation();

            Scanner input = new Scanner(System.in);
            String operation = "";
            try {
                while (!Objects.equals(operation, "STOP")) {
                    System.out.println("Enter an operation: ");
                    operation = input.nextLine();

                    if (this.sendMessage(operation)) {
                        String response = getMessage();
                        System.out.print("Response: " + response);
                    } else {
                        throw(new RuntimeException("Message not sent"));
                    }
                }
            } finally {
                input.close();
            }
    }
}