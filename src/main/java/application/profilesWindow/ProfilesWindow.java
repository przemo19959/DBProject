package application.profilesWindow;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ProfilesWindow {
	private Button button;
	public ProfilesWindow(Button button) {
		this.button=button;
	}
		
	public void init() throws IOException {
		button.setDisable(true);
		Stage stage=new Stage();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("profilesWindow.fxml"));
		BorderPane root = loader.load();
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("profiles.css").toExternalForm());
		stage.getIcons().add(new Image("file:G:/java-workspace/DBProject/icon.png"));
		stage.setTitle("Choose your profile");
		stage.setScene(scene);
		stage.show();
		stage.setAlwaysOnTop(true);
		stage.setOnCloseRequest(val->button.setDisable(false));
	}
}
