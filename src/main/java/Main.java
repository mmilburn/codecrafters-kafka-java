import log.RecordBatch;
import requests.Request;
import responses.Response;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static final ExecutorService executorService = Executors.newFixedThreadPool(8);
    private static List<RecordBatch> batches = new ArrayList<>();

    public static void main(String[] args) {
        int port = 9092;
        if (!args[0].isEmpty()) {
            Path propertiesPath = Path.of(args[0].trim());
            try {
                String logDirs = Files.readAllLines(propertiesPath).stream()
                        .filter(line -> line.startsWith("log.dir"))
                        .findFirst().orElse("");
                List<Path> logFiles = Arrays.stream(logDirs.split("=")[1].trim().split(","))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .flatMap(path -> {
                            try {
                                return Files.walk(Path.of(path));
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .filter(Files::isRegularFile)
                        .filter(path -> path.toString().endsWith(".log")).toList();
                batches = getRecordBatches(logFiles);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            // Since the tester restarts your program quite often, setting SO_REUSEADDR
            // ensures that we don't run into 'Address already in use' errors
            serverSocket.setReuseAddress(true);
            //noinspection InfiniteLoopStatement
            while (true) {
                // Wait for connection from client.
                Socket clientSocket = serverSocket.accept();
                executorService.submit(() -> handleClient(clientSocket));
            }
        } catch (IOException e) {
            System.err.println("IOException: " + Arrays.toString(e.getStackTrace()));
        } finally {
            executorService.shutdown();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
             OutputStream os = clientSocket.getOutputStream()) {
            //noinspection InfiniteLoopStatement
            while (true) {
                if (dis.available() > 0) {
                    int len = dis.readInt();
                    byte[] requestBytes = new byte[len];
                    dis.readFully(requestBytes);
                    ByteBuffer requestBuffer = ByteBuffer.wrap(requestBytes);
                    Request<?> request = Request.fromByteBuffer(requestBuffer);
                    Response response = new Response(request, batches);
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
                System.err.println("IOException: " + Arrays.toString(e.getStackTrace()));
            }
        }
    }

    private static List<RecordBatch> getRecordBatches(List<Path> logFiles) {
        List<RecordBatch> batches = new ArrayList<>();
        for (Path logPath : logFiles) {
            try (FileChannel fileChannel = FileChannel.open(logPath, StandardOpenOption.READ)) {
                ByteBuffer data = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());
                while (data.hasRemaining()) {
                    RecordBatch batch = RecordBatch.fromByteBuffer(data);
                    batches.add(batch);
                }
            } catch (IOException ioNo) {
                System.err.println("IOException: " + Arrays.toString(ioNo.getStackTrace()));
            }
        }
        return batches;
    }

}
