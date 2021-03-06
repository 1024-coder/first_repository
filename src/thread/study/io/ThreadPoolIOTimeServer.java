package thread.study.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolIOTimeServer {

	private static int port = 9999;

	public static void main(String[] args) throws Exception{
		ServerSocket ssocket = new ServerSocket(port);
		ThreadPoolExecutor executor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),10,120,TimeUnit.SECONDS,new ArrayBlockingQueue<Runnable>(10));
		while(true){
			final Socket socket = ssocket.accept();
			executor.execute(new Thread(new Runnable() {
				@Override
				public void run() {
					BufferedReader reader = null;
					BufferedWriter writer = null;
					try {
						reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					String readLine = null;
					boolean close = false;
					try {
						while(!close){
							readLine = reader.readLine();
							if(readLine != null){
								if(readLine.equals("time")){
									readLine = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
								}
								System.out.println(readLine);
								writer.write(readLine);
								writer.newLine();//BufferedWriter 需要显示的调用newLine ，而PrintWriter默认会自动添加换行附
								writer.flush();
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}));
		}
	}
}
