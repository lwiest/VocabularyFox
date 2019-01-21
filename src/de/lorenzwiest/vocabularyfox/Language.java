/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 Lorenz Wiest
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package de.lorenzwiest.vocabularyfox;

public enum Language {
	EN(new String[][] { //
		{ "something", "sth.", "sth", "\\bsomething\\b|\\bsth\\.|\\bsth\\b" }, //
		{ "somebody", "sb.", "sb", "\\bsomebody\\b|\\bsb\\.|\\bsb\\b" } //
	}),
	FR(new String[][] { //
		{ "quelque chose", "qc", "qc.", "quelque chose|\\bqc\\.|\\bqc\\b" }, //
		{ "quelq'un", "qn", "qn.", "quelq'un|\\bqn\\.|\\bqn\\b" }, //
		{ "Monsieur", "M.", "\\bMonsieur\\b" }, //
		{ "Madame", "Mme", "\\bMadame\\b" }, //
	});

	private String[][] abbreviationPairs;

	private Language(String[][] abbreviationPairs) {
		this.abbreviationPairs = abbreviationPairs;
	}

	public String[][] getAbbreviationPairs() {
		return this.abbreviationPairs;
	}
}
