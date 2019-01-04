package application.profilesWindow;

public class ActualProfile {
	//Ta klasa korzysta ze wzorca Singleton
	private static ActualProfile single_instance = null; 
    private ActualProfile() {  
    } 
  
    public static ActualProfile getInstance() { 
        if (single_instance == null) 
            single_instance = new ActualProfile(); 
        return single_instance; 
    }
    
    private String profileName;
	private String databaseURL;
	private String databaseUsername;
	private String databasePassword;
	
	public void setProfile(String[] profileInfo) {
		this.profileName = profileInfo[0];
		this.databaseURL = profileInfo[1];
		this.databaseUsername = profileInfo[2];
		this.databasePassword = profileInfo[3];
	}
	
	public String[] getProfile() {
		return new String[] {profileName,databaseURL,databaseUsername,databasePassword};
	}
}
