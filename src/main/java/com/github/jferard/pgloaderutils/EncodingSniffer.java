package com.github.jferard.pgloaderutils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.charset.Charset;

@SuppressWarnings("unused")
public class EncodingSniffer implements Sniffer {
	public final static Charset UTF_8 = Charset.forName("UTF-8");
	public final static Charset US_ASCII = Charset.forName("US-ASCII");

	private Charset charset;

	public void sniff(String path, final int size) throws IOException {
		InputStream stream = new FileInputStream(path);
		try {
			this.sniff(stream, size);
		} finally {
			stream.close();
		}
	}

	/**
	 * http://codereview.stackexchange.com/questions/59428/validating-utf-8-byte
	 * -array
	 * http://stackoverflow.com/questions/887148/how-to-determine-if-a-string
	 * -contains-invalid-encoded-characters
	 * 
	 * @param stream
	 *            the input stream
	 * @return UTF-8, US-ASCII or null Charset. If null, the charset can be any
	 *         of 8 bytes charsets.
	 * @throws IOException
	 */
	public void sniff(InputStream stream, final int size) throws IOException {
		int expectedLen;
		Charset charset = EncodingSniffer.US_ASCII;
		InputStream bufferedStream = new BufferedInputStream(stream);
		bufferedStream.mark(3);

		// Check for BOM
		if ((bufferedStream.read() & 0xFF) == 0xEF
				&& (bufferedStream.read() & 0xFF) == 0xBB
				&& (bufferedStream.read() & 0xFF) == 0xBF) {
			charset = EncodingSniffer.UTF_8;
		} else {
			bufferedStream.reset();
		}

		int i = 0;
		int c = bufferedStream.read();
		while (c != -1 && i < size) {
			i++;
			// Lead byte analysis
			if ((c & 0x80) == 0x00) { // < 128
				c = bufferedStream.read();
				continue;
			} else if ((c & 0xe0) == 0xc0)
				expectedLen = 2;
			else if ((c & 0xf0) == 0xe0)
				expectedLen = 3;
			else if ((c & 0xf8) == 0xf0)
				expectedLen = 4;
			else {
				this.charset = null;
				return;
			}

			charset = EncodingSniffer.UTF_8;

			// Trailing bytes
			while (--expectedLen > 0) {
				c = bufferedStream.read();
				if ((c & 0xc0) != 0x80) {
					this.charset = null;
					return;
				}
			}
			c = bufferedStream.read();
		}
		this.charset = charset;
	}

	public Charset getCharset() {
		return this.charset;
	}
}
