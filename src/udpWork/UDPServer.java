package udpWork;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;

public class UDPServer {
    private ActiveUsers userList = null;
    private DatagramSocket socket = null;
    private DatagramPacket packet = null;
    private InetAddress address = null;
    private int port = -1;

    public UDPServer(int serverPort) {
        try {
            socket = new DatagramSocket(serverPort);
        } catch (SocketException e) {
            System.out.println("Error: " + e);
        }
        userList = new ActiveUsers();
    }
    public void work(int bufferSize) {
        try {
            System.out.println("Server start...");
            while (true) { // безкінечний цикл роботи з клієнтами
                if(!getUserData(bufferSize)) return; // отримання запиту клієнта
                log(address, port); // вивід інформації про клієнта на екран
                sendUserData(); // формування та відправка відповіді
                // клієнту
            }
        } catch(IOException e) {
            System.out.println("Error: " + e);
        } finally {
            System.out.println("Server end...");
            socket.close();
        }
    }
    private void log(InetAddress address, int port) {
        System.out.println("Request from: " + address.getHostAddress() +
                " port: " + port);
    }
    private boolean getUserData(int bufferSize) throws IOException {
        byte[] buffer = new byte[bufferSize];
        packet = new DatagramPacket(buffer, buffer.length);
        System.out.println(packet.getLength());
        socket.receive(packet);
        if (packet.getLength()==0) return false;

        address = packet.getAddress();
        port = packet.getPort();
        User usr = new User(port, address);
        if (userList.isEmpty()) {
            userList.add(usr);
        } else if (!userList.contains(usr)) {
            userList.add(usr);
        }
        clear(buffer);
        return true;
    }
    private void clear(byte[] buffer){
        Arrays.fill(buffer, (byte) 0);
    }
    private void sendUserData() throws IOException {
        byte[] buffer;
        for (int i = 0; i < userList.size(); i++) {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bout);
            out.writeObject(userList.get(i));
            buffer = bout.toByteArray();
            packet = new DatagramPacket(buffer, buffer.length, address, port);
            socket.send(packet);
        }
        buffer = "end".getBytes();
        packet = new DatagramPacket(buffer, 0, address, port);
        socket.send(packet);
    }
    public static void main(String[] args) {
        (new UDPServer(1501)).work(256);
    }
}
