public class Engines {
    private String side;
    private double power;

    Engines(String side, double power) {
        this.side = side;
        this.power = power;
    }

    public double getPower() {
        return this.power;
    }

    public void setPower(double power) {
        this.power = power;
    }
}
