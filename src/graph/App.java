/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graph;

import gui.Canvas;
import gui.Graph;
import java.io.File;
import lib.ExtensionLoader;
import types.IGraph;
import types.ITool;
import java.util.List;
import static lib.FileUtil.findFiles;
import lib.Util;
import tools.bezier.Bezier;
import tools.line.Line;
import tools.draw.Draw;
import tools.eraser.Eraser;
import tools.selection.Selection;

/**
 *
 * @author dpf
 */
public class App {

    private static final String APP_NAME = "Graph";
    private static final double STARTING_SIZE_FACTOR = 0.7;

    public static final ITool[] BASIC_TOOLS = {new Selection(), new Draw(), new Line(), new Eraser()};
    public static final ITool[] MORE_TOOLS = {new Bezier()};

    private static final String TOOLS_PATH = "tools/";
    private static final String TOOLS_EXT = ".jar";
    private static final String TOOLS_CLASSNAME = "tool.Tool";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        App app = new App();
        Canvas canvas = new Canvas();
        Graph graph = new Graph(canvas);
        canvas.setGraph(graph);

        // Load tools
        app.loadBasicTools(graph);
        app.loadExtensionTools(graph);

        // Start the main frame
        graph.setSize((int) (Util.getScreenWidth() * STARTING_SIZE_FACTOR), (int) (Util.getScreenHeight() * STARTING_SIZE_FACTOR));
        graph.setLocationRelativeTo(null);
        graph.setTitle(APP_NAME);
        graph.setVisible(true);
    }

    public void loadBasicTools(IGraph graph) {
        for (ITool t : BASIC_TOOLS) {
            graph.addToToolbar(t);
        }
        //TODO : remove this
        for (ITool t : MORE_TOOLS) {
            graph.addToToolbar(t);
        }
    }

    public void loadExtensionTools(IGraph graph) {
        List<File> files = null;
        try{
        files = findFiles(TOOLS_PATH, TOOLS_EXT);
        }catch(Exception e){
            
        }
        ExtensionLoader<ITool> el;
        if(files != null){
            for (File s : files) {
                el = new ExtensionLoader<>(TOOLS_PATH + s.getName() + TOOLS_EXT, TOOLS_CLASSNAME);
                ITool t = el.newInstance();
                graph.addToToolbar(t);
            }
        }
    }
    

}
