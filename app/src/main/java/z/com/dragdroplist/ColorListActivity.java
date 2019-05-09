package z.com.dragdroplist;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class ColorListActivity extends AppCompatActivity {


    private Context mContext;
    private ZccLibraryView zccLibraryView;
    private DragRecycleView dragRecycleView;
    private List<SwatchBean> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_list2);

        mContext = ColorListActivity.this;

        zccLibraryView = findViewById(R.id.zcc_grid_view);
        dragRecycleView = findViewById(R.id.dragView);



        //WorkSpace

        list = new ArrayList<>();
        list.add(new SwatchBean(Color.RED, "Red"));
        list.add(new SwatchBean(Color.BLUE, "Blue"));
        int i = 0;
        while (i != 22) {
            list.add(new SwatchBean());
            i++;
        }

        final GridAdpter gridAdpter = new GridAdpter(mContext, list, 4);
        dragRecycleView.setAdapter(gridAdpter);

        gridAdpter.setOnItemInflateListener(new GridAdpter.OnItemInflateListener() {
            @Override
            public void onInflate(int h, int w) {
                dragRecycleView.setItemHeight(h);
                dragRecycleView.setItemWidth(w);
            }
        });


        dragRecycleView.setOnItemDropListener(gridAdpter);


        //List

        List<SwatchBean> swatchBeans = new ArrayList<>();

        int color = (int) Long.parseLong("AAFFFFFF", 16);


        for (int j = 0; j < 4096; j++) {
            color = color - 10;
            swatchBeans.add(new SwatchBean(color, "ZCC-" + j));

        }

        ZccGridAdapter adapter = new ZccGridAdapter(mContext, swatchBeans);
        zccLibraryView.setGridAdapter(adapter);

        adapter.setOnItemInflateListener(new GridAdpter.OnItemInflateListener() {
            @Override
            public void onInflate(int h, int w) {
                zccLibraryView.setRowWidth(w);
                zccLibraryView.setRowHeight(h);
            }
        });

        zccLibraryView.setSwatchCatchListener(dragRecycleView);


    }
}
