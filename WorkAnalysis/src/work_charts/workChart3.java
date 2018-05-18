package work_charts;

import java.awt.Color;
import java.awt.Paint;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

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

public class workChart3 extends ApplicationFrame {
	
	Connection connection=RCPdatabaseConnection.dbConnector("tosia", "1234");
	ArrayList<Employee> employeeslist = new ArrayList<Employee>();
	ArrayList<Integer> project500iterateList = new ArrayList<Integer>();
	ArrayList<Project500> project500List = new ArrayList<Project500>();
	LinkedHashMap<String, String> IOpair = new LinkedHashMap<String, String>();
	
    public static void main(final String[] args) {

        final workChart3 demo = new workChart3("wykres czasu pracy");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);

    }

    // funkcja rysujaca wykres
    public workChart3(final String title) {

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
            	
//                if(row==0 && col ==0){
//                	return new Color(255, 77, 77);
//                }
//                if(row==0 && col ==3){
//                	return new Color(255, 77, 77);
//                }
//                if(row==0 && col ==2){
//                	return new Color(115, 115, 115);
//                }
//                if(row==0 && col ==4){
//                	return new Color(115, 115, 115);
//                }
            	return new Color(255, 77, 77);
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

    // funkcja wrzucajaca dane do wykresu
    private IntervalCategoryDataset createSampleDataset() {

        final TaskSeries s1 = new TaskSeries("seria wynikow");
        
        //NAJPIERW ZBIERAM DANE DLA BAROW WEJSC/WYJSC Z BRAMKI (w ogole nie interesuje mnie HacoSoft! na razie...)
        //biore wszystkie dane z tabel, ale warto dodac opcje (zmodyfikowac queries), zeby user mogl operowac na przedziale dat
        
        // zassij z tabeli access unikalne id_pracownikow (tworzy liste wszystkich pracownikow jacy w ogole przewineli sie przez Zaklad)
        String query="SELECT DISTINCT charts_access.id_karty, charts_cards_name_surname_nrhacosoft.nazwisko_imie, charts_cards_name_surname_nrhacosoft.HacoSoftnumber "
        		+ "FROM charts_access left join charts_cards_name_surname_nrhacosoft on charts_access.id_karty = charts_cards_name_surname_nrhacosoft.id_karty";
		try {
			PreparedStatement pst = connection.prepareStatement(query);
			ResultSet rs=pst.executeQuery();
			String id;
			String surname_name;
			int HacoNr;
			while (rs.next()){
				
				id = rs.getString("id_karty");
				surname_name = rs.getString("nazwisko_imie");
				HacoNr = rs.getInt("HacoSoftnumber");
				Employee e = new Employee(id, surname_name, HacoNr);
				employeeslist.add(e);

			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		System.out.println(employeeslist.toString());
		
		// DLA KAZDEGO PRACOWNIKA STWORZ TASK (NARYSUJ BARY) - NAJPIERW BAR OD BRAMKI, A POZNIEJ BARY PRODUKCYJNE (przy barach produkcyjnych korzystam juz z danych z HacoSofta)
		int i;
		for(i = 0; i < employeeslist.size(); i++){	
		
			IOpair.clear();
			
			System.out.println(employeeslist.get(i).getSurname_name());
			
			//W TYM MIEJSCU PRZYDALBY SIE SKRYPT UZUPELNIAJACY BAZE O BRAKUJACE WEJ/WYJ + FILTRUJACY POWTORZONE BLISKO SIEBIE ZAPISY!!!!!!!!
			//pobierz pary wej/wyj dla kazdego pracownika osobno (omin wpisy typu nowy_pracownik)
			String query2 = "select * from charts_access where id_karty = "+employeeslist.get(i).getId()+"";
			try{
				PreparedStatement pst2 = connection.prepareStatement(query2);
				ResultSet rs2=pst2.executeQuery();
				String in = "";
				String out = "";
				while (rs2.next()){ // UWAGA: zakladam, ze dla kazdego wejscia przypada jedno wyjscie i wpisy zawsze koncza sie na wyjsciu!
					if(rs2.getString("akcja").equals("wejscie")){
						in = rs2.getString("data");
					}
					if(rs2.getString("akcja").equals("wyjscie")){
						out = rs2.getString("data");
						IOpair.put(in, out);
					}
				}
				System.out.println(IOpair.toString());
				
			}catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			
			// stworz taska i subtaski dla niego (kazde para wej/wyj jest sub taskiem) - TASK BRAMKOWY DLA POJEDYNCZEGO PRACOWNIKA
			
			if(!IOpair.isEmpty()){
				
				//znajdz czas pierwszego wejscia i czas ostatniego wyjscia
				Set set = IOpair.entrySet();
				Iterator iterator = set.iterator();
				Iterator iterator2 = set.iterator();
				int k = 0;
				String firstIn = "";
				String lastOut = "";
				
				while(iterator.hasNext()){
					Map.Entry<String, String> mentry = (Map.Entry<String, String>)iterator.next();
					if(k==0){
						firstIn = mentry.getKey();
					}
					lastOut = mentry.getValue(); // jak przejdzie przez wszystkie elementy Hasha, to ostatni ustawi poprawna wartosc (ostatnie wyjscie)
					k++;
				}
			
				// renderuje glowny BAR Bramki
				final Task t = new Task(
						employeeslist.get(i).getSurname_name(), 
		                datetimeString(firstIn), datetimeString(lastOut)
						);
				
				// renderuje sub-taski dla BARow Bramki
				while(iterator2.hasNext()){
					Map.Entry<String, String> mentry = (Map.Entry<String, String>)iterator2.next();
					
					final Task sub = new Task(
							employeeslist.get(i).getSurname_name(),
							datetimeString(mentry.getKey()),
							datetimeString(mentry.getValue())
					);
					t.addSubtask(sub);
					
				}
				s1.add(t);
			}
			
			// stworz taski produkcyjne i ich subtaski - TASKI PRODUKCYJNE DLA POJEDYNCZEGO PRACOWNIKA
			// na razie w trakcie testowania i pracy...nie dokonczylam
			
			//pobiera info ile rekordow z takim samym projektem 500 jest po kolei
			
			String query3 = "SELECT COUNT(CFPROJECT) FROM CHARTS_WERKUREN WHERE AFDELING = 500 AND WERKNEMER = "+employeeslist.get(i).getHacoNr()+" AND BEGINTIJDH IS NOT NULL"
					+ " AND BEGINTIJDM60 is not null and EINDTIJDH is not null and EINDTIJDM60 is not null group by CFPROJECT order by AFDELINGSEQ, DATUM, BEGINTIJDH, BEGINTIJDM60"; 
			
			try{
				
				PreparedStatement pst3 = connection.prepareStatement(query3);
				ResultSet rs3=pst3.executeQuery();
	
				project500iterateList.clear();
				// stworz liste z liczbami rekordow dla takiego samego projektu 500
				while(rs3.next()){
					
					project500iterateList.add(rs3.getInt("COUNT(CFPROJECT)"));
					
				}
				System.out.println(project500iterateList.toString());
				
			}catch (SQLException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}
			
			String query4 = "SELECT * FROM CHARTS_WERKUREN WHERE AFDELING = 500 AND WERKNEMER = "+employeeslist.get(i).getHacoNr()+" AND BEGINTIJDH IS NOT NULL AND BEGINTIJDM60 is not null"
					+ " and EINDTIJDH is not null and EINDTIJDM60 is not null order by AFDELINGSEQ, DATUM, BEGINTIJDH, BEGINTIJDM60 LIMIT 50"; /// TU NIE POWINNO BYC TEGO LIMITU!!!!!!!!! DALAM GO, BO POBIERA ZBYT DUZO WYNIKOW, GDY JEST ZBYT SZEROKI ZAKRES CZASU
			
			try{
				
				PreparedStatement pst4 = connection.prepareStatement(query4);
				ResultSet rs4=pst4.executeQuery();
				
				int y = 0;
				int j = 1;
				int k = project500iterateList.get(y);
				String project500 = "";
				String date = "";
				int beginH = 0;
				int beginMIN = 0;
				int endH = 0;
				int endMIN = 0;
				
				while(rs4.next()){
					
					project500 = rs4.getString("CFPROJECT");
					date = rs4.getString("DATUM");
					beginH = rs4.getInt("BEGINTIJDH");
					beginMIN = rs4.getInt("BEGINTIJDM60"); 
					endH = rs4.getInt("EINDTIJDH");
					endMIN = rs4.getInt("EINDTIJDM60");
					
					Project500 x = new Project500(project500, date, beginH, beginMIN, endH, endMIN);
					project500List.add(x);
					
					if(k == j){
					
						System.out.println("K="+k);
						
						System.out.println(project500List);
						
						// renderuje glowny BAR Projektu 500
						final Task t = new Task(
								project500List.get(0).getProjectNr(), 
				                datetimeString(project500List.get(0).getDateTimeBegin()), datetimeString(project500List.get(project500List.size()-1).getDateTimeEnd())
								);
						
						// renderuje sub-taski dla BARow Projektu 500
						int z = 0;
						for(z = 0; z < project500List.size(); z++){
							
							final Task sub = new Task(
									project500List.get(z).getProjectNr(),
									datetimeString(project500List.get(z).getDateTimeBegin()),
									datetimeString(project500List.get(z).getDateTimeEnd())
							);
							sub.setPercentComplete(1.50); // TA LINIA RENDERUJE BAR W BARZE - MOZNA GO UZYC DO POKAZANIA CZASU TEORETYCZNEGO (ILE NA OBROBKE PRZEWIDZIAL TECHNOLOG)
							t.addSubtask(sub);
							
						}
						
						s1.add(t);
						
						project500List.clear();
						if(y+1 < project500iterateList.size()){
							k = project500iterateList.get(y = y+1);
						}
						j = 0;
						
					}
					j++;
					
				}
				
			}catch (SQLException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}
			
			
		}

        final TaskSeriesCollection collection = new TaskSeriesCollection();
        collection.add(s1);

        return collection;
    }

    // fukncja ustawiajaca date i czas
    private static Date datetime(final int year, final int month, final int day, final int hour, final int min) {

        final Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, min);
        final Date result = calendar.getTime();
        return result;

    }
    
    // fukncja ustawiajaca date i czas (inny typ wrzucanych danych)
    private static Date datetimeString(String dateRecord) {

        final Calendar calendar = Calendar.getInstance();
        int year = Integer.parseInt(dateRecord.substring(0, 4));
        int month = Integer.parseInt(dateRecord.substring(5, 7))-1;;
        int day = Integer.parseInt(dateRecord.substring(8, 10));;
        int hour = Integer.parseInt(dateRecord.substring(11, 13));;
        int min = Integer.parseInt(dateRecord.substring(14, 16));;
        calendar.set(year, month, day, hour, min);
        final Date result = calendar.getTime();
        return result;

    }
}

