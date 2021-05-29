package application;

public class Article {
	private String code;
	private String name;
	private String type;
	
	public Article(String code, String name, String type) {
		this.name = name;
		this.code = code;
		this.type = type;
	}
	
	//Getters and setters
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
