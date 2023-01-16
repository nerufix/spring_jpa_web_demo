package pl.ug.edu.mwitt.jpa;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import javax.xml.XMLConstants;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.nio.charset.Charset;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

public class CSVConversion  {

    static List<String> refNames = List.of("team", "winner", "person", "match");

    public static String prettyFormat(String input, int indent) {
        try {
            Source xmlInput = new StreamSource(new StringReader(input));
            StringWriter stringWriter = new StringWriter();
            StreamResult xmlOutput = new StreamResult(stringWriter);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute("indent-number", indent);
            transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(xmlInput, xmlOutput);
            return xmlOutput.getWriter().toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String prettyFormat(String input) {
        return prettyFormat(input, 2);
    }

    public static void exportToXml(String filename, String classname) throws IOException {
        String content = StreamUtils.copyToString(
                new ClassPathResource(filename+".csv").getInputStream(),
                Charset.defaultCharset());
        StringBuilder sb = new StringBuilder();
        sb.append("<beans xmlns=\"http://www.springframework.org/schema/beans\"" +
                " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
                " xsi:schemaLocation=\"http://www.springframework.org/schema/beans" +
                " http://www.springframework.org/schema/beans/spring-beans.xsd\">");
        CSVParser parser = CSVParser.parse(content, CSVFormat.EXCEL.withHeader().withDelimiter(';'));
        for (CSVRecord rec : parser) {
            sb.append("<bean id=\""+rec.get("id")+"\" " +
                    "class=\"pl.ug.edu.mwitt.jpa.domain."+classname+"\">");
            for (String prop : parser.getHeaderNames()) {
                if (rec.get(prop).equals("null")) {
                    sb.append("<property name=\""+prop+"\"><null/></property>");
                } else if (refNames.contains(prop)) {
                    sb.append("<property name=\""+prop+"\" ref=\""+rec.get(prop)+"\" />");
                } else if (rec.get(prop).charAt(0)=='[') {
                    sb.append("<property name=\""+prop+"\" >");
                    sb.append("<set>");
                    String[] refs = rec.get(prop).substring(1, rec.get(prop).length()-1).split(",");
                    for (String ref : refs) {
                        sb.append("<ref bean=\""+ref+"\" />");
                    }
                    sb.append("</set>");
                    sb.append("</property>");
                } else {
                    sb.append("<property name=\""+prop+"\" value=\""+rec.get(prop)+"\" />");
                }
            }
            sb.append("</bean>");
        }
        sb.append("</beans>");
        System.out.println(prettyFormat(sb.toString()));

        String path = "src/main/resources/"+filename+".xml";
        File file = new File(path);
        String absolutePath = file.getAbsolutePath();
        BufferedWriter writer = new BufferedWriter(
                new FileWriter(absolutePath));
        writer.write(prettyFormat(sb.toString()));
        writer.close();
    }

    public static void main(String[] args) throws IOException {

        exportToXml("teamData", "Team");
        exportToXml("personData", "Person");
        exportToXml("matchData", "Match");
        exportToXml("betData", "Bet");
    }

}