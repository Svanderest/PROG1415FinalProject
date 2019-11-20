package nc.prog1415;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class TcpClient extends AsyncTask<Void, byte[], Boolean> {
    private static TcpClient singletonClient;

    public static TcpClient getClient()
    {
        if(singletonClient == null)
            singletonClient = new TcpClient();
        return singletonClient;
    }

    public Boolean connected;
    public Socket socket;
    public ObjectInputStream in;
    public ObjectOutputStream out;

    public TcpClient()
    {
        connected = false;
        this.execute();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        Log.d("CONNECTION","Connecting to server");
        try {
            InetAddress address = InetAddress.getByName("192.168.86.163");
            socket = new Socket(address,8000);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        connected = true;
        return null;
    }

    public void send(final String str)
    {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                if (!connected)
                    return;
                try {
                    out.writeObject(str);
                    out.flush();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        t.start();
    }

    public void receive(final TextView tv)
    {
        final Handler handler = new Handler();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while (!connected) {
                    try {
                        this.wait();
                    } catch (Exception e)
                    {}
                }
                try {
                    while (true) {
                        Object obj = in.readObject();
                        if (obj instanceof String) {
                                Log.d("READ", obj.toString());
                                final String newMessage = "\n" + obj.toString();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        tv.append(newMessage);
                                    }
                                });

                        }
                    }
                } catch (Exception e) {
                    Log.d("Error",e.getMessage());
                }
            }
        });
        t.start();
    }

}
