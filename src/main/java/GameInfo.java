import java.io.Serializable;

public class GameInfo implements Serializable {
    int size;
    float diff;
    int mineCount;
    long seed;

    GameInfo(int size, float diff, long seed) {
        this.size = size;
        this.diff = diff;
        this.mineCount = (int) (size * size * diff);
        this.seed = seed;
    }
}
