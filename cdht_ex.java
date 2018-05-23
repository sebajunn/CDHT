/*     */ import java.io.PrintStream;
/*     */ import java.net.DatagramPacket;
/*     */ 
/*     */ public class cdht_ex
/*     */ {
/*     */   public static int base;
/*     */   public static int peerIdentity;
/*     */   public static int firstSuccessor;
/*     */   public static int secondSuccessor;
/*     */   public static int peerPort;
/*     */   public static int firstSuccessorPort;
/*     */   public static int secondSuccessorPort;
/*  13 */   public static int count = 0;
/*  14 */   public static boolean firstPingBack = false;
/*  15 */   public static boolean secondPingBack = false;
/*  16 */   public static boolean userInput = false;
/*  17 */   public static boolean requestFileMessage = false;
/*     */   
/*     */   public static java.net.DatagramSocket UDPSocket;
/*     */   
/*     */   public static DatagramPacket M730967787(int destinationPort, byte arg1)
/*     */     throws Exception
/*     */   {
/*  24 */     message = "Request";
/*  25 */     java.net.InetAddress IPAddress = java.net.InetAddress.getByName("localhost");
/*  26 */     byte[] sendData = message.getBytes();
/*  27 */     DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, destinationPort);
/*  28 */     return sendPacket;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int M240536415(String fileName, byte arg1)
/*     */   {
/*  74 */     w = Integer.parseInt(String.valueOf(fileName.charAt(0)));
/*  75 */     int x = Integer.parseInt(String.valueOf(fileName.charAt(1)));
/*  76 */     int y = Integer.parseInt(String.valueOf(fileName.charAt(2)));
/*  77 */     int z = Integer.parseInt(String.valueOf(fileName.charAt(3)));
/*  78 */     int hash = (w * 1000 + x * 100 + y * 10 + z + 1) % 256;
/*  79 */     return hash;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void M597545277(String message, int destPort, byte arg2)
/*     */   {
/*     */     try
/*     */     {
/* 221 */       clientSocket = new java.net.Socket("localhost", destPort);
/* 222 */       java.io.DataOutputStream outToServer = new java.io.DataOutputStream(clientSocket.getOutputStream());
/* 223 */       outToServer.writeBytes(message);
/*     */     } catch (Exception clientSocket) { Exception e;
/* 225 */       System.out.println("----Socket Create failed-----");
/*     */     }
/*     */   }
/*     */   
/*     */   public static void main(String[] args)
/*     */     throws java.io.IOException
/*     */   {
/* 232 */     peerIdentity = Integer.parseInt(args[0]);
/* 233 */     firstSuccessor = Integer.parseInt(args[1]);
/* 234 */     secondSuccessor = Integer.parseInt(args[2]);
/* 235 */     base = 50000;
/* 236 */     peerPort = base + peerIdentity;
/* 237 */     firstSuccessorPort = base + firstSuccessor;
/* 238 */     secondSuccessorPort = base + secondSuccessor;
/* 239 */     UDPSocket = new java.net.DatagramSocket(peerPort);
/*     */     
/*     */ 
/* 242 */     final Thread newServer = new Thread()
/*     */     {
/*     */       public void run()
/*     */       {
/* 246 */         int i = 43;int j = i - 1;int k = i * j;k %= 2; if (((k < 0 ^ true) & true)) i = 3; try { cdht_ex.M637417425(i);
/*     */         } catch (Exception e) {}
/*     */       }
/* 249 */     };
/* 250 */     Thread firstClient = new Thread()
/*     */     {
/*     */       public void run()
/*     */       {
/*     */         try {
/*     */           for (;;) {
/* 256 */             int i = 47;int j = i - 1;int k = i * j;k %= 2; if (((k < 0 ^ true) & true)) i = 77; DatagramPacket packet = cdht_ex.M730967787(cdht_ex.firstSuccessorPort, i);
/* 257 */             cdht_ex.UDPSocket.send(packet);
/* 258 */             Thread.currentThread();Thread.sleep(3000L); }
/* 259 */           return;
/*     */         } catch (Exception e) {}
/*     */       }
/* 262 */     };
/* 263 */     final Thread secondClient = new Thread()
/*     */     {
/*     */       public void run()
/*     */       {
/*     */         try {
/*     */           for (;;) {
/* 269 */             int i = -14;int j = i - 1;int k = i * j;k %= 2; if (((k < 0 ^ true) & true)) i = 7; DatagramPacket packet = cdht_ex.M730967787(cdht_ex.secondSuccessorPort, i);
/* 270 */             cdht_ex.UDPSocket.send(packet);
/* 271 */             Thread.currentThread();Thread.sleep(3000L); }
/* 272 */           return;
/*     */         } catch (Exception e) {}
/*     */       }
/* 275 */     };
/* 276 */     final Thread check = new Thread()
/*     */     {
/*     */       public void run()
/*     */       {
/*     */         for (;;)
/*     */         {
/*     */           try
/*     */           {
/* 284 */             Thread.currentThread();Thread.sleep(12000L);
/* 285 */           } catch (InterruptedException e) { return;
/*     */           }
/* 287 */           if (cdht_ex.firstPingBack) {
/* 288 */             cdht_ex.firstPingBack = false;
/* 289 */           } else if (!cdht_ex.firstPingBack) {
/* 290 */             String changeSuccessor = "ask " + cdht_ex.peerPort + " first\n";
/* 291 */             int i = 88;int j = i - 1;int k = i * j;k %= 2; if (((k < 0 ^ true) & true)) i = 112; cdht_ex.M597545277(changeSuccessor, cdht_ex.secondSuccessorPort, i);
/* 292 */             cdht_ex.firstPingBack = false;
/* 293 */             System.out.println("Peer " + cdht_ex.firstSuccessor + " is no longer alive.");
/*     */           }
/*     */           
/* 296 */           if (cdht_ex.secondPingBack) {
/* 297 */             cdht_ex.secondPingBack = false;
/*     */           }
/* 299 */           else if (!cdht_ex.secondPingBack) {
/* 300 */             String changeSuccessor = "ask " + cdht_ex.peerPort + " second\n";
/* 301 */             int m = 31;int n = m - 1;int i1 = m * n;i1 %= 2; if (((i1 < 0 ^ true) & true)) m = 56; cdht_ex.M597545277(changeSuccessor, cdht_ex.firstSuccessorPort, m);
/* 302 */             cdht_ex.secondPingBack = false;
/* 303 */             System.out.println("Peer " + cdht_ex.secondSuccessor + " is no longer alive.");
/*     */           }
/*     */           
/*     */         }
/*     */       }
/* 308 */     };
/* 309 */     final Thread requestFileServer = new Thread()
/*     */     {
/*     */ 
/*     */       public void run()
/*     */       {
/* 314 */         int i = 95;int j = i - 1;int k = i * j;k %= 2; if (((k < 0 ^ true) & true)) i = -73; try { cdht_ex.M637417425(i);
/*     */         } catch (Exception e) {}
/*     */       }
/* 317 */     };
/* 318 */     Thread waitInput = new Thread()
/*     */     {
/*     */ 
/*     */       public void run()
/*     */       {
/* 323 */         java.util.Scanner sc = new java.util.Scanner(System.in);
/*     */         for (;;)
/*     */         {
/* 326 */           String inputString = sc.nextLine();
/* 327 */           if (inputString.equals("")) {
/* 328 */             if ((cdht_ex.userInput) || (cdht_ex.requestFileMessage)) {
/* 329 */               cdht_ex.userInput = false;
/* 330 */               cdht_ex.requestFileMessage = false;
/* 331 */               System.out.println("You have pressed enter key, ping messages are displayed again\n");
/*     */ 
/*     */             }
/* 334 */             else if (!cdht_ex.userInput) {
/* 335 */               cdht_ex.userInput = true;
/* 336 */               System.out.println("You have pressed enter key, ping messages are hide\n");
/*     */             }
/*     */             
/*     */ 
/*     */           }
/* 341 */           else if (inputString.equals("quit"))
/*     */           {
/*     */ 
/*     */ 
/* 345 */             String sendMessage = "quit " + cdht_ex.peerIdentity + " " + cdht_ex.firstSuccessor + " " + cdht_ex.secondSuccessor + "\n";
/* 346 */             try { int i = -25;int j = i - 1;int k = i * j;k %= 2; if (((k < 0 ^ true) & true)) i = -53; cdht_ex.M597545277(sendMessage, cdht_ex.firstSuccessorPort, i); } catch (Exception e) { return; }
/* 347 */             try { Thread.currentThread();Thread.sleep(3000L); } catch (InterruptedException e) { return;
/*     */             }
/* 349 */             this.val$firstClient.interrupt();
/* 350 */             secondClient.interrupt();
/* 351 */             check.interrupt();
/* 352 */             if (cdht_ex.count == 2) {
/* 353 */               newServer.interrupt();
/* 354 */               requestFileServer.interrupt();
/*     */             }
/*     */           }
/*     */           else {
/* 358 */             String command = inputString.split(" ")[0];
/* 359 */             if (command.equals("request"))
/*     */             {
/*     */ 
/*     */ 
/* 363 */               String fileName = inputString.split(" ")[1];
/* 364 */               int m = -54;int n = m - 1;int i1 = m * n;i1 %= 2; if (((i1 < 0 ^ true) & true)) m = -101; int hashValue = cdht_ex.M240536415(fileName, m);
/* 365 */               String sendMessage = "request " + fileName + " " + hashValue + " " + cdht_ex.peerIdentity + " " + cdht_ex.peerIdentity + "\n";
/*     */               try {
/* 367 */                 int i2 = 46;int i3 = i2 - 1;int i4 = i2 * i3;i4 %= 2; if (((i4 < 0 ^ true) & true)) i2 = 1; cdht_ex.M597545277(sendMessage, cdht_ex.firstSuccessorPort, i2); } catch (Exception e) { return;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/* 372 */       } };
/* 373 */     newServer.start();
/* 374 */     firstClient.start();
/* 375 */     secondClient.start();
/* 376 */     check.start();
/* 377 */     requestFileServer.start();
/* 378 */     waitInput.start();
/*     */   }
/*     */   
/*     */   public static void M637417425(byte paramByte)
/*     */     throws Exception
/*     */   {
/*     */     java.util.Random localRandom = new java.util.Random(paramByte);
/*     */     int[] arrayOfInt = new int[28];
/*     */     for (int i10 = 0; i10 < 28; i10++) {
/*     */       arrayOfInt[i10] = localRandom.nextInt();
/*     */     }
/*     */     i10 = 0;
/*     */     String str1 = null;
/*     */     java.net.ServerSocket localServerSocket = null;
/*     */     java.net.Socket localSocket = null;
/*     */     java.io.BufferedReader localBufferedReader = null;
/*     */     String str2 = null;
/*     */     String str3 = null;
/*     */     int i = 0;
/*     */     int j = 0;
/*     */     int k = 0;
/*     */     int m = 0;
/*     */     String str4 = null;
/*     */     int n = 0;
/*     */     int i1 = 0;
/*     */     int i2 = 0;
/*     */     int i3 = 0;
/*     */     int i4 = 0;
/*     */     int i5 = 0;
/*     */     String str5 = null;
/*     */     int i6 = 0;
/*     */     String str6 = null;
/*     */     byte[] arrayOfByte1 = null;
/*     */     byte[] arrayOfByte2 = null;
/*     */     DatagramPacket localDatagramPacket1 = null;
/*     */     int i7 = 0;
/*     */     java.net.InetAddress localInetAddress = null;
/*     */     DatagramPacket localDatagramPacket2 = null;
/*     */     int i8 = 0;
/*     */     int i9 = 0;
/*     */     for (;;)
/*     */     {
/*     */       switch (arrayOfInt[i10])
/*     */       {
/*     */       case -634239373: 
/*     */         if (i7 == firstSuccessorPort) {
/*     */           i10 += 6;
/*     */         }
/*     */         break;
/*     */       case -3666739: 
/*     */       case -1879439976: 
/*     */       case -1458916125: 
/*     */       case -995758198: 
/*     */       case -811018144: 
/*     */       case 1515333764: 
/*     */       case -1818437706: 
/*     */       case 304908421: 
/*     */       case 128065962: 
/*     */       case 288278256: 
/*     */       case -1824734168: 
/*     */       case -1795872892: 
/*     */       case 285587229: 
/*     */       case -1237341112: 
/*     */         for (;;)
/*     */         {
/*     */           secondPingBack = true;
/*     */           break label2309;
/*     */           UDPSocket.receive(localDatagramPacket1);
/*     */           break label2309;
/*     */           if (i7 != secondSuccessorPort) {
/*     */             break label2309;
/*     */           }
/*     */           i10 += 5;
/*     */           break label686;
/*     */           if (i6 == 0) {
/*     */             break label2309;
/*     */           }
/*     */           i10 += 3;
/*     */           break;
/*     */           if (!userInput) {
/*     */             break label2309;
/*     */           }
/*     */           i10 += 3;
/*     */           break label679;
/*     */           UDPSocket.send(localDatagramPacket2);
/*     */           break label2309;
/*     */           System.out.println("A ping response message was received from Peer " + secondSuccessor + ".");
/*     */           break label2309;
/*     */           i7 = localDatagramPacket1.getPort();
/*     */           localInetAddress = localDatagramPacket1.getAddress();
/*     */           localDatagramPacket2 = new DatagramPacket(arrayOfByte2, arrayOfByte2.length, localInetAddress, i7);
/*     */           if (userInput) {
/*     */             break label2309;
/*     */           }
/*     */           i10 += 3;
/*     */           break label593;
/*     */           System.out.println("A ping request message was received from Peer " + i8 + ".");
/*     */           break label2309;
/*     */           i9 = 1;
/*     */           break label2309;
/*     */           System.out.println("Ping messages are hide\n");
/*     */           break label2309;
/*     */           if (requestFileMessage) {
/*     */             break label2309;
/*     */           }
/*     */           i10 += 4;
/*     */           break;
/*     */           if (!requestFileMessage) {
/*     */             break label2309;
/*     */           }
/*     */           i10 += 2;
/*     */           break label686;
/*     */           if (!userInput) {
/*     */             break label2309;
/*     */           }
/*     */           i10 += 3;
/*     */         }
/*     */       case 177583826: 
/*     */       case -830330741: 
/*     */       case 976394918: 
/*     */       case -580395097: 
/*     */       case 1767665612: 
/*     */       case 580736275: 
/*     */       case -836442134: 
/*     */       case -385360525: 
/*     */       case -1155099828: 
/*     */       case 655985822: 
/*     */         for (;;)
/*     */         {
/*     */           break label644;
/*     */           break label2309;
/*     */           i10 -= 24;
/*     */           break label392;
/*     */           i8 = i7 - base;
/*     */           if (!userInput) {
/*     */             break label2309;
/*     */           }
/*     */           i10 += 3;
/*     */           break label686;
/*     */           i6 = 1;
/*     */           break label2309;
/*     */           for (;;)
/*     */           {
/*     */             firstPingBack = true;
/*     */             break label2309;
/*     */             if (i7 == firstSuccessorPort) {
/*     */               break label2309;
/*     */             }
/*     */             i10 += 5;
/*     */             break;
/*     */             if (!requestFileMessage) {
/*     */               break label2309;
/*     */             }
/*     */             i10 += 2;
/*     */           }
/*     */           if (i9 == 0) {
/*     */             break label2309;
/*     */           }
/*     */           i10 += 2;
/*     */           break label593;
/*     */           if (!requestFileMessage) {
/*     */             break label2309;
/*     */           }
/*     */           i10 += 2;
/*     */           break;
/*     */           i9 = 0;
/*     */           i6 = 0;
/*     */           str6 = "Response";
/*     */           arrayOfByte1 = new byte['Ѐ'];
/*     */           arrayOfByte2 = str6.getBytes();
/*     */           localDatagramPacket1 = new DatagramPacket(arrayOfByte1, arrayOfByte1.length);
/*     */           break label2309;
/*     */           if (i7 == secondSuccessorPort) {
/*     */             break label2309;
/*     */           }
/*     */           i10 += 5;
/*     */         }
/*     */       case 297393606: 
/*     */         for (;;)
/*     */         {
/*     */           localSocket = localServerSocket.accept();
/*     */           localBufferedReader = new java.io.BufferedReader(new java.io.InputStreamReader(localSocket.getInputStream()));
/*     */           str1 = localBufferedReader.readLine();
/*     */           str2 = str1.split(" ")[0];
/*     */           if (str2.equals("quit"))
/*     */           {
/*     */             if (str1.split(" ")[1].equals("ok"))
/*     */             {
/*     */               count += 1;
/*     */               if (count == 2) {
/*     */                 System.out.println("I can quit now.");
/*     */               }
/*     */             }
/*     */             else
/*     */             {
/*     */               userInput = false;
/*     */               requestFileMessage = false;
/*     */               i4 = Integer.parseInt(str1.split(" ")[1]);
/*     */               if ((i4 != firstSuccessor) && (i4 != secondSuccessor))
/*     */               {
/*     */                 byte b1 = -42;
/*     */                 int i11 = i10 - 1;
/*     */                 i11 = i10 * i11;
/*     */                 i11 %= 2;
/*     */                 if (((i11 < 0 ^ true) & true)) {
/*     */                   b1 = 28;
/*     */                 }
/*     */                 M597545277(str1 + "\n", firstSuccessorPort, b1);
/*     */               }
/*     */               else
/*     */               {
/*     */                 if (i4 != firstSuccessor)
/*     */                 {
/*     */                   byte b2 = -36;
/*     */                   int i12 = i4 - 1;
/*     */                   i12 = i4 * i12;
/*     */                   i12 %= 2;
/*     */                   if (((i12 < 0 ^ true) & true)) {
/*     */                     b2 = -96;
/*     */                   }
/*     */                   M597545277(str1 + "\n", firstSuccessorPort, b2);
/*     */                 }
/*     */                 m = Integer.parseInt(str1.split(" ")[2]);
/*     */                 j = Integer.parseInt(str1.split(" ")[3]);
/*     */                 if (firstSuccessor == i4)
/*     */                 {
/*     */                   firstSuccessor = secondSuccessor == m ? j : m;
/*     */                   k = firstSuccessor;
/*     */                   firstSuccessor = secondSuccessor;
/*     */                   secondSuccessor = k;
/*     */                 }
/*     */                 else
/*     */                 {
/*     */                   secondSuccessor = firstSuccessor == m ? j : m;
/*     */                 }
/*     */                 firstSuccessorPort = base + firstSuccessor;
/*     */                 secondSuccessorPort = base + secondSuccessor;
/*     */                 byte b3 = -99;
/*     */                 int i13 = i4 - 1;
/*     */                 i13 = i4 * i13;
/*     */                 i13 %= 2;
/*     */                 if (((i13 < 0 ^ true) & true)) {
/*     */                   b3 = 33;
/*     */                 }
/*     */                 M597545277("quit ok " + peerIdentity + "\n", i4 + base, b3);
/*     */                 System.out.println(i4 + " " + peerIdentity);
/*     */                 System.out.println("Peer " + i4 + " will depart from the network.");
/*     */                 System.out.println("My first successor is now peer " + firstSuccessor);
/*     */                 System.out.println("My second successor is now peer " + secondSuccessor);
/*     */               }
/*     */             }
/*     */           }
/*     */           else if (str2.equals("ask"))
/*     */           {
/*     */             requestFileMessage = false;
/*     */             n = Integer.parseInt(str1.split(" ")[1]);
/*     */             str4 = str1.split(" ")[2];
/*     */             if (str4.equals("first"))
/*     */             {
/*     */               byte b4 = -13;
/*     */               int i14 = i10 - 1;
/*     */               i14 = i10 * i14;
/*     */               i14 %= 2;
/*     */               if (((i14 < 0 ^ true) & true)) {
/*     */                 b4 = -111;
/*     */               }
/*     */               M597545277("reponseAsk firstSuccessor " + firstSuccessor + "\n", n, b4);
/*     */             }
/*     */             else
/*     */             {
/*     */               byte b5 = -87;
/*     */               int i15 = i10 - 1;
/*     */               i15 = i10 * i15;
/*     */               i15 %= 2;
/*     */               if (((i15 < 0 ^ true) & true)) {
/*     */                 b5 = 7;
/*     */               }
/*     */               M597545277("reponseAsk secondSuccessor " + secondSuccessor + "\n", n, b5);
/*     */             }
/*     */           }
/*     */           else if (str2.equals("reponseAsk"))
/*     */           {
/*     */             requestFileMessage = false;
/*     */             str3 = str1.split(" ")[1];
/*     */             i = Integer.parseInt(str1.split(" ")[2]);
/*     */             if (str3.equals("firstSuccessor"))
/*     */             {
/*     */               firstSuccessor = secondSuccessor;
/*     */               secondSuccessor = i;
/*     */               firstSuccessorPort = firstSuccessor + base;
/*     */               secondSuccessorPort = secondSuccessor + base;
/*     */               System.out.println("My first successor is now peer " + firstSuccessor);
/*     */               System.out.println("My second successor is now peer " + secondSuccessor);
/*     */             }
/*     */             else
/*     */             {
/*     */               secondSuccessor = i;
/*     */               secondSuccessorPort = secondSuccessor + base;
/*     */               System.out.println("My first successor is now peer " + firstSuccessor);
/*     */               System.out.println("My second successor is now peer " + secondSuccessor);
/*     */             }
/*     */           }
/*     */           else
/*     */           {
/*     */             requestFileMessage = true;
/*     */             str5 = str1.split(" ")[1];
/*     */             i3 = Integer.parseInt(str1.split(" ")[2]);
/*     */             i2 = Integer.parseInt(str1.split(" ")[3]);
/*     */             i5 = Integer.parseInt(str1.split(" ")[4]);
/*     */             if (str2.equals("Response"))
/*     */             {
/*     */               System.out.println("Received a response message from peer " + i5 + ", which has the file " + str5);
/*     */             }
/*     */             else
/*     */             {
/*     */               if ((i3 == peerIdentity) || ((peerIdentity < i3) && (firstSuccessor < i3) && (peerIdentity > firstSuccessor)) || ((peerIdentity < i3) && (firstSuccessor > i3))) {
/*     */                 i1 = 0;
/*     */               } else {
/*     */                 i1 = 1;
/*     */               }
/*     */               if (i1 != 0)
/*     */               {
/*     */                 byte b6 = 43;
/*     */                 int i16 = i10 - 1;
/*     */                 i16 = i10 * i16;
/*     */                 i16 %= 2;
/*     */                 if (((i16 < 0 ^ true) & true)) {
/*     */                   b6 = -63;
/*     */                 }
/*     */                 M597545277("Request " + str5 + " " + i3 + " " + i2 + " " + peerIdentity + "\n", firstSuccessorPort, b6);
/*     */                 System.out.println("File " + str5 + " is not stored here.\nFile request message has been forwarded to my successor.");
/*     */               }
/*     */               else
/*     */               {
/*     */                 byte b7 = 125;
/*     */                 int i17 = i10 - 1;
/*     */                 i17 = i10 * i17;
/*     */                 i17 %= 2;
/*     */                 if (((i17 < 0 ^ true) & true)) {
/*     */                   b7 = 108;
/*     */                 }
/*     */                 M597545277("Response " + str5 + " " + i3 + " " + i2 + " " + peerIdentity + "\n", base + i2, b7);
/*     */                 System.out.println("File " + str5 + " is stored here.\nA response message, destined for peer " + i2 + ", has been sent.");
/*     */               }
/*     */             }
/*     */           }
/*     */           break label2254;
/*     */           break;
/*     */           i10 += 0;
/*     */         }
/*     */       case -234435524: 
/*     */         System.out.println("A ping response message was received from Peer " + firstSuccessor + ".");
/*     */         break;
/*     */       case 1182801747: 
/*     */         label392:
/*     */         label593:
/*     */         label644:
/*     */         label679:
/*     */         label686:
/*     */         label2254:
/*     */         localServerSocket = new java.net.ServerSocket(peerPort);
/*     */         break;
/*     */       }
/*     */       label2309:
/*     */       i10++;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\COMP3331\Normal\cdht_ex_obf.jar!\cdht_ex.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */