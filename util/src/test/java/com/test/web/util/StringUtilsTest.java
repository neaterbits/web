package com.test.web.util;

import static org.assertj.core.api.Assertions.assertThat;

import com.test.web.util.StringUtils;

import junit.framework.TestCase;

public class StringUtilsTest extends TestCase {

	public void testSplit() {
		checkSplit("", ':', "");
		checkSplit(":", ':', "", "");
		checkSplit("a:", ':', "a", "");
		checkSplit(":b", ':', "", "b");
		checkSplit("a:b", ':', "a", "b");
		checkSplit(" a : b ", ':', " a ", " b ");
	}
	
	private void checkSplit(String s, char c, String ... expected) {
		final String [] result = StringUtils.split(s, c);
		
		assertThat(result.length).isEqualTo(expected.length);
		
		for (int i = 0; i < expected.length; ++ i) {
			assertThat(result[i]).isEqualTo(expected[i]);
		}
	}
	
	public void testCountTokens() {
	    checkTokens("", 0);
        checkTokens("   ", 0);
        checkTokens("a   ", 1);
        checkTokens("  a  ", 1);
        checkTokens("a   b", 2);
        checkTokens(" a   b", 2);
        checkTokens(" a b ", 2);
        checkTokens(" a b", 2);
        checkTokens(" a b c ", 3);
        checkTokens("a b c", 3);
        checkTokens("aa b cc", 3);
        checkTokens("aa bb cc", 3);
        checkTokens(" aa b cc ", 3);
        checkTokens(" aa bb cc ", 3);
        checkTokens(" aa  bb  cc ", 3);
	}
	
	private void checkTokens(String s, int expected) {
	    assertThat(StringUtils.countTokens(s)).isEqualTo(expected);
	}
	
    public void testSplitTokens() {
        checkSplitTokens("");
        checkSplitTokens("   ");
        checkSplitTokens("a   ",        "a");
        checkSplitTokens("  a  ",       "a");
        checkSplitTokens("a   b",       "a", "b");
        checkSplitTokens(" a   b",      "a", "b");
        checkSplitTokens(" a b ",       "a", "b");
        checkSplitTokens(" a b",        "a", "b");
        checkSplitTokens(" a b c ",     "a", "b", "c");
        checkSplitTokens("a b c",       "a", "b", "c");
        checkSplitTokens("aa b cc",     "aa", "b", "cc");
        checkSplitTokens("aa bb cc",    "aa", "bb", "cc");
        checkSplitTokens(" aa b cc ",   "aa", "b", "cc");
        checkSplitTokens(" aa bb cc ",  "aa", "bb", "cc");
        checkSplitTokens(" aa  bb  cc ", "aa", "bb", "cc");
    }
    
    private void checkSplitTokens(String s, String ... expected) {
        assertThat(StringUtils.splitToTokens(s)).isEqualTo(expected);
    }

    public void testHasToken() {
        checkHasToken("", "", false);
        checkHasToken(" ", "", false);
        checkHasToken(" ", " ", false);
        checkHasToken("  ", " ", false);
        checkHasToken("",       "a", false);
        checkHasToken("   ",    "a", false);
        checkHasToken("a   ",   "a", true);
        checkHasToken("  a  ",  "a", true);
        checkHasToken("ab  ",   "a", false);
        checkHasToken("  ab",   "a", false);
        checkHasToken("  ab",   "b", false);
        checkHasToken("  ab  ", "a", false);
        checkHasToken("a   b",  "a", true);
        checkHasToken("a   b",  "b", true);
        checkHasToken(" a   b", "a", true);
        checkHasToken(" a   b", "b", true);
        checkHasToken(" a b ",  "a", true);
        checkHasToken(" a b ",  "a", true);
        checkHasToken(" a b ",  "ab", false);
        
        checkHasToken("aa b cc",     "aa", true);
        checkHasToken("aa b cc",     "a", false);
        checkHasToken("aa b cc",     "b", true);
        checkHasToken("aa b cc",     "bb", false);
        checkHasToken("aa b cc",     "c", false);
        checkHasToken("aa b cc",     "cc", true);

        checkHasToken(" aa  bb  cc ", "aa", true);
        checkHasToken(" aa  bb  cc ", "a", false);
        checkHasToken(" aa  bb  cc ", "bb", true);
        checkHasToken(" aa  bb  cc ", "b", false);
        checkHasToken(" aa  bb  cc ", "cc", true);
        checkHasToken(" aa  bb  cc ", "c", false);
    }
    
    private void checkHasToken(String s, String token, boolean hasToken) {
        assertThat(StringUtils.hasToken(s, token)).isEqualTo(hasToken);
    }

    public void testRemoveTokens() {
        checkRemoveTokens("", "", null);
        checkRemoveTokens(" ", "", null);
        checkRemoveTokens(" ", " ", null);
        checkRemoveTokens("  ", " ", null);
        checkRemoveTokens("",       "a", null);
        checkRemoveTokens("   ",    "a", null);
        checkRemoveTokens("a   ",   "a", "");
        checkRemoveTokens("  a  ",  "a", "");
        checkRemoveTokens("ab  ",   "a", null);
        checkRemoveTokens("  ab",   "a", null);
        checkRemoveTokens("  ab",   "b", null);
        checkRemoveTokens("  ab",   "ab", "");
        checkRemoveTokens("  ab  ", "a", null);
        checkRemoveTokens("a   b",  "a", "b");
        checkRemoveTokens("a   b ",  "b", "a");
        checkRemoveTokens(" a   b", "a", "b");
        checkRemoveTokens(" a   b", "b", "a");
        checkRemoveTokens(" a b ",  "a", "b");
        checkRemoveTokens(" a b ",  "a", "b");
        checkRemoveTokens(" a b ",  "ab", null);
        
        checkRemoveTokens("aa b cc",     "aa", "b cc");
        checkRemoveTokens("aa b cc",     "a", null);
        checkRemoveTokens("aa b cc",     "b", "aa cc");
        checkRemoveTokens("aa b cc",     "bb", null);
        checkRemoveTokens("aa b cc",     "c", null);
        checkRemoveTokens("aa b cc",     "cc", "aa b");

        checkRemoveTokens(" aa  bb  cc ", "aa", "bb cc");
        checkRemoveTokens(" aa  bb  cc ", "a", null);
        checkRemoveTokens(" aa  bb  cc ", "bb", "aa cc");
        checkRemoveTokens(" aa  bb  cc ", "b", null);
        checkRemoveTokens(" aa  bb  cc ", "cc", "aa bb");
        checkRemoveTokens(" aa  bb  cc ", "c", null);

        checkRemoveTokens(" aa  bb  cc ", new String [] { "aa", "c"  }, "bb cc");
        checkRemoveTokens(" aa  bb  cc ", new String [] { "bb", "cc" }, "aa");
        checkRemoveTokens(" aa  bb  cc ", new String [] { "cc", "bb" }, "aa");

        checkRemoveTokens(" aa  bb  cc ", new String [] { "aa", "bb", "cc"  }, "");
    }

    private void checkRemoveTokens(String s, String token, String expected) {
        assertThat(StringUtils.removeTokens(s, token)).isEqualTo(expected);
    }

    private void checkRemoveTokens(String s, String [] tokens, String expected) {
        assertThat(StringUtils.removeTokens(s, tokens)).isEqualTo(expected);

    }

    public void testReplaceTokens() {
        checkReplaceToken("", "", "xyz",    null);
        checkReplaceToken(" ", "", "xyz",   null);
        checkReplaceToken(" ", " ", "xyz",  null);
        checkReplaceToken("  ", " ", "xyz", null);
        checkReplaceToken("",       "a", "xyz", null);
        checkReplaceToken("   ",    "a", "xyz", null);
        checkReplaceToken("a   ",   "a", "xyz", "xyz");
        checkReplaceToken("  a  ",  "a", "xyz", "xyz");
        checkReplaceToken("ab  ",   "a", "xyz", null);
        checkReplaceToken("  ab",   "a", "xyz", null);
        checkReplaceToken("  ab",   "b", "xyz", null);
        checkReplaceToken("  ab",   "ab", "xyz", "xyz");
        checkReplaceToken("  ab  ", "a", "xyz", null);
        checkReplaceToken("a   b",  "a", "xyz", "xyz b");
        checkReplaceToken("a   b ",  "b", "xyz", "a xyz");
        checkReplaceToken(" a   b", "a", "xyz", "xyz b");
        checkReplaceToken(" a   b", "b", "xyz", "a xyz");
        checkReplaceToken(" a b ",  "a", "xyz", "xyz b");
        checkReplaceToken(" a b ",  "a", "xyz", "xyz b");
        checkReplaceToken(" a b ",  "ab", "xyz",null);
        
        checkReplaceToken("aa b cc",     "aa",  "xyz", "xyz b cc");
        checkReplaceToken("aa b cc",     "a",   "xyz", null);
        checkReplaceToken("aa b cc",     "b",   "xyz", "aa xyz cc");
        checkReplaceToken("aa b cc",     "bb",  "xyz", null);
        checkReplaceToken("aa b cc",     "c",   "xyz", null);
        checkReplaceToken("aa b cc",     "cc",  "xyz", "aa b xyz");

        checkReplaceToken(" aa  bb  cc ", "aa", "xyz",  "xyz bb cc");
        checkReplaceToken(" aa  bb  cc ", "a",  "b",     null);
        checkReplaceToken(" aa  bb  cc ", "bb", "123",  "aa 123 cc");
        checkReplaceToken(" aa  bb  cc ", "b",  "xyz",  null);
        checkReplaceToken(" aa  bb  cc ", "cc", "dd",   "aa bb dd");
        checkReplaceToken(" aa  bb  cc ", "c",  "d",    null);
    }

    private void checkReplaceToken(String s, String oldToken, String newToken, String expected) {
        assertThat(StringUtils.replaceToken(s, oldToken, newToken)).isEqualTo(expected);
    }

    public void testToggleTokens() {
        
        checkToggleToken("   ",    "a", true, "a", true);
        checkToggleToken("   ",    "a", false, "", false);
        
        checkToggleToken("", "", null, "",    true);
        checkToggleToken("", "", true, "",    true);
        checkToggleToken("", "", false, "",    false);
        checkToggleToken(" ", "", null, "",   true);
        checkToggleToken(" ", " ", null, " ",  true);
        checkToggleToken("  ", " ", null, " ", true);

        checkToggleToken("",       "a", null, "a", true);
        checkToggleToken("   ",    "a", null, "a", true);
        checkToggleToken("   ",    "a", null, "a", true);
        checkToggleToken("a   ",   "a", null, "", false);
        checkToggleToken("a   ",   "a", true, "a", true);
        checkToggleToken("a   ",   "a", false, "", false);
        
        checkToggleToken("  a  ",  "a", null, "", false);
        checkToggleToken("ab  ",   "a", null, "ab a", true);
        checkToggleToken("ab  ",   "a", true, "ab a", true);
        checkToggleToken("ab  ",   "a", false, "ab", false);
        checkToggleToken("  ab",   "a", null, "ab a", true);
        checkToggleToken("  ab",   "b", null, "ab b", true);
        checkToggleToken("  ab",   "ab", null, "", false);
        checkToggleToken("  ab",   "ab", true, "ab", true);
        checkToggleToken("  ab",   "ab", false, "", false);
        checkToggleToken("  ab  ", "a", null, "ab a", true);
        checkToggleToken("a   b",  "a", null, "b", false);
        checkToggleToken("a   b ", "b", null, "a", false);
        checkToggleToken(" a   b", "a", null, "b", false);
        checkToggleToken(" a   b", "b", null, "a", false);
        checkToggleToken(" a b ",  "a", null, "b", false);
        checkToggleToken(" a b ",  "a", null, "b", false);
        checkToggleToken(" a b ",  "ab", null, "a b ab", true);
        
        checkToggleToken("aa b cc",     "aa",  null, "b cc", false);
        checkToggleToken("aa b cc",     "a",   null, "aa b cc a", true);
        checkToggleToken("aa b cc",     "b",   null, "aa cc", false);
        checkToggleToken("aa b cc",     "bb",  null, "aa b cc bb", true);
        checkToggleToken("aa b cc",     "c",   null, "aa b cc c", true);
        checkToggleToken("aa b cc",     "cc",  null, "aa b", false);

        checkToggleToken(" aa  bb  cc ", "aa", null,  "bb cc", false);
        checkToggleToken(" aa  bb  cc ", "a",  null,  "aa bb cc a", true);
        checkToggleToken(" aa  bb  cc ", "a",  true,  "aa bb cc a", true);
        checkToggleToken(" aa  bb  cc ", "a",  false,  "aa bb cc", false);
        checkToggleToken(" aa  bb  cc ", "bb", null,  "aa cc", false);
        checkToggleToken(" aa  bb  cc ", "bb", true,  "aa bb cc", true);
        checkToggleToken(" aa  bb  cc ", "bb", false,  "aa cc", false);
        checkToggleToken(" aa  bb  cc ", "b",  null,  "aa bb cc b", true);
        checkToggleToken(" aa  bb  cc ", "cc", null,  "aa bb", false);
        checkToggleToken(" aa  bb  cc", "c",  null,  "aa bb cc c", true);
    }

    private void checkToggleToken(String s, String token, Boolean force, String expectedString, boolean expectedStillInList) {
        
        final StringBuilder sb = new StringBuilder();
        final boolean stillInList = StringUtils.toggleToken(s, sb, token, force);

        assertThat(sb.toString()).isEqualTo(expectedString);
        assertThat(stillInList).isEqualTo(expectedStillInList);
    }
}
