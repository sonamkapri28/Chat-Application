import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer {
	static ArrayList<String> userNames = new ArrayList<String>();
	static ArrayList<PrintWriter> printWriters = new ArrayList<PrintWriter>();
	public static void main(String[] args) throws Exception
	{
		System.out.println("Waiting for Clients......");
		ServerSocket ss = new ServerSocket(9806);
		while(true)
		{
			Socket soc = ss.accept();
			System.out.println("Connection Established . . . . ");
			ConversationHandler handler = new ConversationHandler(soc);
			handler.start();
		}
	}

}
class ConversationHandler extends Thread
{
	Socket socket;
	BufferedReader in;
	PrintWriter out;
	String name;
	PrintWriter pw;	//writing data to file
	static FileWriter fw;
	static BufferedWriter bw;
	
	public ConversationHandler(Socket socket) throws IOException
	{
		this.socket = socket;
		fw = new FileWriter("C:\\Users\\sonam\\Desktop\\ServerLog.txt",true);//don't clear the content just append the data
		bw = new BufferedWriter(fw);
		pw = new PrintWriter(bw,true);
	}
	public void run()
	{
		try
		{
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(),true);
			
			int count = 0 ;
			while(true)
			{
				if(count > 0)
				{
					out.println("NAMEALREADYEXISTS");
				}
				else
				{
					out.println("NAMEREQUIRED");
				}
				
				name = in.readLine();
				
				if(name == null)
				{
					return;
				}
				
				if(!ChatServer.userNames.contains(name))	//check if name is not in list
				{
					ChatServer.userNames.add(name);	//if name not in list..add it to list
					break;	//once name  added break the loop
				}
				count++;
			}
			out.println("NAMEACCEPTED"+name);
			ChatServer.printWriters.add(out);
			
			while(true)
			{
				String message = in.readLine();
				if(message == null)
				{
					return;
				}
				pw.println(name + ": "+ message);
				for(PrintWriter writer : ChatServer.printWriters)
				{
					writer.println(name + ": " + message);
				}
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
}