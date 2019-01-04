package application.hierarchy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.base.Preconditions;

//tabela sk³ada siê z listy rekordów
public class Table {
	/**
	 * Field contains virtual table name.
	 */
	private String tableName;
	
	/**
	 * Table contains list of records (<b>Class.Record</b>).
	 */
	private List<Record> records;
	
	/**
	 * List of columns of virtual table.
	 */
	private List<String> columnNames;
	
	/**
	 * List of columns types.
	 */
	private List<String> columnTypes;
	
	/**
	 * List of columns names which are foreign keys.
	 */
	private List<String> fkColumns;  
	
	private List<String> pkInfo;
	private List<String> firstColumnNameInPkTable;
	
	/**
	 * This function creates new virtual table.
	 * @return newly created table with default values (empty lists and "" strings)
	 */
	public static Table create() {
		return new Table();
	}
	
	private Table() {
		tableName="";
		records=new ArrayList<>();
		columnNames=new ArrayList<>();
		columnTypes=new ArrayList<>();
		fkColumns=new ArrayList<>();
		pkInfo=new ArrayList<>();
		firstColumnNameInPkTable=new ArrayList<>();
	}
	
	public List<String> getFkColumns() {
		return fkColumns;
	}
	
	/**
	 * Checks if given column is foreign key.
	 * @param column - name of column to be checked
	 * @return true, if given column is foreign key
	 */
	public boolean isColumnFkKey(String column) {
		Preconditions.checkNotNull(column);
		return fkColumns.contains(column);
	}
	
	/**
	 * Checks if table contains foreign keys.
	 * @return true, if table contains foreign key.
	 */
	public boolean containsFkeys() {
		return (fkColumns.size()>0)?true:false;
	}
	
	/**
	 * Returns size of foreign keys list.
	 * @return number of columns which are foreign keys in table
	 */
	public int getNumberOfFkeys() {
		return fkColumns.size();
	}
	
	/**
	 * Returns size of column names list.
	 * @return number of columns in table
	 */
	public int getNumberOfColumns() {
		return columnNames.size();
	}
	
	/**
	 * Returns item from foreign key list.
	 * @param index - index or column-1 of item to be returned 
	 * @return foreign key column name
	 */
	public String getFkeyColumn(int index) {
		Preconditions.checkElementIndex(index, getNumberOfFkeys());
		return fkColumns.get(index);
	}
	
	/**
	 * For join selection needed is primary key of sub-table.
	 * @param fkColumn - name of foreign key column
	 * @return primary key from sub-table connected to given foreign key
	 */
	public String getPkTableFirstColumnName(String fkColumn) {
		int index=fkColumns.indexOf(Preconditions.checkNotNull(fkColumn));
		Preconditions.checkArgument(index!=-1,"fkcolumn list must contain given fkColumn");
		return firstColumnNameInPkTable.get(index);
	}
	
	/**
	 * 
	 * @param fkColumn 
	 * @return
	 */
	public String getPkTableName(String fkColumn) {
		int index=fkColumns.indexOf(Preconditions.checkNotNull(fkColumn));
		Preconditions.checkArgument(index!=-1,"fkcolumn list must contain given fkColumn");
		String tmp=pkInfo.get(index);
		return tmp.substring(0, tmp.indexOf("???"));
	}
	
	public String getPkColumnName(String fkColumn) {
		int index=fkColumns.indexOf(Preconditions.checkNotNull(fkColumn));
		Preconditions.checkArgument(index!=-1,"fkcolumn list must contain given fkColumn");
		String tmp=pkInfo.get(index);
		return tmp.substring(tmp.indexOf("???")+3);
	}
	
	public Table withRecords(List<Record> records) {
		this.records = Preconditions.checkNotNull(records);
		return this;
	}

	public Table withName(String tableName) {
		this.tableName = Preconditions.checkNotNull(tableName);
		return this;
	}

	public Table withColumns(List<String> columnNames) {
		this.columnNames =  Preconditions.checkNotNull(columnNames);
		return this;
	}
	
	public Table withColumnsTypes(List<String> columnTypes) {
		this.columnTypes =  Preconditions.checkNotNull(columnTypes);
		return this;
	}
	
	public Table withFkColumns(List<String> fkColumns) {
		this.fkColumns =  Preconditions.checkNotNull(fkColumns);
		return this;
	}
	
	public Table withPkInfo(List<String> pkInfo) {
		this.pkInfo =  Preconditions.checkNotNull(pkInfo);
		return this;
	}
	
	public Table withPkFirstColumnNames(List<String> firstColumnNameInPkTable) {
		this.firstColumnNameInPkTable =  Preconditions.checkNotNull(firstColumnNameInPkTable);
		return this;
	}
	
	public String getColumnType(int index) {
		Preconditions.checkElementIndex(index, columnTypes.size());
		return columnTypes.get(index);
	}
	
	public String getTableName() {
		return tableName;
	}
	
	public List<String> getColumnNames() {
		return columnNames;
	}

	public List<Record> getRecords() {
		return records;
	}

	public int getNumberOfRecords() {
		return records.size();
	}
	
	public List<String> getColumn(int index){
		Preconditions.checkElementIndex(index, getNumberOfColumns());
		List<String> result=new ArrayList<>();
		for(int i=0;i<getNumberOfRecords();i++) {
			result.add(records.get(i).getCell(index).get());
		}
		return result;
	}
	
	public List<String> getColumn(String column){
		int index=getColumnNames().indexOf(Preconditions.checkNotNull(column));
		Preconditions.checkArgument(index!=-1,"given column, must be in column list");
		List<String> result=new ArrayList<>();
		for(int i=0;i<getNumberOfRecords();i++) {
			result.add(records.get(i).getCell(index).get());
		}
		return result;
	}
	
	public Record getRecord(int index) {
		Preconditions.checkElementIndex(index, getNumberOfRecords(), "index must be within bounds");
		return records.get(index);
	}
	
	public String idOfGivenItemInGivenColumn(String item, String column) {
		int index=getColumn(column).indexOf(item);
		Preconditions.checkArgument(index!=-1,"given column, must be in column list");
		return  getColumn(0).get(index);
	}
	
	public void addRecord(Record... record) {
		records.addAll(Arrays.asList(Preconditions.checkNotNull(record)));
	}
	
	public void addColumn(String...strings) {
		columnNames.addAll(Arrays.asList(Preconditions.checkNotNull(strings)));
	}
	
	public void removeRecord(int index) {
		Preconditions.checkElementIndex(index, records.size(), "index must be within bounds");
		records.remove(index);
	}
	
	public String getColumnName(int index) {
		Preconditions.checkElementIndex(index, columnNames.size(), "index must be within bounds");
		return columnNames.get(index);
	}
	
	public void removeColumn(String columnName) {
		Preconditions.checkArgument(getColumnNames().contains(columnName));
		int index=getColumnNames().indexOf(columnName);
		getColumnNames().remove(index);
		for(int i=0;i<getNumberOfRecords();i++) {
			getRecord(i).removeFromRecord(index);
		}
	}
	
	@Override
	public String toString() {
		String result="Table Name :"+tableName+"\n"
					+ "  Kolumny  :";
		result+=columnNames+"\n"
					+ "  Rekordy  :";
		for(int i=0;i<getNumberOfRecords();i++) {
			result+=getRecord(i)+"\n"
					+ "            ";
		}
		return result;
	}
	
	//ta funkcja zwraca równoœæ pod wzglêdem zawartoœci - tj. takie same kolumny i rekordy, oraz nazwa tabeli
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Table))
			return false;
		Table that=(Table)obj;
		//sprawdzenie czy iloœæ rekordów, nazwy tabel oraz iloœæ kolumn jest równa
		if(this.getNumberOfRecords()!=that.getNumberOfRecords()
				|| !this.getTableName().equals(that.getTableName())
				|| this.getColumnNames().size()!=that.getColumnNames().size())
			return false;
		
		//sprawadzenie czy nazwy kolumn s¹ takie same
		for(int i=0;i<this.getColumnNames().size();i++) {
			if(!(getColumnName(i).equals(that.getColumnName(i))))
				return false;
		}
		
		//sprawdzenie czy rekordy s¹ takie same
		for(int i=0;i<this.getNumberOfRecords();i++) {
			if((!this.getRecord(i).equals(that.getRecord(i))))
				return false;
		}
		return true;
	}
}
