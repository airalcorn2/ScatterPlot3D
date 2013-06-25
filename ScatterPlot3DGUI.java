/*
 * Copyright (c) 2013 Michael A. Alcorn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
import java.awt.GraphicsConfiguration;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.media.j3d.Canvas3D;
import javax.swing.JFileChooser;

import com.sun.j3d.utils.universe.SimpleUniverse;

public class ScatterPlot3DGUI extends javax.swing.JFrame {

	private static final long serialVersionUID = 1L;
	File dataFile, colorFile;
	static String[] headers;
	boolean colorFileExists = false;
	ScatterPlot3D scatter;

	public ScatterPlot3DGUI() {
		initComponents();
	}

	private void initComponents() {

		fileChooser = new javax.swing.JFileChooser();
		saveMenu = new javax.swing.JPopupMenu();
		saveMenuItem = new javax.swing.JMenuItem();
		GraphicsConfiguration config = SimpleUniverse
				.getPreferredConfiguration();
		plotCanvas = new Canvas3D(config);
		plotCanvas.setSize(400, 400);
		colSearchComboBox = new javax.swing.JComboBox<String>();
		submitButton = new javax.swing.JButton();
		clearButton = new javax.swing.JButton();
		detailScrollPane = new javax.swing.JScrollPane();
		detailTextArea = new javax.swing.JTextArea();
		searchTextField = new javax.swing.JTextField();
		searchButton = new javax.swing.JButton();
		openFileButton = new javax.swing.JButton();
		groupColorsButton = new javax.swing.JButton();
		groupColTextField = new javax.swing.JTextField();
		sizeDataPointLabel = new javax.swing.JLabel();
		sizeDataPointTextField = new javax.swing.JTextField();
		xColTextField = new javax.swing.JTextField();
		xColLabel = new javax.swing.JLabel();
		yColLabel = new javax.swing.JLabel();
		yColTextField = new javax.swing.JTextField();
		zColLabel = new javax.swing.JLabel();
		zColTextField = new javax.swing.JTextField();
		groupColLabel = new javax.swing.JLabel();

		saveMenuItem.setText("Save");
		saveMenu.add(saveMenuItem);

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("ScatterPlot3D");

		plotCanvas.setMinimumSize(new java.awt.Dimension(400, 400));

		colSearchComboBox
				.setModel(new javax.swing.DefaultComboBoxModel<String>(
						new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

		submitButton.setText("Submit");
		submitButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				submitButtonActionPerformed(evt);
			}
		});

		clearButton.setText("Clear");
		clearButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				clearButtonActionPerformed(evt);
			}
		});

		detailTextArea.setColumns(20);
		detailTextArea.setRows(5);
		detailScrollPane.setViewportView(detailTextArea);
		detailTextArea.setLineWrap(true);

		searchTextField.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyPressed(java.awt.event.KeyEvent evt) {
				searchTextFieldKeyPressed(evt);
			}
		});

		searchButton.setText("Search");
		searchButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				searchButtonActionPerformed(evt);
			}
		});

		openFileButton.setText("Data");
		openFileButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				openFileButtonActionPerformed(evt);
			}
		});

		groupColorsButton.setText("Colors");
		groupColorsButton
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						groupColorsButtonActionPerformed(evt);
					}
				});

		sizeDataPointLabel.setText("Point size");

		sizeDataPointTextField.setText("0.05");

		xColTextField.setText("column");

		xColLabel.setText("x");

		yColLabel.setText("y");

		yColTextField.setText("column");

		zColLabel.setText("z");

		zColTextField.setText("column");

		groupColLabel.setText("Groups column");

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addComponent(plotCanvas,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING,
												false)
												.addGroup(
														javax.swing.GroupLayout.Alignment.TRAILING,
														layout.createSequentialGroup()
																.addComponent(
																		searchTextField)
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																.addComponent(
																		searchButton))
												.addComponent(
														colSearchComboBox,
														javax.swing.GroupLayout.Alignment.TRAILING,
														0,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE)
												.addComponent(
														detailScrollPane,
														javax.swing.GroupLayout.Alignment.TRAILING,
														javax.swing.GroupLayout.PREFERRED_SIZE,
														0, Short.MAX_VALUE)
												.addGroup(
														layout.createParallelGroup(
																javax.swing.GroupLayout.Alignment.TRAILING)
																.addGroup(
																		layout.createSequentialGroup()
																				.addComponent(
																						submitButton,
																						javax.swing.GroupLayout.PREFERRED_SIZE,
																						112,
																						javax.swing.GroupLayout.PREFERRED_SIZE)
																				.addPreferredGap(
																						javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																				.addComponent(
																						clearButton,
																						javax.swing.GroupLayout.PREFERRED_SIZE,
																						112,
																						javax.swing.GroupLayout.PREFERRED_SIZE))
																.addGroup(
																		layout.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.LEADING)
																				.addGroup(
																						layout.createSequentialGroup()
																								.addComponent(
																										openFileButton,
																										javax.swing.GroupLayout.PREFERRED_SIZE,
																										112,
																										javax.swing.GroupLayout.PREFERRED_SIZE)
																								.addPreferredGap(
																										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																								.addComponent(
																										groupColorsButton,
																										javax.swing.GroupLayout.PREFERRED_SIZE,
																										112,
																										javax.swing.GroupLayout.PREFERRED_SIZE))
																				.addGroup(
																						layout.createSequentialGroup()
																								.addGap(8,
																										8,
																										8)
																								.addGroup(
																										layout.createParallelGroup(
																												javax.swing.GroupLayout.Alignment.LEADING)
																												.addGroup(
																														layout.createSequentialGroup()
																																.addComponent(
																																		xColLabel,
																																		javax.swing.GroupLayout.PREFERRED_SIZE,
																																		8,
																																		javax.swing.GroupLayout.PREFERRED_SIZE)
																																.addGap(2,
																																		2,
																																		2)
																																.addComponent(
																																		xColTextField,
																																		javax.swing.GroupLayout.PREFERRED_SIZE,
																																		60,
																																		javax.swing.GroupLayout.PREFERRED_SIZE)
																																.addGap(3,
																																		3,
																																		3)
																																.addComponent(
																																		yColLabel,
																																		javax.swing.GroupLayout.PREFERRED_SIZE,
																																		8,
																																		javax.swing.GroupLayout.PREFERRED_SIZE)
																																.addGap(3,
																																		3,
																																		3)
																																.addComponent(
																																		yColTextField,
																																		javax.swing.GroupLayout.PREFERRED_SIZE,
																																		60,
																																		javax.swing.GroupLayout.PREFERRED_SIZE)
																																.addGap(2,
																																		2,
																																		2)
																																.addComponent(
																																		zColLabel,
																																		javax.swing.GroupLayout.PREFERRED_SIZE,
																																		8,
																																		javax.swing.GroupLayout.PREFERRED_SIZE)
																																.addGap(3,
																																		3,
																																		3)
																																.addComponent(
																																		zColTextField,
																																		javax.swing.GroupLayout.PREFERRED_SIZE,
																																		60,
																																		javax.swing.GroupLayout.PREFERRED_SIZE))
																												.addGroup(
																														layout.createSequentialGroup()
																																.addGroup(
																																		layout.createParallelGroup(
																																				javax.swing.GroupLayout.Alignment.LEADING)
																																				.addComponent(
																																						groupColLabel,
																																						javax.swing.GroupLayout.PREFERRED_SIZE,
																																						106,
																																						javax.swing.GroupLayout.PREFERRED_SIZE)
																																				.addComponent(
																																						sizeDataPointLabel))
																																.addGap(4,
																																		4,
																																		4)
																																.addGroup(
																																		layout.createParallelGroup(
																																				javax.swing.GroupLayout.Alignment.LEADING,
																																				false)
																																				.addComponent(
																																						sizeDataPointTextField,
																																						javax.swing.GroupLayout.DEFAULT_SIZE,
																																						112,
																																						Short.MAX_VALUE)
																																				.addComponent(
																																						groupColTextField))))))))
								.addGap(12, 12, 12)));
		layout.setVerticalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						javax.swing.GroupLayout.Alignment.TRAILING,
						layout.createSequentialGroup()
								.addGap(12, 12, 12)
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.TRAILING)
												.addComponent(
														plotCanvas,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE)
												.addGroup(
														layout.createSequentialGroup()
																.addGroup(
																		layout.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.LEADING)
																				.addComponent(
																						groupColorsButton,
																						javax.swing.GroupLayout.Alignment.TRAILING)
																				.addComponent(
																						openFileButton))
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																.addGroup(
																		layout.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.BASELINE)
																				.addComponent(
																						xColTextField,
																						javax.swing.GroupLayout.PREFERRED_SIZE,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						javax.swing.GroupLayout.PREFERRED_SIZE)
																				.addComponent(
																						xColLabel)
																				.addComponent(
																						yColLabel)
																				.addComponent(
																						yColTextField,
																						javax.swing.GroupLayout.PREFERRED_SIZE,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						javax.swing.GroupLayout.PREFERRED_SIZE)
																				.addComponent(
																						zColLabel)
																				.addComponent(
																						zColTextField,
																						javax.swing.GroupLayout.PREFERRED_SIZE,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						javax.swing.GroupLayout.PREFERRED_SIZE))
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																.addGroup(
																		layout.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.BASELINE)
																				.addComponent(
																						groupColTextField,
																						javax.swing.GroupLayout.PREFERRED_SIZE,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						javax.swing.GroupLayout.PREFERRED_SIZE)
																				.addComponent(
																						groupColLabel))
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																.addGroup(
																		layout.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.BASELINE)
																				.addComponent(
																						sizeDataPointLabel,
																						javax.swing.GroupLayout.PREFERRED_SIZE,
																						27,
																						javax.swing.GroupLayout.PREFERRED_SIZE)
																				.addComponent(
																						sizeDataPointTextField,
																						javax.swing.GroupLayout.PREFERRED_SIZE,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						javax.swing.GroupLayout.PREFERRED_SIZE))
																.addGap(5, 5, 5)
																.addGroup(
																		layout.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.BASELINE)
																				.addComponent(
																						submitButton)
																				.addComponent(
																						clearButton))
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																.addComponent(
																		detailScrollPane,
																		javax.swing.GroupLayout.DEFAULT_SIZE,
																		268,
																		Short.MAX_VALUE)
																.addGap(12, 12,
																		12)
																.addComponent(
																		colSearchComboBox,
																		javax.swing.GroupLayout.PREFERRED_SIZE,
																		javax.swing.GroupLayout.DEFAULT_SIZE,
																		javax.swing.GroupLayout.PREFERRED_SIZE)
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																.addGroup(
																		layout.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.BASELINE)
																				.addComponent(
																						searchTextField,
																						javax.swing.GroupLayout.PREFERRED_SIZE,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						javax.swing.GroupLayout.PREFERRED_SIZE)
																				.addComponent(
																						searchButton))))
								.addContainerGap()));

		pack();
	}

	private void openFileButtonActionPerformed(java.awt.event.ActionEvent evt) {

		int returnVal = fileChooser.showOpenDialog(this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {

			dataFile = fileChooser.getSelectedFile();

		} else {

			System.out.println("File access cancelled by user.");

		}
	}

	private void submitButtonActionPerformed(java.awt.event.ActionEvent evt) {

		boolean run = true;

		scatter = new ScatterPlot3D();

		if (this.dataFile != null)
			scatter.dataFile = this.dataFile;
		else {

			run = false;
			detailTextArea.append("No data file selected.\n\n");

		}

		if (isAnInteger(xColTextField.getText()))
			scatter.xCol = Integer.parseInt(xColTextField.getText()) - 1;
		else {

			detailTextArea.append("x column is not an integer.\n\n");
			run = false;

		}

		if (isAnInteger(yColTextField.getText()))
			scatter.yCol = Integer.parseInt(yColTextField.getText()) - 1;
		else {

			detailTextArea.append("y column is not an integer.\n\n");
			run = false;

		}

		if (isAnInteger(zColTextField.getText()))
			scatter.zCol = Integer.parseInt(zColTextField.getText()) - 1;
		else {

			detailTextArea.append("z column is not an integer.\n\n");
			run = false;

		}

		if (isAnInteger(groupColTextField.getText())) {

			scatter.groupCol = Integer.parseInt(groupColTextField
					.getText()) - 1;
			scatter.groupColPresent = true;

		} else {

			if (groupColTextField.getText().isEmpty())
				scatter.groupColPresent = false;
			else {

				detailTextArea.append("Groups column is not an integer.\n\n");
				run = false;

			}
		}

		if (isADouble(sizeDataPointTextField.getText()))
			scatter.sphereRadius = Float
					.parseFloat(sizeDataPointTextField.getText());
		else {

			detailTextArea.append("Point size is not a number.\n\n");
			run = false;

		}

		if (run) {
			scatter.showPlot(colorFile);

			colSearchComboBox
					.setModel(new javax.swing.DefaultComboBoxModel<String>(
							headers));
		}
	}

	boolean isAnInteger(String potInt) {

        return potInt.matches("[0-9]+");

	}

	boolean isADouble(String potDouble) {

        return potDouble.matches("[0-9]*\\.?[0-9]*");

	}

	private void groupColorsButtonActionPerformed(java.awt.event.ActionEvent evt) {

		int returnVal = fileChooser.showOpenDialog(this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {

			colorFile = fileChooser.getSelectedFile();
			colorFileExists = true;

		} else {

			System.out.println("File access cancelled by user.");

		}
	}

	private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {

		this.dispose();
		new ScatterPlot3DGUI().setVisible(true);

	}

	private void searchTextFieldKeyPressed(java.awt.event.KeyEvent evt) {

		if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

			String column = colSearchComboBox.getSelectedItem().toString();

			for (int whichCol = 0; whichCol < headers.length; whichCol++) {

				if (column.equals(headers[whichCol])) {

					scatter.find(searchTextField.getText(), whichCol);
					return;

				}
			}
		}
	}

	private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {

		String column = colSearchComboBox.getSelectedItem().toString();

		for (int whichCol = 0; whichCol < headers.length; whichCol++) {

			if (column.equals(headers[whichCol])) {

				scatter.find(searchTextField.getText(), whichCol);
				return;

			}
		}
	}

	private javax.swing.JButton clearButton;
	private javax.swing.JComboBox<String> colSearchComboBox;
	private javax.swing.JScrollPane detailScrollPane;
	static javax.swing.JTextArea detailTextArea;
	private javax.swing.JFileChooser fileChooser;
	private javax.swing.JLabel groupColLabel;
	private javax.swing.JTextField groupColTextField;
	private javax.swing.JButton groupColorsButton;
	private javax.swing.JButton openFileButton;
	static Canvas3D plotCanvas;
	private javax.swing.JPopupMenu saveMenu;
	private javax.swing.JMenuItem saveMenuItem;
	private javax.swing.JButton searchButton;
	private javax.swing.JTextField searchTextField;
	private javax.swing.JLabel sizeDataPointLabel;
	private javax.swing.JTextField sizeDataPointTextField;
	private javax.swing.JButton submitButton;
	private javax.swing.JLabel xColLabel;
	private javax.swing.JTextField xColTextField;
	private javax.swing.JLabel yColLabel;
	private javax.swing.JTextField yColTextField;
	private javax.swing.JLabel zColLabel;
	private javax.swing.JTextField zColTextField;

}
