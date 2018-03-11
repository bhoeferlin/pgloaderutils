package com.github.jferard.pgloaderutils.csvsniffer;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by jferard on 07/04/17.
 */
public class CSVConstraintsTest {
    @Test
    public void test() {
        CSVConstraintsBuilder b = CSVConstraints.basicBuilder();
        CSVConstraints c = b.build();
        Assert.assertTrue(c.isAllowedDelimiter(','));
        Assert.assertFalse(c.isAllowedDelimiter('#'));
        Assert.assertTrue(c.isAllowedQuote('"'));
        Assert.assertFalse(c.isAllowedQuote('#'));
        Assert.assertTrue(c.isAllowedEscape('"'));
        Assert.assertFalse(c.isAllowedEscape('#'));
    }
}