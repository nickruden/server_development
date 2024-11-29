package Lab4;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.Queue;

public class IntermediateServer {
    private static final String GROUP_ADDRESS = "233.0.0.1";
    private static final int UDP_PORT = 1502;
    private static final int TCP_PORT = 6000;
    private static String lastMessage = "";
    private static Queue<String> lastFiveMessages = new LinkedList<>(); // Очередь для хранения последних 5 сообщений

    // В вашем коде промежуточный сервер не "понимает", что к нему обратился именно клиент в том смысле, как это обычно понимается в клиент-серверной архитектуре.  Он просто пассивно ожидает соединения по TCP-порту и отправляет последнее полученное сообщение любому клиенту, который подключится.  Нет никакой аутентификации или идентификации клиента. Как клиент обращается к промежуточному серверу: Клиент в вашем случае устанавливает TCP-соединение с промежуточным сервером, используя его IP-адрес и порт (в вашем коде это TCP_PORT = 6000).  Клиент не отправляет никаких данных на промежуточный сервер — он лишь устанавливает соединение, после чего сервер отправляет ему данные.  Это подход "публикация-подписка" —  сервер публикует данные, а клиенты подписываются на эти данные (путем подключения).
    public static void main(String[] args) throws IOException {
        Thread udpThread = new Thread(() -> {
            try (MulticastSocket socket = new MulticastSocket(UDP_PORT)) {

                // Получаем адрес мультикаст-группы, к которой нужно присоединиться, и присоединяемся к ней.
                InetAddress group = InetAddress.getByName(GROUP_ADDRESS);
                socket.joinGroup(group);
                byte[] buffer = new byte[256];

                while (true) {
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length); // Это класс, который используется для передачи данных через сокеты с использованием протокола UDP (User Datagram Protocol). Он представляет пакет данных, который передается между узлами сети. buffer: Это массив байтов, в который будут помещены данные, полученные из пакета.
                    socket.receive(packet); // Здесь мы ожидаем получения UDP-пакета. Метод `receive` блокирует выполнение кода до тех пор, пока не будет получен пакет. Когда пакет будет получен, данные будут записаны в `buffer` внутри объекта `packet`.
                    
                    String receivedMessage = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8); // Преобразуем полученные данные из массива байт в строку с использованием кодировки UTF-8.

                    // Если сообщение отличается от последнего полученного (`lastMessage`), выводим его на экран, обновляем `lastMessage` и добавляем сообщение в очередь `lastFiveMessages`.
                    if (!receivedMessage.equals(lastMessage)) {
                        System.out.println("New message: " + receivedMessage);
                        lastMessage = receivedMessage;
                        addMessageToQueue(receivedMessage);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        udpThread.start();

        try (ServerSocket serverSocket = new ServerSocket(TCP_PORT)) {
            System.out.println("Intermediate server is running...");

            while (true) {
                Socket clientSocket = serverSocket.accept(); // Этот метод блокирует выполнение, пока не произойдёт входящее соединение. После того как клиент подключится, он создаст объект `Socket`, который будет представлять это соединение.
                try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) { // Этот объект используется для отправки текстовых данных клиенту через его выходной поток. Мы создаем `PrintWriter`, который оборачивается вокруг выходного потока сокета 
                    String lastMessage = getLastMessage();
                    out.println(lastMessage); // Здесь мы отправляем последнее сообщение клиенту, используя `PrintWriter`.
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // добавляет новое сообщение в очередь, а если очередь уже содержит 5 сообщений, удаляет старейшее.
    private static void addMessageToQueue(String message) {
        if (lastFiveMessages.size() >= 5) {
            lastFiveMessages.poll();
        }
        lastFiveMessages.add(message);
    }

    public static String getLastMessage() {
        return lastMessage;
    }
}