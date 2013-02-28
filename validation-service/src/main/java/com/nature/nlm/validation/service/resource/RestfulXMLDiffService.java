package com.nature.nlm.validation.service.resource;

import com.nature.nlm.validation.model.report.Report;

import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 * User: a.khettar
 * Date: 16/07/2012
 * Time: 17:43
 *
 */
public interface RestfulXMLDiffService {


    /**
     * Apply xml diff using XMLUnit library.
     *
     * @param xml a wrapper containing both XML to apply
     *            diff against.
     *
     * @return  full report of the xml diff.
     */
    public Report diff(final InputStream xml);
}
