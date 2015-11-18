#!/usr/bin/env bash

cp /home/airalcorn2/Dropbox/ScatterPlot3D/ScatterPlot3D/out/artifacts/ScatterPlot3D_jar/ScatterPlot3D.jar /home/airalcorn2/Dropbox/ScatterPlot3D/Builds/Linux/x86/main
cd /home/airalcorn2/Dropbox/ScatterPlot3D/Builds/Linux/x86
jar -cvfm ../one-jar.jar boot-manifest.mf .
mv /home/airalcorn2/Dropbox/ScatterPlot3D/Builds/Linux/one-jar.jar /home/airalcorn2/Dropbox/ScatterPlot3D/Builds/Linux/ScatterPlot3D-linux-x86.jar

cp /home/airalcorn2/Dropbox/ScatterPlot3D/ScatterPlot3D/out/artifacts/ScatterPlot3D_jar/ScatterPlot3D.jar /home/airalcorn2/Dropbox/ScatterPlot3D/Builds/Linux/x86_64/main
cd /home/airalcorn2/Dropbox/ScatterPlot3D/Builds/Linux/x86_64
jar -cvfm ../one-jar.jar boot-manifest.mf .
mv /home/airalcorn2/Dropbox/ScatterPlot3D/Builds/Linux/one-jar.jar /home/airalcorn2/Dropbox/ScatterPlot3D/Builds/Linux/ScatterPlot3D-linux-x86_64.jar

cp /home/airalcorn2/Dropbox/ScatterPlot3D/ScatterPlot3D/out/artifacts/ScatterPlot3D_jar/ScatterPlot3D.jar /home/airalcorn2/Dropbox/ScatterPlot3D/Builds/Windows/x86/main
cd /home/airalcorn2/Dropbox/ScatterPlot3D/Builds/Windows/x86
jar -cvfm ../one-jar.jar boot-manifest.mf .
mv /home/airalcorn2/Dropbox/ScatterPlot3D/Builds/Windows/one-jar.jar /home/airalcorn2/Dropbox/ScatterPlot3D/Builds/Windows/ScatterPlot3D-windows-x86.jar

cp /home/airalcorn2/Dropbox/ScatterPlot3D/ScatterPlot3D/out/artifacts/ScatterPlot3D_jar/ScatterPlot3D.jar /home/airalcorn2/Dropbox/ScatterPlot3D/Builds/Windows/x86_64/main
cd /home/airalcorn2/Dropbox/ScatterPlot3D/Builds/Windows/x86_64
jar -cvfm ../one-jar.jar boot-manifest.mf .
mv /home/airalcorn2/Dropbox/ScatterPlot3D/Builds/Windows/one-jar.jar /home/airalcorn2/Dropbox/ScatterPlot3D/Builds/Windows/ScatterPlot3D-windows-x86_64.jar
