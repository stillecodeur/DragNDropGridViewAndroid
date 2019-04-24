package z.com.dragdroplist;

public class SwatchBean
{
    private int color;
    private String name;

    public SwatchBean() {
    }

    public SwatchBean(int color, String name) {
        this.color = color;
        this.name = name;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
