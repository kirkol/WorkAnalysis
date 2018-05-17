package work_charts;

public class Employee {
	
	private String id;
	private String surname_name;
	private int HacoNr;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSurname_name() {
		return surname_name;
	}

	public void setSurname_name(String surname_name) {
		this.surname_name = surname_name;
	}

	public int getHacoNr() {
		return HacoNr;
	}

	public void setHacoNr(int hacoNr) {
		HacoNr = hacoNr;
	}
	
	public String toString(){
		return id + " - " + surname_name + " - " + HacoNr;
	}

	public Employee(String id, String surname_name, int HacoNr){
		this.id = id;
		this.surname_name = surname_name;
		this.HacoNr = HacoNr;
	}
}
