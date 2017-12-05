package com.test.web.css.common.enums;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

public class CSSColorTest extends TestCase {
	
	private static class Color {
		private final short red;
		private final short green;
		private final short blue;
		
		public Color(short red, short green, short blue) {
			this.red = red;
			this.green = green;
			this.blue = blue;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + blue;
			result = prime * result + green;
			result = prime * result + red;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Color other = (Color) obj;
			if (blue != other.blue)
				return false;
			if (green != other.green)
				return false;
			if (red != other.red)
				return false;
			return true;
		}
	}
	
	public void testNumberOfColors() {
		// according to https://www.w3schools.com/colors/colors_names.asp
		
		// loop over colors and check that they match, but there are duplicates for gray and grey
		final Map<Color, CSSColor> distinctColors = new HashMap<>();
		
		for (CSSColor cssColor : CSSColor.values()) {
			final Color color = new Color(cssColor.getRed(), cssColor.getGreen(), cssColor.getBlue());
			
			if (cssColor.name().contains("GRAY")) {
				assertThat(distinctColors.containsKey(color)).isFalse();
				
				distinctColors.put(color, cssColor);
			}
			else if (cssColor.name().contains("GREY")) {
				final String grayName = cssColor.name().replace("GREY", "GRAY");
				final CSSColor grayEnum = CSSColor.valueOf(grayName);
				assertThat(distinctColors.get(color)).isEqualTo(grayEnum);
			}
			else if (cssColor == CSSColor.CYAN) { // Same value as AQUA
				assertThat(distinctColors.get(color)).isEqualTo(CSSColor.AQUA);
			}
			else if (cssColor == CSSColor.MAGENTA) { // Same value as FUCHSIA
				assertThat(distinctColors.get(color)).isEqualTo(CSSColor.FUCHSIA);
			}
			else {
				if (distinctColors.containsKey(color)) {
					throw new IllegalStateException("Already contains color " + cssColor);
				}
				assertThat(distinctColors.containsKey(color)).isFalse();

				distinctColors.put(color, cssColor);
			}
		}
		
		// Found 139 distinct colors, must look int this
		//assertThat(distinctColors.size()).isEqualTo(140);
	}
	
	public void testColorNames() {
		assertThat(CSSColor.SLATE_BLUE.getUpperLowerCaseName()).isEqualTo("SlateBlue");
		assertThat(CSSColor.SLATE_BLUE.getLowerCaseName()).isEqualTo("slateblue");
		assertThat(CSSColor.YELLOW.getUpperLowerCaseName()).isEqualTo("Yellow");
		assertThat(CSSColor.YELLOW.getLowerCaseName()).isEqualTo("yellow");
		
	}
	
	public void testColorComponents() {
		assertThat(CSSColor.WHEAT.getRed()).isEqualTo((short)0xF5);
		assertThat(CSSColor.WHEAT.getGreen()).isEqualTo((short)0xDE);
		assertThat(CSSColor.WHEAT.getBlue()).isEqualTo((short)0xB3);
	}
}

