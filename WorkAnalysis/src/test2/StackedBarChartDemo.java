package test2;
 
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
 
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
 
public class StackedBarChartDemo extends ApplicationFrame {
 
 private static final long serialVersionUID = -7166735925893038359L;
 
 public static void main(final String[] args) {
  // Call the Constructor with the title of the Chart
  final StackedBarChartDemo demo = new StackedBarChartDemo(
    "Godziny pracy");
  // The window is drawn and shown to the User using the below 3 
  //lines.
  demo.pack();
  RefineryUtilities.centerFrameOnScreen(demo);
  demo.setVisible(true);
 }
 
 /**
  * This constructor creates some dummy ChartObjects first with 
  * some sample read time, write time and timestamps Then it 
  * invokes createDataset method to get a CategoryDataset 
  * object. Then it invokes the createChart method that uses 
  * the above dataset. Later, the dimensions of the window drawn
  * etc. are set
  * 
  * @param title
  */
 public StackedBarChartDemo(String title) {
  super(title);
 
  try {
   // Create sample ChartObjects with read time, write time 
   //and TimeStamp as constructor parameters
   ChartObject chartObject1 = new ChartObject(0.123, 0.123,
     "TimeStamp1");
   ChartObject chartObject2 = new ChartObject(0.234, 0.234,
     "TimeStamp2");
   ChartObject chartObject3 = new ChartObject(0.345, 0.345,
     "TimeStamp3");
   ChartObject chartObject4 = new ChartObject(0.456, 0.456,
     "TimeStamp4");
   List<ChartObject> chartObjectList = 
    new ArrayList<ChartObject>();
   chartObjectList.add(chartObject1);
   chartObjectList.add(chartObject2);
   chartObjectList.add(chartObject3);
   chartObjectList.add(chartObject4);
 
   final CategoryDataset dataset = 
    createDataset(chartObjectList);
   final JFreeChart chart = createChart(dataset);
   final ChartPanel chartPanel = new ChartPanel(chart);
   chartPanel.setPreferredSize
   (new java.awt.Dimension(900, 800));
   setContentPane(chartPanel);
 
  } catch (Exception e) {
   e.printStackTrace();
   return;
  }
 }
 
 /**
  * This method takes the List of ChartObjects as a parameter 
  * and creates three objects needed by the createCategoryDataset 
  * method of the DatasetUtilities class. The createCategoryDataset 
  * method needs 3 parameters. The first and second ones are 
  * Comparable[] and the third one is a double[][]. Since we 
  * created RowKey and ColumnKey objects to implement Comparable
  * interface, we use those objects to create the first two
  * parameters. The actual read/write values are used to populate 
  * a two dimensional  double[][].
  * 
  * @param chartObjectList
  * @return
  */
 private CategoryDataset createDataset
 (List<ChartObject> chartObjectList) {
  int chartObjectListSize = chartObjectList.size();
 
  // This is the Comparable[] that is used as the first 
  //parameter to the createCategoryDataset method
  // These values show up as the legend on the X-axis
  RowKey[] operations = 
   new RowKey[2];
  operations[0] = new RowKey("Read");
  operations[1] = new RowKey("Write");
 
  // This is the Comparable[] that is used as the second
  //parameter to the createCategoryDataset method
  ColumnKey[] timeStampArray = 
   new ColumnKey[chartObjectListSize];
 
  // These two double[] are used to populate a two 
  //dimensional double[][] that is used as the
  // third parameter to the createCategoryDataset method
  double[] readTimes = new double[chartObjectListSize];
  double[] writeTimes = new double[chartObjectListSize];
 
  // In this loop, the arrays are populated
  for (int i = 0; i < chartObjectListSize; i++) {
   timeStampArray[i] = 
    new ColumnKey(chartObjectList.get(i)
     .getTimeDetails());
   readTimes[i] = chartObjectList.get(i).getReadTime();
   writeTimes[i] = chartObjectList.get(i).getWriteTime();
  }
 
  // Populate the two dimensional double[][] using the 
  //two double[] created above
  double[][] data = new double[][] {readTimes,writeTimes};
 
  // Invoke the createCategoryDataset method by passing 
  //the three required parameters
  return DatasetUtilities.createCategoryDataset
  (operations, timeStampArray, data);
 }
 
 private JFreeChart createChart(final CategoryDataset dataset)
 {
  // Use the dataset created in the above method and get the
  // JFreeChartObject.
  // The PlotOrienation can be set to HORIZONTAL or VERTICAL.
  // The tooltip is shown if the second last boolean is
  // set to true. This tooltip shows the deails when the 
  // cursor is hovered over a particular bar in the chart
  final JFreeChart chart = 
   ChartFactory.createStackedBarChart(
    "Read/Write times", "TimeStamp", 
    "Time in Seconds", dataset,
    PlotOrientation.HORIZONTAL, true, true, false);
 
  chart.setBackgroundPaint(new Color(249, 231, 236));
 
  // Set colors etc. here
  CategoryPlot plot = chart.getCategoryPlot();
  plot.getRenderer()
   .setSeriesPaint(0, new Color(128, 0, 0));
  plot.getRenderer()
  .setSeriesPaint(1, new Color(0, 0, 255));
  return chart;
 
 }
 
}