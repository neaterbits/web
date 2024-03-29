package com.test.web.layout.algorithm;

import java.util.function.Supplier;

import com.test.web.render.common.IFont;
import com.test.web.render.common.ITextExtent;
import com.test.web.types.Pixels;

/*
 * Utilities for computing text element lengths
 * 
 */

public final class TextUtil {
	
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
    
    public static final class NumberOfChars {
    	private final int numberOfChars;
    	private final int width;

    	NumberOfChars(int numberOfChars, int width) {
			this.numberOfChars = numberOfChars;
			this.width = width;
		}

		int getNumberOfChars() {
			return numberOfChars;
		}

		public int getWidth() {
			return width;
		}
    }

    @FunctionalInterface
   public  interface OnTextElement {
    	int onElement(String lineText, NumberOfChars numChars, int xPos, int yPos, boolean lineWrapped, boolean atStartOfLine);
    }
    
    /**
     * Split a text into lines, depending on width of some bounding box.
     * Will callback onto caller for each time text is split
     * 
     */
   public  void splitTextIntoLines(
    		String text,
    		int xPos, int yPos,
    		int lineStartPos,
    		IFont font,
    		boolean atStartOfLine,
    		
    		Supplier<Integer> getRemainingWidth,
    		OnTextElement onTextElement) {

    	String remainingText = text;

		while ( ! remainingText.isEmpty() ) {

	    	// find number of chars width regards to this line
			final int remainingWidth = getRemainingWidth.get();
			final NumberOfChars numChars = findNumberOfChars(remainingText, remainingWidth, font);

			final boolean lineWrapped;
			final String lineText;

			final int numCharsOnLine = numChars.getNumberOfChars();

			if (numCharsOnLine == 0) {
				throw new UnsupportedOperationException("TODO handle case where no room for more characters at end of line");
			}
			else if (numCharsOnLine < text.length()) {
				
				// Not enough room for all of text, which means that line wraps.
				// figure max height, baseline and render line
				lineWrapped = true;
				lineText = remainingText.substring(0, numCharsOnLine);

				remainingText = remainingText.substring(numCharsOnLine);
			}
			else {
				// space for all characters
				lineWrapped = false;
				lineText = remainingText;

				remainingText = ""; // in order to exit loop
			}
			
			final int lineHeight = onTextElement.onElement(lineText, numChars, xPos, yPos, lineWrapped, atStartOfLine);

	    	if (lineWrapped) {
				xPos = lineStartPos; // Back to start of line
				yPos += lineHeight;
				atStartOfLine = true;
	    	}
	    	else {
	    		atStartOfLine = false;
	    	}
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
    	else if (width < availableWidth) {
    		int numChars = tryCharacters + 1;
    		retWidth = -1;
    		
    		int lastW = width;
    		
    		for (;;) {
    			final int w = textExtent.getTextExtent(font, string.substring(0, numChars));
    	
    			if (w > availableWidth) {
     				retWidth = lastW;
    				// skip to last
    				-- numChars;
    				break;
    			}
    			else if (w == availableWidth) {
    				retWidth = w;
   			    	break;
    			}
 
    			lastW = w;

    			++ numChars;
    		}

    		ret = numChars;
    	}
		else {
			throw new IllegalStateException("Should not reach here");
		}
    	
    	return new NumberOfChars(ret, retWidth);
    }
}

