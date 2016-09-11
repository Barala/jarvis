package com.temp.app;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * @author ooo
 *
 * This checks whether cassandra is running or not
 * 
 * write one interface to make it better [why interface ??] 
 */
public class CassandraMonitor {
	
	private static final Logger logger = LoggerFactory.getLogger(CassandraMonitor.class);
	private static final AtomicBoolean isCassandraStarted = new AtomicBoolean(false);
	
	public CassandraMonitor() {
		// TODO Auto-generated constructor stub
	}
	
	public void execute(){
		try{
			// right now it's hard coded make it better
			Process p = Runtime.getRuntime().exec("pgrep -f CassandraDaemon");
			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = input.readLine();
			if(line!=null && !isCassadraStarted()){
				// Since there is Pid, set flag to true
				isCassandraStarted.set(true);
			}else if(line==null && isCassadraStarted()){
				// since there is no PID set flag to false
				isCassandraStarted.set(false);
			}
		}catch(Exception e){
			logger.warn("Exception thrown while checking if Cassandra is running or not ", e);
			isCassandraStarted.set(false);
			e.printStackTrace();
		}
	}
	
	 public static boolean isCassadraStarted()
	    {
	        return isCassandraStarted.get();
	    }
}
