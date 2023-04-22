import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Client{
    private static String clientName = null;
    public static void main(String[] args)  throws IOException {
        // System.out.print("Enter your name: ");
        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
        // clientName = consoleReader.readLine();
        

        InetAddress addr = InetAddress.getByName(null);

        System.out.println("addr = " + addr);
        Socket socket = new Socket(addr, Server.PORT);

        try(BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        ){
            System.out.println("socket = " + socket);
            // System.out.println(1);
            String message = null;
            // System.out.println(2);
            // while(true){
                String str =  null;
                // System.out.println(3);
                // System.out.println(4);
                do{
                    str = in.readLine();
                    System.out.println(str);
                    message = consoleReader.readLine();
                    // System.out.println(5);
                    out.println(message);
                    // System.out.println(6);
                    str = in.readLine();
                    // System.out.println(7);
                    System.out.println(str);
                    // System.out.println(8);
                }while(str.equals("A user with this name does not exist"));
                // System.out.println(9);
                // System.out.println(0);
                do{
                    // System.out.println(1);
                    message = consoleReader.readLine();
                    // System.out.println(2);
                    out.println(message);
                    // System.out.println(3);
                    str = in.readLine();
                    // System.out.println(4);
                    System.out.println(str);
                    // System.out.println(5);
                }while(str.equals("You entered wrong password. Enter password: "));
                // System.out.println(6);
                // if(str.equals("END")) break;
                // System.out.println(str);
            // }
            // out.println(clientName);
            while(true){
                message = consoleReader.readLine();
                if(message.equals("END")) break;
                out.println(message);
                // System.out.println(in.readLine());
            }
            // for(int i = 0; i < 10; i++){
            //     out.println("howdy " + i);
            //     String str = in.readLine();
            //     System.out.println(str);
            // }

            out.println("END");
        }catch(IOException e){
            System.out.println("ИО ошибка");
        }finally{
            System.out.println("closing...");
            socket.close();
        }
        consoleReader.close();
    }
}