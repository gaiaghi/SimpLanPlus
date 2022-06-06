package util;

public class LabelManager {
	  
	private static LabelManager manager = null;
	  private static int labCount=0; 
	  private static int funLabCount=0; 
	  
	  public static LabelManager getManager() {
		  if (manager ==null)
			  manager = new LabelManager();
		  return manager;
	  } 

	  public static String newLabel() { 
			return "LABEL"+(labCount++);
	  } 

	  public static String newFunLabel() { 
	    	return "FUN"+(funLabCount++);
	  } 
}
