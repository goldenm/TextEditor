package main;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;

public class View {

	private Controller controller;
	
	public View(final Controller controller){
		this.controller = controller;
		
		JFrame frame = new JFrame(TextEditor.APPNAME);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = (JPanel) frame.getContentPane();
		panel.setLayout(new BorderLayout());
		
		//MenuBar
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		fileMenu.add("New");
		fileMenu.add("Open");
		fileMenu.add("Save");
		fileMenu.add("Save As");
		fileMenu.addSeparator();
		fileMenu.add("Quit");
		menuBar.add(fileMenu);
		
		JMenu editMenu = new JMenu("Edit");
		editMenu.add("Cut");
		editMenu.add("Copy");
		editMenu.add("Paste");
		menuBar.add(editMenu);
		
		JMenu helpMenu = new JMenu("Help");
		helpMenu.add("About TextEditor");
		menuBar.add(helpMenu);
		
		//ToolBar
		JToolBar toolBar = new JToolBar();
		JButton newBtn = new JButton();
		newBtn.setText(null);
		newBtn.setIcon(new ImageIcon("img/new-icon.png"));
		toolBar.add(newBtn);
		
		JButton loadBtn = new JButton();
		loadBtn.setText(null);
		loadBtn.setIcon(new ImageIcon("img/load-icon.png"));
		toolBar.add(loadBtn);
		
		JButton saveBtn = new JButton();
		saveBtn.setText(null);
		saveBtn.setIcon(new ImageIcon("img/save-icon.png"));
		toolBar.add(saveBtn);
		
		toolBar.addSeparator();
		
		JButton cutBtn = new JButton();
		cutBtn.setText(null);
		cutBtn.setIcon(new ImageIcon("img/cut-icon.png"));
		toolBar.add(cutBtn);
		
		JButton copyBtn = new JButton();
		copyBtn.setText(null);
		copyBtn.setIcon(new ImageIcon("img/copy-icon.png"));
		toolBar.add(copyBtn);
		
		JButton pasteBtn = new JButton();
		pasteBtn.setText(null);
		pasteBtn.setIcon(new ImageIcon("img/paste-icon.png"));
		toolBar.add(pasteBtn);
		
		toolBar.addSeparator();
		
		JButton coffeeBtn = new JButton();
		coffeeBtn.setText(null);
		coffeeBtn.setIcon(new ImageIcon("img/coffee-icon.png"));
		toolBar.add(coffeeBtn);
		
		//Text Area
		JTextArea area = new JTextArea(40, 135);
		area.setFont(new Font("Monospaced", Font.PLAIN, 12));
		
		//TODO: sort out word wrap
		JScrollPane scrollPane = new JScrollPane(area, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		panel.add(toolBar, BorderLayout.NORTH);
		panel.add(scrollPane, BorderLayout.SOUTH);
		frame.setJMenuBar(menuBar);
		frame.pack();
		frame.setVisible(true);
	}
}
