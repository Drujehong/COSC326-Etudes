import java.util.List;
import java.util.Objects;
import java.util.ArrayList;

public class FerrersBoard {

  private List<Integer> board; // The Ferrers board
  private FerrersBoard parentBoard; // The parent of the Ferrers board

  public FerrersBoard(List<Integer> board) {
    // Initialize the board with a deep copy of the provided board
    this.board = new ArrayList<>(board);
  }

  /**
   * Gets the Ferrers board
   * 
   * @return
   */
  public List<Integer> getBoard() {
    return new ArrayList<>(board);
  }

  /**
   * Makes a move on the Ferrers board.
   * A move takes a column of the board, removes it, and replaces it as a row in
   * the board of the same length. It must be placed in the correct position to
   * maintain the Ferrers board
   * 
   * @param column the column to remove from the board
   * 
   */
  public void makeMove(int column) {
    // check if the column is within the bounds of the board
    if (column <= board.get(0) - 1) {
      int rowLength = 0;
      for (int i = 0; i < board.size(); i++) {
        if (board.get(i) >= column + 1) {
          rowLength++;
          if (board.get(i) == 1) {
            board.remove(i);
            i--;
            continue;
          }
          board.set(i, board.get(i) - 1);
          ;
        }
      }

      board.add(rowLength);
      // sort in reverse order to maintain Ferrers board
      board.sort((a, b) -> b - a);

    }

  }

  /**
   * Makes a reverse move on the Ferrers board.
   * A reverse move takes a row of the board, removes it, and replaces it as a
   * column in the board of the same length. It must be placed in the correct
   * position to maintain the Ferrers board
   * 
   * @param row the row to remove from the board
   * 
   */
  public void makeReverseMove(int row) {
    // check if the row is within the bounds of the board
    if (row <= board.size() - 1) {
      int rowLength = board.get(row);
      board.remove(row);
      for (int i = 0; i < rowLength; i++) {
        if (i >= board.size()) {
          board.add(1);
        } else {
          board.set(i, board.get(i) + 1);
        }
      }
      // sort in reverse order to maintain Ferrers board
      board.sort((a, b) -> b - a);
    }

  }

  /**
   * Prints the Ferrers board to the console
   * in a visual format for easier understanding
   */
  public void printVisualBoard() {
    System.out.println();
    for (int i = 0; i < board.size(); i++) {
      for (int j = 0; j < board.get(i); j++) {
        System.out.print("* ");
      }
      System.out.println();
    }
    System.out.println();
  }

  /**
   * Creates a deep copy of the Ferrers board
   * 
   * @return
   */
  public FerrersBoard copy() {
    return new FerrersBoard(board);
  }

  /**
   * Checks if the Ferrers board is equal to another object
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    FerrersBoard other = (FerrersBoard) obj;
    return Objects.equals(board, other.board);
  }

  /**
   * Generates a hash code for the Ferrers board
   */
  @Override
  public int hashCode() {
    return Objects.hash(board);
  }

  /**
   * Sets the parent of the Ferrers board
   * 
   * @param parent
   */
  public void setParent(FerrersBoard parent) {
    this.parentBoard = parent;
  }

  /**
   * Gets the parent of the Ferrers board
   * 
   * @return
   */
  public FerrersBoard getParent() {
    return parentBoard;
  }

  /**
   * Converts the Ferrers board to a string
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < board.size(); i++) {
      sb.append(board.get(i));
      if (i != board.size() - 1) {
        sb.append(" ");
      }
    }
    return sb.toString();
  }

}
