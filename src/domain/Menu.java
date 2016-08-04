package domain;

public class Menu {
	
	private String item;
	private String text;
	private String owner;
	private String path;
	private String name;
	private String version;
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	@Override
	public String toString() {
		return "Menu [item=" + item + ", text=" + text + ", owner=" + owner
				+ ", path=" + path + ", name=" + name + ", version=" + version
				+ "]";
	}
	
}
