package a;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ConnectException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class a
{
  public static int a;
  public static int b;
  public static int c;
  public static int d = 0;
  public static int e = 0;
  public static int f;
  public static int g;
  public static int h;
  public static int i;
  public static int j;
  public static final int k = 30000;
  public static final int l = 1000;
  public static final int m = 4;
  public static final int n = 1000;
  public static final String o = "127.0.0.1";
  public static boolean p;
  
  static class b
    extends Thread
  {
    public void run()
    {
      Object localObject = null;int a = a.c.b;
      ServerSocket welcomeSocket;
      try
      {
        welcomeSocket = new ServerSocket(50000 + a.a);
      }
      catch (IOException ex)
      {
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
          if (parsedSentence[0].equals("FILE"))
          {
            System.out.println("Received a response message from peer " + parsedSentence[2] + ", which has the file " + parsedSentence[1] + ".");
            if (a == 0) {
              if (a == 0) {
                break label1078;
              }
            }
          }
          else if (parsedSentence[0].equals("LIST"))
          {
            DataOutputStream output = new DataOutputStream(connectionSocket.getOutputStream());
            output.writeBytes("PEERS " + a.b + " " + a.c + "\n");
            if (a == 0) {
              break label1078;
            }
          }
          if (parsedSentence[0].equals("QUIT"))
          {
            int departingPeer = Integer.parseInt(parsedSentence[3]);
            System.out.println("Peer " + departingPeer + " will depart from the network.");
            DataOutputStream output = new DataOutputStream(connectionSocket.getOutputStream());
            output.writeBytes("QUIT_RECEIVED\n");
            if ((departingPeer == a.c) && (departingPeer == a.e))
            {
              a.c = a.d;
              a.e = a.b;
              System.out.println("My first successor is now peer " + a.b + ".");
              System.out.println("My second successor is now peer " + a.c + ".");
              System.out.println("My first predecessor is now peer " + a.d + ".");
              System.out.println("My second predecessor is now peer " + a.e + ".");
              if (a == 0) {}
            }
            else if ((departingPeer == a.b) || (departingPeer == a.c))
            {
              if (departingPeer == a.b)
              {
                a.b = a.c;
                a.c = Integer.parseInt(parsedSentence[2]);
                if (a == 0) {}
              }
              else
              {
                a.c = Integer.parseInt(parsedSentence[1]);
              }
              System.out.println("My first successor is now peer " + a.b + ".");
              System.out.println("My second successor is now peer " + a.c + ".");
              if (a == 0) {}
            }
            else if ((departingPeer == a.d) || (departingPeer == a.e))
            {
              if (departingPeer == a.d)
              {
                a.d = a.e;
                a.e = Integer.parseInt(parsedSentence[2]);
                if (a == 0) {}
              }
              else
              {
                a.e = Integer.parseInt(parsedSentence[1]);
              }
              System.out.println("My first predecessor is now peer " + a.d + ".");
              System.out.println("My second predecessor is now peer " + a.e + ".");
            }
            if (a == 0) {}
          }
          else if (parsedSentence[0].equals("REQUEST"))
          {
            String fileRequested = parsedSentence[1];
            String sender = parsedSentence[2];
            String requester = parsedSentence[3];
            int hashFile = Integer.parseInt(fileRequested) % 256;
            if ((a.a - Integer.parseInt(sender) + 256) % 256 > (a.a - hashFile + 256) % 256)
            {
              System.out.println("File " + fileRequested + " is here.");
              System.out.println("A response message, destined for peer " + requester + ", has been sent.");
              Socket sendSocket = new Socket(InetAddress.getByName("127.0.0.1"), 50000 + Integer.parseInt(requester));
              DataOutputStream output = new DataOutputStream(sendSocket.getOutputStream());
              String sentence = "FILE " + fileRequested + " " + a.a;
              output.writeBytes(sentence + "\n");
              sendSocket.close();
              if (a == 0) {}
            }
            else
            {
              Socket sendSocket;
              try
              {
                sendSocket = new Socket(InetAddress.getByName("127.0.0.1"), 50000 + a.b);
              }
              catch (ConnectException e)
              {
                Socket sendSocket;
                sendSocket = new Socket(InetAddress.getByName("127.0.0.1"), 50000 + a.c);
              }
              DataOutputStream output = new DataOutputStream(sendSocket.getOutputStream());
              output.writeBytes("REQUEST " + fileRequested + " " + a.a + " " + requester + "\n");
              System.out.println("File " + fileRequested + " is not stored here.");
              System.out.println("File request message has been forwarded to my successor.");
              sendSocket.close();
            }
          }
          label1078:
          connectionSocket.close();
        }
      }
      catch (IOException ex)
      {
        System.out.println(ex);
      }
    }
  }
  
  static class c
    extends Thread
  {
    DatagramSocket a;
    public static boolean b;
    
    public c(DatagramSocket ds)
    {
      this.a = ds;
    }
    
    public void run()
    {
      byte[] arrayOfByte1 = new byte['Ѐ'];int a = b;
      for (;;)
      {
        byte[] buffer;
        DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
        try
        {
          this.a.receive(dp);
          int clientPort = dp.getPort();
          
          String[] parsedSentence = a(dp).split(" ");
          if (parsedSentence[0].equals("REPLY"))
          {
            System.out.println("A ping response message was received from Peer " + (clientPort - 50000) + ".");
            if (a != 0) {
              break label141;
            }
            if (clientPort - 50000 == a.b)
            {
              a.g = Integer.parseInt(parsedSentence[1]);
              if (a == 0) {}
            }
            else
            {
              a.h = Integer.parseInt(parsedSentence[1]);
            }
          }
          if (parsedSentence[0].equals("PING"))
          {
            label141:
            System.out.println("A ping request message was received from Peer " + (clientPort - 50000) + ".");
            a(clientPort - 50000);
            
            byte[] buf = ("REPLY " + parsedSentence[1] + "\n").getBytes();
            DatagramPacket reply = new DatagramPacket(buf, buf.length, dp.getAddress(), clientPort);
            this.a.send(reply);
          }
        }
        catch (IOException ex)
        {
          System.err.println(ex);
        }
        catch (Exception ex)
        {
          System.err.println(ex);
        }
      }
    }
    
    private String a(DatagramPacket request)
      throws Exception
    {
      byte[] buf = request.getData();
      ByteArrayInputStream bais = new ByteArrayInputStream(buf);
      InputStreamReader isr = new InputStreamReader(bais);
      BufferedReader br = new BufferedReader(isr);
      return br.readLine();
    }
    
    private void a(int newPeer)
    {
      int a = b;
      if ((a.d == 0) && (a.e == 0))
      {
        a.d = newPeer;
        return;
      }
      if ((a.e == 0) && (a.d != 0))
      {
        if ((a.a - a.d + 256) % 256 < (a.a - newPeer + 256) % 256)
        {
          a.e = newPeer;
          if (a == 0) {}
        }
        else
        {
          a.e = a.d;
          a.d = newPeer;
        }
        return;
      }
      if ((newPeer != a.d) && (newPeer != a.e))
      {
        a.d = newPeer;
        a.e = 0;
      }
    }
  }
  
  static class d
    extends TimerTask
  {
    DatagramSocket a;
    
    d(DatagramSocket socket)
    {
      this.a = socket;
    }
    
    public void run()
    {
      byte[] sendData = new byte['Ѐ'];
      String str1 = "PING " + a.f + "\n";int a = a.c.b;
      String sentence;
      a.f = (a.f + 1) % 65536;
      sendData = sentence.getBytes();
      try
      {
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("127.0.0.1"), a.b + 50000);
        this.a.send(sendPacket);
        sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("127.0.0.1"), a.c + 50000);
        this.a.send(sendPacket);
        Thread.sleep(1000L);
        if (a.g != a.f - 1)
        {
          a.i += 1;
          if (a == 0) {}
        }
        else
        {
          a.i = 0;
        }
        if (a.h != a.f - 1)
        {
          a.j += 1;
          if (a == 0) {}
        }
        else
        {
          a.j = 0;
        }
        if ((a.i >= 4) || (a.j >= 4))
        {
          int deadPeer = a.b;
          int livePeer = a.c;
          if (a.j >= 4)
          {
            deadPeer = a.c;
            livePeer = a.b;
          }
          System.out.println("Peer " + deadPeer + " is no longer alive.");
          
          Socket sendSocket = new Socket(InetAddress.getByName("127.0.0.1"), 50000 + livePeer);
          DataOutputStream output = new DataOutputStream(sendSocket.getOutputStream());
          output.writeBytes("LIST\n");
          
          BufferedReader inFromServer = new BufferedReader(new InputStreamReader(sendSocket.getInputStream()));
          
          String replyFromServerSentence = inFromServer.readLine();
          String[] parsedSentence = replyFromServerSentence.split(" ");
          if (deadPeer == a.b)
          {
            a.b = a.c;
            a.c = Integer.parseInt(parsedSentence[1]);
            if (a == 0) {}
          }
          else if (Integer.parseInt(parsedSentence[1]) == deadPeer)
          {
            a.c = Integer.parseInt(parsedSentence[2]);
            if (a == 0) {}
          }
          else
          {
            a.c = Integer.parseInt(parsedSentence[1]);
          }
          System.out.println("My first successor is now peer " + a.b + ".");
          System.out.println("My second successor is now peer " + a.c + ".");
          sendSocket.close();
        }
      }
      catch (IOException ex)
      {
        System.out.println(ex);
      }
      catch (InterruptedException ex)
      {
        System.out.println(ex);
      }
    }
  }
  
  public static void main(String[] args)
    throws Exception
  {
    a = Integer.parseInt(args[0]);int a = c.b;
    b = Integer.parseInt(args[1]);
    c = Integer.parseInt(args[2]);
    InetAddress IPAddress = InetAddress.getByName("127.0.0.1");
    f = new Random().nextInt(65536);
    g = h = f;
    i = j = 0;
    
    DatagramSocket socket = new DatagramSocket(50000 + a);
    d ping = new d(socket);
    Timer timer = new Timer();
    timer.schedule(ping, 1000L, 30000L);
    
    c UDPThread = new c(socket);
    UDPThread.start();
    
    b TCPThread = new b();
    TCPThread.start();
    
    Scanner s = new Scanner(System.in);
    do
    {
      String input;
      do
      {
        do
        {
          if ((input = s.nextLine()).isEmpty()) {
            return;
          }
          if (!input.equalsIgnoreCase("quit")) {
            break;
          }
          Socket sendSocketFirst = new Socket(IPAddress, 50000 + b);
          Socket sendSocketSecond = new Socket(IPAddress, 50000 + c);
          DataOutputStream outToServerFirst = new DataOutputStream(sendSocketFirst.getOutputStream());
          DataOutputStream outToServerSecond = new DataOutputStream(sendSocketSecond.getOutputStream());
          String quitMessage = "QUIT " + d + " " + e + " " + a;
          outToServerFirst.writeBytes(quitMessage + "\n");
          outToServerSecond.writeBytes(quitMessage + "\n");
          BufferedReader inFromFirst = new BufferedReader(new InputStreamReader(sendSocketFirst.getInputStream()));
          BufferedReader inFromSecond = new BufferedReader(new InputStreamReader(sendSocketSecond.getInputStream()));
          inFromFirst.readLine();
          inFromSecond.readLine();
          sendSocketFirst.close();
          sendSocketSecond.close();
          
          Socket sendSocketA = new Socket(IPAddress, 50000 + d);
          DataOutputStream outToServerA = new DataOutputStream(sendSocketA.getOutputStream());
          quitMessage = "QUIT " + b + " " + c + " " + a;
          outToServerA.writeBytes(quitMessage + "\n");
          BufferedReader inFromA = new BufferedReader(new InputStreamReader(sendSocketA.getInputStream()));
          inFromA.readLine();
          sendSocketA.close();
          if (e != c)
          {
            Socket sendSocketB = new Socket(IPAddress, 50000 + e);
            DataOutputStream outToServerB = new DataOutputStream(sendSocketB.getOutputStream());
            outToServerB.writeBytes(quitMessage + "\n");
            BufferedReader inFromB = new BufferedReader(new InputStreamReader(sendSocketB.getInputStream()));
            inFromB.readLine();
            sendSocketB.close();
          }
          System.out.println("Peer " + a + " has left the network.");
          System.exit(0);
        } while (a == 0);
        p = !p;
        if ((input.length() != 12) || (!input.substring(0, 7).equalsIgnoreCase("request"))) {
          break;
        }
        String requestedFile = input.substring(8, input.length());
        try
        {
          Integer hash = Integer.valueOf(Integer.parseInt(requestedFile) % 256);
          Socket sendSocket;
          try
          {
            sendSocket = new Socket(IPAddress, 50000 + b);
          }
          catch (ConnectException e)
          {
            Socket sendSocket;
            sendSocket = new Socket(IPAddress, 50000 + c);
          }
          DataOutputStream outToServer = new DataOutputStream(sendSocket.getOutputStream());
          String sentence = "REQUEST " + requestedFile + " " + a + " " + a;
          outToServer.writeBytes(sentence + "\n");
          sendSocket.close();
          System.out.println("File request message for " + requestedFile + " has been sent to my successor. File hash is " + hash + ".");
        }
        catch (NumberFormatException ex)
        {
          System.out.println("Filename is not numeric.");
        }
      } while (a == 0);
      System.out.println("Unknown command : " + input);
      System.out.println("Syntax : QUIT or REQUEST <wxyz>");
    } while (a == 0);
  }
}
