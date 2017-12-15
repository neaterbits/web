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

			"#margin_initial {\n" +
			"  margin : initial;\n" +
			"}\n" +

			"#margin_inherit {\n" +
			"  margin : inherit;\n" +
			"}\n" +

			"#margin_1 {\n" +
			"  margin : 25px;\n" +
			"}\n" +

			"#margin_1_0 {\n" +
			"  margin : 0;\n" +
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

	public static final String CSS_PADDING =

			"#padding_initial {\n" +
			"  padding : initial;\n" +
			"}\n" +

			"#padding_inherit {\n" +
			"  padding : inherit;\n" +
			"}\n" +

			"#padding_1 {\n" +
			"  padding : 25px;\n" +
			"}\n" +

			"#padding_2 {\n" +
			"  padding : 50px 25px;\n" +
			"}\n" +

			"#padding_3 {\n" +
			"  padding : 45px 30px 40px;\n" +
			"}\n" +

			"#padding_4 {\n" +
			"  padding : 35px 15px 30px 20px;\n" +
			"}\n" +
			
			"#padding_4_em {\n" +
			"  padding : .5em 0 0 0;\n" +
			"}\n"
			;	

	public static final String CSS_CLEAR =
			"#clear_both {\n" +
			"  clear : both;\n" +
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

	public static final String CSS_BG_IMAGE =
			"#bgimage {\n" +
			"  background-image : url(\"http://test.com/image1.png\"),\n" +
			"								 none,\n" +
			"                                url(\"http://test.com/image2.jpg\");\n" +
			"}\n";

	public static final String CSS_BG_POSITION =
			"#bgposition {\n" +
			"  background-position : 100px 15%,\n" +
			"                                   left top;\n" +
			"}\n";

	public static final String CSS_BG_SIZE =
			"#bgsize {\n" +
			"  background-size : 250px 120.3em,\n" +
			"                                   auto;\n" +
			"}\n";

	public static final String CSS_BG_REPEAT =
			"#bgrepeat {\n" +
			"  background-repeat : repeat,\n" +
			"                                no-repeat;\n" +
			"}\n";

	public static final String CSS_BG_ATTACHMENT =
			"#bgattachment {\n" +
			"  background-attachment : scroll,\n" +
			"                                fixed;\n" +
			"}\n";

	public static final String CSS_BG_ORIGIN =
			"#bgorigin {\n" +
			"  background-origin : padding-box,\n" +
			"                                border-box;\n" +
			"}\n";

	public static final String CSS_BG_CLIP =
			"#bgclip {\n" +
			"  background-clip : content-box,\n" +
			"                                padding-box;\n" +
			"}\n";

	public static final String CSS_BG =
			"#bgcoloronly {\n" +
			"  background : #AABBCC;\n" +
			"}\n" +
			
			"#bg_img_none_origin_content_box {\n" +
			"  background : none content-box;\n" +
			"}\n" +

			"#bg_two_images {\n" +
			"  background : url(\"http://www.test.com/image1.png\") 100px 120px / auto repeat scroll,\n" +
			"  					   url(\"http://www.test.com/image2.jpg\") 150px 200px / 400px 300px no-repeat fixed content-box padding-box;\n" +
			"}\n" +
			
			"#bg_gradient_angle {\n" +
			"  background : linear-gradient(25deg,  #AABBCC, red 40%, blue 200px, green);\n" +
			"}\n" +

			"#bg_gradient_side {\n" +
			"  background : linear-gradient(to right, red, blue);\n" +
			"}\n" +
	
			"#bg_gradient_corner {\n" +
			"  background : linear-gradient(to top right,  red, green 50%, blue);\n" +
			"}\n" +

			"#bg_gradient_none {\n" +
			"  background : linear-gradient(#AABBCC, #DDEEFF);\n" +
			"}\n"
			;
	
	public static final String CSS_BG_BROWSER_SPECIFIC =
			"#bg_browser_specific {\n" +
			"  background : -moz-linear-gradient(#112233, #AABBCC);\n" +
			"}\n";

	public static final String CSS_BG_BROWSER_SPECIFIC_WITH_NESTED_FUNCTIONS =
			"#bg_browser_specific_with_nested_functions {\n" +
			"  background : -moz-linear-gradient(#112233, foo(#AABBCC, bar), baz(123));\n" +
			"}\n";

	public static final String CSS_TEXT_DECORATION =
			"#text_decoration_1 {\n" +
			"  text-decoration : underline;\n" +
			"}\n"
			;

	public static final String CSS_FILTER_NONE =
			"#filter_none {\n" +
			"  filter : none;\n" +
			"}\n"
			;

	public static final String CSS_FILTER =
			"#filter {\n" +
			"  filter : blur(3px),\n" +
			"            brightness(150%),\n" +
			"            contrast(120%),\n" +
			"            drop-shadow(5px 7px 6px red),\n" +
			"            grayscale(100%),\n" +
			"            hue-rotate(75deg),\n" +
			"            invert(95%),\n" +
			"            opacity(25%),\n" +
			"            saturate(4%),\n" +
			"            sepia(83%),\n" +
			"            url(\"http://foo\");\n" +
			"}\n"
			;
	public static final String CSS_FILTER2 =
			"#filter2 {\n" +
			"  filter : drop-shadow(5px 7px 6px 4px #AABBCC);\n" +
			"}\n"
			;

	public static final String DOCTYPE = "<!DOCTYPE HTML PUBLIC \"-//W3C/DTD HTML 4.01 Frameset//EN\" \"http://w3.org/TR/html4/frameset.dtd\">";
	
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
	public static final String HTML_TEXT_ISSUE =
			DOCTYPE  + "\n" +			
					"<html>\n" +
					"<!-- a single line comment -->\n" +
					"<head>\n" +
					"  <style type=\"text/css\">.a-non-tag-selector {\n" +
					"    width : 100px;\n" +
					"    }\n" +
					"  </style>\n" +
					"</head>\n" +
					" <body>\n" +
					"   <div id=\"main_div\">\n" +
					"   </div>\n" +
					" </body>\n" +
					"</html>";
	
}