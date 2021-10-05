package data;

import java.util.Comparator;

public class SizeComparator implements Comparator<Labwork> {

    @Override
    public int compare(Labwork o1, Labwork o2) {
        int o1Length = 0;
        int o2Length = 0;
        if (o1.getDifficulty() != null) {
            o1Length +=1;
        }
        if (o2.getDifficulty() != null) {
            o2Length += 1;
        }
        if (o1.getAuthor() != null) {
            o1Length += 1;
            if (o1.getAuthor().getBirthday() != null) {
                o1Length += 1;
            }
            if (o1.getAuthor().getHairColor() != null) {
                o1Length += 1;
            }
            if (o1.getAuthor().getLocation() != null) {
                o1Length += 1;
                if (!o1.getAuthor().getLocation().getName().equals("")) {
                    o1Length +=1;
                }
            }
        }

        if (o2.getAuthor() != null) {
            o2Length += 1;
            if (o2.getAuthor().getBirthday() != null) {
                o2Length += 1;
            }
            if (o2.getAuthor().getHairColor() != null) {
                o2Length += 1;
            }
            if (o2.getAuthor().getLocation() != null) {
                o2Length += 1;
                if (!o2.getAuthor().getLocation().getName().equals("")) {
                    o2Length +=1;
                }
            }
        }
        long com = o1Length - o2Length;

        if (com == 0) {
            com = o1.getId() - o2.getId();
        }
        if (com > 0) {
            return 1;
        } else {
            if (com < 0) {
                return -1;
            } else return 0;
        }
    }
}
