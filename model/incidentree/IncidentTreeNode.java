package model.incidentree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import model.incident.Occurrence;
import model.log.Log;

public abstract class IncidentTreeNode implements Runnable, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public enum NodeType {
		ACTI, OP, COND
	};
	
	public NodeType type;
	public String name;
	public IncidentTreeNode left, right;
	public List<Occurrence> occs;
	
	public IncidentTreeNode(String str){
		name = str;
		left = null;
		right = null;
		occs = new ArrayList<Occurrence>();
	}
	
	public NodeType getType() {
		return type;
	}
}
