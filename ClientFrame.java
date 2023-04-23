import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.GroupLayout.Alignment;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ClientFrame extends JFrame{
 
    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable() {
            public void run(){
                ClientFrame frame = new ClientFrame();
            }
        });
    }

    private static String frameTitle = "Клиент";

    private static int frameMinimumWidth = 500;
    private static int frameMinimumHeight = 500;

    private static int smallGap = 5;
    private static int mediumGap = 10;
    private static int largeGap = 15;

    private JTextArea textAreaIncoming;
    private JTextArea textAreaOutgoing;
    private Client client;

    public ClientFrame(){
        super(frameTitle);
        

        setMinimumSize(new Dimension(frameMinimumWidth, frameMinimumHeight));

        final Toolkit kit = Toolkit.getDefaultToolkit();

        setLocation((kit.getScreenSize().width - getWidth()) / 2, (kit.getScreenSize().height - getHeight()) / 2);
        
        textAreaIncoming = new JTextArea(10, 0);
        textAreaIncoming.setEditable(false);

        final JScrollPane scrollPaneIncoming = new JScrollPane(textAreaIncoming);

        textAreaOutgoing = new JTextArea(1, 0);

        client = new Client(this);

        final JScrollPane scrollPaneOutgoing = new JScrollPane(textAreaOutgoing);

        final JPanel messagePanel = new JPanel();

        messagePanel.setBorder(BorderFactory.createTitledBorder("Сообщение"));

        final JButton sendButton = new JButton("Отправить");
        sendButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                sendMessage();
            }
        });

        GroupLayout layout2 = new GroupLayout(messagePanel);
        messagePanel.setLayout(layout2);

        layout2.setHorizontalGroup(layout2.createSequentialGroup()
            .addContainerGap()
            .addGroup(layout2.createParallelGroup(Alignment.TRAILING)
                .addComponent(scrollPaneOutgoing)
                .addComponent(sendButton)
            )
        );

        layout2.setVerticalGroup(layout2.createSequentialGroup()
            .addContainerGap()
            .addGap(10)
            .addComponent(scrollPaneOutgoing)
            .addGap(10)
            .addComponent(sendButton)
            .addContainerGap()
        );

        GroupLayout layout1 = new GroupLayout(getContentPane());
        setLayout(layout1);

        layout1.setHorizontalGroup(layout1.createSequentialGroup()
            .addContainerGap()
            .addGroup(layout1.createParallelGroup()
                .addComponent(scrollPaneIncoming)
                // .addGap(10)
                .addComponent(messagePanel)
            )
            .addContainerGap()
        );

        layout1.setVerticalGroup(layout1.createSequentialGroup()
            .addContainerGap()
            .addComponent(scrollPaneIncoming)
            // .addGap(10)
            .addComponent(messagePanel)
            .addContainerGap()
        );

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void sendMessage(){
        try{
            String message = textAreaOutgoing.getText();

            if (message.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Введите текст сообщения", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // InetAddress addr = InetAddress.getByName(null);
            Socket socket = new Socket(client.addr, 8888);

            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            out.writeUTF(message);

            socket.close();

            textAreaIncoming.append("Я: " + message + "\n");
            textAreaOutgoing.setText("");

        }catch(Exception e){

        }
    }

    public class Client{
        public String clientName;
        public InetAddress addr;
        public ClientFrame frame;
        // public Socket socket;

        public Client(ClientFrame frame){
            this.frame = frame;
            try{
                addr = InetAddress.getByName(null);
            }catch(UnknownHostException e){
                
            }
            frame.textAreaIncoming.append("Client started: addr = " + addr + "\n");
        }


        // public void clientMain() throws IOException{
        //     BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
        //     // clientName = consoleReader.readLine();
    
        //     InetAddress addr = InetAddress.getByName(null);
    
        //     System.out.println("addr = " + addr);
        //     Socket socket = new Socket(addr, 8888);
    
        //     try(BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        //         PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        //     ){
        //         System.out.println("socket = " + socket);
    
        //         // aothorization(consoleReader, in, out);
        //         String message = null;
        //         while(true){
        //             message = consoleReader.readLine();
        //             if(message.equals("END")) break;
        //             out.println(message);
        //         }
    
        //         out.println("END");
        //     }
        //     // catch(IOException e){
        //     //     System.out.println("ИО ошибка");
        //     // }
        //     finally{
        //         System.out.println("closing...");
        //         socket.close();
        //     }
        //     consoleReader.close();
        // }
    
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
}
