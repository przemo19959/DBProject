package application.operations;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import application.hierarchy.DatabaseImpl;
import application.hierarchy.Table;
import application.profilesWindow.ActualProfile;
import application.query.Query;
import application.query.QueryImpl;

public class OperationImpl implements DBOperations {
	private Query query;
	private DatabaseImpl database;
	private Map<String, List<String>> map;
	private Table table;
	
	private ActualProfile profile = ActualProfile.getInstance();
	
	/**
	 * This method creates virtual database instance with actual profile info. Query
	 * instance is created to perform SQL queries. Map is created, its purpose is to get
	 * list of columns to be added to GUI click-on combobox feature. 
	 * @throws SQLException
	 */
	public OperationImpl() throws SQLException {
		database = new DatabaseImpl(profile.getProfile());
		query = QueryImpl.create();
		map = new HashMap<>();
	}
	
	@Override
	public List<String> getTables() {
		return database.getTablesInDB();
	}

	@Override
	public Map<String, List<String>> getMap() {
		return map;
	}

	@Override
	public Table selectAllRecords(String tableName) throws SQLException {
		table = database.singleTableReturnQuery(query.selectAll(tableName).build());	//select + orderby asc first column(usually id)
		if(table.containsFkeys()) { //jeœli tabela zawiera klucze obce
			for(String fkColumn:table.getFkColumns()) {
				map.put(fkColumn,database.singleTableReturnQuery(query.select(table.getPkTableName(fkColumn),Arrays.asList(table.getPkTableFirstColumnName(fkColumn))).build()).getColumn(0));
			}
			table = database.singleTableReturnQuery(query.innerJoin(table).orderBy(Arrays.asList(table.getTableName()+"."+table.getColumnName(0))).build()); // jeœli tabela z kluczami obcymi to select z joinem
		}
		return table;
	}

	@Override
	public void disconnect() throws SQLException {
		database.disconnect();
	}

	@Override
	public int update(int columnIndex, String newValue, int row) throws SQLException {				
		int result = 0;
		if(table.isColumnFkKey(table.getColumnName(columnIndex))) {	//jeœli updatowana kolumna jest kluczem obcym
			String fkColumn=table.getColumnName(columnIndex);
			Table tableTMp = database.singleTableReturnQuery(query.selectAll(table.getPkTableName(fkColumn)).build());
			database.singleReturnQuery(query.update(table.getTableName(), fkColumn, table.getRecord(row).getCell(0).get(),
															tableTMp.idOfGivenItemInGivenColumn(newValue, table.getPkTableFirstColumnName(fkColumn))).build());
			result = 1;
		} else {
			List<String> col = table.getColumn(columnIndex);
			if(!col.contains(newValue))
				database.singleReturnQuery(query.update(table.getTableName(), table.getColumnName(columnIndex), table.getRecord(row).getCell(0).get(), newValue).build());
			result = 1;
		}
		return result;
	}

	@Override
	public String processSQLError(String errorCode) {
		String result="";
		switch(errorCode) {
			case "23503": result="To pole stanowi odniesienie do obcego klucza";break;
		}
		return result;
	}
	
	@Override
	public void insertEmptyRecord() throws SQLException {
		List<String> values=new ArrayList<>();
		IntStream.range(1, table.getNumberOfColumns()).forEach(i->{
			if(table.isColumnFkKey(table.getColumnName(i)))	//jeœli dana kolumna stanowi klucz obcy wtedy ustaw id=1
				values.add("1");
			else if(table.getColumnType(i).equals("date")) //jeœli dane pole jest polem daty
				values.add("2000-01-01");
			else
				values.add("empty");
		});
		database.singleReturnQuery(query.insertInto(table, values).build());
	}
	
//	DELETE FROM table_name
//	WHERE condition;
	@Override
	public void deleteRecord(int row) throws SQLException {
		database.singleReturnQuery(query.delete(table, row).build());
	}
}
