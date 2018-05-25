
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class cdht {

    public static int peerID;       // this node
    public static int firstPeer;    // first successor
    public static int secondPeer;   // second successor
    public static int firstPredecessor = 0; // first preceeding peer
    public static int secondPredecessor = 0; // second preceeding peer
    public static int seqNum;       // next sequence number to be sent
    public static int firstAck;     // last sequence number acknowledged by first successor
    public static int secondAck;    // last sequence number acknowledged by second successor
    public static int firstLostPings;   // number of consecutive pings not ackd by first successor
    public static int secondLostPings;  // number of consecutive pings not ackd by second successor
    
    public static final int PING_TIMING = 30000;    // time between pings in ms
    public static final int FIRST_PING = 1000;      // time before the first ping in ms
    public static final int MAX_LOST_PINGS = 4;     // number of lost pings before successor is considered dead
    public static final int PING_TIMEOUT = 1000;    // timeout before ping is considered lost in ms
    public static final String IP_ADDRESS = "127.0.0.1";
    
    // NESTED CLASS FOR RUNNING A TCP SERVER
    static class TCPServerThread extends Thread {
        
        @Override
        public void run() {        
            // Create a welcoming socket to listen for connections.
            ServerSocket welcomeSocket = null;
            try {
                welcomeSocket = new ServerSocket(50000 + peerID);
            } catch (IOException ex) {
                System.out.println(ex);
            }

            while (true) {
                try {
                    // Wait for clients to connect.
                    Socket connectionSocket = welcomeSocket.accept();
                    BufferedReader inFromClient = new BufferedReader(new 
                            InputStreamReader(connectionSocket.getInputStream()));
                    String incomingSentence = inFromClient.readLine();
                    // Parse the received sentence.
                    String[] parsedSentence = incomingSentence.split(" ");

                    // 4 commands handled are : FILE, LIST, QUIT, REQUEST

                    // Requested file has been received.
                    if (parsedSentence[0].equals("FILE")) {
                        System.out.println("Received a response message from peer " + parsedSentence[2] + 
                                ", which has the file " + parsedSentence[1] + ".");

                    // A request for my successor peers.
                    } else if (parsedSentence[0].equals("LIST")) {
                        DataOutputStream output = new DataOutputStream(connectionSocket.getOutputStream());
                        output.writeBytes("PEERS " + firstPeer + " " + secondPeer + "\n");

                    // A quit message has been received, a successor or predecessor is leaving.
                    } else if (parsedSentence[0].equals("QUIT")) {
                        int departingPeer = Integer.parseInt(parsedSentence[3]);
                        System.out.println("Peer " + departingPeer + " will depart from the network.");
                        DataOutputStream output = new DataOutputStream(connectionSocket.getOutputStream());                    
                        output.writeBytes("QUIT_RECEIVED" + "\n");

                        if (departingPeer == secondPeer && departingPeer == secondPredecessor) { // overlap between successors and predecessors
                            secondPeer = firstPredecessor;
                            secondPredecessor = firstPeer;
                            System.out.println("My first successor is now peer " + firstPeer + ".");
                            System.out.println("My second successor is now peer " + secondPeer + ".");
                            System.out.println("My first predecessor is now peer " + firstPredecessor + ".");
                            System.out.println("My second predecessor is now peer " + secondPredecessor + ".");
                        // A successor has left.
                        } else if (departingPeer == firstPeer || departingPeer == secondPeer) { 
                            if (departingPeer == firstPeer) {
                                firstPeer = secondPeer;     // secondPeer is promoted to first
                                secondPeer = Integer.parseInt(parsedSentence[2]);  // new second successor is departing peer's second successor
                            } else {                        // departingPeer is secondPeer
                                secondPeer = Integer.parseInt(parsedSentence[1]);  // new second successor is departing peer's first successor
                            }
                            System.out.println("My first successor is now peer " + firstPeer + ".");
                            System.out.println("My second successor is now peer " + secondPeer + ".");
                        // A predecessor has left.    
                        } else if (departingPeer == firstPredecessor || departingPeer == secondPredecessor) { 
                            if (departingPeer == firstPredecessor) {
                                firstPredecessor = secondPredecessor;   // secondPredeccessor is promoted to first
                                secondPredecessor = Integer.parseInt(parsedSentence[2]); // new second predecessor is departing peer's second predecessor
                            } else {                    // departingPeer is secondPredecessor
                                secondPredecessor = Integer.parseInt(parsedSentence[1]); // new second predecessor is departing peer's first predecessor
                            }
                            System.out.println("My first predecessor is now peer " + firstPredecessor + ".");
                            System.out.println("My second predecessor is now peer " + secondPredecessor + ".");                            
                        }
     
                    // File request message has been received.
                    } else if (parsedSentence[0].equals("REQUEST")) {
                        String fileRequested = parsedSentence[1];
                        String sender = parsedSentence[2];
                        String requester = parsedSentence[3];
                        int hashFile = Integer.parseInt(fileRequested) % 256;

                        // File is stored here, reply to requesting peer.
                        if ((peerID - Integer.parseInt(sender) + 256) % 256 > (peerID - hashFile + 256) % 256) {
                            System.out.println("File " + fileRequested + " is here.");
                            System.out.println("A response message, destined for peer " + requester + ", has been sent.");
                            Socket sendSocket = new Socket(InetAddress.getByName(IP_ADDRESS), 50000 + Integer.parseInt(requester));
                            DataOutputStream output = new DataOutputStream(sendSocket.getOutputStream());
                            String sentence = "FILE " + fileRequested + " " + peerID;
                            output.writeBytes(sentence + "\n");
                            sendSocket.close();
                        // File is not stored here, forward to successor.
                        } else {
                            Socket sendSocket;
                            try {
                                sendSocket = new Socket(InetAddress.getByName(IP_ADDRESS), 50000 + firstPeer);
                            } catch (ConnectException e){ // fallback to secondPeer if cannot connect to first
                                sendSocket = new Socket(InetAddress.getByName(IP_ADDRESS), 50000 + secondPeer);
                            }
                            DataOutputStream output = new DataOutputStream(sendSocket.getOutputStream());
                            output.writeBytes("REQUEST " + fileRequested + " " + peerID + " " + requester + "\n");
                            System.out.println("File " + fileRequested + " is not stored here.");
                            System.out.println("File request message has been forwarded to my successor.");
                            sendSocket.close();
                        }
                    }
                    connectionSocket.close();

                } catch (IOException ex) {
                    System.out.println(ex);
                }
            }        
        }    
    }
    
    // NESTED CLASS FOR RUNNING A UDP SERVER
    static class UDPServerThread extends Thread {
        DatagramSocket socket;

        public UDPServerThread(DatagramSocket ds) {
            this.socket = ds;
        }

        @Override
        public void run() {
            byte[] buffer = new byte[1024];
            while (true) {
                DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
                try {
                    // Wait for clients to connect
                    socket.receive(dp);
                    int clientPort = dp.getPort();
                    // Parse the received sentence.
                    String[] parsedSentence = dataToString(dp).split(" ");
                    
                    // 2 commands handled are : REPLY, PING
                    
                    if (parsedSentence[0].equals("REPLY")) {
                        System.out.println("A ping response message was received from Peer " + (clientPort - 50000) + ".");
                        // Record the latest sequence number that each successor has acknowledged.
                        if ((clientPort - 50000) == firstPeer)
                            firstAck = Integer.parseInt(parsedSentence[1]);
                        else
                            secondAck = Integer.parseInt(parsedSentence[1]);                           
                    }
                    if (parsedSentence[0].equals("PING")) {
                        System.out.println("A ping request message was received from Peer " + (clientPort - 50000) + ".");
                        updatePredecessors(clientPort - 50000);
                             
                        byte[] buf = ("REPLY " + parsedSentence[1] + "\n").getBytes();  // reply with same sequence number as incoming ping
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
    
        // Method to convert incoming data to a String.
        private String dataToString(DatagramPacket request) throws Exception {
            byte[] buf = request.getData();
            ByteArrayInputStream bais = new ByteArrayInputStream(buf);
            InputStreamReader isr = new InputStreamReader(bais);
            BufferedReader br = new BufferedReader(isr);
            return br.readLine();
        }

        private void updatePredecessors (int newPeer) {
            // If node does not know any predecessors (startup) then set firstPredecessor to be newPeer.
            if (firstPredecessor == 0 && secondPredecessor == 0) {
                firstPredecessor = newPeer;
                return;
            }
            // If node only has one predecessor check whether newPeer becomes first or second predecessor.
            if (secondPredecessor == 0 && firstPredecessor != 0) {
                if ((peerID - firstPredecessor + 256) % 256 < (peerID - newPeer + 256) % 256)
                    secondPredecessor = newPeer;
                else {
                    secondPredecessor = firstPredecessor;
                    firstPredecessor = newPeer;
                }
                return;
            }
            // If newPeer is different from current predecessors then one of them has left ungracefully.
            // Delete both current peers and set firstPredecessor to be newPeer, as for startup.
            if (newPeer != firstPredecessor && newPeer != secondPredecessor) {
                firstPredecessor = newPeer;
                secondPredecessor = 0;
            }
        }  
    } 
    
    
    // NESTED CLASS FOR PINGING OVER UDP
    static class PingTask extends TimerTask {
        DatagramSocket socket;

        PingTask(DatagramSocket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            // System.out.println("Second pred :" + secondPredecessor + " First pred :" + firstPredecessor + " First succ:" + firstPeer + " Second succ:" + secondPeer);  // TESTING
            byte[] sendData = new byte[1024];
            String sentence = "PING " + seqNum + "\n";
            seqNum = (seqNum + 1) % 65536;   // increment sequence number
            sendData = sentence.getBytes();
            try {                
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(IP_ADDRESS), firstPeer + 50000);
                socket.send(sendPacket);
                sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(IP_ADDRESS), secondPeer + 50000);            
                socket.send(sendPacket);
                Thread.sleep(PING_TIMEOUT);     // wait for acknowledgements
                if (firstAck != seqNum - 1)
                    ++firstLostPings;
                else
                    firstLostPings = 0;
                if (secondAck != seqNum - 1)
                    ++secondLostPings;
                else
                    secondLostPings = 0;

                // a peer has left
                if (firstLostPings >= MAX_LOST_PINGS || secondLostPings >= MAX_LOST_PINGS) {
                    int deadPeer = firstPeer;
                    int livePeer = secondPeer;                
                    if (secondLostPings >= MAX_LOST_PINGS) {
                        deadPeer = secondPeer;
                        livePeer = firstPeer;
                    }
                    System.out.println("Peer " + deadPeer + " is no longer alive.");
                    // Request successors from remaining live peer
                    Socket sendSocket = new Socket(InetAddress.getByName(IP_ADDRESS), 50000 + livePeer);
                    DataOutputStream output = new DataOutputStream(sendSocket.getOutputStream());
                    output.writeBytes("LIST" + "\n");
                    BufferedReader inFromServer = new BufferedReader(new 
                        InputStreamReader(sendSocket.getInputStream()));
                    // Wait for reply with successors of livePeer
                    String replyFromServerSentence = inFromServer.readLine();   
                    String[] parsedSentence = replyFromServerSentence.split(" ");
                    // Update my successors
                    if (deadPeer == firstPeer) {
                        firstPeer = secondPeer;
                        secondPeer = Integer.parseInt(parsedSentence[1]);
                    } else {
                        // If firstPeer has not yet updated successors then
                        // secondPeer is secondPeer from firstPeer
                        if (Integer.parseInt(parsedSentence[1]) == deadPeer)
                            secondPeer = Integer.parseInt(parsedSentence[2]);
                        // If firstPeer has updated successors to remove deadPeer
                        // secondPeer is firstPeer from firstPeer
                        else
                            secondPeer = Integer.parseInt(parsedSentence[1]);
                    }
                    System.out.println("My first successor is now peer " + firstPeer + ".");
                    System.out.println("My second successor is now peer " + secondPeer + ".");
                    sendSocket.close();
                }

            } catch (IOException ex) {
                System.out.println(ex);
            } catch (InterruptedException ex) {
                System.out.println(ex);
            }
        }
    }        






    
    public static void main(String[] args) throws Exception {
        
        peerID = Integer.parseInt(args[0]);
        firstPeer = Integer.parseInt(args[1]);
        secondPeer = Integer.parseInt(args[2]);
        InetAddress IPAddress = InetAddress.getByName(IP_ADDRESS);
        seqNum = (new Random()).nextInt(65536);
        firstAck = secondAck = seqNum;
        firstLostPings = secondLostPings = 0;
                        
        // Creating pinging timer task, timer, schedule
        DatagramSocket socket = new DatagramSocket(50000 + peerID);  // Datagram socket for UDP packets through port 50000 + peerID.
        PingTask ping = new PingTask(socket);
        Timer timer = new Timer();
        timer.schedule(ping, FIRST_PING, PING_TIMING);                 

        // Create thread for the UDP server
        UDPServerThread UDPThread = new UDPServerThread(socket);
        UDPThread.start();
        
        // Create thread for TCP server
        TCPServerThread TCPThread = new TCPServerThread();
        TCPThread.start();        
        
        // Read the user input
        Scanner s = new Scanner(System.in);
        String input;
        
        while (!(input = s.nextLine()).isEmpty()) {
            
            if (input.equalsIgnoreCase("quit")) {
                // Send quit message over TCP to both successors
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
                
                // Send quit message over TCP to both predecessors
                Socket sendSocketA = new Socket(IPAddress, 50000 + firstPredecessor);
                DataOutputStream outToServerA = new DataOutputStream(sendSocketA.getOutputStream());
                quitMessage = "QUIT " + firstPeer + " " + secondPeer + " " + peerID;
                outToServerA.writeBytes(quitMessage + "\n");
                BufferedReader inFromA = new BufferedReader(new InputStreamReader(sendSocketA.getInputStream()));
                inFromA.readLine();
                sendSocketA.close();   
                if (secondPredecessor != secondPeer) { // if secondPredecessor = secondPeer the message already sent to secondPeer will suffice
                    Socket sendSocketB = new Socket(IPAddress, 50000 + secondPredecessor);
                    DataOutputStream outToServerB = new DataOutputStream(sendSocketB.getOutputStream());
                    outToServerB.writeBytes(quitMessage + "\n");
                    BufferedReader inFromB = new BufferedReader(new InputStreamReader(sendSocketB.getInputStream()));
                    inFromB.readLine();
                    sendSocketB.close();                    
                }
                
                System.out.println("Peer " + peerID + " has left the network.");
                System.exit(0);
                
            // Send file request message over TCP
            } else if (input.length() == 12 && input.substring(0,7).equalsIgnoreCase("request")) {
                String requestedFile = input.substring(8, input.length());
                Integer hash;
                try {
                    hash = Integer.parseInt(requestedFile) % 256;
                    Socket sendSocket;
                    try {
                        sendSocket = new Socket(IPAddress, 50000 + firstPeer);
                    } catch (ConnectException e){  // fallback to secondPeer if cannot connect to first
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

            } else {
                System.out.println("Unknown command : " + input);
                System.out.println("Syntax : QUIT or REQUEST <wxyz>");
            }
        }
    }           
}



