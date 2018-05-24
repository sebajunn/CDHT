package cdht;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.*;

public class cdht {
    /** This node. */
    public static int peerID;
    /** First successor. */
    public static int firstPeer;
    /** Second successor. */
    public static int secondPeer;
    /** First preceeding peer. */
    public static int firstPredecessor;
    /** Second preceeding peer. */
    public static int secondPredecessor;
    /** Next sequence number to be sent. */
    public static int seqNum;
    /** Last sequence number acknowledged by first successor. */
    public static int firstAck;
    /** Last sequence number acknowledged by second successor. */
    public static int secondAck;
    /** Number of consecutive pings not ackd by first successor. */
    public static int firstLostPings;
    /** Number of consecutive pings not ackd by second successor. */
    public static int secondLostPings;
    /** Time between pings in ms. */
    public static final int PING_TIMING = 30000;
    /** Time before the first ping in ms. */
    public static final int FIRST_PING = 1000;
    /** Number of lost pings before successor is considered dead. */
    public static final int MAX_LOST_PINGS = 4;
    /** Timeout before ping is considered lost in ms. */
    public static final int PING_TIMEOUT = 1000;
    public static final String IP_ADDRESS = "127.0.0.1";
    /** NESTED CLASS FOR RUNNING A TCP SERVER. */










    static class TCPServerThread extends Thread {
        @Override
        public void run() {
            // Create a welcoming socket to listen for connections.
            ServerSocket welcomeSocket;
            welcomeSocket = null;
            try {
                welcomeSocket = new ServerSocket(50000 + peerID);
            } catch (IOException ex) {
                System.out.println(ex);
            }

            do {
                try {
                    // Wait for clients to connect.
                    Socket connectionSocket;
                    connectionSocket = welcomeSocket.accept();
                    BufferedReader inFromClient;
                    inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                    String incomingSentence;
                    incomingSentence = inFromClient.readLine();
                    // Parse the received sentence.
                    String[] parsedSentence;
                    parsedSentence = incomingSentence.split(" ");

                    // 4 commands handled are : FILE, LIST, QUIT, REQUEST

                    // Requested file has been received.
                    switch (parsedSentence[0]) {
                        case "FILE":
                            System.out.printf("Received a response message from peer %s, which has the file %s.%n", parsedSentence[2], parsedSentence[1]);

                            // A request for my successor peers.
                            break;
                        case "LIST": {
                            DataOutputStream output;
                            output = new DataOutputStream(connectionSocket.getOutputStream());
                            output.writeBytes(String.format("PEERS %d %d\n", firstPeer, secondPeer));

                            // A quit message has been received, a successor or predecessor is leaving.
                            break;
                        }
                        case "QUIT": {
                            int departingPeer;
                            departingPeer = Integer.parseInt(parsedSentence[3]);
                            System.out.printf("Peer %d will depart from the network.%n", departingPeer);
                            DataOutputStream output;
                            output = new DataOutputStream(connectionSocket.getOutputStream());
                            output.writeBytes(String.format("QUIT_RECEIVED\n"));

                            if (departingPeer != secondPeer) {
                                if (departingPeer != firstPeer && departingPeer != secondPeer) {
                                    if (departingPeer == firstPredecessor || departingPeer == secondPredecessor) {
                                        if (departingPeer == firstPredecessor) {
                                            firstPredecessor = secondPredecessor;   // secondPredeccessor is promoted to first
                                            secondPredecessor = Integer.parseInt(parsedSentence[2]); // new second predecessor is departing peer's second predecessor
                                        } else {                    // departingPeer is secondPredecessor
                                            secondPredecessor = Integer.parseInt(parsedSentence[1]); // new second predecessor is departing peer's first predecessor
                                        }
                                        System.out.printf("My first predecessor is now peer %d.%n", firstPredecessor);
                                        System.out.printf("My second predecessor is now peer %d.%n", secondPredecessor);
                                    }
                                } else {
                                    if (departingPeer != firstPeer) {                        // departingPeer is secondPeer
                                        secondPeer = Integer.parseInt(parsedSentence[1]);  // new second successor is departing peer's first successor
                                    } else {
                                        firstPeer = secondPeer;     // secondPeer is promoted to first
                                        secondPeer = Integer.parseInt(parsedSentence[2]);  // new second successor is departing peer's second successor
                                    }
                                    System.out.printf("My first successor is now peer %d.%n", firstPeer);
                                    System.out.printf("My second successor is now peer %d.%n", secondPeer);
                                    // A predecessor has left.
                                }
                            } else {
                                if (departingPeer != secondPredecessor) {
                                    if (!(departingPeer != firstPeer && departingPeer != secondPeer)) {
                                        if (departingPeer == firstPeer) {
                                            firstPeer = secondPeer;     // secondPeer is promoted to first
                                            secondPeer = Integer.parseInt(parsedSentence[2]);  // new second successor is departing peer's second successor
                                        } else {                        // departingPeer is secondPeer
                                            secondPeer = Integer.parseInt(parsedSentence[1]);  // new second successor is departing peer's first successor
                                        }
                                        System.out.printf("My first successor is now peer %d.%n", firstPeer);
                                        System.out.printf("My second successor is now peer %d.%n", secondPeer);
                                        // A predecessor has left.
                                    } else {
                                        if (!(departingPeer != firstPredecessor && departingPeer != secondPredecessor)) {
                                            if (departingPeer == firstPredecessor) {
                                                firstPredecessor = secondPredecessor;   // secondPredeccessor is promoted to first
                                                secondPredecessor = Integer.parseInt(parsedSentence[2]); // new second predecessor is departing peer's second predecessor
                                            } else {                    // departingPeer is secondPredecessor
                                                secondPredecessor = Integer.parseInt(parsedSentence[1]); // new second predecessor is departing peer's first predecessor
                                            }
                                            System.out.printf("My first predecessor is now peer %d.%n", firstPredecessor);
                                            System.out.printf("My second predecessor is now peer %d.%n", secondPredecessor);
                                        }
                                    }
                                } else { // overlap between successors and predecessors
                                    secondPeer = firstPredecessor;
                                    secondPredecessor = firstPeer;
                                    System.out.printf("My first successor is now peer %d.%n", firstPeer);
                                    System.out.printf("My second successor is now peer %d.%n", secondPeer);
                                    System.out.printf("My first predecessor is now peer %d.%n", firstPredecessor);
                                    System.out.printf("My second predecessor is now peer %d.%n", secondPredecessor);
                                    // A successor has left.
                                }
                            }
                            // File request message has been received.
                            break;
                        }
                        case "REQUEST":
                            String fileRequested;
                            fileRequested = parsedSentence[1];
                            String sender;
                            sender = parsedSentence[2];
                            String requester;
                            requester = parsedSentence[3];
                            int hashFile;
                            hashFile = Integer.parseInt(fileRequested) % 256;

                            // File is stored here, reply to requesting peer.
                            if (!((((peerID - Integer.parseInt(sender)) + 256) % 256) > (((peerID - hashFile) + 256) % 256))) {
                                Socket sendSocket;
                                try {
                                    sendSocket = new Socket(InetAddress.getByName(IP_ADDRESS), 50000 + firstPeer);
                                } catch (ConnectException e) { // fallback to secondPeer if cannot connect to first
                                    sendSocket = new Socket(InetAddress.getByName(IP_ADDRESS), 50000 + secondPeer);
                                }
                                DataOutputStream output;
                                output = new DataOutputStream(sendSocket.getOutputStream());
                                output.writeBytes(String.format("REQUEST %s %d %s\n", fileRequested, peerID, requester));
                                System.out.printf("File %s is not stored here.%n", fileRequested);
                                System.out.println("File request message has been forwarded to my successor.");
                                sendSocket.close();
                            } else {
                                System.out.printf("File %s is here.%n", fileRequested);
                                System.out.printf("A response message, destined for peer %s, has been sent.%n", requester);
                                Socket sendSocket;
                                sendSocket = new Socket(InetAddress.getByName(IP_ADDRESS), 50000 + Integer.parseInt(requester));
                                DataOutputStream output;
                                output = new DataOutputStream(sendSocket.getOutputStream());
                                String sentence;
                                sentence = "FILE " + fileRequested + " " + peerID;
                                output.writeBytes(sentence + "\n");
                                sendSocket.close();
                                // File is not stored here, forward to successor.
                            }
                            break;
                    }
                    connectionSocket.close();
                } catch (UnknownHostException ex) {
                    System.out.println(ex);
                } catch (IOException ex) {
                    System.out.println(ex);
                }
            } while (true);
        }
    }












    /** NESTED CLASS FOR RUNNING A UDP SERVER. */
    static class UDPServerThread extends Thread {
        DatagramSocket socket;

        public UDPServerThread(DatagramSocket ds) {
            this.socket = ds;
        }

        @Override
        public void run() {
            byte[] buffer = new byte[1024];
            do {
                DatagramPacket dp;
                dp = new DatagramPacket(buffer, buffer.length);
                try {
                    // Wait for clients to connect
                    socket.receive(dp);
                    int clientPort;
                    clientPort = dp.getPort();
                    // Parse the received sentence.
                    String[] parsedSentence;
                    parsedSentence = dataToString(dp).split(" ");
                    // 2 commands handled are : REPLY, PING
                    if (!"REPLY".equals(parsedSentence[0])) {
                    } else {
                        System.out.println("A ping response message was received from Peer " + (clientPort - 50000) + ".");
                        // Record the latest sequence number that each successor has acknowledged.
                        if ((clientPort - 50000) != firstPeer) {
                            secondAck = Integer.parseInt(parsedSentence[1]);
                        } else {
                            firstAck = Integer.parseInt(parsedSentence[1]);
                        }
                    }
                    if (!"PING".equals(parsedSentence[0])) {
                    } else {
                        System.out.printf("A ping request message was received from Peer %d.%n", clientPort - 50000);
                        updatePredecessors(clientPort - 50000);
                        byte[] buf;  // reply with same sequence number as incoming ping
                        buf = (String.format("REPLY %s\n", parsedSentence[1])).getBytes();
                        DatagramPacket reply;
                        reply = new DatagramPacket(buf, buf.length, dp.getAddress(), clientPort);
                        socket.send(reply);
                    }
                } catch (Exception ex) {
                    System.err.println(ex);
                }
            } while (true);
        }
        /** Method to convert incoming data to a String. */
        private String dataToString(DatagramPacket request) throws Exception {
            byte[] buf;
            buf = request.getData();
            ByteArrayInputStream bais;
            bais = new ByteArrayInputStream(buf);
            InputStreamReader isr;
            isr = new InputStreamReader(bais);
            BufferedReader br;
            br = new BufferedReader(isr);
            return br.readLine();
        }

        private void updatePredecessors (int newPeer) {
            // If node does not know any predecessors (startup) then set firstPredecessor to be newPeer.
            if (!(firstPredecessor != 0)) {
                if (secondPredecessor != 0) {
                } else {
                    firstPredecessor = newPeer;
                    return;
                }
            }
            // If node only has one predecessor check whether newPeer becomes first or second predecessor.
            if (secondPredecessor == 0)
                if (firstPredecessor != 0) {
                    if (!((peerID - newPeer + 256) % 256 <= (peerID - firstPredecessor + 256) % 256)) {
                        secondPredecessor = newPeer;
                    } else {
                        secondPredecessor = firstPredecessor;
                        firstPredecessor = newPeer;
                    }
                    return;
                }
            // If newPeer is different from current predecessors then one of them has left ungracefully.
            // Delete both current peers and set firstPredecessor to be newPeer, as for startup.
            if (!(newPeer == firstPredecessor))
                if (newPeer == secondPredecessor) {
                    return;
                }
                firstPredecessor = newPeer;
                secondPredecessor = 0;
        }
    }








    /** NESTED CLASS FOR PINGING OVER UDP. */
    static class PingTask extends TimerTask {
        DatagramSocket socket;

        PingTask(DatagramSocket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            // System.out.println("Second pred :" + secondPredecessor + " First pred :" + firstPredecessor + " First succ:" + firstPeer + " Second succ:" + secondPeer);  // TESTING
            String sentence;
            sentence = String.format("PING %d\n", seqNum);
            seqNum = (seqNum + 1) % 65536;   // increment sequence number
            byte[] sendData;
            sendData = sentence.getBytes();
            try {
                DatagramPacket sendPacket;
                sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(IP_ADDRESS), firstPeer + 50000);
                socket.send(sendPacket);
                sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(IP_ADDRESS), secondPeer + 50000);
                socket.send(sendPacket);
                Thread.sleep(PING_TIMEOUT);     // wait for acknowledgements
                if (!(firstAck != seqNum - 1)) {
                    firstLostPings = 0;
                } else {
                    firstLostPings++;
                }
                if (!(secondAck != seqNum - 1)) {
                    secondLostPings = 0;
                } else {
                    secondLostPings++;
                }

                // a peer has left
                if (firstLostPings < MAX_LOST_PINGS && secondLostPings < MAX_LOST_PINGS) {
                } else {
                    int deadPeer;
                    deadPeer = firstPeer;
                    int livePeer;
                    livePeer = secondPeer;
                    if (MAX_LOST_PINGS > secondLostPings) {
                    } else {
                        deadPeer = secondPeer;
                        livePeer = firstPeer;
                    }
                    System.out.printf("Peer %d is no longer alive.%n", deadPeer);
                    // Request successors from remaining live peer
                    Socket sendSocket;
                    sendSocket = new Socket(InetAddress.getByName(IP_ADDRESS), 50000 + livePeer);
                    DataOutputStream output;
                    output = new DataOutputStream(sendSocket.getOutputStream());
                    output.writeBytes("LIST\n");
                    BufferedReader inFromServer;
                    inFromServer = new BufferedReader(new InputStreamReader(sendSocket.getInputStream()));
                    // Wait for reply with successors of livePeer
                    String replyFromServerSentence;
                    replyFromServerSentence = inFromServer.readLine();
                    String[] parsedSentence = replyFromServerSentence.split(" ");
                    // Update my successors
                    if (deadPeer != firstPeer) {
                        if (!(Integer.parseInt(parsedSentence[1]) == deadPeer)) {
                            secondPeer = Integer.parseInt(parsedSentence[1]);
                        } else {
                            secondPeer = Integer.parseInt(parsedSentence[2]);
                        }
                    }  // If firstPeer has not yet updated successors then
                    // secondPeer is secondPeer from firstPeer
                    else {
                        firstPeer = secondPeer;
                        secondPeer = Integer.parseInt(parsedSentence[1]);
                    }

                    System.out.printf("My first successor is now peer %d.%n", firstPeer);
                    System.out.printf("My second successor is now peer %d.%n", secondPeer);
                    sendSocket.close();
                }
            } catch (IOException | InterruptedException ex) {
                System.out.println(ex);
            }
        }
    }















    public static void main(String[] args) throws Exception {
        peerID = Integer.parseInt(args[0]);
        firstPeer = Integer.parseInt(args[1]);
        secondPeer = Integer.parseInt(args[2]);
        InetAddress IPAddress;
        IPAddress = InetAddress.getByName(IP_ADDRESS);
        seqNum = new Random().nextInt(65536);
        firstAck = secondAck = seqNum;
        firstLostPings = secondLostPings = 0;
        // Creating pinging timer task, timer, schedule
        DatagramSocket socket;  // Datagram socket for UDP packets through port 50000 + peerID.
        socket = new DatagramSocket(50000 + peerID);
        PingTask ping;
        ping = new PingTask(socket);
        Timer timer;
        timer = new Timer();
        timer.schedule(ping, FIRST_PING, PING_TIMING);

        // Create thread for the UDP server
        UDPServerThread UDPThread;
        UDPThread = new UDPServerThread(socket);
        UDPThread.start();
        // Create thread for TCP server
        TCPServerThread TCPThread;
        TCPThread = new TCPServerThread();
        TCPThread.start();
        // Read the user input
        Scanner s;
        s = new Scanner(System.in);
        String input;
        if (!(input = s.nextLine()).isEmpty()) {
            do {
                if (!"quit".equalsIgnoreCase(input)) {
                    if (input.length() != 12 || !"request".equalsIgnoreCase(input.substring(0, 7))) {
                        System.out.println("Unknown command : " + input);
                        System.out.println("Syntax : QUIT or REQUEST <wxyz>");
                    } else {
                        String requestedFile = input.substring(8, input.length());
                        Integer hash;
                        try {
                            hash = Integer.parseInt(requestedFile) % 256;
                            Socket sendSocket;
                            try {
                                sendSocket = new Socket(IPAddress, 50000 + firstPeer);
                            } catch (ConnectException e) {  // fallback to secondPeer if cannot connect to first
                                sendSocket = new Socket(IPAddress, 50000 + secondPeer);
                            }
                            DataOutputStream outToServer = new DataOutputStream(sendSocket.getOutputStream());
                            String sentence;
                            sentence = String.format("REQUEST %s %d %d", requestedFile, peerID, peerID);
                            outToServer.writeBytes(sentence + "\n");
                            sendSocket.close();
                            System.out.println("File request message for " + requestedFile + " has been sent to my successor. File hash is " + hash + ".");
                        } catch (NumberFormatException ex) {
                            System.out.println("Filename is not numeric.");
                        }
                    }
                } else {
                    // Send quit message over TCP to both successors
                    Socket sendSocketFirst;
                    sendSocketFirst = new Socket(IPAddress, 50000 + firstPeer);
                    Socket sendSocketSecond;
                    sendSocketSecond = new Socket(IPAddress, 50000 + secondPeer);
                    DataOutputStream outToServerFirst;
                    outToServerFirst = new DataOutputStream(sendSocketFirst.getOutputStream());
                    DataOutputStream outToServerSecond;
                    outToServerSecond = new DataOutputStream(sendSocketSecond.getOutputStream());
                    String quitMessage;
                    quitMessage = String.format("QUIT %d %d %d", firstPredecessor, secondPredecessor, peerID);
                    outToServerFirst.writeBytes(quitMessage + "\n");
                    outToServerSecond.writeBytes(quitMessage + "\n");
                    BufferedReader inFromFirst;
                    inFromFirst = new BufferedReader(new InputStreamReader(sendSocketFirst.getInputStream()));
                    BufferedReader inFromSecond;
                    inFromSecond = new BufferedReader(new InputStreamReader(sendSocketSecond.getInputStream()));
                    inFromFirst.readLine();
                    inFromSecond.readLine();
                    sendSocketFirst.close();
                    sendSocketSecond.close();
                    // Send quit message over TCP to both predecessors
                    Socket sendSocketA;
                    sendSocketA = new Socket(IPAddress, 50000 + firstPredecessor);
                    DataOutputStream outToServerA = new DataOutputStream(sendSocketA.getOutputStream());
                    quitMessage = String.format("QUIT %d %d %d", firstPeer, secondPeer, peerID);
                    outToServerA.writeBytes(String.format("%s\n", quitMessage));
                    BufferedReader inFromA;
                    inFromA = new BufferedReader(new InputStreamReader(sendSocketA.getInputStream()));
                    inFromA.readLine();
                    sendSocketA.close();
                    if (secondPredecessor == secondPeer) {
                    } else { // if secondPredecessor = secondPeer the message already sent to secondPeer will suffice
                        Socket sendSocketB;
                        sendSocketB = new Socket(IPAddress, 50000 + secondPredecessor);
                        DataOutputStream outToServerB;
                        outToServerB = new DataOutputStream(sendSocketB.getOutputStream());
                        outToServerB.writeBytes(String.format("%s\n", quitMessage));
                        BufferedReader inFromB;
                        inFromB = new BufferedReader(new InputStreamReader(sendSocketB.getInputStream()));
                        inFromB.readLine();
                        sendSocketB.close();
                    }
                    System.out.printf("Peer %d has left the network.%n", peerID);
                    System.exit(0);
                    // Send file request message over TCP
                }
            } while (!(input = s.nextLine()).isEmpty());
        }
    }
}
