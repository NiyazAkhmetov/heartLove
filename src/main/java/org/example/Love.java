package org.example;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.time.Duration;
import java.time.Instant;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Love {

    public Love() {
        JFrame jf = new JFrame("просто обычное сердце с текстом внутри");
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel jp = new MyJPanel();
        jf.add(jp);
        jf.pack();
        jf.setResizable(false);
        jf.setLocationRelativeTo(null);
        jf.setVisible(true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(Love::new);
    }

    class MyJPanel extends JPanel {

        private static final long serialVersionUID = 1L;

        private HeartShape heartShape = new HeartShape();

        private Instant anchorPoint;
        private Duration playDuration = Duration.ofSeconds(1);
        private double scale = 1;

        private double lowerRange = 0.75;
        private double upperRange = 1.25;

        public MyJPanel() {
            super();
            setPreferredSize(new Dimension(800, 600));
            setBackground(new Color(200, 200, 255));

            Timer timer = new Timer(5, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (anchorPoint == null) {
                        anchorPoint = Instant.now();
                    }
                    Duration playTime = Duration.between(anchorPoint, Instant.now());
                    double progress = (double) playTime.toMillis() / playDuration.toMillis();
                    if (progress >= 1) {
                        anchorPoint = null;
                        progress = 1;
                    }

                    if (progress > 0.5) {
                        progress = 1.0 - progress;
                    }

                    scale = ((upperRange - lowerRange) * progress) + lowerRange;
                    repaint();
                }
            });
            timer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();

            AffineTransform gat = new AffineTransform();
            gat.scale(1.0, -1.0);
            gat.translate(getWidth() / 2.0, -getHeight() / 2.0);
            g2d.transform(gat);

            Shape shape = heartShape.createTransformedShape(AffineTransform.getScaleInstance(scale, scale));

            g2d.setPaint(Color.PINK);
            g2d.fill(shape);
            g2d.setStroke(new BasicStroke(5.0f));
            g2d.setPaint(Color.BLACK);
            g2d.draw(shape);

            drawTextInsideHeart(g2d, scale);

            g2d.dispose();
        }

        private void drawTextInsideHeart(Graphics2D g2d, double scale) {
            String name = "LOVE";
            Font font = new Font("Serif", Font.BOLD, 40);

            AffineTransform oldTransform = g2d.getTransform();

            AffineTransform textTransform = new AffineTransform(oldTransform);

            textTransform.scale(scale, scale);

            textTransform.scale(1.0, -1.0);

            g2d.setTransform(textTransform);
            g2d.setFont(font);

            int textWidth = g2d.getFontMetrics().stringWidth(name);
            int textHeight = g2d.getFontMetrics().getHeight();

            double textX = -textWidth / 2.0;
            double textY = textHeight / 4.0;

            g2d.setPaint(Color.WHITE);
            g2d.drawString(name, (float)textX, (float)textY);

            g2d.setTransform(oldTransform);
        }
    }

    public class HeartShape extends Path2D.Double {

        public HeartShape() {
            moveTo(0.0, -150.0);
            curveTo(-200.0, -25.0, -200.0, 225.0, 0, 100.0);
            moveTo(0.0, -150.0);
            curveTo(200.0, -25.0, 205.0, 235.0, 0, 100.0);
        }
    }
}