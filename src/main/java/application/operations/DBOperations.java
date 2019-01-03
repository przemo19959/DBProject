package application.operations;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import application.hierarchy.Table;

public interface DBOperations {
	/**
	 * Function that selects all records form given table. If table contains any foreign keys,
	 * then ...TODO
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	public Table selectAllRecords(String tableName) throws SQLException;
	
	/**
	 * Function that closes JDBC connection.
	 * @throws SQLException
	 */
	public void disconnect() throws SQLException;
	public Map<String, List<String>> getMap();
	public int update(int columnIndex, String newValue, int row) throws SQLException;
	/**
	 * Function that process error codes, usually it returns comment for user, giving
	 * hint what was the cause of error.
	 * @param errorCode
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
	 * @param row - number of row, which is to be deleted
	 * @throws SQLException
	 */
	public void deleteRecord(int row) throws SQLException;
	public List<String> getTables();
}
