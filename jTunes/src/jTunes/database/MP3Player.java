package jTunes.database;
import java.io.File;
import java.net.URISyntaxException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MP3Player extends Application {
	
	private volatile static boolean loop; // Used to track the toggle loop
	private final static Duration a = new Duration(5000); // 5 seconds used to forward and back 5 seconds
	private volatile static MediaPlayer mediaPlayer;
	
	@Override
	public void start(Stage stage) throws URISyntaxException {
		// Getting the parameters and finding the song path
		Parameters params = getParameters(); 
		mediaPlayer = JfxMP3Player.loadSong(params.getRaw().get(0));
		
		// Buttons used
		Button btn_play = new Button("Play");
		Button btn_pause = new Button("Pause");
		Button btn_exit = new Button("Exit"); // Most likely won't be needed for GUI
		Button btn_stop = new Button("Stop");
		Button btn_backToBeginning = new Button("Go back to the beginning");
		Button btn_toggleLoop = new Button("Toggle Loop");
        Label loopStatus = new Label("false");
		Button btn_back5 = new Button("Back 5 seconds");
		Button btn_fwd5 = new Button("Forward 5 seconds");
		
		// Implementation of the buttons
		btn_play.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				mediaPlayer.play();
			}
		});
		
		btn_pause.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				mediaPlayer.pause();
			}
		});
		
		btn_exit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				mediaPlayer.stop();
				//mediaPlayer.dispose();
				//stage.close();
			}
		});
		
		btn_stop.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				mediaPlayer.stop();
			}
		});
		
		btn_backToBeginning.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				mediaPlayer.stop();
				mediaPlayer.play();
				
			}
		});
		
		btn_toggleLoop.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				loop = !loop;
				if(loop)
					mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
				else
					mediaPlayer.setCycleCount(1);
				loopStatus.setText(Boolean.toString(loop));
			}
		});
	
		btn_back5.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				Duration newDuration = mediaPlayer.getCurrentTime().subtract(a);
				mediaPlayer.seek(newDuration);
			}
		});
		
		btn_fwd5.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				Duration newDuration = mediaPlayer.getCurrentTime().add(a);
				mediaPlayer.seek(newDuration);
			}
		});
	  
		mediaPlayer.setAutoPlay(true); // Will start the song automatically
		
		
		// Used to set up the GUI
		VBox root = new VBox();
		root.getChildren().addAll(btn_play, btn_pause, btn_exit, btn_stop, btn_backToBeginning, btn_toggleLoop, loopStatus, btn_back5, btn_fwd5);
		Scene scene = new Scene(root, 500, 500);
		stage.setScene(scene);    
		stage.setTitle("JTunes");
    	stage.setWidth(200);
    	stage.setHeight(275);
    	stage.show();
    }	
	
	public void loadNewMP3File(String filename) throws URISyntaxException{
		mediaPlayer.stop();
		mediaPlayer.dispose();
		mediaPlayer = JfxMP3Player.loadSong(filename);
		mediaPlayer.play();
	}
	
}