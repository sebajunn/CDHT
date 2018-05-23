package cdht;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ConnectException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.Timer;

public class cdht
{
  static
  {
    PING_TIMEOUT = 1000;MAX_LOST_PINGS = 4;FIRST_PING = 1000;PING_TIMING = 30000; } public static int firstPredecessor = 0; public static String IP_ADDRESS = "127.0.0.1";
  public static int secondPredecessor = 0;
  public static int peerID;
  public static int firstPeer;
  public static int secondPeer;
  public static int seqNum;
  public static int firstAck;
  public static int secondAck;
  public static int firstLostPings;
  public static int secondLostPings;
  public static int PING_TIMING;
  public static int FIRST_PING;
  public static int MAX_LOST_PINGS;
  public static int PING_TIMEOUT;
  
  static class TCPServerThread extends Thread
  {
    public TCPServerThread() {}
    
    public void run() {
      java.net.ServerSocket welcomeSocket = null;
      try {
        welcomeSocket = new java.net.ServerSocket(50000 + cdht.peerID);
      } catch (IOException ex) {
        System.out.println(ex);
      }
      try
      {
        for (;;)
        {
          Socket connectionSocket = welcomeSocket.accept();
          
          BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
          String incomingSentence = inFromClient.readLine();
          
          String[] parsedSentence = incomingSentence.split(" ");
          



          if (parsedSentence[0].equals("FILE")) {
            System.out.println("Received a response message from peer " + parsedSentence[2] + ", which has the file " + parsedSentence[1] + ".");


          }
          else if (parsedSentence[0].equals("LIST")) {
            DataOutputStream output = new DataOutputStream(connectionSocket.getOutputStream());
            output.writeBytes("PEERS " + cdht.firstPeer + " " + cdht.secondPeer + "\n");

          }
          else if (parsedSentence[0].equals("QUIT")) {
            int departingPeer = Integer.parseInt(parsedSentence[3]);
            System.out.println("Peer " + departingPeer + " will depart from the network.");
            DataOutputStream output = new DataOutputStream(connectionSocket.getOutputStream());
            output.writeBytes("QUIT_RECEIVED\n");
            
            if ((departingPeer == cdht.secondPeer) && (departingPeer == cdht.secondPredecessor)) {
              cdht.secondPeer = cdht.firstPredecessor;
              cdht.secondPredecessor = cdht.firstPeer;
              System.out.println("My first successor is now peer " + cdht.firstPeer + ".");
              System.out.println("My second successor is now peer " + cdht.secondPeer + ".");
              System.out.println("My first predecessor is now peer " + cdht.firstPredecessor + ".");
              System.out.println("My second predecessor is now peer " + cdht.secondPredecessor + ".");
            }
            else if ((departingPeer == cdht.firstPeer) || (departingPeer == cdht.secondPeer)) {
              if (departingPeer == cdht.firstPeer) {
                cdht.firstPeer = cdht.secondPeer;
                cdht.secondPeer = Integer.parseInt(parsedSentence[2]);
              } else {
                cdht.secondPeer = Integer.parseInt(parsedSentence[1]);
              }
              System.out.println("My first successor is now peer " + cdht.firstPeer + ".");
              System.out.println("My second successor is now peer " + cdht.secondPeer + ".");
            }
            else if ((departingPeer == cdht.firstPredecessor) || (departingPeer == cdht.secondPredecessor)) {
              if (departingPeer == cdht.firstPredecessor) {
                cdht.firstPredecessor = cdht.secondPredecessor;
                cdht.secondPredecessor = Integer.parseInt(parsedSentence[2]);
              } else {
                cdht.secondPredecessor = Integer.parseInt(parsedSentence[1]);
              }
              System.out.println("My first predecessor is now peer " + cdht.firstPredecessor + ".");
              System.out.println("My second predecessor is now peer " + cdht.secondPredecessor + ".");
            }
            
          }
          else if (parsedSentence[0].equals("REQUEST")) {
            String fileRequested = parsedSentence[1];
            String sender = parsedSentence[2];
            String requester = parsedSentence[3];
            int hashFile = Integer.parseInt(fileRequested) % 256;
            

            if ((cdht.peerID - Integer.parseInt(sender) + 256) % 256 > (cdht.peerID - hashFile + 256) % 256) {
              System.out.println("File " + fileRequested + " is here.");
              System.out.println("A response message, destined for peer " + requester + ", has been sent.");
              Socket sendSocket = new Socket(InetAddress.getByName("127.0.0.1"), 50000 + Integer.parseInt(requester));
              DataOutputStream output = new DataOutputStream(sendSocket.getOutputStream());
              String sentence = "FILE " + fileRequested + " " + cdht.peerID;
              output.writeBytes(sentence + "\n");
              sendSocket.close();
            }
            else {
              Socket sendSocket;
              try {
                sendSocket = new Socket(InetAddress.getByName("127.0.0.1"), 50000 + cdht.firstPeer);
              } catch (ConnectException e) { Socket sendSocket;
                sendSocket = new Socket(InetAddress.getByName("127.0.0.1"), 50000 + cdht.secondPeer);
              }
              DataOutputStream output = new DataOutputStream(sendSocket.getOutputStream());
              output.writeBytes("REQUEST " + fileRequested + " " + cdht.peerID + " " + requester + "\n");
              System.out.println("File " + fileRequested + " is not stored here.");
              System.out.println("File request message has been forwarded to my successor.");
              sendSocket.close();
            }
          }
          connectionSocket.close();
        }
      } catch (IOException ex) {
        System.out.println(ex);
      }
    }
  }
  
  static class UDPServerThread extends Thread
  {
    public DatagramSocket socket;
    
    public UDPServerThread(DatagramSocket ds)
    {
      socket = ds;
    }
    
    public void run()
    {
      byte[] buffer = new byte['Ѐ'];
      for (;;) {
        DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
        try
        {
          socket.receive(dp);
          int clientPort = dp.getPort();
          
          byte b1 = 58;int i = clientPort - 1;i = clientPort * i;i %= 2; if (((i < 0 ^ true) & true)) b1 = -10; String[] parsedSentence = super.M730967787(dp, b1).split(" ");
          


          if (parsedSentence[0].equals("REPLY")) {
            System.out.println("A ping response message was received from Peer " + (clientPort - 50000) + ".");
            
            if (clientPort - 50000 == cdht.firstPeer) {
              cdht.firstAck = Integer.parseInt(parsedSentence[1]);
            } else
              cdht.secondAck = Integer.parseInt(parsedSentence[1]);
          }
          if (parsedSentence[0].equals("PING")) {
            System.out.println("A ping request message was received from Peer " + (clientPort - 50000) + ".");
            byte b2 = 109;int j = clientPort - 1;j = clientPort * j;j %= 2; if (((j < 0 ^ true) & true)) b2 = 62; super.M240536415(clientPort - 50000, b2);
            
            byte[] buf = ("REPLY " + parsedSentence[1] + "\n").getBytes();
            DatagramPacket reply = new DatagramPacket(buf, buf.length, dp.getAddress(), clientPort);
            socket.send(reply);
          }
        } catch (IOException ex) {
          System.err.println(ex);
        } catch (Exception ex) {
          System.err.println(ex);
        }
      }
    }
    
    public String M730967787(DatagramPacket request, byte arg2) throws Exception
    {
      buf = request.getData();
      java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(buf);
      InputStreamReader isr = new InputStreamReader(bais);
      BufferedReader br = new BufferedReader(isr);
      return br.readLine();
    }
    
    public void M240536415(int newPeer, byte paramByte)
    {
      if ((cdht.firstPredecessor == 0) && (cdht.secondPredecessor == 0)) {
        cdht.firstPredecessor = newPeer;
        return;
      }
      
      if ((cdht.secondPredecessor == 0) && (cdht.firstPredecessor != 0)) {
        if ((cdht.peerID - cdht.firstPredecessor + 256) % 256 < (cdht.peerID - newPeer + 256) % 256) {
          cdht.secondPredecessor = newPeer;
        } else {
          cdht.secondPredecessor = cdht.firstPredecessor;
          cdht.firstPredecessor = newPeer;
        }
        return;
      }
      

      if ((newPeer != cdht.firstPredecessor) && (newPeer != cdht.secondPredecessor)) {
        cdht.firstPredecessor = newPeer;
        cdht.secondPredecessor = 0;
      }
    }
  }
  
  static class PingTask extends java.util.TimerTask
  {
    public DatagramSocket socket;
    
    public PingTask(DatagramSocket socket)
    {
      this.socket = socket;
    }
    

    public void run()
    {
      byte[] sendData = new byte['Ѐ'];
      String sentence = "PING " + cdht.seqNum + "\n";
      cdht.seqNum = (cdht.seqNum + 1) % 65536;
      sendData = sentence.getBytes();
      try {
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("127.0.0.1"), cdht.firstPeer + 50000);
        socket.send(sendPacket);
        sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("127.0.0.1"), cdht.secondPeer + 50000);
        socket.send(sendPacket);
        Thread.sleep(1000L);
        if (cdht.firstAck != cdht.seqNum - 1) {
          cdht.firstLostPings += 1;
        } else
          cdht.firstLostPings = 0;
        if (cdht.secondAck != cdht.seqNum - 1) {
          cdht.secondLostPings += 1;
        } else {
          cdht.secondLostPings = 0;
        }
        
        if ((cdht.firstLostPings >= 4) || (cdht.secondLostPings >= 4)) {
          int deadPeer = cdht.firstPeer;
          int livePeer = cdht.secondPeer;
          if (cdht.secondLostPings >= 4) {
            deadPeer = cdht.secondPeer;
            livePeer = cdht.firstPeer;
          }
          System.out.println("Peer " + deadPeer + " is no longer alive.");
          
          Socket sendSocket = new Socket(InetAddress.getByName("127.0.0.1"), 50000 + livePeer);
          DataOutputStream output = new DataOutputStream(sendSocket.getOutputStream());
          output.writeBytes("LIST\n");
          
          BufferedReader inFromServer = new BufferedReader(new InputStreamReader(sendSocket.getInputStream()));
          
          String replyFromServerSentence = inFromServer.readLine();
          String[] parsedSentence = replyFromServerSentence.split(" ");
          
          if (deadPeer == cdht.firstPeer) {
            cdht.firstPeer = cdht.secondPeer;
            cdht.secondPeer = Integer.parseInt(parsedSentence[1]);


          }
          else if (Integer.parseInt(parsedSentence[1]) == deadPeer) {
            cdht.secondPeer = Integer.parseInt(parsedSentence[2]);
          }
          else
          {
            cdht.secondPeer = Integer.parseInt(parsedSentence[1]);
          }
          System.out.println("My first successor is now peer " + cdht.firstPeer + ".");
          System.out.println("My second successor is now peer " + cdht.secondPeer + ".");
          sendSocket.close();
        }
      }
      catch (IOException ex) {
        System.out.println(ex);
      } catch (InterruptedException ex) {
        System.out.println(ex);
      }
    }
  }
  
  public static void main(String[] args)
    throws Exception
  {
    peerID = Integer.parseInt(args[0]);
    firstPeer = Integer.parseInt(args[1]);
    secondPeer = Integer.parseInt(args[2]);
    InetAddress IPAddress = InetAddress.getByName("127.0.0.1");
    seqNum = new java.util.Random().nextInt(65536);
    firstAck = cdht.secondAck = seqNum;
    firstLostPings = cdht.secondLostPings = 0;
    

    DatagramSocket socket = new DatagramSocket(50000 + peerID);
    PingTask ping = new PingTask(socket);
    Timer timer = new Timer();
    timer.schedule(ping, 1000L, 30000L);
    

    UDPServerThread UDPThread = new UDPServerThread(socket);
    UDPThread.start();
    

    TCPServerThread TCPThread = new TCPServerThread();
    TCPThread.start();
    

    Scanner s = new Scanner(System.in);
    
    String input;
    while (!(input = s.nextLine()).isEmpty())
    {
      if (input.equalsIgnoreCase("quit"))
      {
        Socket sendSocketFirst = new Socket(IPAddress, 50000 + firstPeer);
        Socket sendSocketSecond = new Socket(IPAddress, 50000 + secondPeer);
        DataOutputStream outToServerFirst = new DataOutputStream(sendSocketFirst.getOutputStream());
        DataOutputStream outToServerSecond = new DataOutputStream(sendSocketSecond.getOutputStream());
        String quitMessage = "QUIT " + firstPredecessor + " " + secondPredecessor + " " + peerID;
        outToServerFirst.writeBytes(quitMessage + "\n");
        outToServerSecond.writeBytes(quitMessage + "\n");
        BufferedReader inFromFirst = new BufferedReader(new InputStreamReader(sendSocketFirst.getInputStream()));
        BufferedReader inFromSecond = new BufferedReader(new InputStreamReader(sendSocketSecond.getInputStream()));
        inFromFirst.readLine();
        inFromSecond.readLine();
        sendSocketFirst.close();
        sendSocketSecond.close();
        

        Socket sendSocketA = new Socket(IPAddress, 50000 + firstPredecessor);
        DataOutputStream outToServerA = new DataOutputStream(sendSocketA.getOutputStream());
        quitMessage = "QUIT " + firstPeer + " " + secondPeer + " " + peerID;
        outToServerA.writeBytes(quitMessage + "\n");
        BufferedReader inFromA = new BufferedReader(new InputStreamReader(sendSocketA.getInputStream()));
        inFromA.readLine();
        sendSocketA.close();
        if (secondPredecessor != secondPeer) {
          Socket sendSocketB = new Socket(IPAddress, 50000 + secondPredecessor);
          DataOutputStream outToServerB = new DataOutputStream(sendSocketB.getOutputStream());
          outToServerB.writeBytes(quitMessage + "\n");
          BufferedReader inFromB = new BufferedReader(new InputStreamReader(sendSocketB.getInputStream()));
          inFromB.readLine();
          sendSocketB.close();
        }
        
        System.out.println("Peer " + peerID + " has left the network.");
        System.exit(0);

      }
      else if ((input.length() == 12) && (input.substring(0, 7).equalsIgnoreCase("request"))) {
        String requestedFile = input.substring(8, input.length());
        try
        {
          Integer hash = Integer.valueOf(Integer.parseInt(requestedFile) % 256);
          Socket sendSocket;
          try {
            sendSocket = new Socket(IPAddress, 50000 + firstPeer);
          } catch (ConnectException e) { Socket sendSocket;
            sendSocket = new Socket(IPAddress, 50000 + secondPeer);
          }
          DataOutputStream outToServer = new DataOutputStream(sendSocket.getOutputStream());
          String sentence = "REQUEST " + requestedFile + " " + peerID + " " + peerID;
          outToServer.writeBytes(sentence + "\n");
          sendSocket.close();
          System.out.println("File request message for " + requestedFile + " has been sent to my successor. File hash is " + hash + ".");
        } catch (NumberFormatException ex) {
          System.out.println("Filename is not numeric.");
        }
      }
      else {
        System.out.println("Unknown command : " + input);
        System.out.println("Syntax : QUIT or REQUEST <wxyz>");
      }
    }
  }
  
  public cdht() {}
}
