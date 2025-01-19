import java.io.*;
import java.net.*;

public class ChatClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 12345)) {
            System.out.println("Connected to the server!");

            // Input and output streams for communication
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

            // Thread to handle server messages
            Thread serverHandler = new Thread(() -> {
                try {
                    String message;
                    while ((message = input.readLine()) != null) {
                        System.out.println("Server: " + message);
                        if (message.equalsIgnoreCase("bye")) {
                            System.out.println("Server ended the chat.");
                            break;
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Connection to server lost.");
                }
            });

            serverHandler.start();

            // Client's messages
            BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
            String clientMessage;
            while ((clientMessage = consoleInput.readLine()) != null) {
                output.println(clientMessage);
                if (clientMessage.equalsIgnoreCase("bye")) {
                    System.out.println("Chat ended by client.");
                    break;
                }
            }

            socket.close();
            System.out.println("Client disconnected.");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
