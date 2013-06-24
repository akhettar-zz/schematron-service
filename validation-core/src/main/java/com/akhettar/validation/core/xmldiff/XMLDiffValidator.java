package com.akhettar.validation.core.xmldiff;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.IOUtils;
import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.akhettar.validation.core.Constants;
import com.akhettar.validation.core.exception.ValidationException;
import com.akhettar.validation.model.report.Message;
import com.akhettar.validation.model.report.Report;
import com.akhettar.validation.model.xmldiff.Xmldiff;
import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * Created by IntelliJ IDEA.
 * User: a.khettar
 * Date: 16/07/2012
 * Time: 17:49
 * To change this template use File | Settings | File Templates.
 */
public class XMLDiffValidator {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    private final Unmarshaller marshaller;
    private final Boolean full_report;

    @Inject
    public XMLDiffValidator(@Named("Xmldiff") final Unmarshaller marshaller, @Named("full-report.xml-dif") final String full_report) {

        this.marshaller = marshaller;
        this.full_report = Boolean.valueOf(full_report);
    }

    /**
     * Apply the diff and builds report based on XMLUnit
     * reports.
     * 
     * @param in
     *            wrapper for two XML to be compared to each other.
     * 
     * @return Report of diff.
     */
    public Report applyDiff(final InputStream in) {

        
        final Report report = new Report();
        byte[] bytes = null;
        try {
            bytes = IOUtils.toByteArray(in);
            LOG.debug("Applying XML diff on {}", new String(bytes));
            final Xmldiff xmldiff = (Xmldiff) this.marshaller.unmarshal(new ByteArrayInputStream(bytes));

            validate(xmldiff);
            final Document doc1 = ((Node) xmldiff.getXml().get(0).getAny()).getOwnerDocument();
            final Document doc2 = ((Node) xmldiff.getXml().get(1).getAny()).getOwnerDocument();

            final Diff diff = new Diff(doc1, doc2);

            // apply configuration
            configure(diff);

            // detailed diff will give us a full report
            final DetailedDiff detailed = new DetailedDiff(diff);

            // loop over the detailed
            for (final Object msg : detailed.getAllDifferences()) {
                final Message message = new Message();
                message.setType("xmldiff");
                message.setContent(msg.toString());
                report.getMessage().add(message);

                // by default, we only send the first message
                if (!full_report.booleanValue())
                {
                    break;
                }
            }

            // Set the status
            if (detailed.getAllDifferences().size() == 0) {
                report.setStatus(Constants.SUCCESS);
                final Message msg = new Message();
                msg.setContent("Similar article");
                report.getMessage().add(msg);
            } else {
                report.setStatus(Constants.FAILURE);
            }

        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new ValidationException(e);
        }
        return report;

    }

    /**
     * Basic validation for the xml input
     * @param xmldiff
     */
    private void validate(Xmldiff xmldiff) {

        if (xmldiff.getXml().size() == 0
                || xmldiff.getXml().size() == 1  || xmldiff.getXml().size() > 2)
        {
            throw new IllegalArgumentException("The input XML must contain only two well formatted XMLs to apply the diff");
        }

        if (xmldiff.getXml().get(0).getAny() == null ||
                xmldiff.getXml().get(1).getAny() == null)
        {
            throw new IllegalArgumentException("The content of the XML may not be empty");
        }

    }

    /**
     * Sets the overrideElement Qualifier and set
     * to ignore comment and white space.
     * 
     * @param diff
     */
    private void configure(Diff diff) {

        diff.overrideElementQualifier(null);
        XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.setIgnoreComments(true);
    }

}
