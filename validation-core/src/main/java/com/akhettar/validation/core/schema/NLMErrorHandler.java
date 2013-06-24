package com.akhettar.validation.core.schema;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import com.akhettar.validation.model.report.Message;

/**
 * NLM DTD Error handler. Errors are captured an appropriate Report object is
 * constructed and saved in an ArrayList, which can be accessed by the
 * validator.
 * 
 * Note that URI resolving of the DTD entities are cached so not load them
 * for each request.
 * 
 * @author a.khettar
 * 
 */
public class NLMErrorHandler extends DefaultHandler {

    //private final String SCHEMA_DIR;
    private final List<Message> messages = new ArrayList<Message>();

    @Override
    public void error(SAXParseException e) throws SAXException {
        addError(e, "error");

    }

    @Override
    public void fatalError(SAXParseException e) throws SAXException {

        addError(e, "fatal-error");
    }

    @Override
    public void warning(SAXParseException e) throws SAXException {
        // we are not handling warning.
    }

    public List<Message> getMessages() {
        return this.messages;
    }

    /**
     * add error to the list.
     * 
     * @param e
     *            SaxParserException.
     * @param type
     */
    private void addError(SAXParseException e, String type) {
        final Message message = new Message();
        message.setColumn(e.getColumnNumber());
        message.setLine(e.getLineNumber());
        message.setContent(e.getMessage());
        message.setValidatedBy("DTD");
        message.setType(type);
        this.messages.add(message);
    }

}
