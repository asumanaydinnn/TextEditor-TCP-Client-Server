import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;


public class TextEditor{
	
	public static void main(String args[]) throws IOException
	{
		Scanner scanner = new Scanner(System.in);
  String ipaddress = args[0];
  int port = Integer.parseInt(args[1]);
  Socket socket = new Socket(ipaddress, port);
  InputStream inputStream = socket.getInputStream();
  InputStreamReader isr = new InputStreamReader(inputStream);
  BufferedReader reader = new BufferedReader(isr);
  PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
  
       
        System.out.print("\nEnter Username Command");
        String username = scanner.nextLine();
        writer.println(username);
        username = reader.readLine();
        System.out.println(username);

        if (username.equals("EXIT\r\n"))
        {
            writer.println("EXIT");
            isr.close();
            reader.close();
            writer.close();
            inputStream.close();
            socket.close();
        }

        if (!username.equals("USER bilkentstu\r\n"))
        {
            writer.println("EXIT");
            isr.close();
            reader.close();
            writer.close();
            inputStream.close();
            socket.close();
        }

        System.out.print("\nEnter Password:");
        String password = scanner.nextLine();
        writer.println(password);
        password = reader.readLine();
        System.out.println(password);

        if (password.equals("EXIT\r\n"))
        {
            writer.println("EXIT");
            isr.close();
            reader.close();
            writer.close();
            inputStream.close();
            socket.close();
        }

        
        if (!password.equals("PASS 421s2020\r\n"))
        {
            writer.println("EXIT");
            isr.close();
            reader.close();
            writer.close();
            inputStream.close();
            socket.close();
        }
  
 }
 
		
		
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
