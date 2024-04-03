import java.awt.*;
import javax.swing.*;
import java.util.Scanner;
import java.util.ArrayList;

public class QuiltingBee extends JFrame {

  private static int frameSize = 550;
  private static int quiltSize = 450;
  private static ArrayList<Square> squares = new ArrayList<Square>();
  private static int baseSize;
  private static double accScale = 0.0;

  /** Constructor for QuiltingBee */
  public QuiltingBee() {
    baseSize = (int)((double)quiltSize/accScale);
    setSize(frameSize, frameSize);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setVisible(true);
  }

  public void paint(Graphics g) {
    // paints recursively
    drawSquare(g, frameSize/2, frameSize/2, 0);
  }

  /**
   * Method to draw square using the inputs g, x, y, and i
   * @param g
   * @param x
   * @param y
   * @param i
   */
  public static void drawSquare(Graphics g, int x, int y, int i) {
    if (i == squares.size()) {
      return;
    } else {
      squares.get(i).size = (int)(squares.get(i).scale * baseSize);
      squares.get(i).xCenter = x;
      squares.get(i).yCenter = y;
      int[] corners = Square.getCorners(squares.get(i));
      g.setColor(squares.get(i).rgb);
      g.fillRect(x - squares.get(i).size/2, y - squares.get(i).size/2, squares.get(i).size, squares.get(i).size);
      drawSquare(g, corners[0], corners[1], i + 1);
      drawSquare(g, corners[2], corners[3], i + 1);
      drawSquare(g, corners[4], corners[5], i + 1);
      drawSquare(g, corners[6], corners[7], i + 1);
    }
  }

  private static class Square extends JPanel {

    public double scale;
    public int size;
    public Color rgb;
    public int xCenter;
    public int yCenter;

    /**
     * Constructor for Square object
     * @param scale
     * @param r
     * @param g
     * @param b
     */
    public Square(double scale, int r, int g, int b) {
      this.scale = scale;
      this.rgb = new Color(r, g, b);
    }

    // Points that next square layer will be centered on
    public static int[] getCorners(Square s) {
      int[] corners = new int[8];

      // top left corner
      corners[0] = s.xCenter - s.size/2;
      corners[1] = s.yCenter + s.size/2;
      // top right corner
      corners[2] = s.xCenter + s.size/2;
      corners[3] = s.yCenter + s.size/2;
      // bottom right corner
      corners[4] = s.xCenter + s.size/2;
      corners[5] = s.yCenter - s.size/2;
      // bottom left corner
      corners[6] = s.xCenter - s.size/2;
      corners[7] = s.yCenter - s.size/2;

      return corners;

    }

  }

  public static void main(String[] args) {

    Scanner scan = new Scanner(System.in);

    while (scan.hasNextLine()) {
      String[] params = scan.nextLine().split(" ");
      double scale = Double.parseDouble(params[0]);
      accScale += scale;

      int[] color = new int[3];
      for (int i = 0; i < color.length; i++) {
        color[i] = Integer.parseInt(params[i + 1]);
      }

      Square layer = new Square(scale, color[0], color[1], color[2]);
      squares.add(layer);
    }

    QuiltingBee quilt = new QuiltingBee();
    scan.close();

  }
}