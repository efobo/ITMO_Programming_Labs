package app;

import data.Labwork;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Класс для работы с коллекцией
 */
public class CollectionManager {
    private TreeSet<Labwork> labworks;
    private File file;
    private LocalDateTime initTime;
    private LocalDateTime saveTime;
    private Asker asker;
    private BufferedReader inputReader;


    public CollectionManager (TreeSet<Labwork> labworks, File file) {
        this.labworks=labworks;
        this.file = file;
        this.inputReader = new BufferedReader(new InputStreamReader(System.in));
        this.asker = new Asker(labworks, inputReader);
        this.initTime = null;
        this.saveTime = null;
    }

    /**
     * Возвращает коллекцию
     * @return labworks
     */
    public TreeSet<Labwork> getCollection () {return labworks;}

    /**
     * Возвращает первый элемент коллекции
     * @return first element
     */
    public Labwork getFirstElement () {return labworks.first();}

    /**
     * Возвращает время инициализации
     * @return init time
     */
    public LocalDateTime getInitTime () {return initTime;}

    /**
     * Возвращает время сохранения
     * @return save time
     */
    public LocalDateTime getSaveTime () {return saveTime;}

    /**
     * Возвращает тип коллекции
     * @return collection type
     */
    public String collectionType() {
        return labworks.getClass().getName();
    }

    /**
     *Возвращает размер коллекции
     * @return collection size
     */
    public int collectionSize() {
        return labworks.size();
    }

    /**
     * Получить элемент по id
     * @param id
     * @return labwork
     */
    public Labwork getById(Long id) {
        for (Labwork lb : labworks) {
            if (lb.getId() == id) return lb;
        }
        return null;
    }

    /**
     * Получить такой же по параметрам элемент
     * @param lbToCompare
     * @return labwork
     */
    public Labwork getByValue (Labwork lbToCompare) {
        for (Labwork lb: labworks) {
            if (lb.equals(lbToCompare)) return lb;
        }
        return null;
    }

    /**
     * Генерирует новый id
     * @return id
     */
    public Long generateID () {
        if (labworks.isEmpty()) return 1L;
        return labworks.last().getId() + 1;
    }

    /**
     * Выбрать элемент с наименьшим id
     * @return id
     */
    public Labwork chooseElementWithLowestID () {
        long m = labworks.first().getId();
        Labwork lbToReturn = labworks.first();
        for (Labwork lb : labworks) {
            if (lb.getId() < m) {
                m = lb.getId();
                lbToReturn = lb;
                }
            }
        if (lbToReturn == null) {
            return null;
        } else return lbToReturn;
    }

    /**
     * Удалить элемент из коллекции
     * @param lb
     */
    public void remove (Labwork lb) { labworks.remove(lb); }

    /**
     * Удалить больший элемент из коллекции
     * @param lb
     */
    public void removeGreater (Labwork lb) {
        labworks.removeIf(labwork -> labwork.compareTo(lb) > 0);
    }

    /**
     * Удалить меньший элемент из коллекции
     * @param lb
     */
    public void removeLower (Labwork lb) {
        labworks.removeIf(labwork -> labwork.compareTo(lb) < 0);
    }

    /**
     * Очистить коллекцию
     */
    public void clearCollection() {
        labworks.clear();
    }

    /**
     * Добавить элемент в коллекцию
     * @param lb
     */
    public void addToCollection (Labwork lb) {labworks.add(lb);}

    public boolean addToCollectionIfMin (Labwork lbToCompare) {
        if (labworks.size() == 0 || lbToCompare.compareTo(labworks.first()) < 0) {
            labworks.add(lbToCompare);
            return true;
        }
        return false;
    }

    /**
     * Сохранить коллекцию в файл
     */
    public void saveCollection () {
        FileRecorder fileRecorder = new FileRecorder(labworks);
        fileRecorder.writeToFile(file);
    }

    /**
     * Показать коллекцию в убывающем порядке
     */
    public void showDescending () {
        Iterator<Labwork> descendingIterator = labworks.descendingIterator();
        for (int l = labworks.size(); l>0; l--) {
            String element = descendingIterator.next().toString();
            System.out.println(element);
        }
    }


}

