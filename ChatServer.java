import java.io.*;
import java.net.*;

public class ChatServer {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Server is listening on port 12345...");
            Socket socket = serverSocket.accept();
            System.out.println("Client connected!");

            // Input and output streams for communication
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

            // Thread to handle client messages
            Thread clientHandler = new Thread(() -> {
                try {
                    String message;
                    while ((message = input.readLine()) != null) {
                        System.out.println("Client: " + message);
                        if (message.equalsIgnoreCase("bye")) {
                            System.out.println("Client disconnected.");
                            break;
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Client connection lost.");
                }
            });

            clientHandler.start();

            // Server's messages
            BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
            String serverMessage;
            while ((serverMessage = consoleInput.readLine()) != null) {
                output.println(serverMessage);
                if (serverMessage.equalsIgnoreCase("bye")) {
                    System.out.println("Chat ended by server.");
                    break;
                }
            }

            socket.close();
            System.out.println("Server stopped.");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
