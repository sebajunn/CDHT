package cdht;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ConnectException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class cdht
{
  public static int peerID;
  public static int firstPeer;
  public static int secondPeer;
  public static int firstPredecessor = 0;
  public static int secondPredecessor;
  public static int seqNum;
  public static int firstAck;
  public static int secondAck;
  public static int firstLostPings;
  public static int secondLostPings;
  public static final int PING_TIMING = 30000;
  public static final int FIRST_PING = 1000;
  public static final int MAX_LOST_PINGS = 4;
  public static final int PING_TIMEOUT = 1000;
  public static final String IP_ADDRESS = "127.0.0.1";
  
  public static void main(String[] paramArrayOfString)
    throws Exception
  {
    peerID = Integer.parseInt(paramArrayOfString[0]);
    firstPeer = Integer.parseInt(paramArrayOfString[1]);
    secondPeer = Integer.parseInt(paramArrayOfString[2]);
    InetAddress localInetAddress = InetAddress.getByName("127.0.0.1");
    seqNum = new Random().nextInt(65536);
    int i = 0;
    int j = i - 1;
    j = i * j;
    j %= 2;
    i -= -1;
    firstAck = secondAck = j - 0 != 0 ? seqNum : seqNum;
    firstLostPings = secondLostPings = 0;
    DatagramSocket localDatagramSocket = new DatagramSocket(50000 + peerID);
    PingTask localPingTask = new PingTask(localDatagramSocket);
    Timer localTimer = new Timer();
    localTimer.schedule(localPingTask, 1000L, 30000L);
    UDPServerThread localUDPServerThread = new UDPServerThread(localDatagramSocket);
    localUDPServerThread.start();
    TCPServerThread localTCPServerThread = new TCPServerThread();
    localTCPServerThread.start();
    Scanner localScanner = new Scanner(System.in);
    String str1;
    while (!(str1 = localScanner.nextLine()).isEmpty())
    {
      Object localObject1;
      Object localObject2;
      Object localObject3;
      String str2;
      if (str1.equalsIgnoreCase("quit"))
      {
        localObject1 = new Socket(localInetAddress, 50000 + firstPeer);
        localObject2 = new Socket(localInetAddress, 50000 + secondPeer);
        localObject3 = new DataOutputStream(((Socket)localObject1).getOutputStream());
        DataOutputStream localDataOutputStream1 = new DataOutputStream(((Socket)localObject2).getOutputStream());
        str2 = "QUIT " + firstPredecessor + " " + secondPredecessor + " " + peerID;
        ((DataOutputStream)localObject3).writeBytes(str2 + "\n");
        localDataOutputStream1.writeBytes(str2 + "\n");
        BufferedReader localBufferedReader1 = new BufferedReader(new InputStreamReader(((Socket)localObject1).getInputStream()));
        BufferedReader localBufferedReader2 = new BufferedReader(new InputStreamReader(((Socket)localObject2).getInputStream()));
        localBufferedReader1.readLine();
        localBufferedReader2.readLine();
        ((Socket)localObject1).close();
        ((Socket)localObject2).close();
        Socket localSocket1 = new Socket(localInetAddress, 50000 + firstPredecessor);
        DataOutputStream localDataOutputStream3 = new DataOutputStream(localSocket1.getOutputStream());
        str2 = "QUIT " + firstPeer + " " + secondPeer + " " + peerID;
        localDataOutputStream3.writeBytes(str2 + "\n");
        BufferedReader localBufferedReader3 = new BufferedReader(new InputStreamReader(localSocket1.getInputStream()));
        localBufferedReader3.readLine();
        localSocket1.close();
        if (secondPredecessor != secondPeer)
        {
          Socket localSocket2 = new Socket(localInetAddress, 50000 + secondPredecessor);
          DataOutputStream localDataOutputStream4 = new DataOutputStream(localSocket2.getOutputStream());
          localDataOutputStream4.writeBytes(str2 + "\n");
          BufferedReader localBufferedReader4 = new BufferedReader(new InputStreamReader(localSocket2.getInputStream()));
          localBufferedReader4.readLine();
          localSocket2.close();
        }
        System.out.println("Peer " + peerID + " has left the network.");
        System.exit(0);
      }
      else if ((str1.length() == 12) && (str1.substring(0, 7).equalsIgnoreCase("request")))
      {
        localObject1 = str1.substring(8, str1.length());
        try
        {
          localObject2 = Integer.valueOf(Integer.parseInt((String)localObject1) % 256);
          try
          {
            localObject3 = new Socket(localInetAddress, 50000 + firstPeer);
          }
          catch (ConnectException localConnectException)
          {
            localObject3 = new Socket(localInetAddress, 50000 + secondPeer);
          }
          DataOutputStream localDataOutputStream2 = new DataOutputStream(((Socket)localObject3).getOutputStream());
          str2 = "REQUEST " + (String)localObject1 + " " + peerID + " " + peerID;
          localDataOutputStream2.writeBytes(str2 + "\n");
          ((Socket)localObject3).close();
          System.out.println("File request message for " + (String)localObject1 + " has been sent to my successor. File hash is " + localObject2 + ".");
        }
        catch (NumberFormatException localNumberFormatException)
        {
          System.out.println("Filename is not numeric.");
        }
      }
      else
      {
        System.out.println("Unknown command : " + str1);
        System.out.println("Syntax : QUIT or REQUEST <wxyz>");
      }
    }
  }
  
  static
  {
    int i = -1;
    int j = i - 1;
    j = i * j;
    j %= 2;
    if (j - 0 != 0)
    {
      i /= 5;
      i *= 3;
      secondPredecessor = 0;
    }
    else
    {
      secondPredecessor = 0;
    }
  }
  
  static class PingTask
    extends TimerTask
  {
    DatagramSocket socket;
    
    PingTask(DatagramSocket paramDatagramSocket)
    {
      this.socket = paramDatagramSocket;
      int i = 2;
      int j = i - 1;
      j = i * j;
      j %= 2;
      if (j - 0 != 0)
      {
        i -= 1;
        return;
      }
    }
    
    /* Error */
    public void run()
    {
      // Byte code:
      //   0: sipush 1024
      //   3: newarray <illegal type>
      //   5: astore_1
      //   6: new 4	java/lang/StringBuilder
      //   9: dup
      //   10: invokespecial 5	java/lang/StringBuilder:<init>	()V
      //   13: ldc 6
      //   15: invokevirtual 7	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   18: getstatic 8	cdht/cdht:seqNum	I
      //   21: invokevirtual 9	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
      //   24: ldc 10
      //   26: invokevirtual 7	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   29: invokevirtual 11	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   32: astore_2
      //   33: getstatic 8	cdht/cdht:seqNum	I
      //   36: iconst_1
      //   37: iadd
      //   38: ldc 12
      //   40: irem
      //   41: putstatic 8	cdht/cdht:seqNum	I
      //   44: aload_2
      //   45: invokevirtual 13	java/lang/String:getBytes	()[B
      //   48: astore_1
      //   49: new 14	java/net/DatagramPacket
      //   52: dup
      //   53: aload_1
      //   54: aload_1
      //   55: arraylength
      //   56: ldc 15
      //   58: invokestatic 16	java/net/InetAddress:getByName	(Ljava/lang/String;)Ljava/net/InetAddress;
      //   61: getstatic 17	cdht/cdht:firstPeer	I
      //   64: ldc 18
      //   66: iadd
      //   67: invokespecial 19	java/net/DatagramPacket:<init>	([BILjava/net/InetAddress;I)V
      //   70: astore_3
      //   71: aload_0
      //   72: getfield 3	cdht/cdht$PingTask:socket	Ljava/net/DatagramSocket;
      //   75: aload_3
      //   76: invokevirtual 20	java/net/DatagramSocket:send	(Ljava/net/DatagramPacket;)V
      //   79: new 14	java/net/DatagramPacket
      //   82: dup
      //   83: aload_1
      //   84: aload_1
      //   85: arraylength
      //   86: ldc 15
      //   88: invokestatic 16	java/net/InetAddress:getByName	(Ljava/lang/String;)Ljava/net/InetAddress;
      //   91: getstatic 21	cdht/cdht:secondPeer	I
      //   94: ldc 18
      //   96: iadd
      //   97: invokespecial 19	java/net/DatagramPacket:<init>	([BILjava/net/InetAddress;I)V
      //   100: astore_3
      //   101: aload_0
      //   102: getfield 3	cdht/cdht$PingTask:socket	Ljava/net/DatagramSocket;
      //   105: aload_3
      //   106: invokevirtual 20	java/net/DatagramSocket:send	(Ljava/net/DatagramPacket;)V
      //   109: ldc2_w 22
      //   112: invokestatic 24	java/lang/Thread:sleep	(J)V
      //   115: getstatic 25	cdht/cdht:firstAck	I
      //   118: getstatic 8	cdht/cdht:seqNum	I
      //   121: iconst_1
      //   122: isub
      //   123: if_icmpeq +14 -> 137
      //   126: getstatic 26	cdht/cdht:firstLostPings	I
      //   129: iconst_1
      //   130: iadd
      //   131: putstatic 26	cdht/cdht:firstLostPings	I
      //   134: goto +7 -> 141
      //   137: iconst_0
      //   138: putstatic 26	cdht/cdht:firstLostPings	I
      //   141: getstatic 27	cdht/cdht:secondAck	I
      //   144: getstatic 8	cdht/cdht:seqNum	I
      //   147: iconst_1
      //   148: isub
      //   149: if_icmpeq +14 -> 163
      //   152: getstatic 28	cdht/cdht:secondLostPings	I
      //   155: iconst_1
      //   156: iadd
      //   157: putstatic 28	cdht/cdht:secondLostPings	I
      //   160: goto +7 -> 167
      //   163: iconst_0
      //   164: putstatic 28	cdht/cdht:secondLostPings	I
      //   167: getstatic 26	cdht/cdht:firstLostPings	I
      //   170: iconst_4
      //   171: if_icmpge +10 -> 181
      //   174: getstatic 28	cdht/cdht:secondLostPings	I
      //   177: iconst_4
      //   178: if_icmplt +325 -> 503
      //   181: getstatic 17	cdht/cdht:firstPeer	I
      //   184: istore 4
      //   186: getstatic 21	cdht/cdht:secondPeer	I
      //   189: istore 5
      //   191: getstatic 28	cdht/cdht:secondLostPings	I
      //   194: iconst_4
      //   195: if_icmplt +13 -> 208
      //   198: getstatic 21	cdht/cdht:secondPeer	I
      //   201: istore 4
      //   203: getstatic 17	cdht/cdht:firstPeer	I
      //   206: istore 5
      //   208: getstatic 29	java/lang/System:out	Ljava/io/PrintStream;
      //   211: new 4	java/lang/StringBuilder
      //   214: dup
      //   215: invokespecial 5	java/lang/StringBuilder:<init>	()V
      //   218: ldc 30
      //   220: invokevirtual 7	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   223: iload 4
      //   225: invokevirtual 9	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
      //   228: ldc 31
      //   230: invokevirtual 7	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   233: invokevirtual 11	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   236: invokevirtual 32	java/io/PrintStream:println	(Ljava/lang/String;)V
      //   239: new 33	java/net/Socket
      //   242: dup
      //   243: ldc 15
      //   245: invokestatic 16	java/net/InetAddress:getByName	(Ljava/lang/String;)Ljava/net/InetAddress;
      //   248: ldc 18
      //   250: iload 5
      //   252: iadd
      //   253: invokespecial 34	java/net/Socket:<init>	(Ljava/net/InetAddress;I)V
      //   256: astore 6
      //   258: new 35	java/io/DataOutputStream
      //   261: dup
      //   262: aload 6
      //   264: invokevirtual 36	java/net/Socket:getOutputStream	()Ljava/io/OutputStream;
      //   267: invokespecial 37	java/io/DataOutputStream:<init>	(Ljava/io/OutputStream;)V
      //   270: astore 7
      //   272: aload 7
      //   274: ldc 38
      //   276: invokevirtual 39	java/io/DataOutputStream:writeBytes	(Ljava/lang/String;)V
      //   279: new 40	java/io/BufferedReader
      //   282: dup
      //   283: new 41	java/io/InputStreamReader
      //   286: dup
      //   287: aload 6
      //   289: invokevirtual 42	java/net/Socket:getInputStream	()Ljava/io/InputStream;
      //   292: invokespecial 43	java/io/InputStreamReader:<init>	(Ljava/io/InputStream;)V
      //   295: invokespecial 44	java/io/BufferedReader:<init>	(Ljava/io/Reader;)V
      //   298: nop
      //   299: iload 4
      //   301: iconst_1
      //   302: isub
      //   303: istore 11
      //   305: iload 4
      //   307: iload 11
      //   309: imul
      //   310: istore 11
      //   312: iload 11
      //   314: iconst_2
      //   315: irem
      //   316: istore 11
      //   318: iload 11
      //   320: iconst_0
      //   321: isub
      //   322: nop
      //   323: ifeq +31 -> 354
      //   326: iload 4
      //   328: iconst_5
      //   329: idiv
      //   330: istore 4
      //   332: astore 8
      //   334: iload 4
      //   336: iconst_1
      //   337: idiv
      //   338: istore 4
      //   340: aload 8
      //   342: iload 4
      //   344: iconst_0
      //   345: isub
      //   346: istore 4
      //   348: invokevirtual 45	java/io/BufferedReader:readLine	()Ljava/lang/String;
      //   351: goto +10 -> 361
      //   354: astore 8
      //   356: aload 8
      //   358: invokevirtual 45	java/io/BufferedReader:readLine	()Ljava/lang/String;
      //   361: astore 9
      //   363: aload 9
      //   365: ldc 46
      //   367: invokevirtual 47	java/lang/String:split	(Ljava/lang/String;)[Ljava/lang/String;
      //   370: astore 10
      //   372: iload 4
      //   374: getstatic 17	cdht/cdht:firstPeer	I
      //   377: if_icmpne +22 -> 399
      //   380: getstatic 21	cdht/cdht:secondPeer	I
      //   383: putstatic 17	cdht/cdht:firstPeer	I
      //   386: aload 10
      //   388: iconst_1
      //   389: aaload
      //   390: invokestatic 48	java/lang/Integer:parseInt	(Ljava/lang/String;)I
      //   393: putstatic 21	cdht/cdht:secondPeer	I
      //   396: goto +38 -> 434
      //   399: aload 10
      //   401: iconst_1
      //   402: aaload
      //   403: invokestatic 48	java/lang/Integer:parseInt	(Ljava/lang/String;)I
      //   406: iload 4
      //   408: if_icmpne +16 -> 424
      //   411: aload 10
      //   413: iconst_2
      //   414: aaload
      //   415: invokestatic 48	java/lang/Integer:parseInt	(Ljava/lang/String;)I
      //   418: putstatic 21	cdht/cdht:secondPeer	I
      //   421: goto +13 -> 434
      //   424: aload 10
      //   426: iconst_1
      //   427: aaload
      //   428: invokestatic 48	java/lang/Integer:parseInt	(Ljava/lang/String;)I
      //   431: putstatic 21	cdht/cdht:secondPeer	I
      //   434: getstatic 29	java/lang/System:out	Ljava/io/PrintStream;
      //   437: new 4	java/lang/StringBuilder
      //   440: dup
      //   441: invokespecial 5	java/lang/StringBuilder:<init>	()V
      //   444: ldc 49
      //   446: invokevirtual 7	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   449: getstatic 17	cdht/cdht:firstPeer	I
      //   452: invokevirtual 9	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
      //   455: ldc 50
      //   457: invokevirtual 7	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   460: invokevirtual 11	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   463: invokevirtual 32	java/io/PrintStream:println	(Ljava/lang/String;)V
      //   466: getstatic 29	java/lang/System:out	Ljava/io/PrintStream;
      //   469: new 4	java/lang/StringBuilder
      //   472: dup
      //   473: invokespecial 5	java/lang/StringBuilder:<init>	()V
      //   476: ldc 51
      //   478: invokevirtual 7	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   481: getstatic 21	cdht/cdht:secondPeer	I
      //   484: invokevirtual 9	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
      //   487: ldc 50
      //   489: invokevirtual 7	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   492: invokevirtual 11	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   495: invokevirtual 32	java/io/PrintStream:println	(Ljava/lang/String;)V
      //   498: aload 6
      //   500: invokevirtual 52	java/net/Socket:close	()V
      //   503: goto +22 -> 525
      //   506: astore_3
      //   507: getstatic 29	java/lang/System:out	Ljava/io/PrintStream;
      //   510: aload_3
      //   511: invokevirtual 54	java/io/PrintStream:println	(Ljava/lang/Object;)V
      //   514: goto +11 -> 525
      //   517: astore_3
      //   518: getstatic 29	java/lang/System:out	Ljava/io/PrintStream;
      //   521: aload_3
      //   522: invokevirtual 54	java/io/PrintStream:println	(Ljava/lang/Object;)V
      //   525: return
      // Exception table:
      //   from	to	target	type
      //   49	503	506	java/io/IOException
      //   49	503	517	java/lang/InterruptedException
    }
  }
  
  static class UDPServerThread
    extends Thread
  {
    DatagramSocket socket;
    
    public UDPServerThread(DatagramSocket paramDatagramSocket)
    {
      this.socket = paramDatagramSocket;
      int i = 2;
      int j = i - 1;
      j = i * j;
      j %= 2;
      if (j - 0 != 0)
      {
        i += 4;
        return;
      }
    }
    
    /* Error */
    public void run()
    {
      // Byte code:
      //   0: sipush 1024
      //   3: newarray <illegal type>
      //   5: astore_1
      //   6: new 3	java/net/DatagramPacket
      //   9: dup
      //   10: aload_1
      //   11: aload_1
      //   12: arraylength
      //   13: invokespecial 4	java/net/DatagramPacket:<init>	([BI)V
      //   16: astore_2
      //   17: aload_0
      //   18: getfield 2	cdht/cdht$UDPServerThread:socket	Ljava/net/DatagramSocket;
      //   21: aload_2
      //   22: invokevirtual 5	java/net/DatagramSocket:receive	(Ljava/net/DatagramPacket;)V
      //   25: aload_2
      //   26: invokevirtual 6	java/net/DatagramPacket:getPort	()I
      //   29: istore_3
      //   30: aload_0
      //   31: aload_2
      //   32: invokespecial 7	cdht/cdht$UDPServerThread:dataToString	(Ljava/net/DatagramPacket;)Ljava/lang/String;
      //   35: ldc 8
      //   37: invokevirtual 9	java/lang/String:split	(Ljava/lang/String;)[Ljava/lang/String;
      //   40: astore 4
      //   42: aload 4
      //   44: iconst_0
      //   45: aaload
      //   46: ldc 10
      //   48: invokevirtual 11	java/lang/String:equals	(Ljava/lang/Object;)Z
      //   51: ifeq +69 -> 120
      //   54: getstatic 12	java/lang/System:out	Ljava/io/PrintStream;
      //   57: new 13	java/lang/StringBuilder
      //   60: dup
      //   61: invokespecial 14	java/lang/StringBuilder:<init>	()V
      //   64: ldc 15
      //   66: invokevirtual 16	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   69: iload_3
      //   70: ldc 17
      //   72: isub
      //   73: invokevirtual 18	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
      //   76: ldc 19
      //   78: invokevirtual 16	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   81: invokevirtual 20	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   84: invokevirtual 21	java/io/PrintStream:println	(Ljava/lang/String;)V
      //   87: iload_3
      //   88: ldc 17
      //   90: isub
      //   91: getstatic 22	cdht/cdht:firstPeer	I
      //   94: if_icmpne +16 -> 110
      //   97: aload 4
      //   99: iconst_1
      //   100: aaload
      //   101: invokestatic 23	java/lang/Integer:parseInt	(Ljava/lang/String;)I
      //   104: putstatic 24	cdht/cdht:firstAck	I
      //   107: goto +13 -> 120
      //   110: aload 4
      //   112: iconst_1
      //   113: aaload
      //   114: invokestatic 23	java/lang/Integer:parseInt	(Ljava/lang/String;)I
      //   117: putstatic 25	cdht/cdht:secondAck	I
      //   120: aload 4
      //   122: iconst_0
      //   123: aaload
      //   124: ldc 26
      //   126: invokevirtual 11	java/lang/String:equals	(Ljava/lang/Object;)Z
      //   129: ifeq +140 -> 269
      //   132: getstatic 12	java/lang/System:out	Ljava/io/PrintStream;
      //   135: new 13	java/lang/StringBuilder
      //   138: dup
      //   139: invokespecial 14	java/lang/StringBuilder:<init>	()V
      //   142: ldc 27
      //   144: invokevirtual 16	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   147: iload_3
      //   148: ldc 17
      //   150: isub
      //   151: invokevirtual 18	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
      //   154: ldc 19
      //   156: invokevirtual 16	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   159: invokevirtual 20	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   162: invokevirtual 21	java/io/PrintStream:println	(Ljava/lang/String;)V
      //   165: aload_0
      //   166: iload_3
      //   167: ldc 17
      //   169: isub
      //   170: invokespecial 28	cdht/cdht$UDPServerThread:updatePredecessors	(I)V
      //   173: new 13	java/lang/StringBuilder
      //   176: dup
      //   177: invokespecial 14	java/lang/StringBuilder:<init>	()V
      //   180: ldc 29
      //   182: invokevirtual 16	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   185: aload 4
      //   187: iconst_1
      //   188: aaload
      //   189: invokevirtual 16	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   192: ldc 30
      //   194: nop
      //   195: iload_3
      //   196: iconst_1
      //   197: isub
      //   198: istore 7
      //   200: iload_3
      //   201: iload 7
      //   203: imul
      //   204: istore 7
      //   206: iload 7
      //   208: iconst_2
      //   209: irem
      //   210: istore 7
      //   212: iload 7
      //   214: iconst_0
      //   215: isub
      //   216: nop
      //   217: ifeq +13 -> 230
      //   220: iload_3
      //   221: iconst_4
      //   222: imul
      //   223: istore_3
      //   224: invokevirtual 16	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   227: goto +6 -> 233
      //   230: invokevirtual 16	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   233: invokevirtual 20	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   236: invokevirtual 31	java/lang/String:getBytes	()[B
      //   239: astore 5
      //   241: new 3	java/net/DatagramPacket
      //   244: dup
      //   245: aload 5
      //   247: aload 5
      //   249: arraylength
      //   250: aload_2
      //   251: invokevirtual 32	java/net/DatagramPacket:getAddress	()Ljava/net/InetAddress;
      //   254: iload_3
      //   255: invokespecial 33	java/net/DatagramPacket:<init>	([BILjava/net/InetAddress;I)V
      //   258: astore 6
      //   260: aload_0
      //   261: getfield 2	cdht/cdht$UDPServerThread:socket	Ljava/net/DatagramSocket;
      //   264: aload 6
      //   266: invokevirtual 34	java/net/DatagramSocket:send	(Ljava/net/DatagramPacket;)V
      //   269: goto +22 -> 291
      //   272: astore_3
      //   273: getstatic 36	java/lang/System:err	Ljava/io/PrintStream;
      //   276: aload_3
      //   277: invokevirtual 37	java/io/PrintStream:println	(Ljava/lang/Object;)V
      //   280: goto +11 -> 291
      //   283: astore_3
      //   284: getstatic 36	java/lang/System:err	Ljava/io/PrintStream;
      //   287: aload_3
      //   288: invokevirtual 37	java/io/PrintStream:println	(Ljava/lang/Object;)V
      //   291: goto -285 -> 6
      // Exception table:
      //   from	to	target	type
      //   17	269	272	java/io/IOException
      //   17	269	283	java/lang/Exception
    }
    
    /* Error */
    private String dataToString(java.net.DatagramPacket arg1)
      throws Exception
    {
      // Byte code:
      //   0: aload_1
      //   1: invokevirtual 39	java/net/DatagramPacket:getData	()[B
      //   4: astore_2
      //   5: new 40	java/io/ByteArrayInputStream
      //   8: dup
      //   9: aload_2
      //   10: invokespecial 41	java/io/ByteArrayInputStream:<init>	([B)V
      //   13: astore_3
      //   14: new 42	java/io/InputStreamReader
      //   17: dup
      //   18: aload_3
      //   19: invokespecial 43	java/io/InputStreamReader:<init>	(Ljava/io/InputStream;)V
      //   22: astore 4
      //   24: new 44	java/io/BufferedReader
      //   27: iconst_2
      //   28: istore 6
      //   30: nop
      //   31: iload 6
      //   33: iconst_1
      //   34: isub
      //   35: istore 7
      //   37: iload 6
      //   39: iload 7
      //   41: imul
      //   42: istore 7
      //   44: iload 7
      //   46: iconst_2
      //   47: irem
      //   48: istore 7
      //   50: iload 7
      //   52: iconst_0
      //   53: isub
      //   54: nop
      //   55: ifeq +30 -> 85
      //   58: iload 6
      //   60: iconst_0
      //   61: imul
      //   62: istore 6
      //   64: dup
      //   65: iload 6
      //   67: iconst_0
      //   68: iadd
      //   69: istore 6
      //   71: aload 4
      //   73: iload 6
      //   75: iconst_2
      //   76: imul
      //   77: istore 6
      //   79: invokespecial 45	java/io/BufferedReader:<init>	(Ljava/io/Reader;)V
      //   82: goto +9 -> 91
      //   85: dup
      //   86: aload 4
      //   88: invokespecial 45	java/io/BufferedReader:<init>	(Ljava/io/Reader;)V
      //   91: astore 5
      //   93: aload 5
      //   95: invokevirtual 46	java/io/BufferedReader:readLine	()Ljava/lang/String;
      //   98: areturn
    }
    
    /* Error */
    private void updatePredecessors(int arg1)
    {
      // Byte code:
      //   0: getstatic 47	cdht/cdht:firstPredecessor	I
      //   3: ifne +44 -> 47
      //   6: getstatic 48	cdht/cdht:secondPredecessor	I
      //   9: nop
      //   10: iload_1
      //   11: iconst_1
      //   12: isub
      //   13: istore_2
      //   14: iload_1
      //   15: iload_2
      //   16: imul
      //   17: istore_2
      //   18: iload_2
      //   19: iconst_2
      //   20: irem
      //   21: istore_2
      //   22: iload_2
      //   23: iconst_0
      //   24: isub
      //   25: nop
      //   26: ifeq +13 -> 39
      //   29: iload_1
      //   30: iconst_3
      //   31: isub
      //   32: istore_1
      //   33: ifne +14 -> 47
      //   36: goto +6 -> 42
      //   39: ifne +8 -> 47
      //   42: iload_1
      //   43: putstatic 47	cdht/cdht:firstPredecessor	I
      //   46: return
      //   47: getstatic 48	cdht/cdht:secondPredecessor	I
      //   50: ifne +58 -> 108
      //   53: getstatic 47	cdht/cdht:firstPredecessor	I
      //   56: ifeq +52 -> 108
      //   59: getstatic 49	cdht/cdht:peerID	I
      //   62: getstatic 47	cdht/cdht:firstPredecessor	I
      //   65: isub
      //   66: sipush 256
      //   69: iadd
      //   70: sipush 256
      //   73: irem
      //   74: getstatic 49	cdht/cdht:peerID	I
      //   77: iload_1
      //   78: isub
      //   79: sipush 256
      //   82: iadd
      //   83: sipush 256
      //   86: irem
      //   87: if_icmpge +10 -> 97
      //   90: iload_1
      //   91: putstatic 48	cdht/cdht:secondPredecessor	I
      //   94: goto +13 -> 107
      //   97: getstatic 47	cdht/cdht:firstPredecessor	I
      //   100: putstatic 48	cdht/cdht:secondPredecessor	I
      //   103: iload_1
      //   104: putstatic 47	cdht/cdht:firstPredecessor	I
      //   107: return
      //   108: iload_1
      //   109: getstatic 47	cdht/cdht:firstPredecessor	I
      //   112: if_icmpeq +18 -> 130
      //   115: iload_1
      //   116: getstatic 48	cdht/cdht:secondPredecessor	I
      //   119: if_icmpeq +11 -> 130
      //   122: iload_1
      //   123: putstatic 47	cdht/cdht:firstPredecessor	I
      //   126: iconst_0
      //   127: putstatic 48	cdht/cdht:secondPredecessor	I
      //   130: return
    }
  }
  
  static class TCPServerThread
    extends Thread
  {
    /* Error */
    public void run()
    {
      // Byte code:
      //   0: aconst_null
      //   1: astore_1
      //   2: new 3	java/net/ServerSocket
      //   5: dup
      //   6: ldc 4
      //   8: getstatic 5	cdht/cdht:peerID	I
      //   11: iadd
      //   12: invokespecial 6	java/net/ServerSocket:<init>	(I)V
      //   15: astore_1
      //   16: goto +11 -> 27
      //   19: astore_2
      //   20: getstatic 8	java/lang/System:out	Ljava/io/PrintStream;
      //   23: aload_2
      //   24: invokevirtual 9	java/io/PrintStream:println	(Ljava/lang/Object;)V
      //   27: aload_1
      //   28: invokevirtual 10	java/net/ServerSocket:accept	()Ljava/net/Socket;
      //   31: astore_2
      //   32: new 11	java/io/BufferedReader
      //   35: dup
      //   36: new 12	java/io/InputStreamReader
      //   39: dup
      //   40: aload_2
      //   41: invokevirtual 13	java/net/Socket:getInputStream	()Ljava/io/InputStream;
      //   44: invokespecial 14	java/io/InputStreamReader:<init>	(Ljava/io/InputStream;)V
      //   47: invokespecial 15	java/io/BufferedReader:<init>	(Ljava/io/Reader;)V
      //   50: astore_3
      //   51: aload_3
      //   52: invokevirtual 16	java/io/BufferedReader:readLine	()Ljava/lang/String;
      //   55: astore 4
      //   57: aload 4
      //   59: ldc 17
      //   61: invokevirtual 18	java/lang/String:split	(Ljava/lang/String;)[Ljava/lang/String;
      //   64: astore 5
      //   66: aload 5
      //   68: iconst_0
      //   69: aaload
      //   70: ldc 19
      //   72: invokevirtual 20	java/lang/String:equals	(Ljava/lang/Object;)Z
      //   75: ifeq +51 -> 126
      //   78: getstatic 8	java/lang/System:out	Ljava/io/PrintStream;
      //   81: new 21	java/lang/StringBuilder
      //   84: dup
      //   85: invokespecial 22	java/lang/StringBuilder:<init>	()V
      //   88: ldc 23
      //   90: invokevirtual 24	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   93: aload 5
      //   95: iconst_2
      //   96: aaload
      //   97: invokevirtual 24	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   100: ldc 25
      //   102: invokevirtual 24	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   105: aload 5
      //   107: iconst_1
      //   108: aaload
      //   109: invokevirtual 24	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   112: ldc 26
      //   114: invokevirtual 24	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   117: invokevirtual 27	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   120: invokevirtual 28	java/io/PrintStream:println	(Ljava/lang/String;)V
      //   123: goto +987 -> 1110
      //   126: aload 5
      //   128: iconst_0
      //   129: aaload
      //   130: ldc 29
      //   132: invokevirtual 20	java/lang/String:equals	(Ljava/lang/Object;)Z
      //   135: ifeq +61 -> 196
      //   138: new 30	java/io/DataOutputStream
      //   141: dup
      //   142: aload_2
      //   143: invokevirtual 31	java/net/Socket:getOutputStream	()Ljava/io/OutputStream;
      //   146: invokespecial 32	java/io/DataOutputStream:<init>	(Ljava/io/OutputStream;)V
      //   149: astore 6
      //   151: aload 6
      //   153: new 21	java/lang/StringBuilder
      //   156: dup
      //   157: invokespecial 22	java/lang/StringBuilder:<init>	()V
      //   160: ldc 33
      //   162: invokevirtual 24	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   165: getstatic 34	cdht/cdht:firstPeer	I
      //   168: invokevirtual 35	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
      //   171: ldc 17
      //   173: invokevirtual 24	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   176: getstatic 36	cdht/cdht:secondPeer	I
      //   179: invokevirtual 35	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
      //   182: ldc 37
      //   184: invokevirtual 24	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   187: invokevirtual 27	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   190: invokevirtual 38	java/io/DataOutputStream:writeBytes	(Ljava/lang/String;)V
      //   193: goto +917 -> 1110
      //   196: aload 5
      //   198: iconst_0
      //   199: aaload
      //   200: ldc 39
      //   202: invokevirtual 20	java/lang/String:equals	(Ljava/lang/Object;)Z
      //   205: ifeq +462 -> 667
      //   208: aload 5
      //   210: iconst_3
      //   211: aaload
      //   212: invokestatic 40	java/lang/Integer:parseInt	(Ljava/lang/String;)I
      //   215: istore 6
      //   217: getstatic 8	java/lang/System:out	Ljava/io/PrintStream;
      //   220: new 21	java/lang/StringBuilder
      //   223: dup
      //   224: invokespecial 22	java/lang/StringBuilder:<init>	()V
      //   227: ldc 41
      //   229: invokevirtual 24	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   232: iload 6
      //   234: invokevirtual 35	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
      //   237: ldc 42
      //   239: invokevirtual 24	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   242: invokevirtual 27	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   245: invokevirtual 28	java/io/PrintStream:println	(Ljava/lang/String;)V
      //   248: new 30	java/io/DataOutputStream
      //   251: dup
      //   252: aload_2
      //   253: invokevirtual 31	java/net/Socket:getOutputStream	()Ljava/io/OutputStream;
      //   256: invokespecial 32	java/io/DataOutputStream:<init>	(Ljava/io/OutputStream;)V
      //   259: astore 7
      //   261: aload 7
      //   263: ldc 43
      //   265: invokevirtual 38	java/io/DataOutputStream:writeBytes	(Ljava/lang/String;)V
      //   268: iload 6
      //   270: getstatic 36	cdht/cdht:secondPeer	I
      //   273: if_icmpne +154 -> 427
      //   276: iload 6
      //   278: getstatic 44	cdht/cdht:secondPredecessor	I
      //   281: if_icmpne +146 -> 427
      //   284: getstatic 45	cdht/cdht:firstPredecessor	I
      //   287: putstatic 36	cdht/cdht:secondPeer	I
      //   290: getstatic 34	cdht/cdht:firstPeer	I
      //   293: putstatic 44	cdht/cdht:secondPredecessor	I
      //   296: getstatic 8	java/lang/System:out	Ljava/io/PrintStream;
      //   299: new 21	java/lang/StringBuilder
      //   302: dup
      //   303: invokespecial 22	java/lang/StringBuilder:<init>	()V
      //   306: ldc 46
      //   308: invokevirtual 24	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   311: getstatic 34	cdht/cdht:firstPeer	I
      //   314: invokevirtual 35	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
      //   317: ldc 26
      //   319: invokevirtual 24	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   322: invokevirtual 27	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   325: invokevirtual 28	java/io/PrintStream:println	(Ljava/lang/String;)V
      //   328: getstatic 8	java/lang/System:out	Ljava/io/PrintStream;
      //   331: new 21	java/lang/StringBuilder
      //   334: dup
      //   335: invokespecial 22	java/lang/StringBuilder:<init>	()V
      //   338: ldc 47
      //   340: invokevirtual 24	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   343: getstatic 36	cdht/cdht:secondPeer	I
      //   346: invokevirtual 35	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
      //   349: ldc 26
      //   351: invokevirtual 24	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   354: invokevirtual 27	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   357: invokevirtual 28	java/io/PrintStream:println	(Ljava/lang/String;)V
      //   360: getstatic 8	java/lang/System:out	Ljava/io/PrintStream;
      //   363: new 21	java/lang/StringBuilder
      //   366: dup
      //   367: invokespecial 22	java/lang/StringBuilder:<init>	()V
      //   370: ldc 48
      //   372: invokevirtual 24	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   375: getstatic 45	cdht/cdht:firstPredecessor	I
      //   378: invokevirtual 35	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
      //   381: ldc 26
      //   383: invokevirtual 24	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   386: invokevirtual 27	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   389: invokevirtual 28	java/io/PrintStream:println	(Ljava/lang/String;)V
      //   392: getstatic 8	java/lang/System:out	Ljava/io/PrintStream;
      //   395: new 21	java/lang/StringBuilder
      //   398: dup
      //   399: invokespecial 22	java/lang/StringBuilder:<init>	()V
      //   402: ldc 49
      //   404: invokevirtual 24	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   407: getstatic 44	cdht/cdht:secondPredecessor	I
      //   410: invokevirtual 35	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
      //   413: ldc 26
      //   415: invokevirtual 24	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   418: invokevirtual 27	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   421: invokevirtual 28	java/io/PrintStream:println	(Ljava/lang/String;)V
      //   424: goto +240 -> 664
      //   427: iload 6
      //   429: getstatic 34	cdht/cdht:firstPeer	I
      //   432: if_icmpeq +11 -> 443
      //   435: iload 6
      //   437: getstatic 36	cdht/cdht:secondPeer	I
      //   440: if_icmpne +107 -> 547
      //   443: iload 6
      //   445: getstatic 34	cdht/cdht:firstPeer	I
      //   448: if_icmpne +22 -> 470
      //   451: getstatic 36	cdht/cdht:secondPeer	I
      //   454: putstatic 34	cdht/cdht:firstPeer	I
      //   457: aload 5
      //   459: iconst_2
      //   460: aaload
      //   461: invokestatic 40	java/lang/Integer:parseInt	(Ljava/lang/String;)I
      //   464: putstatic 36	cdht/cdht:secondPeer	I
      //   467: goto +13 -> 480
      //   470: aload 5
      //   472: iconst_1
      //   473: aaload
      //   474: invokestatic 40	java/lang/Integer:parseInt	(Ljava/lang/String;)I
      //   477: putstatic 36	cdht/cdht:secondPeer	I
      //   480: getstatic 8	java/lang/System:out	Ljava/io/PrintStream;
      //   483: new 21	java/lang/StringBuilder
      //   486: dup
      //   487: invokespecial 22	java/lang/StringBuilder:<init>	()V
      //   490: ldc 46
      //   492: invokevirtual 24	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   495: getstatic 34	cdht/cdht:firstPeer	I
      //   498: invokevirtual 35	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
      //   501: ldc 26
      //   503: invokevirtual 24	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   506: invokevirtual 27	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   509: invokevirtual 28	java/io/PrintStream:println	(Ljava/lang/String;)V
      //   512: getstatic 8	java/lang/System:out	Ljava/io/PrintStream;
      //   515: new 21	java/lang/StringBuilder
      //   518: dup
      //   519: invokespecial 22	java/lang/StringBuilder:<init>	()V
      //   522: ldc 47
      //   524: invokevirtual 24	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   527: getstatic 36	cdht/cdht:secondPeer	I
      //   530: invokevirtual 35	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
      //   533: ldc 26
      //   535: invokevirtual 24	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   538: invokevirtual 27	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   541: invokevirtual 28	java/io/PrintStream:println	(Ljava/lang/String;)V
      //   544: goto +120 -> 664
      //   547: iload 6
      //   549: getstatic 45	cdht/cdht:firstPredecessor	I
      //   552: if_icmpeq +11 -> 563
      //   555: iload 6
      //   557: getstatic 44	cdht/cdht:secondPredecessor	I
      //   560: if_icmpne +104 -> 664
      //   563: iload 6
      //   565: getstatic 45	cdht/cdht:firstPredecessor	I
      //   568: if_icmpne +22 -> 590
      //   571: getstatic 44	cdht/cdht:secondPredecessor	I
      //   574: putstatic 45	cdht/cdht:firstPredecessor	I
      //   577: aload 5
      //   579: iconst_2
      //   580: aaload
      //   581: invokestatic 40	java/lang/Integer:parseInt	(Ljava/lang/String;)I
      //   584: putstatic 44	cdht/cdht:secondPredecessor	I
      //   587: goto +13 -> 600
      //   590: aload 5
      //   592: iconst_1
      //   593: aaload
      //   594: invokestatic 40	java/lang/Integer:parseInt	(Ljava/lang/String;)I
      //   597: putstatic 44	cdht/cdht:secondPredecessor	I
      //   600: getstatic 8	java/lang/System:out	Ljava/io/PrintStream;
      //   603: new 21	java/lang/StringBuilder
      //   606: dup
      //   607: invokespecial 22	java/lang/StringBuilder:<init>	()V
      //   610: ldc 48
      //   612: invokevirtual 24	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   615: getstatic 45	cdht/cdht:firstPredecessor	I
      //   618: invokevirtual 35	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
      //   621: ldc 26
      //   623: invokevirtual 24	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   626: invokevirtual 27	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   629: invokevirtual 28	java/io/PrintStream:println	(Ljava/lang/String;)V
      //   632: getstatic 8	java/lang/System:out	Ljava/io/PrintStream;
      //   635: new 21	java/lang/StringBuilder
      //   638: dup
      //   639: invokespecial 22	java/lang/StringBuilder:<init>	()V
      //   642: ldc 49
      //   644: invokevirtual 24	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   647: getstatic 44	cdht/cdht:secondPredecessor	I
      //   650: invokevirtual 35	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
      //   653: ldc 26
      //   655: invokevirtual 24	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   658: invokevirtual 27	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   661: invokevirtual 28	java/io/PrintStream:println	(Ljava/lang/String;)V
      //   664: goto +446 -> 1110
      //   667: aload 5
      //   669: iconst_0
      //   670: aaload
      //   671: ldc 50
      //   673: invokevirtual 20	java/lang/String:equals	(Ljava/lang/Object;)Z
      //   676: ifeq +434 -> 1110
      //   679: aload 5
      //   681: iconst_1
      //   682: aaload
      //   683: astore 6
      //   685: aload 5
      //   687: iconst_2
      //   688: aaload
      //   689: astore 7
      //   691: aload 5
      //   693: iconst_3
      //   694: aaload
      //   695: astore 8
      //   697: aload 6
      //   699: invokestatic 40	java/lang/Integer:parseInt	(Ljava/lang/String;)I
      //   702: sipush 256
      //   705: irem
      //   706: istore 9
      //   708: getstatic 5	cdht/cdht:peerID	I
      //   711: aload 7
      //   713: invokestatic 40	java/lang/Integer:parseInt	(Ljava/lang/String;)I
      //   716: isub
      //   717: sipush 256
      //   720: iadd
      //   721: sipush 256
      //   724: irem
      //   725: getstatic 5	cdht/cdht:peerID	I
      //   728: iload 9
      //   730: isub
      //   731: sipush 256
      //   734: iadd
      //   735: sipush 256
      //   738: irem
      //   739: if_icmple +217 -> 956
      //   742: getstatic 8	java/lang/System:out	Ljava/io/PrintStream;
      //   745: new 21	java/lang/StringBuilder
      //   748: dup
      //   749: invokespecial 22	java/lang/StringBuilder:<init>	()V
      //   752: ldc 51
      //   754: invokevirtual 24	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   757: aload 6
      //   759: invokevirtual 24	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   762: ldc 52
      //   764: invokevirtual 24	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   767: invokevirtual 27	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   770: invokevirtual 28	java/io/PrintStream:println	(Ljava/lang/String;)V
      //   773: getstatic 8	java/lang/System:out	Ljava/io/PrintStream;
      //   776: new 21	java/lang/StringBuilder
      //   779: dup
      //   780: invokespecial 22	java/lang/StringBuilder:<init>	()V
      //   783: ldc 53
      //   785: invokevirtual 24	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   788: aload 8
      //   790: invokevirtual 24	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   793: ldc 54
      //   795: invokevirtual 24	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   798: invokevirtual 27	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   801: invokevirtual 28	java/io/PrintStream:println	(Ljava/lang/String;)V
      //   804: new 55	java/net/Socket
      //   807: dup
      //   808: ldc 56
      //   810: invokestatic 57	java/net/InetAddress:getByName	(Ljava/lang/String;)Ljava/net/InetAddress;
      //   813: ldc 4
      //   815: aload 8
      //   817: invokestatic 40	java/lang/Integer:parseInt	(Ljava/lang/String;)I
      //   820: iadd
      //   821: invokespecial 58	java/net/Socket:<init>	(Ljava/net/InetAddress;I)V
      //   824: astore 10
      //   826: new 30	java/io/DataOutputStream
      //   829: dup
      //   830: aload 10
      //   832: invokevirtual 31	java/net/Socket:getOutputStream	()Ljava/io/OutputStream;
      //   835: invokespecial 32	java/io/DataOutputStream:<init>	(Ljava/io/OutputStream;)V
      //   838: astore 11
      //   840: new 21	java/lang/StringBuilder
      //   843: iconst_0
      //   844: istore 13
      //   846: nop
      //   847: iload 13
      //   849: iconst_1
      //   850: isub
      //   851: istore 14
      //   853: iload 13
      //   855: iload 14
      //   857: imul
      //   858: istore 14
      //   860: iload 14
      //   862: iconst_2
      //   863: irem
      //   864: istore 14
      //   866: iload 14
      //   868: iconst_0
      //   869: isub
      //   870: nop
      //   871: ifeq +22 -> 893
      //   874: iload 13
      //   876: iconst_1
      //   877: isub
      //   878: istore 13
      //   880: dup
      //   881: iload 13
      //   883: iconst_5
      //   884: idiv
      //   885: istore 13
      //   887: invokespecial 22	java/lang/StringBuilder:<init>	()V
      //   890: goto +7 -> 897
      //   893: dup
      //   894: invokespecial 22	java/lang/StringBuilder:<init>	()V
      //   897: ldc 59
      //   899: invokevirtual 24	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   902: aload 6
      //   904: invokevirtual 24	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   907: ldc 17
      //   909: invokevirtual 24	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   912: getstatic 5	cdht/cdht:peerID	I
      //   915: invokevirtual 35	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
      //   918: invokevirtual 27	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   921: astore 12
      //   923: aload 11
      //   925: new 21	java/lang/StringBuilder
      //   928: dup
      //   929: invokespecial 22	java/lang/StringBuilder:<init>	()V
      //   932: aload 12
      //   934: invokevirtual 24	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   937: ldc 37
      //   939: invokevirtual 24	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   942: invokevirtual 27	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   945: invokevirtual 38	java/io/DataOutputStream:writeBytes	(Ljava/lang/String;)V
      //   948: aload 10
      //   950: invokevirtual 60	java/net/Socket:close	()V
      //   953: goto +157 -> 1110
      //   956: new 55	java/net/Socket
      //   959: dup
      //   960: ldc 56
      //   962: invokestatic 57	java/net/InetAddress:getByName	(Ljava/lang/String;)Ljava/net/InetAddress;
      //   965: ldc 4
      //   967: getstatic 34	cdht/cdht:firstPeer	I
      //   970: iadd
      //   971: invokespecial 58	java/net/Socket:<init>	(Ljava/net/InetAddress;I)V
      //   974: astore 10
      //   976: goto +25 -> 1001
      //   979: astore 11
      //   981: new 55	java/net/Socket
      //   984: dup
      //   985: ldc 56
      //   987: invokestatic 57	java/net/InetAddress:getByName	(Ljava/lang/String;)Ljava/net/InetAddress;
      //   990: ldc 4
      //   992: getstatic 36	cdht/cdht:secondPeer	I
      //   995: iadd
      //   996: invokespecial 58	java/net/Socket:<init>	(Ljava/net/InetAddress;I)V
      //   999: astore 10
      //   1001: new 30	java/io/DataOutputStream
      //   1004: dup
      //   1005: aload 10
      //   1007: invokevirtual 31	java/net/Socket:getOutputStream	()Ljava/io/OutputStream;
      //   1010: invokespecial 32	java/io/DataOutputStream:<init>	(Ljava/io/OutputStream;)V
      //   1013: astore 11
      //   1015: aload 11
      //   1017: new 21	java/lang/StringBuilder
      //   1020: dup
      //   1021: invokespecial 22	java/lang/StringBuilder:<init>	()V
      //   1024: ldc 62
      //   1026: invokevirtual 24	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   1029: aload 6
      //   1031: invokevirtual 24	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   1034: ldc 17
      //   1036: invokevirtual 24	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   1039: getstatic 5	cdht/cdht:peerID	I
      //   1042: invokevirtual 35	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
      //   1045: ldc 17
      //   1047: invokevirtual 24	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   1050: aload 8
      //   1052: invokevirtual 24	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   1055: ldc 37
      //   1057: invokevirtual 24	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   1060: invokevirtual 27	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   1063: invokevirtual 38	java/io/DataOutputStream:writeBytes	(Ljava/lang/String;)V
      //   1066: getstatic 8	java/lang/System:out	Ljava/io/PrintStream;
      //   1069: new 21	java/lang/StringBuilder
      //   1072: dup
      //   1073: invokespecial 22	java/lang/StringBuilder:<init>	()V
      //   1076: ldc 51
      //   1078: invokevirtual 24	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   1081: aload 6
      //   1083: invokevirtual 24	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   1086: ldc 63
      //   1088: invokevirtual 24	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   1091: invokevirtual 27	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   1094: invokevirtual 28	java/io/PrintStream:println	(Ljava/lang/String;)V
      //   1097: getstatic 8	java/lang/System:out	Ljava/io/PrintStream;
      //   1100: ldc 64
      //   1102: invokevirtual 28	java/io/PrintStream:println	(Ljava/lang/String;)V
      //   1105: aload 10
      //   1107: invokevirtual 60	java/net/Socket:close	()V
      //   1110: aload_2
      //   1111: invokevirtual 60	java/net/Socket:close	()V
      //   1114: goto -1087 -> 27
      //   1117: astore_2
      //   1118: getstatic 8	java/lang/System:out	Ljava/io/PrintStream;
      //   1121: aload_2
      //   1122: invokevirtual 9	java/io/PrintStream:println	(Ljava/lang/Object;)V
      //   1125: goto -1098 -> 27
      // Exception table:
      //   from	to	target	type
      //   2	16	19	java/io/IOException
      //   956	976	979	java/net/ConnectException
      //   27	1114	1117	java/io/IOException
    }
  }
}


/* Location:              D:\COMP3331\Normal\cdht_obf.jar!\cdht\cdht.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */