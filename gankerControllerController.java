package gankerControllerController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import gankerController.*;

public class gankerControllerController extends JFrame
{
	static String LANCover;
	static String thisIP;
	static int port = 8787;
	static boolean[] existsList = new boolean[256];
	static gankerController[] gcs;
	static int clients = 0;
	static JList<String> jl;
	static JLabel info;
	public gankerControllerController()
	{
		setSize(400,300);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle(thisIP + ":" + port);
		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		jl = new JList<String>();
		info = new JLabel("ping...");
		c.add(new JScrollPane(jl),BorderLayout.CENTER);
		c.add(info,BorderLayout.SOUTH);
		setVisible(true);
		init();
	}
	public static void init()
	{
		Thread lastT = null;
		clients = 0;
		for(int i = 0; i < 256; i++)
		{
			Thread tmp = new pinger(i,lastT);
			tmp.start();
			lastT = tmp;
		}
		try
		{
			lastT.join();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		if(clients > 0)
		{
			String[] ipList = new String[clients+1];
			gcs = new gankerController[clients];
			for(int j = 0, k = 0; j < 256 && k < clients; j++)
			{
				if(existsList[j] == true)
				{
					ipList[k] = LANCover + j;
					k++;
				}
			}
			ipList[clients] = "reload";
			jl.setListData(ipList);
			info.setText("ready found " + clients + " ganker(s)");
		}
		else
		{
			String[] str = {"no ganker online", "reload"};
			jl.setListData(str);
			info.setText("ready");
		}
		jl.addMouseListener(new MouseAdapter()
			{
				public void mouseClicked(MouseEvent e)
				{
					if(e.getClickCount() == 2)
					{
						int index = jl.getSelectedIndex();
						if(index == -1) return;
						if(jl.getSelectedValue().equals("reload"))
						{
							init();
						}
						else if(gcs[index] == null || !gcs[index].isDisplayable())
						{
							gcs[index] = new gankerController(jl.getSelectedValue(), port);
						}
						else
						{
							gcs[index].setState(JFrame.NORMAL);
							gcs[index].toFront();
						}
					}
				}
			});
	}
	public static void main(String[] args)
	{
		try
		{
			LANCover = InetAddress.getLocalHost().getHostAddress().toString();
			if(LANCover.equals("127.0.0.1"))
			{
				JOptionPane.showMessageDialog(null,"no LAN","",JOptionPane.INFORMATION_MESSAGE);
				System.exit(0);
			}
			thisIP = LANCover;
			LANCover = LANCover.substring(0,LANCover.lastIndexOf('.')+1);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		if(args.length >= 1)
		{
			port = Integer.parseInt(args[0]);
		}
		new gankerControllerController();
	}
}


class pinger extends Thread
{
	int c;
	Thread l;
	public pinger(int count,Thread last)
	{
		c = count;
		l = last;
	}
	public void run()
	{
		try
		{
			Socket s = new Socket(gankerControllerController.LANCover + c,gankerControllerController.port);
			PrintStream ps = new PrintStream(s.getOutputStream());
			ps.println("ping");
			s.close();
			ps.close();
			gankerControllerController.existsList[c] = true;
			gankerControllerController.clients++;
		}
		catch(Exception ex)
		{
			gankerControllerController.existsList[c] = false;
		}
		try
		{
			if(l != null)
			{
				l.join();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}