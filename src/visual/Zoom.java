package visual;

import processing.core.PApplet;
import visual.Constants;

/**
 * Class that allows zooming of the visualization horizontally.
 */
public class Zoom {

	private int swidth, sheight;    // width and height of bar
	private float xpos, ypos;       // x and y position of bar
	private float spos, newspos;    // x position of slider
	private float sposMin, sposMax; // max and min values of slider
	private int loose;              // looseness of slider movement
	private boolean over;           // is the mouse over the slider?
	private boolean locked;
	
	//parent PApplet that we render onto
	private PApplet parent;

	public Zoom (float xposition, float yposition,
			int width, int height, int looseVal, PApplet vis) {
		parent = vis;
		swidth = width;
		sheight = height;
		xpos = xposition;
		ypos = yposition-(sheight/2);
		spos = xpos + (swidth/2);
		newspos = spos;
		sposMin = xpos;
		sposMax = xpos + swidth - sheight;
		loose = looseVal;
	}
	
	/**
	 * Display method onto PApplet.
	 */
	public void display() {
		//update slider
		over = overEvent();
		
		if (parent.mousePressed && over) {
			locked = true;
		}
		else if (!parent.mousePressed) {
			locked = false;
		}
		
		if (locked) {
			newspos = constrain(parent.mouseX-sheight/2, sposMin, sposMax);
		}
		if (PApplet.abs(newspos - spos) > 1) {
			spos = spos + (newspos-spos)/loose;
		}
		
		//display scrollbar + slider
		parent.noStroke();
		parent.fill(180);
		parent.rect(xpos, ypos, swidth, sheight);
		if (over || locked) {
			parent.fill(0, 0, 0);
		} else {
			parent.fill(102, 102, 102);
		}
		parent.rect(spos, ypos, sheight, sheight);
	}

	/**
	 * Update zoom ratio.
	 */
	public void updateZoomRatio() {
		Constants.setZoomRatio(2*((spos - sposMin)/(sposMax - sposMin)));
	}
	
	/**
	 * Returns the slider position as a value between 0 and 1.
	 */
	public float getSliderPos(){
		return ((spos - sposMin)/(sposMax - sposMin));
	}
	
	/**
	 * Constrains val to be between minv and maxv.
	 */
	private float constrain(float val, float minv, float maxv) {
		return PApplet.min(PApplet.max(val, minv), maxv);
	}

	/**
	 * Returns true if the mouse pointer is over the scroll bar.
	 */
	private boolean overEvent() {
		if (parent.mouseX > xpos && parent.mouseX < xpos+swidth &&
				parent.mouseY > ypos && parent.mouseY < ypos+sheight) {
			return true;
		} else {
			return false;
		}
	}
}
