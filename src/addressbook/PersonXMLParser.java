/*
 * Copyright (C) 2014 Javier
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package addressbook;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Javier
 */
public class PersonXMLParser {

    private Document documento;
    
    public PersonXMLParser(String xmlFileName) {
        documento = getXMLDocument(xmlFileName);
    }
    
    private Document getXMLDocument(String xmlFileName) {
        Document documento = null;
        try {
            DocumentBuilderFactory fábricaCreadorDocumento = DocumentBuilderFactory.newInstance();
            DocumentBuilder creadorDocumento = fábricaCreadorDocumento.newDocumentBuilder();
            documento = creadorDocumento.parse(xmlFileName);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(PersonXMLParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(PersonXMLParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PersonXMLParser.class.getName()).log(Level.SEVERE, null, ex);
        }  
        return documento;
    }
    
    public ArrayList<Person> getPersonsList() {
        ArrayList<Person> personsList = new ArrayList();
        NodeList nodeListPerson = documento.getElementsByTagName("person");
        for(int i=0; i<nodeListPerson.getLength(); i++) {
            Node nodePerson = nodeListPerson.item(i);
            NodeList nodeListData = nodePerson.getChildNodes();
                    
            String[] d = new String[15];
//            for(int j=0; j<nodeListData.getLength(); j++) {
            for(int j=0; j<15; j++) {
                d[j] = nodeListData.item(j).getTextContent();
            }
            int id = Integer.valueOf(d[0]);
            Date fechaNac = stringToDate(d[12]);
            Person person = new Person(id, d[1], d[2], d[3], d[4], d[5], d[6], d[7], d[8], d[9], d[10], d[11], fechaNac, d[13], d[14]);
            personsList.add(person);
        }
        return personsList;
    }
    
    private Date stringToDate(String str) {
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = formato.parse(str);
        } catch (ParseException ex) {
            Logger.getLogger(PersonXMLParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return date;
    }
    
}
