package stuba.fei.tp.cdss.parser;

import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

import stuba.fei.tp.cdss.dto.Symptom;

import java.util.*;
import java.io.*;

public class XMLgetSymptomName extends DefaultHandler {
	private String name;
	private String val;
	private Set set;

	public XMLgetSymptomName() {
		set = new HashSet();
		reset();
	}

	private void reset() {
		name = null;
		val = null;
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		val = new String(ch, start, length);
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		int n = 0;

		// System.out.println("uri="+uri+" localName="+localName+" qName="+qName);

		if (qName.equals("symptom")) {
			set.add(attributes.getValue("name"));
			// System.out.println("name="+attributes.getValue("name"));
		}
	}

	public ArrayList<Symptom> getSymptomArray() {
		ArrayList<Symptom> symptoms = new ArrayList<Symptom>();
		Object[] array = set.toArray();

		for (int i = 0; i < array.length; i++) {
			String string = (String) array[i];
			Symptom s = new Symptom(string);

			symptoms.add(s);
		}

		return symptoms;
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// System.out.println("endElement uri="+uri+" localName="+localName+" qName="+qName);
	}

	public boolean load(InputStream fileInputStream) {
		try {
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = saxParserFactory.newSAXParser();
			saxParser.parse(fileInputStream, this);
		} catch (Exception e) {
			System.out.println(fileInputStream.toString() + "\tNemozem nacitat subor");
			// e.printStackTrace();
			return false;
		}

		return true;
	}
}
