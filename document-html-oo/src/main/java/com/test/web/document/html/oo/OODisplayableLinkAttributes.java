package com.test.web.document.html.oo;

import com.test.web.document.html.common.HTMLAttribute;
import com.test.web.document.html.common.ICommonDisplayableLinkAttributes;
import com.test.web.document.html.common.enums.HTMLTarget;
import com.test.web.types.IEnum;
import com.test.web.util.StringUtils;

// <a> and <area> are displayable links that can delegate to this class
// cannot inherit since <a> is container while <area> is leaf element
final class OODisplayableLinkAttributes extends OOCommonLinkAttributes
	implements ICommonDisplayableLinkAttributes {

	private String download; // download filename
	
	private HTMLTarget target; // either this
	private String targetFrame; // ... or a target frame
	
	@Override
	public final String getDownload() {
		return download;
	}

	@Override
	public final void setDownload(String download) {
		this.download = download;
	}

	@Override
	public final HTMLTarget getTarget() {
		return target;
	}

	@Override
	public final void setTarget(HTMLTarget target, String targetFrame) {
		if (target != null && targetFrame != null) {
			throw new IllegalArgumentException("Cannot set both target and target frame");
		}

		this.target = target;
		this.targetFrame = targetFrame;
	}

	@Override
	public final String getTargetFrame() {
		return targetFrame;
	}
	
	String getAttributeValue(HTMLAttribute attribute) {
		
		final String value;
		
		switch (attribute) {
		case DOWNLOAD:
			value = download;
			break;
			
		case TARGET:
			if (target != null) {
				value = target.getName();
			}
			else if (targetFrame != null) {
				value = targetFrame;
			}
			else {
				value = null;
			}
			break;
			
		default:
			value = super.getAttributeValue(attribute);
			break;
		}

		return value;
	}
	
	boolean setAttributeValue(HTMLAttribute attribute, String value) {

		final String trimmed = StringUtils.trimToNull(value);
		
		boolean wasSet = value != null;
		
		switch (attribute) {
		case DOWNLOAD:
			this.download = value;
			break;
			
		case TARGET:
			if (trimmed == null) {
				target = null;
				targetFrame = null;
			}
			else {
				final HTMLTarget t = IEnum.asEnum(HTMLTarget.class, trimmed, true);
				
				if (t != null) {
					target = t;
					targetFrame = null;
				}
				else {
					target = null;
					targetFrame = trimmed;
				}
			}
			break;
			
		default:
			wasSet = super.setAttributeValue(attribute, value);
			break;
		}
		
		return wasSet;
	}
}
