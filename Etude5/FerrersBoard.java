import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;

public class FerrersBoard {
  private List<Integer> board; // Represents the rows of the board

  // Constructor
  public FerrersBoard(List<Integer> board) {
    this.board = board;
  }

  // Get a copy of the board
  public ArrayList<Integer> getBoard() {
    return new ArrayList<>(board);
  }

  /**
   * Makes a move on the Ferrers board.
   * A move takes a column of the board, removes it, and replaces it as a row in
   * the board of the same length. It must be placed in the correct position to
   * maintain the
   * Ferrers board
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

  public PathResult findShortestPath(FerrersBoard initialBoard, FerrersBoard targetBoard) {
    Queue<State> queue = new LinkedList<>();
    Set<List<Integer>> visited = new HashSet<>();

    queue.offer(new State(initialBoard, 0, null)); // Initial state has no previous state
    visited.add(initialBoard.getBoard());

    while (!queue.isEmpty()) {
      State currentState = queue.poll();

      if (currentState.board.getBoard().equals(targetBoard.getBoard())) {
        // Reconstruct the path
        List<FerrersBoard> path = new ArrayList<>();
        State state = currentState;
        while (state != null) {
          path.add(state.board);
          state = state.previousState;
        }
        Collections.reverse(path);
        return new PathResult(path, currentState.moves);
      }

      for (FerrersBoard neighbour : generateNeighbours(currentState.board)) {
        if (!visited.contains(neighbour.getBoard())) {
          queue.offer(new State(neighbour, currentState.moves + 1, currentState));
          visited.add(neighbour.getBoard());
        }
      }
    }

    return null;
  }

  // Generates all valid neighbors of a board
  private List<FerrersBoard> generateNeighbours(FerrersBoard board) {
    List<FerrersBoard> neighbours = new ArrayList<>();
    for (int column = 0; column < board.getBoard().size(); column++) {
      FerrersBoard neighbour = new FerrersBoard(new ArrayList<>(board.getBoard())); // Create a copy
      neighbour.makeMove(column);
      neighbours.add(neighbour);
    }
    return neighbours;
  }
}