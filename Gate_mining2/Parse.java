
import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import java.util.*;
import java.io.*;

class Node
{
	private int id;
	private String val;

	public Node(int id, String val)
	{
		this.id = id;
		this.val = val;
	}

	public String toString()
	{
		return "Node id="+id+" val="+val;
	}
}

class PFlowLoader extends DefaultHandler
{
	private String value;

	private String[] symptom;
	private String ant;
	private String name;
	private String val;
	private boolean nasiel;

	public PFlowLoader()
	{
		reset();
	}

	private void reset()
	{
		value = "";

		nasiel = false;
		name = null;
		val = null;
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException
	{
		value = new String(ch, start, length);
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
	{
		int n = 0;
		int i;
/*
		for(i = 0; i < symptom.length; i++)
		{
			if( attributes != null && attributes.getValue("Type") != null && attributes.getValue("Type").equals(symptom[i]) )
			{
				break;
			}
		}

		if( qName.equals("Annotation") && ( i < symptom.length ) )
		{
			nasiel = nasiel;
			ant = attributes.getValue("Type");
			System.out.println("Annotation "+attributes.getValue("Type"));
			System.out.println("\t<patient>");
		}
*/
		//System.out.println("startElement qName = >"+qName+"< id = >"+attributes.getValue("id")+"< localName = >"+localName+"< qName = >"+qName+"<");
        }

	public boolean isNumeric(String str)
	{
		return true;
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException
	{
		int i;

		//System.out.println("endElement qName = >"+qName+"< localName = >"+localName+"< qName = >"+qName+"< value = >"+value+"<");

		for(i = 0; i < symptom.length; i++)
		{
			if( qName.equals("Name") && value.equals(symptom[i]) )
			{
				break;
			}
		}

			if( qName.equals("Value") && nasiel )
			{
				if( name.equals("diagnoze") )
				{
					System.out.println("\t\t<diagnoze>"+value+"</diagnoze>");
				}
				else
				{
					System.out.println("\t\t<symptom name=\""+name+"\">"+value+"</symptom>");
				}

				nasiel = false;
			}

		if( i < symptom.length )
		{
			nasiel = true;
			//System.out.println("value = "+value);
			name = value;
		}
	}

	public boolean load(String filename, String[] symptom)
	{
		this.symptom = symptom;

		try{
			System.out.println("\t<pacient>");

			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = saxParserFactory.newSAXParser();
			saxParser.parse(filename, this);

			System.out.println("\t</pacient>");
		}catch (Exception e)
		{
			System.out.println(filename+"\tNemozem nacitat subor");
			e.printStackTrace();
			return false;
		}

		return true;
	}
}

public class Parse
{
	public static void main(String[] args)
	{
		PFlowLoader x = new PFlowLoader();
		String filename = args[0];
		String disease = args[1];

		x.load(filename, args);
	}
}
