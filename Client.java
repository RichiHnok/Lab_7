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
        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
        clientName = consoleReader.readLine();
        

        InetAddress addr = InetAddress.getByName(null);

        System.out.println("addr = " + addr);
        Socket socket = new Socket(addr, Server.PORT);

        try(BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        ){
            System.out.println("socket = " + socket);
            String message = null;
            while(true){
                message = consoleReader.readLine();
                if(message.equals("END")) break;
                out.println(clientName + ": " + message);
                System.out.println(in.readLine());
            }
            // for(int i = 0; i < 10; i++){
            //     out.println("howdy " + i);
            //     String str = in.readLine();
            //     System.out.println(str);
            // }

            out.println("END");
        }finally{
            System.out.println("closing...");
            socket.close();
        }
        consoleReader.close();
    }
}