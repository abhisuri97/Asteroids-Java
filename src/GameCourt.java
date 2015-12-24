/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * GameCourt
 * 
 * This class holds the primary game logic for how different objects interact
 * with one another. Take time to understand how the timer interacts with the
 * different methods and how it repaints the GUI on every tick().
 * 
 */
@SuppressWarnings("serial")
public class GameCourt extends JPanel {

	// the state of the game logic
    public static TreeSet<GameObj> allObjects = new TreeSet<GameObj>(); // holds everything
    // Private variable to store ship
	private Spaceship ship; 

	public boolean playing = false; // whether the game is running
	public static int END = 0; // game state
	public static int INTRO = 1; // game state
	public static int PLAYING = 2; //games state
	public static int GAMESTATE = 1; // begin with intro
	private JLabel status; // Current status text (i.e. Running...)
    public TreeSet<GameObj> toAdd = new TreeSet<GameObj>(); //NOTE: ALL OBJECTS TO BE ADDED GO THRU
                                                           // THIS BUFFER 
    
    //USER SPECIFIC STUFF
    public static int myHealth = 100; //SHIP HEALTH STATS
    public static int myScore = 0;
    public static String nickName = ""; // nickName for user
    
	// Game constants
	public static final int COURT_WIDTH = 680;
	public static final int COURT_HEIGHT = 680;
	public static double SHIP_VELOCITY = 1.5;
	public static double SHIP_F_THETA = 0;
	public static double SHIP_D_THETA = 0;
	// Update interval for timer, in milliseconds
	public static final int INTERVAL = 25;
	//KEEP TRACK OF PRESSED KEYS
	public static boolean leftArrow = false; 
	public static boolean rightArrow = false;
	public static boolean upArrow = false;
	public static boolean spaceBar = false;
	//TICKCOUNT USED FOR OPERATIONS (SUCH AS DETERMINING FIRING INTERVALS FOR LASERS)
    public static int tickCount = 0;
    
    // BUG FIXING: When window is resized, it caused score list to regenerate. Keeps track of resize
    // count
    public static int windowResizeCount = 0;
    public static int startLaserTick = 0;
    public static int LaserFatigue = 100;
    public static int ShieldFatigue = 150; //50 buffer to recharge
    public static boolean inCollision = false;
    // Collision points for drawing
    public static boolean shielded = false;
    public static ArrayList<Asteroid> collided = new ArrayList<Asteroid>();
	public GameCourt(JLabel status) {
	    
		// creates border around the court area, JComponent method
		setBorder(BorderFactory.createLineBorder(Color.BLACK));

		// The timer is an object which triggers an action periodically
		// with the given INTERVAL. One registers an ActionListener with
		// this timer, whose actionPerformed() method will be called
		// each time the timer triggers. We define a helper method
		// called tick() that actually does everything that should
		// be done in a single timestep.
		Timer timer = new Timer(INTERVAL, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tick();
                tickCount++;
			}
		});
		timer.start(); // MAKE SURE TO START THE TIMER!

		// Enable keyboard focus on the court area.
		// When this component has the keyboard focus, key
		// events will be handled by its key listener.
		setFocusable(true);

		// This key listener allows the ship to move as long
		// as an arrow key is pressed, by changing the ship's
		// velocity accordingly. (The tick method below actually
		// moves the ship.)
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
			    // Intro state: After user presses enter, it prompts them for input username
			    if (e.getKeyCode() == KeyEvent.VK_ENTER && GAMESTATE==INTRO) {
			        while(true) {
			            try {
    			        	String tempnickName = 
    			        	  JOptionPane.showInputDialog(null,
    			        	  "Please Enter Your Nickname so you can be added to the high score list!");
    			            if(tempnickName.isEmpty()) {
                                JOptionPane.showMessageDialog(null,"Nickname must not be empty!");
                                continue;
    			            }
                            else {
                                nickName = tempnickName;
                                break;
                            }
			            } 
			            // catch if they click cancel or x out
			            catch (Exception e1) {
                           
                            JOptionPane.showMessageDialog(null,"Nickname must not be empty!");
                        }
			        }  
			        // if pass, go to playing state
			        GAMESTATE = PLAYING;
			    }
			    
			    if (e.getKeyCode() == KeyEvent.VK_ENTER && GAMESTATE==END) {
	                // on end screen (high score list) pressing enter -> resets game and 
			        // sends to intro
                    reset();    
                    GAMESTATE = INTRO;
                }
			    if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			        leftArrow = true;
                    
                }
			    else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			        rightArrow = true;
                }
			    else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    upArrow = true;
                }
			    if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			        // fire lasers
    			        
    			        double xvel = -5*Math.cos(Math.PI/2+Math.toRadians(ship.d_theta));
    	                double yvel = -5*Math.sin(Math.PI/2+Math.toRadians(ship.d_theta));
    	                double pos_x = ship.pos_x;
    	                double pos_y = ship.pos_y;
    	                double f_theta = ship.f_theta;
    	                
    	                Laser fired = new Laser(xvel, yvel, pos_x+ship.getWidth()/2, 
    	                        pos_y+ship.getWidth()/2, COURT_WIDTH, COURT_HEIGHT, 
    	                        f_theta, f_theta);
    	                int timeSinceLastLaser = tickCount - startLaserTick;
    	                
    	                if(timeSinceLastLaser >= 3 && LaserFatigue > 0) {
    	                    startLaserTick = tickCount;
    	                    LaserFatigue--;
    	                    allObjects.add(fired);
    	                }
                }
			    if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
			        if(ShieldFatigue >= 50) {
			            shielded = true;
			        }
			    }
			    if (e.getKeyCode() == KeyEvent.VK_Q) {
			        // QUIT: sends to end screen
                    myHealth = 0;
                   
                }
			    if (e.getKeyCode() == KeyEvent.VK_P) {
			        // pause switch game state
			        playing = !playing;
			        
			    }
			    
			}
			
			

            public void keyReleased(KeyEvent e) {
			    if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			        spaceBar = false;
			    } if(e.getKeyCode() == KeyEvent.VK_UP) {
			        upArrow = false;
			    } if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			        leftArrow = false;
			    } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			        rightArrow = false;
			    } if(e.getKeyCode() == KeyEvent.VK_SHIFT) {
			        shielded = false;
			        
			        
			    }
                     
			}
		});

		this.status = status;
	}
	public void motionControl() {
	    if (leftArrow) {
            ship.d_theta -= 3.5;
            ship.f_theta -= 3.5;
        }
        else if (rightArrow) {
            ship.d_theta += 3.5;
            ship.f_theta += 3.5;
        }
        
        if (upArrow) {
            // ship velocity controls
            double xvel = SHIP_VELOCITY*Math.cos(Math.PI/2+Math.toRadians(ship.d_theta));
            double yvel = SHIP_VELOCITY*Math.sin(Math.PI/2+Math.toRadians(ship.d_theta));
            if((Math.abs(ship.v_x) >= 3)) {
                if(ship.v_x < 0) {
                    ship.v_x = -3;
                } else {
                    ship.v_x = 3;
                }
                
            } 
            else if ((Math.abs(ship.v_y) >= 3)){
                if(ship.v_y < 0) {
                    ship.v_y = -3;
                } else {
                    ship.v_y = 3;
                }
            }
            else {  
                ship.v_x -= .15*xvel;
                ship.v_y -= .15*yvel;
            }
                
        }
        else if (upArrow == false) {
            double xvel = ship.v_x;
            double yvel = ship.v_y;
            if(Math.abs(xvel) > 3 || Math.abs(yvel) > 3) {
                ship.v_x -= .7*xvel;
                ship.v_y -= .7*yvel;
            }
        }
        if (shielded) {
            if(ShieldFatigue >= 50) {
                shielded();
            }
            else {
                shielded = false;
            }
        }
        if (shielded == false) {
            if(ship.getImgName().equals("res/shilded.png")) {
                Spaceship ship2 = new Spaceship(ship.v_x, ship.v_y, ship.pos_x, ship.pos_y,
                    COURT_WIDTH, COURT_HEIGHT, ship.f_theta, ship.d_theta, "res/spaceship1.png");
                ship.valid = false;
                ship=ship2;
            }
        }
    }
	private void shielded() {
	    if(ship.getImgName().equals("res/spaceship1.png")) {
        Spaceship shieldedShip = new Spaceship(ship.v_x, ship.v_y, ship.pos_x, ship.pos_y,
                COURT_WIDTH, COURT_HEIGHT, ship.f_theta, ship.d_theta, "res/shilded.png");
        ship.valid = false;
        ship = shieldedShip;
	    }
    }
	public static void generateAsteroids () {
        int x = (int)Math.floor(Math.random()*4 + 1);
        double vel = .5*x;
        int XorY = (int)Math.floor(Math.random()*4 + 1);
        double pos_y = -100;
        double pos_x = -100;
         if (XorY == 1 || XorY == 2) { // y = -100
            pos_x = Math.random() * 680;
            double theta = Math.toRadians((int)Math.floor(Math.random()*150+15));
            double velx = vel * Math.cos(theta);
            double vely = vel*Math.sin(theta);
            Asteroid generated = new Asteroid(velx, vely, pos_x, pos_y, 
                    COURT_WIDTH, COURT_HEIGHT, x);
            allObjects.add(generated);
         } 
         if (XorY == 3 || XorY == 4) { // y = -100
            pos_y = Math.random() * 680;
            double theta = Math.toRadians((int)Math.floor(Math.random()*75));
            theta *= Math.floor(Math.random()*2) == 1 ? 1: -1;
            double velx = vel * Math.cos(theta);
            double vely = vel*Math.sin(theta);
            Asteroid generated = new Asteroid(velx, vely, pos_x, pos_y, 
                    COURT_WIDTH, COURT_HEIGHT, x);
            allObjects.add(generated);
         } 
        
    }
	public void generateChildAsteroids(Asteroid p) {
	    int child = p.getChildNo();

	    p.getVelX();
	    p.valid = false;
	    if (child < 4) {
	        double theta = Math.toRadians((int)Math.floor(Math.random()*75));
            theta *= Math.floor(Math.random()*2) == 1 ? 1: -1;
            
	        Asteroid generated = new Asteroid(2 * p.getVelX()*Math.cos(theta),  2 * 
	                p.getVelY()*Math.sin(theta),  p.getPosX()+10,  p.getPosY()+10 ,
	                 COURT_WIDTH,  COURT_HEIGHT,  child+1);
            Asteroid generated2 = new Asteroid(2 * p.getVelX()*Math.cos(-theta),  2 * 
                    p.getVelY()*Math.sin(-theta), p.getPosX()-10,  
                    p.getPosY()-10, COURT_WIDTH, COURT_HEIGHT, child+1);
            toAdd.add(generated);
	        toAdd.add(generated2);
	        int rand = (int) Math.floor(Math.random() * 100);
	        if(rand >= 49 && rand <= 51) {
	            PowerUp newPowerup = new PowerUp(COURT_WIDTH, COURT_HEIGHT, p.getPosX()+5,
	                    p.getPosY()+5);
	            toAdd.add(newPowerup);
	        }
	    } 
	}
	/**
	 * (Re-)set the game to its initial state.
	 */
	public void reset() {
	    playing = true;
	    tickCount = 0;
	    myHealth = 10000;
	    myScore = 0;
	    upArrow = false;
	    rightArrow = false;
	    spaceBar = false;
	    leftArrow = false;
	    LaserFatigue = 100;
	    ShieldFatigue = 150;
	    GAMESTATE = INTRO;
	    collided = new ArrayList<Asteroid>();
	    
        ship = new Spaceship(0,0, 340, 340, COURT_WIDTH, 
                COURT_HEIGHT, 0, 0, "res/spaceship1.png");
        allObjects = new TreeSet<GameObj>();
        windowResizeCount = 0;
        startLaserTick = 0;

		// Make sure that this component has the keyboard focus
		requestFocusInWindow();
	}
	public void showHighScores(Graphics g, ArrayList<Scorecontainer> scores) {
	    
	    ArrayList<Scorecontainer> sorted = new ArrayList<Scorecontainer>(); 
	    Collections.sort(scores);
	    Collections.reverse(scores);
	    String[] nameArray = new String[scores.size()];
	    int[] scoreArray = new int[scores.size()];
	    int itercount = 0;
	    for(Scorecontainer i : scores) {
	        nameArray[itercount] = i.getName();
	        scoreArray[itercount] = i.getScore();
	        itercount++;
	    }
	    sorted = scores;
	    int counterj = 0;
	    int counteri = 0;
	    String[][] data = new String[sorted.size()][2];
	    Font font = new Font ("Arial", Font.BOLD, 20);
	    int position = 0;
	    for(int i = 0; i < nameArray.length; i++) {
	        if(scoreArray[i] == myScore && nameArray[i].equals("\""+nickName+"\"")) {
	            position = i;
	            break;
	        }
	    }
	    g.setFont(font);
	    g.setColor(Color.BLACK);
	    g.fillRect(0, 0, 680, 680);
	    g.setColor(Color.WHITE);
	    g.drawString("TOP 10 HIGH SCORES. YOU ARE RANKED #" + (position+1), 20, 30);
        Font font2 = new Font ("Arial", Font.BOLD, 18);
	    g.setFont(font2);
	    for(int i=0; i < data.length; i++) {
	        for(int j=0; j<data[0].length; j++) {
	            
	            if(j == 0) {
	                data[i][j] = nameArray[counterj];
	                if(nameArray[counterj].length() > 13) {
	                    nameArray[counterj] = nameArray[counterj].substring(0, 12) + "...";
	                }
	                g.drawString((i+1)+"." + nameArray[counterj], 100, counterj*50 + 50);
	                counterj++;
	            }else if(j == 1) {
	                data[i][j] = ""+scoreArray[counteri];
	                g.drawString(""+scoreArray[counteri], 350, counteri*50  + 50);
                    counteri++;
	            }
	            if(counteri >= 10 && counterj >= 10) {
                    break;
                }
	        }
	        if(counteri >= 10 && counterj >= 10) {
                break;
            }
	    }
	    g.drawString("Press Enter to go to main menu", 20, 620);
	   
	}
	
	public void resetIntro(Graphics g) {
	    super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g;
        String img_name = "res/title.png";
        BufferedImage img = null;
        try {
            if (img == null) {
                img = ImageIO.read(getClass().getClassLoader().getResourceAsStream(img_name));
            }
        } catch (IOException e) {
            System.out.println("MEH3 Error:" + e.getMessage());
        }
        g2d.drawImage(img, null, 0,0);
        
        playing = true;
        status.setText("Start Screen");

        // Make sure that this component has the keyboard focus
        requestFocusInWindow();
    }
	public void playGame(Graphics g) {

        allObjects.add(ship);
        
        if( playing == true ) {
            status.setText("Running..");
        }
        else if(playing == false) {
            status.setText("Paused");
        }
        
	    int style = Font.BOLD | Font.BOLD;
        Font font = new Font ("Arial", style , 30);
        
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        String img_name = "res/background.png";
        BufferedImage img = null;
        try {
            if (img == null) {
                img = ImageIO.read(getClass().getClassLoader().getResourceAsStream(img_name));
            }
        } catch (IOException e) {
            System.out.println("Background Image Error:" + e.getMessage());
        }
        g2d.drawImage(img, null, 0,0);

        for (GameObj o : allObjects) {
            o.draw(g2d); 
        }
        
        g.setColor(Color.RED);
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawRect(19, 49, 201, 41);
        int x = myHealth/100;
        if(x > 70) {
            g.setColor(Color.GREEN);
        }
        else if (x > 40) {
            g.setColor(Color.ORANGE);
        } else {
            g.setColor(Color.RED);
        }
        if(x == 0) {
            GAMESTATE = END;                
        }
        
        g.fillRect(20, 50, myHealth/50, 40);
        g.drawString("HEALTH:"+myHealth/100, 20, 40);
        
        g.setColor(Color.WHITE);
        g.drawString("Laser Recharge:"+LaserFatigue + "%", 20, 640);
        
        g.setColor(Color.WHITE);
        g.drawString("Shield Life:"+(ShieldFatigue-50) + "%", 20, 600);
        if(inCollision) {
            g.setColor(Color.RED);
            g.drawString("IN COLLISION", 300, 640);
        }
        g.setColor(Color.WHITE);
        g.drawString("SCORE:"+myScore, 400, 40);
        
        
        Font font2 = new Font ("Arial", Font.PLAIN , 12);

        g.setFont(font2);
        NumberFormat nf = new DecimalFormat("##.###");
        
        g.drawString("Velocity X:" + nf.format(ship.v_x), 550, 600);
        g.drawString("Velocity Y:" + nf.format(ship.v_y), 550, 620);
        g.drawString("OVERALL:" + nf.format(SHIP_VELOCITY), 550, 640);

	}
	/**
	 * This method is called every time the timer defined in the constructor
	 * triggers.
	 */

	public static boolean Collision(double x1, double y1, BufferedImage image1,
            double x2, double y2, BufferedImage image2) {

	        double width1 = x1 + image1.getWidth() -1,
	        height1 = y1 + image1.getHeight() -1,
	        width2 = x2 + image2.getWidth() -1,
	        height2 = y2 + image2.getHeight() -1;

	        int xstart = (int) Math.max(x1, x2),
	        ystart = (int) Math.max(y1, y2),
	        xend   = (int) Math.min(width1, width2),
	        yend   = (int) Math.min(height1, height2);

	        int toty = Math.abs(yend - ystart);
	        int totx = Math.abs(xend - xstart);

	        for (int y=1;y < toty-1;y++){
	            int ny = Math.abs(ystart - (int) y1) + y;
	            int ny1 = Math.abs(ystart - (int) y2) + y;
	            y += 5;
	            for (int x=1;x < totx-1;x++) {
	                int nx = Math.abs(xstart - (int) x1) + x;
	                int nx1 = Math.abs(xstart - (int) x2) + x;
	                x +=5;
	                try {
	                    if (((image1.getRGB(nx,ny) & 0xFF000000) != 0x00) &&
	                            ((image2.getRGB(nx1,ny1) & 0xFF000000) != 0x00)) {
	                        inCollision = true;
	                        return true;
	                        
	                    }
	                    else {
	                        inCollision = false;
	                    }
	                } catch (Exception e) {
	                }
	            }
	        }

	        return false;
	}

	void tick() {
	    if (!playing) {
	        status.setText("Paused");
	    }
		if (playing) {
			// advance the ship in its
			// current direction.
		    motionControl();
		    
		    // regen laser fatigue
		    if(tickCount % 40 == 0) {
		        if(LaserFatigue < 100) {
		            LaserFatigue ++;
		        }
		    }
		    
		    //if shielded, reduce shieldfatigue
		    if(ShieldFatigue >= 0 && shielded) {
		        if(tickCount % 10 == 0) {
		            ShieldFatigue -= 2;
		        }
            }
		    
		    // regen shield fatigue
		    if(tickCount % 40 == 0) {
                if(ShieldFatigue < 150) {
                    ShieldFatigue ++;
                }
            }
		    // clear collided list
		    if(tickCount % 20 == 0) {
                collided.clear();
            }
		    // loop thru Game Objects
			for (GameObj o : allObjects) {
			    
			    // move method for anything but a ship
			    if(!(o instanceof Spaceship)) {
			        o.move();
			        if(o.move()) {
			            // triggered if the object is outside the game boundaries
                        o.valid = false;
                        // marking as false will make the object be deleted in the next cycle
                    }
			    } else {
			        // move the ship's position
			        o.moveship();
			    }
			    
			    double x = o.getPosX();
                double y = o.getPosY();
                
                for(GameObj p : allObjects) {
                    // collision check between objects o and p where o and p are any objects that
                    // are within distance 150 of each other
                    if(!p.equals(o) || !(p.getClass().equals(o.getClass()))) {
                        if(((Math.abs(p.pos_x-o.pos_x) + Math.abs(p.pos_y-o.pos_y)) < 150) && 
                                (o instanceof Spaceship || o instanceof Laser)) {
                            
                            if(Collision(x,y, o.getImg(),
                                    p.pos_x,p.pos_y,p.getImg()) ||
                                    Collision(o.v_x+x, o.v_y + y, o.getImg(),
                                            p.pos_x + p.v_x, p.pos_y + p.v_y, p.getImg())
                                    ) {
                                p.getImgName();
                                
                                // collision specific events:
                                if(p instanceof PowerUp && o instanceof Spaceship) {
                                    // if P is powerup and O is spaceship, increment the health
                                    if (myHealth + 100 <= 10000) {
                                        myHealth += 100;
                                    } else {
                                        myHealth = 10000;
                                    }
                                    p.valid = false;
                                }  else if (p instanceof Asteroid && o instanceof Spaceship) {
                                    // if p is an asteroid and o is a spaceship
                                    if(shielded == true) {
                                        // if shield is on, perform bounce method
                                        // add to collided list so they don't bounce again (see bounce)
                                        bounce((Asteroid) p,(Spaceship)o);
                                        collided.add((Asteroid)p);
                                    }
                                    myHealth = myHealth - 2;
                                }
                                else if (p instanceof Asteroid && o instanceof Laser) {
                                    //mark laser as invalid and set its velocity to 0;
                                    // add to score
                                    myScore += 100;
                                    o.valid = false;
                                    o.v_x = 0;
                                    o.v_y = 0;
                                    generateChildAsteroids((Asteroid) p);
                                }
                                
                            }
                        }
                    }
                }
			}             

			
			double x = Math.random() * 40;
			if(x > 7.5 && x < 11.5 && allObjects.size() < 40 && GAMESTATE==PLAYING) {
			    // random asteroid generation
			    generateAsteroids();
			}
			Iterator<GameObj> iter = allObjects.iterator();
			// iterate thru allObjects list  
			while(iter.hasNext()) {
			    GameObj elem = iter.next();
			    
			    if (elem.getValid() == false) {
			        // remove from allObjects if validity is false
			        iter.remove();
			    }
			    
			}
			

	        allObjects.addAll(toAdd);

	        toAdd.clear();
	        if(myHealth <= 0) {
	            playing = false;
	        }

			repaint();
		}
	}
	// if shielded, and if the asteroid isn't in the collided list
	public void bounce(Asteroid p, Spaceship o) {
	    if(!collided.contains(p)) {
	    double newVelX1 = (p.v_x * (p.getWidth()/2- o.getWidth()*2) 
                + (2 * o.getWidth() * o.v_x)) / 
                    (p.getWidth() + o.getWidth());
	    double newVelY1 = (p.v_y * (p.getWidth()/2- o.getWidth()*2) 
    + (2 * o.getWidth() * o.v_y)) / 
        (p.getWidth() + o.getWidth());
	    double newVelX2 = (o.v_x * (o.getWidth()*2 - p.getWidth()/2) 
    + (2 * p.getWidth() * p.v_x))/
    (p.getWidth() + o.getWidth());
	    double newVelY2 = (o.v_y * (o.getWidth()*2 - p.getWidth()/2) 
    + (2 * p.getWidth() * p.v_y))/
    (p.getWidth() + o.getWidth());
	    p.v_x = newVelX1;
	    p.v_y = newVelY1;
	    o.v_x = newVelX2;
	    o.v_y = newVelY2;
	    }
    }
    @Override
	public void paintComponent(Graphics g) {
	    if(GAMESTATE == INTRO) {
            resetIntro(g);
	    }
	    if(GAMESTATE == PLAYING) {
    	    playGame(g);
    	    
	    }
	    if (GAMESTATE == END) {
			playing = false;
            String fileName = "highscores.txt";
            
            
            String line = null;
            ArrayList<Scorecontainer> scores = new ArrayList<Scorecontainer>();
            int count = 0;
            String key = "";
            String value = "0";
            try {
                if (windowResizeCount < 1) {
                FileWriter fw = new FileWriter(fileName,true);
                BufferedWriter bufferedWriter = new BufferedWriter(fw); 
                bufferedWriter.write("(\""+nickName+"\","+myScore+")\n");
                bufferedWriter.close();
                FileReader fileReader = new FileReader(fileName);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                while((line = bufferedReader.readLine()) != null) {
                    String s = line;
                    if(!s.isEmpty()) {
                        for (String i : s.substring(1,s.length()-1).split(",")) {
                            if(count < 2 && count % 2 == 0) {
                                key = i;
                            } else if (count < 2 && count % 2 == 1) {
                                value = i;
                            }
                            count++;
                        } 
                    }
                    count = 0;
                    Scorecontainer score = new Scorecontainer(key, Integer.valueOf(value));
                    scores.add(score);
                }
                windowResizeCount++;
                bufferedReader.close();
                }
                else {
                    FileReader fileReader = new FileReader(fileName);

                
                    BufferedReader bufferedReader = new BufferedReader(fileReader);
                    while((line = bufferedReader.readLine()) != null) {
                        String s = line;
                        for (String i : s.substring(1,s.length()-1).split(",")) {
                            if(count < 2 && count % 2 == 0) {
                                key = i;
                            } else if (count < 2 && count % 2 == 1) {
                                value = i;
                            }
                            count++;
                        } 
                        
                        count = 0;
                        Scorecontainer score = new Scorecontainer(key, Integer.valueOf(value));
                        scores.add(score);

                }
               bufferedReader.close();


            }}
            catch(FileNotFoundException ex) {
                System.out.println(
                    "Unable to open file '" + 
                    fileName + "'");                
            }
            catch(IOException ex) {
                System.out.println(
                    "Error reading file '" 
                    + fileName + "'");                  
            }
            showHighScores(g,scores);
	    }
		
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(COURT_WIDTH, COURT_HEIGHT);
	}
}