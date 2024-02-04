package monami.lms.serverutils;

public class CurrentLoggedInUser {

	  private static CurrentLoggedInUser single_instance = null; 
	  
	    private int userId; 
	  
	    private CurrentLoggedInUser() 
	    { 
	    } 
	  
	    public static CurrentLoggedInUser getInstance() 
	    { 
	        if (single_instance == null) 
	            single_instance = new CurrentLoggedInUser(); 
	  
	        return single_instance; 
	    }

		public int getUserId() {
			return userId;
		}

		public void setUserId(int userId) {
			this.userId = userId;
		} 
	    
	    
}
