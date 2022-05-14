

public class PID {

    private double P, I, D;
    private double lastError, sumIntegral;
    private int max;
    private int min;

    public PID(double P, double I, double D, int max, int min) {
        this.P = P;
        this.I = I;
        this.D = D;
        this.lastError = 0;
        this.sumIntegral = 0;
        this.max = max;
        this.min = min;
    }

    public double control(double dt, double error) {
        this.sumIntegral += this.I * error * dt;
        double difference = (error - lastError) / dt;
        double constIntegral;
        if(this.sumIntegral < min)
            constIntegral=min;
        else if(this.sumIntegral > max)
            constIntegral = max;
        else
            constIntegral = this.sumIntegral;
        double output = this.P * error + this.D * difference + constIntegral;
        this.lastError = error;
        return output;
    }

}
