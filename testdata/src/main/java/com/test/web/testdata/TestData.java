package com.test.web.testdata;

public class TestData {

	public static final String CSS =
		"h1 {\n" +
		"  width : 20%;\n" +
		"  height : 100px;\n" +
		"  background-color : #AABBCC;\n" +
		"}\n" +

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
			
	
	public static final String HTML =
	
	"<!DOCTYPE HTML PUBLIC \"-//W3C/DTD HTML 4.01 Frameset//EN\" \"http://w3.org/TR/html4/frameset.dtd\">" +				
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
}
