import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;


public class TextEditor{

	//it will be used in login auth functions
	//auto-flush
	PrintWriter writer = null;
	BufferReader fromServer = null;
	InputStream inputStream = null;
	InputStreamReader inputStreamre = null;
	Boolean userAuth = false;
	Boolean passAuth = false; 
	int version = 1;

	public static void main(String args[]) throws IOException
	{
		Scanner scanner = new Scanner(System.in);
		String ipaddress = args[0];
		int port = Integer.parseInt(args[1]);
		Socket socket = new Socket(ipaddress, port);
		System.out.println("Connection is started.");

		//to take the input from client
		inputStream = socket.getInputStream();
		inputStreamre = new InputStreamReader(inputStream);
		fromServer = new BufferedReader(isr);
		writer = new PrintWriter(socket.getOutputStream(), true);

		//USER AUTH
		System.out.print("\nEnter Username Command");

		writer.println("USER " + usernameClient +"\r\n");
		String[] response = {"OK","INVALID"};
		String responseFromServer = fromServer.readLine();
		System.out.println(responseFromServer);
		try {
			if(responseFromServer.contains(response[0]) && !responseFromServer.contains(response[1]))
			{
				userAuth = true;
			}
		}
		catch(IOException exc)
		{
			System.out.println("About Username:" + exc.getMessage());
			userAuth = false;
		}
		
		//exit function will be writen
		if(userAuth == false)
		{
			socket.close();
		}

		//PASS AUTH
		System.out.print("\nEnter password Command");
		writer.println("PASS " + passwordClient +"\r\n");
		String[] response = {"OK","INVALID"};
		String responseFromServer = fromServer.readLine();
		System.out.println(responseFromServer);
		try {
			if( responseFromServer.contains(response[0]) && !responseFromServer.contains(response[1]))
			{
				passAuth = true;
			}
		}
		catch(IOException exc)
		{
			System.out.println("About Password:" + exc.getMessage());
			passAuth = false;
		}  

		//exit function will be writen
		if(passAuth == false)
		{
			socket.close();
		}
	}
	public static void updateTheVersion(int version)
	{
		writer.println("UPDT "+ version);
		
	}
	public static void writeToFile(int version, int lineNo, String text)
	{

	}
	public static String appendTheFile(int version, String text)
	{
		return "";
	}
	
	public static void exit()
	{

	}

}
