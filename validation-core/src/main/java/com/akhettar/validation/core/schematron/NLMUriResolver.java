package com.akhettar.validation.core.schematron;

import java.io.File;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;

import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * URIResolver, this will under external file referenced by
 * the schematron file such as not-allowed-values.xml and subject-codes.xml
 * - see Nature-NLM.sch
 * 
 * @author a.khettar
 * 
 */
public class NLMUriResolver implements URIResolver {

    private final String RULES_FOLDER;

    @Inject
    public NLMUriResolver(@Named("rules.fs") String path) {
        this.RULES_FOLDER = path;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.xml.transform.URIResolver#resolve(java.lang.String,
     * java.lang.String)
     */
    @Override
    public Source resolve(String href, String base) throws TransformerException {

        return new StreamSource(new File(RULES_FOLDER.concat(File.separator).concat(href)));
    }

}
