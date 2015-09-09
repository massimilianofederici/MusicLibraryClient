package massimiliano.test;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class TCPClient {

    public static final String SERVERIP = "192.168.0.4";
    public static final int SERVERPORT = 10000;
    private OnMessageReceived mMessageListener = null;

    public TCPClient(OnMessageReceived listener) {
        mMessageListener = listener;
    }

    public void sendMessage(JSONObject message) {
        Socket socket = connect();
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            out.println(message.toString());
            out.flush();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String serverMessage = in.readLine();
            if (serverMessage != null && mMessageListener != null) {
                mMessageListener.messageReceived(new JSONArray(serverMessage));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            disconnect(socket);
        }
    }

    public Socket connect() {
        try {
            InetAddress serverAddr = InetAddress.getByName(SERVERIP);
            Socket socket = new Socket(serverAddr, SERVERPORT);
            return socket;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void disconnect(Socket socket) {
        if (socket != null) {
            try {
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public interface OnMessageReceived {

        void messageReceived(JSONArray message);
    }
}