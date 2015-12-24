/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/** An object in the game. 
 *
 *  Game objects exist in the game court. They have a position, 
 *  velocity, size and bounds. Their velocity controls how they 
 *  move; their position should always be within their bounds.
 */
public class GameObj implements Comparable<GameObj>{

	/** Current position of the object (in terms of graphics coordinates)
	 *  
	 * Coordinates are given by the upper-left hand corner of the object.
	 * This position should always be within bounds.
	 *  0 <= pos_x <= max_x 
	 *  0 <= pos_y <= max_y 
	 */
	public double pos_x; 
	public double pos_y;

	/** Size of object, in pixels */
	public int width;
	public int height;
	
	/** Velocity: number of pixels to move every time move() is called */
	public double v_x;
	public double v_y;

	/** Upper bounds of the area in which the object can be positioned.  
	 *    Maximum permissible x, y positions for the upper-left 
	 *    hand corner of the object
	 */
	public int max_x;
	public int max_y;
	/** f_theta = Facing theta is the direction in which the object is facing. 
	 * Not necessarily the direction in which it is moving. d_theta Direction 
	 * theta is the angle at which the object is moving.
	 */
	public double f_theta;
	public double d_theta;
	/**
	 * valid objects are those which are currently on the game screen.
	 */
	public boolean valid;
	public BufferedImage img;
	public String img_name;
	/**
	 * Constructor
	 */
	public GameObj(double v_x, double v_y, double pos_x, double pos_y, 
		int width, int height, int court_width, int court_height, 
		double f_theta, double d_theta, String img_name){
		this.v_x = v_x;
		this.v_y = v_y;
		this.pos_x = pos_x;
		this.pos_y = pos_y;
		this.width = width;
		this.height = height;
		this.f_theta = f_theta;
		this.d_theta = d_theta;
		// take the width and height into account when setting the 
		// bounds for the upper left corner of the object.
		this.max_x = court_width - width;
		this.max_y = court_height - height;
		this.valid = true;
		this.img_name = img_name;
		try {
            if (this.img == null) {
                this.img = ImageIO.read(getClass().getClassLoader().getResourceAsStream(img_name));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }

	}
	
	public double getVelX() { return this.v_x; }
	public double getVelY() { return this.v_y; }
	public double getPosX() { return this.pos_x; }
	public double getPosY() { return this.pos_y; }
	public int getWidth() { return this.width; }
	public int getHeight() { return this.height; }
	public double getFtheta() { return this.f_theta; }
	public double getDtheta() { return this.d_theta; }
	public boolean getValid() { return this.valid; }
	public String getImgName() { return new String(this.img_name); }
	public BufferedImage getOrigImg() { return this.img; }
	public BufferedImage getImg() { return rotate(this.img,this.getFtheta()); }
	public int imgGetWidth() { return this.img.getWidth();}
    public int imgGetHeight() { return this.img.getHeight(); }
    
    /**
     * http://www.java-forums.org/java-2d/63393-rotating-buffered-image.html
     */
    public static BufferedImage rotate(BufferedImage img, double rotation) {
        int w = img.getWidth();  
        int h = img.getHeight();  
        BufferedImage newImage = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        Graphics2D g2 = newImage.createGraphics();
        g2.rotate(Math.toRadians(rotation), w/2, h/2);  
        g2.drawImage(img,null,0,0);
        return newImage;  
    }
    
	/**
	 * Moves the object by its velocity.  Ensures that the object does
	 * not go outside its bounds by clipping.
	 */
	public void moveship(){
		pos_x += v_x;
		pos_y += v_y;

		clipship();
	}
	public boolean move(){
        pos_x += v_x;
        pos_y += v_y;

        return clip();
    }
	/**
     * Prevents the object from going outside of the bounds of the area 
     * designated for the object. (i.e. Object cannot go outside of the 
     * active area the user defines for it).
     * 
     * EDITED: CHANGED BEHAVIOR SO OBJECT WILL "WRAP" AROUND THE SCREEN AND
     * STAY IN THE OBJECT WINDOW WITHOUT RESTRICTION
     */ 
    public boolean clip(){
        if (pos_x + 120 < 0) {
            return true;
        }
        else if (pos_x - 120> max_x) { 
            return true;
        }

        if (pos_y + 120< 0) {
            return true;
        }
        else if (pos_y - 120> max_y) {
            return true;
        }
       
        return false;
    }

	/**
	 * Prevents the object from going outside of the bounds of the area 
	 * designated for the object. (i.e. Object cannot go outside of the 
	 * active area the user defines for it).
	 * 
	 * EDITED: CHANGED BEHAVIOR SO OBJECT WILL "WRAP" AROUND THE SCREEN AND
	 * STAY IN THE OBJECT WINDOW WITHOUT RESTRICTION
	 */ 
	public void clipship(){
		if (pos_x < 0) {pos_x = 0; v_x = 0;v_y=0;}
		else if (pos_x > max_x) {pos_x = max_x; v_x = 0;v_y=0;}

		if (pos_y< 0) {pos_y = 0; v_x = 0;v_y=0;}
		else if (pos_y> max_y) {pos_y = max_y; v_x = 0;v_y=0;}
	}

	
	
	/**
	 * Default draw method that provides how the object should be drawn 
	 * in the GUI. This method does not draw anything. Subclass should 
	 * override this method based on how their object should appear.
	 * 
	 * @param g 
	 *	The <code>Graphics</code> context used for drawing the object.
	 * 	Remember graphics contexts that we used in OCaml, it gives the 
	 *  context in which the object should be drawn (a canvas, a frame, 
	 *  etc.)
	 */
	public void draw(Graphics2D g2d) {
        BufferedImage img = getImg();
        g2d.drawImage(img, (int) pos_x, (int) pos_y, width, height, null);
    }
	public int compareTo(GameObj o) {
        if(((o).getPosX() == getPosX()) && ((o).getPosY() == getPosY()) && (o == this )) {
            return 0;
        } 
        else return 1;
    }
    public boolean equals(Object o) {
        if((((GameObj)o).getPosX() == getPosX()) && (((GameObj)o).getPosY() == getPosY())) {
            return true;
        } else return false;
    }
}
