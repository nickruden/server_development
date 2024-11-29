package Lab4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

public class client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 6000;

    public static void main(String[] args) {
        client client = new client();
        client.run();
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Нажмите Enter, чтобы узнать погоду, или введите 'exit' для выхода:");
            String input = scanner.nextLine();
            if ("exit".equalsIgnoreCase(input)) {
                break;
            }
            try {
                String message = getLastMessage();
                System.out.println("Прогноз погоды: " + message);
            } catch (IOException e) {
                System.err.println("Ошибка при получении данных от сервера: " + e.getMessage());
            }
        }
        scanner.close();
    }

    private String getLastMessage() throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            return in.readLine();
        }
    }
}