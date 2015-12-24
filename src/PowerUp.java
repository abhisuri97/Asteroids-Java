/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */


/**
 * A game object displayed using an image.
 * 
 * Note that the image is read from the file when the object is constructed, and
 * that all objects created by this constructor share the same image data (i.e.
 * img is static). This important for efficiency: your program will go very
 * slowing if you try to create a new BufferedImage every time the draw method
 * is invoked.
 */
public class PowerUp extends GameObj {
	public static final String IMG_FILE = "res/poison.png";
	public static final int SIZE = 40;
	public static final double INIT_VEL_X = 0;
	public static final double INIT_VEL_Y = 0;
	public static final double INIT_F_THETA = 0;
	public static final double INIT_D_THETA = 0;

	public PowerUp(int courtWidth, int courtHeight, double pos_x, double pos_y) {
        super(INIT_VEL_X, INIT_VEL_Y, pos_x, pos_y, SIZE, SIZE, courtWidth,
                courtHeight, INIT_F_THETA, INIT_D_THETA, IMG_FILE);

    }
	


}
