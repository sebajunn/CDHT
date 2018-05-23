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

public class a
{
  public static int a;
  public static int b;
  public static int c;
  public static int d;
  public static int e;
  public static int f;
  public static int g;
  public static int h;
  public static int i;
  public static int j;
  public static final int k = 30000;
  public static final int l = 1000;
  public static final int m = 4;
  public static final int n = 1000;
  public static final String o;
  public static boolean p;
  private static final String[] q;
  
  public static void main(String[] paramArrayOfString)
    throws Exception
  {
    a = Integer.parseInt(paramArrayOfString[0]);
    boolean bool = c.b;
    b = Integer.parseInt(paramArrayOfString[1]);
    c = Integer.parseInt(paramArrayOfString[2]);
    String[] arrayOfString = q;
    InetAddress localInetAddress = InetAddress.getByName(arrayOfString[10]);
    f = new Random().nextInt(65536);
    g = h = f;
    i = j = 0;
    DatagramSocket localDatagramSocket = new DatagramSocket(50000 + a);
    d locald = new d(localDatagramSocket);
    Timer localTimer = new Timer();
    localTimer.schedule(locald, 1000L, 30000L);
    c localc = new c(localDatagramSocket);
    localc.start();
    b localb = new b();
    localb.start();
    Scanner localScanner = new Scanner(System.in);
    do
    {
      String str1;
      do
      {
        Object localObject2;
        Object localObject3;
        String str2;
        do
        {
          if ((str1 = localScanner.nextLine()).isEmpty()) {
            return;
          }
          arrayOfString = q;
          if (!str1.equalsIgnoreCase(arrayOfString[1])) {
            break;
          }
          localObject1 = new Socket(localInetAddress, 50000 + b);
          localObject2 = new Socket(localInetAddress, 50000 + c);
          localObject3 = new DataOutputStream(((Socket)localObject1).getOutputStream());
          DataOutputStream localDataOutputStream1 = new DataOutputStream(((Socket)localObject2).getOutputStream());
          str2 = arrayOfString[11] + d + " " + e + " " + a;
          ((DataOutputStream)localObject3).writeBytes(str2 + "\n");
          localDataOutputStream1.writeBytes(str2 + "\n");
          BufferedReader localBufferedReader1 = new BufferedReader(new InputStreamReader(((Socket)localObject1).getInputStream()));
          BufferedReader localBufferedReader2 = new BufferedReader(new InputStreamReader(((Socket)localObject2).getInputStream()));
          localBufferedReader1.readLine();
          localBufferedReader2.readLine();
          ((Socket)localObject1).close();
          ((Socket)localObject2).close();
          Socket localSocket1 = new Socket(localInetAddress, 50000 + d);
          DataOutputStream localDataOutputStream3 = new DataOutputStream(localSocket1.getOutputStream());
          str2 = arrayOfString[4] + b + " " + c + " " + a;
          localDataOutputStream3.writeBytes(str2 + "\n");
          BufferedReader localBufferedReader3 = new BufferedReader(new InputStreamReader(localSocket1.getInputStream()));
          localBufferedReader3.readLine();
          localSocket1.close();
          if (e != c)
          {
            Socket localSocket2 = new Socket(localInetAddress, 50000 + e);
            DataOutputStream localDataOutputStream4 = new DataOutputStream(localSocket2.getOutputStream());
            localDataOutputStream4.writeBytes(str2 + "\n");
            BufferedReader localBufferedReader4 = new BufferedReader(new InputStreamReader(localSocket2.getInputStream()));
            localBufferedReader4.readLine();
            localSocket2.close();
          }
          arrayOfString = q;
          System.out.println(arrayOfString[7] + a + arrayOfString[0]);
          System.exit(0);
        } while (!bool);
        try
        {
          if (p) {
            tmpTernaryOp = false;
          }
        }
        catch (ConnectException localConnectException2)
        {
          throw localConnectException2;
        }
        p = true;
        try
        {
          if ((str1.length() != 12) || (!str1.substring(0, 7).equalsIgnoreCase(q[8]))) {
            break;
          }
        }
        catch (ConnectException localConnectException3)
        {
          throw localConnectException3;
        }
        Object localObject1 = str1.substring(8, str1.length());
        try
        {
          localObject2 = Integer.valueOf(Integer.parseInt((String)localObject1) % 256);
          try
          {
            localObject3 = new Socket(localInetAddress, 50000 + b);
          }
          catch (ConnectException localConnectException1)
          {
            localObject3 = new Socket(localInetAddress, 50000 + c);
          }
          DataOutputStream localDataOutputStream2 = new DataOutputStream(((Socket)localObject3).getOutputStream());
          arrayOfString = q;
          str2 = arrayOfString[3] + (String)localObject1 + " " + a + " " + a;
          localDataOutputStream2.writeBytes(str2 + "\n");
          ((Socket)localObject3).close();
          System.out.println(arrayOfString[13] + (String)localObject1 + arrayOfString[6] + localObject2 + ".");
        }
        catch (NumberFormatException localNumberFormatException)
        {
          arrayOfString = q;
          System.out.println(arrayOfString[5]);
        }
      } while (!bool);
      arrayOfString = q;
      System.out.println(arrayOfString[12] + str1);
      System.out.println(arrayOfString[9]);
    } while (!bool);
  }
}


/* Location:              D:\COMP3331\obf\First\cdht.jar!\a.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */