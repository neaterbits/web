package com.test.web.layout;

import com.test.web.css.common.enums.CSSDisplay;
import com.test.web.render.common.IDelayedRenderer;
import com.test.web.render.common.IFont;

final class ElementLayout implements IElementRenderLayout {
	
	// display class for this element
	private CSSDisplay display;
	
	// font used to render this element
	private IFont font;
	
	// whether width/height was specified in CSS
	private boolean hasCSSWidth;
	private boolean hasCSSHeight;

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

		this.display = toCopy.display;
		this.font = toCopy.font;
		this.hasCSSWidth = toCopy.hasCSSWidth;
		this.hasCSSHeight = toCopy.hasCSSHeight;
		
		this.outer = toCopy.outer.makeCopy();
		this.absolute = toCopy.outer.makeCopy();
		this.inner = toCopy.inner.makeCopy();
		this.margin = toCopy.margin.makeCopy();
		this.padding = toCopy.padding.makeCopy();
		
		this.sumWidth = toCopy.sumWidth;
		this.sumHeight = toCopy.sumHeight;
		
		this.renderer = toCopy.renderer;
	}
	
	ElementLayout makeCopy() {
		return new ElementLayout(this);
	}

	CSSDisplay getDisplay() {
		return display;
	}

	void setDisplay(CSSDisplay display) {
		this.display = display;
	}
	
	void clear() {
		this.display = null;
		this.renderer = null;
		this.sumWidth = sumHeight = 0;
		this.boundsComputed = false;
	}
	
	void setRenderer(int zIndex, IDelayedRenderer renderer) {
		
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

	void setFont(IFont font) {
		this.font = font;
	}
	
	@Override
	public int getZIndex() {
		return zIndex;
	}

	void setHasCSSWidth(boolean hasCSSWidth) {
		this.hasCSSWidth = hasCSSWidth;
	}

	void setHasCSSHeight(boolean hasCSSHeight) {
		this.hasCSSHeight = hasCSSHeight;
	}

	public boolean hasCSSWidth() {
		return hasCSSWidth;
	}

	public boolean hasCSSHeight() {
		return hasCSSHeight;
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

	void setBoundsComputed() {
		if (boundsComputed) {
			throw new IllegalStateException("bounds computed twice");
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
	public String toString() {
		return "ElementLayout [display=" + display + ", font=" + font + ", hasCSSWidth=" + hasCSSWidth
				+ ", hasCSSHeight=" + hasCSSHeight + ", sumWidth=" + sumWidth + ", sumHeight=" + sumHeight
				+ ", dimensions=" + outer + ", margin=" + margin + ", padding=" + padding + ", inner=" + inner
				+ ", renderer=" + renderer + "]";
	}
}
