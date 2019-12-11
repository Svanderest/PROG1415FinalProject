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
import android.util.Pair;
import android.widget.TextView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import nc.com.Business;
import nc.com.Feedback;

public class TcpClient extends AsyncTask<Void, byte[], Boolean> {
    private static TcpClient singletonClient;

    public static TcpClient getClient(LocationManager lm)
    {
        if(singletonClient == null)
            singletonClient = new TcpClient(lm);
        return singletonClient;
    }

    public static TcpClient getClient()
    {
        return singletonClient;
    }

    public ArrayList<Business> businesses = new ArrayList<Business>();
    public ArrayList<Feedback> feedback = new ArrayList<Feedback>();
    public ArrayList<Integer> sent = new ArrayList<Integer>();
    public Boolean connected;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private LocationManager lm;
    public Location location = null;
    String serverLocation;

    public TcpClient(LocationManager lm)
    {
        connected = false;
        this.lm = lm;
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
                            //sent.add(TcpClient.this.hashCode());
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
            InetAddress address = InetAddress.getByName("192.168.93.77");
            socket = new Socket(address,8000);
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
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
                    sent.add(message.hashCode());
                    Log.d("HASH CODE SENT", String.valueOf(message.hashCode()));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        t.start();
    }

    public void receive(final Runnable onReceipt, final int id)
    {
        final Handler h = new Handler();
        Log.d("HASH CODE RECEIVING", String.valueOf(id));
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!(connected  && (sent.contains(id) || id == 0))) {
                    try {
                        this.wait();
                    }
                    catch (Exception e)
                    {
                    }
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
                                final nc.com.Location myLocation = new nc.com.Location(location.getLongitude(),location.getLatitude());
                                Collections.sort(businesses, new Comparator<Business>() {
                                    @Override
                                    public int compare(Business business, Business t1) {
                                        if(business.getDistance(myLocation) < t1.getDistance(myLocation))
                                            return -1;
                                        else if(business.getDistance(myLocation) > t1.getDistance(myLocation))
                                            return 1;
                                        else
                                            return 0;
                                    }
                                });
                                Log.d("Business",String.valueOf(((ArrayList)obj).size()) + " business objects received");
                                validResult = true;
                                for(int i = 0; i < businesses.size(); i++)
                                {
                                    long distance = Math.round(myLocation.getDistance(businesses.get(i)));
                                    if(distance < 1000)
                                        businesses.get(i).address += " - " + String.valueOf(distance) + "m";
                                    else
                                        businesses.get(i).address += " - " + String.valueOf(distance / 1000.0) + "km";
                                }
                            }
                            else if(((ArrayList)obj).get(0) instanceof Feedback) {
                                feedback = (ArrayList<Feedback>)obj;
                                Collections.sort(feedback, new Comparator<Feedback>() {
                                    @Override
                                    public int compare(Feedback feedback, Feedback t1) {
                                        return -feedback.date.compareTo(t1.date);
                                    }
                                });
                                Log.d("Feedback",String.valueOf(((ArrayList)obj).size()) + " feedback objects received");
                                validResult = true;
                            }
                        }
                        else if(obj instanceof ArrayList && ((ArrayList)obj).size() == 0) {
                            validResult = true;
                            Log.d("Not found","No objects received");
                        }
                        else
                            Log.d("MESSAGE TYPE",obj.getClass().getName());

                    }
                    //sent.remove(id);
                    h.post(onReceipt);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }
}
