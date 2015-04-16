package GUI.Components;

import java.awt.*;

/**
 * AbtractClickableComponent implements standard behaviour for clickables
 */
public class AbstractClickableComponent extends Component implements Clickable
{
    private boolean clicked;
    @Override public boolean isClicked() {
        return clicked;
    }

    @Override public void setClicked(final boolean b) {
        clicked = true;
    }

    @Override public void clickAction() {
        this.clicked = true;
    }
}
