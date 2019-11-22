package nc.prog1415;

import java.io.Serializable;

public class RequestBusinessDetails implements Serializable {
    public double lt;
    public double lg;

    public RequestBusinessDetails(double lt, double lg)
    {
        this.lg = lg;
        this.lt = lt;
    }
}
