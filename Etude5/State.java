public class State {
  FerrersBoard board;
  int moves;
  State previousState;

  /**
   * Constructor for the State object
   * 
   * @param board
   * @param moves
   * @param previousState
   */
  public State(FerrersBoard board, int moves, State previousState) {
    this.board = board;
    this.moves = moves;
    this.previousState = previousState;
  }
}
