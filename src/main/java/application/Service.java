package application;

import application.hierarchy.Table;

public interface Service {
	public void showTable(Table table);
	public void connect();
	public void disconnect();
}
