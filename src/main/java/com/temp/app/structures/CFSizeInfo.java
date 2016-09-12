package com.temp.app.structures;

/**
 * Class name
 * 
 * True Size
 * 
 * Size on disk
 * 
 * @author ooo
 *
 */
public class CFSizeInfo {
	private String cfName;
	private double trueSize;
	private double diskSize;
	
	public CFSizeInfo(String cfName, double trueSize, double diskSize) {
		this.cfName = cfName;
		this.trueSize = trueSize;
		this.diskSize = diskSize;
	}

	public String getCfName() {
		return cfName;
	}

	public void setCfName(String cfName) {
		this.cfName = cfName;
	}

	public double getTrueSize() {
		return trueSize;
	}

	public void setTrueSize(double trueSize) {
		this.trueSize = trueSize;
	}

	public double getDiskSize() {
		return diskSize;
	}

	public void setDiskSize(double diskSize) {
		this.diskSize = diskSize;
	}
	
}
