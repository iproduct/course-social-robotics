package demos;

public class Todo {
	private String title;
	private int minutes;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getMinutes() {
		return minutes;
	}
	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}
	public Todo(String title, int minutes) {
		super();
		this.title = title;
		this.minutes = minutes;
	}
	@Override
	public String toString() {
		return "Todo [title=" + title + ", minutes=" + minutes + "]";
	}
}
