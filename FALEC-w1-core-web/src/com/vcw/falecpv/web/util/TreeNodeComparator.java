/**
 * 
 */
package com.vcw.falecpv.web.util;

import java.util.Comparator;

import org.primefaces.model.TreeNode;

/**
 * @author cvillarreal
 *
 */
public class TreeNodeComparator implements Comparator<TreeNode> {

	/**
	 * 
	 */
	public TreeNodeComparator() {
	}

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(TreeNode n1, TreeNode n2) {
		return ((String)n1.getData()).compareTo((String)n2.getData());
	}

}
