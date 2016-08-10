package evaluation;

import model.incidentree.IncidentTree;
import model.incidentree.IncidentTreeNode;
import model.incidentree.IncidentTreeNode.NodeType;
import model.incidentree.OpNode;

public class DistributiveRule extends OperatorRule {
	//if reverse if true, then its (a.b)|(a.d) => a.(b|d)
	boolean reverse = false;

	@Override
	public boolean checkQualified(IncidentTreeNode cur) {
		if(cur.type != NodeType.OP)
			return false;
		if(cur.name.equals("|")){
			if(cur.left == null || cur.right == null)
				return false;
			if(cur.left.type != NodeType.OP || cur.right.type != NodeType.OP || !cur.left.name.equals(cur.right.name))
				return false;
			if(cur.left.name.equals("|"))
				return false;
			if(cur.left.left == null || cur.left.right == null || cur.right.left == null || cur.right.right == null)
				return false;
			if(!isSameTree(cur.left.left, cur.right.left) && !isSameTree(cur.left.right, cur.right.right))
				return false;
			reverse = true;
			return true;
		}else{
			if(cur.left.name.equals("|") || cur.right.name.equals("|")){
				reverse = false;
				return true;
			}
		}
		return false;
	}

	private boolean isSameTree(IncidentTreeNode node1, IncidentTreeNode node2) {
		if(node1 == null && node2 == null)
			return true;
		if(node1 == null || node2 == null)
			return false;
		if(node1.type != node2.type)
			return false;
		if(!node1.name.equals(node2.name))
			return false;
		return isSameTree(node1.left, node2.left) && isSameTree(node1.right, node2.right);
	}

	@Override
	public void perform(IncidentTree it, IncidentTreeNode cur,
			String accessCode) {
		IncidentTreeNode parent = it.root, node = it.root;
		for(char ch: accessCode.toCharArray()){
			if(ch == '0'){
				parent = node;
				node = node.left;
			}else{
				parent = node;
				node = node.right;
			}
		}
		
		IncidentTreeNode newRoot = null;
		if(reverse){
			newRoot = node.left;
			if(isSameTree(cur.left.left, cur.right.left)){
				node.left = newRoot.right;
				node.right = node.right.right;
				newRoot.right = node;
			}else{
				node.left = newRoot.left;
				node.right = node.right.left;
				newRoot.left = node;
			}
		}else{
			if(node.left.type == NodeType.OP && node.left.name.equals("|")){
				newRoot = node.left;
				node.left = newRoot.left;
				OpNode tmp = new OpNode(node.name);
				tmp.left = newRoot.right;
				tmp.right = node.right;
				newRoot.left = node;
				newRoot.right = tmp;
			}else{
				newRoot = node.right;
				node.right = newRoot.left;
				OpNode tmp = new OpNode(node.name);
				tmp.right = newRoot.right;
				tmp.left = node.left;
				newRoot.left = node;
				newRoot.right = tmp;
			}
		}
		
		if(accessCode.length() == 0){
			it.root = newRoot;
		}else if(accessCode.charAt(accessCode.length()-1) == '0'){
			parent.left = newRoot;
		}else{
			parent.right = newRoot;
		}
	}

}
