import java.text.DecimalFormat;

public class Bereshit_Spaceship {

	public final double WEIGHT_EMP = 165; // kg
	public final double WEIGHT_FULE = 420; // kg

	public final double WEIGHT_FULL = WEIGHT_EMP + WEIGHT_FULE; // kg
	public final static double MAIN_ENG_F = 430; // N
	public final static double SECOND_ENG_F = 25; // N
	public final static double MAIN_BURN = 0.15; // liter per sec, 12 liter per m'
	public final static double SECOND_BURN = 0.009; // liter per sec 0.6 liter per m'
	public static final double ALL_BURN = MAIN_BURN + 8 * SECOND_BURN;

	private double vs;
	private double hs;
	private double dist;
	private double ang;
	private double alt;
	private double lat;
	private double time;
	private double dt;
	private double acc; // Acceleration rate (m/s^2)
	private double fuel;
	private double weight;
	private double NN; // rate[0,1]

	private PID pid;
	private Engines[] engines;
	private Point location;

	public Bereshit_Spaceship() {
		vs = 24.8;
		hs = 932;
		ang = 58.3;
		alt = 13748; // 30 k"m
		lat = 0;
		dist = (new Point(lat, alt).distance2D(Moon.realDestinationPoint));
		time = 0;
		dt = 1;
		acc = 0;
		fuel = 121;
		weight = WEIGHT_EMP + fuel;

		pid = new PID(0.7, 1, 0.01, 1, 0);
		NN = 0.7;

		location = new Point(0, 0);

		engines = new Engines[8];
		engines[0] = new Engines("North1", 0);
		engines[1] = new Engines("North2", 0);
		engines[2] = new Engines("East1", 0);
		engines[3] = new Engines("East2", 0);
		engines[4] = new Engines("South1", 0);
		engines[5] = new Engines("South2", 0);
		engines[6] = new Engines("West1", 0);
		engines[7] = new Engines("West2", 0);
	}

	public double getVS() {
		return vs;
	}

	public double getHS() {
		return hs;
	}

	public double getDist() {
		return dist;
	}

	public double getAng() {
		return ang;
	}

	public double getAlt() {
		return alt;
	}

	public double getTime() {
		return time;
	}

	public double getDT() {
		return dt;
	}

	public double getAcc() {
		return acc;
	}

	public double getFuel() {
		return fuel;
	}

	public double getWeight() {
		return weight;
	}

	public double getNN() {
		return NN;
	}

	public Point getLocation() {
		return location;
	}

	public void setVS(double vs) {
		this.vs = vs;
	}

	public void setHS(double hs) {
		this.hs = hs;
	}

	public void setDist(double d) {
		this.dist = d;
	}

	public void setAng(double a) {
		this.ang = a;
	}

	public void setAlt(double alt) {
		this.alt = alt;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public void setTime(double t) {
		this.time = t;
	}

	public void setDT(double dt) {
		this.dt = dt;
	}

	public void setAcc(double acc) {
		this.acc = acc;
	}

	public void setFuel(double f) {
		this.fuel = f;
	}

	public void setWeight(double w) {
		this.weight = w;
	}

	public void setNN(double nn) {
		this.NN = nn;
	}

	public void setLocation(double x, double y) {
		this.location.x = x;
		this.location.y = y;
	}

	public double enginesPower() {
		double sum = 0;
		for (int i = 0; i < engines.length; i++) {
			sum += engines[i].getPower();
		}
		return sum;
	}


	public void updateEngines() {
		if (alt < 2000 && ang > 0) {
			engines[0].setPower(0);
			engines[1].setPower(0.5);
			engines[2].setPower(0);
			engines[3].setPower(0);
			engines[4].setPower(0);
			engines[5].setPower(0.5);
			engines[6].setPower(1);
			engines[7].setPower(1);
			ang -= enginesPower() * dt;
			if (ang < 1) {
				ang = 0;
			}
		}
	}

	public void timer() {
		time = (time + dt);
	}

	public void loactionUpdate() {
		alt = (alt - dt * vs); // y
		lat = (lat + dt * hs); // x
		dist = (new Point(lat, alt).distance2D(Moon.realDestinationPoint));
	}

	public void speedControl(double h_acc, double v_acc) {
		if (hs > 0)
			hs = (hs - h_acc * dt) < 0 ? 0.1 : (hs - h_acc * dt);
		if (hs < 2 && alt <= 2000)
			hs = 0;
		vs = (vs - v_acc * dt) < 2 ? 0.4: (vs - v_acc * dt);
		if (alt < 15 && vs > 2) {
			vs = (vs - 2) < 2 ? 0.3: (vs - 2);
		}
	}

	public void fuelControl() {
		double dw = dt * Bereshit_Spaceship.ALL_BURN * NN; // Difference weight
		if (fuel > 0) {
			fuel -= dw;
			weight = (WEIGHT_EMP + fuel);
			acc = (NN * accMax(weight));
		} else { // ran out of fuel
			acc = 0;
		}
	}

	public double constraint(double x) {
		x = x > 1 ? 1 : x;
		return x < 0 ? 0 : x;
	}

	public void NNControl() {
		if (alt > 2000) {
			if (vs > 25) {
				NN = constraint(NN + 0.003 * dt);
			}
			if (vs < 20) {
				NN = constraint(NN - 0.003 * dt);
			}
		} else {
			NN = constraint(pid.control(dt, 0.5 - NN));

			if (alt < 5) {
				NN = 0.4;
			}
			else if (alt < 125) {
				NN = 1;
				if (vs < 5) {
					NN = 0.7;
				}
			}

		}
	}

	public static double accMax(double weight) {
		return acc(weight, 8);
	}

	public static double acc(double weight, int seconds) {
		double t = 0;
		t += Bereshit_Spaceship.MAIN_ENG_F;
		t += seconds * Bereshit_Spaceship.SECOND_ENG_F;
		double ans = t / weight;
		return ans;
	}

	public void print() {
		DecimalFormat df = new DecimalFormat("#.##");
		String output = "NN : " + df.format(NN) + " Time: " + df.format(time) + " , VS: " + df.format(vs)
				+ " , HS: " + df.format(hs) + " , Dist: " + df.format(dist) + " ,Lat: " + df.format(lat)
				+ " , Alt: " + df.format(alt) + " , Ang: " + df.format(ang) + " , Weight: " + df.format(weight)
				+ " , Acc: " + df.format(acc) + " , Fuel : " + df.format(fuel);
		System.out.println(output);
	}
}