package com.github.jferard.pgloaderutils.csvsniffer.csd;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

/**
 * Created by jferard on 06/04/17.
 */
public class CSDSchemaTest {
    @Test
    public void testEmpty() {
        CSDSchema<CSDField> s = new CSDSchema<CSDField>(Collections.<CSDField>emptyList(), false);
        Assert.assertEquals("()", s.getColumns());
        Assert.assertFalse(s.hasOptionalHeader());
        Assert.assertFalse(s.iterator().hasNext());
        Assert.assertEquals(0, s.size());
        Assert.assertEquals("[]", s.toString());
    }

    @Test
    public void testOneField() {
        CSDField f = TestUtil.getMandatoryField();
        CSDSchema<CSDField> s = new CSDSchema<CSDField>(Arrays.asList(f), true);
        Assert.assertEquals("(code)", s.getColumns());
        Assert.assertTrue(s.hasOptionalHeader());
        Assert.assertTrue(s.iterator().hasNext());
        Assert.assertEquals(f, s.iterator().next());
        Assert.assertEquals(1, s.size());
    }
}