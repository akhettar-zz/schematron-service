package com.nature.nlm.validation.core.services;

import com.google.inject.Inject;
import com.nature.nlm.validation.core.xmldiff.XMLDiffValidator;
import com.nature.nlm.validation.model.report.Report;

import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 * User: a.khettar
 * Date: 16/07/2012
 * Time: 17:44
 * To change this template use File | Settings | File Templates.
 */
public class XMLDiffServiceImpl implements XMLDiffService {


    private XMLDiffValidator validator;


    @Inject
    public XMLDiffServiceImpl(final XMLDiffValidator validator)
    {
        this.validator = validator;
    }


    /**
     * {@inheritDoc}
     *
     * @param xml a wrapper containing both XML to apply
     *            diff against.
     *
     * @return
     */
    @Override
    public Report diff(InputStream xml) {

        return validator.applyDiff(xml);
    }
}
