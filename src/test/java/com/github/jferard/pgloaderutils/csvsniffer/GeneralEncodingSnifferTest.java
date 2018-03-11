package com.github.jferard.pgloaderutils.csvsniffer;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * Created by jferard on 22/03/17.
 */
public class GeneralEncodingSnifferTest {
    private GeneralEncodingSniffer sniffer;

    @Before
    public void setUp() {
        this.sniffer = new GeneralEncodingSniffer();
    }
    
    @Test
    public void test() throws IOException {
        Map<String, Charset> charsetByName = Charset.availableCharsets();
        String s = Resources.toString(Resources.getResource("A_petite_Jeanne_UTF8"), Charsets.UTF_8);

        for (Charset charset : charsetByName.values()) {
            try {
                byte[] bytes = s.getBytes(charset);
                ByteArrayInputStream stream = new ByteArrayInputStream(bytes);

                CharBuffer cbuf = charset.decode(ByteBuffer.wrap(bytes));
                if (!s.equals(cbuf.toString())) {
                    continue; // Charset can't decode/encode all chars
                }

                sniffer.sniff(stream, s.length());

                Assert.assertTrue(sniffer.getCharsets().size() < charsetByName.size());
                Assert.assertTrue(sniffer.getCharsets().contains(charset));
            } catch (UnsupportedOperationException e) {
                // Skip charset
            } catch (CharacterCodingException e) {
                // Skip charset
            }
        }
    }

}