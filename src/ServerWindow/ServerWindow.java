package ServerWindow;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
public class ServerWindow extends JFrame {
    private HttpServer server;
    private JButton startButton;
    private JButton stopButton;

    public ServerWindow() {
        setTitle("Java GUI Server");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        startButton = new JButton("Start Server");
        stopButton = new JButton("Stop Server");
        stopButton.setEnabled(false);

        startButton.addActionListener(e -> startServer());
        stopButton.addActionListener(e -> stopServer());

        add(startButton);
        add(stopButton);
    }

    private void startServer() {
        try {
            server = HttpServer.create(new InetSocketAddress(8000), 0);
            server.createContext("/", new MyHandler());
            server.setExecutor(null); // Creates a default executor
            server.start();

            startButton.setEnabled(false);
            stopButton.setEnabled(true);
            System.out.println("Server started on port 8000");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopServer() {
        if (server != null) {
            server.stop(0);
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
            System.out.println("Server stopped");
        }
    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "Hello, this is the server!";
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ServerWindow gui = new ServerWindow();
            gui.setVisible(true);
        });
    }
}
