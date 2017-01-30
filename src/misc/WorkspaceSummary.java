package misc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/*
 * Created on Jan 29, 2017
 * By Kenneth Evans, Jr.
 */

public class WorkspaceSummary
{
    public static final String LS = System.getProperty("line.separator");
    public static final String COMMA = ",";
    private static final String DEFAULT_WORKSPACE = "C:/eclipseWorkspaces/Work";
    private static final String DEFAULT_OUTPUT_FILE = "C:/Users/evans/Documents/Personal/Calc/Eclipse Work Project Summary.csv";

    public static List<Summary> getData() {
        File workDir = new File(DEFAULT_WORKSPACE);
        File[] projects = workDir.listFiles();
        File[] files;
        File corePrefs;
        File uiPrefs;
        List<Summary> projectsList = new ArrayList<Summary>();
        Summary summary;
        for(File project : projects) {
            summary = new Summary(project.getName());
            if(project.getName().startsWith(".")) {
                continue;
            }
            files = project.listFiles();
            if(files == null) {
                continue;
            }
            for(File file : files) {
                if(file.getName().equals(".settings")) {
                    uiPrefs = new File(file, "org.eclipse.jdt.ui.prefs");
                    if(uiPrefs.exists()) {
                        summary.setUiPrefs("ui.prefs");
                    }
                    corePrefs = new File(file, "org.eclipse.jdt.core.prefs");
                    if(corePrefs.exists()) {
                        summary.setCorePrefs("core.prefs");
                        setCorePrefs(summary, corePrefs);
                    }
                } else if(file.getName().toLowerCase().endsWith(".jar")) {
                    summary.appendJars(file.getName());
                } else if(file.getName().equals(".classpath")) {
                    setClasspath(summary, file);
                } else if(file.getName().equals(".project")) {
                    setProject(summary, file);
                } else if(file.getName().equals("plugin.xml")) {
                    summary.setPlugin(file.getName());
                } else if(file.getName().equals("plugin.properties")) {
                    summary.setProperties(file.getName());
                } else if(file.getName().endsWith(".product")) {
                    summary.setProduct(".product");
                } else if(file.getName().endsWith(".feature")) {
                    summary.setFeature(".feature");
                } else if(file.getName().equals("META-INF")) {
                    summary.setManifest("META-INF");
                } else if(file.getName().equals(".svn")) {
                    summary.setSvn("SVN");
                } else if(file.getName().equals(".git")) {
                    summary.setGit("Git");
                }
            }
            projectsList.add(summary);
        }
        Collections.sort(projectsList, new Comparator<Summary>() {
            public int compare(Summary o1, Summary o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        return projectsList;
    }

    public static void setClasspath(Summary summary, File file) {
        BufferedReader in = null;
        final String test = "StandardVMType/";
        try {
            in = new BufferedReader(new FileReader(file));
            String line;
            while((line = in.readLine()) != null) {
                if(line.contains("org.eclipse.jdt.launching.JRE_CONTAINER")) {
                    int index = line.indexOf(test);
                    if(index < 0) {
                        summary.setJ2se("Default");
                    } else {
                        String start = line.substring(index + test.length());
                        int end = start.indexOf("\"/>");
                        if(end < 0) end = start.length();
                        start = start.substring(0, end);
                        end = start.indexOf("\">");
                        if(end < 0) end = start.length();
                        start = start.substring(0, end);
                        summary.setJ2se(start.substring(0, end));
                    }
                }
            }
        } catch(

        Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if(in != null) in.close();
            } catch(IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void setProject(Summary summary, File file) {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(file));
            String line;
            while((line = in.readLine()) != null) {
                if(line.toLowerCase().contains("<nature>")) {
                    int index1 = line.indexOf("<");
                    int index2 = line.lastIndexOf(">");
                    if(index1 > 0 && index2 > 0) {
                        String text = line.toLowerCase()
                            .substring(index1, index2 + 1)
                            .replaceAll("<nature>", "")
                            .replaceAll("</nature>", "");
                        summary.appendNatures(text);
                    }
                }
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if(in != null) in.close();
            } catch(IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void setCorePrefs(Summary summary, File file) {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(file));
            String line;
            while((line = in.readLine()) != null) {
                if(line.contains("targetPlatform")) {
                    summary.setTarget(getValue(line));
                } else if(line.contains("compliance")) {
                    summary.setCompliance(getValue(line));
                } else if(line.contains("source")) {
                    summary.setSource(getValue(line));
                }
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if(in != null) in.close();
            } catch(IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Gets the value from a line of the form xxx=value;
     * 
     * @param line
     * @return The value.
     */
    public static String getValue(String line) {
        if(line == null || line.length() < 2) {
            return "";
        }
        int index = line.lastIndexOf("=");
        if(index < 0) {
            return "Not found";
        }
        return line.substring(index + 1, line.length());
    }

    public static void printSummary(PrintWriter writer,
        List<Summary> summaryList) {
        // Headings
        String[] headings = new String[] {"Name", "J2SE", "Target",
            "Compliance", "Source", "UI Prefs", "Core Prefs", "Manifest",
            "Plug-in", "Product", "Properties", "Feature", "SVN", "Git", "Jars",
            "Natures"};
        for(String heading : headings) {
            writer.print(heading + COMMA);
        }
        writer.println();
        // Projects
        if(summaryList == null) {
            writer.println("No projects");
        }
        for(Summary summary : summaryList) {
            writer.print(summary.getName() + COMMA);
            writer.print(summary.getJ2se() + COMMA);
            writer.print(summary.getTarget() + COMMA);
            writer.print(summary.getCompliance() + COMMA);
            writer.print(summary.getSource() + COMMA);
            writer.print(summary.getUiPrefs() + COMMA);
            writer.print(summary.getCorePrefs() + COMMA);
            writer.print(summary.getManifest() + COMMA);
            writer.print(summary.getPlugin() + COMMA);
            writer.print(summary.getProduct() + COMMA);
            writer.print(summary.getProperties() + COMMA);
            writer.print(summary.getFeature() + COMMA);
            writer.print(summary.getSvn() + COMMA);
            writer.print(summary.getGit() + COMMA);
            writer.print(summary.getJars() + COMMA);
            writer.print(summary.getNatures() + COMMA);
            writer.println();
        }
        writer.flush();
    }

    public static void main(String[] args) {
        List<Summary> projectsList = getData();
        printSummary(new PrintWriter(System.out, true), projectsList);
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new FileWriter(DEFAULT_OUTPUT_FILE));
            printSummary(new PrintWriter(new FileWriter(DEFAULT_OUTPUT_FILE)),
                projectsList);
        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
            if(writer != null) {
                writer.close();
            }
        }
        System.out.println();
        System.out.println("All Done");
    }

    private static class Summary
    {
        private String name = "";
        private String manifest = "";
        private String plugin = "";
        private String properties = "";
        private String product = "";
        private String feature = "";
        private String target = "";
        private String compliance = "";
        private String source = "";
        private String uiPrefs = "";
        private String corePrefs = "";
        private String svn = "";
        private String git = "";
        private String j2se = "";
        private String jars = "";
        private String natures = "";

        /**
         * @return The value of properties.
         */
        public String getProperties() {
            return properties;
        }

        /**
         * @param properties The new value for properties.
         */
        public void setProperties(String properties) {
            this.properties = properties;
        }

        Summary(String name) {
            this.name = name;
        }

        /**
         * @return The value of uiPrefs.
         */
        public String getUiPrefs() {
            return uiPrefs;
        }

        /**
         * @param uiPrefs The new value for uiPrefs.
         */
        public void setUiPrefs(String uiPrefs) {
            this.uiPrefs = uiPrefs;
        }

        /**
         * @return The value of corePrefs.
         */
        public String getCorePrefs() {
            return corePrefs;
        }

        /**
         * @param corePrefs The new value for corePrefs.
         */
        public void setCorePrefs(String corePrefs) {
            this.corePrefs = corePrefs;
        }

        /**
         * @return The value of name.
         */
        public String getName() {
            return name;
        }

        /**
         * @return The value of manifest.
         */
        public String getManifest() {
            return manifest;
        }

        /**
         * @param manifest The new value for manifest.
         */
        public void setManifest(String manifest) {
            this.manifest = manifest;
        }

        /**
         * @return The value of plugin.
         */
        public String getPlugin() {
            return plugin;
        }

        /**
         * @param plugin The new value for plugin.
         */
        public void setPlugin(String plugin) {
            this.plugin = plugin;
        }

        /**
         * @return The value of target.
         */
        public String getTarget() {
            return target;
        }

        /**
         * @param target The new value for target.
         */
        public void setTarget(String target) {
            this.target = target;
        }

        /**
         * @return The value of compliance.
         */
        public String getCompliance() {
            return compliance;
        }

        /**
         * @param compliance The new value for compliance.
         */
        public void setCompliance(String compliance) {
            this.compliance = compliance;
        }

        /**
         * @return The value of source.
         */
        public String getSource() {
            return source;
        }

        /**
         * @param source The new value for source.
         */
        public void setSource(String source) {
            this.source = source;
        }

        /**
         * @return The value of product.
         */
        public String getProduct() {
            return product;
        }

        /**
         * @param product The new value for product.
         */
        public void setProduct(String product) {
            this.product = product;
        }

        /**
         * @return The value of svn.
         */
        public String getSvn() {
            return svn;
        }

        /**
         * @param svn The new value for svn.
         */
        public void setSvn(String svn) {
            this.svn = svn;
        }

        /**
         * @return The value of git.
         */
        public String getGit() {
            return git;
        }

        /**
         * @param git The new value for git.
         */
        public void setGit(String git) {
            this.git = git;
        }

        /**
         * @return The value of feature.
         */
        public String getFeature() {
            return feature;
        }

        /**
         * @param feature The new value for feature.
         */
        public void setFeature(String feature) {
            this.feature = feature;
        }

        /**
         * @return The value of j2se.
         */
        public String getJ2se() {
            return j2se;
        }

        /**
         * @param j2se The new value for j2se.
         */
        public void setJ2se(String j2se) {
            this.j2se = j2se;
        }

        /**
         * @return The value of jars.
         */
        public String getJars() {
            return jars;
        }

        /**
         * @param jars The new value for jars.
         */
        public void appendJars(String jars) {
            if(this.jars.isEmpty()) {
                this.jars = jars;
            } else {
                this.jars += " " + jars;
            }
        }

        /**
         * @return The value of natures.
         */
        public String getNatures() {
            return natures;
        }

        /**
         * @param natures The new value for natures.
         */
        public void appendNatures(String natures) {
            if(this.natures.isEmpty()) {
                this.natures = natures;
            } else {
                this.natures += " " + natures;
            }
        }

    }

}
