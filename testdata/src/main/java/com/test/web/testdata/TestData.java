package com.test.web.testdata;

public class TestData {

	public static final String CSS =
		"h1 {\n" +
		" /* comment */ /* another comment */ width : 20%;\n" +
		"  height /* comment */ /* another comment */ : 100px;\n" +
		"  background-color : /* comment */ /* another comment */ #AABBCC;\n" +
		"}\n" +
		
		"/* This is a comment */\n" +

		"#an_element .and_a_class {\n" +
		"  margin-left : 10;\n" +
		"  margin-right : auto;\n" +
		"  float: left;\n" +
		"  position: relative;\n" +
		"}\n" +

		"#another-element {\n" +
		"  margin : auto;\n" +
		"}\n" +

		".a_class #an_element {\n" +
		"  position : absolute;\n" +
		"  float : right;\n" +
		"  padding-top : 30px;\n" +
		"  text-align : left;\n" +
		"}\n";				

	public static final String CSS_MARGINS =
			"#margin_auto {\n" +
			"  margin : auto;\n" +
			"}\n" +

			"#margin_1 {\n" +
			"  margin : 25px;\n" +
			"}\n" +

			"#margin_2 {\n" +
			"  margin : 50px 25px;\n" +
			"}\n" +

			"#margin_3 {\n" +
			"  margin : 45px 30px 40px;\n" +
			"}\n" +

			"#margin_4 {\n" +
			"  margin : 35px 15px 30px 20px;\n" +
			"}\n";	


	public static final String CSS_FONTSIZE =
			"#fontsize_medium {\n" +
			"  font-size : medium;\n" +
			"}\n" +

			"#fontsize_20px {\n" +
			"  font-size : 20px;\n" +
			"}\n";

	public static final String CSS_FONTWEIGHT =
			"#fontweight_normal {\n" +
			"  font-weight : normal;\n" +
			"}\n" +

			"#fontweight_300 {\n" +
			"  font-weight : 300;\n" +
			"}\n";

	public static final String CSS_COLORS =
			"#color_rgba1 {\n" +
			"  color : rgba(125, 234, 83, 1);\n" +
			"}\n" +

			"#color_rgba2 {\n" +
			"  color : rgba(134, 76, 223, .1);\n" +
			"}\n" +
	
			"#color_rgba3 {\n" +
			"  color : rgba(63, 89, 23, 0.015);\n" +
			"}\n" +

			"#color_rgb {\n" +
			"  color : rgb(39, 43, 24);\n" +
			"}\n" +
			
			"#color_turquoise {\n" +
			"  color : turquoise;\n" +
			"}\n" +

			"#color_initial {\n" +
			"  color : initial;\n" +
			"}\n"
			;
	
	public static final String CSS_BG_COLORS =
			"#bgcolor_rgba1 {\n" +
			"  background-color : rgba(125, 234, 83, 1);\n" +
			"}\n" +

			"#bgcolor_rgba2 {\n" +
			"  background-color : rgba(134, 76, 223, .1);\n" +
			"}\n" +
	
			"#bgcolor_rgba3 {\n" +
			"  background-color : rgba(63, 89, 23, 0.015);\n" +
			"}\n" +

			"#bgcolor_rgb {\n" +
			"  background-color : rgb(39, 43, 24);\n" +
			"}\n" +
			
			"#bgcolor_turquoise {\n" +
			"  background-color : turquoise;\n" +
			"}\n" +

			"#bgcolor_initial {\n" +
			"  background-color : transparent;\n" +
			"}\n"
			;



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
