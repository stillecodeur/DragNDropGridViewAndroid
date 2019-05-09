package z.com.dragdroplist;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import org.askerov.dynamicgrid.DynamicGridView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnStartDragListener {


    private static final String TAG = MainActivity.class.getSimpleName();
    private Context context;
    private DynamicGridView gridView;
    private RecyclerView recyclerView;
    private GridAdpter gridAdpter;
    private List<SwatchBean> list;
    private int selectedPos = 0;
    private int oldPos = 0;
    private int newPos = 0;
    private ItemTouchHelper mItemTouchHelper;
    private DragRecycleView dragRecycleView;
    private int[][] itemMatrix;
    private ListDragView listDragView;
    private SwatchAdapter swatchAdapter;
    private List<SwatchBean> swatchBeans;
    private GridView mGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        context = MainActivity.this;
        dragRecycleView = findViewById(R.id.dragView);
        listDragView = findViewById(R.id.listDragView);
        mGridView=findViewById(R.id.gridView);

        list = new ArrayList<>();
        itemMatrix = new int[5][4];


        list.add(new SwatchBean(Color.RED, "Red"));
        list.add(new SwatchBean(Color.BLUE, "Blue"));
        int i = 0;
        while (i != 22) {
            list.add(new SwatchBean());
            i++;
        }


        swatchBeans = new ArrayList<>();

        for (int j = 0; j < 24; j++) {
            swatchBeans.add(new SwatchBean(Color.YELLOW, "Yellow"));
        }

        swatchAdapter = new SwatchAdapter(context, swatchBeans);
        listDragView.setAdapter(swatchAdapter);

        final GridAdpter gridAdpter = new GridAdpter(context, list, 4);
        dragRecycleView.setAdapter(gridAdpter);
        listDragView.setSwatchCatchListener(dragRecycleView);


        mGridView.setAdapter(gridAdpter);
//        mGridView.setVerticalScrollBarEnabled(true);

//        GridLayoutManager gridLayoutManager=new GridLayoutManager(context,4);
//
//        recyclerView.setLayoutManager(gridLayoutManager);
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
//                gridLayoutManager.getOrientation());
//
//        CustonDecoration custonDecoration=new CustonDecoration(16,4);
//
//        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(dragAdapter);
//        recyclerView.addItemDecoration(custonDecoration);
//        recyclerView.setAdapter(dragAdapter);
//        mItemTouchHelper = new ItemTouchHelper(callback);
//        mItemTouchHelper.attachToRecyclerView(recyclerView);
//

//
//        gridAdpter = new GridAdpter(context, list,4);
//
//
//        gridView = (DynamicGridView) findViewById(R.id.dynamic_grid);
//        gridView.setAdapter(gridAdpter);
////        gridView.startEditMode();
//
//
//
////        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
////            @Override
////            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
////                selectedPos=position;
////                gridView.startEditMode();
////                return true;
////            }
////        });
////
////        gridView.setOnDropListener(new DynamicGridView.OnDropListener() {
////            @Override
////            public void onActionDrop() {
////
////                gridView.stopEditMode();
////            }
////        });gridView.startEditMode();
//
//        gridView.startEditMode();
//        gridView.setOnDragListener(new DynamicGridView.OnDragListener() {
//            @Override
//            public void onDragStarted(int position) {
//                oldPos=position;
////                Log.d(TAG, "onDragStarted: "+position);
//
//            }
//
//            @Override
//            public void onDragPositionsChanged(int oldPosition, int newPosition) {
//                oldPos=oldPosition;
//                newPos=newPosition;
//            }
//        });
//
//        gridView.setOnDropListener(new DynamicGridView.OnDropListener() {
//            @Override
//            public void onActionDrop() {
////                newPos=pos;
//                Log.d(TAG, "onActionDrop: Old"+oldPos+"NEw"+newPos);
//                int oldItem=list.get(oldPos);
//                int newItem=list.get(newPos);
//
//
//                list.set(newPos,oldItem);
//                list.set(oldPos,newItem);
//
//                gridAdpter.notifyDataSetChanged();
//
//            }
//        });


        gridAdpter.setOnItemInflateListener(new GridAdpter.OnItemInflateListener() {
            @Override
            public void onInflate(int h, int w) {
                dragRecycleView.setItemHeight(h);
                dragRecycleView.setItemWidth(w);
            }
        });


        dragRecycleView.setOnItemDropListener(gridAdpter);


    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
}
