package parsers.dom;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import parsers.models.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DomParser {
    private static final Logger LOGGER = LogManager.getLogger(DomParser.class);
    Cutter cutter = new Cutter();
    Engeneer engeneer = new Engeneer();
    Equipment equipment = new Equipment();
    ServiceStation serviceStation = new ServiceStation();
    Worker worker = new Worker();
    List<Worker> workersList = new ArrayList<>();
    List<Compressor> compressors = new ArrayList<>();
    List<Equipment> eqipmentList = new ArrayList<>();

    public void parse() throws ParserConfigurationException, IOException, SAXException, ParseException {
        File xmlFile = new File(System.getProperty("user.dir") + "/src/main/resources/test.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(xmlFile);
        doc.getDocumentElement().normalize();
        Element element = doc.getDocumentElement();
        serviceStation.setId(Integer.parseInt(element.getAttribute("id")));
        serviceStation.setName(element.getElementsByTagName("name").item(0).getTextContent());
        serviceStation.setAddress(element.getElementsByTagName("address").item(0).getTextContent());
        NodeList nodeList = doc.getElementsByTagName("engeneer");
        worker.setEngeneer(engeneer);
        Node engeneerNode = nodeList.item(0);
        Element element1 = (Element) engeneerNode;
        engeneer.setName(element1.getElementsByTagName("name").item(0).getTextContent());
        engeneer.setSurname(element1.getElementsByTagName("surname").item(0).getTextContent());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
        engeneer.setBirthday(sdf.parse(element1.getElementsByTagName("birthday").item(0).getTextContent()));
        worker.setEngeneer(engeneer);
        workersList.add(worker);
        serviceStation.setWorkers(workersList);
        nodeList = doc.getElementsByTagName("compressor");


        for (int i = 0; i < nodeList.getLength(); i++) {
            Compressor compressor = new Compressor();
            Node node = nodeList.item(i);
            Element el = (Element) node;
            compressor.setManufacture(el.getElementsByTagName("manufacture").item(0).getTextContent());
            compressor.setPerformance(Integer.parseInt(el.getElementsByTagName("performance").item(0).getTextContent()));
            compressor.setYear(Integer.parseInt(el.getElementsByTagName("year").item(0).getTextContent()));
            compressors.add(compressor);
            equipment.setCutter(cutter);
            equipment.setCompressors(compressors);

        }
        NodeList cutterList = doc.getElementsByTagName("cutter");
        Node cutterNode = cutterList.item(0);
        Element cutterElement = (Element) cutterNode;
        cutter.setManufacture(cutterElement.getElementsByTagName("manufacture").item(0).getTextContent());
        cutter.setSteelHardness(Integer.parseInt(cutterElement.getElementsByTagName("steel_hardness").item(0).getTextContent()));


        eqipmentList.add(equipment);
        serviceStation.setEquipment(eqipmentList);

        LOGGER.info(serviceStation);
    }

    public ServiceStation takeServiceStation() {
        return serviceStation;
    }

}
