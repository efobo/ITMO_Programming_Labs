package GUI.CommandsPanels;



import GUI.ColorGetter;
import GUI.FrameKeeper;
import data.Labwork;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TreeSet;

public class ShowPanel extends JPanel implements ActionListener {
    private TreeSet<Labwork> labworks;
    private int [] w = {-350, -300, -250, -200, -150, -100, -50, 0, 50, 100, 150, 200, 250, 300, 350};
    private int [] h = {-350, -300, -250, -200, -150, -100, -50, 50, 100, 150, 200, 250, 300, 350};
    private int width = 20;
    private int height = 20;
    private int radius;
    private Timer timer = new Timer(1, this);

    public ShowPanel (TreeSet<Labwork> labworks, int radius) {
        this.labworks = labworks;
        this.radius = radius;
        this.setBackground(Color.BLACK);
        timer.start();
    }

    @Override
    public void paintComponent (Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 800, 800);
        if (radius > 0) {
            if (radius < 1600) {
                g.setColor(Color.ORANGE);
                g.fillOval(400 - radius / 2, 400 - radius / 2, radius, radius);
            }

            if (radius > 300 && radius < 2500) {
                g.setColor(Color.CYAN);
                g.fillOval(400 - radius / 2, 400 - radius / 2, radius-200, radius-200);
            }
            if (radius > 600 && radius < 2700) {
                g.setColor(Color.ORANGE);
                g.fillOval(400 - radius / 2, 400 - radius / 2, radius-500, radius-500);
            }


        }
        g.setColor(Color.WHITE);
        g.drawLine(400, 0, 400, 800);
        g.drawLine(0, 400, 800, 400);
        g.setFont(g.getFont().deriveFont(g.getFont().getSize()*1.3F));
        g.setColor(Color.BLUE);
        for (int i = 0; i < 14; i++) {
            g.fillOval((int)h[i]+400, 400, 5, 5 );
            g.drawString(""+h[i], (int)h[i]+375, 400);
        }

        for (int i = 0; i < 15; i++) {
            g.fillOval( 400, (int)w[i]+400,5, 5 );
            g.drawString(""+(-w[i]), 400, (int)w[i]+400);
        }


        for (Labwork lb: labworks) {
            g.setColor(ColorGetter.getColorFromString(lb.getUser().getUsername()));
            g.fillRect((int)(lb.getCoordinates().getX()+400 - width/2), -(int)(lb.getCoordinates().getY() -400 + height/2), width, height);

        }


    }


    @Override
    public void actionPerformed(ActionEvent e) {
        try {
        if (!FrameKeeper.getShowLabworks().equals(labworks)) {

            if (radius > 0) {
                radius = radius + 5;
                repaint();
            }
            if (radius > 2700) {
                radius = 0;
                repaint();
            }
        }
        } catch (NullPointerException exp) {}
            FrameKeeper.setShowLabworks(labworks);

    }


}
