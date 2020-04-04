import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.io.*;
import java.util.*;


public class TextEditor{

 //it will be used in login auth functions
 //auto-flush
 private static PrintWriter writer = null;
 private static BufferedReader fromServer = null;
 private static InputStream inputStream = null;
 private static InputStreamReader inputStreamre = null;
 private static Boolean userAuth = false;
 private static Boolean passAuth = false; 
 private static int version = 1;
 private static String ipaddress = "localhost";
 private static int port = 60000;

 public static void main(String args[]) throws IOException
 {
  //String ipaddress = args[0];// = "localhost";
  //int port = Integer.parseInt(args[1]);// = 60000;
  Socket socket = new Socket(ipaddress, port);
  System.out.println("Connection is started.");

  //to take the input from client
  inputStream = socket.getInputStream();
  inputStreamre = new InputStreamReader(inputStream);
  fromServer = new BufferedReader(inputStreamre);
  writer = new PrintWriter(socket.getOutputStream(), true);
  
  getFileFromServer();

  //USER AUTH
  System.out.print("\nEnter Username");
  //String in = scanner.readLine();
  //exit function will be writen
  userAuth = sendUserCommand("bilkentstu");
  if(userAuth == false)
  {
   socket.close();
  }
  //PASS AUTH
  System.out.print("\nEnter password");
  passAuth = sendPassCommand("cs421s2020");
  if(passAuth == false)
  {
   socket.close();
  }
 }
 public static boolean sendUserCommand(String usernameClient)
 {
  writer.println("USER " + usernameClient +"\r\n");
  String[] response = {"OK","INVALID"};
  //String responseFromServer = fromServer.readLine();
  //System.out.println(responseFromServer);
  try {
    String responseFromServer = fromServer.readLine();
  System.out.println(responseFromServer);
   return responseFromServer.contains(response[0]) && !responseFromServer.contains(response[1]);
  }
  catch(IOException exc)
  {
   System.out.println("About Username:" + exc.getMessage());
  }
  return false;
 }
 public static boolean sendPassCommand(String passwordClient)
 {
  writer.println("PASS " + passwordClient +"\r\n");
  String[] response = {"OK","INVALID"};
  //String responseFromServer = fromServer.readLine();
  //System.out.println(responseFromServer);
  try {
     String responseFromServer = fromServer.readLine();
  System.out.println(responseFromServer);
   return responseFromServer.contains(response[0]) && !responseFromServer.contains(response[1]);
  }
  catch(IOException exc)
  {
   System.out.println("About Password:" + exc.getMessage());
  }  
  return false;
 }
 
 public static void getFileFromServer()
 {
    int bytesRead;
    int current = 0;
    FileOutputStream fos = null;
    BufferedOutputStream bos = null;
    Socket sock = null;
    String ourfile = ("CS421_2020SPRING_PA1.txt");
    
    try {
      sock = new Socket(ipaddress, port);
      byte [] mybytearray  = new byte [6022386];
      InputStream is = sock.getInputStream();
      fos = new FileOutputStream(ourfile);
      bos = new BufferedOutputStream(fos);
      bytesRead = is.read(mybytearray,0,mybytearray.length);
      current = bytesRead;

      do {
         bytesRead =
            is.read(mybytearray, current, (mybytearray.length-current));
         if(bytesRead >= 0) current += bytesRead;
      } while(bytesRead > -1);

      bos.write(mybytearray, 0 , current);
      bos.flush();
      System.out.println("File " + ourfile + " is retrieved: " + current + " bytes read received");
    }
    catch(IOException exc)
    {
      System.out.println("About Password:" + exc.getMessage());
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
