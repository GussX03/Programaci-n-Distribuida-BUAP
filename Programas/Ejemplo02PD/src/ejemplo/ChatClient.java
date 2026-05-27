package ejemplo;

import java.io.*;
import java.net.*;
import javax.swing.*; // Para usar JOptionPane para el nombre de usuario

public class ChatClient {
    private static final String SERVER_ADDRESS = "localhost"; // La IP donde corre el servidor
    private static final int SERVER_PORT = 1234;

    private BufferedReader in;
    private PrintWriter out;
    private JFrame frame = new JFrame("Chat Client");
    private JTextArea messageArea = new JTextArea(8, 40);
    private JTextField textField = new JTextField(40);

    public ChatClient() {
        // Configuración de la interfaz gráfica básica
        messageArea.setEditable(false);
        frame.getContentPane().add(new JScrollPane(messageArea), "Center");
        frame.getContentPane().add(textField, "South");
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        textField.addActionListener(e -> {
            out.println(textField.getText()); // Envía el texto del campo al servidor
            textField.setText(""); // Limpia el campo
        });
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

        // Bucle para procesar mensajes del servidor
        while (true) {
            String line = in.readLine();
            if (line == null) {
                // Si la línea es null, el servidor cerró la conexión
                messageArea.append("¡El servidor cerró la conexión!\n");
                break;
            }
            if (line.startsWith("SUBMITNAME")) {
                out.println(getName()); // El servidor pide el nombre
            } else if (line.startsWith("MESSAGE")) {
                messageArea.append(line.substring(8) + "\n"); // Muestra el mensaje del chat
            }
        }
        socket.close(); // Cierra el socket cuando termina el bucle
    }

    public static void main(String[] args) throws Exception {
        ChatClient client = new ChatClient();
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.run();
    }
}
