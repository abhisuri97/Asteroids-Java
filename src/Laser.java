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
public class Laser extends GameObj {
    public static final String IMG_FILE = "res/plasmashot.png";
    public static final int SIZE = 24;
    /**
     * Note that, because we don't need to do anything special when constructing
     * a Square, we simply use the superclass constructor called with the
     * correct parameters
     */
    public Laser(double Velx, double Vely, double x, double y,  int courtWidth,
            int courtHeight, double f_theta, double d_theta) {
        super(Velx, Vely, x, y, SIZE, SIZE, courtWidth,
                courtHeight, f_theta, d_theta,IMG_FILE);
    }
    

    
    

}
