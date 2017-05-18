package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

/**
 * Created by Luciana on 18/05/2017.
 */

public class CommunicationThread extends Thread {

    private Socket socket;

    public CommunicationThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            Log.v(Constants.TAG, "Connection opened with " + socket.getInetAddress() + ":" + socket.getLocalPort());
            String result = null;
            BufferedReader reader = Utilities.getReader(socket);
            String string = reader.readLine();
            if (string == null || string.isEmpty()) {
                Log.v(Constants.TAG, "No query text!");
                socket.close();
                return;
            }

            Log.v(Constants.TAG, "Query --------------------------" + string);

            HttpClient httpClient = new DefaultHttpClient();

            HttpGet httpGet = new HttpGet(Constants.GET_WEB_SERVICE_ADDRESS
                    + "aq?" + Constants.QUERY + "=" + string);
            ResponseHandler<String> responseHandlerGet = new BasicResponseHandler();
            try {
                result = httpClient.execute(httpGet, responseHandlerGet);
            } catch (ClientProtocolException clientProtocolException) {
                Log.e(Constants.TAG, clientProtocolException.getMessage());
                if (Constants.DEBUG) {
                    clientProtocolException.printStackTrace();
                }
            } catch (IOException ioException) {
                Log.e(Constants.TAG, ioException.getMessage());
                if (Constants.DEBUG) {
                    ioException.printStackTrace();
                }
            }

            if (result == null) {
                Log.v(Constants.TAG, "No result from server");
                socket.close();
                return;
            }

            Log.d(Constants.TAG, "got server result " + result);
            PrintWriter printWriter = Utilities.getWriter(socket);
            printWriter.println(result.substring(result.indexOf("name") + 4));
            printWriter.flush();
            socket.close();
            Log.v(Constants.TAG, "Connection closed");
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        }
    }
}