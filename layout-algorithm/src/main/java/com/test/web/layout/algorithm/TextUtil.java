package com.test.web.layout.algorithm;

import com.test.web.render.common.IFont;
import com.test.web.render.common.ITextExtent;
import com.test.web.types.Pixels;

/*
 * Utilities for computing text element lengths
 * 
 */

final class TextUtil {
	
	private final ITextExtent textExtent;
	
	public TextUtil(ITextExtent textExtent) {
		this.textExtent = textExtent;
	}

    // Get text length or available width, whichever is longer
	@Deprecated // not necessary?
    int getTextLengthOrAvailableWidth(String text, int availableWidth, IFont font) {
    	
    	if (text == null) {
    		throw new IllegalArgumentException("text == null");
    	}
    	
    	if (font == null) {
    		throw new IllegalArgumentException("font == null");
    	}
    	
    	// TODO: does not have to get extent of complete text, can do an approximization to check whether > availableWidh, since text can be quite long

    	final int width = textExtent.getTextExtent(font, text);
    	
    	return availableWidth == Pixels.NONE ? width : Math.min(width, availableWidth);
    }
    
    private static int indexOfNewLine(String string) {
    	
		int lfIdx = string.indexOf('\n');
		int crIdx = string.indexOf('\r');
		
		final int ret;
		
    	if (lfIdx >= 0 && crIdx >= 0) {
    		ret = Math.min(lfIdx, crIdx);
    	}
    	else if (lfIdx >= 0) {
    		ret = lfIdx;
    	}
    	else if (crIdx >= 0) {
    		ret = crIdx;
    	}
    	else {
    		ret = -1;
    	}
  
    	return ret;
    }
    
    static class NumberOfChars {
    	private final int numberOfChars;
    	private final int width;

    	NumberOfChars(int numberOfChars, int width) {
			this.numberOfChars = numberOfChars;
			this.width = width;
		}

		int getNumberOfChars() {
			return numberOfChars;
		}

		int getWidth() {
			return width;
		}
    }
    
	NumberOfChars findNumberOfChars(String inputString, int availableWidth, IFont font) {
    	
		// Only until first newline
		final int newLineIdx = indexOfNewLine(inputString);
		
		final String string;
		
		/*
		string = newLineIdx >= 0
				? inputString.substring(0, newLineIdx)
				: inputString;
		*/
		
		if (newLineIdx >= 0) {
			throw new IllegalArgumentException("newline in inut string");
		}
		
		string = inputString;
		
		// figure out approximate number of characters
    	final int guessCharacters = availableWidth / font.getAverageWidth();
    	final int tryCharacters = Math.min(string.length(), guessCharacters);
    	final int width = textExtent.getTextExtent(font, string.substring(0, tryCharacters));
    	
    	final int ret;
    	
    	int retWidth;
    	
    	if (width > availableWidth) {
    		// try with fewer characters
    		if (tryCharacters == 0) {
    			ret = 0;
    			retWidth = 0;
    		}
    		else {
	    		int numChars = tryCharacters - 1;
	    		retWidth = -1;
	    		
	    		for (;;) {
	    			final int w = textExtent.getTextExtent(font, string.substring(0, numChars));
	    			
	    			if (w <= availableWidth) {
	    				retWidth = w;
	    				break;
	    			}
	    			
	    			-- numChars;
	    		}
	    		
	    		ret = numChars;
    		}
    	}
    	else  if (width == availableWidth) {
    		ret = tryCharacters;
    		retWidth = width;
    	}
    	else if (tryCharacters == string.length()) {
    		ret = tryCharacters;
    		retWidth = width;
    	}
    	else {
    		int numChars = tryCharacters + 1;
    		retWidth = -1;
    		
    		for (;;) {
    			final int w = textExtent.getTextExtent(font, string.substring(0, numChars));
    	
    			if (w > availableWidth) {
    				-- numChars;
    				break;
    			}
    			else if (w == availableWidth) {
   			    	break;
    			}
 
  				retWidth = w;
  				 
    			++ numChars;
    		}
    		
    		ret = numChars;
    	}
    	
    	return new NumberOfChars(ret, retWidth);
    }
    
    
    int getTextLineHeight(StackElement cur, IFont font) {
		// TODO add spacing between text lines
    	return font.getHeight();
    }
    
    @Deprecated // Should lay out one text line at a time
    int computeTextLinesHeight(String text, StackElement cur, IFont font) {
		String s = text;
		
		int height = 0;
		
		for (;;) {
		
			// For each line, find with of text
			int numChars = findNumberOfChars(s, cur.getAvailableWidth(), font).getNumberOfChars();
			
			if (numChars == 0 && !s.isEmpty()) {
				throw new IllegalStateException("No room for characters in element of width " + cur.getAvailableWidth());
			}
			
			// System.out.println("## numChars "+ numChars + " of \"" + s + "\"");
			
			height += getTextLineHeight(cur, font);
			
			if (numChars == s.length()) {
				// was room for rest of string, exit
				break;
			}
			
			s = s.substring(numChars);
		}
 
		return height;
    }
}

