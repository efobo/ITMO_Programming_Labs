package managers;

import app.Main;
import data.*;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;

public class CollectionFileRecorder {
    public boolean check = true;
    private final File file;

    public CollectionFileRecorder(File file) {
        this.file = file;
    }

    public TreeSet<Labwork> readCollection() {
        TreeSet<Labwork> labworks = new TreeSet<>(new SizeComparator());

        if (file != null) {
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

                    LocalDate birthday;
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
                    float xl;
                    double yl;
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
                        person = new Person(attributes.getNamedItem("PersonName").getNodeValue(), birthday, Color.valueOf(attributes.getNamedItem("eyeColor").getNodeValue()), hairColor, location);
                    } else {
                        person = null;
                    }
                    labworks.add(new Labwork(i + 1, attributes.getNamedItem("name").getNodeValue(), coordinates, cd, mp, d, person));
                }
                System.out.println("Коллекция успешно загружена");
                ResponseOutput.appendln("Коллекция успешно загружена");
                Main.logger.log(Level.INFO, "Коллекция успешно загружена");

            } catch (ParserConfigurationException e) {
                check = false;
                System.out.println("Ошибка парсера");
                ResponseOutput.appendln("Ошибка парсера");
                Main.logger.log(Level.SEVERE, "Ошибка парсера");
            } catch (IOException e) {
                check = false;
                System.out.println("Ошибка ввода");
                ResponseOutput.appendln("Ошибка ввода");
                Main.logger.log(Level.WARNING, "Ошибка ввода");
            } catch (SAXException e) {
                check = false;
                ResponseOutput.appendln("Неправильно заполнен файл");
                System.out.println("Неправильно заполнен файл");
                Main.logger.log(Level.SEVERE, "Неправильно заполнен файл");
            } catch (IllegalArgumentException e) {
                check = false;
                System.out.println("Неправильно введены данные в файле");
                ResponseOutput.appendln("Неправильно введены данные в файле");
                Main.logger.log(Level.SEVERE, "Неправильно введены данные в файле");
            } catch (IllegalStateException e) {
                check = false;
                System.out.println("Непредвиденная ошибка");
                ResponseOutput.appendln("Непредвиденная ошибка");
                Main.logger.log(Level.SEVERE, "Непредвиденная ошибка");
                System.exit(0);
            }
        } else System.out.println("Передано значение null вместо файла");
        return labworks;
    }

    public void writeToFile(Collection<Labwork> labworks) {
        if (file != null) {
            StringBuilder text = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<project>\n" + "\t<Labwork>\n");

            for (Labwork lb : labworks) {
                text.append("\t\t<labwork name = \"").append(lb.getName()).append("\" xc = \"").append(lb.getCoordinates()
                        .getX()).append("\" yc = \"").append(lb.getCoordinates().getY()).append("\" creationDate = \"")
                        .append(lb.getCreationDate()).append("\" minimalPoint = \"").append(lb.getMinimalPoint()).append("\" ");
                if (lb.getDifficulty() != null) {
                    text.append("difficulty = \"").append(lb.getDifficulty()).append("\" ");
                }
                if (lb.getAuthor() != null) {
                    text.append("PersonName = \"").append(lb.getAuthor().getName()).append("\" ");
                    if (lb.getAuthor().getBirthday() != null) {
                        text.append("birthday =\"").append(lb.getAuthor().getBirthday()).append("\" ");
                    }
                    text.append("eyeColor = \"").append(lb.getAuthor().getEyeColor()).append("\" ");
                    if (lb.getAuthor().getHairColor() != null) {
                        text.append("hairColor = \"")
                                .append(lb.getAuthor()
                                        .getHairColor())
                                .append("\" ");
                    }
                    if (lb.getAuthor().getLocation() != null) {
                        text.append("xl = \"").append(lb.getAuthor().getLocation().getX()).append("\" yl = \"")
                                .append(lb.getAuthor().getLocation().getY()).append("\" ");
                        if (lb.getAuthor().getLocation().getName() != null) {
                            text.append("locationName = \"").append(lb.getAuthor().getLocation().getName()).append("\" ");
                        }
                    }
                }
                text.append("/>\n");
            }

            text.append("\t</Labwork>\n").append("</project>");

            try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file))) {
                byte[] buffer = text.toString().getBytes();
                bos.write(buffer, 0, buffer.length);
                ResponseOutput.appendln("Коллекция сохранена в файл");
            } catch (IOException e) {
                ResponseOutput.appendln("Ошибка! Файл является директорией и не может быть открыт");
            }
        } else ResponseOutput.appendln("Системная переменная с загрузочным файлом не найдена");

    }
}
