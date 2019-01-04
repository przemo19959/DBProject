package sampleWindow;

import application.hierarchy.Table;

public interface Service {
	/**
	 * This method displays table in GUI window.Firstly tableview is cleared. Then for every table
	 * column new column in tableview is added. Then each column is populated with data from
	 * passed as argument table. If given column is foreign key, then combobox style modyfy is added
	 * to that column else textfield style modify. Then to each column is added edit commit
	 * listener, so that every modification is automatically visible in app and DB is updated.
	 * @param table
	 */
	public void showTable(Table table);
	
	/**
	 * This method opens connection to database and initialize all GUI nodes, so that user
	 * can interact with app. List of DB tables is updated and added to comobox, so that user can
	 * choose table to interact with.
	 */
	public void connect();
	
	/**
	 * This method closes JDBC connection. Next it cleans-up GUI application, so that
	 * table node is disabled, not active, insert and delete buttons become inactive etc.
	 */
	public void disconnect();
}
