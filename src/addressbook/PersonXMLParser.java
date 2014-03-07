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

import com.sun.org.apache.xml.internal.serializer.OutputPropertiesFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.sql.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

/**
 *
 * @author Javier
 */
public class PersonXMLParser {

    private static Document documento;
    
    public PersonXMLParser(String xmlFileName) {
        documento = getXMLDocument(xmlFileName);
    }
    
    private Document getXMLDocument(String xmlFileName) {
        documento = null;
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
            int k=0;
            for(int j=0; j<nodeListData.getLength(); j++) {
                if(nodeListData.item(j).getNodeType()==Node.ELEMENT_NODE) {
                    d[k++] = nodeListData.item(j).getTextContent();
                }
            }
            int id = Integer.valueOf(d[0]);
            Date fechaNac = Date.valueOf(d[12]);
            Person person = new Person(id, d[1], d[2], d[3], d[4], d[5], d[6], d[7], d[8], d[9], d[10], d[11], fechaNac, d[13], d[14]);
            personsList.add(person);
        }
        return personsList;
    }
    
    public static void createXML(ArrayList<Person> personsList, String xmlFileName) {
        try {
            DocumentBuilderFactory fábricaCreadorDocumento = DocumentBuilderFactory.newInstance();
            DocumentBuilder creadorDocumento = fábricaCreadorDocumento.newDocumentBuilder();
            documento = creadorDocumento.newDocument();
            
            Element raiz = documento.createElement("persons");
            documento.appendChild(raiz);
                        
            for(Person person: personsList) {
                Element elementPerson = documento.createElement("person");
                raiz.appendChild(elementPerson);
                
                addElementData(elementPerson, "id", String.valueOf(person.getId()));
                addElementData(elementPerson, "name", person.getName());
                addElementData(elementPerson, "surnames", person.getSurnames());
                addElementData(elementPerson, "alias", person.getAlias());
                addElementData(elementPerson, "email", person.getEmail());
                addElementData(elementPerson, "phone_number", person.getPhoneNumber());
                addElementData(elementPerson, "mobile_number", person.getMobileNumber());
                addElementData(elementPerson, "address", person.getAddress());
                addElementData(elementPerson, "post_code", person.getPostCode());
                addElementData(elementPerson, "city", person.getCity());
                addElementData(elementPerson, "province", person.getProvince());
                addElementData(elementPerson, "country", person.getCountry());
                addElementData(elementPerson, "birth_date", person.getBirthDate().toString());
                addElementData(elementPerson, "comments", person.getComments());
                addElementData(elementPerson, "photo_file_name", person.getPhotoFileName());
            }
            
            //Mostrar en salida estándar el documento XML generado 
            TransformerFactory fábricaTransformador = TransformerFactory.newInstance();
            Transformer transformador = fábricaTransformador.newTransformer();
            transformador.setOutputProperty(OutputKeys.INDENT, "yes");
            transformador.setOutputProperty(OutputPropertiesFactory.S_KEY_INDENT_AMOUNT, "3");            Source origen = new DOMSource(documento);
            Result destino = new StreamResult(xmlFileName);
            transformador.transform(origen, destino);            
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(PersonXMLParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(PersonXMLParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(PersonXMLParser.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    private static void addElementData(Element parent, String tag, String value) {
        Element element = documento.createElement(tag);
        parent.appendChild(element);
        Text text = documento.createTextNode(value);
        element.appendChild(text);        
    }
    
}
