package hierarchy;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import application.hierarchy.DatabaseImpl;
import application.hierarchy.Record;
import application.hierarchy.Table;
import application.query.Query;
import application.query.QueryImpl;

class DatabaseImplTest {
	private static DatabaseImpl database;
	private static Query query;
	private static Table table;
	private static List<String> columns;
	private static List<Record> records;
	//zawartoœæ tabeli
	private static String[][] temp= {{"iam","here","www.pl","sd"},
							  {"isd","hesdre","aaawww.pl","s23d"},
							  {"igfg","herdsde","wdddaww.pl","s44ssd"},
							  {"isdfg","de","wd765ww.pl","355d"},
							  {"i44g","h55rdsde","wd434aww.pl","s23d"}};
	private static String[][] temp2= {{"1","iam","here","www.pl","sd"},
	   							  {"2","isd","hesdre","aaawww.pl","s23d"},
	   							  {"3","igfg","herdsde","wdddaww.pl","s44ssd"},
	   							  {"4","isdfg","de","wd765ww.pl","355d"},
	   							  {"5","i44g","h55rdsde","wd434aww.pl","s23d"}};
	
	@BeforeAll
	static void setup() throws Exception{
		database=new DatabaseImpl();
		query=QueryImpl.create();
		
		//utwórz lokalnie tabele
		columns=new ArrayList<>();
		records=new ArrayList<>();
		
		columns.addAll(Arrays.asList("id","names","surname","e_mail","address"));	//lista kolumn tabeli
		Record record=null;
		for(int i=0;i<5;i++) {
			record=Record.create();
			record.addToRecord(temp2[i]);	//dodaj tablicê przedmiotów do rekordu
			records.add(record);
		}
		table=Table.create().withName("fake")	//lokalna tablica
				.withColumns(columns)
				.withRecords(records);
		
		database.singleReturnQuery(query.create(table.getTableName(),table.getColumnNames()).build());	//utwórz tabele
	}
	
	@BeforeEach
	void init() throws Exception {
//		System.out.println("Initializing...:");
		for(int i=0;i<temp.length;i++)
			database.singleReturnQuery(query.insertInto(table, Arrays.asList(temp[i])).build());
//		System.out.println("records where added...");
	}
	
//	@Test
//	@DisplayName("dummy")
//	void dummy() {
//		
//	}
	
	//ta funkcja jest potrzebna, bowiem pomiêdzy ka¿dym testem licznik id wzrasta niezale¿nie
	//rozwi¹zaniem by³oby ka¿dowrazowo dropowanie tabeli i tworzenie nowej, a tutaj raz jest
	//tabela tworzona a pod sam koniec wszystkich testów dopiero tabela jest dropowana
	public void increseIdBy(int value) {
		int start=value;
		for(int i=0;i<table.getNumberOfRecords();i++) {
			table.getRecord(i).setCell(0, String.valueOf(start++));
		}
	}
	
	//Klucz id[1-5]
	@Test
	@DisplayName("select all records works correctly")
	void test1() throws Exception{
		String tmp=query.selectAll(table.getTableName()).build();
		System.out.println(tmp);
		Table result=database.singleTableReturnQuery(tmp);
		System.out.println("Result: "+result.toString());
		assertThat(result.equals(table)).isEqualTo(true);
	}
	
	//Klucz id[6-10]
	@Test
	@DisplayName("select columns names and e_mail")
	void test4() throws Exception{
		String tmp=query.select(table.getTableName(), Arrays.asList("names","e_mail")).build();
		System.out.println(tmp);
		Table result=database.singleTableReturnQuery(tmp);
		System.out.println("Result: "+result.toString());
		table.removeColumn("id");
		table.removeColumn("surname");
		table.removeColumn("address");
//		System.out.println("Table: "+table.toString());
		assertThat(result.equals(table)).isEqualTo(true);
	}
	
	//Klucz id[11-15]
	@Test
	@DisplayName("delete all records")
	void test5() throws Exception{
		String tmp=query.delete(table, -1).build();
		System.out.println(tmp);
		int status=database.singleReturnQuery(tmp);
		System.out.println("Result status: "+status);	//iloœæ zmienionych rekordów
		Table result=database.singleTableReturnQuery("SELECT * FROM fake;");
		table=Table.create().withName("fake").withColumns(columns);
		assertThat(result.equals(table)).isEqualTo(true);
	}
	
	//Klucz id[16-20] =>table[0-4]
	@Test
	@DisplayName("delete selected records")
	void test6() throws Exception{
		String tmp=query.delete(table, 17).build();
		System.out.println(tmp);
		int status=database.singleReturnQuery(tmp);
		System.out.println("Result status: "+status);	//iloœæ zmienionych rekordów
		Table result=database.singleTableReturnQuery("SELECT * FROM fake;");
		System.out.println("Result: "+result.toString());
		increseIdBy(16);
		table.removeRecord(1);
//		System.out.println("TAble: "+table.toString());
		assertThat(result.equals(table)).isEqualTo(true);
	}
	
//	@Test
//	@Disabled
//	@DisplayName("table is created correctly")
//	void test2() throws Exception{
//		assertThat(database.singleReturnQuery(query.prepareCreateQuery(table.getTableName(),table.getColumnNames()))).isEqualTo(0);
//	}
//	
////	@Test
//	@Disabled
//	@DisplayName("table is dropped correctly")
//	void test3() throws Exception{
//		assertThat(database.singleReturnQuery(query.prepareDropQuery(table.getTableName()))).isEqualTo(0);
//	}
	
	@AfterEach
	void prepare() throws Exception {
		String tmp=query.delete(table, -1).build();
//		System.out.println("preparing...: "+tmp);
		database.singleReturnQuery(tmp);
		
		columns.clear();
		records.clear();
		
		columns.addAll(Arrays.asList("id","names","surname","e_mail","address"));	//lista kolumn tabeli
		Record record=null;
		for(int i=0;i<5;i++) {
			record=Record.create();
			record.addToRecord(temp2[i]);	//dodaj tablicê przedmiotów do rekordu
			records.add(record);
		}
		table=Table.create().withName("fake")	//lokalna tablica
				.withColumns(columns)
				.withRecords(records);
	}
	
	@AfterAll
	static void finish() throws Exception {
		String tmp=query.drop(table.getTableName()).build();
//		System.out.println("finishing...: "+tmp);
		database.singleReturnQuery(tmp);
	}
}
