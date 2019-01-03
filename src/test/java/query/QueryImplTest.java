package query;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import application.hierarchy.Record;
import application.hierarchy.Table;
import application.query.Query;
import application.query.QueryImpl;

class QueryImplTest {
	private String[] values= {"cos","tam","takiego","i","inne","elementy","234"};
	private String[] values2= {"1","cos","tam","takiego","i","inne"};
	private String[] cleanValues= {};
	private List<String> columns= Arrays.asList("id","name","surname","length","23","xxxs","adress","zc");
	private List<String> columns2= Arrays.asList("id","name","xxxs","adress","zc");
	private List<String> columnsFkeys= Arrays.asList("id","name","fk_xxxs","fk_adress","zc");
	private Table table;
	private Query query;
	
	@BeforeEach
	void init() {
		table=Table.create().withName("tabela 1")
				.withColumns(columns);
		query=QueryImpl.create();
	}
	
	@Nested
	@DisplayName("INSERT group test")
	class insertClass{
		@Test
		@DisplayName("throws exception when table passed is null")
		void test1() {
			assertThrows(NullPointerException.class, ()->query.insertInto(null, Arrays.asList(values)).build());
		}
		
		@Test
		@DisplayName("throws exception when table of values is empty")
		void test2() {
			assertThrows(IllegalArgumentException.class, ()->query.insertInto(table, Arrays.asList(cleanValues)).build());
		}
		
		@Test
		@DisplayName("returns correct query")
		void test3() {
			assertThat(query.insertInto(table, Arrays.asList(values)).build()).isEqualTo("INSERT INTO tabela 1 (name,surname,length,23,xxxs,adress,zc)\n"
					+ " VALUES ('cos','tam','takiego','i','inne','elementy','234')");
		}
		
		@Test
		@DisplayName("throws exception when table of values is null")
		void test4() {
			assertThrows(NullPointerException.class, ()->query.insertInto(table, null).build());
		}
		
		@Test
		@DisplayName("throws exception when length of values and columnNames tables are inequal")
		void test5() {
			assertThrows(IllegalArgumentException.class, ()->query.insertInto(table, Arrays.asList(values2)).build());
		}
	}
	
	@Nested
	@DisplayName("DELETE group test")
	class deleteClass{
		
		@Test
		@DisplayName("throws exception when table passed is null")
		void test1() {
			assertThrows(NullPointerException.class,()->query.delete(null, 0).build());
		}
		
		@Disabled
		@DisplayName("throws exception when index of record to be deleted is out of bounds")
		void test2() {
			table.addRecord(Record.create());
			assertThrows(IllegalArgumentException.class,()->query.delete(table, 2130).build());
		}
		
		@Test
		@DisplayName("produces correct query")
		void test3() {
			table.addRecord(Record.create().withItems(Arrays.asList("1","tam")));
			assertThat(query.delete(table, 1).build()).isEqualTo("DELETE FROM tabela 1 WHERE tabela 1.id=1");
		}
		
	}
	
	@Nested
	@DisplayName("CREATE group test")
	class createClass{
		@Test
		@DisplayName("query is correct")
		void test1() {
			assertThat(query.create("fake", columns).build()).isEqualTo("CREATE TABLE fake (id serial not null,name varchar(40),surname varchar(40),length varchar(40)"
					+ ",23 varchar(40),xxxs varchar(40),adress varchar(40),zc varchar(40),primary key (id))");
		}
	}
	
	@Nested
	@DisplayName("SELECT group test")
	class selectClass{
		@Test
		@DisplayName("query is correct")
		void test1() {
			assertThat(query.select(table.getTableName(), columns2).build()).isEqualTo("SELECT id, name, xxxs, adress, zc\nFROM "+table.getTableName());
		}
	}
	
	@Nested
	@DisplayName("ORDER BY group test")
	class orderByClass{
		@Test
		@DisplayName("query is correct")
		void test1() {
			List<String> columns=Arrays.asList("cos","tam","here","where","iam");
			assertThat(query.select("fakeTab", columns).orderBy(Arrays.asList("tam","where")).descOrder().build()).isEqualTo("SELECT cos, tam, here, where, iam\n"
					+ "FROM fakeTab\n"
					+ "ORDER BY tam, where DESC");
		}
	}
	
	@Nested
	@DisplayName("JOIN group test")
	class joinClass{
		
		@Test
		@DisplayName("exception is thrown, when null table is passed")
		void test1() {
			assertThrows(NullPointerException.class, ()->query.innerJoin(null).build());
		}

		@Test
		@DisplayName("query is correct")
		void test3() {
			Table tmp=Table.create().withName("fakeTab")
									.withColumns(columnsFkeys)
									.withFkColumns(Arrays.asList("fk_xxxs","fk_adress"))
									.withPkInfo(Arrays.asList("xxxs???nazwa","adress???cos"))
									.withPkFirstColumnNames(Arrays.asList("nazwa1","adr"));
			assertThat(query.innerJoin(tmp).build()).isEqualTo("SELECT fakeTab.id,fakeTab.name,xxxs.nazwa1 AS fk_xxxs,adress.adr AS fk_adress,fakeTab.zc\n"
					+ "FROM fakeTab\n"
					+ "INNER JOIN xxxs ON fakeTab.fk_xxxs=xxxs.nazwa\n"
					+ "INNER JOIN adress ON fakeTab.fk_adress=adress.cos");
		}
	}
		
	@Nested
	@DisplayName("UPDATE group test")
	class updateClass{
		@Test
		@DisplayName("corret query")
		void test1() {
			assertThat(query.update("tab", "jakas", "234", "cos").build()).isEqualTo("UPDATE tab\n"
					+ "SET jakas='cos'\n"
					+ "WHERE id=234");
		}
	}
}
