import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class ConsoleClient{
    // private static String clientName = null;
    public static void main(String[] args)  throws IOException {
        // System.out.print("Enter your name: ");
        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
        // clientName = consoleReader.readLine();

        InetAddress addr = InetAddress.getByName(null);

        System.out.println("addr = " + addr);
        Socket socket = new Socket(addr, ConsoleServer.PORT);

        try(BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        ){
            System.out.println("socket = " + socket);

            aothorization(consoleReader, in, out);
            String message = null;
            while(true){
                message = consoleReader.readLine();
                if(message.equals("END")) break;
                out.println(message);
            }

            out.println("END");
        }catch(IOException e){
            System.out.println("ИО ошибка");
        }finally{
            System.out.println("closing...");
            socket.close();
        }
        consoleReader.close();
    }

    public static void aothorization(BufferedReader consoleReader, BufferedReader in, PrintWriter out) throws IOException{
        String message = null;
            String str =  null;
            do{
                str = in.readLine();
                System.out.println(str);
                message = consoleReader.readLine();
                out.println(message);
                str = in.readLine();
                System.out.println(str);
            }while(str.equals("A user with this name does not exist"));
            do{
                message = consoleReader.readLine();
                out.println(message);
                str = in.readLine();
                System.out.println(str);
            }while(str.equals("You entered wrong password. Enter password: "));

    }
}