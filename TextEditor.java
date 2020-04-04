import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.io.*;
import java.util.*;

public class TextEditor {

	private static PrintWriter writer = null;
	private static BufferedReader fromServer = null;
	private static InputStream inputStream = null;
	private static InputStreamReader inputStreamRe = null;
	private static Socket socketClient = null;
	private static Scanner in = null;
	private static FileOutputStream fileOut = null;
	private static BufferedOutputStream bufferOut = null;

	//Authentication
	private static Boolean userAuth = false;
	private static Boolean passAuth = false;

	//Version of the file
	private static  int version = 1;

	//	private static String ipaddress = "localhost";
	//	private static int port = 60000;

	public static void main(String args[]) throws IOException {

		String ipaddress = args[0]; // = "localhost";
		int port = Integer.parseInt(args[1]);// = 60000;

		//Connect to the server
		socketClient = new Socket(ipaddress, port);
		System.out.println("Connection is started.");

		// to take the input from client
		inputStream = socketClient.getInputStream();
		inputStreamRe = new InputStreamReader(inputStream);
		fromServer = new BufferedReader(inputStreamRe);
		writer = new PrintWriter(socketClient.getOutputStream(), true);
		in  = new Scanner(System.in);

		// USER AUTH
		System.out.print("\nEnter Username");
		String userName = in.nextLine();
		userAuth = sendUserCommand(userName);
		if (userAuth == false) {
			exitClient();
		}
		// PASS AUTH
		System.out.print("\nEnter password");
		String password = in.nextLine();
		passAuth = sendPassCommand(password);
		if (passAuth == false) {
			exitClient();
		}

		boolean ok = false;
		System.out.println("Please enter a command to execute: ");
		String command = in.nextLine();
		while(command != "EXIT")
		{
			if(command.contains("UPDT"))
			{
				ok = updateTheVersion();
				if(ok)
				{
					getFileFromServer();
				}
				else
				{
					System.out.println("The other functions can be used.");
				}
			}
			else if(command.contains("WRTE"))
			{
				String[] words = new String[4];
				int count = 0;
				for(int i = 0; i< command.length(); i++)
				{
					if(command.substring(i,i+1) == " ")
					{
						words[count] = command.substring(0,i);
						command = command.substring(i+2,command.length()-i);
						count++;
					}
				}
				String version = words[1];
				int versionX = Integer.parseInt(version);
				String lineNo = words[2];
				int line = Integer.parseInt(lineNo);

				ok = writeToFile(versionX,line, words[3]);
				if(ok)
				{
					System.out.println("Operation is successful");
				}
			}
			else if(command.contains("UPDT"))
			{
				String[] words = new String[3];
				int count = 0;
				for(int i = 0; i< command.length(); i++)
				{
					if(command.substring(i,i+1) == " ")
					{
						words[count] = command.substring(0,i);
						command = command.substring(i+2,command.length()-i);
						count++;
					}
				}
				String version = words[1];
				int versionX = Integer.parseInt(version);
				ok = appendTheFile(versionX, words[2]);
			}
			System.out.println("Please enter a command to execute: ");
			command = in.nextLine();
		}
		exitClient();
	}

	public static boolean sendUserCommand(String usernameClient) 
	{
		writer.println("USER " + usernameClient + "\r\n");
		String[] response = { "OK", "INVALID" };
		// String responseFromServer = fromServer.readLine();
		// System.out.println(responseFromServer);
		try {
			String responseFromServer = fromServer.readLine();
			System.out.println(responseFromServer);
			return responseFromServer.contains(response[0]) && !responseFromServer.contains(response[1]);
		} catch (IOException exc) {
			System.out.println("About Username:" + exc.getMessage());
		}
		return false;
	}

	public static boolean sendPassCommand(String passwordClient)
	{
		writer.println("PASS " + passwordClient + "\r\n");
		String[] response = { "OK", "INVALID" };
		// String responseFromServer = fromServer.readLine();
		// System.out.println(responseFromServer);
		try {
			String responseFromServer = fromServer.readLine();
			System.out.println(responseFromServer);
			return responseFromServer.contains(response[0]) && !responseFromServer.contains(response[1]);
		} catch (IOException exc) {
			System.out.println("About Password:" + exc.getMessage());
		}
		return false;
	}
	public static void getFileFromServer()
	{	
		byte[] byteArray = new byte[6022386];
		String ourfile = ("CS421_2020SPRING_PA1.txt");

		try {
			//get the input stream
			inputStream = socketClient.getInputStream();
			fileOut = new FileOutputStream(ourfile);

			//read the stream to array
			inputStream.read(byteArray, 0, byteArray.length);

			//write array to the file
			fileOut.write(byteArray, 0,byteArray.length);

			int version = getVersionOfClient();
			version++;
			setVersionOfClient(version);

		} catch (IOException exc) {
			System.out.println("About file update:" + exc.getMessage());
		}
	}

	public static boolean updateTheVersion()
	{
		writer.println("UPDT " + getVersionOfClient());
		String[] response = { "OK", "INVALID" };

		try
		{
			String responseFromServer = fromServer.readLine();
			System.out.println(responseFromServer);
			return responseFromServer.contains(response[0]) && !responseFromServer.contains(response[1]);
		}
		catch (IOException exc) {
			System.out.println("About version: " + exc.getMessage());
		}
		return false;
	}

	public static boolean writeToFile(int version, int lineNo, String text) 
	{
		writer.println("WRTE " + version + " " +lineNo + " "+ text + "/r/n");
		String[] response = { "OK", "INVALID" };

		try
		{
			String responseFromServer = fromServer.readLine();
			System.out.println(responseFromServer);
			return responseFromServer.contains(response[0]) && !responseFromServer.contains(response[1]);
		}
		catch (IOException exc) {
			System.out.println("About Append: " + exc.getMessage());
		}
		return false;
	}
	public static boolean appendTheFile(int version, String text) 
	{
		writer.println("APND " + version + " " + text + "/r/n");
		String[] response = { "OK", "INVALID" };

		try
		{
			String responseFromServer = fromServer.readLine();
			System.out.println(responseFromServer);
			return responseFromServer.contains(response[0]) && !responseFromServer.contains(response[1]);
		}
		catch (IOException exc) {
			System.out.println("About Append: " + exc.getMessage());
		}
		return false;
	}
	public static void exitClient()
	{
		try
		{
			writer.close();
			fromServer.close();
			inputStream.close();
			inputStreamRe.close();
			socketClient.close();
			fileOut.close();
			bufferOut.close();
			in.close();
		}
		catch(IOException exc)
		{
			System.out.println("Exception while exiting: " + exc.getMessage());
		}
		System.out.println("Connection is closed.");
		System.exit(0);
	}
	public static int getVersionOfClient()
	{
		return version;
	}
	public static void setVersionOfClient(int versionUpdate)
	{
		version = versionUpdate;
	}
}
