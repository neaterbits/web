package com.test.web.layout;

import com.test.web.render.common.IFont;
import com.test.web.render.common.ITextExtent;

/*
 * Utiilties for computing text element lengths
 * 
 */

final class TextUtil {
	
	private final ITextExtent textExtent;
	
	public TextUtil(ITextExtent textExtent) {
		this.textExtent = textExtent;
	}

    // Get text length or available width, whichever is longer
    int getTextLengthOrAvailableWidth(String text, int availableWidth, IFont font) {
    	
    	if (text == null) {
    		throw new IllegalArgumentException("text == null");
    	}
    	
    	if (font == null) {
    		throw new IllegalArgumentException("font == null");
    	}
    	
    	// TODO: does not have to get extent of complete text, can do an approximization to check whether > availableWidh, since text can be quite long

    	final int width = textExtent.getTextExtent(font, text);
    	
    	return availableWidth == -1 ? width : Math.min(width, availableWidth);
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

	int findNumberOfChars(String inputString, int availableWidth, IFont font) {
    	
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
    	
    	if (width > availableWidth) {
    		// try with fewer characters
    		if (tryCharacters == 0) {
    			ret = 0;
    		}
    		else {
	    		int numChars = tryCharacters - 1;
	    		
	    		for (;;) {
	    			final int w = textExtent.getTextExtent(font, string.substring(0, numChars));
	    			
	    			if (w <= availableWidth) {
	    				break;
	    			}
	    			
	    			-- numChars;
	    		}
	    		
	    		ret = numChars;
    		}
    	}
    	else  if (width == availableWidth) {
    		ret = tryCharacters;
    	}
    	else if (tryCharacters == string.length()) {
    		ret = tryCharacters;
    	}
    	else {
    		int numChars = tryCharacters + 1;
    		
    		for (;;) {
    			final int w = textExtent.getTextExtent(font, string.substring(0, numChars));
    			
    			if (w > availableWidth) {
    				-- numChars;
    				break;
    			}
    			else if (w == availableWidth) {
    				break;
    			}
    			
    			++ numChars;
    		}
    		
    		ret = numChars;
    	}
    	
    	return ret;
    }
    
    
    int getTextLineHeight(StackElement cur, IFont font) {
		// TODO add spacing between text lines
    	return font.getHeight();
    }
}

