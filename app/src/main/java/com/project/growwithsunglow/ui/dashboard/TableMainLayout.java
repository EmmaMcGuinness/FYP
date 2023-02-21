package com.project.growwithsunglow.ui.dashboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.project.growwithsunglow.BlockModel;

import java.util.ArrayList;
import java.util.List;

public class TableMainLayout extends RelativeLayout {
    public final String TAG = "TableMainLayout.java";

    // set the header titles
    String headers[] = {
            "Sunglow Nurseries",
            "Block 1",
            "Block 2",
            "Block 4",
            "Block 6",
            "Block 7",
            "Block 9",
            "Block 10",
            "Block 14"
    };
    String columns[] = {
            "Planted: ",
            "56 Days After: ",
            "3 days of 15degrees: ",
            "Predicted Flower: ",
            "Actual Flower: ",
            "GDH Requied: "
    };

    TableLayout tableA;
    TableLayout tableB;
    TableLayout tableC;
    TableLayout tableD;

    HorizontalScrollView horizontalScrollViewB;
    HorizontalScrollView horizontalScrollViewD;

    ScrollView scrollViewC;
    ScrollView scrollViewD;

    Context context;

    List<SampleObject> sampleObjects = this.sampleObjects();
    List<BlockModel> blocks = this.blocks();
    List<Titles> titles = this.titles();

    int headerCellsWidth[] = new int[headers.length];

    public TableMainLayout(Context context) {

        super(context);

        this.context = context;

        // initialize the main components (TableLayouts, HorizontalScrollView, ScrollView)
        this.initComponents();
        this.setComponentsId();
        this.setScrollViewAndHorizontalScrollViewTag();


        // no need to assemble component A, since it is just a table
        this.horizontalScrollViewB.addView(this.tableB);

        this.scrollViewC.addView(this.tableC);

        this.scrollViewD.addView(this.horizontalScrollViewD);
        this.horizontalScrollViewD.addView(this.tableD);

        // add the components to be part of the main layout
        this.addComponentToMainLayout();
        this.setBackgroundColor(Color.RED);


        // add some table rows
        this.addTableRowToTableA();
        this. addTableRowToTableB();

        this.resizeHeaderHeight();

        this.getTableRowHeaderCellWidth();

        this.generateTableC();

        this.generateTableD();

        this.resizeBodyTableRowHeight();
    }
    List<BlockModel> blocks(){
        List<BlockModel> blocks = new ArrayList<BlockModel>();

        return blocks;
    }

    List<Titles> titles(){
        List<Titles> titles = new ArrayList<Titles>();

        for(int x = 1; x<=1; x++){
            Titles title = new Titles(
                    "Planted: ",
                    "56 Days After: ",
                    "3 days of 15degrees: ",
                    "Predicted Flower: ",
                    "Actual Flower: ",
                    "GDH Requied: "
            );
            titles.add(title);
        }
        return titles;
    }

    // this is just the sample data
    List<SampleObject> sampleObjects(){

        List<SampleObject> sampleObjects = new ArrayList<SampleObject>();

        for(int x=1; x<=5; x++){

            SampleObject sampleObject = new SampleObject(
                    "Col 1, Row " + x,
                    "Col 2, Row " + x,
                    "Col 3, Row " + x,
                    "Col 4, Row " + x,
                    "Col 5, Row " + x,
                    "Col 6, Row " + x,
                    "Col 7, Row " + x,
                    "Col 8, Row " + x,
                    "Col 9, Row " + x
            );

            sampleObjects.add(sampleObject);
        }

        return sampleObjects;

    }

    // initalized components
    private void initComponents(){

        this.tableA = new TableLayout(this.context);
        this.tableB = new TableLayout(this.context);
        this.tableC = new TableLayout(this.context);
        this.tableD = new TableLayout(this.context);

        this.horizontalScrollViewB = new MyHorizontalScrollView(this.context);
        this.horizontalScrollViewD = new MyHorizontalScrollView(this.context);

        this.scrollViewC = new MyScrollView(this.context);
        this.scrollViewD = new MyScrollView(this.context);

        this.tableA.setBackgroundColor(Color.GREEN);
        this.horizontalScrollViewB.setBackgroundColor(Color.LTGRAY);

    }

    // set essential component IDs
    @SuppressLint("ResourceType")
    private void setComponentsId(){
        this.tableA.setId(1);
        this.horizontalScrollViewB.setId(2);
        this.scrollViewC.setId(3);
        this.scrollViewD.setId(4);
    }

    // set tags for some horizontal and vertical scroll view
    private void setScrollViewAndHorizontalScrollViewTag(){

        this.horizontalScrollViewB.setTag("horizontal scroll view b");
        this.horizontalScrollViewD.setTag("horizontal scroll view d");

        this.scrollViewC.setTag("scroll view c");
        this.scrollViewD.setTag("scroll view d");
    }

    // we add the components here in our TableMainLayout
    private void addComponentToMainLayout(){

        // RelativeLayout params were very useful here
        // the addRule method is the key to arrange the components properly
        LayoutParams componentB_Params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        componentB_Params.addRule(RelativeLayout.RIGHT_OF, this.tableA.getId());

        LayoutParams componentC_Params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        componentC_Params.addRule(RelativeLayout.BELOW, this.tableA.getId());

        LayoutParams componentD_Params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        componentD_Params.addRule(RelativeLayout.RIGHT_OF, this.scrollViewC.getId());
        componentD_Params.addRule(RelativeLayout.BELOW, this.horizontalScrollViewB.getId());

        // 'this' is a relative layout,
        // we extend this table layout as relative layout as seen during the creation of this class
        this.addView(this.tableA);
        this.addView(this.horizontalScrollViewB, componentB_Params);
        this.addView(this.scrollViewC, componentC_Params);
        this.addView(this.scrollViewD, componentD_Params);

    }



    private void addTableRowToTableA(){
        this.tableA.addView(this.componentATableRow());
    }

    private void addTableRowToTableB(){
        this.tableB.addView(this.componentBTableRow());
    }

    // generate table row of table A
    TableRow componentATableRow(){

        TableRow componentATableRow = new TableRow(this.context);
        TextView textView = this.headerTextView(this.headers[0]);
        componentATableRow.addView(textView);

        return componentATableRow;
    }

    // generate table row of table B
    TableRow componentBTableRow(){

        TableRow componentBTableRow = new TableRow(this.context);
        int headerFieldCount = this.headers.length;

        TableRow.LayoutParams params = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
        params.setMargins(4, 0, 0, 2);

        for(int x=0; x<(headerFieldCount-1); x++){
            TextView textView = this.headerTextView(this.headers[x+1]);
            textView.setLayoutParams(params);
            componentBTableRow.addView(textView);
        }

        return componentBTableRow;
    }

    private void generateTableC(){
        for(int x=0; x<this.headerCellsWidth.length; x++){
            Log.v("TableMainLayout.java", this.headerCellsWidth[x]+"");
        }
        for(Titles title : this.titles){
            TableRow tableRowForTableC1 = this.tableRowForTableC1(title);
            TableRow tableRowForTableC2 = this.tableRowForTableC2(title);
            TableRow tableRowForTableC3 = this.tableRowForTableC3(title);
            TableRow tableRowForTableC4 = this.tableRowForTableC4(title);
            TableRow tableRowForTableC5 = this.tableRowForTableC5(title);

            tableRowForTableC1.setBackgroundColor(Color.LTGRAY);
            this.tableC.addView(tableRowForTableC1);
            this.tableC.addView(tableRowForTableC2);
            this.tableC.addView(tableRowForTableC3);
            this.tableC.addView(tableRowForTableC4);
            this.tableC.addView(tableRowForTableC5);
        }
    }

    // generate table row of table C and table D
    private void generateTableD(){

        // just seeing some header cell width
        for(int x=0; x<this.headerCellsWidth.length; x++){
            Log.v("TableMainLayout.java", this.headerCellsWidth[x]+"");
        }

        for(SampleObject sampleObject : this.sampleObjects){

            //TableRow tableRowForTableC = this.tableRowForTableC(sampleObject);
            TableRow taleRowForTableD = this.taleRowForTableD(sampleObject);

            //tableRowForTableC.setBackgroundColor(Color.LTGRAY);
            taleRowForTableD.setBackgroundColor(Color.LTGRAY);

            //this.tableC.addView(tableRowForTableC);
            this.tableD.addView(taleRowForTableD);

        }
    }

    // a TableRow for table C
    TableRow tableRowForTableC1(Titles title){
        TableRow tableRowForTableC1 = new TableRow(this.context);

        String info[] = {
                title.planted,
                title.daysAfter,
                title.threeDays,
                title.predicted,
                title.actual
        };

        for(int x=0 ; x<1; x++) {
            TableRow.LayoutParams params = new TableRow.LayoutParams(this.headerCellsWidth[0], LayoutParams.MATCH_PARENT);
            params.setMargins(0, 2, 0, 0);


            TextView textView = this.bodyTextView(this.columns[x]);
            tableRowForTableC1.addView(textView, params);
        }
        return tableRowForTableC1;
    }

    TableRow tableRowForTableC2(Titles title){
        TableRow tableRowForTableC2 = new TableRow(this.context);

        String info[] = {
                title.planted,
                title.daysAfter,
                title.threeDays,
                title.predicted,
                title.actual
        };

        for(int x=0 ; x<1; x++) {
            TableRow.LayoutParams params = new TableRow.LayoutParams(this.headerCellsWidth[0], LayoutParams.MATCH_PARENT);
            params.setMargins(0, 2, 0, 0);


            TextView textView = this.bodyTextView(info[x+1]);
            tableRowForTableC2.addView(textView, params);
        }
        return tableRowForTableC2;
    }

    TableRow tableRowForTableC3(Titles title){
        TableRow tableRowForTableC3 = new TableRow(this.context);

        String info[] = {
                title.planted,
                title.daysAfter,
                title.threeDays,
                title.predicted,
                title.actual
        };

        for(int x=0 ; x<1; x++) {
            TableRow.LayoutParams params = new TableRow.LayoutParams(this.headerCellsWidth[0], LayoutParams.MATCH_PARENT);
            params.setMargins(0, 2, 0, 0);


            TextView textView = this.bodyTextView(info[x+2]);
            tableRowForTableC3.addView(textView, params);
        }
        return tableRowForTableC3;
    }

    TableRow tableRowForTableC4(Titles title){
        TableRow tableRowForTableC4 = new TableRow(this.context);

        String info[] = {
                title.planted,
                title.daysAfter,
                title.threeDays,
                title.predicted,
                title.actual
        };

        for(int x=0 ; x<1; x++) {
            TableRow.LayoutParams params = new TableRow.LayoutParams(this.headerCellsWidth[0], LayoutParams.MATCH_PARENT);
            params.setMargins(0, 2, 0, 0);


            TextView textView = this.bodyTextView(info[x+3]);
            tableRowForTableC4.addView(textView, params);
        }
        return tableRowForTableC4;
    }
    TableRow tableRowForTableC5(Titles title){
        TableRow tableRowForTableC5 = new TableRow(this.context);

        String info[] = {
                title.planted,
                title.daysAfter,
                title.threeDays,
                title.predicted,
                title.actual
        };

        for(int x=0 ; x<1; x++) {
            TableRow.LayoutParams params = new TableRow.LayoutParams(this.headerCellsWidth[0], LayoutParams.MATCH_PARENT);
            params.setMargins(0, 2, 0, 0);


            TextView textView = this.bodyTextView(info[x+4]);
            tableRowForTableC5.addView(textView, params);
        }
        return tableRowForTableC5;
    }

    TableRow taleRowForTableD(SampleObject sampleObject){

        TableRow taleRowForTableD = new TableRow(this.context);

        int loopCount = ((TableRow)this.tableB.getChildAt(0)).getChildCount();
        String info[] = {
                sampleObject.header2,
                sampleObject.header3,
                sampleObject.header4,
                sampleObject.header5,
                sampleObject.header6,
                sampleObject.header7,
                sampleObject.header8,
                sampleObject.header9
        };

        for(int x=0 ; x<loopCount; x++){
            TableRow.LayoutParams params = new TableRow.LayoutParams( headerCellsWidth[x+1],LayoutParams.MATCH_PARENT);
            params.setMargins(4, 4, 0, 0);

            TextView textViewB = this.bodyTextView(info[x]);
            taleRowForTableD.addView(textViewB,params);
        }

        return taleRowForTableD;

    }

    // table cell standard TextView
    TextView bodyTextView(String label){

        TextView bodyTextView = new TextView(this.context);
        bodyTextView.setBackgroundColor(Color.WHITE);
        bodyTextView.setText(label);
        bodyTextView.setGravity(Gravity.CENTER);
        bodyTextView.setPadding(5, 5, 5, 5);

        return bodyTextView;
    }

    // header standard Textview
    TextView headerTextView(String label){

        TextView headerTextView = new TextView(this.context);
        headerTextView.setBackgroundColor(Color.RED);
        headerTextView.setText(label);
        headerTextView.setTextColor(Color.WHITE);
        headerTextView.setGravity(Gravity.CENTER);
        headerTextView.setPadding(5, 5, 5, 5);

        return headerTextView;
    }

    // resizing TableRow height starts here
    void resizeHeaderHeight() {

        TableRow productNameHeaderTableRow = (TableRow) this.tableA.getChildAt(0);
        TableRow productInfoTableRow = (TableRow)  this.tableB.getChildAt(0);

        int rowAHeight = this.viewHeight(productNameHeaderTableRow);
        int rowBHeight = this.viewHeight(productInfoTableRow);

        TableRow tableRow = rowAHeight < rowBHeight ? productNameHeaderTableRow : productInfoTableRow;
        int finalHeight = rowAHeight > rowBHeight ? rowAHeight : rowBHeight;

        this.matchLayoutHeight(tableRow, finalHeight);
    }

    void getTableRowHeaderCellWidth(){

        int tableAChildCount = ((TableRow)this.tableA.getChildAt(0)).getChildCount();
        int tableBChildCount = ((TableRow)this.tableB.getChildAt(0)).getChildCount();;

        for(int x=0; x<(tableAChildCount+tableBChildCount); x++){

            if(x==0){
                this.headerCellsWidth[x] = this.viewWidth(((TableRow)this.tableA.getChildAt(0)).getChildAt(x));
            }else{
                this.headerCellsWidth[x] = this.viewWidth(((TableRow)this.tableB.getChildAt(0)).getChildAt(x-1));
            }

        }
    }

    // resize body table row height
    void resizeBodyTableRowHeight(){

        int tableC_ChildCount = this.tableC.getChildCount();

        for(int x=0; x<tableC_ChildCount; x++){

            TableRow productNameHeaderTableRow = (TableRow) this.tableC.getChildAt(x);
            TableRow productInfoTableRow = (TableRow)  this.tableD.getChildAt(x);

            int rowAHeight = this.viewHeight(productNameHeaderTableRow);
            int rowBHeight = this.viewHeight(productInfoTableRow);

            TableRow tableRow = rowAHeight < rowBHeight ? productNameHeaderTableRow : productInfoTableRow;
            int finalHeight = rowAHeight > rowBHeight ? rowAHeight : rowBHeight;

            this.matchLayoutHeight(tableRow, finalHeight);
        }

    }

    // match all height in a table row
    // to make a standard TableRow height
    private void matchLayoutHeight(TableRow tableRow, int height) {

        int tableRowChildCount = tableRow.getChildCount();

        // if a TableRow has only 1 child
        if(tableRow.getChildCount()==1){

            View view = tableRow.getChildAt(0);
            TableRow.LayoutParams params = (TableRow.LayoutParams) view.getLayoutParams();
            params.height = height - (params.bottomMargin + params.topMargin);

            return ;
        }

        // if a TableRow has more than 1 child
        for (int x = 0; x < tableRowChildCount; x++) {

            View view = tableRow.getChildAt(x);

            TableRow.LayoutParams params = (TableRow.LayoutParams) view.getLayoutParams();

            if (!isTheHeighestLayout(tableRow, x)) {
                params.height = height - (params.bottomMargin + params.topMargin);
                return;
            }
        }

    }

    // check if the view has the highest height in a TableRow
    private boolean isTheHeighestLayout(TableRow tableRow, int layoutPosition) {

        int tableRowChildCount = tableRow.getChildCount();
        int heighestViewPosition = -1;
        int viewHeight = 0;

        for (int x = 0; x < tableRowChildCount; x++) {
            View view = tableRow.getChildAt(x);
            int height = this.viewHeight(view);

            if (viewHeight < height) {
                heighestViewPosition = x;
                viewHeight = height;
            }
        }

        return heighestViewPosition == layoutPosition;
    }

    // read a view's height
    private int viewHeight(View view) {
        view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        return view.getMeasuredHeight();
    }

    // read a view's width
    private int viewWidth(View view) {
        view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        return view.getMeasuredWidth();
    }

    // horizontal scroll view custom class
    class MyHorizontalScrollView extends HorizontalScrollView{

        public MyHorizontalScrollView(Context context) {
            super(context);
        }

        @Override
        protected void onScrollChanged(int l, int t, int oldl, int oldt) {
            String tag = (String) this.getTag();

            if(tag.equalsIgnoreCase("horizontal scroll view b")){
                horizontalScrollViewD.scrollTo(l, 0);
            }else{
                horizontalScrollViewB.scrollTo(l, 0);
            }
        }

    }

    // scroll view custom class
    class MyScrollView extends ScrollView{

        public MyScrollView(Context context) {
            super(context);
        }

        @Override
        protected void onScrollChanged(int l, int t, int oldl, int oldt) {

            String tag = (String) this.getTag();

            if(tag.equalsIgnoreCase("scroll view c")){
                scrollViewD.scrollTo(0, t);
            }else{
                scrollViewC.scrollTo(0,t);
            }
        }
    }
}