package application.hierarchy;

import java.sql.SQLException;
import java.util.List;

public interface Database {
	/**
	 * This method accept queries, which returns single value. Example INSERT, UPDATE, DELETE.
	 * @param query - query to be executed
	 * @return done query result code
	 * @throws SQLException
	 */
	public int singleReturnQuery(String query) throws SQLException;
	
	/**
	 * This method accept queries, which returns single table. Example SELECT. Several items are
	 * read form resultSet. First table name is read. Next for every column in table, type and name
	 * of every column is read and written to lists. Next content of table is read and written
	 * into records (<b>Class.Record</b>). Next function checks if table contains any foreign keys.
	 * If table contains foreign keys, then sub table name and binded primary key is read, also
	 * foreign keys column names are read. Next if foreign keys exists, primary key column name of
	 * sub table is read. With all those items vitual table is created.
	 * @param query - query to be executed
	 * @return new created table
	 * @throws SQLException
	 */
	public Table singleTableReturnQuery(String query) throws SQLException;
	
	/**
	 * Closes JDBC connection.
	 * @throws SQLException
	 */
	public void disconnect() throws SQLException;
	public List<String> getTablesInDB();
}
