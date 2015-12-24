/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */


/**
 * A basic game object displayed as a yellow circle, starting in the upper left
 * corner of the game court.
 * 
 */
public class Asteroid extends GameObj {
	public static final int SIZE = 120;
	public static final double INIT_F_THETA = 0;
	public static final double INIT_D_THETA = 90;

	public Asteroid(double vel_x, double vel_y, double pos_x, double pos_y ,
	        int courtWidth, int courtHeight, int childno) {
		    super(vel_x, vel_y, pos_x, pos_y, (int)(Math.floor(120.0/childno)), 
		            (int)(Math.floor(120.0/childno)),
	                courtWidth, courtHeight, INIT_F_THETA, INIT_D_THETA, "res/asteroid"+childno+".png");
	}
	public int getChildNo() {
	    return (int)(SIZE/this.width);
	}
	

	
	

}
