package luxmeter.puzzlefx.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Application wide used constants
 */
public final class AppConstants {
    // you are not supposed to create an instance of this class
    private AppConstants() {
    }

    public static final int MAX_WINDOW_WIDTH = 600;
    public static final int MAX_WINDOW_HEIGHT = 480;
    public static final String WINDOW_TITLE = "PuzzleFx";
    public static final String IMAGE_LOCATION = "/doge.jpg";
    public static final List<String> SUPPORTED_IMAGE_EXTENSION = Collections.unmodifiableList(Arrays.asList("*.jpg","*.jpeg","*.png","*.bmp"));
    public static final String GAME_EXTENSION = ".game";
    public static final int NUM_HORIZONTAL_SLICES = 2;
    public static final int NUM_VERTICAL_SLICES = 2;
}
