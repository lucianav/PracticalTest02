package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Luciana on 18/05/2017.
 */

public class ClientThread extends Thread {

    private String address;
    private int port;
    EditText queryText;

    private Socket socket;

    public ClientThread(String address, int port, EditText queryText) {
        this.address = address;
        this.port = port;
        this.queryText = queryText;
    }

    @Override
    public void run() {
        try {
            Log.v(Constants.TAG, "in client thread ");
            socket = new Socket(address, port);
            if (socket == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Could not create socket!");
                return;
            }
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            if (bufferedReader == null || printWriter == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Buffered Reader / Print Writer are null!");
                return;
            }
            printWriter.println(queryText);
            printWriter.flush();
            String result = null;
            while ((result = bufferedReader.readLine()) != null) {
                Toast.makeText(queryText.getContext().getApplicationContext(), result, Toast.LENGTH_SHORT);
            }
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
                    if (Constants.DEBUG) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
    }

}

