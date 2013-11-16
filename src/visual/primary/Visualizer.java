package visual.primary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import data.Line;
import data.RepoFile;

import processing.core.*;
import visual.primary.Constants;
import visual.primary.Constants.Colour;
import visual.primary.HorizontalScrollBar;
import visual.primary.Zoom;

@SuppressWarnings("serial")
public class Visualizer extends PApplet {
	
	//set of project authors
	private HashMap<String,Integer> authorColorScheme = null;
	//set of fileBars
	private FileDisplayContainer displayFiles = null;
	//horizontal scroll bar and zooming
	private HorizontalScrollBar hscroll;
	private Zoom hzoom;
	//timeline scrollbar
	private TimeLine timescroll;
	//list of commit ids
	private ArrayList<String> listOfCommitIds;
	
	public Iterator<String> getCommitIds() {
		return listOfCommitIds.iterator();
	}
	
	public Visualizer(ArrayList<String> authorList, 
			ArrayList<RepoFile> repfiles, ArrayList<String> commitIds) {
		super();				
		mapAuthorsToColors(authorList);		
		displayFiles = new FileDisplayContainer(repfiles, this);		
	}
	
	@Override
	public void setup() {
		//resizability
		if(frame != null){
			frame.setResizable(true);
		}	
		
		//horizontal scroll bar
		int hscrollOffset = (int) (Constants.lineStripeHeight + 
			Constants.fileDisplayStartY + Constants.horizontalScrollBarOffset);
		hscroll = new HorizontalScrollBar(Constants.horizontalScrollBarX, 
				hscrollOffset, Constants.scrollBarWidth, Constants.scrollBarHeight,
				Constants.scrollBarLooseness, this);
		hzoom = new Zoom(Constants.zoomX, hscrollOffset+Constants.zoomOffset,
				Constants.zoomWidth, Constants.zoomHeight, Constants.zoomLooseness, this);
		int toffset = hscrollOffset + Constants.zoomOffset + Constants.zoomWidth + Constants.timeOffset;
		timescroll = new TimeLine(Constants.timeX, toffset,
				Constants.timeWidth, Constants.timeHeight, Constants.timeLooseness, this);
	}
		
	@Override
	public void draw() {
		//refresh screen
		background(Constants.white);	
		//display files
		hzoom.updateZoomRatio();
		int filesPosX = Constants.fileDisplayStartX -
			(int)(hscroll.getSliderPos()*displayFiles.getLength());
		displayFiles.display(filesPosX,Constants.fileDisplayStartY);			
		//display scroll bar
		hscroll.display();
		hzoom.display();
		timescroll.display();
	}
	
	//get corresponding color for author
	public int getAuthorColor(String author){
		return authorColorScheme.get(author);
	}
	
	private void mapAuthorsToColors(ArrayList<String> authorList) {
		//create map of authors to colours
		if(authorList == null){
			System.exit(ERROR);
		}
		//get number of authors
		int numAuthors = authorList.size();	
		//get number of available colours
		int numColors = 0;
		for(@SuppressWarnings("unused") Colour c : Colour.values()){
			numColors += 1;
		}
		
		//map author to colours
		ArrayList<Integer> requiredColors = new ArrayList<Integer>();
		if(numAuthors > numColors){
			int perColor = (int)Math.ceil((double)numAuthors/numColors);
			for(Colour c : Colour.values()){
				requiredColors.add(c.get());
			}
			for(Colour c : Colour.values()){
				int originalColor = c.get();
				for(int i = perColor; i > 1; i--){
					int offset = (int)(Colour.offset()*((double)i/perColor));
					int currentColor = originalColor;
					for(int j = 4; j >= 0; j -= 2){
						int temp = Math.max(((int)Math.pow(0x10,j))*offset,0x0);
						currentColor -= temp;
					}
					requiredColors.add(currentColor);
				}
			}
		}
		else if(numAuthors <= numColors){
			for(Colour c : Colour.values()){
				requiredColors.add(c.get());
			}
		}

		//assign colour to each author
		if(requiredColors.size() < numAuthors) System.exit(ERROR);
		authorColorScheme = new HashMap<String,Integer>();
		Iterator<String> authIter = authorList.iterator();
		Iterator<Integer> colorIter = requiredColors.iterator();
		while(authIter.hasNext()){
			String author = authIter.next();
			Integer colr = colorIter.next();
			authorColorScheme.put(author,colr);
		}
	}


	
}
