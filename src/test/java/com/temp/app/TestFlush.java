package com.temp.app;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;

/**
 * 
 * @author ooo
 *
 */
public class TestFlush {
	private static JMXNodeTool jmxNodeTool;
	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(TestFlush.class);
	private static String hostName = "localhost";
	private static int jmxPort = 7199;
	private static String snapshotName = "jarvis";
	
	public static void main(String[] args) throws ExecutionException, InterruptedException {
		try {
			jmxNodeTool = new JMXNodeTool(hostName, jmxPort);
			flush();
			takeSnapshot(snapshotName);
			clearSnapshot(snapshotName);
			System.out.println("successfully done!!");
		} catch (IOException e) {
			logger.error("can't flush the keyspaces");
			e.printStackTrace();
		}
	}
	
	private static void takeSnapshot(String snapshot) throws IOException{
		jmxNodeTool.snapshot(snapshot);
	}
	
	private static void clearSnapshot(String snapshot) throws IOException{
		jmxNodeTool.clearSnapshot(snapshot);
	}
	
	private static void flush() throws IOException, ExecutionException, InterruptedException{
		jmxNodeTool.flush();
	}
}
