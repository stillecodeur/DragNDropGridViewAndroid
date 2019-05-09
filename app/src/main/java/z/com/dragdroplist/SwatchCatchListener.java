package z.com.dragdroplist;

import android.graphics.Bitmap;
import android.view.MotionEvent;

public interface SwatchCatchListener {
    void onCatchSwatch(Bitmap bitmap, SwatchBean swatchBean, int widthOffset, int heightOffset);

    void onDragSwatch(MotionEvent e);

    void onDropSwatch(MotionEvent e);
}
