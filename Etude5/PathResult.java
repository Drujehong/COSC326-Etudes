import java.util.List;

public class PathResult {
    List<FerrersBoard> path;
    int moves;

    public PathResult(List<FerrersBoard> path, int moves) {
      this.path = path;
      this.moves = moves;
    }
}
