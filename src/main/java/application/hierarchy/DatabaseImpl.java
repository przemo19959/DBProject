package application.hierarchy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseImpl implements Database{
	private Connection connection;
	private ResultSet resultSet;
	private List<String> listOfTablesNamesInDatabase;
	
	public DatabaseImpl() throws ClassNotFoundException, SQLException {
		connection = DriverManager.getConnection(
					"jdbc:postgresql://localhost/db1", "postgres",
					"postgres");
		listOfTablesNamesInDatabase=new ArrayList<>();
		getDatabaseTables(); //pobranie nazw tabel w bazie danych
	}
	
	@Override
	public List<String> getTablesInDB() {
		return listOfTablesNamesInDatabase;
	}
	
	@Override
	public int singleReturnQuery(String query) throws SQLException {
		int result=0;
		try(Statement statement=connection.createStatement()){
			result=statement.executeUpdate(query);
		}
		return result;
	}
	
	@Override
	public Table singleTableReturnQuery(String query) throws SQLException {
		//ca³e info jakie ma zawieraæ obiekt klasy Table
		String tableName="";	//nazwa tabeli
		List<String> columnNames=new ArrayList<>();	//nazwy wszystkich kolumn
		List<String> columnTypes=new ArrayList<>(); //typy danych wszystkich kolumn
		List<Record> recordList=new ArrayList<>();  //rekordy jakie posiada w wyniku podanego zapytania
		List<String> fkColumns=new ArrayList<>();  //lista kolumn bêd¹cych kluczem obcym
		List<String> pkInfo=new ArrayList<>();		//lista informacji o kluczu g³ównym powi¹zanym z kluczem obcym
		List<String> firstColumnNameInPkTable=new ArrayList<>();	//nazwa pierwszej kolumny, tabeli posiadaj¹cej klucz g³ówny do którego odnosi siê klucz obcy
		Record record=null;
		try(Statement statement=connection.createStatement()){			
			resultSet=statement.executeQuery(query);
			tableName=resultSet.getMetaData().getTableName(1);	//pobierz nazwê tabeli
			for(int i=1;i<=resultSet.getMetaData().getColumnCount();i++) {
				columnTypes.add(resultSet.getMetaData().getColumnTypeName(i));	//pobierz typy kolumn tabeli
				columnNames.add(resultSet.getMetaData().getColumnName(i));		//pobierz nazwy kolumn
			};
			while(resultSet.next()) {
				record=Record.create();
				for(int i=1;i<=columnNames.size();i++) {
					record.addToRecord(resultSet.getString(i));	//pobierz zawartoœæ wynikaj¹ca z zapytania query
				}
				recordList.add(record);
			}
			
			//sprawdz czy dana tabela posiada klucze obce
			resultSet=connection.getMetaData().getImportedKeys(connection.getCatalog(), null, tableName);
			while(resultSet.next()) {
					pkInfo.add(resultSet.getString(3)+"???"+resultSet.getString(4)); //pobierz nazwê tabeli i kolumnê odnosz¹c¹ siê do klucza obcego
					fkColumns.add(resultSet.getString(8)); //pobierz nazwy kolumn, bêd¹cych kluczem obcym
			}
			for(int i=0;i<fkColumns.size();i++) {
				String subTableName=pkInfo.get(i);
				resultSet=statement.executeQuery("SELECT * FROM "+subTableName.substring(0, subTableName.indexOf("???")));
				firstColumnNameInPkTable.add(resultSet.getMetaData().getColumnName(2));
			}
		}
		return Table.create().withName(tableName)
						  .withColumns(columnNames)
						  .withColumnsTypes(columnTypes)
						  .withFkColumns(fkColumns)
						  .withPkInfo(pkInfo)
						  .withPkFirstColumnNames(firstColumnNameInPkTable)
						  .withRecords(recordList);
	}
	
	@Override
	public void disconnect() throws SQLException {
		connection.close();
	}
	
	private void getDatabaseTables() throws SQLException {
		resultSet=connection.getMetaData().getTables(null, null, "%", new String[]{"TABLE"});
		while(resultSet.next()) {
			listOfTablesNamesInDatabase.add(resultSet.getString(3));
		}
	}
}
