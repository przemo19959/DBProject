package application.profilesWindow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import application.files.FileService;
import application.files.FileServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

public class ProfilesWindowController {

	@FXML
	BorderPane root;
	@FXML
	AnchorPane mainPane;
	@FXML
	ListView<String> profilesList;
	@FXML
	Button addProfileButton;
	@FXML
	Button deleteProfileButton;
	@FXML
	Button commitButton;
	@FXML
	Button chooseButton;
	@FXML
	TextField profileNameField;
	@FXML
	TextField urlAddressField;
	@FXML
	TextField usernameField;
	@FXML
	TextField passwordField;
	@FXML
	Label infoLabel;

	private ObservableList<String> obs;
	private FileService fileService = new FileServiceImpl();
	@FXML Label profileInfo;

	@FXML
	void initialize() {
		try {
			addProfilesToList();
		} catch (IOException e) {
			e.printStackTrace();
		}

		addProfileButton.setOnAction(ev -> {
			if(profileNameField.isDisabled()) {
				disableGroup(false);
			}
		});
		
		//obs³uga przycisku potwierdzaj¹cego
		commitButton.setOnAction(ev -> {
			//jeœli któreœ pole jest puste
			if(profileNameField.getText().isEmpty()|| urlAddressField.getText().isEmpty()|| usernameField.getText().isEmpty()|| passwordField.getText().isEmpty()) {
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
			String item = profilesList.getSelectionModel().getSelectedItem();
			if(!item.equals("")) {
				chooseButton.setDisable(false);
				String[] infos=getProfileInfo(item);
				profileInfo.setText("Profile Name: "+infos[0]+"\nDatabase URL: "+infos[1]+"\nDatabase username: "+infos[2]);
			} else {
				chooseButton.setDisable(true);
				profileInfo.setText("");
			}
		});
	}

	private void addNewProfileToFile() {
		try {
			fileService.writeTxtFile("profiles", profileNameField.getText()+ FileService.separator+ urlAddressField.getText()+ FileService.separator+ usernameField.getText()+ FileService.separator
													+ passwordField.getText()+ "\n");
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
				return profiles.get(i).split(FileService.separator);
			}
		}
		return new String[] {};
	}
}
