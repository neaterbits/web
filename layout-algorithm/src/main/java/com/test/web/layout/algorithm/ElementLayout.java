package com.test.web.layout.algorithm;

import com.test.web.layout.common.IBounds;
import com.test.web.layout.common.IElementRenderLayout;
import com.test.web.layout.common.IWrapping;
import com.test.web.layout.common.Wrapping;
import com.test.web.layout.common.enums.Display;
import com.test.web.render.common.IDelayedRenderer;
import com.test.web.render.common.IFont;

final class ElementLayout implements IElementRenderLayout, ElementLayoutSettersGetters  {
	
	private int layoutPartsSetFlag;
	
	// display class for this element
	private Display display;
	
	// font used to render this element
	private IFont font;
	
	// sum width and height of sub elements
	private int sumWidth;
	private int sumHeight;

	// dimensions/position of the resulting html element, always including margins and padding
	// so might not be same as width/height from CSS since by default padding and margin comes in addition to these
	private final Dimensions outer;

	// same as outer but positioned relative to start of document
	private final Dimensions absolute;

	// margin in pixels
	private final Wrapping margin;
	
	// padding in pixels
	private final Wrapping padding;
	
	private final Dimensions inner;

	private int zIndex;
	private IDelayedRenderer renderer;
	
	// Set to true whenever initialized outer, margin, padding and inner
	private boolean boundsComputed;

	// Offset into renderqueue for render operations for this element
	// TODO move elsewhere? Not directly related to layout
	private int renderQueueStartOffset;
	private int renderQueueEndOffset;
	
	ElementLayout() {
		this.outer = new Dimensions();
		this.absolute = new Dimensions();
		this.inner = new Dimensions();
		this.margin = new Wrapping();
		this.padding = new Wrapping();
		
		this.sumWidth = 0;
		this.sumHeight = 0;
	}
	
	private ElementLayout(ElementLayout toCopy) {

		this.layoutPartsSetFlag = toCopy.layoutPartsSetFlag;
		this.display = toCopy.display;
		this.font = toCopy.font;
		this.outer = toCopy.outer.makeCopy();
		this.absolute = toCopy.outer.makeCopy();
		this.inner = toCopy.inner.makeCopy();
		this.margin = toCopy.margin.makeCopy();
		this.padding = toCopy.padding.makeCopy();
		
		this.sumWidth = toCopy.sumWidth;
		this.sumHeight = toCopy.sumHeight;
		
		this.renderer = toCopy.renderer;
	}
	
	@Override
	public IElementRenderLayout makeCopy() {
		return new ElementLayout(this);
	}

	@Override
	public Display getDisplay() {
		return display;
	}

	void setDisplay(Display display) {
		if (display == null) {
			throw new IllegalArgumentException("display == null");
		}

		if (this.display != null) {
			throw new IllegalStateException("display already set");
		}

		this.display = display;
	}
	
	@Override
	public void clear() {
		this.layoutPartsSetFlag = 0;
		this.display = null;
		this.renderer = null;
		this.sumWidth = sumHeight = 0;
		this.boundsComputed = false;
		
		// For easier debugging
		absolute.init(-1, -1, -1, -1);
		outer.init(-1, -1, -1, -1);
		inner.init(-1, -1, -1, -1);
	}
	
	@Override
	public void setRenderer(int zIndex, IDelayedRenderer renderer) {
		
		if (renderer == null) {
			throw new IllegalArgumentException("renderer == null");
		}
		
		this.zIndex = zIndex;
		this.renderer = renderer;
	}

	@Override
	public IFont getFont() {
		return font;
	}

	@Override
	public void setFont(IFont font) {
		this.font = font;
	}
	
	@Override
	public int getZIndex() {
		return zIndex;
	}

	Dimensions getOuter() {
		return outer;
	}

	Dimensions getAbsolute() {
		return absolute;
	}

	Dimensions getInner() {
		return inner;
	}

	Wrapping getMarginWrapping() {
		return margin;
	}
	
	Wrapping getPaddingWrapping() {
		return padding;
	}

	@Override
	public void setBoundsComputed() {
		if (boundsComputed) {
			throw new IllegalStateException("bounds computed twice");
		}
		
		if (this.layoutPartsSetFlag != LayoutPart.ALL_MASK) {
			throw new IllegalStateException(String.format("Not all layout parts set: %04x", layoutPartsSetFlag));
		}

		this.boundsComputed = true;
	}

	@Override
	public boolean areBoundsComputed() {
		return boundsComputed;
	}

	@Override
	public IBounds getOuterBounds() {
		return outer;
	}

	@Override
	public IBounds getAbsoluteBounds() {
		return absolute;
	}

	@Override
	public IBounds getInnerBounds() {
		return inner;
	}

	@Override
	public IWrapping getMargins() {
		return margin;
	}

	@Override
	public IWrapping getPadding() {
		return padding;
	}

	@Override
	public IDelayedRenderer getRenderer() {
		return renderer;
	}

	@Override
	public int getRenderQueueStartOffset() {
		return renderQueueStartOffset;
	}

	@Override
	public int getRenderQueueEndOffset() {
		return renderQueueEndOffset;
	}
	
	@Override
	public void setRenderQueueOffsets(int startOffset, int endOffset) {
		this.renderQueueStartOffset = startOffset;
		this.renderQueueEndOffset   = endOffset;
	}
	
	@Override
	public IWrapping initMargins(int top, int right, int bottom, int left) {
  		getMarginWrapping().init(top, right, bottom, left);
  		
  		return getMargins();
	}
	
	@Override
	public IWrapping initPadding(int top, int right, int bottom, int left) {
    	getPaddingWrapping().init(top, right, bottom, left);

    	return getPadding();
	}

	private void markLayoutPartAsSet(LayoutPart part) {

		final int flag = part.flag();
		
		if ((this.layoutPartsSetFlag & flag) != 0) {
			throw new IllegalStateException("part already set: " + part);
		}
		
		this.layoutPartsSetFlag |= flag;
	}

	@Override
	public void initOuter(int left, int top, int width, int height) {
		getOuter().init(left, top, width, height);
		
		markLayoutPartAsSet(LayoutPart.OUTER_POSITION);
		markLayoutPartAsSet(LayoutPart.OUTER_WIDTH_HEIGHT);
	}

	@Override
	public void initInner(int left, int top, int width, int height) {
		getInner().init(left, top, width, height);

		markLayoutPartAsSet(LayoutPart.INNER_POSITION);
		markLayoutPartAsSet(LayoutPart.INNER_WIDTH_HEIGHT);
	}

	@Override
	public void initOuterPosition(int left, int top) {
		getOuter().initPosition(left, top);

		markLayoutPartAsSet(LayoutPart.OUTER_POSITION);
	}

	@Override
	public void initInnerPosition(int left, int top) {
		getInner().initPosition(left, top);

		markLayoutPartAsSet(LayoutPart.INNER_POSITION);
	}

	@Override
	public void initOuterWidthHeight(int width, int height) {
		getOuter().initWidthHeight(width, height);

		markLayoutPartAsSet(LayoutPart.OUTER_WIDTH_HEIGHT);
	}

	@Override
	public void initInnerWidthHeight(int width, int height) {
		getInner().initWidthHeight(width, height);

		markLayoutPartAsSet(LayoutPart.INNER_WIDTH_HEIGHT);
	}

	@Override
	public String toString() {
		return "ElementLayout [display=" + display + ", font=" + font 
				+ ", sumWidth=" + sumWidth + ", sumHeight=" + sumHeight
				+ ", dimensions=" + outer + ", margin=" + margin + ", padding=" + padding + ", inner=" + inner
				+ ", renderer=" + renderer + "]";
	}
}
