import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class cdht_ex
{
  static int base;
  static int peerIdentity;
  static int firstSuccessor;
  static int secondSuccessor;
  static int peerPort;
  static int firstSuccessorPort;
  static int secondSuccessorPort;
  static int count = 0;
  static boolean firstPingBack = false;
  static boolean secondPingBack = false;
  static boolean userInput = false;
  static boolean requestFileMessage = false;
  static DatagramSocket UDPSocket;
  
  public static DatagramPacket UDPClient(int destinationPort)
    throws Exception
  {
    String message = "Request";
    InetAddress IPAddress = InetAddress.getByName("localhost");
    byte[] sendData = message.getBytes();
    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, destinationPort);
    return sendPacket;
  }
  
  public static void UDPServer()
    throws Exception
  {
    boolean haveShownHideMessage = false;
    boolean haveShownHideRequestMessage = false;
    
    String message = "Response";
    byte[] receiveData = new byte['Ѐ'];
    byte[] sendData = message.getBytes();
    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
    for (;;)
    {
      UDPSocket.receive(receivePacket);
      int port = receivePacket.getPort();
      InetAddress IPAdress = receivePacket.getAddress();
      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAdress, port);
      if ((userInput) && (!haveShownHideMessage)) {
        haveShownHideMessage = true;
      }
      if ((requestFileMessage) && (!haveShownHideRequestMessage))
      {
        System.out.println("Ping messages are hide\n");
        haveShownHideRequestMessage = true;
      }
      if ((port != firstSuccessorPort) && (port != secondSuccessorPort))
      {
        UDPSocket.send(sendPacket);
        int peer = port - base;
        if ((!userInput) && (!requestFileMessage)) {
          System.out.println("A ping request message was received from Peer " + peer + ".");
        }
      }
      if (port == firstSuccessorPort)
      {
        if ((!userInput) && (!requestFileMessage)) {
          System.out.println("A ping response message was received from Peer " + firstSuccessor + ".");
        }
        firstPingBack = true;
      }
      if (port == secondSuccessorPort)
      {
        if ((!userInput) && (!requestFileMessage)) {
          System.out.println("A ping response message was received from Peer " + secondSuccessor + ".");
        }
        secondPingBack = true;
      }
    }
  }
  
  public static int stringToHash(String fileName)
  {
    int w = Integer.parseInt(String.valueOf(fileName.charAt(0)));
    int x = Integer.parseInt(String.valueOf(fileName.charAt(1)));
    int y = Integer.parseInt(String.valueOf(fileName.charAt(2)));
    int z = Integer.parseInt(String.valueOf(fileName.charAt(3)));
    int hash = (w * 1000 + x * 100 + y * 10 + z + 1) % 256;
    return hash;
  }
  
  public static void TCPServer()
    throws Exception
  {
    ServerSocket welcomeServer = new ServerSocket(peerPort);
    for (;;)
    {
      Socket connectionSocket = welcomeServer.accept();
      BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
      String clientMessage = inFromClient.readLine();
      String status = clientMessage.split(" ")[0];
      if (status.equals("quit"))
      {
        if (clientMessage.split(" ")[1].equals("ok"))
        {
          count += 1;
          if (count == 2) {
            System.out.println("I can quit now.");
          }
        }
        else
        {
          userInput = false;
          requestFileMessage = false;
          int quitPeer = Integer.parseInt(clientMessage.split(" ")[1]);
          if ((quitPeer != firstSuccessor) && (quitPeer != secondSuccessor))
          {
            TCPClient(clientMessage + "\n", firstSuccessorPort);
          }
          else
          {
            if (quitPeer != firstSuccessor) {
              TCPClient(clientMessage + "\n", firstSuccessorPort);
            }
            int newFirstSuccessor = Integer.parseInt(clientMessage.split(" ")[2]);
            int newSecondSuccessor = Integer.parseInt(clientMessage.split(" ")[3]);
            if (firstSuccessor != quitPeer)
            {
              secondSuccessor = firstSuccessor == newFirstSuccessor ? newSecondSuccessor : newFirstSuccessor;
            }
            else
            {
              firstSuccessor = secondSuccessor == newFirstSuccessor ? newSecondSuccessor : newFirstSuccessor;
              int tmp = firstSuccessor;
              firstSuccessor = secondSuccessor;
              secondSuccessor = tmp;
            }
            firstSuccessorPort = base + firstSuccessor;secondSuccessorPort = base + secondSuccessor;TCPClient("quit ok " + peerIdentity + "\n", quitPeer + base);System.out.println(quitPeer + " " + peerIdentity);System.out.println("Peer " + quitPeer + " will depart from the network.");System.out.println("My first successor is now peer " + firstSuccessor);System.out.println("My second successor is now peer " + secondSuccessor);
          }
        }
      }
      else if (status.equals("ask"))
      {
        requestFileMessage = false;int reponseToPort = Integer.parseInt(clientMessage.split(" ")[1]);String whichSuccessor = clientMessage.split(" ")[2];
        if (whichSuccessor.equals("first")) {
          TCPClient("reponseAsk firstSuccessor " + firstSuccessor + "\n", reponseToPort);
        } else {
          TCPClient("reponseAsk secondSuccessor " + secondSuccessor + "\n", reponseToPort);
        }
      }
      else if (status.equals("reponseAsk"))
      {
        requestFileMessage = false;String changedSuccessor = clientMessage.split(" ")[1];int newSuccessor = Integer.parseInt(clientMessage.split(" ")[2]);
        if (changedSuccessor.equals("firstSuccessor"))
        {
          firstSuccessor = secondSuccessor;secondSuccessor = newSuccessor;firstSuccessorPort = firstSuccessor + base;secondSuccessorPort = secondSuccessor + base;System.out.println("My first successor is now peer " + firstSuccessor);System.out.println("My second successor is now peer " + secondSuccessor);
        }
        else
        {
          secondSuccessor = newSuccessor;secondSuccessorPort = secondSuccessor + base;System.out.println("My first successor is now peer " + firstSuccessor);System.out.println("My second successor is now peer " + secondSuccessor);
        }
      }
      else
      {
        requestFileMessage = true;String fileName = clientMessage.split(" ")[1];int hashValue = Integer.parseInt(clientMessage.split(" ")[2]);int sourcePeerIdentity = Integer.parseInt(clientMessage.split(" ")[3]);int clientPeerIdentity = Integer.parseInt(clientMessage.split(" ")[4]);
        if (status.equals("Response"))
        {
          System.out.println("Received a response message from peer " + clientPeerIdentity + ", which has the file " + fileName);
        }
        else
        {
          boolean forwardMessage;
          boolean forwardMessage;
          if ((hashValue == peerIdentity) || ((peerIdentity < hashValue) && (firstSuccessor < hashValue) && (peerIdentity > firstSuccessor)) || ((peerIdentity < hashValue) && (firstSuccessor > hashValue))) {
            forwardMessage = false;
          } else {
            forwardMessage = true;
          }
          if (forwardMessage)
          {
            TCPClient("Request " + fileName + " " + hashValue + " " + sourcePeerIdentity + " " + peerIdentity + "\n", firstSuccessorPort);System.out.println("File " + fileName + " is not stored here.\nFile request message has been forwarded to my successor.");
          }
          else
          {
            TCPClient("Response " + fileName + " " + hashValue + " " + sourcePeerIdentity + " " + peerIdentity + "\n", base + sourcePeerIdentity);System.out.println("File " + fileName + " is stored here.\nA response message, destined for peer " + sourcePeerIdentity + ", has been sent.");
          }
        }
      }
    }
  }
  
  public static void TCPClient(String message, int destPort)
  {
    try
    {
      Socket clientSocket = new Socket("localhost", destPort);
      DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
      outToServer.writeBytes(message);
    }
    catch (Exception e)
    {
      System.out.println("----Socket Create failed-----");
    }
  }
  
  public static void main(String[] args)
    throws IOException
  {
    peerIdentity = Integer.parseInt(args[0]);
    firstSuccessor = Integer.parseInt(args[1]);
    secondSuccessor = Integer.parseInt(args[2]);
    base = 50000;
    peerPort = base + peerIdentity;
    firstSuccessorPort = base + firstSuccessor;
    secondSuccessorPort = base + secondSuccessor;
    UDPSocket = new DatagramSocket(peerPort);
    
    final Thread newServer = new Thread()
    {
      public void run()
      {
        try
        {
          
        }
        catch (Exception e) {}
      }
    };
    Thread firstClient = new Thread()
    {
      public void run()
      {
        try
        {
          for (;;)
          {
            DatagramPacket packet = cdht_ex.UDPClient(cdht_ex.firstSuccessorPort);
            cdht_ex.UDPSocket.send(packet);
            Thread.currentThread();Thread.sleep(3000L);
          }
          return;
        }
        catch (Exception e) {}
      }
    };
    final Thread secondClient = new Thread()
    {
      public void run()
      {
        try
        {
          for (;;)
          {
            DatagramPacket packet = cdht_ex.UDPClient(cdht_ex.secondSuccessorPort);
            cdht_ex.UDPSocket.send(packet);
            Thread.currentThread();Thread.sleep(3000L);
          }
          return;
        }
        catch (Exception e) {}
      }
    };
    final Thread check = new Thread()
    {
      public void run()
      {
        for (;;)
        {
          try
          {
            Thread.currentThread();Thread.sleep(12000L);
          }
          catch (InterruptedException e)
          {
            return;
          }
          if (!cdht_ex.firstPingBack)
          {
            if (!cdht_ex.firstPingBack)
            {
              String changeSuccessor = "ask " + cdht_ex.peerPort + " first\n";cdht_ex.TCPClient(changeSuccessor, cdht_ex.secondSuccessorPort);cdht_ex.firstPingBack = false;System.out.println("Peer " + cdht_ex.firstSuccessor + " is no longer alive.");
            }
          }
          else {
            cdht_ex.firstPingBack = false;
          }
          if (cdht_ex.secondPingBack)
          {
            cdht_ex.secondPingBack = false;
          }
          else if (!cdht_ex.secondPingBack)
          {
            String changeSuccessor = "ask " + cdht_ex.peerPort + " second\n";cdht_ex.TCPClient(changeSuccessor, cdht_ex.firstSuccessorPort);cdht_ex.secondPingBack = false;System.out.println("Peer " + cdht_ex.secondSuccessor + " is no longer alive.");
          }
        }
      }
    };
    final Thread requestFileServer = new Thread()
    {
      public void run()
      {
        try
        {
          
        }
        catch (Exception e) {}
      }
    };
    Thread waitInput = new Thread()
    {
      public void run()
      {
        Scanner sc = new Scanner(System.in);
        for (;;)
        {
          String inputString = sc.nextLine();
          if (inputString.equals(""))
          {
            if ((cdht_ex.userInput) || (cdht_ex.requestFileMessage))
            {
              cdht_ex.userInput = false;
              cdht_ex.requestFileMessage = false;
              System.out.println("You have pressed enter key, ping messages are displayed again\n");
            }
            else if (!cdht_ex.userInput)
            {
              cdht_ex.userInput = true;
              System.out.println("You have pressed enter key, ping messages are hide\n");
            }
          }
          else if (inputString.equals("quit"))
          {
            String sendMessage = "quit " + cdht_ex.peerIdentity + " " + cdht_ex.firstSuccessor + " " + cdht_ex.secondSuccessor + "\n";
            try
            {
              cdht_ex.TCPClient(sendMessage, cdht_ex.firstSuccessorPort);
            }
            catch (Exception e)
            {
              return;
            }
            try
            {
              Thread.currentThread();Thread.sleep(3000L);
            }
            catch (InterruptedException e)
            {
              return;
            }
            this.val$firstClient.interrupt();
            secondClient.interrupt();
            check.interrupt();
            if (cdht_ex.count == 2)
            {
              newServer.interrupt();
              requestFileServer.interrupt();
            }
          }
          else
          {
            String command = inputString.split(" ")[0];
            if (command.equals("request"))
            {
              String fileName = inputString.split(" ")[1];
              int hashValue = cdht_ex.stringToHash(fileName);
              String sendMessage = "request " + fileName + " " + hashValue + " " + cdht_ex.peerIdentity + " " + cdht_ex.peerIdentity + "\n";
              try
              {
                cdht_ex.TCPClient(sendMessage, cdht_ex.firstSuccessorPort);
              }
              catch (Exception e)
              {
                return;
              }
            }
          }
        }
      }
    };
    newServer.start();
    firstClient.start();
    secondClient.start();
    check.start();
    requestFileServer.start();
    waitInput.start();
  }
}
