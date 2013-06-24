package com.akhettar.validation.core.services;

import java.io.InputStream;

import com.akhettar.validation.model.report.Report;

/**
 * Created by IntelliJ IDEA.
 * User: a.khettar
 * Date: 16/07/2012
 * Time: 17:43
 *
 */
public interface XMLDiffService {


    /**
     * Apply xml diff using XMLUnit library.
     *
     * @param xml a wrapper containing both XML to apply
     *            diff against.
     *
     * @return  full report of the xml diff.
     */
    public Report diff (final InputStream xml);
}
