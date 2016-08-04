package domain;

public class Person {

	private String text;
	private String path;
	private String item;
	private String name;
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getItem() {
		return item;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setItem(String item) {
		this.item = item;
	}
	@Override
	public String toString() {
		return "Person [text=" + text + ", path=" + path + ", item=" + item
				+ ", name=" + name + "]";
	}
	

}
