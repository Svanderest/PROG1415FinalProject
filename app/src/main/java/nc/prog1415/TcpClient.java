package nc.prog1415;

import android.os.Handler;
import android.widget.TextView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class TcpClient {
    public Boolean connected;
    public Socket socket;
    public ObjectInputStream in;
    public ObjectOutputStream out;

    public TcpClient()
    {
        Connection con = new Connection(this);
        con.execute();
    }

    public synchronized void send(final String str)
    {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (!connected)
                        this.wait();
                }
                catch (InterruptedException e){}
                try {
                    out.writeObject(str);
                    out.flush();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                this.notify();
            }
        });
        t.start();
    }

    public void receive(final TextView tv)
    {
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                String content = tv.getText().toString();
                synchronized (this)
                {
                    try {
                        while (!connected)
                            this.wait();
                    } catch (InterruptedException e) {}
                    try
                    {
                        Object obj = in.readObject();
                        while (!(obj instanceof String))
                        {
                            this.wait();
                            obj = in.readObject();
                        }
                        content += obj.toString();
                    }catch (Exception e) {}
                    this.notify();
                }
                tv.setText(content);
            }
        });
    }

}
