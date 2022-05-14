
public class Moon {
	// from: https://he.wikipedia.org/wiki/%D7%94%D7%99%D7%A8%D7%97
	public static final double RADIUS = 3475*1000; // meters
	public static final double ACC = 1.622;// m/s^2
	public static final double EQ_SPEED = 1700;// m/s

	public static final int LANDING_AREA_X = 1012;
	public static final int LANDING_AREA_Y = 550;
	public static final Point realDestinationPoint = new Point(268467, 0);

	public static double getAcc(double speed) {
		double n = Math.abs(speed)/EQ_SPEED;
		double ans = (1-n)*ACC;
		return ans;
	}
}
