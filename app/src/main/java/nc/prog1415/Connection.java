package nc.prog1415;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

class Connection extends AsyncTask<Void, byte[], Boolean> {

    public boolean connected;
    public Socket socket;
    public ObjectInputStream in;
    public ObjectOutputStream out;

    public Connection()
    {
        connected = false;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        Log.d("CONNECTION","Connecting to server");
        try {
            InetAddress address = InetAddress.getByName("192.168.93.62");
            socket = new Socket(address,8000);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        connected = true;
        return null;
    }
}
