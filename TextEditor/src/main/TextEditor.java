package main;

public class TextEditor {

	public static final String APPNAME = "TextEditor";
	
	static Controller controller;
	static View view;
	
	public static void main(String[] args){
		controller = new Controller();
		view = new View(controller);
		controller.setView(view);
	}
}
