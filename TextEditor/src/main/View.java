package main;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.text.DefaultEditorKit;

@SuppressWarnings({ "serial" })
public class View {

	private Controller controller;
	private String fileName = "Untitled";
	
	private JFrame frame;
	private JTextArea area;
	private JScrollPane scrollPane;
	private JFileChooser dialog;
	private Action saveAction, saveAsAction;
	
	BufferedImage newIcon, openIcon, saveIcon, helpIcon, cutIcon, copyIcon, pasteIcon;
	
	private boolean modified;
	
	public View(final Controller controller){
		this.controller = controller;
		
		dialog = new JFileChooser(FileSystemView.getFileSystemView().getDefaultDirectory());
		dialog.addChoosableFileFilter(new FileNameExtensionFilter(".txt", "txt"));
		dialog.setAcceptAllFileFilterUsed(false);
		
		frame = new JFrame(TextEditor.APPNAME + " - " + fileName);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = (JPanel) frame.getContentPane();
		panel.setLayout(new BorderLayout());
		
		// Text Area
		area = new JTextArea(40, 135);
		area.setFont(new Font("Monospaced", Font.PLAIN, 12));
		area.setWrapStyleWord(true);
		area.setLineWrap(true);
		scrollPane = new JScrollPane(area, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		ActionMap am = area.getActionMap();
		
		try {
			newIcon = ImageIO.read(getClass().getResource("/new-icon.png"));
			openIcon = ImageIO.read(getClass().getResource("/open-icon.png"));
			saveIcon = ImageIO.read(getClass().getResource("/save-icon.png"));
			helpIcon = ImageIO.read(getClass().getResource("/coffee-icon.png"));
			cutIcon = ImageIO.read(getClass().getResource("/cut-icon.png"));
			copyIcon = ImageIO.read(getClass().getResource("/copy-icon.png"));
			pasteIcon = ImageIO.read(getClass().getResource("/paste-icon.png"));
		} catch (IOException e1) {
			System.err.println("Class loader can't find the resource images.");
			e1.printStackTrace();
		}
		
		// Actions
		Action newAction = new AbstractAction("New", new ImageIcon(newIcon)){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(checkSaveHandled()){
					newTextArea();
					controller.newEvent();
				}
			}
		};
		newAction.putValue(AbstractAction.SHORT_DESCRIPTION, "New");
		
		Action openAction = new AbstractAction("Open", new ImageIcon(openIcon)){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(checkSaveHandled()){
					if(dialog.showOpenDialog(panel)==JFileChooser.APPROVE_OPTION) {
						controller.openEvent(dialog.getSelectedFile().getPath(), dialog.getSelectedFile().getName());
						setModified(false);
					}
				}
			}
		};
		openAction.putValue(AbstractAction.SHORT_DESCRIPTION, "Open");
		
		saveAction = new AbstractAction("Save", new ImageIcon(saveIcon)){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(controller.fileExists()){
					controller.saveEvent(area.getText());
					setModified(false);
				}
				else{
					//TODO- If you load a file and open a new one, the filename still appears in the dialog when you save
					if(dialog.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION){
						controller.saveAsEvent(area.getText(), dialog.getSelectedFile().getAbsolutePath());
						setModified(false);
					}
				}
			}
		};
		saveAction.putValue(AbstractAction.SHORT_DESCRIPTION, "Save");
		
		saveAsAction = new AbstractAction("SaveAs", new ImageIcon(saveIcon)){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(dialog.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION){
					controller.saveAsEvent(area.getText(), dialog.getSelectedFile().getAbsolutePath());
					setModified(false);
				}
			}
		};
		saveAsAction.putValue(AbstractAction.SHORT_DESCRIPTION, "Save As");
		
		Action quitAction = new AbstractAction("Quit"){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(checkSaveHandled()){
					System.exit(0);
				}
			}
		};
		
		Action cutAction = am.get(DefaultEditorKit.cutAction);
		cutAction.putValue(Action.SMALL_ICON, new ImageIcon(cutIcon));
		cutAction.putValue(AbstractAction.SHORT_DESCRIPTION, "Cut");
		
		Action copyAction =  am.get(DefaultEditorKit.copyAction);
		copyAction.putValue(Action.SMALL_ICON, new ImageIcon(copyIcon));
		copyAction.putValue(AbstractAction.SHORT_DESCRIPTION, "Copy");
		
		Action pasteAction =  am.get(DefaultEditorKit.pasteAction);
		pasteAction.putValue(Action.SMALL_ICON, new ImageIcon(pasteIcon));
		pasteAction.putValue(AbstractAction.SHORT_DESCRIPTION, "Paste");
		
		Action aboutAction = new AbstractAction("About TextEditor", new ImageIcon(helpIcon)){
			@Override
			public void actionPerformed(ActionEvent e) {
				//TODO - Implement this
				JOptionPane.showMessageDialog(frame,
					    "TextEditor -- a simple text editor implemented in Java\n" +
					    		"Author \t Matt Golden\nReleased under The MIT License (MIT)",
					    "About TextEditor",
					    JOptionPane.INFORMATION_MESSAGE);
			}
		};
		aboutAction.putValue(AbstractAction.SHORT_DESCRIPTION, "About TextEditor");
		
		setModified(false);
		
		// KeyListener to update modified boolean
		KeyListener keyListener = new KeyAdapter(){
			@Override
			public void keyPressed(KeyEvent e) {
				super.keyPressed(e);
				setModified(true);
			}
		};
		
		area.addKeyListener(keyListener);
		
		// MenuBar
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
		toolBar.add(newAction);
		toolBar.add(openAction);
		toolBar.add(saveAction);
		toolBar.addSeparator();
		toolBar.add(cutAction);
		toolBar.add(copyAction);
		toolBar.add(pasteAction);
		toolBar.addSeparator();
		toolBar.add(aboutAction);
		
		panel.add(toolBar, BorderLayout.NORTH);
		panel.add(scrollPane, BorderLayout.CENTER);
		frame.setJMenuBar(menuBar);
		frame.pack();
		frame.setVisible(true);
		frame.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				if(checkSaveHandled()){
					System.exit(0);
				}
			}
		});
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		area.requestFocus();
	}
	
	// This passes test cases but is pretty ugly and could use some refactoring.
	public boolean checkSaveHandled(){
		if(modified){
			switch(JOptionPane.showConfirmDialog(frame, "Would you like to save " + fileName + "?", "Save?", JOptionPane.YES_NO_CANCEL_OPTION)) {
			case JOptionPane.YES_OPTION:	if(controller.fileExists()){
												controller.saveEvent(area.getText());
												setModified(false);
												return true;
											}
											else{
												if(dialog.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION){
													controller.saveAsEvent(area.getText(), dialog.getSelectedFile().getAbsolutePath());
													setModified(false);
													return true;
												}
											}
			case JOptionPane.NO_OPTION:		return true;
			case JOptionPane.CANCEL_OPTION:	return false;
			}
		}
		return true;
	}
	
	private void newTextArea(){
		area.setText(null);
		fileName = "Untitled";
		frame.setTitle(TextEditor.APPNAME + " - " + fileName);
		modified = false;
	}
	
	public void updateView(String content, String fileName){
		area.setText(content);
		frame.setTitle(TextEditor.APPNAME + " - " + fileName);
		modified = false;
	}
	
	// No getter for modified.  It's fine being an inaccessible member of
	// this class because no other class needs to use it yet.
	public void setModified(boolean modified){
		saveAction.setEnabled(modified);
		saveAsAction.setEnabled(modified);
		this.modified = modified;
	}
}
