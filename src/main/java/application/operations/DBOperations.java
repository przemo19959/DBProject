package application.operations;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import application.hierarchy.Table;

public interface DBOperations {
	/**
	 * Function that selects all records form given table. If table contains any foreign keys,
	 * then INNER JOIN query is made and map is generated.
	 * @param tableName - table name to query.
	 * @return virtual Table.
	 * @throws SQLException
	 */
	public Table selectAllRecords(String tableName) throws SQLException;
	
	/**
	 * Function that closes JDBC connection.
	 * @throws SQLException
	 */
	public void disconnect() throws SQLException;
	
	/**
	 * This method returns map with fk columns and coressponding list of record for one selected column from
	 * parent table. This is needed for click-on combobox feature.
	 * @return - map of fkcolumns and coresponding list of columns.
	 */
	public Map<String, List<String>> getMap();
	
	/**
	 * This method performs UPDATE SQL query. If updated table doesn't contain
	 * foreign keys and new value is given, then pointed cell is updated. If
	 * column to be updated is foreign key, then first parent table for that
	 * foreign key is queried. Then this table cell is updated by choosing from
	 * click-on combobox.
	 * @param columnIndex - to point cell horizontally.
	 * @param newValue - newValue to be written
	 * @param row - id of row to be updated
	 * @return 1 if successfully updated, otherwise 0.
	 * @throws SQLException
	 */
	public int update(int columnIndex, String newValue, int row) throws SQLException;
	
	/**
	 * Function that process error codes, usually it returns comment for user, giving
	 * hint what was the cause of error.
	 * @param errorCode - errorCode to be processed.
	 * @return
	 */
	public String processSQLError(String errorCode);
	
	/**
	 * Function inserts empty new record, if given column is foreign key, then first value
	 * from subtable is written, if given column is of date type, then 2000-01-01 is inserted,
	 * otherwise string "empty" is inserted.
	 * @throws SQLException
	 */
	public void insertEmptyRecord() throws SQLException;
	
	/**
	 * Function deletes given record from table.
	 * @param row - number of row, which is to be deleted.
	 * @throws SQLException
	 */
	public void deleteRecord(int row) throws SQLException;
	
	/**
	 * This method returns all table in database names.
	 * @return list of tables names.
	 */
	public List<String> getTables();
}
