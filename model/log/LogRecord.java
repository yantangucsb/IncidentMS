package model.log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.incident.*;

public class LogRecord {
	public long lsn;
	public long wid;
	public long islsn;
	public String actiName;
	public Map<String, String> attRead;
	public Map<String, String> attWrite;
	
	public LogRecord(String logEntry){
		attRead = new HashMap<String, String>();
		attWrite = new HashMap<String, String>();
		
		String[] parts = logEntry.split(" ");
		lsn = Long.parseLong(parts[0]);
		wid = Long.parseLong(parts[1]);
		islsn = Long.parseLong(parts[2]);
		actiName = parts[3];
		int i = 4;
		while(i < parts.length && !parts[i].equals("#")){
			String[] pair = parts[i].split("=");
			attRead.put(pair[0], pair[1]);
			i++;
		}
		i++;
		while(i < parts.length){
//			System.out.println(parts[i]);
			String[] pair = parts[i].split("=");
//			System.out.println(pair.length);
			attWrite.put(pair[0], pair[1]);
			i++;
		}
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(lsn);
		sb.append(' ');
		sb.append(wid);
		sb.append(' ');
		sb.append(islsn);
		sb.append(' ');
		sb.append(actiName);
		sb.append(' ');
		for(Map.Entry<String, String> pair: attRead.entrySet()){
			sb.append(pair.getKey());
			sb.append('=');
			sb.append(pair.getValue());
			sb.append(' ');
		}
		sb.append("# ");
		for(Map.Entry<String, String> pair: attWrite.entrySet()){
			sb.append(pair.getKey());
			sb.append('=');
			sb.append(pair.getValue());
			sb.append(' ');
		}
		
		sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}
}
