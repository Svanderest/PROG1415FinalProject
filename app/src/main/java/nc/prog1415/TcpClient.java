package nc.prog1415;

import android.content.Intent;
import android.content.res.Resources;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import nc.com.Business;

public class TcpClient extends AsyncTask<Void, byte[], Boolean> {
    private static TcpClient singletonClient;

    public static TcpClient getClient(LocationManager lm, String serverLocation)
    {
        if(singletonClient == null)
            singletonClient = new TcpClient(lm, serverLocation);
        return singletonClient;
    }

    public static TcpClient getClient()
    {
        return singletonClient;
    }

    public ArrayList<Business> businesses = new ArrayList<Business>();
    public Boolean connected;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private LocationManager lm;
    private Location location = null;
    String serverLocation;

    public TcpClient(LocationManager lm, String serverLocation)
    {
        connected = false;
        this.lm = lm;
        this.serverLocation = serverLocation;
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.NO_REQUIREMENT);
        criteria.setPowerRequirement(Criteria.NO_REQUIREMENT);

        //display the best service available based on the required criteria
        String best = lm.getBestProvider(criteria, true);
        try {
            lm.requestLocationUpdates(best, 10000, 5, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    if (connected) {
                        Boolean firstUpdate = TcpClient.this.location == null;
                        TcpClient.this.location = location;
                        if (firstUpdate) {
                            nc.com.Location l = new nc.com.Location(location.getLongitude(), location.getLatitude());
                            send(l);
                        }
                    }
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            });
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        this.execute();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        Log.d("CONNECTION","Connecting to server");
        try {
            InetAddress address = InetAddress.getByName(serverLocation);
                    Resources.getSystem().getString(R.string.ServerAddress);
            socket = new Socket(address,8000);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        connected = true;
        return null;
    }

    public void send(final Object message)
    {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                if (!connected)
                    return;
                try {
                    out.writeObject(message);
                    out.flush();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        t.start();
    }

    public void receive(final Runnable onReceipt)
    {
        final Handler h = new Handler();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!connected) {
                    try {
                        this.wait();
                    } catch (Exception e)
                    {}
                }
                try {
                    boolean validResult = false;
                    while (!validResult)
                    {
                        Object obj = in.readObject();
                        if(obj instanceof ArrayList && ((ArrayList)obj).size() > 0)
                        {
                            if(((ArrayList)obj).get(0) instanceof Business)
                            {
                                businesses = (ArrayList<Business>) obj;
                                validResult = true;
                            }
                        }
                    }
                    h.post(onReceipt);
                }
                catch (Exception e) {
                    e.printStackTrace();
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
