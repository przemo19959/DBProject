package hierarchy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import application.hierarchy.Record;
import application.hierarchy.Table;

class TableSpec {
	private Table table;
	
	@BeforeEach
	void init() {
		table=Table.create().withName("tabela 1");
	}
	
	@Test
	@DisplayName("table name is correct")
	void test() {
		assertEquals("tabela 1", table.getTableName());
	}
	
	@Test
	@DisplayName("size of table is 0, when no record where added")
	void test1() {
		assertEquals(0, table.getNumberOfRecords());
	}
	
	@Test
	@DisplayName("size of table is equal to 3, after 3 records where added")
	void test2() {
		table.addRecord(Record.create(),Record.create(),Record.create());
		assertEquals(3, table.getNumberOfRecords());
	}
	
	@Test
	@DisplayName("size is equal to 0, after adding 2 and removing two records")
	void test3() {
		table.addRecord(Record.create(),Record.create());
		table.removeRecord(0);
		table.removeRecord(0);
		assertEquals(0, table.getNumberOfRecords());
	}
	
	@Test
	@DisplayName("test of toString method")
	void test4() {
		table.addColumn("kol1","kol2","kol3");
		table.addRecord(Record.create().withItems(Arrays.asList("cos","tam","jest")),
						Record.create().withItems(Arrays.asList("one","two","three")),
						Record.create().withItems(Arrays.asList("four","five","six")));
//		System.out.println(table.toString());
		assertThat(table.toString()).isEqualTo("Table Name :tabela 1\n" + 
				"  Kolumny  :[kol1, kol2, kol3]\n" + 
				"  Rekordy  :cos:tam:jest\n" + 
				"            one:two:three\n" + 
				"            four:five:six\n            ");
	}
	
	@Test
	@DisplayName("one column where removed")
	void test5() {
		table.addColumn("kol1","kol2","kol3");
		table.addRecord(Record.create().withItems(new ArrayList<>(Arrays.asList("cos","tam","jest"))),
						Record.create().withItems(new ArrayList<>(Arrays.asList("one","two","three"))),
						Record.create().withItems(new ArrayList<>(Arrays.asList("four","five","six"))));
		table.removeColumn("kol2");
		assertThat(table.toString()).isEqualTo("Table Name :tabela 1\n" + 
				"  Kolumny  :[kol1, kol3]\n" + 
				"  Rekordy  :cos:jest\n" + 
				"            one:three\n" + 
				"            four:six\n            ");
	}
	
	@Test
	@DisplayName("two column where removed")
	void test6() {
		table.addColumn("kol1","kol2","kol3");
		table.addRecord(Record.create().withItems(new ArrayList<>(Arrays.asList("cos","tam","jest"))),
						Record.create().withItems(new ArrayList<>(Arrays.asList("one","two","three"))),
						Record.create().withItems(new ArrayList<>(Arrays.asList("four","five","six"))));
		table.removeColumn("kol2");
		table.removeColumn("kol1");
		assertThat(table.toString()).isEqualTo("Table Name :tabela 1\n" + 
				"  Kolumny  :[kol3]\n" + 
				"  Rekordy  :jest\n" + 
				"            three\n" + 
				"            six\n            ");
	}
	
	@Test
	@DisplayName("fk list is not null after calling static create method")
	void test7() {
		assertThat(table.getFkColumns()).isNotNull();
	}
	
	@Test
	@DisplayName("exception is thrown, when null column name is passed "
			+ "for fk check")
	void test8() {
		assertThrows(NullPointerException.class, ()->table.isColumnFkKey(null));
	}
	
	@Test
	@DisplayName("method returns false, if given column is not fk column")
	void test9() {
		assertThat(table.isColumnFkKey("cos")).isEqualTo(false);
	}
	
	@Test
	@DisplayName("method returns true, if given column is fk column")
	void test10() {
		table=table.withFkColumns(Arrays.asList("cos"));
		assertThat(table.isColumnFkKey("cos")).isEqualTo(true);
	}
	
	@Test
	@DisplayName("method returns true, when fk column where added")
	void test11() {
		table=table.withFkColumns(Arrays.asList("cos","tam"));
		assertThat(table.containsFkeys()).isEqualTo(true);
	}
	
	@Test
	@DisplayName("method returns false, when no fk column where added")
	void test12() {
		assertThat(table.containsFkeys()).isEqualTo(false);
	}
	
	@Test
	@DisplayName("method returns 0, when no fk column where added")
	void test13() {
		assertThat(table.getNumberOfFkeys()).isEqualTo(0);
	}
	
	@Test
	@DisplayName("method returns 4, when 4 fk column where added")
	void test14() {
		table=table.withFkColumns(Arrays.asList("a","bc","cos","tam"));
		assertThat(table.getNumberOfFkeys()).isEqualTo(4);
	}
	
	@Test
	@DisplayName("method returns 0, when no column where added")
	void test15() {
		assertThat(table.getNumberOfColumns()).isEqualTo(0);
	}
	
	@Test
	@DisplayName("method returns 2, when 2 columns where added")
	void test16() {
		table=table.withColumns(Arrays.asList("cos","nic"));
		assertThat(table.getNumberOfColumns()).isEqualTo(2);
	}
	
	@Test
	@DisplayName("method throws exception, when index out of bounds is passed")
	void test17() {
		assertThrows(IndexOutOfBoundsException.class, ()->table.getFkeyColumn(234));
		assertThrows(IndexOutOfBoundsException.class, ()->table.getFkeyColumn(-234));
	}
	
	@Test
	@DisplayName("method gets proper fk column form list")
	void test18() {
		table=table.withFkColumns(Arrays.asList("cos","tam","mamy"));
		assertThat(table.getFkeyColumn(2)).isEqualTo("mamy");
	}
	
	@Test
	@DisplayName("method throws exception, when passed fkColumn is null")
	void test19() {
		assertThrows(NullPointerException.class, ()->table.getPkTableFirstColumnName(null));
	}
	
	@Test
	@DisplayName("method throws exception, when given fkcolumn is not in list")
	void test20() {
		table=table.withFkColumns(Arrays.asList("cos","tam")); //takie klucze obce posiada tabela
		assertThrows(IllegalArgumentException.class, ()->table.getPkTableFirstColumnName("mam"));
	}
	
	@Test
	@DisplayName("method gets proper pk table first column, when given fkcolumn is in list")
	void test21() {
		table=table.withFkColumns(Arrays.asList("cos","tam","mam"))
				.withPkFirstColumnNames(Arrays.asList("jest","cos","tutaj"));
		assertThat(table.getPkTableFirstColumnName("cos")).isEqualTo("jest");
		assertThat(table.getPkTableFirstColumnName("mam")).isEqualTo("tutaj");
	}
	
	@Test
	@DisplayName("method throws exception, when passed fkColumn is null")
	void test22() {
		assertThrows(NullPointerException.class, ()->table.getPkTableName(null));
	}
	
	@Test
	@DisplayName("method throws exception, when given fkcolumn is not in list")
	void test23() {
		table=table.withFkColumns(Arrays.asList("cos","tam")); //takie klucze obce posiada tabela
		assertThrows(IllegalArgumentException.class, ()->table.getPkTableName("mam"));
	}
	
	@Test
	@DisplayName("method gets proper pk table name, when given fkcolumn is in list")
	void test24() {
		table=table.withFkColumns(Arrays.asList("cos","tam","mam"))
				.withPkInfo(Arrays.asList("jest???cos","cos???cos","tutaj???here"));
		assertThat(table.getPkTableName("cos")).isEqualTo("jest");
		assertThat(table.getPkTableName("mam")).isEqualTo("tutaj");
		assertThat(table.getPkTableName("tam")).isEqualTo("cos");
	}
	
	@Test
	@DisplayName("method throws exception, when passed fkColumn is null")
	void test25() {
		assertThrows(NullPointerException.class, ()->table.getPkColumnName(null));
	}
	
	@Test
	@DisplayName("method throws exception, when given fkcolumn is not in list")
	void test26() {
		table=table.withFkColumns(Arrays.asList("cos","tam")); //takie klucze obce posiada tabela
		assertThrows(IllegalArgumentException.class, ()->table.getPkColumnName("mam"));
	}
	
	@Test
	@DisplayName("method gets proper pk table name, when given fkcolumn is in list")
	void test27() {
		table=table.withFkColumns(Arrays.asList("cos","tam","mam"))
				.withPkInfo(Arrays.asList("jest???cos","cos???cos","tutaj???here"));
		assertThat(table.getPkColumnName("cos")).isEqualTo("cos");
		assertThat(table.getPkColumnName("tam")).isEqualTo("cos");
		assertThat(table.getPkColumnName("mam")).isEqualTo("here");
	}
	
	@Test
	@DisplayName("method throws exception, when null list is passed")
	void test28() {
		assertThrows(NullPointerException.class, ()->table.withRecords(null));
		assertThrows(NullPointerException.class, ()->table.withName(null));
		assertThrows(NullPointerException.class, ()->table.withColumns(null));
		assertThrows(NullPointerException.class, ()->table.withColumnsTypes(null));
		assertThrows(NullPointerException.class, ()->table.withFkColumns(null));
		assertThrows(NullPointerException.class, ()->table.withPkFirstColumnNames(null));
		assertThrows(NullPointerException.class, ()->table.withPkInfo(null));
	}
	
	
	
	@Nested
	@DisplayName("equals test group")
	class equalsClass{
	
		@Test
		@DisplayName("equals method return true for two empty tables")
		void test1() {
			Table tmp=Table.create().withName("tabela 1");
			assertThat(table.equals(tmp)).isEqualTo(true);
		}
		
		@Test
		@DisplayName("equals method return false for two empty tables with different names")
		void test2() {
			Table tmp=Table.create().withName("fase");
			assertThat(table.equals(tmp)).isEqualTo(false);
		}
		
		@Test
		@DisplayName("equals method return false for two tables with different column names")
		void test3() {
			Table tmp=Table.create().withName("tabela 1").withColumns(Arrays.asList("cos"));
			assertThat(table.equals(tmp)).isEqualTo(false);
		}
		
		@Test
		@DisplayName("equals method return true for two tables with same column names")
		void test4() {
			table.withColumns(Arrays.asList("cos","tam"));
			Table tmp=Table.create().withName("tabela 1").withColumns(Arrays.asList("cos","tam"));
			assertThat(table.equals(tmp)).isEqualTo(true);
		}
		
		@Test
		@DisplayName("equals method return false for two tables with different records")
		void test5() {
			table.withColumns(Arrays.asList("cos","tam"));
			Table tmp=Table.create().withName("tabela 1").withColumns(Arrays.asList("cos","tam")).withRecords(Arrays.asList(Record.create().withItems(Arrays.asList("tak","nie"))));
			assertThat(table.equals(tmp)).isEqualTo(false);
		}
		
		@Test
		@DisplayName("equals method return false for two tables with different number of records")
		void test6() {
			table.withColumns(Arrays.asList("cos","tam")).withRecords(Arrays.asList(Record.create().withItems(Arrays.asList("tak","nie"))));
			Table tmp=Table.create().withName("tabela 1").withColumns(Arrays.asList("cos","tam"))
					.withRecords(Arrays.asList(Record.create().withItems(Arrays.asList("tak","nie")),Record.create()));
			assertThat(table.equals(tmp)).isEqualTo(false);
		}
		
		@Test
		@DisplayName("equals method return false for two tables with different count of item in records")
		void test7() {
			table.withColumns(Arrays.asList("cos","tam")).withRecords(Arrays.asList(Record.create().withItems(Arrays.asList("tak","nie","lub"))));
			Table tmp=Table.create().withName("tabela 1").withColumns(Arrays.asList("cos","tam")).withRecords(Arrays.asList(Record.create().withItems(Arrays.asList("tak","nie"))));
			assertThat(table.equals(tmp)).isEqualTo(false);
		}
	}

}
