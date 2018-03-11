/*******************************************************************************
 * CSV Sniffer - A simple sniffer to detect file encoding and CSV format of a file
 *    Copyright (C) 2016 J. Férard <https://github.com/jferard>
 * 
 * This file is part of CSV Sniffer.
 * 
 * CSV Sniffer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * CSV Sniffer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.github.jferard.pgloaderutils.csvsniffer;

import com.google.common.io.Resources;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;


public class StreamParserTest {

	@Test
	public final void test() throws UnsupportedEncodingException {
		StreamParser streamParser = new StreamParser(1024);
		for (byte b : "line1\nline2\r\nline3\n\rline4".getBytes("ASCII"))
			streamParser.put(b);
		
		List<Line> lines = streamParser.getLines();
		
		Assert.assertEquals(5, lines.size());
		Assert.assertEquals("line1", lines.get(0).toString());
		Assert.assertEquals("line2", lines.get(1).toString());
		Assert.assertEquals("line3", lines.get(2).toString());
		Assert.assertEquals("", lines.get(3).toString());
		Assert.assertEquals("", lines.get(4).toString());
	}

	@Test
	public final void test3() throws IOException {
		StreamParser streamParser = new StreamParser(1024);
		InputStream stream = Resources.getResource("sirc-17804_9075_14209_201612_L_M_20170104_171522721-part" +
				".csv").openStream();
		int c = stream.read();
		while (c != -1) {
			if (c < 128)
				streamParser.put((byte) c);
			c = stream.read();
		}
		List<Line> lines = streamParser.getLines();

		Assert.assertEquals(58, lines.size());
	}

}
