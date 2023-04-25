package org.main.UtilsClass;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;


public class MaterialTabbed extends JTabbedPane {

    public MaterialTabbed() {
        setUI(new AquaBarTabbedPaneUI());
    }

    public class AquaBarTabbedPaneUI extends BasicTabbedPaneUI {

        private  final Insets NO_INSETS = new Insets(0, 2, 0, 2);
        private ColorSet selectedColorSet;
        private ColorSet defaultColorSet;
        private ColorSet hoverColorSet;
        private boolean contentTopBorderDrawn = true;
        private Color lineColor = new Color(103, 103, 103);
        private Color dividerColor = new Color(103, 103, 103);
        private Insets contentInsets = new Insets(10, 10, 10, 10);
        private int lastRollOverTab = -1;


        public AquaBarTabbedPaneUI() {

            selectedColorSet = new ColorSet();
            selectedColorSet.topGradColor1 = new Color(103,103,103);
            selectedColorSet.topGradColor2 = new Color(103,103,103);

            selectedColorSet.bottomGradColor1 = new Color(103,103,103);
            selectedColorSet.bottomGradColor2 = new Color(103,103,103);

            defaultColorSet = new ColorSet();
            defaultColorSet.topGradColor1 = new Color(253, 253, 253);
            defaultColorSet.topGradColor2 = new Color(237, 237, 237);

            defaultColorSet.bottomGradColor1 = new Color(222, 222, 222);
            defaultColorSet.bottomGradColor2 = new Color(255, 255, 255);

            hoverColorSet = new ColorSet();
            hoverColorSet.topGradColor1 = new Color(244, 244, 244);
            hoverColorSet.topGradColor2 = new Color(223, 223, 223);

            hoverColorSet.bottomGradColor1 = new Color(211, 211, 211);
            hoverColorSet.bottomGradColor2 = new Color(235, 235, 235);

            maxTabHeight = 20;

            setContentInsets(0);
        }

        public void setContentTopBorderDrawn(boolean b) {
            contentTopBorderDrawn = b;
        }

        public void setContentInsets(Insets i) {
            contentInsets = i;
        }

        public void setContentInsets(int i) {
            contentInsets = new Insets(i, i, i, i);
        }

        public int getTabRunCount(JTabbedPane pane) {
            return 1;
        }

        protected void installDefaults() {
            super.installDefaults();

            RollOverListener l = new RollOverListener();
            tabPane.addMouseListener(l);
            tabPane.addMouseMotionListener(l);

            tabAreaInsets = NO_INSETS;
            tabInsets = new Insets(0, 0, 0, 1);
        }

        protected boolean scrollableTabLayoutEnabled() {
            return false;
        }

        protected Insets getContentBorderInsets(int tabPlacement) {
            return contentInsets;
        }

        protected int calculateTabHeight(int tabPlacement, int tabIndex,
                                         int fontHeight) {
            return 21;
        }

        protected int calculateTabWidth(int tabPlacement, int tabIndex,
                                        FontMetrics metrics) {
            int w = super.calculateTabWidth(tabPlacement, tabIndex, metrics);
            int wid = metrics.charWidth('M');
            w += wid * 2;
            return w;
        }

        protected int calculateMaxTabHeight(int tabPlacement) {
            return 21;
        }

        protected void paintTabArea(Graphics g, int tabPlacement, int selectedIndex) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setPaint(new GradientPaint(0, 0, defaultColorSet.topGradColor1, 0,
                    10, defaultColorSet.topGradColor2));
            g2d.fillRect(0, 0, tabPane.getWidth(), 10);

            g2d.setPaint(new GradientPaint(0, 10, defaultColorSet.bottomGradColor1,
                    0, 21, defaultColorSet.bottomGradColor2));
            g2d.fillRect(0, 10, tabPane.getWidth(), 11);
            super.paintTabArea(g, tabPlacement, selectedIndex);

            if (contentTopBorderDrawn) {
                g2d.setColor(lineColor);
                g2d.drawLine(0, 20, tabPane.getWidth() - 1, 20);
            }
        }

        protected void paintTabBackground(Graphics g, int tabPlacement,
                                          int tabIndex, int x, int y, int w, int h, boolean isSelected) {
            Graphics2D g2d = (Graphics2D) g;
            ColorSet colorSet;

            Rectangle rect = rects[tabIndex];

            if (isSelected) {
                colorSet = selectedColorSet;
            } else if (getRolloverTab() == tabIndex) {
                colorSet = hoverColorSet;
            } else {
                colorSet = defaultColorSet;
            }

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            int width = rect.width;
            int xpos = rect.x;
            if (tabIndex > 0) {
                width--;
                xpos++;
            }

            g2d.setPaint(new GradientPaint(xpos, 0, colorSet.topGradColor1, xpos,
                    10, colorSet.topGradColor2));
            g2d.fillRect(xpos, 0, width, 10);

            g2d.setPaint(new GradientPaint(0, 10, colorSet.bottomGradColor1, 0, 21,
                    colorSet.bottomGradColor2));
            g2d.fillRect(xpos, 10, width, 11);

            if (contentTopBorderDrawn) {
                g2d.setColor(lineColor);
                g2d.drawLine(rect.x, 20, rect.x + rect.width - 1, 20);
            }
        }

        protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex,
                                      int x, int y, int w, int h, boolean isSelected) {
            Rectangle rect = getTabBounds(tabIndex, new Rectangle(x, y, w, h));
            g.setColor(dividerColor);
            g.drawLine(rect.x + rect.width, 0, rect.x + rect.width, 20);
        }

        protected void paintContentBorderTopEdge(Graphics g, int tabPlacement,
                                                 int selectedIndex, int x, int y, int w, int h) {

        }

        protected void paintContentBorderRightEdge(Graphics g, int tabPlacement,
                                                   int selectedIndex, int x, int y, int w, int h) {
            // Do nothing
        }

        protected void paintContentBorderLeftEdge(Graphics g, int tabPlacement,
                                                  int selectedIndex, int x, int y, int w, int h) {
            // Do nothing
        }

        protected void paintContentBorderBottomEdge(Graphics g, int tabPlacement,
                                                    int selectedIndex, int x, int y, int w, int h) {
            // Do nothing
        }

        protected void paintFocusIndicator(Graphics g, int tabPlacement,
                                           Rectangle[] rects, int tabIndex, Rectangle iconRect,
                                           Rectangle textRect, boolean isSelected) {
            // Do nothing
        }

        protected int getTabLabelShiftY(int tabPlacement, int tabIndex,
                                        boolean isSelected) {
            return 0;
        }

        private class ColorSet {
            Color topGradColor1;
            Color topGradColor2;

            Color bottomGradColor1;
            Color bottomGradColor2;
        }

        private class RollOverListener implements MouseMotionListener,
                MouseListener {

            public void mouseDragged(MouseEvent e) {
            }

            public void mouseMoved(MouseEvent e) {
                checkRollOver();
            }

            public void mouseClicked(MouseEvent e) {
            }

            public void mousePressed(MouseEvent e) {
            }

            public void mouseReleased(MouseEvent e) {
            }

            public void mouseEntered(MouseEvent e) {
                checkRollOver();
            }

            public void mouseExited(MouseEvent e) {
                tabPane.repaint();
            }

            private void checkRollOver() {
                int currentRollOver = getRolloverTab();
                if (currentRollOver != lastRollOverTab) {
                    lastRollOverTab = currentRollOver;
                    Rectangle tabsRect = new Rectangle(0, 0, tabPane.getWidth(), 20);
                    tabPane.repaint(tabsRect);
                }
            }
        }
    }





}
