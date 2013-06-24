package com.akhettar.validation.core.modules;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;

import net.sf.saxon.FeatureKeys;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.xerces.util.XMLCatalogResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.xml.sax.EntityResolver;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;

import com.akhettar.validation.core.Constants;
import com.akhettar.validation.core.Validator;
import com.akhettar.validation.core.cache.Cache;
import com.akhettar.validation.core.cache.ObjectMemoryCache;
import com.akhettar.validation.core.exception.ModulesConfigurationException;
import com.akhettar.validation.core.schema.NLMErrorHandler;
import com.akhettar.validation.core.schema.SchemaValidator;
import com.akhettar.validation.core.schematron.NLMUriResolver;
import com.akhettar.validation.core.schematron.SchematronValidator;
import com.akhettar.validation.core.services.ValidationService;
import com.akhettar.validation.core.services.ValidationServiceImp;
import com.akhettar.validation.core.services.XMLDiffService;
import com.akhettar.validation.core.services.XMLDiffServiceImpl;
import com.akhettar.validation.core.xmldiff.XMLDiffValidator;
import com.akhettar.validation.model.report.Message;
import com.akhettar.validation.model.report.Report;
import com.akhettar.validation.model.xmldiff.Xmldiff;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.nature.components.service.resources.ResourceLookUp;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

/**
 * Guice configuration module for {@link ValidationService}.
 *
 * @author a.khettar
 */
public class ValidationServiceModule extends AbstractModule {

    private static final Logger LOG = LoggerFactory.getLogger(ValidationServiceModule.class);

    /*
     * (non-Javadoc)
     * 
     * @see com.google.inject.Module#configure(com.google.inject.Binder)
     */
    @Override
    public void configure() {

        try {
            LOG.info("Starting validation service module...");

            System.setProperty("javax.xml.transform.TransformerFactory", "net.sf.saxon.TransformerFactoryImpl");
            ResourceLookUp lookup = new ResourceLookUp(Constants.PROPERTY_FILE);

            // configure logback
            this.configureLogback(lookup);

            // binding cache
            bind(new TypeLiteral<Cache<String, Templates>>() {
            }).to(new TypeLiteral<ObjectMemoryCache<String, Templates>>() {
            }).asEagerSingleton();

            Names.bindProperties(binder(), lookup.getProperties());
            bind(DefaultHandler.class).to(NLMErrorHandler.class);
            bind(URIResolver.class).to(NLMUriResolver.class);
            bind(Validator.class).annotatedWith(Names.named("dtd")).to(SchemaValidator.class);
            bind(Validator.class).annotatedWith(Names.named("schematron")).to(SchematronValidator.class);
            bind(ValidationService.class).to(ValidationServiceImp.class);
            bind(XMLDiffService.class).to(XMLDiffServiceImpl.class);
            bind(XMLDiffValidator.class);
            LOG.info("Validation service modules have been successfully started.");

        } catch (IOException e) {

            LOG.error("Failed to load property file: " + Constants.PROPERTY_FILE + ".properties");
            throw new ModulesConfigurationException(e);
        }

    }

    /**
     * Creates an instance of JaxbContext.
     *
     * @return {@link JAXBContext}.
     * @throws JAXBException
     */
    @Provides
    @Singleton
    @Named("Report")
    JAXBContext getContext() throws JAXBException {
        return JAXBContext.newInstance(Report.class);

    }

    /**
     * Provides Unmarshaller
     *
     * @param context the given JaxbContext
     * @return {@link Unmarshaller}.
     * @throws JAXBException failure to create the unmarshaller.
     */
    @Provides
    Unmarshaller getUnmarshaller(@Named("Report") JAXBContext context) throws JAXBException {

        return context.createUnmarshaller();
    }

    /**
     * Provides Unmarshaller
     *
     * @return {@link Unmarshaller}.
     * @throws JAXBException failure to create the unmarshaller.
     */
    @Provides
    @Named("Xmldiff")
    Unmarshaller getXMLDiffUnmarshaller() throws JAXBException {

        JAXBContext ctx = JAXBContext.newInstance(Xmldiff.class);
        return ctx.createUnmarshaller();

    }

    /**
     * Provider for DocumentBuilder.
     *
     * @return
     * @throws ParserConfigurationException
     */
    @Provides
    @Singleton
    DocumentBuilder getDocumentBuilder() throws ParserConfigurationException {
        DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
        return fac.newDocumentBuilder();
    }

    /**
     * Get {@link SAXParser}.
     *
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    @Provides
    @Singleton
    SAXParser getSaxParser() throws ParserConfigurationException, SAXException {
        final SAXParserFactory fac = SAXParserFactory.newInstance();
        fac.setValidating(true);
        final SAXParser parser = fac.newSAXParser();
        return parser;
    }

    /**
     * Provides {@link XMLReader}
     *
     * @return {@link SAXParser}
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    @Provides
    XMLReader getXMLReader(EntityResolver resolver) throws ParserConfigurationException, SAXException {
        final SAXParserFactory fac = SAXParserFactory.newInstance();
        fac.setValidating(true);
        final SAXParser parser = fac.newSAXParser();
        XMLReader reader = parser.getXMLReader();
        reader.setEntityResolver(resolver);
        return reader;
    }

    /**
     * Gets svrl to report transform.
     *
     * @param schema_dir path to schema Dir.
     * @return {@link Transformer}
     * @throws TransformerConfigurationException
     *
     */
    @Provides
    @Named("report")
    Templates getTransformer(Cache<String, Templates> cache, @Named("schematron.fs") String schema_dir) throws TransformerConfigurationException {
        if (cache.get(Constants.VALIDATION_REPORT_MD5_KEY) != null) {
            return cache.get(Constants.VALIDATION_REPORT_MD5_KEY);
        }
        TransformerFactory tf = TransformerFactory.newInstance();
        Templates template = tf.newTemplates(new StreamSource(schema_dir.concat(File.separator).concat(Constants.REPORT_XSLT)));
        cache.store(Constants.VALIDATION_REPORT_MD5_KEY, template, 10080);
        return template;

    }

    /**
     * Provides catalog resolver.
     *
     * @param catlogs path to schema Dir.
     * @return EntiryResolver
     * @throws TransformerConfigurationException
     *
     */
    @Provides
    @Singleton
    EntityResolver getResolver(@Named("catalog.files") String catlogs) {

        XMLCatalogResolver resolver = new XMLCatalogResolver(StringUtils.split(catlogs, ";"));
        resolver.setPreferPublic(true);
        return resolver;

    }

    /**
     * Provides schematron transformer.
     *
     * @param cache          cache instance.
     * @param schematron_dir Schema directory path in the file server.
     * @param expiry         how many minutes to keep the transformer in cache.
     * @param stages         represents the names of xslt template to go through to achieve
     *                       schematron validation.
     * @return
     */
    @Provides
    @Named("schematron")
    Templates getSchematronTemplates(Cache<String, Templates> cache, @Named("schematron.fs") String schematron_dir,
                                     @Named("rules.fs") String rules_dir, @Named("expiryTimeInMin") int expiry,
                                     @Named(Constants.SCHEMATRON_XSLT_STAGES) String stages) throws TransformerException {

        // get straight from the cache.
        if (cache.get(DigestUtils.md5Hex(Constants.SCHEMATRON_MD5_KEY)) != null) {
            return cache.get(DigestUtils.md5Hex(Constants.SCHEMATRON_MD5_KEY));

        }
        // create new instance.
        TransformerFactory fac = TransformerFactory.newInstance();
        fac.setAttribute(FeatureKeys.LINE_NUMBERING, true);
        Node node = null;

        // first input is our Schematron Rules. It gets transformed through three stages, and the last xslt output
        // will be used to assert against the XML (jats).
        Source source = new StreamSource(rules_dir.concat(File.separator).concat(Constants.SCHEMATRON_FILE));
        for (String stage : StringUtils.split(stages, " ")) {
            Source xsl = new StreamSource(schematron_dir.concat(File.separator).concat(stage));
            Transformer t = fac.newTransformer(xsl);
            DOMResult result = new DOMResult();
            t.transform(source, result);
            source = new DOMSource(node = result.getNode());
        }
        final Templates templates = fac.newTemplates(new DOMSource(node));

        // update cache
        cache.store(DigestUtils.md5Hex(Constants.SCHEMATRON_MD5_KEY), templates, expiry);
        return templates;
    }

    /**
     * Gets the Xstream marshaller for logging purposes.
     *
     * @return {@link XStream}.
     */
    @Provides
    @Singleton
    XStream getMarshaller() {

        XStream xstream = new XStream(new StaxDriver());
        xstream.alias("report", Report.class);
        xstream.alias("message", Message.class);
        xstream.useAttributeFor(Report.class, "href");
        xstream.useAttributeFor(Message.class, "line");
        xstream.useAttributeFor(Message.class, "validatedBy");
        xstream.useAttributeFor(Message.class, "code");
        xstream.useAttributeFor(Message.class, "type");
        xstream.useAttributeFor(Message.class, "column");
        xstream.useAttributeFor(Message.class, "location");
        xstream.useAttributeFor(Message.class, "id");
        return xstream;

    }

    /**
     * Configures Logback.
     *
     * @param lookup
     */
    private void configureLogback(ResourceLookUp lookup) {

        // assume SLF4J is bound to logback in the current environment
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        String path;
        try {
            path = lookup.getResource(Constants.LOGBACK_FILE);
            if (!new File(path).exists()) {
                LOG.warn("Can't find external lgoback.xml in path: {}, using the one in classpath", path);
                return; // ignore configuration, logback will use whatever what in class path.
            }
            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(context);
            context.reset();
            configurator.doConfigure(new File(path));
        } catch (RuntimeException e1) {
            // use the one in class path.
            LOG.warn("Using logback.xml from classpath.");
            return;
        } catch (JoranException e) {
            LOG.error("Failed to configure logback.xml", e);
            StatusPrinter.printInCaseOfErrorsOrWarnings(context);

        }

    }

}
