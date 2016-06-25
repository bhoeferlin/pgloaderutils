package com.github.jferard.pgloaderutils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Joiner;

public class CSVSnifferTest {
	private static final Charset ASCII = Charset.forName("US-ASCII");
	private Joiner joiner;

	@Before
	public void setUp() {
		this.joiner = Joiner.on('\n');
	}

	/**
	 * @throws IOException
	 * @see https://en.wikipedia.org/wiki/Comma-separated_values
	 */
	@Test
	public final void test() throws IOException {
		CSVFormatSniffer csvSniffer = new CSVFormatSniffer(CSVConstraints.builder().build());
		InputStream stream = new ByteArrayInputStream(
				this.joiner.join("Year,Make,Model", "1997,Ford,E350",
						"2000,Mercury,Cougar").getBytes(ASCII));

		csvSniffer.sniff(stream, 1000);
		Assert.assertEquals(',', (char) csvSniffer.getFinalDelimiter());
		Assert.assertEquals(0, (char) csvSniffer.getFinalQuote());
		Assert.assertEquals(0, (char) csvSniffer.getFinalEscape());
	}

	@Test
	public final void test2() throws IOException {
		CSVFormatSniffer csvSniffer = new CSVFormatSniffer(
				CSVConstraints.builder().build());
		InputStream stream = new ByteArrayInputStream(this.joiner
				.join("Year,Make,Model,Description,Price",
						"1997,Ford,E350,\"ac, abs, moon\",3000.00",
						"1999,Chevy,\"Venture \"\"Extended Edition\"\"\",\"\",4900.00",
						"1999,Chevy,\"Venture \"\"Extended Edition, Very Large\"\"\",,5000.00",
						"1996,Jeep,Grand Cherokee,\"MUST SELL!\n air, moon roof, loaded\",4799.00")
				.getBytes(ASCII));

		csvSniffer.sniff(stream, 1000);
		Assert.assertEquals(',', (char) csvSniffer.getFinalDelimiter());
		Assert.assertEquals('"', (char) csvSniffer.getFinalQuote());
		Assert.assertEquals('"', (char) csvSniffer.getFinalEscape());
	}

}
