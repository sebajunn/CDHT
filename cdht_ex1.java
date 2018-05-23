import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class cdht_ex
{
  static int base;
  static int peerIdentity;
  static int firstSuccessor;
  static int secondSuccessor;
  static int peerPort;
  static int firstSuccessorPort;
  static int secondSuccessorPort;
  static int count;
  static boolean firstPingBack;
  static boolean secondPingBack;
  static boolean userInput;
  static boolean requestFileMessage;
  static DatagramSocket UDPSocket;
  
  /* Error */
  public static DatagramPacket UDPClient(int destinationPort)
    throws Exception
  {
    // Byte code:
    //   0: ldc 2
    //   2: astore_1
    //   3: ldc 3
    //   5: invokestatic 4	java/net/InetAddress:getByName	(Ljava/lang/String;)Ljava/net/InetAddress;
    //   8: astore_2
    //   9: aload_1
    //   10: invokevirtual 5	java/lang/String:getBytes	()[B
    //   13: astore_3
    //   14: new 6	java/net/DatagramPacket
    //   17: dup
    //   18: aload_3
    //   19: aload_3
    //   20: arraylength
    //   21: aload_2
    //   22: iload_0
    //   23: invokespecial 7	java/net/DatagramPacket:<init>	([BILjava/net/InetAddress;I)V
    //   26: astore 4
    //   28: aload 4
    //   30: areturn
    //   31: ret 3
    //   33: iadd
    //   34: ret 3
    //   36: swap
    //   37: iload 4
    //   39: istore_2
    //   40: dup
    // Line number table:
    //   Java source line #24	-> byte code offset #0
    //   Java source line #25	-> byte code offset #3
    //   Java source line #26	-> byte code offset #9
    //   Java source line #27	-> byte code offset #14
    //   Java source line #28	-> byte code offset #28
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	31	0	destinationPort	int
    //   3	28	1	message	String
    //   9	22	2	IPAddress	java.net.InetAddress
    //   14	17	3	sendData	byte[]
    //   28	3	4	sendPacket	DatagramPacket
  }
  
  /* Error */
  public static void UDPServer()
    throws Exception
  {
    // Byte code:
    //   0: iconst_0
    //   1: istore_0
    //   2: iconst_0
    //   3: istore_1
    //   4: ldc 8
    //   6: astore_2
    //   7: sipush 1024
    //   10: newarray <illegal type>
    //   12: astore_3
    //   13: aload_2
    //   14: invokevirtual 5	java/lang/String:getBytes	()[B
    //   17: astore 4
    //   19: new 6	java/net/DatagramPacket
    //   22: dup
    //   23: aload_3
    //   24: aload_3
    //   25: arraylength
    //   26: invokespecial 9	java/net/DatagramPacket:<init>	([BI)V
    //   29: astore 5
    //   31: getstatic 10	cdht_ex:UDPSocket	Ljava/net/DatagramSocket;
    //   34: aload 5
    //   36: invokevirtual 11	java/net/DatagramSocket:receive	(Ljava/net/DatagramPacket;)V
    //   39: aload 5
    //   41: invokevirtual 12	java/net/DatagramPacket:getPort	()I
    //   44: istore 6
    //   46: aload 5
    //   48: invokevirtual 13	java/net/DatagramPacket:getAddress	()Ljava/net/InetAddress;
    //   51: astore 7
    //   53: new 6	java/net/DatagramPacket
    //   56: dup
    //   57: aload 4
    //   59: aload 4
    //   61: arraylength
    //   62: aload 7
    //   64: iload 6
    //   66: invokespecial 7	java/net/DatagramPacket:<init>	([BILjava/net/InetAddress;I)V
    //   69: astore 8
    //   71: getstatic 14	cdht_ex:userInput	Z
    //   74: ifeq +9 -> 83
    //   77: iload_0
    //   78: ifne +5 -> 83
    //   81: iconst_1
    //   82: istore_0
    //   83: getstatic 15	cdht_ex:requestFileMessage	Z
    //   86: ifeq +17 -> 103
    //   89: iload_1
    //   90: ifne +13 -> 103
    //   93: getstatic 16	java/lang/System:out	Ljava/io/PrintStream;
    //   96: ldc 17
    //   98: invokevirtual 18	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   101: iconst_1
    //   102: istore_1
    //   103: iload 6
    //   105: getstatic 19	cdht_ex:firstSuccessorPort	I
    //   108: if_icmpeq +70 -> 178
    //   111: iload 6
    //   113: getstatic 20	cdht_ex:secondSuccessorPort	I
    //   116: if_icmpeq +62 -> 178
    //   119: getstatic 10	cdht_ex:UDPSocket	Ljava/net/DatagramSocket;
    //   122: aload 8
    //   124: invokevirtual 21	java/net/DatagramSocket:send	(Ljava/net/DatagramPacket;)V
    //   127: iload 6
    //   129: getstatic 22	cdht_ex:base	I
    //   132: isub
    //   133: istore 9
    //   135: getstatic 14	cdht_ex:userInput	Z
    //   138: ifne +40 -> 178
    //   141: getstatic 15	cdht_ex:requestFileMessage	Z
    //   144: ifne +34 -> 178
    //   147: getstatic 16	java/lang/System:out	Ljava/io/PrintStream;
    //   150: new 23	java/lang/StringBuilder
    //   153: dup
    //   154: invokespecial 24	java/lang/StringBuilder:<init>	()V
    //   157: ldc 25
    //   159: invokevirtual 26	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   162: iload 9
    //   164: invokevirtual 27	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   167: ldc 28
    //   169: invokevirtual 26	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   172: invokevirtual 29	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   175: invokevirtual 18	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   178: iload 6
    //   180: getstatic 19	cdht_ex:firstSuccessorPort	I
    //   183: if_icmpne +51 -> 234
    //   186: getstatic 14	cdht_ex:userInput	Z
    //   189: ifne +41 -> 230
    //   192: getstatic 15	cdht_ex:requestFileMessage	Z
    //   195: ifne +35 -> 230
    //   198: getstatic 16	java/lang/System:out	Ljava/io/PrintStream;
    //   201: new 23	java/lang/StringBuilder
    //   204: dup
    //   205: invokespecial 24	java/lang/StringBuilder:<init>	()V
    //   208: ldc 30
    //   210: invokevirtual 26	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   213: getstatic 31	cdht_ex:firstSuccessor	I
    //   216: invokevirtual 27	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   219: ldc 28
    //   221: invokevirtual 26	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   224: invokevirtual 29	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   227: invokevirtual 18	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   230: iconst_1
    //   231: putstatic 32	cdht_ex:firstPingBack	Z
    //   234: iload 6
    //   236: getstatic 20	cdht_ex:secondSuccessorPort	I
    //   239: if_icmpne +51 -> 290
    //   242: getstatic 14	cdht_ex:userInput	Z
    //   245: ifne +41 -> 286
    //   248: getstatic 15	cdht_ex:requestFileMessage	Z
    //   251: ifne +35 -> 286
    //   254: getstatic 16	java/lang/System:out	Ljava/io/PrintStream;
    //   257: new 23	java/lang/StringBuilder
    //   260: dup
    //   261: invokespecial 24	java/lang/StringBuilder:<init>	()V
    //   264: ldc 30
    //   266: invokevirtual 26	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   269: getstatic 33	cdht_ex:secondSuccessor	I
    //   272: invokevirtual 27	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   275: ldc 28
    //   277: invokevirtual 26	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   280: invokevirtual 29	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   283: invokevirtual 18	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   286: iconst_1
    //   287: putstatic 34	cdht_ex:secondPingBack	Z
    //   290: goto -259 -> 31
    //   293: iadd
    //   294: goto -294 -> 0
    //   297: istore_1
    //   298: bipush 54
    //   300: goto -300 -> 0
    //   303: swap
    // Line number table:
    //   Java source line #33	-> byte code offset #0
    //   Java source line #34	-> byte code offset #2
    //   Java source line #36	-> byte code offset #4
    //   Java source line #37	-> byte code offset #7
    //   Java source line #38	-> byte code offset #13
    //   Java source line #39	-> byte code offset #19
    //   Java source line #41	-> byte code offset #31
    //   Java source line #42	-> byte code offset #39
    //   Java source line #43	-> byte code offset #46
    //   Java source line #44	-> byte code offset #53
    //   Java source line #46	-> byte code offset #71
    //   Java source line #47	-> byte code offset #83
    //   Java source line #48	-> byte code offset #93
    //   Java source line #49	-> byte code offset #101
    //   Java source line #51	-> byte code offset #103
    //   Java source line #54	-> byte code offset #119
    //   Java source line #55	-> byte code offset #127
    //   Java source line #56	-> byte code offset #135
    //   Java source line #57	-> byte code offset #147
    //   Java source line #59	-> byte code offset #178
    //   Java source line #60	-> byte code offset #186
    //   Java source line #61	-> byte code offset #198
    //   Java source line #62	-> byte code offset #230
    //   Java source line #64	-> byte code offset #234
    //   Java source line #65	-> byte code offset #242
    //   Java source line #66	-> byte code offset #254
    //   Java source line #67	-> byte code offset #286
    //   Java source line #69	-> byte code offset #290
    // Local variable table:
    //   start	length	slot	name	signature
    //   2	291	0	haveShownHideMessage	boolean
    //   4	289	1	haveShownHideRequestMessage	boolean
    //   7	286	2	message	String
    //   13	280	3	receiveData	byte[]
    //   19	274	4	sendData	byte[]
    //   31	262	5	receivePacket	DatagramPacket
    //   46	247	6	port	int
    //   53	240	7	IPAdress	java.net.InetAddress
    //   71	222	8	sendPacket	DatagramPacket
    //   135	45	9	peer	int
  }
  
  /* Error */
  public static int stringToHash(String fileName)
  {
    // Byte code:
    //   0: aload_0
    //   1: iconst_0
    //   2: invokevirtual 35	java/lang/String:charAt	(I)C
    //   5: invokestatic 36	java/lang/String:valueOf	(C)Ljava/lang/String;
    //   8: invokestatic 37	java/lang/Integer:parseInt	(Ljava/lang/String;)I
    //   11: istore_1
    //   12: aload_0
    //   13: iconst_1
    //   14: invokevirtual 35	java/lang/String:charAt	(I)C
    //   17: invokestatic 36	java/lang/String:valueOf	(C)Ljava/lang/String;
    //   20: invokestatic 37	java/lang/Integer:parseInt	(Ljava/lang/String;)I
    //   23: istore_2
    //   24: aload_0
    //   25: iconst_2
    //   26: invokevirtual 35	java/lang/String:charAt	(I)C
    //   29: invokestatic 36	java/lang/String:valueOf	(C)Ljava/lang/String;
    //   32: invokestatic 37	java/lang/Integer:parseInt	(Ljava/lang/String;)I
    //   35: istore_3
    //   36: aload_0
    //   37: iconst_3
    //   38: invokevirtual 35	java/lang/String:charAt	(I)C
    //   41: invokestatic 36	java/lang/String:valueOf	(C)Ljava/lang/String;
    //   44: invokestatic 37	java/lang/Integer:parseInt	(Ljava/lang/String;)I
    //   47: istore 4
    //   49: iload_1
    //   50: sipush 1000
    //   53: imul
    //   54: iload_2
    //   55: bipush 100
    //   57: imul
    //   58: iadd
    //   59: iload_3
    //   60: bipush 10
    //   62: imul
    //   63: iadd
    //   64: iload 4
    //   66: iadd
    //   67: iconst_1
    //   68: iadd
    //   69: sipush 256
    //   72: irem
    //   73: istore 5
    //   75: iload 5
    //   77: ireturn
    //   78: bipush 54
    //   80: iload_1
    //   81: imul
    //   82: swap
    //   83: iadd
    //   84: bipush 54
    //   86: iload_1
    //   87: istore 4
    // Line number table:
    //   Java source line #74	-> byte code offset #0
    //   Java source line #75	-> byte code offset #12
    //   Java source line #76	-> byte code offset #24
    //   Java source line #77	-> byte code offset #36
    //   Java source line #78	-> byte code offset #49
    //   Java source line #79	-> byte code offset #75
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	78	0	fileName	String
    //   12	66	1	w	int
    //   24	54	2	x	int
    //   36	42	3	y	int
    //   49	29	4	z	int
    //   75	3	5	hash	int
  }
  
  /* Error */
  public static void TCPServer()
    throws Exception
  {
    // Byte code:
    //   0: new 38	java/net/ServerSocket
    //   3: dup
    //   4: getstatic 39	cdht_ex:peerPort	I
    //   7: invokespecial 40	java/net/ServerSocket:<init>	(I)V
    //   10: astore_2
    //   11: aload_2
    //   12: invokevirtual 41	java/net/ServerSocket:accept	()Ljava/net/Socket;
    //   15: astore_3
    //   16: new 42	java/io/BufferedReader
    //   19: dup
    //   20: new 43	java/io/InputStreamReader
    //   23: dup
    //   24: aload_3
    //   25: invokevirtual 44	java/net/Socket:getInputStream	()Ljava/io/InputStream;
    //   28: invokespecial 45	java/io/InputStreamReader:<init>	(Ljava/io/InputStream;)V
    //   31: invokespecial 46	java/io/BufferedReader:<init>	(Ljava/io/Reader;)V
    //   34: astore 4
    //   36: aload 4
    //   38: invokevirtual 47	java/io/BufferedReader:readLine	()Ljava/lang/String;
    //   41: astore_1
    //   42: aload_1
    //   43: ldc 48
    //   45: invokevirtual 49	java/lang/String:split	(Ljava/lang/String;)[Ljava/lang/String;
    //   48: iconst_0
    //   49: aaload
    //   50: astore 5
    //   52: aload 5
    //   54: ldc 50
    //   56: invokevirtual 51	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   59: ifeq +405 -> 464
    //   62: aload_1
    //   63: ldc 48
    //   65: invokevirtual 49	java/lang/String:split	(Ljava/lang/String;)[Ljava/lang/String;
    //   68: iconst_1
    //   69: aaload
    //   70: ldc 52
    //   72: invokevirtual 51	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   75: ifeq +27 -> 102
    //   78: getstatic 53	cdht_ex:count	I
    //   81: iconst_1
    //   82: iadd
    //   83: putstatic 53	cdht_ex:count	I
    //   86: getstatic 53	cdht_ex:count	I
    //   89: iconst_2
    //   90: if_icmpne +1060 -> 1150
    //   93: getstatic 16	java/lang/System:out	Ljava/io/PrintStream;
    //   96: ldc 54
    //   98: invokevirtual 18	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   101: return
    //   102: iconst_0
    //   103: putstatic 14	cdht_ex:userInput	Z
    //   106: iconst_0
    //   107: putstatic 15	cdht_ex:requestFileMessage	Z
    //   110: aload_1
    //   111: ldc 48
    //   113: invokevirtual 49	java/lang/String:split	(Ljava/lang/String;)[Ljava/lang/String;
    //   116: iconst_1
    //   117: aaload
    //   118: invokestatic 37	java/lang/Integer:parseInt	(Ljava/lang/String;)I
    //   121: istore 6
    //   123: iload 6
    //   125: getstatic 31	cdht_ex:firstSuccessor	I
    //   128: if_icmpeq +39 -> 167
    //   131: iload 6
    //   133: getstatic 33	cdht_ex:secondSuccessor	I
    //   136: if_icmpeq +31 -> 167
    //   139: new 23	java/lang/StringBuilder
    //   142: dup
    //   143: invokespecial 24	java/lang/StringBuilder:<init>	()V
    //   146: aload_1
    //   147: invokevirtual 26	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   150: ldc 55
    //   152: invokevirtual 26	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   155: invokevirtual 29	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   158: getstatic 19	cdht_ex:firstSuccessorPort	I
    //   161: invokestatic 56	cdht_ex:TCPClient	(Ljava/lang/String;I)V
    //   164: goto +297 -> 461
    //   167: iload 6
    //   169: getstatic 31	cdht_ex:firstSuccessor	I
    //   172: if_icmpeq +28 -> 200
    //   175: new 23	java/lang/StringBuilder
    //   178: dup
    //   179: invokespecial 24	java/lang/StringBuilder:<init>	()V
    //   182: aload_1
    //   183: invokevirtual 26	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   186: ldc 55
    //   188: invokevirtual 26	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   191: invokevirtual 29	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   194: getstatic 19	cdht_ex:firstSuccessorPort	I
    //   197: invokestatic 56	cdht_ex:TCPClient	(Ljava/lang/String;I)V
    //   200: aload_1
    //   201: ldc 48
    //   203: invokevirtual 49	java/lang/String:split	(Ljava/lang/String;)[Ljava/lang/String;
    //   206: iconst_2
    //   207: aaload
    //   208: invokestatic 37	java/lang/Integer:parseInt	(Ljava/lang/String;)I
    //   211: istore 7
    //   213: aload_1
    //   214: ldc 48
    //   216: invokevirtual 49	java/lang/String:split	(Ljava/lang/String;)[Ljava/lang/String;
    //   219: iconst_3
    //   220: aaload
    //   221: invokestatic 37	java/lang/Integer:parseInt	(Ljava/lang/String;)I
    //   224: istore 8
    //   226: getstatic 31	cdht_ex:firstSuccessor	I
    //   229: iload 6
    //   231: if_icmpne +40 -> 271
    //   234: getstatic 33	cdht_ex:secondSuccessor	I
    //   237: iload 7
    //   239: if_icmpne +8 -> 247
    //   242: iload 8
    //   244: goto +5 -> 249
    //   247: iload 7
    //   249: putstatic 31	cdht_ex:firstSuccessor	I
    //   252: getstatic 31	cdht_ex:firstSuccessor	I
    //   255: istore 9
    //   257: getstatic 33	cdht_ex:secondSuccessor	I
    //   260: putstatic 31	cdht_ex:firstSuccessor	I
    //   263: iload 9
    //   265: putstatic 33	cdht_ex:secondSuccessor	I
    //   268: goto +21 -> 289
    //   271: getstatic 31	cdht_ex:firstSuccessor	I
    //   274: iload 7
    //   276: if_icmpne +8 -> 284
    //   279: iload 8
    //   281: goto +5 -> 286
    //   284: iload 7
    //   286: putstatic 33	cdht_ex:secondSuccessor	I
    //   289: getstatic 22	cdht_ex:base	I
    //   292: getstatic 31	cdht_ex:firstSuccessor	I
    //   295: iadd
    //   296: putstatic 19	cdht_ex:firstSuccessorPort	I
    //   299: getstatic 22	cdht_ex:base	I
    //   302: getstatic 33	cdht_ex:secondSuccessor	I
    //   305: iadd
    //   306: putstatic 20	cdht_ex:secondSuccessorPort	I
    //   309: new 23	java/lang/StringBuilder
    //   312: dup
    //   313: invokespecial 24	java/lang/StringBuilder:<init>	()V
    //   316: ldc 57
    //   318: invokevirtual 26	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   321: getstatic 58	cdht_ex:peerIdentity	I
    //   324: invokevirtual 27	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   327: ldc 55
    //   329: invokevirtual 26	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   332: invokevirtual 29	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   335: iload 6
    //   337: getstatic 22	cdht_ex:base	I
    //   340: iadd
    //   341: invokestatic 56	cdht_ex:TCPClient	(Ljava/lang/String;I)V
    //   344: getstatic 16	java/lang/System:out	Ljava/io/PrintStream;
    //   347: new 23	java/lang/StringBuilder
    //   350: dup
    //   351: invokespecial 24	java/lang/StringBuilder:<init>	()V
    //   354: iload 6
    //   356: invokevirtual 27	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   359: ldc 48
    //   361: invokevirtual 26	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   364: getstatic 58	cdht_ex:peerIdentity	I
    //   367: invokevirtual 27	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   370: invokevirtual 29	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   373: invokevirtual 18	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   376: getstatic 16	java/lang/System:out	Ljava/io/PrintStream;
    //   379: new 23	java/lang/StringBuilder
    //   382: dup
    //   383: invokespecial 24	java/lang/StringBuilder:<init>	()V
    //   386: ldc 59
    //   388: invokevirtual 26	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   391: iload 6
    //   393: invokevirtual 27	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   396: ldc 60
    //   398: invokevirtual 26	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   401: invokevirtual 29	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   404: invokevirtual 18	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   407: getstatic 16	java/lang/System:out	Ljava/io/PrintStream;
    //   410: new 23	java/lang/StringBuilder
    //   413: dup
    //   414: invokespecial 24	java/lang/StringBuilder:<init>	()V
    //   417: ldc 61
    //   419: invokevirtual 26	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   422: getstatic 31	cdht_ex:firstSuccessor	I
    //   425: invokevirtual 27	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   428: invokevirtual 29	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   431: invokevirtual 18	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   434: getstatic 16	java/lang/System:out	Ljava/io/PrintStream;
    //   437: new 23	java/lang/StringBuilder
    //   440: dup
    //   441: invokespecial 24	java/lang/StringBuilder:<init>	()V
    //   444: ldc 62
    //   446: invokevirtual 26	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   449: getstatic 33	cdht_ex:secondSuccessor	I
    //   452: invokevirtual 27	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   455: invokevirtual 29	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   458: invokevirtual 18	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   461: goto +689 -> 1150
    //   464: aload 5
    //   466: ldc 63
    //   468: invokevirtual 51	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   471: ifeq +108 -> 579
    //   474: iconst_0
    //   475: putstatic 15	cdht_ex:requestFileMessage	Z
    //   478: aload_1
    //   479: ldc 48
    //   481: invokevirtual 49	java/lang/String:split	(Ljava/lang/String;)[Ljava/lang/String;
    //   484: iconst_1
    //   485: aaload
    //   486: invokestatic 37	java/lang/Integer:parseInt	(Ljava/lang/String;)I
    //   489: istore 6
    //   491: aload_1
    //   492: ldc 48
    //   494: invokevirtual 49	java/lang/String:split	(Ljava/lang/String;)[Ljava/lang/String;
    //   497: iconst_2
    //   498: aaload
    //   499: astore 7
    //   501: aload 7
    //   503: ldc 64
    //   505: invokevirtual 51	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   508: ifeq +37 -> 545
    //   511: new 23	java/lang/StringBuilder
    //   514: dup
    //   515: invokespecial 24	java/lang/StringBuilder:<init>	()V
    //   518: ldc 65
    //   520: invokevirtual 26	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   523: getstatic 31	cdht_ex:firstSuccessor	I
    //   526: invokevirtual 27	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   529: ldc 55
    //   531: invokevirtual 26	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   534: invokevirtual 29	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   537: iload 6
    //   539: invokestatic 56	cdht_ex:TCPClient	(Ljava/lang/String;I)V
    //   542: goto +34 -> 576
    //   545: new 23	java/lang/StringBuilder
    //   548: dup
    //   549: invokespecial 24	java/lang/StringBuilder:<init>	()V
    //   552: ldc 66
    //   554: invokevirtual 26	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   557: getstatic 33	cdht_ex:secondSuccessor	I
    //   560: invokevirtual 27	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   563: ldc 55
    //   565: invokevirtual 26	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   568: invokevirtual 29	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   571: iload 6
    //   573: invokestatic 56	cdht_ex:TCPClient	(Ljava/lang/String;I)V
    //   576: goto +574 -> 1150
    //   579: aload 5
    //   581: ldc 67
    //   583: invokevirtual 51	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   586: ifeq +200 -> 786
    //   589: iconst_0
    //   590: putstatic 15	cdht_ex:requestFileMessage	Z
    //   593: aload_1
    //   594: ldc 48
    //   596: invokevirtual 49	java/lang/String:split	(Ljava/lang/String;)[Ljava/lang/String;
    //   599: iconst_1
    //   600: aaload
    //   601: astore 6
    //   603: aload_1
    //   604: ldc 48
    //   606: invokevirtual 49	java/lang/String:split	(Ljava/lang/String;)[Ljava/lang/String;
    //   609: iconst_2
    //   610: aaload
    //   611: invokestatic 37	java/lang/Integer:parseInt	(Ljava/lang/String;)I
    //   614: istore 7
    //   616: aload 6
    //   618: ldc 68
    //   620: invokevirtual 51	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   623: ifeq +91 -> 714
    //   626: getstatic 33	cdht_ex:secondSuccessor	I
    //   629: putstatic 31	cdht_ex:firstSuccessor	I
    //   632: iload 7
    //   634: putstatic 33	cdht_ex:secondSuccessor	I
    //   637: getstatic 31	cdht_ex:firstSuccessor	I
    //   640: getstatic 22	cdht_ex:base	I
    //   643: iadd
    //   644: putstatic 19	cdht_ex:firstSuccessorPort	I
    //   647: getstatic 33	cdht_ex:secondSuccessor	I
    //   650: getstatic 22	cdht_ex:base	I
    //   653: iadd
    //   654: putstatic 20	cdht_ex:secondSuccessorPort	I
    //   657: getstatic 16	java/lang/System:out	Ljava/io/PrintStream;
    //   660: new 23	java/lang/StringBuilder
    //   663: dup
    //   664: invokespecial 24	java/lang/StringBuilder:<init>	()V
    //   667: ldc 61
    //   669: invokevirtual 26	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   672: getstatic 31	cdht_ex:firstSuccessor	I
    //   675: invokevirtual 27	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   678: invokevirtual 29	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   681: invokevirtual 18	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   684: getstatic 16	java/lang/System:out	Ljava/io/PrintStream;
    //   687: new 23	java/lang/StringBuilder
    //   690: dup
    //   691: invokespecial 24	java/lang/StringBuilder:<init>	()V
    //   694: ldc 62
    //   696: invokevirtual 26	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   699: getstatic 33	cdht_ex:secondSuccessor	I
    //   702: invokevirtual 27	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   705: invokevirtual 29	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   708: invokevirtual 18	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   711: goto +72 -> 783
    //   714: iload 7
    //   716: putstatic 33	cdht_ex:secondSuccessor	I
    //   719: getstatic 33	cdht_ex:secondSuccessor	I
    //   722: getstatic 22	cdht_ex:base	I
    //   725: iadd
    //   726: putstatic 20	cdht_ex:secondSuccessorPort	I
    //   729: getstatic 16	java/lang/System:out	Ljava/io/PrintStream;
    //   732: new 23	java/lang/StringBuilder
    //   735: dup
    //   736: invokespecial 24	java/lang/StringBuilder:<init>	()V
    //   739: ldc 61
    //   741: invokevirtual 26	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   744: getstatic 31	cdht_ex:firstSuccessor	I
    //   747: invokevirtual 27	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   750: invokevirtual 29	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   753: invokevirtual 18	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   756: getstatic 16	java/lang/System:out	Ljava/io/PrintStream;
    //   759: new 23	java/lang/StringBuilder
    //   762: dup
    //   763: invokespecial 24	java/lang/StringBuilder:<init>	()V
    //   766: ldc 62
    //   768: invokevirtual 26	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   771: getstatic 33	cdht_ex:secondSuccessor	I
    //   774: invokevirtual 27	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   777: invokevirtual 29	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   780: invokevirtual 18	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   783: goto +367 -> 1150
    //   786: iconst_1
    //   787: putstatic 15	cdht_ex:requestFileMessage	Z
    //   790: aload_1
    //   791: ldc 48
    //   793: invokevirtual 49	java/lang/String:split	(Ljava/lang/String;)[Ljava/lang/String;
    //   796: iconst_1
    //   797: aaload
    //   798: astore 6
    //   800: aload_1
    //   801: ldc 48
    //   803: invokevirtual 49	java/lang/String:split	(Ljava/lang/String;)[Ljava/lang/String;
    //   806: iconst_2
    //   807: aaload
    //   808: invokestatic 37	java/lang/Integer:parseInt	(Ljava/lang/String;)I
    //   811: istore 7
    //   813: aload_1
    //   814: ldc 48
    //   816: invokevirtual 49	java/lang/String:split	(Ljava/lang/String;)[Ljava/lang/String;
    //   819: iconst_3
    //   820: aaload
    //   821: invokestatic 37	java/lang/Integer:parseInt	(Ljava/lang/String;)I
    //   824: istore 8
    //   826: aload_1
    //   827: ldc 48
    //   829: invokevirtual 49	java/lang/String:split	(Ljava/lang/String;)[Ljava/lang/String;
    //   832: iconst_4
    //   833: aaload
    //   834: invokestatic 37	java/lang/Integer:parseInt	(Ljava/lang/String;)I
    //   837: istore 9
    //   839: aload 5
    //   841: ldc 8
    //   843: invokevirtual 51	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   846: ifeq +42 -> 888
    //   849: getstatic 16	java/lang/System:out	Ljava/io/PrintStream;
    //   852: new 23	java/lang/StringBuilder
    //   855: dup
    //   856: invokespecial 24	java/lang/StringBuilder:<init>	()V
    //   859: ldc 69
    //   861: invokevirtual 26	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   864: iload 9
    //   866: invokevirtual 27	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   869: ldc 70
    //   871: invokevirtual 26	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   874: aload 6
    //   876: invokevirtual 26	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   879: invokevirtual 29	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   882: invokevirtual 18	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   885: goto +265 -> 1150
    //   888: iload 7
    //   890: getstatic 58	cdht_ex:peerIdentity	I
    //   893: if_icmpeq +44 -> 937
    //   896: getstatic 58	cdht_ex:peerIdentity	I
    //   899: iload 7
    //   901: if_icmpge +20 -> 921
    //   904: getstatic 31	cdht_ex:firstSuccessor	I
    //   907: iload 7
    //   909: if_icmpge +12 -> 921
    //   912: getstatic 58	cdht_ex:peerIdentity	I
    //   915: getstatic 31	cdht_ex:firstSuccessor	I
    //   918: if_icmpgt +19 -> 937
    //   921: getstatic 58	cdht_ex:peerIdentity	I
    //   924: iload 7
    //   926: if_icmpge +16 -> 942
    //   929: getstatic 31	cdht_ex:firstSuccessor	I
    //   932: iload 7
    //   934: if_icmple +8 -> 942
    //   937: iconst_0
    //   938: istore_0
    //   939: goto +5 -> 944
    //   942: iconst_1
    //   943: istore_0
    //   944: iload_0
    //   945: ifeq +99 -> 1044
    //   948: new 23	java/lang/StringBuilder
    //   951: dup
    //   952: invokespecial 24	java/lang/StringBuilder:<init>	()V
    //   955: ldc 71
    //   957: invokevirtual 26	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   960: aload 6
    //   962: invokevirtual 26	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   965: ldc 48
    //   967: invokevirtual 26	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   970: iload 7
    //   972: invokevirtual 27	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   975: ldc 48
    //   977: invokevirtual 26	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   980: iload 8
    //   982: invokevirtual 27	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   985: ldc 48
    //   987: invokevirtual 26	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   990: getstatic 58	cdht_ex:peerIdentity	I
    //   993: invokevirtual 27	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   996: ldc 55
    //   998: invokevirtual 26	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1001: invokevirtual 29	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1004: getstatic 19	cdht_ex:firstSuccessorPort	I
    //   1007: invokestatic 56	cdht_ex:TCPClient	(Ljava/lang/String;I)V
    //   1010: getstatic 16	java/lang/System:out	Ljava/io/PrintStream;
    //   1013: new 23	java/lang/StringBuilder
    //   1016: dup
    //   1017: invokespecial 24	java/lang/StringBuilder:<init>	()V
    //   1020: ldc 72
    //   1022: invokevirtual 26	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1025: aload 6
    //   1027: invokevirtual 26	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1030: ldc 73
    //   1032: invokevirtual 26	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1035: invokevirtual 29	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1038: invokevirtual 18	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   1041: goto +109 -> 1150
    //   1044: new 23	java/lang/StringBuilder
    //   1047: dup
    //   1048: invokespecial 24	java/lang/StringBuilder:<init>	()V
    //   1051: ldc 74
    //   1053: invokevirtual 26	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1056: aload 6
    //   1058: invokevirtual 26	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1061: ldc 48
    //   1063: invokevirtual 26	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1066: iload 7
    //   1068: invokevirtual 27	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   1071: ldc 48
    //   1073: invokevirtual 26	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1076: iload 8
    //   1078: invokevirtual 27	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   1081: ldc 48
    //   1083: invokevirtual 26	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1086: getstatic 58	cdht_ex:peerIdentity	I
    //   1089: invokevirtual 27	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   1092: ldc 55
    //   1094: invokevirtual 26	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1097: invokevirtual 29	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1100: getstatic 22	cdht_ex:base	I
    //   1103: iload 8
    //   1105: iadd
    //   1106: invokestatic 56	cdht_ex:TCPClient	(Ljava/lang/String;I)V
    //   1109: getstatic 16	java/lang/System:out	Ljava/io/PrintStream;
    //   1112: new 23	java/lang/StringBuilder
    //   1115: dup
    //   1116: invokespecial 24	java/lang/StringBuilder:<init>	()V
    //   1119: ldc 72
    //   1121: invokevirtual 26	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1124: aload 6
    //   1126: invokevirtual 26	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1129: ldc 75
    //   1131: invokevirtual 26	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1134: iload 8
    //   1136: invokevirtual 27	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   1139: ldc 76
    //   1141: invokevirtual 26	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1144: invokevirtual 29	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1147: invokevirtual 18	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   1150: goto -1139 -> 11
    //   1153: ret 1
    //   1155: bipush 54
    //   1157: imul
    //   1158: swap
    //   1159: istore_1
    //   1160: iload_1
    // Line number table:
    //   Java source line #84	-> byte code offset #0
    //   Java source line #86	-> byte code offset #11
    //   Java source line #87	-> byte code offset #16
    //   Java source line #88	-> byte code offset #36
    //   Java source line #89	-> byte code offset #42
    //   Java source line #91	-> byte code offset #52
    //   Java source line #94	-> byte code offset #62
    //   Java source line #99	-> byte code offset #78
    //   Java source line #100	-> byte code offset #86
    //   Java source line #101	-> byte code offset #93
    //   Java source line #102	-> byte code offset #101
    //   Java source line #109	-> byte code offset #102
    //   Java source line #110	-> byte code offset #106
    //   Java source line #111	-> byte code offset #110
    //   Java source line #112	-> byte code offset #123
    //   Java source line #115	-> byte code offset #139
    //   Java source line #120	-> byte code offset #167
    //   Java source line #121	-> byte code offset #200
    //   Java source line #122	-> byte code offset #213
    //   Java source line #123	-> byte code offset #226
    //   Java source line #124	-> byte code offset #234
    //   Java source line #125	-> byte code offset #252
    //   Java source line #126	-> byte code offset #257
    //   Java source line #127	-> byte code offset #263
    //   Java source line #128	-> byte code offset #268
    //   Java source line #129	-> byte code offset #271
    //   Java source line #130	-> byte code offset #289
    //   Java source line #131	-> byte code offset #299
    //   Java source line #134	-> byte code offset #309
    //   Java source line #135	-> byte code offset #344
    //   Java source line #136	-> byte code offset #376
    //   Java source line #137	-> byte code offset #407
    //   Java source line #138	-> byte code offset #434
    //   Java source line #140	-> byte code offset #461
    //   Java source line #142	-> byte code offset #464
    //   Java source line #147	-> byte code offset #474
    //   Java source line #148	-> byte code offset #478
    //   Java source line #149	-> byte code offset #491
    //   Java source line #151	-> byte code offset #501
    //   Java source line #152	-> byte code offset #545
    //   Java source line #153	-> byte code offset #576
    //   Java source line #154	-> byte code offset #579
    //   Java source line #159	-> byte code offset #589
    //   Java source line #160	-> byte code offset #593
    //   Java source line #161	-> byte code offset #603
    //   Java source line #162	-> byte code offset #616
    //   Java source line #165	-> byte code offset #626
    //   Java source line #166	-> byte code offset #632
    //   Java source line #167	-> byte code offset #637
    //   Java source line #168	-> byte code offset #647
    //   Java source line #169	-> byte code offset #657
    //   Java source line #170	-> byte code offset #684
    //   Java source line #174	-> byte code offset #714
    //   Java source line #175	-> byte code offset #719
    //   Java source line #176	-> byte code offset #729
    //   Java source line #177	-> byte code offset #756
    //   Java source line #179	-> byte code offset #783
    //   Java source line #183	-> byte code offset #786
    //   Java source line #184	-> byte code offset #790
    //   Java source line #185	-> byte code offset #800
    //   Java source line #186	-> byte code offset #813
    //   Java source line #187	-> byte code offset #826
    //   Java source line #189	-> byte code offset #839
    //   Java source line #194	-> byte code offset #849
    //   Java source line #201	-> byte code offset #888
    //   Java source line #204	-> byte code offset #937
    //   Java source line #205	-> byte code offset #942
    //   Java source line #206	-> byte code offset #944
    //   Java source line #207	-> byte code offset #948
    //   Java source line #208	-> byte code offset #1010
    //   Java source line #210	-> byte code offset #1044
    //   Java source line #211	-> byte code offset #1109
    //   Java source line #215	-> byte code offset #1150
    // Local variable table:
    //   start	length	slot	name	signature
    //   939	4	0	forwardMessage	boolean
    //   944	209	0	forwardMessage	boolean
    //   42	1111	1	clientMessage	String
    //   11	1142	2	welcomeServer	java.net.ServerSocket
    //   16	1137	3	connectionSocket	java.net.Socket
    //   36	1117	4	inFromClient	java.io.BufferedReader
    //   52	1101	5	status	String
    //   123	341	6	quitPeer	int
    //   491	88	6	reponseToPort	int
    //   603	183	6	changedSuccessor	String
    //   800	353	6	fileName	String
    //   213	251	7	newFirstSuccessor	int
    //   501	78	7	whichSuccessor	String
    //   616	170	7	newSuccessor	int
    //   813	340	7	hashValue	int
    //   226	238	8	newSecondSuccessor	int
    //   826	327	8	sourcePeerIdentity	int
    //   257	14	9	tmp	int
    //   839	314	9	clientPeerIdentity	int
  }
  
  /* Error */
  public static void TCPClient(String message, int destPort)
  {
    // Byte code:
    //   0: new 77	java/net/Socket
    //   3: dup
    //   4: ldc 3
    //   6: iload_1
    //   7: invokespecial 78	java/net/Socket:<init>	(Ljava/lang/String;I)V
    //   10: astore_2
    //   11: new 79	java/io/DataOutputStream
    //   14: dup
    //   15: aload_2
    //   16: invokevirtual 80	java/net/Socket:getOutputStream	()Ljava/io/OutputStream;
    //   19: invokespecial 81	java/io/DataOutputStream:<init>	(Ljava/io/OutputStream;)V
    //   22: astore_3
    //   23: aload_3
    //   24: aload_0
    //   25: invokevirtual 82	java/io/DataOutputStream:writeBytes	(Ljava/lang/String;)V
    //   28: goto +12 -> 40
    //   31: astore_2
    //   32: getstatic 16	java/lang/System:out	Ljava/io/PrintStream;
    //   35: ldc 84
    //   37: invokevirtual 18	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   40: return
    //   41: iadd
    //   42: iadd
    //   43: bipush 54
    //   45: imul
    // Line number table:
    //   Java source line #221	-> byte code offset #0
    //   Java source line #222	-> byte code offset #11
    //   Java source line #223	-> byte code offset #23
    //   Java source line #226	-> byte code offset #28
    //   Java source line #224	-> byte code offset #31
    //   Java source line #225	-> byte code offset #32
    //   Java source line #227	-> byte code offset #40
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	41	0	message	String
    //   0	41	1	destPort	int
    //   11	20	2	clientSocket	java.net.Socket
    //   32	9	2	e	Exception
    //   23	8	3	outToServer	java.io.DataOutputStream
    // Exception table:
    //   from	to	target	type
    //   0	28	31	java/lang/Exception
  }
  
  /* Error */
  public static void main(String[] args)
    throws java.io.IOException
  {
    // Byte code:
    //   0: aload_0
    //   1: iconst_0
    //   2: aaload
    //   3: invokestatic 37	java/lang/Integer:parseInt	(Ljava/lang/String;)I
    //   6: putstatic 58	cdht_ex:peerIdentity	I
    //   9: aload_0
    //   10: iconst_1
    //   11: aaload
    //   12: invokestatic 37	java/lang/Integer:parseInt	(Ljava/lang/String;)I
    //   15: putstatic 31	cdht_ex:firstSuccessor	I
    //   18: aload_0
    //   19: iconst_2
    //   20: aaload
    //   21: invokestatic 37	java/lang/Integer:parseInt	(Ljava/lang/String;)I
    //   24: putstatic 33	cdht_ex:secondSuccessor	I
    //   27: ldc 85
    //   29: putstatic 22	cdht_ex:base	I
    //   32: getstatic 22	cdht_ex:base	I
    //   35: getstatic 58	cdht_ex:peerIdentity	I
    //   38: iadd
    //   39: putstatic 39	cdht_ex:peerPort	I
    //   42: getstatic 22	cdht_ex:base	I
    //   45: getstatic 31	cdht_ex:firstSuccessor	I
    //   48: iadd
    //   49: putstatic 19	cdht_ex:firstSuccessorPort	I
    //   52: getstatic 22	cdht_ex:base	I
    //   55: getstatic 33	cdht_ex:secondSuccessor	I
    //   58: iadd
    //   59: putstatic 20	cdht_ex:secondSuccessorPort	I
    //   62: new 86	java/net/DatagramSocket
    //   65: dup
    //   66: getstatic 39	cdht_ex:peerPort	I
    //   69: invokespecial 87	java/net/DatagramSocket:<init>	(I)V
    //   72: putstatic 10	cdht_ex:UDPSocket	Ljava/net/DatagramSocket;
    //   75: new 88	cdht_ex$1
    //   78: dup
    //   79: invokespecial 89	cdht_ex$1:<init>	()V
    //   82: astore_1
    //   83: new 90	cdht_ex$2
    //   86: dup
    //   87: invokespecial 91	cdht_ex$2:<init>	()V
    //   90: astore_2
    //   91: new 92	cdht_ex$3
    //   94: dup
    //   95: invokespecial 93	cdht_ex$3:<init>	()V
    //   98: astore_3
    //   99: new 94	cdht_ex$4
    //   102: dup
    //   103: invokespecial 95	cdht_ex$4:<init>	()V
    //   106: astore 4
    //   108: new 96	cdht_ex$5
    //   111: dup
    //   112: invokespecial 97	cdht_ex$5:<init>	()V
    //   115: astore 5
    //   117: new 98	cdht_ex$6
    //   120: dup
    //   121: aload_2
    //   122: aload_3
    //   123: aload 4
    //   125: aload_1
    //   126: aload 5
    //   128: invokespecial 99	cdht_ex$6:<init>	(Ljava/lang/Thread;Ljava/lang/Thread;Ljava/lang/Thread;Ljava/lang/Thread;Ljava/lang/Thread;)V
    //   131: astore 6
    //   133: aload_1
    //   134: invokevirtual 100	java/lang/Thread:start	()V
    //   137: aload_2
    //   138: invokevirtual 100	java/lang/Thread:start	()V
    //   141: aload_3
    //   142: invokevirtual 100	java/lang/Thread:start	()V
    //   145: aload 4
    //   147: invokevirtual 100	java/lang/Thread:start	()V
    //   150: aload 5
    //   152: invokevirtual 100	java/lang/Thread:start	()V
    //   155: aload 6
    //   157: invokevirtual 100	java/lang/Thread:start	()V
    //   160: return
    //   161: swap
    //   162: ret 1
    //   164: imul
    //   165: iload 4
    // Line number table:
    //   Java source line #232	-> byte code offset #0
    //   Java source line #233	-> byte code offset #9
    //   Java source line #234	-> byte code offset #18
    //   Java source line #235	-> byte code offset #27
    //   Java source line #236	-> byte code offset #32
    //   Java source line #237	-> byte code offset #42
    //   Java source line #238	-> byte code offset #52
    //   Java source line #239	-> byte code offset #62
    //   Java source line #242	-> byte code offset #75
    //   Java source line #250	-> byte code offset #83
    //   Java source line #263	-> byte code offset #91
    //   Java source line #276	-> byte code offset #99
    //   Java source line #309	-> byte code offset #108
    //   Java source line #318	-> byte code offset #117
    //   Java source line #373	-> byte code offset #133
    //   Java source line #374	-> byte code offset #137
    //   Java source line #375	-> byte code offset #141
    //   Java source line #376	-> byte code offset #145
    //   Java source line #377	-> byte code offset #150
    //   Java source line #378	-> byte code offset #155
    //   Java source line #379	-> byte code offset #160
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	161	0	args	String[]
    //   83	78	1	newServer	Thread
    //   91	70	2	firstClient	Thread
    //   99	62	3	secondClient	Thread
    //   108	53	4	check	Thread
    //   117	44	5	requestFileServer	Thread
    //   133	28	6	waitInput	Thread
  }
  
  static
  {
    for (;;)
    {
      count = 0;
      firstPingBack = false;
      secondPingBack = false;
      userInput = false;
      requestFileMessage = false;return;
    }
  }
}
