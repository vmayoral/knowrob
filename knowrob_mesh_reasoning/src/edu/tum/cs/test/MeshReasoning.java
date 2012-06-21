/*******************************************************************************
 * Copyright (c) 2012 Stefan Profanter. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the GNU Public License v3.0 which accompanies
 * this distribution, and is available at http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors: Stefan Profanter - initial API and implementation, Year: 2012
 ******************************************************************************/
package edu.tum.cs.test;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JFrame;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import edu.tum.cs.vis.model.ItemModel;
import edu.tum.cs.vis.model.uima.analyzer.FlatSurfaceAnalyzer;
import edu.tum.cs.vis.model.uima.analyzer.MeshAnalyzer;
import edu.tum.cs.vis.model.uima.analyzer.NeighborAnalyzer;
import edu.tum.cs.vis.model.uima.cas.MeshCas;
import edu.tum.cs.vis.model.view.MeshReasoningView;
import edu.tum.cs.vis.model.view.MeshReasoningViewControl;

/**
 * Main test class for testing MeshReasoning
 * 
 * @author Stefan Profanter
 * 
 */
public class MeshReasoning {

	/**
	 * Log4j logger
	 */
	private static Logger	logger	= Logger.getRootLogger();

	/**
	 * Main Method loading the mesh, drawing it and starting the analyzer
	 * 
	 * @param args
	 *            command line arguments
	 */
	public static void main(String[] args) {
		DOMConfigurator.configureAndWatch("log4j.xml", 60 * 1000);

		logger.info("MeshReasoning started");

		// ItemModel model = new ItemModel("/home/stefan/simple_dome.kmz");
		// ItemModel model = new ItemModel(
		// "/home/stefan/ros/knowrob/knowrob_cad_models/models/collada/hospital_bed.kmz");
		// ItemModel model = new ItemModel("/home/stefan/Downloads/triangle.dae");
		// ItemModel model = new ItemModel("/home/stefan/Downloads/saintpeter.kmz");
		ItemModel model = new ItemModel("/home/stefan/CoTeSys/cups/cup2.kmz");
		// ItemModel model = new ItemModel("/home/stefan/Downloads/cube.kmz");
		if (!model.parseModel()) {
			throw new RuntimeException("Couldn't parse model. Maybe path to model is wrong.");
		}

		MeshCas cas = new MeshCas();

		/*Group g = new Group();
		g.setMesh(new Mesh());
		
		Triangle t = new Triangle();
		Appearance app = new Appearance();
		app.setColourFill(new Color(0,255,0));
		t.setAppearance(app);
		t.getPosition()[0] = new Point3f(1,4,0);
		t.getPosition()[1] = new Point3f(5,4,0);
		t.getPosition()[2] = new Point3f(2,1,1);
		
		t.updateNormalVector();
		
		g.getMesh().triangles.add(t);*/

		cas.setGroup(model.getParser().getGroup());

		// cas.setGroup(g);

		JFrame frame = new JFrame();
		frame.setMinimumSize(new Dimension(800, 600));
		frame.setSize(1324, 768);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Mesh reasoning view");
		frame.setLocationRelativeTo(null);

		ArrayList<MeshAnalyzer> analyzer = new ArrayList<MeshAnalyzer>();

		MeshReasoningView mrv = new MeshReasoningView();
		MeshReasoningViewControl control = new MeshReasoningViewControl(cas, analyzer);
		mrv.setControl(control);
		mrv.init();

		mrv.getCasList().add(cas);

		frame.setLayout(new BorderLayout());
		frame.getContentPane().add(mrv, BorderLayout.CENTER);
		frame.getContentPane().add(control, BorderLayout.LINE_END);
		frame.setVisible(true);

		NeighborAnalyzer na = new NeighborAnalyzer();
		FlatSurfaceAnalyzer fsa = new FlatSurfaceAnalyzer();
		analyzer.add(na);
		analyzer.add(fsa);

		na.process(cas);

		fsa.process(cas);

	}
}