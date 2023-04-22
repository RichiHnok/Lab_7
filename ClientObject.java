import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientObject {

    private String name;
    private static int amount = 0;
    private int id;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public ClientObject(Socket socket){
        this.socket = socket;
        try{
            in = new BufferedReader(
                new InputStreamReader(
                    socket.getInputStream()
                )
            );
            out = new PrintWriter(
                new BufferedWriter(
                    new OutputStreamWriter(
                        socket.getOutputStream()
                    )
                ), true
            );
            // name = in.readLine();
        }catch(IOException e){

        }
        amount++;
        id = amount;
    }

    public Socket getSocket(){
        return socket;
    }

    public BufferedReader getIn(){
        return in;
    }

    public PrintWriter getOut(){
        return out;
    }

    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public void closeSocket() throws IOException{
        socket.close();
    }
}
