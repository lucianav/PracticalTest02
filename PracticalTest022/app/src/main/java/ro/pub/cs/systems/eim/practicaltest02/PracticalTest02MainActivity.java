package ro.pub.cs.systems.eim.practicaltest02;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PracticalTest02MainActivity extends AppCompatActivity {

    Button startButton;
    Button stopButton;
    Button queryButton;
    EditText portText;
    EditText queryText;

    private ServerThread serverThread = null;
    private ClientThread clientThread = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);

        portText = (EditText) findViewById(R.id.port);
        queryText = (EditText) findViewById(R.id.query_text);
        portText = (EditText) findViewById(R.id.port);

        startButton = (Button) findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (serverThread != null)
//                    return;
                serverThread = new ServerThread(Integer.valueOf(portText.getText().toString()));
                serverThread.startServer();
            }
        });
        stopButton = (Button) findViewById(R.id.stop_button);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (serverThread == null)
                    return;
                serverThread.stopServer();
            }
        });
        queryButton = (Button) findViewById(R.id.query_button);
        queryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = queryText.getText().toString();
                if (query == null || query.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "No query inserted!!!", Toast.LENGTH_SHORT);
                    return;
                }

                Log.v(Constants.TAG, "Will instantiate client thread");
                clientThread = new ClientThread("localhost", Integer.valueOf(portText.getText().toString()), queryText);
                clientThread.start();
            }
        });
    }

    @Override
    protected void onDestroy() {

        if (serverThread != null)
            serverThread.stopServer();
        super.onDestroy();
    }
}
