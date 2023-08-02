package org.bambrikii.tiny.db;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Function;

public class Net {
    public static final int BUFFER_SIZE = 1024;
    private final int port;

    public Net(int port) {
        this.port = port;
    }

    public void listen(Function<String, String> func) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("Server is listening on port " + port);

            String text = "";
            while (notExit(text)) {
                try (
                        Socket socket = serverSocket.accept();
                        InputStream input = socket.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                        OutputStream output = socket.getOutputStream();
                        PrintWriter writer = new PrintWriter(output, true);
                ) {
                    System.out.println("New client connected");

                    do {
                        text = reader.readLine();
                        String response = func.apply(text);
                        writer.println("Server: " + response);

                    } while (notExit(text));
                }
            }

        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private static boolean notExit(String text) {
        return !text.equals("v1:exit");
    }
}
