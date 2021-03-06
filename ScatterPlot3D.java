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

import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseWheelZoom;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.picking.PickCanvas;
import com.sun.j3d.utils.picking.PickResult;
import com.sun.j3d.utils.universe.SimpleUniverse;

import javax.imageio.ImageIO;
import javax.media.j3d.*;
import javax.swing.*;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ScatterPlot3D extends MouseAdapter {

    SimpleUniverse universe;
    TransformGroup objRotate;
    BranchGroup sceneBranchGroup;
    PickCanvas pickCanvas;
    Canvas3D offScreenCanvas;
    JPopupMenu saveMenu;
    JMenuItem saveMenuItem;
    int OFF_SCREEN_SCALE = 3;
    int viewpointCount;
    ScatterPlot3DData data;
    File dataFile, colorFile;
    int N, xCol, yCol, zCol, groupCol, totalCol, groupNum;
    float sphereRadius;
    boolean groupColPresent;
    Sphere[] spheres;
    Color3f[] colors;

    public void showPlot(File theColorFile) {

        colorFile = theColorFile;

        data = new ScatterPlot3DData(xCol, yCol, zCol, groupCol, dataFile,
                colorFile, groupColPresent);

        N = data.N;
        if (groupColPresent)
            groupNum = data.groups.size();
        totalCol = data.colTotal;

        viewpointCount = 1;

        /*************************** Create Universe **********************/

        universe = new SimpleUniverse(ScatterPlot3DGUI.plotCanvas);
        createSceneBranchGroup();
        universe.getViewingPlatform().setNominalViewingTransform();
        universe.addBranchGraph(sceneBranchGroup);
        createOffscreenCanvas();
        universe.getViewer().getView().addCanvas3D(offScreenCanvas);

        /*************************** Save Menu ****************************/

        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
        saveMenu = new JPopupMenu();
        saveMenuItem = new JMenuItem("Save viewpoint");
        saveMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveMenuItemActionPerformed(evt);
            }
        });
        saveMenu.add(saveMenuItem);

        pickCanvas = new PickCanvas(ScatterPlot3DGUI.plotCanvas,
                sceneBranchGroup);
        pickCanvas.setMode(PickCanvas.GEOMETRY);
        ScatterPlot3DGUI.plotCanvas.addMouseListener(this);

    }

    private void createSceneBranchGroup() {

        sceneBranchGroup = new BranchGroup();
        sceneBranchGroup.setCapability(BranchGroup.ALLOW_DETACH);

        objRotate = new TransformGroup();
        objRotate.setCapability(Group.ALLOW_CHILDREN_EXTEND);
        objRotate.setCapability(Group.ALLOW_CHILDREN_WRITE);
        objRotate.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        objRotate.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        objRotate.setCapability(TransformGroup.ENABLE_PICK_REPORTING);

        sceneBranchGroup.addChild(objRotate);

        addAxes();
        addDataPoints();

        /*************************** Lighting ****************************/

        Color3f lightColor = new Color3f(1.0f, 1.0f, 1.0f);
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0),
                100.0);
        Vector3f lightDirection = new Vector3f(4.0f, -7.0f, -12.0f);
        DirectionalLight light = new DirectionalLight(lightColor,
                lightDirection);
        light.setInfluencingBounds(bounds);
        sceneBranchGroup.addChild(light);

        /**************************** Mouse *****************************/

        MouseRotate myMouseRotate = new MouseRotate();
        myMouseRotate.setTransformGroup(objRotate);
        myMouseRotate.setSchedulingBounds(new BoundingSphere());
        sceneBranchGroup.addChild(myMouseRotate);

        MouseWheelZoom myMouseZoom = new MouseWheelZoom();
        myMouseZoom.setTransformGroup(objRotate);
        myMouseZoom.setSchedulingBounds(new BoundingSphere());
        sceneBranchGroup.addChild(myMouseZoom);

    }

    // For saving viewpoints
    private void createOffscreenCanvas() {

        GraphicsConfiguration config = SimpleUniverse
                .getPreferredConfiguration();
        offScreenCanvas = new Canvas3D(config, true);

        Screen3D sOn = ScatterPlot3DGUI.plotCanvas.getScreen3D();
        Screen3D sOff = offScreenCanvas.getScreen3D();

        Dimension dim = sOn.getSize();
        dim.width *= OFF_SCREEN_SCALE;
        dim.height *= OFF_SCREEN_SCALE;

        sOff.setSize(dim);
        sOff.setPhysicalScreenWidth(sOn.getPhysicalScreenWidth()
                * OFF_SCREEN_SCALE);
        sOff.setPhysicalScreenHeight(sOn.getPhysicalScreenHeight()
                * OFF_SCREEN_SCALE);

    }

    private void addDataPoints() {

        spheres = new Sphere[N];
        colors = new Color3f[N];

        for (int i = 0; i < N; i++) {

            Sphere sphere = new Sphere(sphereRadius);

            Color3f col = null;

            /******************** Data Point Color ***********************/

            if (groupColPresent) {

                String group = data.data[i][groupCol];

                if (data.groupColors.containsKey(group))
                    col = new Color3f(data.groupColors.get(group));
                else
                    ScatterPlot3DGUI.detailTextArea
                            .append("Groups in data file do not match groups in colors file.\n\n");

            } else {

                col = new Color3f((float) Math.random(), (float) Math.random(),
                        (float) Math.random());

            }

            colors[i] = col;

            /****************** Data Point Appearance **********************/

            Material material = new Material();
            material.setCapability(Material.ALLOW_COMPONENT_WRITE);
            material.setDiffuseColor(col);

            Appearance ap = new Appearance();
            ap.setCapability(Appearance.ALLOW_MATERIAL_WRITE);
            ap.setCapability(Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_WRITE);
            ap.setMaterial(material);

            sphere.setAppearance(ap);

            // Assign index to data point
            sphere.setUserData(i);
            spheres[i] = sphere;

            // Get normalized coordinates for data point
            float x = (float) data.normal[i][0];
            float y = (float) data.normal[i][1];
            float z = (float) data.normal[i][2];

            /******************* Data Point Location ***********************/

            TransformGroup tg = new TransformGroup();
            Transform3D transform = new Transform3D();

            Vector3f vector = new Vector3f(x, y, z);
            transform.setTranslation(vector);
            tg.setTransform(transform);
            tg.addChild(sphere);
            objRotate.addChild(tg);

        }
    }

    private void addAxes() {

        /************************* Axes Colors ***************************/

        Appearance xAxisAppearance = new Appearance();
        ColoringAttributes xAxisColor = new ColoringAttributes(1.0f, 0.0f,
                0.0f, ColoringAttributes.NICEST);
        xAxisAppearance.setColoringAttributes(xAxisColor);

        Appearance yAxisAppearance = new Appearance();
        ColoringAttributes yAxisColor = new ColoringAttributes(0.0f, 1.0f,
                0.0f, ColoringAttributes.NICEST);
        yAxisAppearance.setColoringAttributes(yAxisColor);

        Appearance zAxisAppearance = new Appearance();
        ColoringAttributes zAxisColor = new ColoringAttributes(0.0f, 0.0f,
                1.0f, ColoringAttributes.NICEST);
        zAxisAppearance.setColoringAttributes(zAxisColor);

        /************************* Create Axes ****************************/

        Cylinder xAxis = new Cylinder(0.005f, 3.0f, xAxisAppearance);
        xAxis.setUserData(-1);

        Cylinder yAxis = new Cylinder(0.005f, 3.0f, yAxisAppearance);
        yAxis.setUserData(-1);

        Cylinder zAxis = new Cylinder(0.005f, 3.0f, zAxisAppearance);
        zAxis.setUserData(-1);

        /********************** Transform X-Axis *************************/

        TransformGroup xAxisTransformGroup = new TransformGroup();
        Transform3D xAxisTransform = new Transform3D();

        xAxisTransform.rotZ(Math.PI / 2);
        xAxisTransformGroup.setTransform(xAxisTransform);
        xAxisTransformGroup.addChild(xAxis);
        objRotate.addChild(xAxisTransformGroup);

        /******************* Don't Transform Y-Axis **********************/

        objRotate.addChild(yAxis);

        /********************** Transform Z-Axis *************************/

        TransformGroup zAxisTransformGroup = new TransformGroup();
        Transform3D zAxisTransform = new Transform3D();

        zAxisTransform.rotX(Math.PI / 2);
        zAxisTransformGroup.setTransform(zAxisTransform);
        zAxisTransformGroup.addChild(zAxis);
        objRotate.addChild(zAxisTransformGroup);

    }

    // Data point selection
    public void mouseClicked(MouseEvent e) {

        if (e.getButton() == MouseEvent.BUTTON1) {

            pickCanvas.setShapeLocation(e);

            PickResult result = pickCanvas.pickClosest();

            // If nothing is selected, restore all data points
            if (result == null) {

                for (int i = 0; i < N; i++) {

                    spheres[i].getAppearance().getMaterial()
                            .setDiffuseColor(colors[i]);
                    TransparencyAttributes t_attr = new TransparencyAttributes(
                            TransparencyAttributes.NONE, 0.0f);

                    spheres[i].getAppearance()
                            .setTransparencyAttributes(t_attr);

                }

                ScatterPlot3DGUI.detailTextArea.append("Nothing selected.\n\n");

            } else {

                Primitive p = (Primitive) result.getNode(PickResult.PRIMITIVE);

                Shape3D s = (Shape3D) result.getNode(PickResult.SHAPE3D);

                if (p != null) {

                    int index = Integer.parseInt(p.getUserData().toString());

                    if (index == -1) {

                        ScatterPlot3DGUI.detailTextArea
                                .append("Axis selected.\n");

                    } else {

                        for (int i = 0; i < totalCol; i++) {

                            ScatterPlot3DGUI.detailTextArea
                                    .append(ScatterPlot3DGUI.headers[i] + ": "
                                            + data.data[index][i] + "\n");
                        }
                    }

                    // Adjust label
                    ScatterPlot3DGUI.detailTextArea.append("\n");
                    ScatterPlot3DGUI.detailTextArea
                            .setCaretPosition(ScatterPlot3DGUI.detailTextArea
                                    .getDocument().getLength());

                } else if (s != null) {

                    System.out.print("s: ");
                    System.out.println(s.getClass().getName());

                } else {

                    System.out.println("null");

                }
            }

        } else if (e.getButton() == MouseEvent.BUTTON3) {

            saveMenu.show(e.getComponent(), e.getX(), e.getY());

        }
    }

    ArrayList<Integer> multiGroupSearch(String search, int searchCol) {

        search = search.replace("{", "");
        search = search.replace("}", "");

        String[] groups = search.split(",");

        ArrayList<Integer> foundIndices = new ArrayList<Integer>();

        for (int i = 0; i < N; i++)
            for (String group : groups)
                if (group.equals(data.data[i][searchCol])) {

                    foundIndices.add(i);
                    break;

                }

        if (foundIndices.isEmpty())
            ScatterPlot3DGUI.detailTextArea.append("Could not find \"" + search
                    + "\" in " + ScatterPlot3DGUI.headers[searchCol]
                    + " column.\n\n");

        return foundIndices;

    }

    void multiGroupSearch(String search, int searchCol, boolean[] indices) {

        boolean found = false;

        search = search.replace("{", "");
        search = search.replace("}", "");

        String[] groups = search.split(",");

        for (int i = 0; i < N; i++) {

            if (!indices[i]) continue;

            boolean isFound = false;

            for (String group : groups)
                if (group.equals(data.data[i][searchCol])) {

                    found = true;
                    isFound = true;
                    break;

                }

            indices[i] = isFound;
        }


        if (!found)
            ScatterPlot3DGUI.detailTextArea.append("Could not find \"" + search
                    + "\" in " + ScatterPlot3DGUI.headers[searchCol]
                    + " column that also meets other search criteria.\n\n");

    }

    ArrayList<Integer> numberRangeSearch(String search, int searchCol) {

        search = search.replace("[", "");
        search = search.replace("]", "");

        String[] numberRanges = search.split(",");

        ArrayList<Integer> foundIndices = new ArrayList<Integer>();

        for (int i = 0; i < N; i++)
            for (String numberRange : numberRanges) {

                String[] numbers = numberRange.split("-");

                if (numbers.length != 2) {

                    ScatterPlot3DGUI.detailTextArea.append("One or more of the search terms was not a range of two numbers.\n\n");
                    return foundIndices;

                }

                double low = Double.parseDouble(numbers[0]);
                double high = Double.parseDouble(numbers[1]);

                if (high < low) {

                    double temp = low;
                    low = high;
                    high = temp;

                }

                try {

                    if (Double.parseDouble(data.data[i][searchCol]) >= low
                            && Double.parseDouble(data.data[i][searchCol]) <= high) {

                        foundIndices.add(i);
                        break;

                    }

                } catch (NumberFormatException notNum) {

                    ScatterPlot3DGUI.detailTextArea.append(ScatterPlot3DGUI.headers[searchCol]
                            + " column contains non-numbers.\n\n");
                    return foundIndices;

                }
            }

        if (foundIndices.isEmpty())
            ScatterPlot3DGUI.detailTextArea.append("Could not find \"" + search
                    + "\" in " + ScatterPlot3DGUI.headers[searchCol]
                    + " column.\n\n");

        return foundIndices;

    }

    void numberRangeSearch(String search, int searchCol, boolean[] indices) {

        boolean found = false;

        search = search.replace("[", "");
        search = search.replace("]", "");

        String[] numberRanges = search.split(",");

        for (int i = 0; i < N; i++) {

            if (!indices[i]) continue;

            boolean isFound = false;

            for (String numberRange : numberRanges) {

                String[] numbers = numberRange.split("-");

                if (numbers.length != 2) {

                    ScatterPlot3DGUI.detailTextArea.append("One or more of the search terms was not a range of two numbers.\n\n");
                    return;

                }

                double low = Double.parseDouble(numbers[0]);
                double high = Double.parseDouble(numbers[1]);

                if (high < low) {

                    double temp = low;
                    low = high;
                    high = temp;

                }

                try {

                    if (Double.parseDouble(data.data[i][searchCol]) >= low
                            && Double.parseDouble(data.data[i][searchCol]) <= high) {

                        found = true;
                        isFound = true;
                        break;

                    }

                } catch (NumberFormatException notNum) {

                    ScatterPlot3DGUI.detailTextArea.append(ScatterPlot3DGUI.headers[searchCol]
                            + " column contains non-numbers.\n\n");
                    return;

                }
            }

            indices[i] = isFound;

        }

        if (!found)
            ScatterPlot3DGUI.detailTextArea.append("Could not find \"" + search
                    + "\" in " + ScatterPlot3DGUI.headers[searchCol]
                    + " column that also meets other search criteria.\n\n");

    }

    ArrayList<Integer> standardSearch(String search, int searchCol) {

        ArrayList<Integer> foundIndices = new ArrayList<Integer>();

        for (int i = 0; i < N; i++)
            if (search.equals(data.data[i][searchCol]))
                foundIndices.add(i);

        if (foundIndices.isEmpty())
            ScatterPlot3DGUI.detailTextArea.append("Could not find \"" + search
                    + "\" in " + ScatterPlot3DGUI.headers[searchCol]
                    + " column.\n\n");

        return foundIndices;

    }

    void standardSearch(String search, int searchCol, boolean[] indices) {

        boolean found = false;

        for (int i = 0; i < N; i++) {

            if (search.equals(data.data[i][searchCol])) {

                found = true;
                continue;

            }

            indices[i] = false;
        }

        if (!found)
            ScatterPlot3DGUI.detailTextArea.append("Could not find \"" + search
                    + "\" in " + ScatterPlot3DGUI.headers[searchCol]
                    + " column that also meets other search criteria.\n\n");

        return;
    }

    // Search function
    public void find(String search, int searchCol) {

        ArrayList<Integer> foundIndices;

        if (search.startsWith("{"))
            foundIndices = multiGroupSearch(search, searchCol);
        else if (search.startsWith("["))
            foundIndices = numberRangeSearch(search, searchCol);
        else
            foundIndices = standardSearch(search, searchCol);

        if (!foundIndices.isEmpty()) {

            fadePoints(foundIndices);

            // List details of search target
            for (int index : foundIndices) {

                for (int i = 0; i < totalCol; i++)
                    ScatterPlot3DGUI.detailTextArea
                            .append(ScatterPlot3DGUI.headers[i] + ": "
                                    + data.data[index][i] + "\n");

                // Automatically scroll text area to bottom
                ScatterPlot3DGUI.detailTextArea.append("\n");
                ScatterPlot3DGUI.detailTextArea
                        .setCaretPosition(ScatterPlot3DGUI.detailTextArea
                                .getDocument().getLength());

            }
        }
    }

    // Special search function
    public void specialFind(int numHeaders, ArrayList<JTextField> searchFields) {

        boolean[] foundIndices = new boolean[N];
        for (int i = 0; i < N; i++) foundIndices[i] = true;

        for (int i = 0; i < numHeaders; i++) {

            String search = searchFields.get(i).getText();

            if (search.isEmpty())
                continue;
            else if (search.startsWith("{"))
                multiGroupSearch(search, i, foundIndices);
            else if (search.startsWith("["))
                numberRangeSearch(search, i, foundIndices);
            else
                standardSearch(search, i, foundIndices);

        }

        ArrayList<Integer> theIndices = new ArrayList<Integer>();
        for (int i = 0; i < N; i++)
            if (foundIndices[i]) theIndices.add(i);

        if (!theIndices.isEmpty()) {

            fadePoints(theIndices);

            // List details of search target
            for (int index : theIndices) {

                for (int i = 0; i < totalCol; i++)
                    ScatterPlot3DGUI.detailTextArea
                            .append(ScatterPlot3DGUI.headers[i] + ": "
                                    + data.data[index][i] + "\n");

                // Automatically scroll text area to bottom
                ScatterPlot3DGUI.detailTextArea.append("\n");
                ScatterPlot3DGUI.detailTextArea
                        .setCaretPosition(ScatterPlot3DGUI.detailTextArea
                                .getDocument().getLength());

            }
        }
    }

    void fadePoints(ArrayList<Integer> foundIndices) {

        int foundIndicesPointer = 0;

        for (int i = 0; i < N; i++) {

            // Fade points that don't match search
            if (foundIndicesPointer == foundIndices.size() || i != foundIndices.get(foundIndicesPointer)) {

                TransparencyAttributes t_attr = new TransparencyAttributes(
                        TransparencyAttributes.NICEST, 0.99f);

                spheres[i].getAppearance().setTransparencyAttributes(t_attr);

            } else {

                spheres[i].getAppearance().getMaterial()
                        .setDiffuseColor(colors[i]);
                TransparencyAttributes t_attr = new TransparencyAttributes(
                        TransparencyAttributes.NONE, 0.0f);

                spheres[i].getAppearance().setTransparencyAttributes(t_attr);

                foundIndicesPointer++;

            }
        }
    }

    private void saveMenuItemActionPerformed(java.awt.event.ActionEvent evt) {

        try {

            BufferedImage viewImage = new BufferedImage(4000, 4000,
                    BufferedImage.TYPE_INT_ARGB);

            ImageComponent2D buffer = new ImageComponent2D(
                    ImageComponent.FORMAT_RGBA, viewImage);

            offScreenCanvas.setOffScreenBuffer(buffer);
            offScreenCanvas.renderOffScreenBuffer();
            offScreenCanvas.waitForOffScreenRendering();
            viewImage = offScreenCanvas.getOffScreenBuffer().getImage();
            File outputFile = new File("ScatterPlot3DViewpoint"
                    + viewpointCount + ".png");
            ImageIO.write(viewImage, "png", outputFile);
            viewpointCount++;

        } catch (IOException e) {

        }
    }
}