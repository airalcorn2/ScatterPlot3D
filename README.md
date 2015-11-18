1. In IntelliJ: File -> Project Structure -> Artifacts -> + -> Extracted Directory -> j3dcore.jar; j3dutils.jar; vecmath.jar

2. In IntelliJ: Build -> Build Artifacts...

3. Delete "SUN_MICR.RSA" and "SUN_MICR.SF" from the "META-INF" direcotry in ScatterPlot3D.jar.

4. Create a root directory for each target build. In each root directory, create "main" and "binlib" sub-directories.

5. Copy ScatterPlot3D.jar into root/main/ and copy the appropriate native libraries into root/binlib/.

6. Unjar one-jar-boot-0.97.jar in the root directory, and delete the "src" directory.

7. Edit "boot-manifest.mf" so that it reads:

    Manifest-Version: 1.0
    Main-Class: com.simontuffs.onejar.Boot
    One-Jar-Main-Class: Run

8. cd root

9. jar -cvfm ../one-jar.jar boot-manifest.mf .
    (Note: make sure you include the period in the above command)
