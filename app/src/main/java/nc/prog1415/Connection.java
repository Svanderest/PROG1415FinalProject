package nc.prog1415;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

class Connection extends AsyncTask<Void, byte[], Boolean> {

    public  TcpClient client;

    public Connection(TcpClient t)
    {
        client = t;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        Log.d("CONNECTION","Connecting to server");
        try {
            InetAddress address = InetAddress.getByName("192.168.93.77");
            client.socket = new Socket(address,8000);
            client.out = new ObjectOutputStream(client.socket.getOutputStream());
            client.in = new ObjectInputStream(client.socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        client.connected = true;
        return null;
    }
}
