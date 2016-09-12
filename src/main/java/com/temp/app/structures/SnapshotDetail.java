package com.temp.app.structures;

import java.util.HashMap;
import java.util.*;

/**
 * Skeleton for snapshot Details
 * 
 * Name of snapshot
 * 
 * set of KS inside the snapshot
 * 
 * set of CF inside KS
 * 
 * for each Cf TrueSize and SizeOnDisk
 * 
 * @author ooo
 *
 */
public class SnapshotDetail {
	private String snapshotName;
	private Map<String, Set<CFSizeInfo>> ksAndCfInfo = new HashMap<>();
	
	public SnapshotDetail(String snapshotName, Map<String, Set<CFSizeInfo>> ksAndCfInfo) {
		this.snapshotName = snapshotName;
		this.ksAndCfInfo = ksAndCfInfo;
	}

	public String getSnapshotName() {
		return snapshotName;
	}

	public void setSnapshotName(String snapshotName) {
		this.snapshotName = snapshotName;
	}

	public Map<String, Set<CFSizeInfo>> getKsAndCfInfo() {
		return ksAndCfInfo;
	}

	public void setKsAndCfInfo(Map<String, Set<CFSizeInfo>> ksAndCfInfo) {
		this.ksAndCfInfo = ksAndCfInfo;
	}

}

