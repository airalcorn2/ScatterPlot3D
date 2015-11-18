1. In IntelliJ: File -> Project Structure -> Artifacts -> + -> Extracted Directory -> <code>j3dcore.jar</code>; <code>j3dutils.jar</code>; <code>vecmath.jar</code>

2. In IntelliJ: Build -> Build Artifacts...

3. Delete <code>SUN_MICR.RSA</code> and <code>SUN_MICR.SF</code> from the <code>META-INF</code> directory in <code>ScatterPlot3D.jar</code>.

4. Create a root directory for each target build. In each root directory, create <code>main</code> and <code>binlib</code> sub-directories.

5. Copy <code>ScatterPlot3D.jar</code> into <code>/path/to/root/main/</code> and copy the appropriate native libraries into <code>/path/to/root/binlib/</code>.

6. Unjar <code>one-jar-boot-0.97.jar</code> in the root directory, and delete the <code>src</code> directory.

7. Edit <code>boot-manifest.mf</code> so that it reads:

    Manifest-Version: 1.0
    Main-Class: com.simontuffs.onejar.Boot
    One-Jar-Main-Class: Run

8. <code>cd root</code>

9. <code>jar -cvfm ../one-jar.jar boot-manifest.mf .</code>
    (Note: make sure you include the period in the above command)
