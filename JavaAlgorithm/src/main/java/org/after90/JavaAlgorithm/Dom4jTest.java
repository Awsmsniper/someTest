package org.after90.JavaAlgorithm;

import java.io.FileWriter;
import java.io.StringWriter;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Dom4jTest {
	public void writeXmlFile() {
		try {
			Document document = DocumentHelper.createDocument();
			OutputFormat of = OutputFormat.createPrettyPrint();
			of.setEncoding("UTF-8");
			Element root = document.addElement("root");

			Element author1 = root.addElement("author").addAttribute("name", "James汉字").addAttribute("location", "UK")
					.addText("James Strachan");

			Element author2 = root.addElement("author").addAttribute("name", "Bob").addAttribute("location", "US")
					.addText("Bob McWhirter");
			StringWriter sw = new StringWriter();
			XMLWriter writer = new XMLWriter(sw, of);
			writer.write(document);
			writer.flush();
			writer.close();
			log.info(sw.toString());
		} catch (Exception e) {
			log.error("", e);
		}
	}
}
