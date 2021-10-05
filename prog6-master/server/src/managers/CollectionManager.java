package managers;

import Exceptions.EmptyCollectionException;
import data.Labwork;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Класс для работы с коллекцией
 */
public class CollectionManager {
    private TreeSet<Labwork> labworks;
    private LocalDateTime initTime;
    private LocalDateTime saveTime;


    public CollectionManager (TreeSet<Labwork> labworks) {
        this.labworks = labworks;
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
        return labworks.stream().filter(lb -> lb.getId() == id).findFirst().orElse(null);
    }

    /**
     * Получить такой же по параметрам элемент
     * @param lbToCompare
     * @return labwork
     */
    public Labwork getByValue (Labwork lbToCompare) {
        return labworks.stream().filter(lb -> lb.equals(lbToCompare)).findFirst().orElse(null);
    }

    /**
     * Генерирует новый id
     * @return id
     */
    public Long generateID () {
        if (labworks.isEmpty()) return 1L;
        return getMaxID() + 1;
    }

    /**
     * Возвращает значение максимального id на данный момент
     * @return max ID
     */
    public Long getMaxID () {
        long m =labworks.stream().map(lb -> lb.getId()).max(Long :: compareTo).get();
        return m;
    }

    /**
     * Выбрать элемент с наименьшим id
     * @return id
     */
    public Labwork chooseElementWithLowestID () throws EmptyCollectionException {
        if (labworks.isEmpty()) throw new EmptyCollectionException();
        long m = labworks.stream().map(lb -> lb.getId()).min(Long :: compareTo).get();
        return labworks.stream().filter(lb -> lb.getId() == m).findFirst().get();

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

    /**
     * Сохранить коллекцию в файл
     */
    public void saveCollection (CollectionFileRecorder cfr) {
        cfr.writeToFile(labworks);
        saveTime = LocalDateTime.now();
    }

    /**
     * Показать коллекцию в убывающем порядке
     */
    public void showDescending () {
        labworks.stream().sorted((o1, o2) -> -o1.compareTo(o2)).forEach(deskLb -> ResponseOutput.appendln(deskLb.toString()));
    }


}

