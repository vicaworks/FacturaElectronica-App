/**
 * 
 */
package com.vcw.falecpv.web.ctrl.cajachica;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.vcw.falecpv.web.common.BaseCtrl;

/**
 * @author cristianvillarreal
 *
 */
@Named
@ViewScoped
public class CajaChicaCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6920391538405286693L;
	
	private Integer activeTab=0;

	/**
	 * 
	 */
	public CajaChicaCtrl() {
	}

	/**
	 * @return the activeTab
	 */
	public Integer getActiveTab() {
		return activeTab;
	}

	/**
	 * @param activeTab the activeTab to set
	 */
	public void setActiveTab(Integer activeTab) {
		this.activeTab = activeTab;
	}
	
	public void tabChange(Integer tab) {
		activeTab = tab;
	}
	
}
