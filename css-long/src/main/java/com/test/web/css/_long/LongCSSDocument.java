package com.test.web.css._long;


import java.util.HashMap;
import java.util.Map;

import com.test.web.buffers.LongBuffersIntegerIndex;
import com.test.web.css.common.CSSDisplay;
import com.test.web.css.common.CSSFloat;
import com.test.web.css.common.CSSTarget;
import com.test.web.css.common.CSSUnit;
import com.test.web.css.common.CSStyle;
import com.test.web.css.common.ICSSJustify;
import com.test.web.css.common.Justify;
import com.test.web.io._long.LongTokenizer;
import com.test.web.parse.css.CSSOverflow;
import com.test.web.parse.css.CSSParserListener;
import com.test.web.parse.css.CSSPosition;
import com.test.web.parse.css.CSSTextAlign;

/***
 * For storing and accessing a CSS document, encoding in arrays of long [] for less GC and memory churn
 * 
 * Must be singlethreaded during parsing
 * 
 * @author nhl
 *
 */

public final class LongCSSDocument extends LongBuffersIntegerIndex implements CSSParserListener<LongTokenizer, Void>, ICSSDocument<Integer> {

	// CSS types by element tag, ID and class
	private Map<String, Integer> byTag;
	private Map<String, Integer> byId;
	private Map<String, Integer> byClass;
	

	private int curParseElement;

	private Map<String, Integer> getMap(CSSTarget target) {
		final Map<String, Integer> map;
		
		switch (target) {
		case TAG:
			if (this.byTag == null) {
				this.byTag = new HashMap<>();
			}
			map = this.byTag;
			break;
			
		case ID:
			if (this.byId == null) {
				this.byId = new HashMap<>();
			}
			map = this.byId;
			break;

		case CLASS:
			if (this.byClass == null) {
				this.byClass = new HashMap<>();
			}
			map = this.byClass;
			break;
			
		default:
			throw new IllegalArgumentException("Unexpected target " + target);
		}

		return map;
	}
	
	/***************************************************** Access interface *****************************************************/ 
	

	@Override
	public boolean isSet(CSSTarget target, String targetName, CSStyle style) {

		final Integer entry = getMap(target).get(targetName);

		final boolean isSet;

		if (entry == null) {
			isSet = false;
		}
		else {
			isSet = LongCSS.hasStyle(style, buf(entry), offset(entry));
		}

		return isSet;
	}
	
	
	@Override
	public boolean isSet(Integer ref, CSStyle style) {
		return LongCSS.hasStyle(style, buf(ref), offset(ref));
	}

	@Override
	public Integer get(CSSTarget target, String targetName) {
		return getMap(target).get(targetName);
	}

	@Override
	public int getWidth(Integer ref) {
		return LongCSS.getWidth(buf(ref), offset(ref));
	}

	@Override
	public CSSUnit getWidthUnit(Integer ref) {
		return LongCSS.getWidthUnit(buf(ref), offset(ref));
	}

	@Override
	public int getHeight(Integer ref) {
		return LongCSS.getHeight(buf(ref), offset(ref));
	}

	@Override
	public CSSUnit getHeightUnit(Integer ref) {
		return LongCSS.getHeightUnit(buf(ref), offset(ref));
	}

	@Override
	public <PARAM> void getMargins(Integer ref, ICSSJustify<PARAM> setter, PARAM param) {
		LongCSS.getMargin(buf(ref), offset(ref), setter, param);
	}

	@Override
	public <PARAM> void getPadding(Integer ref, ICSSJustify<PARAM> setter, PARAM param) {
		LongCSS.getPadding(buf(ref), offset(ref), setter, param);
	}

	@Override
	public CSSDisplay getDisplay(Integer ref) {
		return LongCSS.getDisplay(buf(ref), offset(ref));
	}

	@Override
	public CSSPosition getPosition(Integer ref) {
		return LongCSS.getPosition(buf(ref), offset(ref));
	}

	@Override
	public CSSFloat getFloat(Integer ref) {
		return LongCSS.getFloat(buf(ref), offset(ref));
	}

	@Override
	public CSSTextAlign getTextAlign(Integer ref) {
		return LongCSS.getTextAlign(buf(ref), offset(ref));
	}

	@Override
	public CSSOverflow getOverflow(Integer ref) {
		return LongCSS.getOverflow(buf(ref), offset(ref));
	}

	/***************************************************** Parse listener *****************************************************/ 
	
	
	
	@Override
	public Void onEntityStart(CSSTarget target, String targetName) {
		
		if (targetName == null || targetName.isEmpty()) {
			throw new IllegalArgumentException("no target name");
		}
		
		final Map<String, Integer> map;
		
		switch (target) {
		case TAG:
			if (this.byTag == null) {
				this.byTag = new HashMap<>();
			}
			map = this.byTag;
			break;
			
		case ID:
			if (this.byId == null) {
				this.byId = new HashMap<>();
			}
			map = this.byId;
			break;

		case CLASS:
			if (this.byClass == null) {
				this.byClass = new HashMap<>();
			}
			map = this.byClass;
			break;
			
		default:
			throw new IllegalArgumentException("Unexpected target " + target);
		}
		
		
		// Started a CSS entity, must allocate out of buffer
		// Start out allocating a compact element, we might re-allocate if turns out to have more than the standard compact elements
		
		this.curParseElement = allocate(LongCSS.CSS_ENTITY_COMPACT, target.name());
		
		// Store in map
		if (map.containsKey(targetName)) {
			throw new IllegalStateException("TODO: handle multiple instanes for same target name");
		}
		
		map.put(targetName, this.curParseElement);

		return null;
	}

	@Override
	public void onEntityEnd(Void context) {
		// Nothing to do as write pos in buffer was already advanced
	}

	@Override
	public void onWidth(Void context, int width, CSSUnit unit) {
		LongCSS.addWidth(buf(curParseElement), offset(curParseElement), width, unit);
	}

	@Override
	public void onHeight(Void context, int height, CSSUnit unit) {
		LongCSS.addHeight(buf(curParseElement), offset(curParseElement), height, unit);
	}

	@Override
	public void onMargin(Void context,
			int top, CSSUnit topUnit, Justify topType,
			int right, CSSUnit rightUnit, Justify rightType,
			int bottom, CSSUnit bottomUnit, Justify bottomType,
			int left, CSSUnit leftUnit, Justify leftType) {
		
		LongCSS.setMargin(buf(curParseElement), offset(curParseElement), top, topUnit, topType, right, rightUnit, rightType, bottom, bottomUnit, bottomType, left, leftUnit, leftType);
	}

	@Override
	public void onPadding(Void context,
			int top, CSSUnit topUnit, Justify topType,
			int right, CSSUnit rightUnit, Justify rightType,
			int bottom, CSSUnit bottomUnit, Justify bottomType,
			int left, CSSUnit leftUnit, Justify leftType) {
		
		LongCSS.setPadding(buf(curParseElement), offset(curParseElement), top, topUnit, topType, right, rightUnit, rightType, bottom, bottomUnit, bottomType, left, leftUnit, leftType);
	}

	@Override
	public void onBackgroundColor(Void context, int r, int g, int b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextAlign(Void context, CSSTextAlign textAlign) {
		LongCSS.setTextAlign(buf(curParseElement), offset(curParseElement), textAlign);
		
	}
	@Override
	public void onDisplay(Void context, CSSDisplay display) {
		LongCSS.setDisplay(buf(curParseElement), offset(curParseElement), display);
	}

	@Override
	public void onPosition(Void context, CSSPosition position) {
		LongCSS.setPosition(buf(curParseElement), offset(curParseElement), position);
	}

	@Override
	public void onFloat(Void context, CSSFloat _float) {
		LongCSS.setFloat(buf(curParseElement), offset(curParseElement), _float);
	}

	@Override
	public void onOverflow(Void context, CSSOverflow overflow) {
		LongCSS.setOverflow(buf(curParseElement), offset(curParseElement), overflow);
	}
}
