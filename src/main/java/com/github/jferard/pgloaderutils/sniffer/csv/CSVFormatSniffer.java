/*
 * Some utilities for loading csv data into a PosgtreSQL database:
 * detect file encoding, CSV format and populate database
 *
 *     Copyright (C) 2016, 2018 J. Férard <https://github.com/jferard>
 *
 * This file is part of pgLoader Utils.
 *
 * pgLoader Utils is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * pgLoader Utils is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.jferard.pgloaderutils.sniffer.csv;

import com.github.jferard.pgloaderutils.sniffer.Sniffer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * Three things to determine : finalDelimiter, finalQuotechar, finalEscapechar
 * <p>
 * Consider the following line : a\|a,"a|a","a|a","a|a"
 *
 * @author Julien Férard
 */
@SuppressWarnings("unused")
public class CSVFormatSniffer implements Sniffer {
    static final int ASCII_BYTE_COUNT = 128;
    private static final int BONUS_FOR_IRREGULAR_LINES = 5;
    private static final int DEFAULT_LINE_SIZE = 1024;

    static List<Byte> asNewList(final byte[] array) {
        final List<Byte> l = new LinkedList<Byte>();
        for (final byte i : array)
            l.add(i);
        return l;
    }

    private final CSVConstraints csvParams;
    private byte finalDelimiter;
    private byte finalEscape;
    private byte finalQuote;

    public CSVFormatSniffer(final CSVConstraints csvParams) {
        this.csvParams = csvParams;
    }

    public byte getDelimiter() {
        return this.finalDelimiter;
    }

    public byte getEscape() {
        return this.finalEscape;
    }

    public byte getQuote() {
        return this.finalQuote;
    }

    @Override
    public void sniff(final InputStream inputStream, final int size) throws IOException, ParseException {
        final byte[] allowedDelimiters = this.csvParams.getAllowedDelimiters();
        final byte[] allowedQuotes = this.csvParams.getAllowedQuotes();
        final byte[] allowedEscapes = this.csvParams.getAllowedEscapes();

        // n fields -> n-1 delimiters
        final int minDelimiters = this.csvParams.getMinFields() - 1;

        final List<Line> lines = this.getLines(inputStream, size);
        DelimiterComputer delimiterComputer = new DelimiterComputer(lines, allowedDelimiters, minDelimiters);
        this.finalDelimiter = delimiterComputer.compute();
        this.finalQuote = this.computeQuote(lines, allowedQuotes);
        this.finalEscape = this.computeEscape(lines, allowedEscapes);
    }

    private List<Line> getLines(InputStream inputStream, int size) throws IOException {
        final StreamParser streamParser = new StreamParser(inputStream, CSVFormatSniffer.DEFAULT_LINE_SIZE);
        List<Line> lines = new ArrayList<Line>();
        int i = 0;
        Line line = streamParser.getNextLine();
        while (line != null) {
            i += line.getSize();
            if (i >= size) break;

            lines.add(line);
            line = streamParser.getNextLine();
        }
        return lines;
    }

    private byte computeEscape(final List<Line> lines, final byte[] allowedEscapes) {
        final int[] escapes = new int[CSVFormatSniffer.ASCII_BYTE_COUNT];
        List<Byte> keptEscapes = CSVFormatSniffer.asNewList(allowedEscapes);
        keptEscapes.add(this.finalQuote);

        for (final Line line : lines) {
            final List<Part> parts = line.asParts(this.finalDelimiter);
            for (final Part part : parts) {
                part.trim();
                part.trimOne(this.finalQuote);
            }
            for (final Part part : parts) {
                final int c = part.findCharBefore(this.finalQuote);
                if (c >= 0 && keptEscapes.contains(Byte.valueOf((byte) c))) escapes[c]++;
            }
        }

        return Collections.max(keptEscapes, new Comparator<Byte>() {
            @Override
            public int compare(final Byte e1, final Byte e2) {
                return escapes[e1] - escapes[e2];
            }
        });
    }

    public void sniff(final String path, final int size) throws IOException, ParseException {
        final InputStream stream = new FileInputStream(path);
        try {
            this.sniff(stream, size);
        } finally {
            stream.close();
        }
    }

    private byte computeQuote(final List<Line> lines, final byte[] allowedQuotes) {
        final int[] quotes = new int[CSVFormatSniffer.ASCII_BYTE_COUNT];
        final List<Byte> keptQuotes = CSVFormatSniffer.asNewList(allowedQuotes);

        int partCount = 0;
        for (final Line line : lines) {
            final List<Part> parts = line.asParts(this.finalDelimiter);
            for (final Part part : parts) {
                part.trim();
                partCount++;
            }
            for (final byte q : keptQuotes) {
                for (final Part part : parts) {
                    quotes[q] += part.quoteValue(q);
                }
            }
        }

        Byte max = Collections.max(keptQuotes, new Comparator<Byte>() {

            @Override
            public int compare(final Byte q1, final Byte q2) {
                return quotes[q1] - quotes[q2];
            }
        });
        if (5 * quotes[max] >= partCount) return max;
        else return '\0';
    }
}