package application.query;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.base.Preconditions;

import application.hierarchy.Table;

public class QueryImpl implements Query {
	private String query;
	
	public static Query create() {
		return new QueryImpl();
	}
	
	private QueryImpl() {
		query="";
	}
	
	@Override
	public String build() {
		return query;
	}
	
	@Override
	public QueryImpl insertInto(Table table, List<String> values) {
		Preconditions.checkNotNull(table,"table can't be null");
		Preconditions.checkNotNull(values,"values table can't be null");
		Preconditions.checkArgument(values.size()>0,"values table must not be empty");
		Preconditions.checkArgument(values.size()==table.getColumnNames().size()-1,"values length must be equal to number of column minus 1(id column)");
		query="INSERT INTO "+table.getTableName()+" (";
		query+=table.getColumnNames().subList(1, table.getColumnNames().size()).stream().collect(Collectors.joining(","))+")\n VALUES (";
		query+=values.stream().map(value->"'"+value+"'").collect(Collectors.joining(","))+")";
		return this;
	}
	
	@Override
	public QueryImpl delete(Table table, int idOfRecord) {
		Preconditions.checkNotNull(table,"table can't be null");
		Preconditions.checkArgument(idOfRecord>=-1 && idOfRecord!=0,"index must be >=-1 and not equal to 0");
		if(idOfRecord==-1)	//delete all records 
			query="DELETE FROM "+table.getTableName();
		else
			query="DELETE FROM "+table.getTableName()+" WHERE "+table.getTableName()+".id="+idOfRecord; //delete selected records
		return this;
	}

	@Override
	public QueryImpl create(String tableName, List<String> columns) {
		Preconditions.checkArgument(tableName!=null && columns!=null);
		query="CREATE TABLE "+tableName+" (";
		query+="id serial not null,";	//dodanie klucza g³ównego + auto-increment
		query+=columns.subList(1, columns.size()).stream()	//pierwszy element stanowi¹cy id wycinane
				.map(column->column+" varchar(40)")	//tworzenie tabeli o sta³ym typie danych
				.collect(Collectors.joining(","))+",primary key (id))";
		return this;
	}
	
	@Override
	public QueryImpl drop(String tableName) {
		Preconditions.checkNotNull(tableName,"table can't be null");
		query="DROP TABLE "+tableName;
		return this;
	} 
	
	@Override
	public QueryImpl select(String tableName, List<String> columns) {
		query="SELECT "+columns.stream().collect(Collectors.joining(", "))+"\nFROM "+tableName;
		return this;
	}
	
	@Override
	public QueryImpl selectAll(String tableName) {
		query="SELECT * FROM "+tableName;
		return this;
	}
		
	@Override
	public QueryImpl innerJoin(Table table) {
		Preconditions.checkNotNull(table);
		query="SELECT ";
		query+=table.getColumnNames().stream().map(item->{
			if(table.isColumnFkKey(item))
				return table.getPkTableName(item)+"."+table.getPkTableFirstColumnName(item)+" AS "+item;
			return table.getTableName()+"."+item;
		}).collect(Collectors.joining(","));
		query+="\nFROM "+table.getTableName();
		query+="\n"+table.getFkColumns().stream()
				.map(item->"INNER JOIN "+table.getPkTableName(item)+" ON "+table.getTableName()+"."+item+"="+table.getPkTableName(item)+"."+table.getPkColumnName(item))
				.collect(Collectors.joining("\n"));
		return this;
	}
	
	@Override
	public QueryImpl update(String tableName, String column, String row, String newValue) {
		query="UPDATE "+tableName+"\n"
				+ "SET "+column+"='"+newValue+"'\n"
				+ "WHERE id="+row;
		return this;
	}
	
//	SELECT column1, column2, ...
//	FROM table_name
//	ORDER BY column1, column2, ... ASC|DESC;
	
	@Override
	public QueryImpl orderBy(List<String> columns) {
		query+="\nORDER BY "+columns.stream().collect(Collectors.joining(", "));
		return this;
	}
	
	@Override
	public QueryImpl descOrder() {
		query+=" DESC";
		return this;
	}
}
