package application.hierarchy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseImpl implements Database {
	private Connection connection;
	private ResultSet resultSet;
	private List<String> listOfTablesNamesInDatabase;

	private String tableName; // nazwa tabeli
	private List<String> columnNames; // nazwy wszystkich kolumn
	private List<String> columnTypes; // typy danych wszystkich kolumn
	private List<Record> recordList; // rekordy jakie posiada w wyniku podanego zapytania
	private List<String> fkColumns; // lista kolumn bêd¹cych kluczem obcym
	private List<String> pkInfo; // lista informacji o kluczu g³ównym powi¹zanym z kluczem obcym
	private List<String> firstColumnNameInPkTable; // nazwa pierwszej kolumny, tabeli posiadaj¹cej klucz g³ówny do którego odnosi siê klucz obcy
	private Record record;
	
	/**
	 * This constructor creates virtual database object. It gets connection for current database.
	 * Then fills list of tables names from database.
	 * @param args - params to init connection, database URL, username and password as Strings
	 * @throws SQLException
	 */
	public DatabaseImpl(String[] args) throws SQLException {
		// example: "jdbc:postgresql://localhost/db1", "postgres","postgres"
		connection = DriverManager.getConnection("jdbc:postgresql:"+ args[1], args[2], args[3]);
		listOfTablesNamesInDatabase = new ArrayList<>();
		getDatabaseTables(); // pobranie nazw tabel w bazie danych
	}

	@Override
	public List<String> getTablesInDB() {
		return listOfTablesNamesInDatabase;
	}

	@Override
	public int singleReturnQuery(String query) throws SQLException {
		int result = 0;
		try (Statement statement = connection.createStatement()) {
			result = statement.executeUpdate(query);
		}
		return result;
	}

	/**
	 * This method creates new lists for capturing info of table. This info contains
	 * names of every columns, data types of every column, columns which are foreign keys, primary keys
	 * related to previosly mentioned foreign keys, names of first column in table which contains primary key.
	 * Last list contains all content of table, it is list of Record.class objects.
	 */
	private void createLists() {
		columnNames = new ArrayList<>(); 
		columnTypes = new ArrayList<>(); 
		recordList = new ArrayList<>(); 
		fkColumns = new ArrayList<>(); 
		pkInfo = new ArrayList<>(); 
		firstColumnNameInPkTable = new ArrayList<>(); 
	}
	
	/**
	 * This method initializes all lists through <b>createLists()</b> method. Also tableName and record are set to
	 * default values.
	 */
	private void initVariables() {
		tableName = ""; // nazwa tabeli
		createLists();
		record = null;
	}
	
	/**
	 * This method executes given query against database. It gets part of table metadata. More precisely
	 * table name, column data types and column names.
	 * @param statement - statement from current database connection
	 * @param query - query to be executed
	 * @throws SQLException
	 */
	private void getTableMetaDataInfo(Statement statement, String query) throws SQLException {
		resultSet = statement.executeQuery(query);
		tableName = resultSet.getMetaData().getTableName(1); // pobierz nazwê tabeli
		for(int i = 1;i<= resultSet.getMetaData().getColumnCount();i++) {
			columnTypes.add(resultSet.getMetaData().getColumnTypeName(i)); // pobierz typy kolumn tabeli
			columnNames.add(resultSet.getMetaData().getColumnName(i)); // pobierz nazwy kolumn
		}
	}
	
	/**
	 * This method recieves content of table with ResultSet. Using <b>Record.class</b> each row
	 * is inserted to virtual record and added to record list -> virtual table.
	 * @param resultSet - set with table content
	 * @throws SQLException
	 */
	private void getTableContent(ResultSet resultSet) throws SQLException {
		while (resultSet.next()) {
			record = Record.create();
			for(int i = 1;i<= columnNames.size();i++) {
				record.addToRecord(resultSet.getString(i)); // pobierz zawartoœæ wynikaj¹ca z zapytania query
			}
			recordList.add(record);
		}
	}
	
	/**
	 * This method chcecks wether table contains any foreign keys. If so name and primary key name of referenced table
	 * is recieved. Also foreign key column name is recievied and written to coresponding lists.
	 * @throws SQLException
	 */
	private void getForeignKeysInfo() throws SQLException {
		resultSet = connection.getMetaData().getImportedKeys(connection.getCatalog(), null, tableName);
		while (resultSet.next()) {
			pkInfo.add(resultSet.getString(3)+ "???"+ resultSet.getString(4)); // pobierz nazwê tabeli i kolumnê odnosz¹c¹ siê do klucza obcego
			fkColumns.add(resultSet.getString(8)); // pobierz nazwy kolumn, bêd¹cych kluczem obcym
		}
	}
	
	/**
	 * This method for every foreign key gets first column name in parent table. It is needed, so that when join select is 
	 * executed, only one column is displayed instead of whole parent table.
	 * @param statement - statement from current database connection.
	 * @throws SQLException
	 */
	private void getParentTableColumnName(Statement statement) throws SQLException {
		for(int i = 0;i< fkColumns.size();i++) {
			String subTableName = pkInfo.get(i);
			resultSet = statement.executeQuery("SELECT * FROM "+ subTableName.substring(0, subTableName.indexOf("???")));
			firstColumnNameInPkTable.add(resultSet.getMetaData().getColumnName(2));
		}
	}

	@Override
	public Table singleTableReturnQuery(String query) throws SQLException {
		initVariables();
		try (Statement statement = connection.createStatement()) {
			getTableMetaDataInfo(statement, query);
			getTableContent(resultSet);
			getForeignKeysInfo();
			getParentTableColumnName(statement);
		}
		return Table.create().withName(tableName).withColumns(columnNames).withColumnsTypes(columnTypes).withFkColumns(fkColumns)
				.withPkInfo(pkInfo).withPkFirstColumnNames(firstColumnNameInPkTable).withRecords(recordList);
	}

	@Override
	public void disconnect() throws SQLException {
		connection.close();
	}
	
	/**
	 * This method gets all table names in database, and adds them to coresponding list.
	 * @throws SQLException
	 */
	private void getDatabaseTables() throws SQLException {
		resultSet = connection.getMetaData().getTables(null, null, "%", new String[]{"TABLE"});
		while (resultSet.next()) {
			listOfTablesNamesInDatabase.add(resultSet.getString(3));
		}
	}
}
