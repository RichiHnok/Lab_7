// import java.io.BufferedReader;
// import java.io.BufferedWriter;
import java.io.IOException;
// import java.io.InputStreamReader;
// import java.io.OutputStreamWriter;
// import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
// import java.util.ArrayList;
import java.util.LinkedList;

public class ConsoleServer {
    
    public static final int PORT = 8888;

    public static LinkedList<ClientObject> clients = new LinkedList<>();

    public static HashMap<String, String> clientsPasswords = new HashMap<>(){{
        put("Richi", "1111");
        put("Tom", "2222");
    }};

    public static void main(String[] args) throws IOException{
        SocketChecker socketChecker = new SocketChecker();
        socketChecker.start();
        InChecker inChecker = new InChecker();
        inChecker.start();
    }

    public static class SocketChecker extends Thread{
        public void run(){
            try{
                ServerSocket s = new ServerSocket(PORT);
                System.out.println("Server started: " + s);
                try{
                    while(true){
                        Socket socket = s.accept();
                        ClientObject newClient = new ClientObject(socket);
                        authoriazation(newClient);
                        clients.add(newClient);
                        
                        System.out.println(clients.getLast().getName() + " has joined");
                        
                        Thread.sleep(300);
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
                    ClientObject client = null;
                    for(int i = 0, n = clients.size(); i < n; i++){
                        client = clients.get(i);
                        if(client.getIn().ready()){
                            str = client.getIn().readLine();
                            if(str.equals("END")){
                                System.out.println(1);
                                clients.remove(i);
                                System.out.println(2);
                                client.closeSocket();
                                System.out.println(3);
                                System.out.println(client.getName() + " disconnected");
                                System.out.println(4);
                                continue;
                            }
                            System.out.println(client.getName() + ": " + str);
                        }
                    }
                    Thread.sleep(80);
                }
            }catch(InterruptedException e){

            }catch(IOException e){

            }
        }
    }

    public static void authoriazation(ClientObject client) throws IOException{
        String name = null;
        first: while(true){
            client.getOut().println("Enter your name to autorize: ");
            name = client.getIn().readLine();
            if(clientsPasswords.containsKey(name)){
                String password = null;
                
                client.getOut().println("Enter password: ");
                do{
                    password = client.getIn().readLine();
                    if(clientsPasswords.get(name).equals(password)){
                        client.getOut().println("Authorization was successful. Welcome to chat");
                        client.setName(name);
                        break first;
                        
                    }else{
                        client.getOut().println("You entered wrong password. Enter password: ");
                    }
                }while(true);
            }else{
                client.getOut().println("A user with this name does not exist");
            }
        }
    }
}
