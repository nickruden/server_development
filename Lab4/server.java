package Lab4;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class server {
    private static final String GROUP_ADDRESS = "233.0.0.1";
    private static final int PORT = 1502;
    private static final String MESSAGE_FILE = "C:\\MyFiles\\University\\server development\\Lab4\\weather.txt";
    private static int messageIndex = 0;

    // Мультикаст — это способ отправки данных от одного отправителя (поставщика) к группе получателей (подписчиков). Вместо того чтобы отправлять данные каждому получателю по отдельности, отправитель отправляет данные на один общий адрес (мультикаст-адрес). Все устройства, которые подписались на этот адрес (принадлежат мультикаст-группе), получат эти данные.
    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        InetAddress group = InetAddress.getByName(GROUP_ADDRESS); //  получаем объект `InetAddress`, представляющий адрес мультикаст-группы. Это дает возможность нашему сокету знать, куда отправлять сообщения.
        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    List<String> messages = readMessagesFromFile(); // загружает все сообщения из указанного файла в список строк.
                    
                    //  если индекс сообщения превышает количество сообщений, то скидываем индекс на 0, чтобы снова начать с первого сообщения.
                    if (messageIndex >= messages.size()) {
                        messageIndex = 0;
                    }

                    String message = messages.get(messageIndex); // получаем сообщение по существующему индексу.
                    messageIndex++; //  увеличиваем индекс для следующего сообщения.

                    if (message != null) {
                        byte[] buffer = message.getBytes(StandardCharsets.UTF_8); // конвертируем сообщение в массив байт с использованием кодировки UTF-8. Это необходимо, так как сеть работает с двоичными данными (байтами), и текстовые данные должны быть преобразованы в байты.
                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT); // создаём пакет DatagramPacket, который содержит данные (массив байт), его длину, адрес и порт назначения. Этот пакет будет отправлен в мультикаст-группу.
                        socket.send(packet);
                        System.out.println("Message sent: " + message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 1000);

    }

    private static List<String> readMessagesFromFile() throws IOException {
        List<String> messages = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(MESSAGE_FILE));
        String line;
        while ((line = reader.readLine()) != null) {
            messages.add(line);
        }
        return messages;
    }
}