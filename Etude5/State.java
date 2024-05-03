public class State {
  FerrersBoard board;
  int moves;
  State previousState;

  public State(FerrersBoard board, int moves, State previousState) {
    this.board = board;
    this.moves = moves;
    this.previousState = previousState;
  }
}
