package misc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JOptionPane;

/*
 * Created on Jun 20, 2017
 * By Kenneth Evans, Jr.
 */

public class FindAndroidStudioProjectParameters
{
    private static final String PROJECT_DIR = "C:/AndroidStudioProjects";
    private static final String OUT_DIR = "C:/Scratch/Android Studio";
    public static final String COMMA = ",";
    public static final String ITEM_DELIMITER = "\n";
    private static final String[] KEYS = {"applicationId", "compileSdkVersion",
        "minSdkVersion", "targetSdkVersion", "versionCode", "versionName",
        "buildToolsVersion"};
    private static final String[] VALUES = {"", "", "", "", "", "", "",};
    private static final String[] CVS_HEADINGS = new String[] {"Project Name",
        "gradle.build", "applicationId", "buildToolsVersion",
        "compileSdkVersion", "minSdkVersion", "targetSdkVersion", "versionCode",
        "versionName",};
    private static final String CVS_FILENAME_TEMPLATE = OUT_DIR
        + "/AndroidStudioProjectData-%s.csv";
    public static final String format = "yyyy-MM-dd-hh-mm";
    public static final SimpleDateFormat formatter = new SimpleDateFormat(
        format);
    public static final String LS = System.getProperty("line.separator");

    private static ArrayList<Data> run() {
        ArrayList<Data> dataList = new ArrayList<Data>();
        File projectDir = new File(PROJECT_DIR);
        File[] projects = projectDir.listFiles();
        File[] files, topFiles;
        for(File project : projects) {
            if(!project.isDirectory()) continue;
            topFiles = project.listFiles();
            for(File topFile : topFiles) {
                if(!topFile.isDirectory()) continue;
                files = topFile.listFiles();
                for(File file : files) {
                    if(!file.getName().equals("build.gradle")) continue;
                    Data data = process(file);
                    if(data != null) {
                        dataList.add(data);
                    }
                }
            }
        }
        return dataList;
    }

    private static Data process(File file) {
        Data data = new Data();
        String[] values = VALUES.clone();
        data.setProjectName(file.getParentFile().getParentFile().getName());
        data.setGradleBuildName(
            file.getParentFile().getName() + "/" + file.getName());
//        String[] dataString = VALUES;
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(file));
            String line;
            String key;
            int start;
//            int end;
            while((line = in.readLine()) != null) {
                line = line.trim();
                for(int i = 0; i < KEYS.length; i++) {
                    key = KEYS[i];
                    if(line.contains(key)) {
                        start = line.indexOf(key) + key.length();
                        values[i] = line.substring(start);
                    }
                }
            }
            in.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        data.setValues(values);
        return data;
    }

    private static String getDataAsCsv(ArrayList<Data> dataList) {
        String info = "";
        for(Data data : dataList) {
            info += data.getProjectName() + COMMA;
            info += data.getGradleBuildName() + COMMA;
            info += data.getApplicationId() + COMMA;
            info += data.getBuildToolsVersion() + COMMA;
            info += data.getCompileSdkVersion() + COMMA;
            info += data.getMinSdkVersion() + COMMA;
            info += data.getTargetSdkVersion() + COMMA;
            info += data.getVersionCode() + COMMA;
            info += data.getVersionName() + COMMA;
            info += LS;
        }
        return info;
    }

    private static void listData(ArrayList<Data> dataList) {
        System.out.println(getDataAsCsv(dataList));
    }

    private static void writeCSV(ArrayList<Data> dataList) {
        String fileName = String.format(CVS_FILENAME_TEMPLATE,
            formatter.format(new Date()));
        File file = new File(fileName);
        if(file.exists()) {
            int selection = JOptionPane.showConfirmDialog(null,
                "File already exists:" + LS + fileName + "\nOK to replace?",
                "Warning", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE);
            if(selection != JOptionPane.OK_OPTION) return;
        }
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(file);
            // Headings
            String[] headings = CVS_HEADINGS;
            for(String heading : headings) {
                writer.print(heading + COMMA);
            }
            writer.println();
            writer.print(getDataAsCsv(dataList));
        } catch(Exception ex) {
            ex.printStackTrace();
            return;
        } finally {
            if(writer != null) {
                writer.flush();
                writer.close();
            }
        }
        System.out.println("Wrote " + fileName);
    }

    public static void main(String[] args) {
        ArrayList<Data> dataList = run();
        listData(dataList);
        writeCSV(dataList);
        System.out.println();
        System.out.println("All Done");

    }

    private static class Data
    {
        private String projectName;
        private String gradleBuildName;
        private String buildToolsVersion;
        private String compileSdkVersion;
        private String applicationId;
        private String minSdkVersion;
        private String targetSdkVersion;
        private String versionCode;
        private String versionName;

        public void setValues(String[] values) {
            setApplicationId(values[0]);
            setCompileSdkVersion(values[1]);
            setMinSdkVersion(values[2]);
            setTargetSdkVersion(values[3]);
            setVersionCode(values[4]);
            setVersionName(values[5]);
            setBuildToolsVersion(values[6]);
        }

        /**
         * @return The value of projectName.
         */
        public String getProjectName() {
            return projectName;
        }

        /**
         * @param projectName The new value for projectName.
         */
        public void setProjectName(String projectName) {
            this.projectName = projectName;
        }

        /**
         * @return The value of gradleBuildName.
         */
        public String getGradleBuildName() {
            return gradleBuildName;
        }

        /**
         * @param gradleBuildName The new value for gradleBuildName.
         */
        public void setGradleBuildName(String gradleBuildName) {
            this.gradleBuildName = gradleBuildName;
        }

        /**
         * @return The value of buildToolsVersion.
         */
        public String getBuildToolsVersion() {
            return buildToolsVersion;
        }

        /**
         * @param buildToolsVersion The new value for buildToolsVersion.
         */
        public void setBuildToolsVersion(String buildToolsVersion) {
            this.buildToolsVersion = buildToolsVersion;
        }

        /**
         * @return The value of compileSdkVersion.
         */
        public String getCompileSdkVersion() {
            return compileSdkVersion;
        }

        /**
         * @param compileSdkVersion The new value for compileSdkVersion.
         */
        public void setCompileSdkVersion(String compileSdkVersion) {
            this.compileSdkVersion = compileSdkVersion;
        }

        /**
         * @return The value of applicationId.
         */
        public String getApplicationId() {
            return applicationId;
        }

        /**
         * @param applicationId The new value for applicationId.
         */
        public void setApplicationId(String applicationId) {
            this.applicationId = applicationId;
        }

        /**
         * @return The value of minSdkVersion.
         */
        public String getMinSdkVersion() {
            return minSdkVersion;
        }

        /**
         * @param minSdkVersion The new value for minSdkVersion.
         */
        public void setMinSdkVersion(String minSdkVersion) {
            this.minSdkVersion = minSdkVersion;
        }

        /**
         * @return The value of targetSdkVersion.
         */
        public String getTargetSdkVersion() {
            return targetSdkVersion;
        }

        /**
         * @param targetSdkVersion The new value for targetSdkVersion.
         */
        public void setTargetSdkVersion(String targetSdkVersion) {
            this.targetSdkVersion = targetSdkVersion;
        }

        /**
         * @return The value of versionCode.
         */
        public String getVersionCode() {
            return versionCode;
        }

        /**
         * @param versionCode The new value for versionCode.
         */
        public void setVersionCode(String versionCode) {
            this.versionCode = versionCode;
        }

        /**
         * @return The value of versionName.
         */
        public String getVersionName() {
            return versionName;
        }

        /**
         * @param versionName The new value for versionName.
         */
        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

    }

}
