package ro.pub.cs.systems.eim.practicaltest02v1;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread {
    private final String address;
    private final int port;
    private final String searchInput;
    private final TextView resultTextView;

    private Socket socket;

    public ClientThread(String address, int port, String searchInput, TextView resultTextView) {
        this.address = address;
        this.port = port;
        this.searchInput = searchInput;
        this.resultTextView = resultTextView;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(address, port);
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            printWriter.println(searchInput);
            printWriter.flush();
            
            String result;
            while ((result = bufferedReader.readLine()) != null) {
                final String finalizedResult = result;
                resultTextView.post(() -> resultTextView.setText(finalizedResult));
            }
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
                }
            }
        }
    }
}
