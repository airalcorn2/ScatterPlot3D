import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

public class ScatterPlot3DData {

	int N, xCol, yCol, zCol, colTotal, groupCol;
	boolean groupColPresent;
	File dataFile, colorFile;

	double xMax, yMax, zMax, xMin, yMin, zMin;

	ArrayList<String> rawData, groups;
	String[][] data;
	double[][] raw;
	double[][] normal;

	Hashtable<String, float[]> groupColors;

	public ScatterPlot3DData(int xColNo, int yColNo, int zColNo,
			int theGroupCol, File theDataFile, File theColorFile,
			boolean isGroupColPresent) {

		xCol = xColNo;
		yCol = yColNo;
		zCol = zColNo;

		dataFile = theDataFile;
		colorFile = theColorFile;

		readRawData();
		createStringArray();
		createDoubleArray();
		createNormalArray();

		groupColPresent = isGroupColPresent;

		if (groupColPresent) {

			groupCol = theGroupCol;
			countGroups();
			assignGroupColors();

		}
	}

	public void readRawData() {

		Scanner csv;

		try {

			csv = new Scanner(dataFile);

			// Get column headers
			ScatterPlot3DGUI.headers = csv.nextLine().split(",");

			colTotal = ScatterPlot3DGUI.headers.length;

			// Read in data from .csv file
			rawData = new ArrayList<String>();
			while (csv.hasNextLine()) {
				rawData.add(csv.nextLine());
			}

			// Sample size
			N = rawData.size();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void createStringArray() {

		data = new String[N][colTotal];

		for (int i = 0; i < N; i++) {

			String[] thisRow = rawData.get(i).split(",");
			for (int j = 0; j < colTotal; j++)
				data[i][j] = thisRow[j];

		}
	}

	public void createDoubleArray() {

		xMax = Double.NEGATIVE_INFINITY;
		yMax = Double.NEGATIVE_INFINITY;
		zMax = Double.NEGATIVE_INFINITY;

		xMin = Double.POSITIVE_INFINITY;
		yMin = Double.POSITIVE_INFINITY;
		zMin = Double.POSITIVE_INFINITY;

		raw = new double[N][3];

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < colTotal; j++) {

				if (j == xCol) {

					raw[i][0] = Double.parseDouble(data[i][j]);
					xMax = Math.max(xMax, raw[i][0]);
					xMin = Math.min(xMin, raw[i][0]);

				} else if (j == yCol) {

					raw[i][1] = Double.parseDouble(data[i][j]);
					yMax = Math.max(yMax, raw[i][1]);
					yMin = Math.min(yMin, raw[i][1]);

				} else if (j == zCol) {

					raw[i][2] = Double.parseDouble(data[i][j]);
					zMax = Math.max(zMax, raw[i][2]);
					zMin = Math.min(zMin, raw[i][2]);

				}
			}
		}
	}

	public void createNormalArray() {

		normal = new double[N][3];

		double xRange = xMax - xMin;
		double yRange = yMax - yMin;
		double zRange = zMax - zMin;

		for (int i = 0; i < N; i++) {

			double min = 1;
			double range = 1;

			for (int j = 0; j < 3; j++) {

				switch (j) {

				case 0:
					min = xMin;
					range = xRange;
					break;

				case 1:
					min = yMin;
					range = yRange;
					break;

				case 2:
					min = zMin;
					range = zRange;
					break;
				}

				normal[i][j] = (2 * (raw[i][j] - min) / range) - 1;

			}
		}
	}

	public void countGroups() {

		groups = new ArrayList<String>();

		for (int i = 0; i < N; i++)
			if (!groups.contains(data[i][groupCol]))
				groups.add(data[i][groupCol]);

	}

	public void assignGroupColors() {

		groupColors = new Hashtable<String, float[]>();

		if (colorFile == null) {

			for (String group : groups) {

				float[] floatColors = { (float) Math.random(),
						(float) Math.random(), (float) Math.random() };
				groupColors.put(group, floatColors);

			}

		} else {

			Scanner csv;

			try {

				csv = new Scanner(colorFile);

				for (int i = 0; i < groups.size(); i++) {

					String[] parts = csv.nextLine().split(",");

					String group;
					group = parts[0];

					float[] rgb = new float[3];
					rgb[0] = Float.parseFloat(parts[1]);
					rgb[1] = Float.parseFloat(parts[2]);
					rgb[2] = Float.parseFloat(parts[3]);

					groupColors.put(group, rgb);

				}

			} catch (FileNotFoundException e) {

				e.printStackTrace();

			}
		}
	}
}