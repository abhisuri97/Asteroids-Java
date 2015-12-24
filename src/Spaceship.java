/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */



/**
 * A basic game object displayed as a black square, starting in the upper left
 * corner of the game court.
 * 
 */
public class Spaceship extends GameObj {
    public static final String IMG_FILE = "res/spaceship1.png";
    public static final int SIZE = 60;
    public static final double INIT_POS_X = 300;
    public static final double INIT_POS_Y = 300;
    public static final double INIT_VEL_X = 0;
    public static final double INIT_VEL_Y = 0;
    public static final double INIT_F_THETA = 0;
    public static final double INIT_D_THETA = 0;
	/**
	 * Note that, because we don't need to do anything special when constructing
	 * a Square, we simply use the superclass constructor called with the
	 * correct parameters
	 */
	public Spaceship(double vel_x, double vel_y, double pos_x, double pos_y, int courtWidth, 
	        int courtHeight, double f_theta, double d_theta, String img_file) {
	    super(vel_x, vel_y, pos_x, pos_y, SIZE, SIZE, courtWidth, courtHeight,
	            f_theta, d_theta, img_file);

	}

}
