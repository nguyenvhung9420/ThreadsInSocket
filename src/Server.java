// Java implementation of  Server side
// It contains two classes : Server and ClientHandler
// Save file as Server.java

import java.io.*;
import java.text.*;
import java.util.*;
import java.net.*;

// Server class
public class Server
{


    public static void main(String[] args) throws IOException
    {
        int numOfClient = 0;

        // server is listening on port 5056
        ServerSocket server = new ServerSocket(5056);

        // running infinite loop for getting client request
        while (true)
        {
            Socket client = null;

            try
            {
                // socket object to receive incoming client requests
                client = server.accept();
                numOfClient += 1;

                System.out.println("Client " + numOfClient + " is connected : " + client);

                //Get a new form to show:
                //gameForms.add(new MainFormUI());
                //gameForms.get(gameForms.size() - 1).setVisible(true);

                // obtaining input and out streams
                DataInputStream dis = new DataInputStream(client.getInputStream());
                DataOutputStream dos = new DataOutputStream(client.getOutputStream());

                //Send to client its port:
                dos.writeUTF(client.getPort() + "");

                System.out.println("Assigning new thread for this client");

                // create a new thread object
                Thread t = new ClientHandler(client, dis, dos);

                // Invoking the start() method
                t.start();

            }
            catch (Exception e){
                client.close();
                e.printStackTrace();
            }
        }
    }
}

// ClientHandler class
class ClientHandler extends Thread
{
    DateFormat fordate = new SimpleDateFormat("yyyy/MM/dd");
    DateFormat fortime = new SimpleDateFormat("hh:mm:ss");
    final DataInputStream dis;
    final DataOutputStream dos;
    final Socket s;

    MainFormUI gameForm = new MainFormUI();
    private Network networkHandle;

    // Constructor
    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos)
    {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
    }

    public void sendToServerToPublish(String messageToSend){
        try {
            dos.writeUTF(messageToSend);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run()
    {
        gameForm.setVisible(true);


        try {
            networkHandle = new Network(s.getPort());
        } catch (IOException e) {
            e.printStackTrace();
        }


        String received;
        String toreturn;
        while (true)
        {
            try {
                // Ask user what he wants
                dos.writeUTF("What do you want?[Date | Time]..\n"+
                        "Type Exit to terminate connection.");

                // receive the answer from client
                received = dis.readUTF();

                if(received.equals("Exit"))
                {
                    System.out.println("Client " + this.s + " sends exit...");
                    System.out.println("Closing this connection.");
                    this.s.close();
                    System.out.println("Connection closed");
                    break;
                } else {
                    System.out.println("The client sends: " + received);
                    gameForm.appendTextToForm(received);
                    sendToServerToPublish(received);

                    //String nachricht = networkHandle.receive();
                    //System.out.println("Received via Network: " + nachricht);
                }

                // creating Date object
                Date date = new Date();

                // write on output stream based on the
                // answer from the client
                switch (received) {

                    case "Date" :
                        toreturn = fordate.format(date);
                        dos.writeUTF(toreturn);
                        break;

                    case "Time" :
                        toreturn = fortime.format(date);
                        dos.writeUTF(toreturn);
                        break;

                    default:
                        dos.writeUTF("Invalid input");
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try
        {
            // closing resources
            this.dis.close();
            this.dos.close();

        }catch(IOException e){
            e.printStackTrace();
        }
    }
}

