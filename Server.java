import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    
    public static final int PORT = 8888;

    public static ArrayList<ClientObject> clients = new ArrayList<>();

    public static void main(String[] args) throws IOException{
        SocketChecker socketChecker = new SocketChecker();
        socketChecker.start();
        InChecker inChecker = new InChecker();
        inChecker.start();
        // ServerSocket s = new ServerSocket(PORT);
        // System.out.println("Started: " + s);

        // try{
        //     Socket socket = s.accept();

        //     try{
        //         System.out.println("Connection accepted: " + socket);
        //         BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        //         PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

        //         while(true){
        //             String str = in.readLine();
        //             if(str.equals("END")) break;
        //             // System.out.println("Echoing: " + str);
        //             // out.println(str);
        //         }
        //     } finally {
        //         System.out.println("closing...");
        //         socket.close();
        //     }
        // } finally{
        //     s.close();
        // }
    }

    public static class SocketChecker extends Thread{
        public void run(){
            try{
                ServerSocket s = new ServerSocket(PORT);
                System.out.println("Server started: " + s);
                try{
                    while(true){
                        Socket socket = s.accept();
                        clients.add(new ClientObject(socket));
                        System.out.println("client " + clients.get(clients.size() - 1).getId() + " has joined");
                        
                        Thread.sleep(100);
                    }
                }catch(InterruptedException e){

                }finally{
                    s.close();
                }
            }
            catch(IOException e){

            }
        }
    }

    public static class InChecker extends Thread{
        public void run(){
            try{                
                String str = null;
                while(true){
                    for(ClientObject client : clients){
                        if(client.getIn().ready()){
                            str = client.getIn().readLine();
                            if(str.equals("END")){
                                client.closeSocket();
                                clients.remove(client);
                                System.out.println("client " + client.getId() + " disconnected");
                                continue;
                            }
                            System.out.println("client " + client.getId() + ": " + str);

                        }
                    }
                    Thread.sleep(50);
                }
            }catch(InterruptedException e){

            }catch(IOException e){

            }
        }
    }
}
