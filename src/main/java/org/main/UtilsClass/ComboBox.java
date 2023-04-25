package org.main.UtilsClass;

import org.main.UtilsClass.shadow.ShadowRenderer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicTextFieldUI;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.Hashtable;

public class ComboBox extends JComboBox {



    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
        createImageShadow();
        repaint();
    }




    private int round = 30;
    //private Color shadowColor = new Color(170, 170, 170);
    private Color shadowColor = new Color(25,25,112);
    private BufferedImage imageShadow;
    private final Insets shadowSize = new Insets(2, 5, 8, 5);


    public ComboBox(String[] items) {
        setUI(new TextUI());
        setOpaque(false);
        setModel(new DefaultComboBoxModel<>(items));

     /*   setRenderer(new DefaultListCellRenderer() {
            @Override
            public void paint(Graphics g) {
                setBackground(new Color(255, 255, 255));
                 setForeground(new Color(80, 80, 80));
                super.paint(g);
            }
        });*/


        setBorder(new EmptyBorder(5, 12, 10, 12));

    }
    public ComboBox() {
        setUI(new TextUI());
        setOpaque(false);


     /*   setRenderer(new DefaultListCellRenderer() {
            @Override
            public void paint(Graphics g) {
                setBackground(new Color(255, 255, 255));
                 setForeground(new Color(80, 80, 80));
                super.paint(g);
            }
        });*/


        setBorder(new EmptyBorder(5, 12, 10, 12));

    }



    private class TextUI extends BasicComboBoxUI {

        @Override
        public void paint(Graphics g, JComponent c) {
            setBackground(new Color(255, 255, 255));
            super.paint(g,c);
        }

        @Override
        protected JButton createArrowButton() {
            return new JButton() {
                @Override
                public int getWidth() {
                    return 0;
                }
            };
        }






    }

    class MyListCellRenderer extends DefaultListCellRenderer {

        public MyListCellRenderer() {
            //setOpaque(true);
        }

        public Component getListCellRendererComponent(JList jc, Object val, int idx, boolean isSelected, boolean cellHasFocus) {
            setText(val.toString());

            if (isSelected)
                setBackground(Color.BLUE);
            else
                setBackground(Color.WHITE);
            return this;
        }
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        double width = getWidth() - (shadowSize.left + shadowSize.right);
        double height = getHeight() - (shadowSize.top + shadowSize.bottom);
        double x = shadowSize.left;
        double y = shadowSize.top;
        //  Create Shadow Image
        g2.drawImage(imageShadow, 0, 0, null);
        //  Create Background Color
        g2.setColor(getBackground());
        Area area = new Area(new RoundRectangle2D.Double(x, y, width, height, round, round));
        g2.fill(area);
        g2.dispose();
        super.paintComponent(grphcs);
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        createImageShadow();
    }

    private void createImageShadow() {
        int height = getHeight();
        int width = getWidth();
        if (width > 0 && height > 0) {
            imageShadow = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = imageShadow.createGraphics();
            BufferedImage img = createShadow();
            if (img != null) {
                g2.drawImage(createShadow(), 0, 0, null);
            }
            g2.dispose();
        }
    }

    private BufferedImage createShadow() {
        int width = getWidth() - (shadowSize.left + shadowSize.right);
        int height = getHeight() - (shadowSize.top + shadowSize.bottom);
        if (width > 0 && height > 0) {
            BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = img.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.fill(new RoundRectangle2D.Double(0, 0, width, height, round, round));
            g2.dispose();
            return new ShadowRenderer(5, 0.3f, shadowColor).createShadow(img);
        } else {
            return null;
        }
    }



}
