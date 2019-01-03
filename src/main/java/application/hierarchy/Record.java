package application.hierarchy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.base.Preconditions;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

//klasa reprezentuj¹ca rekord w bazie danych
public class Record {
	/**
	 * Record in virtual table, it is list containing strings.
	 */
	private List<StringProperty> items;
	
	/**
	 * Function creates new record. By default created record is empty (empty list).
	 * @return new record
	 */
	public static Record create() {
		return new Record();
	}
	
	private Record() {
		this.items = new ArrayList<>();
	}
	
	/**
	 * This function adds elements to record form given list. After that operation record
	 * and given list will contain same elements.
	 * @param items - list with items to be inserted to record
	 * @return record with elements from given list
	 */
	public Record withItems(List<String> items) {
		Preconditions.checkArgument(items!=null,"passed collection must not be equal to null");
		this.items = items.stream()
				.map(item->new SimpleStringProperty(item))
				.collect(Collectors.toList());
		return this;
	}
	
	/**
	 * Function adds elements to record, the same effect as method <b>withItems(items)</b>, but this method
	 * has varargs argument rather than list.
	 * @param items - items to be added to record
	 */
	public void addToRecord(String... items) {
		Preconditions.checkArgument(this.items!=null,"record list must be created before adding items to it");
		this.items.addAll(Arrays.stream(items)
				.map(item->new SimpleStringProperty(item))
				.collect(Collectors.toList()));
	}
	
	/**
	 * function verifies whether record is empty.
	 * @return true, if record is empty
	 */
	public boolean isRecordEmpty() {
		return items.isEmpty();
	}
		
	/**
	 * This funtion returns value at given index of record.
	 * @param index - index or column-1 of record 
	 * @return item at specified column
	 */
	public StringProperty getCell(int index) {
		Preconditions.checkElementIndex(index, items.size(),"index must be in correct bounds");
		return items.get(index);
	}
	
	/**
	 * This function sets new value of specified cell in record.
	 * @param index - index or column-1 of record
	 * @param value - new value to be written into record
	 */
	public void setCell(int index, String value) {
		Preconditions.checkArgument(value!=null,"new value must not be null");
		Preconditions.checkElementIndex(index, items.size());
		items.set(index, new SimpleStringProperty(value));
	}
	
	/**
	 * This function returns length (number of items in list) of record.
	 * @return length of record
	 */
	public int getRecordLength() {
		return items.size();
	}
	
	/**
	 * This function removes item from record.
	 * @param index - index of item to be removed
	 */
	public void removeFromRecord(int index) {
		Preconditions.checkElementIndex(index, items.size(), "index out of bounds!!!");
		items.remove(index);
	}
	
	@Override
	public String toString() {
		return items.stream()
				.map(item->item.get())
				.collect(Collectors.joining(":"));
	}
	
	@Override
	public boolean equals(Object obj) {
		Preconditions.checkNotNull(obj);
		if(!(obj instanceof Record))
			return false;
		Record that=(Record)obj;
		if(this.getRecordLength()!=that.getRecordLength())
			return false;
		for(int i=0;i<this.getRecordLength();i++) {
			if(!this.getCell(i).get().equals(that.getCell(i).get()))
				return false;
		}
		return true;
	}
}
