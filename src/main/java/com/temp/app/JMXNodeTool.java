package com.temp.app;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import javax.management.MBeanServerConnection;
import javax.management.openmbean.TabularData;

import org.apache.cassandra.tools.NodeProbe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.temp.app.structures.CFSizeInfo;
import com.temp.app.structures.SnapshotDetail;
import com.temp.app.utils.BoundedExponentialRetryCallable;


/**
 * 
 * @author ooo
 *
 */
public class JMXNodeTool extends NodeProbe {
	
    private static final Logger logger = LoggerFactory.getLogger(JMXNodeTool.class);
    private static volatile JMXNodeTool tool = null;
    private MBeanServerConnection mbeanServerConn = null;
    private final static String PATTERN = "\\d+";

	public JMXNodeTool(String host, int port) throws IOException {
		super(host, port);
	}

	public JMXNodeTool(String host) throws IOException {
		super(host);
	}
	
	// now write one function for flush
	public static synchronized JMXNodeTool connect(String hostName, int jmxPort) throws Exception{
		JMXNodeTool jmxNodeTool = null;
		
		// test connection
		if(!CassandraMonitor.isCassadraStarted()){
			String exceptionMsg = "Cassandra is not yet started, check back again later";
			logger.error(exceptionMsg);
			throw new Exception(exceptionMsg);
		}
		
		try {
			jmxNodeTool = new BoundedExponentialRetryCallable<JMXNodeTool>()
				{
					@Override
					public JMXNodeTool retriableCall() throws Exception
					{
						JMXNodeTool nodetool = new JMXNodeTool(hostName, jmxPort);
						Field fields[] = NodeProbe.class.getDeclaredFields();
						for (int i = 0; i < fields.length; i++)
						{
							if (!fields[i].getName().equals("mbeanServerConn"))
								continue;
							fields[i].setAccessible(true);
							nodetool.mbeanServerConn = (MBeanServerConnection) fields[i].get(nodetool);
						}
						return nodetool;
					}
				}.call();
	} catch (Exception e) {
		logger.error(e.getMessage(), e);
		throw new Exception(e.getMessage());
	}
		
		
		return jmxNodeTool;
	}

	
	// flush
    public void flush() throws IOException, ExecutionException, InterruptedException
    {
        for (String keyspace : getKeyspaces()){
        	forceKeyspaceFlush(keyspace, new String[0]);
        }
    }
    
    public void flush(String keyspace) throws IOException, ExecutionException, InterruptedException
    {
        	forceKeyspaceFlush(keyspace, new String[0]);
    }
    
    // snapshot
    public void snapshot(String snapshotName) throws IOException{
    	for(String keyspace : getKeyspaces()){
    		takeSnapshot(snapshotName, null, keyspace);
    	}
    	
    }
    
    // clear snapshot
    public void clearSnapshot(String snapshotName) throws IOException{
    	for(String keyspace : getKeyspaces()){
    		clearSnapshot("jarvis", keyspace);
    	}
    }
    
    // get list of snapshots
    public List<SnapshotDetail> getSnapshotList(){
    	Map<String, TabularData> snapshotDetails=getSnapshotDetails();
    	List<SnapshotDetail> allSnpashots = new ArrayList<>();
    	for(Map.Entry<String, TabularData> snapshotDetail : snapshotDetails.entrySet()){
    		  Set<?> values = snapshotDetail.getValue().keySet();
    		  Map<String, Set<CFSizeInfo>> ksAndCfInfo = new HashMap<>();
    		  for (Object eachValue : values)
    		  {
    		  final List<?> value = (List<?>) eachValue;
    		  /**
    		   * size will be 5 always so we can directly map our entity
    		   */
    		  if(value.size()==5){
    			  // skip first value which is name of snapshot
        		  String keyspace = value.get(1).toString();
        		  String cfName = value.get(2).toString();
        		  String trueSizeString = value.get(3).toString();
        		  String sizeOnDiskString = value.get(4).toString();
    		  
        		  double trueSize = getDoubleFromString(trueSizeString);
        		  double sizeOnDisk = getDoubleFromString(sizeOnDiskString);
    		  
        		  CFSizeInfo cfSizeInfo = new CFSizeInfo(cfName, trueSize, sizeOnDisk);
        		  if(ksAndCfInfo.containsKey(keyspace)){
        			  ksAndCfInfo.get(keyspace).add(cfSizeInfo);
        		  }else{
        			  ksAndCfInfo.put(keyspace, new HashSet<>(Arrays.asList(cfSizeInfo)));
        		  }
    		  }
    		  }
    		  allSnpashots.add(new SnapshotDetail(snapshotDetail.getKey(), ksAndCfInfo));
    	}
    	return allSnpashots;
    }
    
    /**
     * 
     * @param value
     * @return
     */
    private static double getDoubleFromString(String value){
		return Double.parseDouble(value.split(" ")[0]);
    }
}
