package z.com.dragdroplist;

public interface OnItemDropListener {
    void onDropAtIndex(int pickedIndex, int droppedIndex);

    void onDropOutside(int pickedIndex);
}
