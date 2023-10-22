package tema2_4;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.List;
import tema2_4.TextFormat;


public class ToolbarDraw extends JFrame {
    private DrawingPanel drawingPanel;
    private JButton lineButton, rectangleButton, filledRectangleButton, roundedRectangleButton, filledRoundedRectangleButton;
    private JButton ovalButton, filledOvalButton, arcButton, sectorButton, freehandLineButton;
    private JButton polygonButton, filledPolygonButton, imageButton, textButton;
    private Color selectedColor = Color.BLACK;
    private String textToDraw = "";
    private BufferedImage image;
    private TextFormat textFormat;

    public ToolbarDraw() {
        setTitle("Drawing Application");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        textFormat = new TextFormat();
        drawingPanel = new DrawingPanel();
        drawingPanel.setBackground(Color.GRAY);
        add(drawingPanel, BorderLayout.CENTER);
        JPanel toolPanel = new JPanel();
        toolPanel.setLayout(new BoxLayout(toolPanel, BoxLayout.Y_AXIS));
        toolPanel.setBackground(Color.DARK_GRAY);


        lineButton = createToolButton("Line");
        rectangleButton = createToolButton("Rectangle");
        filledRectangleButton = createToolButton("Filled Rectangle");
        roundedRectangleButton = createToolButton("Rounded Rectangle");
        filledRoundedRectangleButton = createToolButton("Filled Rounded Rectangle");
        ovalButton = createToolButton("Oval");
        filledOvalButton = createToolButton("Filled Oval");
        arcButton = createToolButton("Arc");
        sectorButton = createToolButton("Sector");
        freehandLineButton = createToolButton("Freehand Line");
        polygonButton = createToolButton("Polygon");
        filledPolygonButton = createToolButton("Filled Polygon");
        imageButton = createToolButton("Image");
        textButton = createToolButton("Text");

        JButton colorButton = new JButton("Select Color");
        colorButton.setForeground(Color.WHITE);
        colorButton.setBackground(Color.DARK_GRAY); // You can choose your preferred color
        Font buttonFont = new Font("Arial", Font.BOLD, 14); // You can customize the font
        colorButton.setFont(buttonFont);
        
        colorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedColor = JColorChooser.showDialog(null, "Select Color", selectedColor);
            }
        });

      
        toolPanel.add(lineButton);
        toolPanel.add(rectangleButton);
        toolPanel.add(filledRectangleButton);
        toolPanel.add(roundedRectangleButton);
        toolPanel.add(filledRoundedRectangleButton);
        toolPanel.add(ovalButton);
        toolPanel.add(filledOvalButton);
        toolPanel.add(arcButton);
        toolPanel.add(sectorButton);
        toolPanel.add(freehandLineButton);
        toolPanel.add(polygonButton);
        toolPanel.add(filledPolygonButton);
        toolPanel.add(imageButton);
        toolPanel.add(textButton);
        toolPanel.add(colorButton);
       // toolPanel.add(textInput);

        add(toolPanel, BorderLayout.WEST );

        imageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    try {
                        image = ImageIO.read(selectedFile);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        freehandLineButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingPanel.setDrawingTool("Freehand Line");
            }
        });

        polygonButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingPanel.setDrawingTool("Polygon");
            }
        });

        filledPolygonButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingPanel.setDrawingTool("Filled Polygon");
            }
        });

        textButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingPanel.setDrawingTool("Text");
                textFormat.setVisible(true);
                
            }
        });

        setVisible(true);
    }

    private JButton createToolButton(String text) {
        JButton button = new JButton(text);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingPanel.setDrawingTool(text);
            }
        });
        
        button.setForeground(Color.WHITE);
        button.setBackground(Color.DARK_GRAY); 
        Font buttonFont = new Font("Arial", Font.BOLD, 14); 
        button.setFont(buttonFont);
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ToolbarDraw());
    }

    class DrawingPanel extends JPanel {
        private String drawingTool = "Line";
        public Point startPoint = new Point(0, 0);
        public Point endPoint = new Point(0, 0);
        private List<Point> points = new ArrayList<>();
        private List<Point> polygonVertices = new ArrayList<>();

        public DrawingPanel() {
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    startPoint = e.getPoint();
                    points.add(startPoint);
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    endPoint = e.getPoint();
                    if (drawingTool.equals("Freehand Line")) {
                        endPoint = e.getPoint();
                        repaint();
                    }
                    if (drawingTool.equals("Polygon") || drawingTool.equals("Filled Polygon")) {
                        polygonVertices.add(new Point(e.getX(), e.getY()));
                    }
                    repaint();
                }
            });

            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    if (drawingTool.equals("Freehand Line")) {
                        endPoint = e.getPoint();
                        points.add(endPoint); // Add the new point
                        repaint();
                    }
                }
            });
        }
        
        public void setDrawingTool(String tool) {
            drawingTool = tool;
            if (drawingTool.equals("Freehand Line") || drawingTool.equals("Polygon") || drawingTool.equals("Filled Polygon")) {
                startPoint = new Point(0, 0);
                endPoint = new Point(0, 0);
                polygonVertices.clear();
                points.clear();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(selectedColor);
           
            if (drawingTool.equals("Line")) {
                g.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
            } else if (drawingTool.equals("Rectangle")) {
                int x = Math.min(startPoint.x, endPoint.x);
                int y = Math.min(startPoint.y, endPoint.y);
                int width = Math.abs(startPoint.x - endPoint.x);
                int height = Math.abs(startPoint.y - endPoint.y);
                g.drawRect(x, y, width, height);
            } else if (drawingTool.equals("Filled Rectangle")) {
                int x = Math.min(startPoint.x, endPoint.x);
                int y = Math.min(startPoint.y, endPoint.y);
                int width = Math.abs(startPoint.x - endPoint.x);
                int height = Math.abs(startPoint.y - endPoint.y);
                g.fillRect(x, y, width, height);
            } else if (drawingTool.equals("Rounded Rectangle")) {
                int x = Math.min(startPoint.x, endPoint.x);
                int y = Math.min(startPoint.y, endPoint.y);
                int width = Math.abs(startPoint.x - endPoint.x);
                int height = Math.abs(startPoint.y - endPoint.y);
                int arcWidth = 20;
                int arcHeight = 20;
                g.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
            } else if (drawingTool.equals("Filled Rounded Rectangle")) {
                int x = Math.min(startPoint.x, endPoint.x);
                int y = Math.min(startPoint.y, endPoint.y);
                int width = Math.abs(startPoint.x - endPoint.x);
                int height = Math.abs(startPoint.y - endPoint.y);
                int arcWidth = 20;
                int arcHeight = 20;
                g.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
            } else if (drawingTool.equals("Oval")) {
                int x = Math.min(startPoint.x, endPoint.x);
                int y = Math.min(startPoint.y, endPoint.y);
                int width = Math.abs(startPoint.x - endPoint.x);
                int height = Math.abs(startPoint.y - endPoint.y);
                g.drawOval(x, y, width, height);
            } else if (drawingTool.equals("Filled Oval")) {
                int x = Math.min(startPoint.x, endPoint.x);
                int y = Math.min(startPoint.y, endPoint.y);
                int width = Math.abs(startPoint.x - endPoint.x);
                int height = Math.abs(startPoint.y - endPoint.y);
                g.fillOval(x, y, width, height);
            } else if (drawingTool.equals("Arc")) {
                g.drawArc(startPoint.x, startPoint.y, endPoint.x - startPoint.x, endPoint.y - startPoint.y, 45, 90);
            } else if (drawingTool.equals("Sector")) {
                g.fillArc(startPoint.x, startPoint.y, endPoint.x - startPoint.x, endPoint.y - startPoint.y, 45, 90);
            } else if (drawingTool.equals("Freehand Line")) {
                // conectez punctele cu linii
                for (int i = 1; i < points.size(); i++) {
                    Point p1 = points.get(i - 1);
                    Point p2 = points.get(i);
                    g.drawLine(p1.x, p1.y, p2.x, p2.y);
                }
            } else if (drawingTool.equals("Polygon")) {
                if (polygonVertices.size() > 1) {
                    int[] xPoints = new int[polygonVertices.size()];
                    int[] yPoints = new int[polygonVertices.size()];
                    for (int i = 0; i < polygonVertices.size(); i++) {
                        xPoints[i] = polygonVertices.get(i).x;
                        yPoints[i] = polygonVertices.get(i).y;
                    }
                    g.drawPolygon(xPoints, yPoints, polygonVertices.size());
                }
            } else if (drawingTool.equals("Filled Polygon")) {
                if (polygonVertices.size() > 1) {
                    int[] xPoints = new int[polygonVertices.size()];
                    int[] yPoints = new int[polygonVertices.size()];
                    for (int i = 0; i < polygonVertices.size(); i++) {
                        xPoints[i] = polygonVertices.get(i).x;
                        yPoints[i] = polygonVertices.get(i).y;
                    }
                    g.fillPolygon(xPoints, yPoints, polygonVertices.size());
                }
            } else if (drawingTool.equals("Image")) {
                if (image != null) {
                    g.drawImage(image, startPoint.x, startPoint.y, this);
                }
            } else if (drawingTool.equals("Text")) {
                g.setColor(selectedColor);  
                int x = startPoint.x;
                int y = startPoint.y;
                
                Font newFont = TextFormat.updateFont();
                g.setFont(newFont);
                textToDraw=TextFormat.getTextToDraw();
                g.drawString(textToDraw, x, y);
            }
            }
        }
    }

