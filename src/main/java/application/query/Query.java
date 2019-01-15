package application.query;


import java.util.List;

import application.hierarchy.Table;

public interface Query {
	/**
	 * This method returns final query String. Class implementing should
	 * use Builder pattern.
	 * @return string with query.
	 */
	public String build();
	
	/**
	 * This method generates SQL INSERT query. Table is passed to get table name and column names,
	 * inserting assumes that first column is <b>id</b> with auto-generating value assignment, so that
	 * database is responsible for writing value into that column. Next, list of values to be inserted
	 * is passed, every value is surrounded with ' sign.
	 * @param table - table to which record must be inserted.
	 * @param values - values for new record.
	 * @return class implementing this interface (Builder Pattern).
	 */
	public QueryImpl insertInto(Table table, List<String> values);
	
	/**
	 * This method generates SQL DELETE query. It needs table name and id of record
	 * to be deleted.
	 * @param table - table in which records are to be deleted.
	 * @param indexOfRecord - id value of records to be deleted.
	 * @return class implementing this interface (Builder Pattern).
	 */
	public QueryImpl delete(Table table, int indexOfRecord);
	
	/**
	 * This method creates fixed structure table needed for test purposes.
	 * @param tableName
	 * @param columns
	 * @return
	 */
	public QueryImpl create(String tableName,List<String> columns);
	
	/**
	 * This method drops (deletes form database) table, method used only for
	 * testing.
	 * @param tableName
	 * @return
	 */
	public QueryImpl drop(String tableName);
	
	/**
	 * This method generates SQL SELECT query with given columns.
	 * @param tableName - to point from which table.
	 * @param columns - list of columns to display from table.
	 * @return class implementing this interface (Builder Pattern).
	 */
	public QueryImpl select(String tableName, List<String> columns);
	
	/**
	 * This method generates SQL SELECT query which shows all columns. Equivalent
	 * to SELECT * FROM table;.
	 * @param tableName - to point from which table.
	 * @return class implementing this interface (Builder Pattern).
	 */
	public QueryImpl selectAll(String tableName);	
	
	/**
	 * This method generates SQL INNER JOIN query. First all columns from actual
	 * table and those from parent tables are selected. To fulfill relation, then
	 * blocks of INNER JOIN ON are inserted. All information needed for this query
	 * is from passed table instance.
	 * @param table - table on which query is to be performed.
	 * @return class implementing this interface (Builder Pattern).
	 */
	public QueryImpl innerJoin(Table table);
	
	/**
	 * This method generates SQL UPDATE query. 
	 * @param tableName - on which table query is to bep performed.
	 * @param column - column name to point cell horizontally.
	 * @param row - row id value to point cell vertically.
	 * @param newValue - new value to be written.
	 * @return class implementing this interface (Builder Pattern).
	 */
	public QueryImpl update(String tableName, String column, String row, String newValue);
	
	/**
	 * This method appends to query ORDER BY clause. List of columns is taken
	 * to point by which columns ordering is to be made.
	 * @param columns - list of columns to order by.
	 * @return class implementing this interface (Builder Pattern).
	 */
	public QueryImpl orderBy(List<String> columns);
	
	/**
	 * This method appends to query DESC clause. <b>Use only with method orderBy()</b> otherwise
	 * wrong SQL query exception is to be thrown.
	 * @return
	 */
	public QueryImpl descOrder();
}
