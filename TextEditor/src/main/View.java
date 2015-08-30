package main;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.text.DefaultEditorKit;

@SuppressWarnings({ "serial", "unused" })
public class View {

	private Controller controller;
	private String fileName = "Untitled";
	
	private JFrame frame;
	private JTextArea area;
	
	private boolean modified;
	
	public View(final Controller controller){
		this.controller = controller;
		modified = false;
		
		final JFileChooser dialog = new JFileChooser(FileSystemView.getFileSystemView().getDefaultDirectory());
		dialog.addChoosableFileFilter(new FileNameExtensionFilter(".txt", "txt"));
		dialog.setAcceptAllFileFilterUsed(false);
		
		frame = new JFrame(TextEditor.APPNAME + " - " + fileName);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = (JPanel) frame.getContentPane();
		panel.setLayout(new BorderLayout());
		
		//Text Area
		area = new JTextArea(40, 135);
		area.setFont(new Font("Monospaced", Font.PLAIN, 12));
		area.setWrapStyleWord(true);
		area.setLineWrap(true);
		
		JScrollPane scrollPane = new JScrollPane(area, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		ActionMap am = area.getActionMap();
		
		//Actions
		Action newAction = new AbstractAction("New", new ImageIcon("img/new-icon.png")){
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("New fired.");
				controller.newEvent();
			}
		};
		
		Action openAction = new AbstractAction("Open", new ImageIcon("img/open-icon.png")){
			@Override
			public void actionPerformed(ActionEvent e) {
				checkSave();
				if(dialog.showOpenDialog(panel)==JFileChooser.APPROVE_OPTION) {
					controller.openEvent(dialog.getSelectedFile().getPath(), dialog.getSelectedFile().getName());
				}
			}
		};
		
		Action saveAction = new AbstractAction("Save", new ImageIcon("img/save-icon.png")){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(controller.fileExists()){
					controller.saveEvent();
				}
				else{
					if(dialog.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION){
						controller.saveAsEvent(area.getText(), dialog.getSelectedFile().getAbsolutePath() + ".txt");
					}
				}
			}
		};
		
		Action saveAsAction = new AbstractAction("SaveAs", new ImageIcon("img/save-icon.png")){
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("SaveAs fired.");
				controller.saveEvent();
			}
		};
		
		Action quitAction = new AbstractAction("Quit"){
			@Override
			public void actionPerformed(ActionEvent e) {
				//Check for whether save is needed
				System.exit(0);
			}
		};
		
		Action cutAction = am.get(DefaultEditorKit.cutAction);
		cutAction.putValue(Action.SMALL_ICON, new ImageIcon("img/cut-icon.png"));
		
		Action copyAction =  am.get(DefaultEditorKit.copyAction);
		copyAction.putValue(Action.SMALL_ICON, new ImageIcon("img/copy-icon.png"));
		
		Action pasteAction =  am.get(DefaultEditorKit.pasteAction);
		pasteAction.putValue(Action.SMALL_ICON, new ImageIcon("img/paste-icon.png"));
		
		Action aboutAction = new AbstractAction("About TextEditor", new ImageIcon("img/coffee-icon.png")){
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("About fired.");
			}
		};
		
		//MenuBar
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		fileMenu.add(newAction);
		fileMenu.add(openAction);
		fileMenu.add(saveAction);
		fileMenu.add(saveAsAction);
		fileMenu.addSeparator();
		fileMenu.add(quitAction);
		menuBar.add(fileMenu);
		
		JMenu editMenu = new JMenu("Edit");
		editMenu.add(cutAction);
		editMenu.add(copyAction);
		editMenu.add(pasteAction);
		menuBar.add(editMenu);
		
		JMenu helpMenu = new JMenu("Help");
		helpMenu.add(aboutAction);
		menuBar.add(helpMenu);
		
		//ToolBar
		JToolBar toolBar = new JToolBar();
		JButton newBtn = new JButton();
		toolBar.add(newAction);
		
		JButton openBtn = new JButton();
		toolBar.add(openAction);
		
		JButton saveBtn = new JButton();
		toolBar.add(saveAction);
		
		toolBar.addSeparator();
		
		JButton cutBtn = new JButton();
		toolBar.add(cutAction);
		
		JButton copyBtn = new JButton();
		toolBar.add(copyAction);
		
		JButton pasteBtn = new JButton();
		toolBar.add(pasteAction);
		
		toolBar.addSeparator();
		
		JButton coffeeBtn = new JButton();
		toolBar.add(aboutAction);
		
		panel.add(toolBar, BorderLayout.NORTH);
		panel.add(scrollPane, BorderLayout.CENTER);
		frame.setJMenuBar(menuBar);
		frame.pack();
		frame.setVisible(true);
		area.requestFocus();
	}
	
	public void checkSave(){
		if(modified){
			
			if(JOptionPane.showConfirmDialog(frame, "Would you like to save " + fileName + "?", "Save?", JOptionPane.YES_NO_CANCEL_OPTION) == JOptionPane.YES_OPTION){
				
			}
			
//			int option  = JOptionPane.showConfirmDialog(frame, "Would you like to save " + fileName + "?", "Save?", JOptionPane.YES_NO_CANCEL_OPTION);			
//			switch(option){
//				case JOptionPane.YES_OPTION:
//					System.out.println("YEAH!");
//					break;
//				case JOptionPane.NO_OPTION:
//					System.out.println("Hell, no!");
//					break;
//			}
		}
	}
	
	
	public void updateView(String content, String fileName){
		area.setText(content);
		frame.setTitle(TextEditor.APPNAME + " - " + fileName);
		modified = false;
	}
}
