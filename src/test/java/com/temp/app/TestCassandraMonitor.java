package com.temp.app;

public class TestCassandraMonitor {
	public static void testCassandraMonitor(){
		CassandraMonitor cassandraMonitor = new CassandraMonitor();
		cassandraMonitor.execute();
		System.out.println(CassandraMonitor.isCassadraStarted());
	}
	
	
	public static void main(String[] args) {
		testCassandraMonitor();
	}
}
