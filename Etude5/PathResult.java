import java.util.List;

public class PathResult {
    List<FerrersBoard> path;
    int moves;

    /**
     * Constructor for PathResult Object
     * 
     * @param path
     * @param moves
     */
    public PathResult(List<FerrersBoard> path, int moves) {
      this.path = path;
      this.moves = moves;
    }
}
