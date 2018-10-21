import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

public class Block extends Rectangle {

    public Block(int x, int y, int w, int h, Color color) {
        super(w, h);

        this.setTranslateX(x);
        this.setTranslateY(y);
        this.setFill(color);
    }
}
