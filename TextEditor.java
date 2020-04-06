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
 int version;

 public static void main(String args[]) throws IOException {
  
  TextEditor myObje = new TextEditor();
  myObje.version = 0;
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
  System.out.print("Username Command:");
  String userName = in.nextLine();
  userAuth = sendUserCommand(userName);
  if (userAuth == false) {
   exitClient();
  }
  // PASS AUTH
  System.out.print("Password Command:");
  String password = in.nextLine();
  passAuth = sendPassCommand(password);
  if (passAuth == false) {
   exitClient();
  }
  String[] response = { "OK", "INVALID" };
  System.out.println("Please enter a command to execute: ");
  String command = in.nextLine();
  while(command != "EXIT")
  {
   if(command.contains("UPDT"))
   {
    String responseFromServer =  updateTheVersion(myObje.getVersionOfClient());
    System.out.println(responseFromServer);
    if(responseFromServer.contains(response[0]) && !responseFromServer.contains(response[1]))
    {
     int versionCurrent = myObje.getVersionOfClient();
     versionCurrent++;
     myObje.setVersionOfClient(versionCurrent);
    }
    else
    {
     System.out.println("The other functions can be used.");
    }
   }
   else if(command.contains("WRTE"))
   {
    System.out.println("What line do you want to add?");
    String lineNo = in.nextLine();
    int line = Integer.parseInt(lineNo);
    System.out.println("What text do you want to add?");
    String text = in.nextLine();
    String responseFromServer = writeToFile((myObje.getVersionOfClient()),line,text);
    System.out.println(responseFromServer);
    if(responseFromServer.contains(response[0]) && !responseFromServer.contains(response[1]))
    {
     int versionCurrent = myObje.getVersionOfClient();
     versionCurrent++;
     myObje.setVersionOfClient(versionCurrent);
     System.out.println("Operation is successful");
    }
   }
   else if(command.contains("APND"))
   {
    System.out.println("what is the text you want to add?");
    String text = in.nextLine();
    String responseFromServer = appendTheFile(myObje.getVersionOfClient(), text);
    System.out.println(responseFromServer);
    if(responseFromServer.contains(response[0]) && !responseFromServer.contains(response[1]))
    {
     int versionCurrent = myObje.getVersionOfClient();
     versionCurrent++;
     myObje.setVersionOfClient(versionCurrent);
     System.out.println("Operation is successful");
    }
   }
   System.out.println("Please enter a command to execute: ");
   command = in.nextLine();
  }
  exitClient();
 }

 public static boolean sendUserCommand(String usernameClient) 
 {
  writer.println(usernameClient);
  String[] response = { "OK", "INVALID" };
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
  writer.println(passwordClient);
  String[] response = { "OK", "INVALID" };
  try {
   String responseFromServer = fromServer.readLine();
   System.out.println(responseFromServer);
   return responseFromServer.contains(response[0]) && !responseFromServer.contains(response[1]);
  } catch (IOException exc) {
   System.out.println("About Password:" + exc.getMessage());
  }
  return false;
 }
 public static String updateTheVersion(int versionCurrent)
 {
  writer.println("UPDT " + versionCurrent);
  try
  {
   String responseFromServer = fromServer.readLine();
   System.out.println(responseFromServer);
   return responseFromServer;
  }
  catch (IOException exc) {
   return exc.getMessage();
  }
 }
 public static String writeToFile(int version, int lineNo, String text) 
 {
  writer.println("WRTE " + version + " " + lineNo + " "+ text + "");
  try
  {
   String responseFromServer = fromServer.readLine();
   System.out.println(responseFromServer);
   return responseFromServer;
  }
  catch (IOException exc) {
   return exc.getMessage();
  }
 }
 public static String appendTheFile(int version, String text) 
 {
  writer.println("APND " + version + " " + text + "");
  try
  {
   String responseFromServer = fromServer.readLine();
   System.out.println(responseFromServer);
   return responseFromServer;
  }
  catch (IOException exc) {
   return exc.getMessage();
  }
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
 public int getVersionOfClient()
 {
  return this.version;
 }
 public void setVersionOfClient(int versionCurrent)
 {
  this.version = versionCurrent;
 }
}
