
import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import java.util.*;

class PFlowLoader extends DefaultHandler
{
	private String value;

	private String disease;
	private String ant;
	private String name;
	private String val;

	public PFlowLoader()
	{
		reset();
	}

	private void reset()
	{
		value = "";

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

		if( qName.equals("Annotation") && attributes.getValue("Type").equals(disease) )
		{
			ant = attributes.getValue("Type");
			//System.out.println("Annotation "+attributes.getValue("Type"));
			System.out.println("\t<patient>");
		}

		//System.out.println("startElement qName = >"+qName+"< id = >"+attributes.getValue("id")+"< localName = >"+localName+"< qName = >"+qName+"<");
        }

	public boolean isNumeric(String str)
	{
		return true;
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException
	{
		//System.out.println("endElement qName = >"+qName+"< localName = >"+localName+"< qName = >"+qName+"< value = >"+value+"<");

		if( qName.equals("Name") )
		{
			//System.out.println("Name = >"+value+"<");
			name = value;
		}

		if( qName.equals("Value") )
		{
			//System.out.println("Value = >"+value+"<");

			val = value;

			if( ant != null && ant.equals(disease) && name != null && val != null )
			{
				if( name.equals("diagnoze") )
				{
					System.out.println("\t\t<diagnosis>"+val+"></diagnosis>");
				}
				else
				{
					System.out.println("\t\t<symptom name=\""+name+"\">"+val+"</symptom>");
				}
			}

			reset();
		}

		if( qName.equals("Annotation") && ant != null && ant.equals(disease) )
		{
			//System.out.println("end Annotation");

			System.out.println("\t</patient>");
			ant = null;
		}
	}

	public boolean load(String filename, String disease)
	{
		this.disease = disease;

		try{
			System.out.println("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
			System.out.println("<records>");

			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = saxParserFactory.newSAXParser();
			saxParser.parse(filename, this);

			System.out.println("</records>");
		}catch (Exception e)
		{
			System.out.println(filename+"\tNemozem nacitat subor");
			//e.printStackTrace();
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

		x.load(filename, disease);
	}
}
