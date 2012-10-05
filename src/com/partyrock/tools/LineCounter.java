package com.partyrock.tools;

import java.io.File;
import java.util.Scanner;

/**
 * Counts lines. That's about it. It's a random, but fun tool. Does not count
 * itself.
 * @author Matthew
 */
public class LineCounter {
	private String[] extensions = { "java", "php", "html", "txt" };

	private int count, blankCount, singleCount, words, letteredWords;

	public LineCounter(String path) {
		File f = new File(path);

		add(f);

		System.out.println("You have " + count + " lines.");
		System.out.println(blankCount + " lines are blank.");
		System.out.println(singleCount + " only have one character.");
		System.out.println("For a total of "
				+ (count - blankCount - singleCount) + " useful lines.");
		System.out.println("There are " + words + " words, " + letteredWords
				+ " of which contain a letter.");
	}

	public void add(File f) {
		if (f.isDirectory())
			for (File file : f.listFiles())
				add(file);

		String name = f.getName();
		if (name.contains(".")
				&& name.lastIndexOf(".") != name.length() - 1
				&& contains(extensions, name.toLowerCase().substring(name.lastIndexOf(".") + 1))
				&& !name.equals(this.getClass().getSimpleName() + ".java")) {
			count(f);
		}

	}

	public boolean containsLetter(String x) {
		char[] c = x.toCharArray();
		for (char a : c)
			if (Character.isLetter(a))
				return true;
		return false;
	}

	public void count(File f) {
		System.out.println("Counting " + f);
		Scanner in;
		try {
			in = new Scanner(f);
			while (in.hasNext()) {
				String line = in.nextLine();
				count++;
				line = line.trim();

				String[] wordsArray = line.split("[ -+.:<>]");

				for (String x : wordsArray) {
					if (x.trim().equals(""))
						continue;
					words++;
					if (containsLetter(x))
						letteredWords++;
				}

				if (line.equals(""))
					blankCount++;
				else if (line.length() == 1)
					singleCount++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public boolean contains(String[] array, String element) {
		for (String n : array)
			if (n.equals(element))
				return true;
		return false;
	}

	public static void main(String... args) {
		new LineCounter("./src");
	}
}
