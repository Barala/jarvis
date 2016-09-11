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
	
	public static void main(String[] args) throws ExecutionException, InterruptedException {
		try {
			jmxNodeTool = new JMXNodeTool(hostName, jmxPort);
			// first flush
			jmxNodeTool.flush();
			// then take snapshot
			//jmxNodeTool.snapshot();
			jmxNodeTool.clearSnapshot("jarvis");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("can't flush the keyspaces");
			e.printStackTrace();
		}
		System.out.println("flushed successfully!!");
	}
}
