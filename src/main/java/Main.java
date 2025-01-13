import requests.Request;
import responses.Response;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        int port = 9092;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            // Since the tester restarts your program quite often, setting SO_REUSEADDR
            // ensures that we don't run into 'Address already in use' errors
            serverSocket.setReuseAddress(true);
            //noinspection InfiniteLoopStatement
            while (true) {
                // Wait for connection from client.
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            System.out.println("IOException: " + Arrays.toString(e.getStackTrace()));
        }
    }

    public static void handleClient(Socket clientSocket) {
        try (DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
             OutputStream os = clientSocket.getOutputStream()) {
            //noinspection InfiniteLoopStatement
            while (true) {
                if (dis.available() > 0) {
                    int len = dis.readInt();
                    byte[] requestBytes = new byte[len];
                    dis.readFully(requestBytes);
                    ByteBuffer requestBuffer = ByteBuffer.wrap(requestBytes);
                    Request request = Request.fromByteBuffer(requestBuffer);
                    Response response = new Response(request);
                    os.write(response.toBytes());
                    os.flush();
                }
            }
        } catch (IOException e) {
            System.err.println(Arrays.toString(e.getStackTrace()));
        } finally {
            try {
                if (clientSocket != null) {
                    clientSocket.close();
                }
            } catch (IOException e) {
                System.out.println("IOException: " + Arrays.toString(e.getStackTrace()));
            }
        }
    }
}
