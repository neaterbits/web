package com.test.web.testdata;

public class TestData {

	public static final String CSS =
		"h1 {\n" +
		" /* comment */ /* another comment */ width : 20%;\n" +
		"  height /* comment */ /* another comment */ : 100px;\n" +
		"  background-color : /* comment */ /* another comment */ #AABBCC;\n" +
		"}\n" +
		
		"/* This is a comment */\n" +

		"#an_element {\n" +
		"  margin-left : 10;\n" +
		"  margin-right : auto;\n" +

		"  float: left;\n" +
		"  position: relative;\n" +
		"}\n" +

		".a_class {\n" +
		"  position : absolute;\n" +
		"  float : right;\n" +
		"  padding-top : 30px;\n" +
		"}\n";				
			
	
	private static final String DOCTYPE = "<!DOCTYPE HTML PUBLIC \"-//W3C/DTD HTML 4.01 Frameset//EN\" \"http://w3.org/TR/html4/frameset.dtd\">";
	
	public static final String HTML =
	
	DOCTYPE  + "\n" +			
	"<html>\n" +
	"<!-- a single line comment -->\n" +
	"<head>\n" +
	"  <title id=\"title_id\" class=\"title_class\">Document Title</title>\n" +
	"  <link rel=\"stylesheet\" type=\"text/css\" href=\"test_stylesheet.css\"/>\n" +
	"  <script id=\"script_id\" type=\"text/javascript\">\n" +
	"    function func() {\n" +
	"    }\n" +
	"  </script>\n" +
	"</head>\n" +
	"<!-- a \n" +
	" multi line \n" +
	" comment -->\n" +
	" <body>\n" +
	"   <div id=\"main_div\">\n" +
	"     Some text before span\n" +
	"     <span class=\"text_content\">This is a text</span>\n" +
	"     Some text after span\n" +
	"   </div>\n" +
	" </body>\n" +
	"</html>";
	
	public static String wrap(String htmlBody) {
		return DOCTYPE + "\n" +
				"<html>\n" +
				"<head>\n" +
				"</head>\n" +
				" <body>\n" +

				htmlBody +

				" </body>\n" +
				"</html>";
		
	}
}
