import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;

// import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JFrame;
// import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;


public class ServerFrame extends JFrame{
    
    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                ServerFrame frame = new ServerFrame();
                Server.SocketChecker socketChecker = frame.server.new SocketChecker();
                socketChecker.start();
                Server.InChecker inChecker = frame.server.new InChecker();
                inChecker.start();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        });
    }

    private static String frameTitle = "Сервер";

    private static int frameMinimumWidth = 500;
    private static int frameMinimumHeight = 500;

    private static int serverPort = 8888;

    private static int smallGap = 5;
    private static int mediumGap = 10;
    private static int largeGap = 15;

    private JTextArea textAreaIncoming;
    private Server server;

    public ServerFrame(){
        super(frameTitle);
        server = new Server(serverPort, this);
        setMinimumSize(new Dimension(frameMinimumWidth, frameMinimumHeight));

        final Toolkit kit = Toolkit.getDefaultToolkit();

        setLocation((kit.getScreenSize().width - getWidth()) / 2, (kit.getScreenSize().height - getHeight()) / 2);
        
        textAreaIncoming = new JTextArea(10, 0);
        textAreaIncoming.setEditable(false);

        final JScrollPane scrollPaneIncoming = new JScrollPane(textAreaIncoming);
        // final JPanel messagePanel = new JPanel();
        // messagePanel.setBorder(BorderFactory.createTitledBorder("Сообщение"));

        GroupLayout layout1 = new GroupLayout(getContentPane());
        setLayout(layout1);

        layout1.setHorizontalGroup(layout1.createSequentialGroup()
            .addContainerGap()
            .addGroup(layout1.createParallelGroup()
                .addComponent(scrollPaneIncoming)
                // .addGap(10)
                // .addComponent(messagePanel)
            )
            .addContainerGap()
        );

        layout1.setVerticalGroup(layout1.createSequentialGroup()
            .addContainerGap()
            .addComponent(scrollPaneIncoming)
            .addGap(10)
            // .addComponent(messagePanel)
            // .addContainerGap()
        );
    }

    public class Server {

        public ServerFrame frame;
        
        public final int PORT;

        public static LinkedList<ClientObject> clients = new LinkedList<>();

        public static HashMap<String, String> clientsPasswords = new HashMap<>(){{
            put("Richi", "1111");
            put("Tom", "2222");
        }};

        public Server(int PORT, ServerFrame frame){
            this.frame = frame;
            this.PORT =PORT;
        }

        // public static void main(String[] args) throws IOException{
        //     SocketChecker socketChecker = new SocketChecker();
        //     socketChecker.start();
        //     InChecker inChecker = new InChecker();
        //     inChecker.start();
        // }

        public class SocketChecker extends Thread{
            public void run(){
                try{
                    ServerSocket s = new ServerSocket(PORT);
                    frame.textAreaIncoming.append("Server started: " + s);
                    try{
                        while(true){
                            Socket socket = s.accept();
                            ClientObject newClient = new ClientObject(socket);
                            // authoriazation(newClient);
                            clients.add(newClient);
                            
                            frame.textAreaIncoming.append(clients.getLast().getName() + " has joined");
                            
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

        public class InChecker extends Thread{
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
                                    // System.out.println(1);
                                    clients.remove(i);
                                    // System.out.println(2);
                                    client.closeSocket();
                                    // System.out.println(3);
                                    frame.textAreaIncoming.append(client.getName() + " disconnected");
                                    // System.out.println(4);
                                    continue;
                                }
                                frame.textAreaIncoming.append(client.getName() + ": " + str);
                            }
                        }
                        Thread.sleep(80);
                    }
                }catch(InterruptedException e){

                }catch(IOException e){

                }
            }
        }

        public void authoriazation(ClientObject client) throws IOException{
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

    // public static void authoriazation(CleintObject client) throws IOException{
    //     String name = null;
    //     first: while(true){
    //         client.getOut().println("Enter your name to authorize: ");
    //         name = client.getIn().readLine();
    //         if(clientsPasswords.containsKey())
    //     }
    // }
}
