import java.io.Serializable;

//game board information to share between server and client
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
