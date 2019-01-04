package application.profilesWindow;

/**
 * This class represents actual profile. When user chooses one profile, then in this object that data is.
 * This class uses Singleton pattern, so that only one instance exits in program.
 * @author hex
 *
 */
public class ActualProfile {
	//Ta klasa korzysta ze wzorca Singleton
	private static ActualProfile single_instance = null; 
    private ActualProfile() {  
    } 
    
    /**
     * This method returns singleton instance of this class. It encapsulates chosen profile info.
     * @return - singleton instance of this class.
     */
    public static ActualProfile getInstance() { 
        if (single_instance == null) 
            single_instance = new ActualProfile(); 
        return single_instance; 
    }
    
    private String profileName;
	private String databaseURL;
	private String databaseUsername;
	private String databasePassword;
	
	/**
	 * This method sets new values of profile info. It contains profile name, database url, database username and 
	 * username password. This info is needed in order to get connection with database.
	 * @param profileInfo - profile info which is to be written to this class.
	 */
	public void setProfile(String[] profileInfo) {
		this.profileName = profileInfo[0];
		this.databaseURL = profileInfo[1];
		this.databaseUsername = profileInfo[2];
		this.databasePassword = profileInfo[3];
	}
	
	/**
	 * This method returns profile info in String array.
	 * @return - profile info.
	 */
	public String[] getProfile() {
		return new String[] {profileName,databaseURL,databaseUsername,databasePassword};
	}
}
