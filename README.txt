=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 120 Game Project README
PennKey: _______
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an approprate use of the concept. You may copy and paste from your proposal
  document if you did not change the features you are implementing.

  1. Complex Collisions: Using images of objects themselves, I was able to make a
  Collision Method (See GameCourt.Java) that compares pixel transparency between two
  images. If there is a region where both images overlap and do not have two pixels
  they are marked as being collided. I also used some simple physics equations to figure
  out bouncing when two object collide under shield mode.

  2. File I/O: Utilized File I/O to display top scores in game and input player scores
  as well. All scores are stored in highscores.txt and 1 name is on each line
  formatted as ("NAME", SCORE). The file itself is not sorted by score, but the game
  reads the file and then sorts the list automatically.

  3. Modeling State Using Collections: All objects in the game are stored in an allObjects
  TreeSet. Objects to be added in the next cycle are stored in a toAdd treeSet. All Objects
  have a "Valid" parameter which is marked true if they are to stay in the game or False otherwise.
  On every tick, the allObjects treeSet is parsed through and objects are updated.

  4.Object Oriented Design Using Inheritance and Sub Typing. All sprites (asteroids, ship, lasers,
  powerups) extend the general GameObj class which contains methods that determine each sprite's
  movement, game state, drawing parameters, etc. The specific sprite classes are used for
  instantiating the objects with specific parameters.


=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.
    Game - Used to start up game and set up JPanel as well as basic controllers
    GameCourt - Contains all methods to draw game screen and handle basic game logic
                     Has class for complex collision, updating on tick based on game state,
                     and reading/writing IO
    GameObj - Has all methods to instantiate and draw all objects as well as maintain their
              parameters.
    Laser - Instantiates Laser with specific parameters
    PowerUp - Instantiates PowerUp with specific parameters.
    Spaceship - Instantiates Space Ship with specific parameters.
    Asteroid - Instantiates Asteroid object with specific paramters
    Scorecontainer - Used to instantiate Scorecontainer object used during File I/O

- Revisit your proposal document. What components of your plan did you end up
  keeping? What did you have to change? Why?
  I ended up keeping most of the components other than how the classes were split up.

- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?
  JOptionPanes would not work on Linux computers so I had to figure out an alternative. I managed
  to get one option frame working by placing it earlier in the code, but I did have to manually
  draw out the high score table.

- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?
  I believe the GameCouter.java file could have been split up into multiple classes as I feel
  it blended in the Model and View aspects of MVC a bit too much. But overall, there are no
  major places where I would refactor the game other than that class.



========================
=: External Resources :=
========================

- Cite any external resources (libraries, images, tutorials, etc.) that you may
  have used while implementing your game.
  Asteroids:
    http://deepspaceindustries.com/wp-content/uploads/2015/01/asteroid-png-0.png
    http://www.rhysy.net/Science/QE2circumference.png
    http://ojasper.nl/src/space_bodies/doc/asteroid.png
    http://opengameart.org/sites/default/files/styles/watermarked/public/1346944555.png
  Spaceship:
    thenounproject.com
        Rocket - By Antonis Makriyannis, GR
  Laser:
    ScreenCapture: http://www.jharbour.com/BeginningJava/
  Observed example implementation from http://www.jharbour.com/BeginningJava/
  StackOverflow.com...countless times
