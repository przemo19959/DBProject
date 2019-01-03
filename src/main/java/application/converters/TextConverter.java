package application.converters;

import javafx.util.StringConverter;

public class TextConverter extends StringConverter<String> {
	@Override
	public String fromString(String string) {
		return string;
	}
	
	@Override
	public String toString(String object) {
		return object;
	}
}
