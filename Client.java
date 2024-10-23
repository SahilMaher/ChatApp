import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

import javax.swing.*;
import java.awt.*;
public class Client extends JFrame
{
    BufferedReader br;
    PrintWriter out;
    boolean b=true;
    Socket socket;
    private JLabel heading=new JLabel("Client Area");
    private JTextArea messageArea=new JTextArea();
    private JTextField messaInput=new JTextField();
    private Font font=new Font("Roboto",Font.PLAIN,20);

    
    public Client()
    {
        try {
            socket=new Socket("localhost",7777);
             br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out=new PrintWriter(socket.getOutputStream());
            cteateGui();
            handleEents();
           startReading();
           //startWriting();
            
        } catch (Exception e) {
           
        }

    }

    public void cteateGui()
    {
        this.setTitle("Client Massage");
        this.setSize(500,500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        heading.setFont(font);
        messageArea.setFont(font);
        messaInput.setFont(font);
       
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
   
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        messaInput.setHorizontalAlignment(SwingConstants.CENTER);
        this.setLayout(new BorderLayout());
        
        this.add(heading,BorderLayout.NORTH);
        this.add(messageArea,BorderLayout.CENTER);
        this.add(messaInput,BorderLayout.SOUTH);

        this.setVisible(true);

    }
    public void handleEents()
    {
        messaInput.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
               
            }

            @Override
            public void keyPressed(KeyEvent e) {
                
            }

            @Override
            public void keyReleased(KeyEvent e)
            {
                if(e.getKeyCode()==10)
                {
                   
                    String contentToSend=messaInput.getText();
                    messageArea.append("Me : "+contentToSend+"\n");
                    out.println(contentToSend);
                    out.flush();
                    messaInput.setText("");
                    messaInput.requestFocus();
                }
           
            }
            
        });
    }
    public void startReading()
{
    Runnable r1=()->
    {
        try {
        while(b)
        {
         
            String msg= br.readLine();
           messageArea.append("Server :"+msg+"\n");

            if(msg.equals("exit"))
            {
                JOptionPane.showMessageDialog(this, "Server Terminate");
               messaInput.setEnabled(false);
                System.out.println("Server Terminate the Chat");
             
                socket.close();
                break;
            }
          
        }
        } catch (IOException e) {
           
            e.printStackTrace();
        }

        

    };
    
    new Thread(r1).start(); 


}
public void startWriting()

{
    Runnable r2=()-> 
    {
        try {
        while (b)
        {
           
              BufferedReader br1=new BufferedReader(new InputStreamReader(System.in));
              String content =br1.readLine();
              out.println(content);
              out.flush();
              if(content.equals("exit"))
              {
                socket.close();
                break;
              }
             
        }      
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        

    };
    new Thread(r2).start(); 
}
    public static void main(String[] args) 
    {
        new Client();
    }
    
}
