package nl.cwi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BibTeXGenerator {

    public static String NOKEY = "BibTex key %s could not be retrieved from DBLP.%n";

    public static String FNF = "File %s could not be found.%n";

    public static String IOE = "I/O exception occured while reading %s.%n";

    public static String NAF = "Argument %s is not a file.%n";
    
    public static String USAGE = "BibTeX generator.\nUsage: bibgen <tex-file>";

    public static void main(String[] args) {

	if (args.length != 1) {
	    System.out.println(USAGE);
	    return;
	}

	File file = new File(args[0]);
	if (!file.isFile() || !file.getName().endsWith(".tex")) {
	    System.err.printf(NAF, args[0]);
	    return;
	}

	for (String key : getKeys(file))
	    printEntry(key);
    }

    private static void printEntry(String key) {
	String url = key.replaceFirst("DBLP:", "http://dblp.uni-trier.de/rec/bib2/") + ".bib";
	try {
	    URL website = new URL(url);
	    try (InputStream is = website.openStream();) {
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		String inputLine;
		while ((inputLine = in.readLine()) != null)
		    System.out.println(inputLine);
	    } catch (IOException e) {
		System.err.printf(NOKEY, key);
	    }
	} catch (MalformedURLException e) {
	    System.err.printf(NOKEY, key);
	}
    }

    private static Set<String> getKeys(File texfile) {
	Set<String> refs = new HashSet<>();
	Pattern p = Pattern.compile("cite\\{[^{},]+(,[^{},]+)*");

	try (BufferedReader br = new BufferedReader(new FileReader(texfile))) {
	    String line;
	    while ((line = br.readLine()) != null) {
		Matcher m = p.matcher(line);
		while (m.find())
		    refs.addAll(Arrays.asList(m.group().substring(5).split("\\s*,\\s*")));
	    }
	} catch (FileNotFoundException e) {
	    System.err.printf(FNF, texfile.getName());
	} catch (IOException e) {
	    System.err.printf(IOE, texfile.getName());
	}
	return refs;
    }
}
