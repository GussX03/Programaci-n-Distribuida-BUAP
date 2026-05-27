package ejemplo;

import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;

public class ChatClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;
    private BufferedReader in;
    private PrintWriter out;
    private JFrame frame = new JFrame("Pizarra Electrónica");
    private JTextArea messageArea = new JTextArea(15, 40);
    private JTextField textField = new JTextField(30);
    private JButton sendButton = new JButton("Enviar");

    public ChatClient() {
        messageArea.setEditable(false);
        messageArea.setLineWrap(true);

        // Panel inferior con campo de texto y botón
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(textField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);

        frame.getContentPane().add(new JScrollPane(messageArea), BorderLayout.CENTER);
        frame.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Fuerza el foco al campo de texto al iniciar
        textField.requestFocusInWindow();

        // Enviar con Enter
        textField.addActionListener(e -> enviarMensaje());

        // Enviar con botón
        sendButton.addActionListener(e -> enviarMensaje());
    }

    private void enviarMensaje() {
        String texto = textField.getText().trim();
        if (out != null && !texto.isEmpty()) {
            out.println(texto);
            textField.setText("");
            textField.requestFocusInWindow();
        }
    }

    private String getName() {
        return JOptionPane.showInputDialog(
                frame,
                "Elige un nombre de usuario:",
                "Selección de Nombre",
                JOptionPane.PLAIN_MESSAGE
        );
    }

    private void run() throws IOException {
        Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        while (true) {
            String line = in.readLine();
            if (line == null) {
                messageArea.append("¡El servidor cerró la conexión!\n");
                break;
            }
            if (line.startsWith("SUBMITNAME")) {
                out.println(getName());
            } else if (line.startsWith("MESSAGE")) {
                String msg = line.substring(8);

                SwingUtilities.invokeLater(() -> {
                    messageArea.append(msg + "\n");
                    if (msg.contains("Pizarra borrada")) {
                        messageArea.setText("");
                    }
                });
            }
        }
        socket.close();
    }

    public static void main(String[] args) throws Exception {
        ChatClient client = new ChatClient();
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        new Thread(() -> {
            try {
                client.run();
            } catch (IOException e) {
                client.messageArea.append("Error de conexión: " + e.getMessage() + "\n");
            }
        }).start();
    }
}