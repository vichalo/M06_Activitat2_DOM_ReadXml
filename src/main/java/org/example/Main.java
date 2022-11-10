package org.example;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;



public class Main {
    ArrayList<ResultadoCarrera_TO_DO>resultadoCarrera_to_do=new ArrayList<>();
    public void create(Document document, String name) {
        Element element = document.createElement(name);
        document.appendChild(element);
    }

    public void append(Document document, Element elementRoot, String nomCamp, String info) {
        Element element2 = document.createElement(nomCamp);
        Text text = document.createTextNode(info);
        element2.appendChild(text);
        elementRoot.appendChild(element2);
    }

    public void Read() {
        File file = new File("monaco_2017.xml");
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(file);
            NodeList nodeList = document.getElementsByTagName("Result");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node=nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    resultadoCarrera_to_do.add(new ResultadoCarrera_TO_DO(element));

                }
            }

        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }
        public static void main (String[]args){
            Main app = new Main();
            String[] moduls = {"Acces a Dades", "Programació de serveis i processos",
                    "Desenvolupament d' interficies", "Programació multimedia i dispositius mobils",
                    "Sistemes de gestio empresarial", "Formacio i orientacio laboral"};

            boolean[] permetDUAL = {false, true, false, false, true, true};
            int[] hores = {96, 93, 96, 95, 55, 63};
            double[] notes = {8.45, 9.0, 8.0, 7.34, 8.2, 7.4};


            try (FileWriter fr = new FileWriter("arxiu.xml")) {
                DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                Document document = builder.newDocument();
                document.setXmlVersion("1.0");

                app.create(document, "modulos");
                for (int i = 0; i < moduls.length; i++) {
                    Element modulos = document.createElement("modulo");
                    modulos.setAttribute("hores", String.valueOf(hores[i]));
                    modulos.setAttribute("permet_dual", String.valueOf(permetDUAL[i]));
                    document.getDocumentElement().appendChild(modulos);
                    app.append(document, modulos, "nombre", moduls[i]);
                    app.append(document, modulos, "nota", String.valueOf(notes[i]));

                }

                Source source = new DOMSource(document);
                Result result = new StreamResult(fr);
                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xalan}indent-amount", "5");
                transformer.transform(source, result);
                Result console = new StreamResult(System.out);
                transformer.transform(source, console);

            } catch (IOException | ParserConfigurationException | TransformerException e) {
                throw new RuntimeException(e);
            }
            app.Read();
            for (ResultadoCarrera_TO_DO r: app.resultadoCarrera_to_do) {
                System.out.println(r);
            }
            for (int i = 0; i < app.resultadoCarrera_to_do.size(); i++) {
                System.out.println();
            }
        }
    }