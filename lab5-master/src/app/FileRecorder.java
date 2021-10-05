package app;

import data.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.TreeSet;

/**
 * Класс для обработки файлов
 */
public class FileRecorder {
    private TreeSet <Labwork> labworks = new TreeSet<Labwork>();
    public boolean check = true;
    public FileRecorder (TreeSet <Labwork> labworks) {
        this.labworks = labworks;
    }

    /**
     * Метод создает коллекцию treeset из файла
     * @param file
     */
    public void makeTreeSetFromFile (File file) {
        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);
            NodeList labworksElements = document.getDocumentElement().getElementsByTagName("labwork");

            for (int i = 0; i < labworksElements.getLength(); i++) {
                Node labwork = labworksElements.item(i);
                NamedNodeMap attributes = labwork.getAttributes();
                Coordinates coordinates = new Coordinates(Long.parseLong(attributes.getNamedItem("xc").getNodeValue()), Float.parseFloat(attributes.getNamedItem("yc").getNodeValue()));
                LocalDateTime cd = LocalDateTime.parse(attributes.getNamedItem("creationDate").getNodeValue());
                double mp = Double.parseDouble(attributes.getNamedItem("minimalPoint").getNodeValue());

                Difficulty d;
                if (attributes.getNamedItem("difficulty") != null) {
                    d = Difficulty.valueOf(attributes.getNamedItem("difficulty").getNodeValue());
                } else {
                    d = null;
                }

                java.time.LocalDate birthday;
                if (attributes.getNamedItem("birthday") != null) {
                    birthday = LocalDate.parse(attributes.getNamedItem("birthday").getNodeValue());
                } else {
                    birthday = null;
                }
                data.hair.Color hairColor;
                if (attributes.getNamedItem("hairColor") != null) {
                    hairColor = data.hair.Color.valueOf(attributes.getNamedItem("hairColor").getNodeValue());
                } else {
                    hairColor = null;
                }

                Location location;
                Float xl;
                Double yl;
                String locationName;
                if (attributes.getNamedItem("xl") != null) {
                    if (attributes.getNamedItem("yl") == null) throw new IllegalArgumentException();
                    else {
                        xl = Float.parseFloat(attributes.getNamedItem("xl").getNodeValue());
                        yl = Double.parseDouble(attributes.getNamedItem("yl").getNodeValue());
                        if (attributes.getNamedItem("locationName") != null) {
                            locationName = attributes.getNamedItem("locationName").getNodeValue();
                        } else {
                            locationName = null;
                        }
                        location = new Location(xl, yl, locationName);
                    }
                } else {
                    if (attributes.getNamedItem("yl") != null) throw new IllegalArgumentException();
                    else {
                        if (attributes.getNamedItem("locationName") != null) throw new IllegalArgumentException();
                        else {
                            location = null;
                        }
                    }
                }

                Person person;
                if (attributes.getNamedItem("PersonName") != null) {
                    person = new Person(attributes.getNamedItem("PersonName").getNodeValue(), birthday, data.Color.valueOf(attributes.getNamedItem("eyeColor").getNodeValue()), hairColor, location);
                } else {
                    person = null;
                }

                labworks.add(new Labwork(i + 1, attributes.getNamedItem("name").getNodeValue(), coordinates, cd, mp, d, person));
            }

        } catch (IOException e) {
            System.out.println("Неверно введено имя файла");
            check = false;
        } catch (ParserConfigurationException e) {
            check = false;
            System.out.println("Ошибка парсера");
        } catch (IllegalArgumentException e) {
            check = false;
            System.out.println("Неправильно введены данные в файле");
        } catch (SAXException e) {
            check = false;
            System.out.println("Неправильно заполнен файл");
        }
    }

    /**
     * Метод записывает имеющуюся коллекцию в файл
     * @param file
     */
    public void writeToFile(File file) {
        String text = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<project>\n" + "\t<Labwork>\n";

        for (Labwork lb: labworks) {
            text = text + "\t\t<labwork name = \"" + lb.getName() +
                    "\" xc = \"" + String.valueOf(lb.getCoordinates().getX())
                    + "\" yc = \"" + String.valueOf(lb.getCoordinates().getY()) +
                    "\" creationDate = \"" + String.valueOf(lb.getCreationDate()) +
                    "\" minimalPoint = \"" + String.valueOf(lb.getMinimalPoint()) + "\" ";
            if (lb.getDifficulty() != null) {
                text = text + "difficulty = \"" + String.valueOf(lb.getDifficulty()) + "\" ";
            }
            if (lb.getAuthor() != null) {
                text = text + "PersonName = \"" + lb.getAuthor().getName() + "\" ";
                if (lb.getAuthor().getBirthday() != null ) {
                    text = text + "birthday =\"" + String.valueOf(lb.getAuthor().getBirthday()) + "\" ";
                }
                text = text + "eyeColor = \"" + String.valueOf(lb.getAuthor().getEyeColor()) + "\" ";
                if (lb.getAuthor().getHairColor() != null ) {
                    text = text + "hairColor = \"" + String.valueOf(lb.getAuthor().getHairColor()) + "\" ";
                }
                if (lb.getAuthor().getLocation() != null) {
                    text = text + "xl = \"" + String.valueOf(lb.getAuthor().getLocation().getX()) +
                            "\" yl = \"" + String.valueOf(lb.getAuthor().getLocation().getY()) + "\" ";
                    if (lb.getAuthor().getLocation().getName() != null) {
                        text = text + "locationName = \"" + lb.getAuthor().getLocation().getName() + "\" ";
                    }
                }
            }
            text = text + "/>\n";
        }

        text = text + "\t</Labwork>\n" +
                "</project>";
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file))) {
            byte [] buffer = text.getBytes();
            bos.write(buffer, 0, buffer.length);
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Ошибка ввода");
            e.printStackTrace();
        }

    }
}
