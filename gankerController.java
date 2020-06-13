package gankerController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class gankerController extends JFrame
{
	JTextField input, magnification;
	static int serverPort;
	static String serverIP;
	ArrayList<String> commandHistory = new ArrayList<>();
	int cmdHisIndex = 0;
	public gankerController(String ip,int port)
	{
		serverIP = ip;
		serverPort = port;
		setTitle(serverIP + ":" + serverPort);
		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		input = new JTextField(10);
		input.addKeyListener(new KeyAdapter()
		{
			public void keyPressed(KeyEvent e)
			{
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					sendAction();
				}
				else if(e.getKeyCode() == KeyEvent.VK_UP)
				{
					if(cmdHisIndex > 0)
					{
						cmdHisIndex--;
						updateCmd();
					}
				}
				else if(e.getKeyCode() == KeyEvent.VK_DOWN)
				{
					if(cmdHisIndex < commandHistory.size() - 1)
					{
						cmdHisIndex++;
						updateCmd();
					}
				}
			}
		});
		magnification = new JTextField(10);
		magnification.setText("60000");
		JButton submit = new JButton("send");
		submit.addActionListener((e) ->
		{
			sendAction();
		});
		JButton stop = new JButton("stop");
		stop.addActionListener((e) ->
		{
			sendMessage("stop");
		});
		JButton check = new JButton("check");
		check.addActionListener((e) ->
		{
			try
			{
				Socket s = new Socket(serverIP,serverPort);
				PrintStream ps = new PrintStream(s.getOutputStream());
				ps.println("ping");
				s.close();
				ps.close();
				JOptionPane.showMessageDialog(gankerController.this,"The ganker is alive.\naddress:" + serverIP + ":" + serverPort,"",JOptionPane.INFORMATION_MESSAGE);
			}
			catch(Exception ex)
			{
				JOptionPane.showMessageDialog(gankerController.this,"The ganker is not alive.\naddress:" + serverIP + ":" + serverPort,"",JOptionPane.INFORMATION_MESSAGE);
			}
		});
		JButton exit = new JButton("exit");
		exit.addActionListener((e) ->
		{
			sendMessage("exit");
		});
		c.add(input,BorderLayout.WEST);
		c.add(new JLabel("magnification:"));
		c.add(magnification,BorderLayout.EAST);
		JPanel jp = new JPanel();
		jp.add(submit);
		jp.add(stop);
		jp.add(check);
		jp.add(exit);
		c.add(jp,BorderLayout.SOUTH);
		setSize(330,100);
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	void updateCmd()
	{
		input.setText(commandHistory.get(cmdHisIndex));
	}
	void sendAction()
	{
		String txt = input.getText();
		if(commandHistory.size() == 0 || !txt.equals(commandHistory.get(commandHistory.size() - 1)))
		{
			commandHistory.add(txt);
			cmdHisIndex = commandHistory.size() - 1;
		}
		try
		{
			long input_number = Long.parseLong(txt);
			sendMessage(input_number * Long.parseLong(magnification.getText()));
		}
		catch(NumberFormatException nfe)
		{
			sendMessage(txt);
		}
	}
	void sendMessage(Object msg)
	{
		try
		{
			Socket s = new Socket(serverIP,serverPort);
			PrintStream ps = new PrintStream(s.getOutputStream());
			ps.println(msg);
			s.close();
			ps.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public static void main(String[] args)
	{
		String ip = "localhost";
		int port = 8787;
		if(args.length >= 1)
		{
			ip = args[0];
		}
		if(args.length >= 2)
		{
			port = Integer.parseInt(args[1]);
		}
		new gankerController(ip,port);
	}
}