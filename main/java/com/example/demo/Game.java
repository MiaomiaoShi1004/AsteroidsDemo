package com.example.demo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TimerTask;
import java.util.Timer;
import java.util.stream.Collectors;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Polygon;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class Game {
	
    private Stage stage;
    private String playerName;
    private int score = 0;
    private int life = 3;
    private int time = 0;
    private Label scoreLabel;
    private Label lifeLabel;
    private Label timeLabel;
    private Pane pane;
    private Ship ship;
    private Alien alien;
    private Projectile projectile;
    public static List<Asteroid> asteroids = new ArrayList<>();
    public static int numAsteroids = 10;
    
    String css = this.getClass().getResource("style.css").toExternalForm();
    
    public Game(Stage stage, String playerName) {
        this.stage = stage;
        this.playerName = playerName;
        this.scoreLabel = new Label("Score: 0");
        this.lifeLabel = new Label("Life: 3");
        this.timeLabel = new Label("Time: 0");
    }
    
    public void updateScore() {
    	score += 1;
    	scoreLabel.setText("Score: " + score);
    }
    
    public void updateLife() {
    	life -= 1;
    	lifeLabel.setText("Life: " + life);
    }
    
    public void addLife() {
    	life += 1;
    	lifeLabel.setText("Life: " + life);
    }
    
    public void updateTime() {
    	time += 1;
    	timeLabel.setText("Time: " + time);
    }
    
    // Takes a number and an empty list of asteroids
    public List<Asteroid> createAsteroids(int numAsteroids, List<Asteroid> asteroids) {
		for(int i=asteroids.size(); i<numAsteroids; i++) {
			Asteroid newAsteroid = this.addAsteroid();
			newAsteroid.addStyleClass("asteroid");
			asteroids.add(newAsteroid);
		}
		return asteroids;
    }
    
    public Ship createShip() {
    	this.ship = new Ship(300,200);
    	this.ship.addStyleClass("ship");
    	return ship;
    }
    
    public Alien createAlien() {
    	this.alien = new Alien(0,30,2);
    	this.alien.addStyleClass("alien");
    	return alien;
    }
    
    public Projectile createProjectile(int x, int y) {
    	this.projectile = new Projectile(x,y);
    	this.projectile.addStyleClass("projectile");
    	return projectile;
    }
    
    public Asteroid addAsteroid() {
        Random rnd = new Random();
        Asteroid newAsteroid = new Asteroid(rnd.nextInt(600), rnd.nextInt(400));
        return newAsteroid;
    }
    
    public void gameOverScreen() {
    	// Create a VBox for the start screen
	    VBox gameOver = new VBox(10);
	    gameOver.setPrefSize(Main.WIDTH, Main.HEIGHT);
	    gameOver.getStyleClass().add("start-screen");

	    // Create a Label for the title of the game
	    Label titleLabel = new Label("Game Over");
	    titleLabel.getStyleClass().add("title-label");
	    
	    Label scoreLabel = new Label(playerName + " your score was = " + score);
	    scoreLabel.getStyleClass().add("score-label");
	    
	    Label playAgain = new Label("Woud you like to play again?");
	    playAgain.getStyleClass().add("restart-label");
	    
	    // Create a Button to re play the game
	    Button playAgainButton = new Button("Play Again");
	    playAgainButton.setOnAction(e -> {
	        try {
	            Main.startGame(playerName);
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	    });
	    playAgainButton.getStyleClass().add("start-button");
	     
	    gameOver.getChildren().addAll(titleLabel, scoreLabel, playAgain, playAgainButton);
	    
	    Scene endScene = new Scene(gameOver);
    	endScene.getStylesheets().add(css);
    	stage.setTitle("Game Over");
	    stage.setScene(endScene);
	    stage.show();

    }
    
    public int levelUpAsteroidCount(int numAsteroids, List<Asteroid> asteroids) {
    	
    	// Increase the asteroids by 25% each level
    	System.out.print("Current number of asteroids = " + asteroids.size() + "\n");
    	int currentNumAsteroids = asteroids.size();
        int additionalAsteroids = (int) Math.ceil(numAsteroids * 0.25);
        numAsteroids += additionalAsteroids;
        createAsteroids(numAsteroids, asteroids);
        System.out.print("New target number of asteroids = " + asteroids.size() + "\n");
        System.out.print("Number of asteroids to be created = " + (asteroids.size() - currentNumAsteroids) + "\n");
        
        // Loop through the newly added asteroids and add them to the pane
        for (int i = currentNumAsteroids; i < asteroids.size(); i++) {
            Polygon asteroidCharacter = asteroids.get(i).getCharacter();
            pane.getChildren().add(asteroidCharacter);
        }
        return numAsteroids;
    }
    
    
    public void start() throws Exception {
    	
    	
    	// Track time and add 1 to the players score every half second.
    	Timeline timelineScore = new Timeline(new KeyFrame(Duration.seconds(0.01), event -> {
    		updateScore();
    		if(score%10000 == 0) {
    			addLife();
    		}
    		}));
        timelineScore.setCycleCount(Timeline.INDEFINITE);
        timelineScore.play();
        
        // Track time and add 1 to the timer every second
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
    		updateTime();
    		}));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        
    	// Create a new pane for the game to run in
		pane = new Pane();
		pane.setPrefSize(600, 400);
		
		// Place the score, life and time labels on the pane
		scoreLabel.setLayoutX(10);
	    scoreLabel.setLayoutY(10);
	    lifeLabel.setLayoutX(10);
	    lifeLabel.setLayoutY(45);
	    timeLabel.setLayoutX(10);
	    timeLabel.setLayoutY(80);
	    scoreLabel.getStyleClass().add("score-label");
	    lifeLabel.getStyleClass().add("life-label");
	    timeLabel.getStyleClass().add("time-label");
	    pane.getChildren().addAll(scoreLabel, lifeLabel, timeLabel);
	    
		// Create a new scene from the pane
		Scene scene = new Scene(pane);
		scene.getStylesheets().add(css);
		
		pane.getStyleClass().add("game-background");
		
		// Create a ship
		ship = createShip();
		
		// Create alien
		alien = createAlien();
		
		// multiple projectiles (empty at first)
        List<Projectile> projectiles = new ArrayList<>();
        
        // multiple projectiles fired by alien (empty at first)
        List<Projectile> projectilesAlien = new ArrayList<>();
		
		// Create asteroids
		createAsteroids(numAsteroids, asteroids);
		
		// Add elements to pane
		pane.getChildren().add(ship.getCharacter());
		asteroids.forEach(asteroid -> pane.getChildren().add(asteroid.getCharacter()));
		
		
		// Create a hash set to track the pressed keys
		Set<KeyCode> pressedKeys = new HashSet<>();
		
		// When keys are pressed, add them to the pressedKeys hash set
		scene.setOnKeyPressed(new EventHandler<KeyEvent>(){
			public void handle(KeyEvent event) {
				pressedKeys.add(event.getCode());
				// Print to the console to see what's happening
				System.out.println(pressedKeys);
			};
		});
		
		// When keys are released, remove them from the pressedKeys hash set
		scene.setOnKeyReleased(new EventHandler<KeyEvent>(){
			// handle is called to manage the event handler (key released)
			public void handle(KeyEvent event) {
				pressedKeys.remove(event.getCode());
				// Print to the console to see what's happening
				System.out.println(pressedKeys);
			};
		});
		
		// Track time and after 15 seconds, level up
        Timeline levelUpTimeline = new Timeline(new KeyFrame(Duration.seconds(15), event -> {
        	numAsteroids = levelUpAsteroidCount(numAsteroids, asteroids);
        }));
        levelUpTimeline.setCycleCount(Timeline.INDEFINITE);
        levelUpTimeline.play();
        
		
		// Animation Class - Runs all the animation for the game
		new AnimationTimer() {
			
			public void gameOver() {
	        	System.out.println("Game over! You have been hit by alien.");
	            stop();
	            timeline.stop();
	    		levelUpTimeline.stop();
	    		timelineScore.stop();
	    		gameOverScreen();
	        }
			
			public void restart() {
				// Have the ship re spawn in a safe area and reduce the life by 1
				ship.hyperSpaceJump();
				updateLife();
				Timer timer = new Timer();
				ship.addStyleClass("red");
				timer.schedule(new TimerTask() {
				    @Override
				    public void run() {
				        ship.removeStyleClass("red");
				    }
				}, 100);
				timer.schedule(new TimerTask() {
				    @Override
				    public void run() {
				        ship.addStyleClass("red");
				    }
				}, 200);
				timer.schedule(new TimerTask() {
				    @Override
				    public void run() {
				        ship.removeStyleClass("red");
				    }
				}, 300);
			}
			
			private long lastBullet = 0; //for bullet to not be continuous
            double startTime = System.currentTimeMillis(); //set generate alien game start time
            int alienFlag = 0; //a flag for alien ship appearing
            double lastAlienBullet = 0; //a timer for fire shooting speed of alien
			public void handle(long now) {
				// If the pressedKeys set contains the LEFT key
				// rotate the ship 5 degrees left continuously
				if(pressedKeys.contains(KeyCode.LEFT)) {
					ship.turnLeft();
				}
				
				// If the pressedKeys set contains the RIGHT key
				// rotate the ship 5 degrees right continuously
				if(pressedKeys.contains(KeyCode.RIGHT)) {
					ship.turnRight();
				}
				
				//If the pressedKeys set contains the UP key
				// move the ship forward
				if(pressedKeys.contains(KeyCode.UP)) {
					ship.accelerate();
				}
				
				// hyper jump
				if(pressedKeys.contains(KeyCode.H)) {
                    ship.hyperSpaceJump();
                }
				
				if (pressedKeys.contains(KeyCode.SPACE) && (now - lastBullet > 300_000_000)) {
	                // press space for shooting
	                // && limit the number of projectiles
	                if (pressedKeys.contains(KeyCode.SPACE) && projectiles.size() < 5) {
	                    // create a projectile and its direction is the same as the ship's direction.
	                    Projectile projectile = createProjectile((int) ship.getCharacter().getTranslateX(), (int) ship.getCharacter().getTranslateY());
	                    projectile.getCharacter().setRotate(ship.getCharacter().getRotate());
	                    projectiles.add(projectile);

	                    projectile.accelerate();
	                    projectile.setMovement(projectile.getMovement().normalize().multiply(3));
	                    // Present the projectile in the screen
	                    pane.getChildren().add(projectile.getCharacter());
	                    lastBullet = now;
	                }
				}
				
				// Change the position of the ship based on the acceleration
				ship.move();
				asteroids.forEach(asteroid -> asteroid.move());
				projectiles.forEach(projectile -> projectile.move());
                projectilesAlien.forEach(projectile -> projectile.move());
				
				//select a random time to generate alien ship, and make it move
                Random rnd = new Random();
                if (System.currentTimeMillis()-startTime > rnd.nextInt(10000, 50000) && alienFlag == 0) {
                    alien.setLives(2); //set live for alien ship
                    pane.getChildren().add(alien.getCharacter()); //add alien ship to pane
                    alienFlag++; // alienFlag increment
                    startTime = System.currentTimeMillis(); // refresh start time
                }
                
                if (alien.getLives() > 0) { //if the time is in random time bound, live of alien will be set to 1, and make it move
                    // alien to move
                    alien.move();
                }
                
                // let the alien ship shooting toward the user ship every 3 seconds
                if (alien.getLives() != 0 && System.currentTimeMillis()-lastAlienBullet > 3000 && alienFlag == 1) {
                    System.out.println("Alien fire!!!");
                    Projectile projectile = createProjectile((int) alien.getCharacter().getTranslateX(), (int) alien.getCharacter().getTranslateY());
                    // calculate the direction
                    double userx = ship.getCharacter().getTranslateX();
                    double usery = ship.getCharacter().getTranslateY();
                    double alienx = alien.getCharacter().getTranslateX()+10;
                    double alieny = alien.getCharacter().getTranslateY()+10;
                    double distance = Math.sqrt((userx-alienx)*(userx-alienx) + (usery-alieny)*(usery-alieny));
                    double bulletRotate1 = Math.toDegrees(Math.asin(Math.abs((usery-alieny))/distance));
                    double bulletRotate2 = 180-Math.toDegrees(Math.asin(Math.abs((usery-alieny))/distance));
                    //set the direction of bullets from alien
                    if (userx > alienx) {
                        if (usery < alieny) {
                            projectile.getCharacter().setRotate(alien.getCharacter().getRotate()-bulletRotate1);
                        }
                        else {
                            projectile.getCharacter().setRotate(alien.getCharacter().getRotate()+bulletRotate1);
                        }
                    }
                    else {
                        if (usery < alieny) {
                            projectile.getCharacter().setRotate(alien.getCharacter().getRotate()-bulletRotate2);
                        }
                        else {
                            projectile.getCharacter().setRotate(alien.getCharacter().getRotate()+bulletRotate2);
                        }
                    }
                    projectilesAlien.add(projectile); //add alien bullet to general bullet list
                    projectile.accelerate();
                    projectile.setMovement(projectile.getMovement().normalize().multiply(2));
                    // Present the projectile in the screen
                    pane.getChildren().add(projectile.getCharacter());

                    lastAlienBullet = System.currentTimeMillis();
                }

                // stops the application if alien projectile hit ship
                projectilesAlien.forEach(projectile -> {
                	if (ship.collide(projectile)) {
                    	System.out.println("1 Life Lost");
                    	if(life>=0) {
                    		restart();
                    	}
             
                    	if(life<0) {
                    		System.out.println("Life = 0. Game Over.");
                    		gameOver();
                    	}
                    }
                });

                // projectile hit asteroids
                projectiles.forEach(projectile -> {
                    List<Asteroid> collisions = asteroids.stream().filter(asteroid -> asteroid.collide(projectile)).collect(Collectors.toList());
                    // Removed asteroid from the asteroid list
                    collisions.stream().forEach(collided -> {
                        asteroids.remove(collided);
                        pane.getChildren().remove(collided.getCharacter());
                    });
                });

                //  Using iterator to safely remove collided projectiles and asteroids
                Iterator<Projectile> projectileIterator = projectiles.iterator();
                while (projectileIterator.hasNext()) {
                    Projectile projectile = projectileIterator.next();

                    Iterator<Asteroid> asteroidIterator = asteroids.iterator();
                    boolean projectileCollided = false;
                    while (asteroidIterator.hasNext()) {
                        Asteroid asteroid = asteroidIterator.next();
                        if (asteroid.collide(projectile)) {
                            projectileCollided = true;
                            asteroidIterator.remove();
                            pane.getChildren().remove(asteroid.getCharacter());
                        }
                    }

                    //  Removing collided projectiles
                    if (projectileCollided) {
                        projectileIterator.remove();
                        pane.getChildren().remove(projectile.getCharacter());
                    }
                }
                
                // projectile hit Alien
                projectiles.forEach(projectile -> {
                    if (alien.collide(projectile)) {
                        int alienLife = alien.getLives();
                        if (alienLife > 0){
                            alien.setLives(alienLife-1);
                        }
                        alienLife = alien.getLives();
                        if (alienLife == 0){
                            System.out.println("The alien ship have 0 life");
                            pane.getChildren().remove(alien.getCharacter());
                        }
                    }
                });
                
                // stops the application if a collision happens
                asteroids.forEach(asteroid -> {
                    if (ship.collide(asteroid)) {
                    	System.out.println("1 Life Lost");
                    	if(life>=0) {
                    		restart();
                    	}
             
                    	if(life<0) {
                    		System.out.println("Life = 0. Game Over.");
                    		gameOver();
                    	}
                    }
                });
            }
		}.start();
		
		stage.setScene(scene);
		stage.show();
	}
}