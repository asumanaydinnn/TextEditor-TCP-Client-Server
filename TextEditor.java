import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;


public class TextEditor{
	
	public static void main(String args[]) throws IOException
	{
		String Addr = args[0];
		String ControlPort = args[1];
		
		
	}
	public static int sendUserNametoServer(String username)
	{
		
		return 0;
		
	}
	
	public static int sendPasswordtoServer(String password)
	{
		return 0;
	}
	
	public static void writeToFile(int version, int lineNo, String text)
	{
		FileWriter filewr = null;
		BufferedWriter bfwr = null;
		
	}
	public static String appendTheFile(int version, String text)
	{
		return "";
	}
	//update 
	public static void exit()
	{
		
	}

}
