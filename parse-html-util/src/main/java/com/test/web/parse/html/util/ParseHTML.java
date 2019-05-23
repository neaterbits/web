package com.test.web.parse.html.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.neaterbits.util.io.loadstream.LoadStream;
import com.neaterbits.util.io.loadstream.SimpleLoadStream;
import com.neaterbits.util.parse.IParse;
import com.neaterbits.util.parse.ParserException;
import com.test.web.css.oo.OOCSSBase;
import com.test.web.document.html._long.LongHTMLDocument;
import com.test.web.document.html.oo.OOHTMLDocument;
import com.test.web.document.html.oo.OOTagElement;
import com.test.web.io._long.StringBuffers;
import com.test.web.parse.html.HTMLParser;
import com.test.web.parse.html.IHTMLParserListener;

public class ParseHTML {

    public static <STYLE_DOCUMENT> HTMLParser<OOTagElement, STYLE_DOCUMENT, OOCSSBase> createParser(
            OOHTMLDocument document, IHTMLParserListener<OOTagElement> parserListener, LoadStream stream,
            IParse<STYLE_DOCUMENT> parseStyleDocument) {

        final StringBuffers input = new StringBuffers(stream);

        final HTMLParser<OOTagElement, STYLE_DOCUMENT, OOCSSBase> parser = new HTMLParser<>(input, input,
                parserListener, document.getStyleParserListener(), parseStyleDocument);

        return parser;
    }

    public static <STYLE_DOCUMENT> OOHTMLDocument parseOOHTMLDocument(String html, IParse<STYLE_DOCUMENT> parseStyleDocument) throws ParserException {

        final OOHTMLDocument document = new OOHTMLDocument();

        final HTMLParser<OOTagElement, STYLE_DOCUMENT, OOCSSBase> parser = createParser(document, document,
                new SimpleLoadStream(html), parseStyleDocument);

        try {
            parser.parseHTMLFile();
        } catch (IOException ex) {
            throw new IllegalStateException("IO eception while parsing", ex);
        }

        return document;
    }

    private static <STYLE_DOCUMENT> LongHTMLDocument loadLongHTMLDocument(
            SimpleLoadStream stream,
            IParse<STYLE_DOCUMENT> parseStyleDocument)  throws IOException, ParserException {
        
        final LongHTMLDocument htmlDocument;
        
        final StringBuffers buffers = new StringBuffers(stream);

        htmlDocument = new LongHTMLDocument(buffers);
        
        final HTMLParser<Integer, STYLE_DOCUMENT, Void> parser = new HTMLParser<>(
                buffers,
                buffers,
                htmlDocument,
                htmlDocument.getStyleParserListener(),
                parseStyleDocument);
        
        parser.parseHTMLFile();

        return htmlDocument;
    }

    // Helper method for loading a document, useful from unit tests
    public static <STYLE_DOCUMENT> LongHTMLDocument loadLongHTMLDocument(File file, IParse<STYLE_DOCUMENT> parseStyleDocument) throws IOException, ParserException {
        
        final LongHTMLDocument doc;
        
        try (InputStream inputStream = new FileInputStream(file)) {
            doc = loadLongHTMLDocument(new SimpleLoadStream(inputStream), parseStyleDocument);
        }

        return doc;
    }

    public static <STYLE_DOCUMENT> LongHTMLDocument parseLongHTMLDocument(String text, IParse<STYLE_DOCUMENT> parseStyleDocument)  throws ParserException {
        try {
            return loadLongHTMLDocument(new SimpleLoadStream(text), parseStyleDocument);
        } catch (IOException ex) {
            throw new IllegalStateException("Should not catch IOException when parsing from a String", ex);
        }
    }


}
