package com.test.web.css.common;

import com.test.web.css.common.enums.CSSColorGamut;
import com.test.web.css.common.enums.CSSDisplayMode;
import com.test.web.css.common.enums.CSSHover;
import com.test.web.css.common.enums.CSSInvertedColors;
import com.test.web.css.common.enums.CSSLightLevel;
import com.test.web.css.common.enums.CSSMediaFeature;
import com.test.web.css.common.enums.CSSMediaType;
import com.test.web.css.common.enums.CSSOrientation;
import com.test.web.css.common.enums.CSSOverflowBlock;
import com.test.web.css.common.enums.CSSOverflowInline;
import com.test.web.css.common.enums.CSSPointer;
import com.test.web.css.common.enums.CSSRange;
import com.test.web.css.common.enums.CSSScan;
import com.test.web.css.common.enums.CSSScripting;
import com.test.web.css.common.enums.CSSUnit;
import com.test.web.css.common.enums.CSSUpdate;
import com.test.web.types.Ratio;

// Access to media queries within all rule types that support those
public interface CSSDocumentMediaQueries {
	
	@FunctionalInterface
	public interface GetDimension {
		void onDimension(int value, CSSUnit unit, CSSRange range);
	}
	
	@FunctionalInterface
	public interface GetAspectRatio {
		void onAspectRatio(Ratio value, CSSRange range);
	}
	
	@FunctionalInterface
	public interface GetIntegerValue {
		void onIntegerValue(int value, CSSRange range);
	}

	int getNumMediaQueries(int ruleIdx);
	
	boolean isMediaQueryNegated(int ruleIdx, int mediaQueryIdx);
	
	// null if no mediatype
	CSSMediaType getMediaType(int ruleIdx, int mediaQueryIdx);
	
	int getNumMediaFeatures(int ruleIdx, int mediaQueryIdx, int [] nesting);

	CSSMediaFeature getMediaFeature(int ruleIdx, int mediaQueryIdx, int [] nesting);
	
	void getWidth(int ruleIdx, int mediaQueryIdx, int [] nesting, GetDimension getter);
	void getHeight(int ruleIdx, int mediaQueryIdx, int [] nesting, GetDimension getter);
	void getAspectRatio(int ruleIdx, int mediaQueryIdx, int [] nesting, GetAspectRatio getter);
	CSSOrientation getOrientation(int ruleIdx, int mediaQueryIdx, int [] nesting);
	void getResolution(int ruleIdx, int mediaQueryIdx, int [] nesting, GetIntegerValue getter);
	CSSScan getScan(int ruleIdx, int mediaQueryIdx, int [] nesting, GetIntegerValue getter);
	boolean getGrid(int ruleIdx, int mediaQueryIdx, int [] nesting);
	CSSUpdate getUpdate(int ruleIdx, int mediaQueryIdx, int [] nesting);
	CSSOverflowBlock getOverflowBlock(int ruleIdx, int mediaQueryIdx, int [] nesting);
	CSSOverflowInline getOverflowInline(int ruleIdx, int mediaQueryIdx, int [] nesting);
	void getColor(int ruleIdx, int mediaQueryIdx, int [] nesting, GetIntegerValue getter);
	CSSColorGamut getColorGamut(int ruleIdx, int mediaQueryIdx, int [] nesting);
	void getColorIndex(int ruleIdx, int mediaQueryIdx, int [] nesting, GetIntegerValue getter);
	CSSDisplayMode getDisplayMode(int ruleIdx, int mediaQueryIdx, int [] nesting);
	void getMonocrome(int ruleIdx, int mediaQueryIdx, int [] nesting, GetIntegerValue getter);
	CSSInvertedColors getInvertedColors(int ruleIdx, int mediaQueryIdx, int [] nesting);
	CSSPointer getPointer(int ruleIdx, int mediaQueryIdx, int [] nesting);
	CSSHover getHover(int ruleIdx, int mediaQueryIdx, int [] nesting);
	CSSPointer getAnyPointer(int ruleIdx, int mediaQueryIdx, int [] nesting);
	CSSHover getAnyHover(int ruleIdx, int mediaQueryIdx, int [] nesting);
	CSSLightLevel getLightLevel(int ruleIdx, int mediaQueryIdx, int [] nesting);
	CSSScripting getScripting(int ruleIdx, int mediaQueryIdx, int [] nesting);
	void getDeviceWidth(int ruleIdx, int mediaQueryIdx, int [] nesting, GetDimension getter);
	void getDeviceHeight(int ruleIdx, int mediaQueryIdx, int [] nesting, GetDimension getter);
	void getDeviceAspectRatio(int ruleIdx, int mediaQueryIdx, int [] nesting, GetAspectRatio getter);
}
