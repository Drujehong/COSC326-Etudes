import java.awt.*;
import javax.swing.*;
import java.util.Scanner;
import java.util.ArrayList;

public class QuiltingBee extends JPanel {

  private static int frameSize = 550;
  private static ArrayList<Square> squares = new ArrayList<Square>();
  private static double accScale = 0.0;
  private static int currentSquareLayer = 0;

  /** Constructor for QuiltingBee */
  public QuiltingBee() {
    JFrame frame = new JFrame("Quilting Bees");
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.getContentPane().add(this);
    frame.setPreferredSize(new Dimension(frameSize + 25, frameSize + 50));
    frame.setLocationRelativeTo(null);
    frame.pack();
    frame.setVisible(true);
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    while(currentSquareLayer < squares.size()) {
      drawSquare(g, frameSize/2, frameSize/2, 0);
      currentSquareLayer++;
    }
    currentSquareLayer = 0;
  }

  /**
   * Method to draw square using the inputs g, x, y, and i
   * @param g
   * @param x
   * @param y
   * @param i
   */
  public static void drawSquare(Graphics g, int x, int y, int i) {
    double squareSize = ((squares.get(i).scale / accScale) * frameSize);
    int convertedSquareSize = (int) squareSize;

    if(currentSquareLayer == squares.get(i).layer) {
      g.setColor(squares.get(i).rgb);
      g.fillRect(x - convertedSquareSize/2, y - convertedSquareSize/2, convertedSquareSize, convertedSquareSize);
    }
    if(squares.get(i + 1)!=null) {
      drawSquare(g, x - convertedSquareSize / 2, y - convertedSquareSize / 2, i + 1);
      drawSquare(g, x - convertedSquareSize / 2, y + convertedSquareSize / 2, i + 1);
      drawSquare(g, x + convertedSquareSize / 2, y - convertedSquareSize / 2, i + 1);
      drawSquare(g, x + convertedSquareSize / 2, y + convertedSquareSize / 2, i + 1);
    }
  }

  private static class Square {

    public double scale;
    public Color rgb;
    public int layer;

    /**
     * Constructor for Square object
     * @param scale
     * @param r
     * @param g
     * @param b
     * @param layer
     */
    public Square(double scale, int r, int g, int b, int layer) {
      this.scale = scale;
      this.rgb = new Color(r, g, b);
      this.layer = layer;
    }

  }

  public static void main(String[] args) {

    Scanner scan = new Scanner(System.in);
    int squareLayer = 0;
    while (scan.hasNextLine()) {
      String[] params = scan.nextLine().split("\\s+"); //Modified due to client request
      double scale = Double.parseDouble(params[0]);
      accScale += scale;

      int[] color = new int[3];
      for (int i = 0; i < color.length; i++) {
        color[i] = Integer.parseInt(params[i + 1]);
      }

      Square square = new Square(scale, color[0], color[1], color[2], squareLayer);
      squares.add(square);
      squareLayer++;
    }
    squares.add(null); // to make sure the loop in recursive eventually stops

    QuiltingBee quilt = new QuiltingBee();
    scan.close();

  }
}