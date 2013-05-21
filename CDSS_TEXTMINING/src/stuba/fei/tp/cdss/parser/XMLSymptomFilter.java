package stuba.fei.tp.cdss.parser;

import java.io.InputStream;

import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

public class XMLSymptomFilter extends DefaultHandler
{
	private String[] requiredSymptoms;
	private String res;
	private String name;
	private String val;
	private boolean write;

	public XMLSymptomFilter(String[] requiredSymptoms)
	{
		this.requiredSymptoms = requiredSymptoms;
		res = "";
		reset();
	}

	private void reset()
	{
		name = "";
		val = "";
		write = false;
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException
	{
		val = new String(ch, start, length);
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
	{
		if( ! qName.equals("symptom") )
		{
			res += "<"+qName+">";

			if( ! qName.equals("diagnosis") )
			{
				res += "\n";
			}
		}
		else
		{
			String name = attributes.getValue("name");
			int i;

			for(i = 0; i < requiredSymptoms.length; i++)
			{
				if( requiredSymptoms[i].equals(name) )
				{
					break;
				}
			}

			if( i < requiredSymptoms.length )
			{
				res += "<"+qName+" name=\""+name+"\">";
				write = true;
			}
		}
        }

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException
	{
		if( qName.equals("symptom") )
		{
			if( write )
			{
				res += val;
				res += "</"+qName+">\n";
			}

			reset(); 
			return;
		}

		if( qName.equals("diagnosis") )
		{
			res += val;
			res += "</"+qName+">\n";
			reset();
			return;
		}

		res += "</"+qName+">\n";

		reset();
	}

	public String getXML()
	{
		return res;
	}

	public boolean load(InputStream fileInputStream)
	{
		try{
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = saxParserFactory.newSAXParser();
			saxParser.parse(fileInputStream, this);
		}catch (Exception e)
		{
			System.out.println(fileInputStream+"\tNemozem nacitat subor");
			//e.printStackTrace();
			return false;
		}

		return true;
	}
}
