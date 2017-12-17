package com.test.web.document.oo;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import static org.assertj.core.api.Assertions.assertThat;

public class ClassAttributeParserTest extends TestCase {

	public void testEmpty() {
		String [] entries = parse("");
		assertThat(entries.length).isEqualTo(0);

		entries = parse(" ");
		assertThat(entries.length).isEqualTo(0);
	}

	public void testOne() {
		String [] entries = parse("value1");
		assertThat(entries.length).isEqualTo(1);
		assertThat(entries[0]).isEqualTo("value1");
		
		entries = parse(" value1");
		assertThat(entries.length).isEqualTo(1);
		assertThat(entries[0]).isEqualTo("value1");
		
		entries = parse("value1 ");
		assertThat(entries.length).isEqualTo(1);
		assertThat(entries[0]).isEqualTo("value1");
		
		entries = parse(" value1 ");
		assertThat(entries.length).isEqualTo(1);
		assertThat(entries[0]).isEqualTo("value1");
	}

	public void testTwo() {
		String [] entries = parse("value1 value2");
		assertThat(entries.length).isEqualTo(2);
		assertThat(entries[0]).isEqualTo("value1");
		assertThat(entries[1]).isEqualTo("value2");
		
		entries = parse("value1  value2");
		assertThat(entries.length).isEqualTo(2);
		assertThat(entries[0]).isEqualTo("value1");
		assertThat(entries[1]).isEqualTo("value2");

		entries = parse(" value1 value2");
		assertThat(entries.length).isEqualTo(2);
		assertThat(entries[0]).isEqualTo("value1");
		assertThat(entries[1]).isEqualTo("value2");
		
		entries = parse("value1 value2 ");
		assertThat(entries.length).isEqualTo(2);
		assertThat(entries[0]).isEqualTo("value1");
		assertThat(entries[1]).isEqualTo("value2");
		
		entries = parse(" value1 value2");
		assertThat(entries.length).isEqualTo(2);
		assertThat(entries[0]).isEqualTo("value1");
		assertThat(entries[1]).isEqualTo("value2");
	}

	private String [] parse(String s) {
		final List<String> items = new ArrayList<>();
		
		ClassAttributeParser.parseClassAttribute(s, item -> items.add(item));
		
		return items.toArray(new String[items.size()]);
	}
}
