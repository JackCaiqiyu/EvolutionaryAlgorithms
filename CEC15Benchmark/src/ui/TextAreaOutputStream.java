/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

//~--- JDK imports ------------------------------------------------------------

import java.awt.BorderLayout;
import java.awt.Container;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class TextAreaOutputStream extends OutputStream {
	private final JFrame main = new JFrame("System.out");
	private final StringBuilder sb = new StringBuilder();
	private final JTextArea textArea;

	public TextAreaOutputStream() {
		this(25, 80);
	}

	public TextAreaOutputStream(int rows, int columns) {
		textArea = new JTextArea(rows, columns);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);

		// OutputStream out = new TextAreaOutputStream(textArea);
		System.setOut(new PrintStream(this));

		Container contentPane = main.getContentPane();

		contentPane.setLayout(new BorderLayout());
		contentPane.add(new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
		main.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		main.pack();
		main.setVisible(true);
	}

	@Override
	public void flush() {
	}

	@Override
	public void close() {
	}

	@Override
	public void write(int b) throws IOException {
		if (b == '\r') {
			return;
		}

		if (b == '\n') {
			final String text = sb.toString() + "\n";

			textArea.append(text);
			sb.setLength(0);
		} else {
			sb.append((char) b);
		}
	}
}

// ~ Formatted by Jindent --- http://www.jindent.com
