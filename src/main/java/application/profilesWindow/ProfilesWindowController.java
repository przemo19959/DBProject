package application.profilesWindow;

import java.io.IOException;
import java.util.List;

import application.files.FileService;
import application.files.FileServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import sampleWindow.SampleController;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.ListView;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

public class ProfilesWindowController {

	@FXML
	private BorderPane root;
	@FXML
	private AnchorPane mainPane;
	@FXML
	private ListView<String> profilesList;
	@FXML
	private Button addProfileButton;
	@FXML
	private Button deleteProfileButton;
	@FXML
	private Button commitButton;
	@FXML
	private Button chooseButton;
	@FXML
	private TextField profileNameField;
	@FXML
	private TextField urlAddressField;
	@FXML
	private TextField usernameField;
	@FXML
	private TextField passwordField;
	@FXML
	private Label infoLabel;

	private ObservableList<String> obs;
	private FileService fileService = new FileServiceImpl();
	@FXML private Label profileInfo;
	private Scene mainScene;
	private Stage mainStage;
	private ActualProfile profile=ActualProfile.getInstance();
	private FXMLLoader loader = new FXMLLoader(SampleController.class.getResource("Sample.fxml"));
	
	public void setMainScene(Scene mainScene, Stage stage) {
		this.mainScene = mainScene;
		this.mainStage = stage;
	}
	
	private void changeToSampleWindow() throws IOException {
		mainScene.setRoot(loader.load());
		mainStage.sizeToScene();
		mainStage.centerOnScreen();
		
		SampleController con=loader.getController();
		con.setMainScene(mainScene, mainStage);
	}

	@FXML
	void initialize() {
		try {
			addProfilesToList();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		addProfileButton.setOnAction(ev -> {
			infoLabel.setText("");
			if(profileNameField.isDisabled()) {
				disableGroup(false);
			}
		});
		
		//obs³uga przycisku potwierdzaj¹cego
		commitButton.setOnAction(ev -> {
			//jeœli któreœ pole jest puste
			if(profileNameField.getText().isEmpty() || urlAddressField.getText().isEmpty() || usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
				infoLabel.setStyle("-fx-text-fill:purple;");
				infoLabel.setText("Set all textFields!");
			} else {
				if(!obs.contains(profileNameField.getText())) {	//jeœli nowy profil
					addNewProfileToFile();
					
					obs.add(profileNameField.getText());
					clearFields();

					infoLabel.setStyle("-fx-text-fill:green;");
					infoLabel.setText("Profile added!");

					disableGroup(true);
				} else {	//jeœli lista zawiera profil o tej nazwie.
					infoLabel.setStyle("-fx-text-fill:purple;");
					infoLabel.setText("Profile already exists!");
					profileNameField.clear();
				}
			}
		});

		profilesList.setOnMouseClicked(ev -> {
			infoLabel.setText("");
			String item = profilesList.getSelectionModel().getSelectedItem();
			if(item!=null && !item.equals("")) {
				chooseButton.setDisable(false);
				deleteProfileButton.setDisable(false);
				String[] infos=getProfileInfo(item);
				profileInfo.setText("Profile Name: "+infos[0]+"\nDatabase URL: "+infos[1]+"\nDatabase username: "+infos[2]);
			} else {
				chooseButton.setDisable(true);
				deleteProfileButton.setDisable(true);
				profileInfo.setText("");
			}
		});
		
		deleteProfileButton.setOnAction(ev->{
			infoLabel.setText("");
			deleteProfile();
			profileInfo.setText("");
		});
		
		chooseButton.setOnAction(ev->{
			String item = profilesList.getSelectionModel().getSelectedItem();
			if(item!=null && !item.equals("")) {
				try {
					changeToSampleWindow();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void addNewProfileToFile() {
		String info=profileNameField.getText()+ FileService.separator+ urlAddressField.getText()+ FileService.separator+ usernameField.getText()+ FileService.separator
				+ passwordField.getText();
		try {
			if(obs.size()>0)
				info="\n"+info;
			fileService.writeTxtFile("profiles",info);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void clearFields() {
		profileNameField.clear();
		urlAddressField.clear();
		usernameField.clear();
		passwordField.clear();
	}

	private void disableGroup(boolean value) {
		profileNameField.setDisable(value);
		urlAddressField.setDisable(value);
		usernameField.setDisable(value);
		passwordField.setDisable(value);
		commitButton.setDisable(value);
	}
	
	private void deleteProfile(){
		String item = profilesList.getSelectionModel().getSelectedItem();
		int index=obs.indexOf(item);
		if(!item.equals("")) {
			obs.remove(index);
			try {
				fileService.deleteRecordFromTxtFile("profiles", index);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		chooseButton.setDisable(true);
		deleteProfileButton.setDisable(true);
	}

	private void addProfilesToList() throws IOException {
		if(!fileService.txtFileExists("profiles"))
			fileService.createTxtFile("profiles");
		List<String> profiles = fileService.readTxtFile("profiles");
		for(int i = 0;i< profiles.size();i++) {
			profiles.set(i, profiles.get(i).substring(0, profiles.get(i).indexOf(FileService.separator)));
		}
		obs = FXCollections.observableArrayList(profiles);
		profilesList.setItems(obs);
	}
	
	private String[] getProfileInfo(String selectedProfile) {
		List<String> profiles=null;
		try {
			profiles=fileService.readTxtFile("profiles");
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(int i=0;i<profiles.size();i++) {
			if(profiles.get(i).startsWith(selectedProfile)) {
				String[] tmp=profiles.get(i).split(FileService.separator);
				profile.setProfile(tmp);
				return tmp;
			}
		}
		return new String[] {};
	}
}
