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
package com.github.jferard.pgloaderutils.csvsniffer;

import java.util.LinkedList;
import java.util.List;

/** The StreamParser class parses a stream in lines without the encoding information. */
class StreamParser {
	private static final int CR = 0x0D;
	private static final int LF = 0x0A;

	List<Line> lines;
	private int size;
	private int lastChar;
	private Line curLine;

	StreamParser(int defaultSize) {
		this.lastChar = 0;
		this.size = defaultSize;
		this.lines = new LinkedList<Line>();
		this.curLine = new Line(this.size);
	}

	void put(byte c) {
		if (this.lastChar == CR) {
			if (c == LF) {
				this.lastChar = LF;
			} else {
				this.lines.add(this.curLine);
				this.curLine = new Line(this.size);
				this.curLine.append(c);
				this.lastChar = 0;
			}
		} else {
			if (c == CR || c == LF) {
				this.lines.add(this.curLine);
				this.curLine = new Line(this.size);
				this.lastChar = c;
			} else
				this.curLine.append(c);
		}
	}

	public List<Line> getLines() {
		return this.lines;
	}
}