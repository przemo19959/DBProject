package hierarchy;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import application.hierarchy.Record;
import javafx.beans.property.SimpleStringProperty;

class RecordSpec {
	private Record record;
	
	@BeforeEach
	void init() {
		record=Record.create();
	}
	
	@Test
	@DisplayName("record size is equal to 0, when no items where added")
	void test1() {
		assertThat(record.getRecordLength()).isEqualTo(0);
	}
	
	@Test
	@DisplayName("record size is equal to 3, when 3 items where added")
	void test2() {
		record.addToRecord("cos","tam","takiego");
		assertThat(record.getRecordLength()).isEqualTo(3);
	}
	
	@Test
	@DisplayName("items names added to record are correct")
	void test3() {
		record.addToRecord("cos","takiego");
		assertThat(new SimpleStringProperty("cos").get()).isEqualTo(record.getCell(0).get());
		assertThat(new SimpleStringProperty("takiego").get()).isEqualTo(record.getCell(1).get());
	}
	
	@Test
	@DisplayName("printing record info")
	void test4() {
		record.addToRecord("cos","cos2","23+sdasd");
		assertThat(record.toString()).isEqualTo("cos:cos2:23+sdasd");
	}
	
	@Test
	@DisplayName("method equals returns true when records contains the same items")
	void test5() {
		record.addToRecord("cos");
		Record tmp=Record.create();
		tmp.addToRecord("cos");
		assertThat(record.equals(tmp)).isEqualTo(true);
	}
	
	@Test
	@DisplayName("method equals returns false when records contains the different items")
	void test6() {
		record.addToRecord("cos");
		Record tmp=Record.create();
		tmp.addToRecord("costam");
		assertThat(record.equals(tmp)).isEqualTo(false);
	}
	
	@Test
	@DisplayName("method equals returns false when records are of different lengths")
	void test7() {
		record.addToRecord("cos","tam","takiego");
		Record tmp=Record.create();
		tmp.addToRecord("costam");
		assertThat(record.equals(tmp)).isEqualTo(false);
	}
	
	@Test
	@DisplayName("method equals returns true when no items where added")
	void test8() {
		Record tmp=Record.create();
		assertThat(record.equals(tmp)).isEqualTo(true);
	}
	
	@Test
	@DisplayName("when no arguments where passed to builder, then empty/empty collection is created")
	void test9() {
		Record tmp=Record.create();
		assertThat(tmp.isRecordEmpty()).isEqualTo(true);
	}
}
