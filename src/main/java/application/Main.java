package application;
	
import application.profilesWindow.ProfilesWindowController;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	private ProfilesWindowController profCon;
	
	
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(ProfilesWindowController.class.getResource("profilesWindow.fxml"));
			AnchorPane root = loader.load();
			profCon=loader.getController();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.getIcons().add(new Image("file:G:/java-workspace/DBProject/icon.png"));
			profCon.setMainScene(scene, primaryStage);
			primaryStage.setTitle("Profiles");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
