/**
 * 
 */
package com.vcw.falecpv.web.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 * @author cristianvillarreal
 *
 */
@Named
@SessionScoped
public class GuestPreferences implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 28715271998980081L;

	private String menuMode = "layout-static";//"layout-slim";

    private String theme = "blue";//"blue";bluegrey

    private String menuColor = "layout-menu-light"; // "layout-menu-light"; layout-menu-dark

    private String topBarColor = "layout-topbar-blue";//"layout-topbar-blue"; layout-topbar-bluegrey

    private String logo = "logo-roma";

    private String profileMode = "popup";
    
    private boolean orientationRTL;

    private List<TopbarColor> topbarColors = new ArrayList<TopbarColor>();

    private List<ComponentTheme> componentThemes = new ArrayList<ComponentTheme>();

    @PostConstruct
    public void init() {  
        topbarColors.add(new TopbarColor("Light", "layout-topbar-light", "logo-roma", "light.png"));
        topbarColors.add(new TopbarColor("Dark", "layout-topbar-dark", "logo-roma-white", "dark.png"));
        topbarColors.add(new TopbarColor("Blue", "layout-topbar-blue", "logo-roma-white", "blue.png"));
        topbarColors.add(new TopbarColor("Green", "layout-topbar-green", "logo-roma-white", "green.png"));
        topbarColors.add(new TopbarColor("Orange", "layout-topbar-orange", "logo-roma-white", "orange.png"));
        topbarColors.add(new TopbarColor("Magenta", "layout-topbar-magenta", "logo-roma-white", "magenta.png"));
        topbarColors.add(new TopbarColor("Blue Grey", "layout-topbar-bluegrey", "logo-roma-white", "bluegrey.png"));
        topbarColors.add(new TopbarColor("Deep Purple", "layout-topbar-deeppurple", "logo-roma-white", "deeppurple.png"));
        topbarColors.add(new TopbarColor("Brown", "layout-topbar-brown", "logo-roma-white", "brown.png"));
        topbarColors.add(new TopbarColor("Lime", "layout-topbar-lime", "logo-roma-white", "lime.png"));
        topbarColors.add(new TopbarColor("Rose", "layout-topbar-rose", "logo-roma-white", "rose.png"));
        topbarColors.add(new TopbarColor("Cyan", "layout-topbar-cyan", "logo-roma-white", "cyan.png"));
        topbarColors.add(new TopbarColor("Teal", "layout-topbar-teal", "logo-roma-white", "teal.png"));
        topbarColors.add(new TopbarColor("Deep Orange", "layout-topbar-deeporange", "logo-roma-white", "deeporange.png"));
        topbarColors.add(new TopbarColor("Indigo", "layout-topbar-indigo", "logo-roma-white", "indigo.png"));
        topbarColors.add(new TopbarColor("Pink", "layout-topbar-pink", "logo-roma-white", "pink.png"));
        topbarColors.add(new TopbarColor("Purple", "layout-topbar-purple", "logo-roma-white", "purple.png"));

        componentThemes.add(new ComponentTheme("Blue", "blue","blue.svg"));
        componentThemes.add(new ComponentTheme("Blue Grey", "bluegrey","bluegrey.svg"));
        componentThemes.add(new ComponentTheme("Brown", "brown","brown.svg"));
        componentThemes.add(new ComponentTheme("Cyan", "cyan","cyan.svg"));
        componentThemes.add(new ComponentTheme("Deep Orange", "deeporange","deeporange.svg"));
        componentThemes.add(new ComponentTheme("Deep Purple", "deeppurple","deeppurple.svg"));
        componentThemes.add(new ComponentTheme("Green", "green","green.svg"));
        componentThemes.add(new ComponentTheme("Teal", "teal","teal.svg"));
        componentThemes.add(new ComponentTheme("Indigo", "indigo","indigo.svg"));
        componentThemes.add(new ComponentTheme("Lime", "lime","lime.svg"));
        componentThemes.add(new ComponentTheme("Magenta", "magenta","magenta.svg"));
        componentThemes.add(new ComponentTheme("Orange", "orange","orange.svg"));
        componentThemes.add(new ComponentTheme("Pink", "pink","pink.svg"));
        componentThemes.add(new ComponentTheme("Purple", "purple","purple.svg"));
        componentThemes.add(new ComponentTheme("Rose", "rose","rose.svg"));
        
        
    }

    public String getTheme() {
    	//theme = "rose";
//    	theme = "blue";
//    	theme = "bluegrey";
    	theme = "myblue";
    	//theme = "indigo";
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getMenuMode() {
        return this.menuMode;
    }

    public void setMenuMode(String menuMode) {
        this.menuMode = menuMode;

        if (this.menuMode.equals("layout-horizontal")) {
            this.profileMode = "popup";
        }
    }

    public String getMenuColor() {
    	menuColor = "layout-menu-light";
        return this.menuColor;
    }

    public void setMenuColor(String menuColor) {
        this.menuColor = menuColor;
    }

    public String getTopBarColor() {
    	topBarColor = "layout-topbar-blue";
        return this.topBarColor;
    }

    public void setTopBarColor(String topBarColor, String logo) {
        this.topBarColor = topBarColor;
        this.logo = logo;
    }

    public String getLogo() {
    	logo =  "logo-roma";
        return this.logo;
    }

    public String getProfileMode() {
        return this.profileMode;
    }

    public void setProfileMode(String profileMode) {
        if (this.menuMode.equals("layout-horizontal")) {
            this.profileMode = "popup";
        }
        else {
            this.profileMode = profileMode;
        }
    }
    
    public boolean isOrientationRTL() {
        return orientationRTL;
    }

    public void setOrientationRTL(boolean orientationRTL) {
        this.orientationRTL = orientationRTL;
    }

    public List<TopbarColor> getTopbarColors() {
        return topbarColors;
    }

    public List<ComponentTheme> getComponentThemes() {
        return componentThemes;
    }
    
    public class TopbarColor {
        String name;
        String className;
        String image;
        String logo;

        public TopbarColor(String name, String className, String logo, String image) {
            this.name = name;
            this.className = className;
            this.image = image;
            this.logo = logo;
        }

        public String getName() {
            return this.name;
        }

        public String getClassName() {
            return this.className;
        }

         public String getImage() {
            return this.image;
        }
        
        public String getLogo() {
            return this.logo;
        }
    }
    
    public class ComponentTheme {
        String name;
        String file;
        String image;

        public ComponentTheme(String name, String file, String image) {
            this.name = name;
            this.file = file;
            this.image = image;
        }

        public String getName() {
            return this.name;
        }

        public String getFile() {
            return this.file;
        }

         public String getImage() {
            return this.image;
        }
    }


}
