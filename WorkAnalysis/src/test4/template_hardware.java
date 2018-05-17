package test4;

import java.awt.Color;
import java.awt.Paint;
import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.GanttRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.time.TimePeriod;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import work_charts.RCPdatabaseConnection;

public class template_hardware extends ApplicationFrame {
	
	
    public static void main(final String[] args) {

        final template_hardware demo = new template_hardware("wykres czasu pracy");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);

    }

    public template_hardware(final String title) {

        super(title);

        final IntervalCategoryDataset dataset = createSampleDataset();

        // create the chart...
        final JFreeChart chart = ChartFactory.createGanttChart(
            "moj test dla 500",  // chart title
            "Task",              // domain axis label
            "Date/time",              // range axis label
            dataset,             // data
            false,                // include legend
            true,                // tooltips
            false                // urls
        );
        final CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(new Color(230, 230, 230));
        
        final CategoryItemRenderer renderer = plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(166, 166, 166));					// renderer, ktory koloruje glowny pasek (dla roznych serii moze byc rozny - w moim wykresie jest tylko jedna seria)
        
        //proba kolorowania poszczegolnych barow
        plot.setRenderer(new GanttRenderer() {
        	
        	//koloruje grube bary dla kazdego wyniku osobno
            @Override
            public Paint getItemPaint(int row, int col) {
            	
                if(row==0 && col ==0){
                	return new Color(255, 77, 77);
                }
                if(row==0 && col ==3){
                	return new Color(255, 77, 77);
                }
                if(row==0 && col ==2){
                	return new Color(115, 115, 115);
                }
                if(row==0 && col ==4){
                	return new Color(115, 115, 115);
                }
            	return new Color(166, 166, 166);
            }
            
            //koloruje pasek postepu (srodkowy)
            public Paint getCompletePaint(){
            	
            	return Color.green;
            }
            
        });
        
        final GanttRenderer rendererGantt = (GanttRenderer) plot.getRenderer();
        rendererGantt.setIncompletePaint(new Color(166, 166, 166));           // renderer, ktory koloruje pasek "ile brakuje do konca" (tu kolor glownego paska, domyslnie jest czerwony)
        
        BarRenderer rendererBar = (BarRenderer) plot.getRenderer();
        rendererBar.setBarPainter(new StandardBarPainter());  // bary nie maja gradientu dzieki temu (nie "blyszcza")
        
        // add the chart to a panel...
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(900, 970));
        setContentPane(chartPanel);

    }

    private IntervalCategoryDataset createSampleDataset() {

        final TaskSeries s1 = new TaskSeries("Pierwsza seria");
        
        final Task t1 = new Task(
                "KIRKALO - BRAMKA", 
                datetime(2018, Calendar.APRIL, 2, 6, 30), datetime(2018, Calendar.APRIL, 2, 15, 30)
            );
        s1.add(t1);
        
        final Task t2 = new Task(
                "500/1", 
                datetime(2018, Calendar.APRIL, 2, 6, 45), datetime(2018, Calendar.APRIL, 2, 8, 30)
            );
        t2.setPercentComplete(0.40);
        s1.add(t2);
        
        final Task t3 = new Task(
                "500/2", 
                datetime(2018, Calendar.APRIL, 2, 11, 30), datetime(2018, Calendar.APRIL, 2, 14, 30)
            );
        t3.setPercentComplete(1.20);
        s1.add(t3);
        
        // podzielone
        
        final Task t4 = new Task(
                "LIPSKI - BRAMKA", 
                datetime(2018, Calendar.APRIL, 2, 6, 0), datetime(2018, Calendar.APRIL, 2, 15, 0)
            );
        
        final Task t41 = new Task(
                "LIPSKI - BRAMKA1", 
                datetime(2018, Calendar.APRIL, 2, 6, 0), datetime(2018, Calendar.APRIL, 2, 9, 35)
            );
        
        final Task t42 = new Task(
                "LIPSKI - BRAMKA2", 
                datetime(2018, Calendar.APRIL, 2, 9, 50), datetime(2018, Calendar.APRIL, 2, 15, 0)
            );
        
        t4.addSubtask(t41);
        t4.addSubtask(t42);
        s1.add(t4);
        
        // podzielone
        
        final Task t5 = new Task(
                "500/3", 
                datetime(2018, Calendar.APRIL, 2, 6, 15), datetime(2018, Calendar.APRIL, 2, 13, 30)
            );
        
        final Task t51 = new Task(
                "500/3-1", 
                datetime(2018, Calendar.APRIL, 2, 6, 15), datetime(2018, Calendar.APRIL, 2, 7, 15)
            );
        t51.setPercentComplete(1.00);
        
        final Task t52 = new Task(
                "500/3-2", 
                datetime(2018, Calendar.APRIL, 2, 10, 30), datetime(2018, Calendar.APRIL, 2, 13, 30)
            );
        t52.setPercentComplete(1.40);
        
        t5.addSubtask(t51);
        t5.addSubtask(t52);
        s1.add(t5);
        
        final Task t6 = new Task(
                "500/4", 
                datetime(2018, Calendar.APRIL, 2, 12, 30), datetime(2018, Calendar.APRIL, 2, 17, 30)
            );
        t6.setPercentComplete(0.30);
        s1.add(t6);
        
        // podzielone
        
        final Task t7 = new Task(
                "500/5", 
                datetime(2018, Calendar.APRIL, 2, 8, 0), datetime(2018, Calendar.APRIL, 2, 12, 30)
            );
        
        final Task t71 = new Task(
                "500/5-1", 
                datetime(2018, Calendar.APRIL, 2, 8, 0), datetime(2018, Calendar.APRIL, 2, 9, 15)
            );
        t71.setPercentComplete(1.30);
        
        final Task t72 = new Task(
                "500/5-2", 
                datetime(2018, Calendar.APRIL, 2, 11, 0), datetime(2018, Calendar.APRIL, 2, 11, 1)
            );
        t72.setPercentComplete(53.30);
        
        t7.addSubtask(t71);
        t7.addSubtask(t72);
        s1.add(t7);
       

        final TaskSeriesCollection collection = new TaskSeriesCollection();
        collection.add(s1);

        return collection;
    }

    private static Date datetime(final int year, final int month, final int day, final int hour, final int min) {

        final Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, min);
        final Date result = calendar.getTime();
        return result;

    }
}

