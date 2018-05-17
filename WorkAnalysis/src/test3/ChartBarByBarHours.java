package test3;

import java.util.Calendar;
import java.util.Date;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.time.SimpleTimePeriod;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class ChartBarByBarHours extends ApplicationFrame {

    public ChartBarByBarHours(final String title) {

        super(title);

        final IntervalCategoryDataset dataset = createDataset();
        final JFreeChart chart = createChart(dataset);

        // add the chart to a panel...
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);

    }

    public static IntervalCategoryDataset createDataset() {

        final TaskSeries s1 = new TaskSeries("Godziny z bramki");
        s1.add(new Task("Antonina Kirkalo-Kowerczyk",
               new SimpleTimePeriod(time(2018, Calendar.APRIL, 3, 8, 30),
            		   				time(2018, Calendar.APRIL, 3, 15, 30))));
        s1.add(new Task("Czes³aw Lipski",
               new SimpleTimePeriod(time(2018, Calendar.APRIL, 3, 6, 00),
            		   				time(2018, Calendar.APRIL, 3, 16, 30))));

        final TaskSeries s2 = new TaskSeries("Godziny z rejestracji");
        s2.add(new Task("Antonina Kirkalo-Kowerczyk",
               new SimpleTimePeriod(time(2018, Calendar.APRIL, 3, 10, 30),
                                    time(2018, Calendar.APRIL, 3, 12, 30))));
        
        s2.add(new Task("Czes³aw Lipski",
               new SimpleTimePeriod(time(2018, Calendar.APRIL, 3, 7, 15),
                                    time(2018, Calendar.APRIL, 3, 9, 30))));

        
        final TaskSeries s3 = new TaskSeries("Godziny teoretyczne");
        s3.add(new Task("Antonina Kirkalo-Kowerczyk",
               new SimpleTimePeriod(time(2018, Calendar.APRIL, 3, 10, 30),
                       				time(2018, Calendar.APRIL, 3, 13, 30))));
        s3.add(new Task("Czes³aw Lipski",
               new SimpleTimePeriod(time(2018, Calendar.APRIL, 3, 7, 15),
                       				time(2018, Calendar.APRIL, 3, 12, 30))));


        final TaskSeriesCollection collection = new TaskSeriesCollection();
        collection.add(s1);
        collection.add(s2);
        collection.add(s3);
        
        return collection;
    }


    private static Date date(final int day, final int month, final int year) {

        final Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        final Date result = calendar.getTime();
        return result;

    }
    
    private static Date time(final int year, final int month, final int day, final int hour, final int minute) {

    	final Calendar calendar = Calendar.getInstance();
    	calendar.set(year, month, day, hour, minute);
    	final Date result = calendar.getTime();
    	return result;
    	
    }
        
    private JFreeChart createChart(final IntervalCategoryDataset dataset) {
        final JFreeChart chart = ChartFactory.createGanttChart(
            "Wykres godzin pracy",  // chart title
            "Pracownicy",              // domain axis label
            "Czas",              // range axis label
            dataset,             // data
            true,                // include legend
            true,                // tooltips
            false                // urls
        );    
//        chart.getCategoryPlot().getDomainAxis().setMaxCategoryLabelWidthRatio(10.0f);
        return chart;    
    }
    
    public static void main(final String[] args) {

        final ChartBarByBarHours demo = new ChartBarByBarHours("Wykres godzin pracy");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);

    }

}

           
       