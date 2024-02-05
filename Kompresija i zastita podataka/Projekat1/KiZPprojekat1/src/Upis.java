import java.nio.file.Paths;
public class Upis {
	
	public static String getFilePath(String fileName)
	{
		String currentDirectory = System.getProperty("user.dir");
        String path = Paths.get(currentDirectory, fileName).toString();
        
        //System.out.println("Putanja: \"" + path + "\".");
        return path;
	}
}
