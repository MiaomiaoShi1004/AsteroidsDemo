package com.example.demo;

import java.net.URL;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;


public class Main extends Application {
	
	public static int WIDTH = 600;
	public static int HEIGHT = 400;/**/
	
	
	private static Stage stage;
	
	String css = this.getClass().getResource("style.css").toExternalForm();
	
	@Override
	public void start(Stage primaryStage) throws Exception {
	    
		Main.stage = primaryStage;

	    // Create a VBox for the start screen
	    VBox startScreen = new VBox(10);
	    startScreen.setPrefSize(WIDTH, HEIGHT);
	    startScreen.getStyleClass().add("start-screen");

	    // Create a Label for the title of the game
	    Label titleLabel = new Label("Asteroids");
	    titleLabel.getStyleClass().add("title-label");

	    // Create a TextField for the user to enter their name
	    Label nameInputLabel = new Label("Enter your name: ");
	    nameInputLabel.getStyleClass().add("name-input-label");
	    TextField nameInput = new TextField();
	    nameInput.getStyleClass().add("name-input");

	    // Create a Button to start the game
	    Button startButton = new Button("Start");
	    startButton.setOnAction(e -> {
	        try {
	            startGame(nameInput.getText());
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	    });
	    startButton.getStyleClass().add("start-button");

	    // Add the components to the VBox
	    startScreen.getChildren().addAll(titleLabel, nameInputLabel, nameInput, startButton);
	    
	    // Load the stylesheet
	    try {
	    	Scene startScene = new Scene(startScreen);
	    	startScene.getStylesheets().add(css);
	    	stage.setTitle("Asteroids");
		    stage.setScene(startScene);
		    stage.show();
	    } catch(Exception e) {
	    	e.printStackTrace();
	    }
	    
	}

	public static void startGame(String playerName) throws Exception {
	    Game game = new Game(stage, playerName);
	    game.start();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}