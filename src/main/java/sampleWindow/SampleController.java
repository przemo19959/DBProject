package sampleWindow;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import application.converters.TextConverter;
import application.hierarchy.Record;
import application.hierarchy.Table;
import application.operations.DBOperations;
import application.operations.OperationImpl;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class SampleController implements Service {
	@FXML
	private Label infoBar;
	@FXML
	private TableView<Record> tableView;
	@FXML
	private ComboBox<String> tableListBox;
	@FXML
	private Button connectButton;
	@FXML
	private Button disconnectButton;
	@FXML
	BorderPane mainPane;
	@FXML
	Button insertQueryButton;
	@FXML
	Button deleteRecordButton;

	private DBOperations operations;
	private ObservableList<Record> data; // lista obserwowalna
	private boolean dbIsClosed = true;
	private ArrayList<TableColumn<Record, String>> columns = new ArrayList<>();
	private Timeline fadingTimeLine;
	
	//na potrzeby testu
	private long startTime=0;
	
	private String getSelectedTableName() {
		return tableListBox.getSelectionModel().getSelectedItem();
	}

	@FXML
	void initialize() {
		fadingLabelInit(infoBar);
		tableView.setDisable(true);
				
		// po³¹cz/roz³¹cz z baz¹ danych
		connectButton.setOnAction(ev ->connect());
		disconnectButton.setOnAction(ev -> disconnect());

		// wybór tabeli z bazy danych poprzez wybranie nazwy tabeli z combo-box
		tableListBox.getSelectionModel().selectedItemProperty().addListener((obs, old, next) -> {
			if(!dbIsClosed) {
				try {
					
					startTime=System.currentTimeMillis();
					showTable(operations.selectAllRecords(next));
					System.out.println((System.currentTimeMillis()-startTime)+"[ms]");	//test ile zajmuje ³adowanie
					
					insertQueryButton.setDisable(false);
					deleteRecordButton.setDisable(false);
					startFading("SELECT query done :-)");
				} catch (SQLException e) {
					infoBar.setText(e.getMessage());
				}
			}
		});

		// regulacja okna i szerokoœci elementów.
		mainPane.heightProperty().addListener((obs, old, next) -> tableView.setPrefHeight(next.longValue()* 0.65));
		
		// obs³uga przycisku dodaj¹cego nowy rekord w tabeli.
		insertQueryButton.setOnAction(ev -> {
			if(!dbIsClosed && data!= null) {
				try {
					operations.insertEmptyRecord();
					showTable(operations.selectAllRecords(getSelectedTableName()));
					startFading("INSERT INTO query done :-)");
				} catch (SQLException e) {
					infoBar.setText(e.getMessage());
				}
			}
		});
		
		//obs³uga przycisku usuwaj¹cego wskazany rekord z tabeli.
		deleteRecordButton.setOnAction(ev -> {
			if(!dbIsClosed && data!= null) {
				int id = Integer.valueOf(tableView.getSelectionModel().getSelectedItems().get(0).getCell(0).get());
				try {
					operations.deleteRecord(id);
					showTable(operations.selectAllRecords(getSelectedTableName()));
					startFading("DELETE query done :-)");
				} catch (SQLException e) {
					infoBar.setText(e.getMessage());
				}
			}
		});
	}

	// funkcja aktualizuj¹ca listê obserwowaln¹ oraz kolumny w widoku
	@Override
	public void showTable(Table table) {
		columns.clear();
		tableView.getColumns().clear();

		for(int i = 0;i< table.getColumnNames().size();i++) {
			final int x = i;
			String item = table.getColumnName(i);
			columns.add(new TableColumn<>(item)); // dodaj kolumnê
			columns.get(x).setEditable(true);
			columns.get(x).setCellValueFactory(param -> param.getValue().getCell(x));
			if(operations.getMap().containsKey(item)) // jeœli nazwa kolumny rozpoczyna siê od fk -> klucz obcy
				columns.get(x).setCellFactory(value -> new ComboBoxTableCell<>(FXCollections.observableArrayList(operations.getMap().get(item)))); // combobox dla kolumn, bêd¹cych kluczem obcym
			else
				columns.get(x).setCellFactory(value -> new TextFieldTableCell<>(new TextConverter())); // dla pozosta³ych zwyk³e pola tekstowe

			columns.get(x).setOnEditCommit(val -> {
				try {
					int index = operations.update(x, val.getNewValue(), val.getTablePosition().getRow());
					startFading("UPDATE query done affected row: "+val.getTablePosition().getRow()+" :-)");
					if(index== 0)
						data.get(val.getTablePosition().getRow()).setCell(x, val.getOldValue());
					else
						data.get(val.getTablePosition().getRow()).setCell(x, val.getNewValue());
				} catch (SQLException e) {
					infoBar.setText(operations.processSQLError(e.getSQLState()));
					data.get(val.getTablePosition().getRow()).setCell(x, val.getOldValue()); // pozostaw pole bez zmiany
					tableView.refresh(); // odœwie¿ widok
				}
			});
		}
		data = FXCollections.observableArrayList(table.getRecords());
		tableView.setItems(data);
		tableView.getColumns().addAll(columns);
	}

	@Override
	public void connect() {
		if(dbIsClosed) {
			try {
				operations = new OperationImpl(); // utworzenie po³¹czenia z baz¹ oraz query
				dbIsClosed = false;
				tableView.setDisable(false);
				startFading("Successfully connected to db :-)");
				// za³adowanie nazw tablic do comboxa
				List<String> tmp = operations.getTables();
				tableListBox.getItems().addAll(tmp);
				connectButton.setDisable(true);
				disconnectButton.setDisable(false);
			} catch (SQLException | ClassNotFoundException e) {
//				infoBar.setText(e.getMessage());
				startFading("B³¹d: "+e.getMessage());
			}
		}
	}
	
	@Override
	public void disconnect() {
		if(!dbIsClosed) {
			try {
				operations.disconnect();
				dbIsClosed = true;
			} catch (SQLException e) {
				infoBar.setText(e.getMessage());
			}
			tableView.setDisable(true);
			tableView.getItems().clear();
			insertQueryButton.setDisable(true);
			deleteRecordButton.setDisable(true);
			startFading("Successfully disconnected from db :-)");
			tableListBox.getItems().clear();
			connectButton.setDisable(false);
			disconnectButton.setDisable(true);
		}
	}
	/**
	 * This method initialize fading label animation. Fill property of label is animated. Start value is black, and
	 * during 3.5s value becomes rgb(128,128,128) (that is color of label background), so that label becomes invisble with time.
	 * @param fadingLabel label which is to be animated.
	 */
	private void fadingLabelInit(Label fadingLabel) {
		final KeyValue value1=new KeyValue(fadingLabel.textFillProperty(), Color.BLACK);
		final KeyFrame frame1=new KeyFrame(Duration.ZERO, value1);
		final KeyValue value2=new KeyValue(fadingLabel.textFillProperty(), Color.rgb(128, 128, 128));
		final KeyFrame frame2=new KeyFrame(Duration.millis(3500), value2);
		fadingTimeLine=new Timeline(frame1,frame2);
	}
	
	/**
	 * This method starts fading effect. Firstly any actually running animation is stopped and returned to starting position.
	 * Then text of label is set. Next animation is fired, so that user sees fading effect of label.
	 * @param text new value of label content.
	 */
	private void startFading(String text) {
		fadingTimeLine.stop();
		infoBar.setText(text);
		fadingTimeLine.play();
	}

}
