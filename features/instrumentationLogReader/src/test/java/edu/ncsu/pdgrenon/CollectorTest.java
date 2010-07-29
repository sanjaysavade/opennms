package edu.ncsu.pdgrenon;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;


public class CollectorTest {
	private Date getDate(String dateString) throws ParseException {
		return new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss,S").parse(dateString);
	}
	@Test
	public void testStartNotSetEnd() throws ParseException {
		Collector c = new Collector();
		c.addLog("2010-05-26 12:12:38,027 DEBUG [CollectdScheduler-50 Pool-fiber11] Collectd: collector.collect: collectData: end: 24/216.216.217.254/SNMP");	
		c.addLog("2010-05-26 12:12:40,883 DEBUG [CollectdScheduler-50 Pool-fiber11] Collectd: collector.collect: collectData: begin: 24/216.216.217.254/SNMP");
		c.addLog("2010-05-26 12:12:48,027 DEBUG [CollectdScheduler-50 Pool-fiber11] Collectd: collector.collect: collectData: end: 24/216.216.217.254/SNMP");
		c.addLog("2010-05-26 12:12:50,883 DEBUG [CollectdScheduler-50 Pool-fiber11] Collectd: collector.collect: collectData: begin: 24/216.216.217.254/SNMP");
		assertEquals(getDate("2010-05-26 12:12:40,883"), c.getStartTime());
		assertEquals(getDate("2010-05-26 12:12:48,027"), c.getEndTime());
		assertEquals(7144, c.getDuration());
	}
	@Test
	public void testStartAndEndTime() throws ParseException {
		Collector c = new Collector();
		c.addLog("2010-05-26 12:12:40,883 DEBUG [CollectdScheduler-50 Pool-fiber11] Collectd: collector.collect: collectData: begin: 24/216.216.217.254/SNMP");
		c.addLog("2010-05-26 12:12:48,027 DEBUG [CollectdScheduler-50 Pool-fiber11] Collectd: collector.collect: collectData: end: 24/216.216.217.254/SNMP");	
		
		assertEquals(getDate("2010-05-26 12:12:40,883"), c.getStartTime());
		assertEquals(getDate("2010-05-26 12:12:48,027"), c.getEndTime());
		assertEquals(7144, c.getDuration());
	}
	@Test
	public void testServiceCount(){
		Collector c = new Collector();
		assertEquals(0, c.getServiceCount());
		c.addLog("2010-05-26 12:12:40,883 DEBUG [CollectdScheduler-50 Pool-fiber11] Collectd: collector.collect: collectData: begin: 24/216.216.217.254/SNMP");
		c.addLog("2010-05-26 12:12:48,027 DEBUG [CollectdScheduler-50 Pool-fiber11] Collectd: collector.collect: collectData: end: 24/216.216.217.254/SNMP");
		assertEquals(1, c.getServiceCount());
		c.addLog("2010-06-01 09:36:28,950 DEBUG [CollectdScheduler-50 Pool-fiber1] Collectd: collector.collect: persistDataQueueing: begin: 19/209.61.128.9/SNMP");
		c.addLog("2010-06-01 09:36:28,995 DEBUG [CollectdScheduler-50 Pool-fiber1] Collectd: collector.collect: persistDataQueueing: end: 19/209.61.128.9/SNMP");
		c.addLog("2010-05-26 12:12:40,883 DEBUG [CollectdScheduler-50 Pool-fiber11] Collectd: collector.collect: collectData: begin: 24/216.216.217.254/SNMP");
		c.addLog("2010-05-26 12:12:48,027 DEBUG [CollectdScheduler-50 Pool-fiber11] Collectd: collector.collect: collectData: end: 24/216.216.217.254/SNMP");
		c.addLog("2010-06-01 09:36:02,541 DEBUG [CollectdScheduler-50 Pool-fiber1] Collectd: collector.collect: end:7/172.20.1.12/SNMP");
		c.addLog("2010-06-01 09:36:02,542 DEBUG [CollectdScheduler-50 Pool-fiber1] Collectd: collector.collect: begin:27/172.20.1.6/SNMP");
		c.addLog("2010-06-01 09:36:02,544 DEBUG [CollectdScheduler-50 Pool-fiber1] Collectd: collector.collect: collectData: begin: 27/172.20.1.6/SNMP");
		c.addLog("2010-06-01 09:36:03,508 DEBUG [CollectdScheduler-50 Pool-fiber1] Collectd: collector.collect: collectData: end: 27/172.20.1.6/SNMP");
		c.addLog("2010-06-01 09:36:02,541 DEBUG [CollectdScheduler-50 Pool-fiber1] Collectd: collector.collect: error: 7/172.20.1.12/SNMP: org.opennms.netmgt.collectd.CollectionTimedOut: Timeout retrieving SnmpCollectors for 172.20.1.12 for kenny.internal.opennms.com/172.20.1.12: SnmpCollectors for 172.20.1.12: snmpTimeoutError for: kenny.internal.opennms.com/172.20.1.12");
		c.addLog("2010-06-01 09:36:27,644 DEBUG [CollectdScheduler-50 Pool-fiber1] Collectd: collector.collect: collectData: begin: 19/209.61.128.9/SNMP");
		c.addLog("2010-06-01 09:36:28,950 DEBUG [CollectdScheduler-50 Pool-fiber1] Collectd: collector.collect: collectData: end: 19/209.61.128.9/SNMP");
		c.addLog("2010-06-01 09:33:56,292 DEBUG [CollectdScheduler-50 Pool-fiber1] Collectd: collector.collect: collectData: begin: 83/172.20.1.15/SNMP");
		c.addLog("2010-06-01 09:33:56,440 DEBUG [CollectdScheduler-50 Pool-fiber1] Collectd: collector.collect: collectData: end: 83/172.20.1.15/SNMP");
		assertEquals(5, c.getServiceCount());
	}
	
	@Test
	public void testThreadcount(){
		Collector c = new Collector();
		assertEquals(0, c.getThreadCount());
		c.addLog("2010-05-26 12:12:40,883 DEBUG [CollectdScheduler-50 Pool-fiber11] Collectd: collector.collect: collectData: begin: 24/216.216.217.254/SNMP");
		c.addLog("2010-05-26 12:12:48,027 DEBUG [CollectdScheduler-50 Pool-fiber11] Collectd: collector.collect: collectData: end: 24/216.216.217.254/SNMP");
		assertEquals(1, c.getThreadCount());
		c.addLog("2010-06-01 09:36:28,950 DEBUG [CollectdScheduler-50 Pool-fiber1] Collectd: collector.collect: persistDataQueueing: begin: 19/209.61.128.9/SNMP");
		c.addLog("2010-06-01 09:36:28,995 DEBUG [CollectdScheduler-50 Pool-fiber1] Collectd: collector.collect: persistDataQueueing: end: 19/209.61.128.9/SNMP");
		c.addLog("2010-06-01 09:33:31,964 DEBUG [CollectdScheduler-50 Pool-fiber0] Collectd: collector.collect: collectData: begin: 60/172.20.1.202/SNMP");
		c.addLog("2010-06-01 09:33:32,477 DEBUG [CollectdScheduler-50 Pool-fiber0] Collectd: collector.collect: collectData: end: 60/172.20.1.202/SNMP");
		c.addLog("2010-06-01 08:45:12,104 DEBUG [CollectdScheduler-50 Pool-fiber2] Collectd: collector.collect: persistDataQueueing: begin: 86/172.20.1.25/WMI");
		c.addLog("2010-06-01 08:45:12,104 DEBUG [CollectdScheduler-50 Pool-fiber2] Collectd: collector.collect: persistDataQueueing: end: 86/172.20.1.25/WMI");
		c.addLog("2010-06-01 08:39:46,648 DEBUG [CollectdScheduler-50 Pool-fiber3] Collectd: collector.collect: begin:58/172.20.1.201/SNMP");
		c.addLog("2010-06-01 08:39:46,650 DEBUG [CollectdScheduler-50 Pool-fiber3] Collectd: collector.collect: collectData: begin: 58/172.20.1.201/SNMP");
		assertEquals(5, c.getThreadCount());
	}

	@Test 
	public void testCollectionsPerService() {
		Collector c = new Collector();
		c.addLog("2010-05-26 12:12:40,883 DEBUG [CollectdScheduler-50 Pool-fiber11] Collectd: collector.collect: collectData: begin: 24/216.216.217.254/SNMP");
		c.addLog("2010-05-26 12:12:48,027 DEBUG [CollectdScheduler-50 Pool-fiber11] Collectd: collector.collect: collectData: end: 24/216.216.217.254/SNMP");
		c.addLog("2010-06-01 09:36:28,950 DEBUG [CollectdScheduler-50 Pool-fiber1] Collectd: collector.collect: persistDataQueueing: begin: 19/209.61.128.9/SNMP");
		c.addLog("2010-06-01 09:36:28,995 DEBUG [CollectdScheduler-50 Pool-fiber1] Collectd: collector.collect: persistDataQueueing: end: 19/209.61.128.9/SNMP");
		c.addLog("2010-06-01 09:33:31,964 DEBUG [CollectdScheduler-50 Pool-fiber0] Collectd: collector.collect: collectData: begin: 60/172.20.1.202/SNMP");
		c.addLog("2010-06-01 09:33:32,477 DEBUG [CollectdScheduler-50 Pool-fiber0] Collectd: collector.collect: collectData: end: 60/172.20.1.202/SNMP");
		c.addLog("2010-06-01 08:45:12,104 DEBUG [CollectdScheduler-50 Pool-fiber2] Collectd: collector.collect: persistDataQueueing: begin: 86/172.20.1.25/WMI");
		c.addLog("2010-06-01 08:45:12,104 DEBUG [CollectdScheduler-50 Pool-fiber2] Collectd: collector.collect: persistDataQueueing: end: 86/172.20.1.25/WMI");
		c.addLog("2010-06-01 08:39:46,648 DEBUG [CollectdScheduler-50 Pool-fiber3] Collectd: collector.collect: begin:58/172.20.1.201/SNMP");
		c.addLog("2010-06-01 08:39:46,650 DEBUG [CollectdScheduler-50 Pool-fiber3] Collectd: collector.collect: collectData: begin: 58/172.20.1.201/SNMP");
		assertEquals(1,c.getCollectionsPerService("24/216.216.217.254/SNMP"));
		assertEquals(0,c.getCollectionsPerService("19/209.61.128.9/SNMP"));
		assertEquals(1,c.getCollectionsPerService("60/172.20.1.202/SNMP"));
		assertEquals(0,c.getCollectionsPerService("86/172.20.1.25/WMI"));
		assertEquals(0,c.getCollectionsPerService("58/172.20.1.201/SNMP"));
	}
	@Test
	public void testAverageCollectionTimePerService() {
		Collector c = new Collector();
		c.addLog("2010-05-26 12:12:40,883 DEBUG [CollectdScheduler-50 Pool-fiber11] Collectd: collector.collect: collectData: begin: 24/216.216.217.254/SNMP");
		c.addLog("2010-05-26 12:12:48,027 DEBUG [CollectdScheduler-50 Pool-fiber11] Collectd: collector.collect: collectData: end: 24/216.216.217.254/SNMP");
		c.addLog("2010-06-01 09:36:28,950 DEBUG [CollectdScheduler-50 Pool-fiber1] Collectd: collector.collect: persistDataQueueing: begin: 19/209.61.128.9/SNMP");
		c.addLog("2010-06-01 09:36:28,995 DEBUG [CollectdScheduler-50 Pool-fiber1] Collectd: collector.collect: persistDataQueueing: end: 19/209.61.128.9/SNMP");
		c.addLog("2010-06-01 09:33:31,964 DEBUG [CollectdScheduler-50 Pool-fiber0] Collectd: collector.collect: collectData: begin: 60/172.20.1.202/SNMP");
		c.addLog("2010-06-01 09:33:32,477 DEBUG [CollectdScheduler-50 Pool-fiber0] Collectd: collector.collect: collectData: end: 60/172.20.1.202/SNMP");
		c.addLog("2010-06-01 08:45:12,104 DEBUG [CollectdScheduler-50 Pool-fiber2] Collectd: collector.collect: persistDataQueueing: begin: 86/172.20.1.25/WMI");
		c.addLog("2010-06-01 08:45:12,104 DEBUG [CollectdScheduler-50 Pool-fiber2] Collectd: collector.collect: persistDataQueueing: end: 86/172.20.1.25/WMI");
		c.addLog("2010-06-01 08:39:46,648 DEBUG [CollectdScheduler-50 Pool-fiber3] Collectd: collector.collect: begin:58/172.20.1.201/SNMP");
		c.addLog("2010-06-01 08:39:46,650 DEBUG [CollectdScheduler-50 Pool-fiber3] Collectd: collector.collect: collectData: begin: 58/172.20.1.201/SNMP");
		assertEquals(7144,c.getAverageCollectionTimePerService("24/216.216.217.254/SNMP"));
		assertEquals(0,c.getAverageCollectionTimePerService("19/209.61.128.9/SNMP"));
		assertEquals(513,c.getAverageCollectionTimePerService("60/172.20.1.202/SNMP"));
		assertEquals(0,c.getAverageCollectionTimePerService("86/172.20.1.25/WMI"));
		assertEquals(0,c.getAverageCollectionTimePerService("58/172.20.1.201/SNMP"));
	}
	@Test
	public void testTotalCollectionTimePerService() {
		Collector c = new Collector();
		c.addLog("2010-05-26 12:12:40,883 DEBUG [CollectdScheduler-50 Pool-fiber11] Collectd: collector.collect: collectData: begin: 24/216.216.217.254/SNMP");
		c.addLog("2010-05-26 12:12:48,027 DEBUG [CollectdScheduler-50 Pool-fiber11] Collectd: collector.collect: collectData: end: 24/216.216.217.254/SNMP");
		c.addLog("2010-06-01 09:36:28,950 DEBUG [CollectdScheduler-50 Pool-fiber1] Collectd: collector.collect: persistDataQueueing: begin: 19/209.61.128.9/SNMP");
		c.addLog("2010-06-01 09:36:28,995 DEBUG [CollectdScheduler-50 Pool-fiber1] Collectd: collector.collect: persistDataQueueing: end: 19/209.61.128.9/SNMP");
		c.addLog("2010-06-01 09:33:31,964 DEBUG [CollectdScheduler-50 Pool-fiber0] Collectd: collector.collect: collectData: begin: 60/172.20.1.202/SNMP");
		c.addLog("2010-06-01 09:33:32,477 DEBUG [CollectdScheduler-50 Pool-fiber0] Collectd: collector.collect: collectData: end: 60/172.20.1.202/SNMP");
		c.addLog("2010-06-01 08:45:12,104 DEBUG [CollectdScheduler-50 Pool-fiber2] Collectd: collector.collect: persistDataQueueing: begin: 86/172.20.1.25/WMI");
		c.addLog("2010-06-01 08:45:12,104 DEBUG [CollectdScheduler-50 Pool-fiber2] Collectd: collector.collect: persistDataQueueing: end: 86/172.20.1.25/WMI");
		c.addLog("2010-06-01 08:39:46,648 DEBUG [CollectdScheduler-50 Pool-fiber3] Collectd: collector.collect: begin:58/172.20.1.201/SNMP");
		c.addLog("2010-06-01 08:39:46,650 DEBUG [CollectdScheduler-50 Pool-fiber3] Collectd: collector.collect: collectData: begin: 58/172.20.1.201/SNMP");
		assertEquals(7144,c.getAverageCollectionTimePerService("24/216.216.217.254/SNMP"));
		assertEquals(0,c.getAverageCollectionTimePerService("19/209.61.128.9/SNMP"));
		assertEquals(513,c.getAverageCollectionTimePerService("60/172.20.1.202/SNMP"));
		assertEquals(0,c.getAverageCollectionTimePerService("86/172.20.1.25/WMI"));
		assertEquals(0,c.getAverageCollectionTimePerService("58/172.20.1.201/SNMP"));
	}
	
	@Test
	public void testTotalCollectionTime() {
		Collector c = new Collector();
		c.addLog("2010-03-13 02:20:30,525 DEBUG [CollectdScheduler-400 Pool-fiber51] Collectd: collector.collect: collectData: begin: 32028/209.219.9.78/SNMP");
		c.addLog("2010-03-13 02:21:09,976 DEBUG [CollectdScheduler-400 Pool-fiber51] Collectd: collector.collect: collectData: end: 32028/209.219.9.78/SNMP");
		c.addLog("2010-03-13 02:32:42,641 DEBUG [CollectdScheduler-400 Pool-fiber25] Collectd: collector.collect: collectData: begin: 32028/209.219.9.78/SNMP");
		c.addLog("2010-03-13 02:33:01,973 DEBUG [CollectdScheduler-400 Pool-fiber25] Collectd: collector.collect: collectData: end: 32028/209.219.9.78/SNMP");
		c.addLog("2010-03-13 02:43:27,749 DEBUG [CollectdScheduler-400 Pool-fiber112] Collectd: collector.collect: collectData: begin: 32028/209.219.9.78/SNMP");
		c.addLog("2010-03-13 02:43:31,517 DEBUG [CollectdScheduler-400 Pool-fiber112] Collectd: collector.collect: collectData: end: 32028/209.219.9.78/SNMP");
		c.addLog("2010-03-13 02:54:13,334 DEBUG [CollectdScheduler-400 Pool-fiber166] Collectd: collector.collect: collectData: begin: 32028/209.219.9.78/SNMP");
		c.addLog("2010-03-13 02:54:35,223 DEBUG [CollectdScheduler-400 Pool-fiber166] Collectd: collector.collect: collectData: end: 32028/209.219.9.78/SNMP");
		c.addLog("2010-03-13 03:05:47,554 DEBUG [CollectdScheduler-400 Pool-fiber307] Collectd: collector.collect: collectData: begin: 32028/209.219.9.78/SNMP");
		c.addLog("2010-03-13 03:06:28,926 DEBUG [CollectdScheduler-400 Pool-fiber307] Collectd: collector.collect: collectData: end: 32028/209.219.9.78/SNMP");
		c.addLog("2010-03-13 03:18:27,559 DEBUG [CollectdScheduler-400 Pool-fiber264] Collectd: collector.collect: collectData: begin: 32028/209.219.9.78/SNMP");
		c.addLog("2010-03-13 03:19:06,934 DEBUG [CollectdScheduler-400 Pool-fiber264] Collectd: collector.collect: collectData: end: 32028/209.219.9.78/SNMP");
		assertEquals(39451+19332+3768+21889+41372+39375,c.getTotalCollectionTimePerService("32028/209.219.9.78/SNMP"));
	}

	@Test 
	public void testReadLogMessagesFromFile () throws IOException {
		Collector c = new Collector();
		c.readLogMessagesFromFile("TestLogFile.log");
		assertEquals(7144,c.getAverageCollectionTimePerService("24/216.216.217.254/SNMP"));
		assertEquals(0,c.getAverageCollectionTimePerService("19/209.61.128.9/SNMP"));
		assertEquals(513,c.getAverageCollectionTimePerService("60/172.20.1.202/SNMP"));
		assertEquals(0,c.getAverageCollectionTimePerService("86/172.20.1.25/WMI"));
		assertEquals(0,c.getAverageCollectionTimePerService("58/172.20.1.201/SNMP"));
	}
	@Test
	public void testPrintGlobalStats () throws IOException {
		Collector c = new Collector ();
		c.readLogMessagesFromFile("TestLogFile.log");
		String expectedOutput = 
			"Start Time: 2010-05-26 12:12:40,883\n" +
			"End Time: 2010-06-01 08:45:12,104\n" +
			"Duration: 5d20h32m31.221s\n" +
			"Total Services: 5\n" +
			"Threads Used: 5\n";
		StringWriter out = new StringWriter();
		c.printGlobalStats(new PrintWriter(out, true));
		String actualOutput = out.toString(); 
		assertEquals(expectedOutput,actualOutput);
	}
	@Test 
	public void testPrintServiceStats () throws IOException {
		Collector c = new Collector ();
		c.readLogMessagesFromFile("TestLogFile.log");
		String expectedOutput = String.format(Collector.SERVICE_DATA_FORMAT, 
				"24/216.216.217.254/SNMP",
				"7.144s",
				 1,
				"7.144s",
                100.0,
                "0s",
                0.0,
                "0s",
                "7.144s"
				);
		StringWriter out = new StringWriter();
		c.printServiceStats("24/216.216.217.254/SNMP", new PrintWriter(out, true));
		String actualOutput = out.toString();
		assertEquals(expectedOutput,actualOutput);
	}

	@Test
	public void testFormatDuration () {
		assertEquals("0s",Collector.formatDuration(0));
		assertEquals("3s",Collector.formatDuration(3000));
		assertEquals("7s",Collector.formatDuration(7000));
		assertEquals("2.345s",Collector.formatDuration(2345));
		assertEquals("1m",Collector.formatDuration(60000));
		assertEquals("2m",Collector.formatDuration(120000));
		assertEquals("2m3.456s",Collector.formatDuration(123456));
		assertEquals("2m0.456s",Collector.formatDuration(120456));
		assertEquals("1h",Collector.formatDuration(3600*1000));
		assertEquals("2h",Collector.formatDuration(2*3600*1000));
		assertEquals("1h1m1s",Collector.formatDuration(3600*1000+60000+1000));
		assertEquals("1h0m1s",Collector.formatDuration(3600*1000+1000));
		assertEquals("1d",Collector.formatDuration(3600*1000*24));
		assertEquals("1d0h0m0.001s",Collector.formatDuration(3600*1000*24+1));
	}
	@Ignore
	@Test
	public void testPrintReport() throws IOException{
		Collector c = new Collector();
		c.readLogMessagesFromFile("TestLogFile.log");
		StringWriter out = new StringWriter ();
		c.printReport(new PrintWriter(out,true));
		String expectedOutput = fromFile("TestLogFile.out");
		String actualOutput = out.toString();
		assertEquals(expectedOutput,actualOutput);
	}
	private String fromFile(String fileName) throws IOException {
		StringBuilder buf = new StringBuilder();
		File logFile = new File(fileName);
		BufferedReader r = new BufferedReader(new FileReader(logFile));	
		String line = r.readLine();
		while(line != null){
			buf.append(line);
			buf.append("\n");
			line = r.readLine();
		}
		r.close();
		return buf.toString();
	}
}
