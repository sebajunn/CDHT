//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package a;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;

public class a {
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

    public a() {
    }

    public static void main(String[] args) throws Exception {
        boolean var10000 = a.c.b;
        a = Integer.parseInt(args[0]);
        int a = var10000;
        b = Integer.parseInt(args[1]);
        c = Integer.parseInt(args[2]);
        InetAddress IPAddress = InetAddress.getByName("127.0.0.1");
        f = (new Random()).nextInt(65536);
        g = h = f;
        j = 0;
        i = 0;
        DatagramSocket socket = new DatagramSocket('썐' + a);
        d ping = new d(socket);
        Timer timer = new Timer();
        timer.schedule(ping, 1000L, 30000L);
        c UDPThread = new c(socket);
        UDPThread.start();
        b TCPThread = new b();
        TCPThread.start();
        Scanner s = new Scanner(System.in);

        String input;
        while(!(input = s.nextLine()).isEmpty()) {
            DataOutputStream outToServer;
            String sentence;
            if (input.equalsIgnoreCase("quit")) {
                Socket sendSocketFirst = new Socket(IPAddress, '썐' + b);
                Socket sendSocketSecond = new Socket(IPAddress, '썐' + c);
                DataOutputStream outToServerFirst = new DataOutputStream(sendSocketFirst.getOutputStream());
                outToServer = new DataOutputStream(sendSocketSecond.getOutputStream());
                sentence = "QUIT " + d + " " + e + " " + a;
                outToServerFirst.writeBytes(sentence + "\n");
                outToServer.writeBytes(sentence + "\n");
                BufferedReader inFromFirst = new BufferedReader(new InputStreamReader(sendSocketFirst.getInputStream()));
                BufferedReader inFromSecond = new BufferedReader(new InputStreamReader(sendSocketSecond.getInputStream()));
                inFromFirst.readLine();
                inFromSecond.readLine();
                sendSocketFirst.close();
                sendSocketSecond.close();
                Socket sendSocketA = new Socket(IPAddress, '썐' + d);
                DataOutputStream outToServerA = new DataOutputStream(sendSocketA.getOutputStream());
                sentence = "QUIT " + b + " " + c + " " + a;
                outToServerA.writeBytes(sentence + "\n");
                BufferedReader inFromA = new BufferedReader(new InputStreamReader(sendSocketA.getInputStream()));
                inFromA.readLine();
                sendSocketA.close();
                if (e != c) {
                    Socket sendSocketB = new Socket(IPAddress, '썐' + e);
                    DataOutputStream outToServerB = new DataOutputStream(sendSocketB.getOutputStream());
                    outToServerB.writeBytes(sentence + "\n");
                    BufferedReader inFromB = new BufferedReader(new InputStreamReader(sendSocketB.getInputStream()));
                    inFromB.readLine();
                    sendSocketB.close();
                }

                System.out.println("Peer " + a + " has left the network.");
                System.exit(0);
                if (!a) {
                    continue;
                }

                label60: {
                    try {
                        if (p) {
                            var10000 = false;
                            break label60;
                        }
                    } catch (ConnectException var26) {
                        throw var26;
                    }

                    var10000 = true;
                }

                p = var10000;
            }

            label72: {
                try {
                    if (input.length() != 12 || !input.substring(0, 7).equalsIgnoreCase("request")) {
                        break label72;
                    }
                } catch (ConnectException var25) {
                    throw var25;
                }

                String requestedFile = input.substring(8, input.length());

                try {
                    Integer hash = Integer.parseInt(requestedFile) % 256;

                    Socket sendSocket;
                    try {
                        sendSocket = new Socket(IPAddress, '썐' + b);
                    } catch (ConnectException var23) {
                        sendSocket = new Socket(IPAddress, '썐' + c);
                    }

                    outToServer = new DataOutputStream(sendSocket.getOutputStream());
                    sentence = "REQUEST " + requestedFile + " " + a + " " + a;
                    outToServer.writeBytes(sentence + "\n");
                    sendSocket.close();
                    System.out.println("File request message for " + requestedFile + " has been sent to my successor. File hash is " + hash + ".");
                } catch (NumberFormatException var24) {
                    System.out.println("Filename is not numeric.");
                }

                if (!a) {
                    continue;
                }
            }

            System.out.println("Unknown command : " + input);
            System.out.println("Syntax : QUIT or REQUEST <wxyz>");
            if (a) {
                break;
            }
        }

    }
}
