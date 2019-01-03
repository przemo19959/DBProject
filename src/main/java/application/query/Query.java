package application.query;


import java.util.List;

import application.hierarchy.Table;

public interface Query {
	public String build();
	
	public QueryImpl insertInto(Table table, List<String> values);
	public QueryImpl delete(Table table, int indexOfRecord);
	public QueryImpl create(String tableName,List<String> columns);
	public QueryImpl drop(String tableName);
	public QueryImpl select(String tableName, List<String> columns);
	public QueryImpl selectAll(String tableName);	
	public QueryImpl innerJoin(Table table);
	public QueryImpl update(String tableName, String column, String row, String newValue);
	public QueryImpl orderBy(List<String> columns);
	public QueryImpl descOrder();
}
