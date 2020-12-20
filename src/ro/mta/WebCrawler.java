package ro.mta;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

import ro.mta.commands.AbstractCommand;
import ro.mta.commands.CommandFactory;
import ro.mta.Logger;

import static ro.mta.Sitemap.*;

public class WebCrawler {

    private static PostProcessor postProcessor;

    private HashMap<String, String> rules = new HashMap<>();

    public static void main(String[] args) throws IOException {

        interpretConfig("f.conf");
        String s = "\n" +
                "# Created by https://www.toptal.com/developers/gitignore/api/intellij,java,windows\n" +
                "# Edit at https://www.toptal.com/developers/gitignore?templates=intellij,java,windows\n" +
                "\n" +
                "### Intellij ###\n" +
                "# Covers JetBrains IDEs: IntelliJ, RubyMine, PhpStorm, AppCode, PyCharm, CLion, Android Studio, WebStorm and Rider\n" +
                "# Reference: https://intellij-support.jetbrains.com/hc/en-us/articles/206544839\n" +
                "\n" +
                "# User-specific stuff\n" +
                ".idea/**/workspace.xml\n" +
                ".idea/**/tasks.xml\n" +
                ".idea/**/usage.statistics.xml\n" +
                ".idea/**/dictionaries\n" +
                ".idea/**/shelf\n" +
                "\n" +
                "# Generated files\n" +
                ".idea/**/contentModel.xml\n" +
                "\n" +
                "# Sensitive or high-churn files\n" +
                ".idea/**/dataSources/\n" +
                ".idea/**/dataSources.ids\n" +
                ".idea/**/dataSources.local.xml\n" +
                ".idea/**/sqlDataSources.xml\n" +
                ".idea/**/dynamic.xml\n" +
                ".idea/**/uiDesigner.xml\n" +
                ".idea/**/dbnavigator.xml\n" +
                "\n" +
                "# Gradle\n" +
                ".idea/**/gradle.xml\n" +
                ".idea/**/libraries\n" +
                "\n" +
                "# Gradle and Maven with auto-import\n" +
                "# When using Gradle or Maven with auto-import, you should exclude module files,\n" +
                "# since they will be recreated, and may cause churn.  Uncomment if using\n" +
                "# auto-import.\n" +
                "# .idea/artifacts\n" +
                "# .idea/compiler.xml\n" +
                "# .idea/jarRepositories.xml\n" +
                "# .idea/modules.xml\n" +
                "# .idea/*.iml\n" +
                "# .idea/modules\n" +
                "# *.iml\n" +
                "# *.ipr\n" +
                "\n" +
                "# CMake\n" +
                "cmake-build-*/\n" +
                "\n" +
                "# Mongo Explorer plugin\n" +
                ".idea/**/mongoSettings.xml\n" +
                "\n" +
                "# File-based project format\n" +
                "*.iws\n" +
                "\n" +
                "# IntelliJ\n" +
                "out/\n" +
                "\n" +
                "# mpeltonen/sbt-idea plugin\n" +
                ".idea_modules/\n" +
                "\n" +
                "# JIRA plugin\n" +
                "atlassian-ide-plugin.xml\n" +
                "\n" +
                "# Cursive Clojure plugin\n" +
                ".idea/replstate.xml\n" +
                "\n" +
                "# Crashlytics plugin (for Android Studio and IntelliJ)\n" +
                "com_crashlytics_export_strings.xml\n" +
                "crashlytics.properties\n" +
                "crashlytics-build.properties\n" +
                "fabric.properties\n" +
                "\n" +
                "# Editor-based Rest Client\n" +
                ".idea/httpRequests\n" +
                "\n" +
                "# Android studio 3.1+ serialized cache file\n" +
                ".idea/caches/build_file_checksums.ser\n" +
                "\n" +
                "### Intellij Patch ###\n" +
                "# Comment Reason: https://github.com/joeblau/gitignore.io/issues/186#issuecomment-215987721\n" +
                "\n" +
                "# *.iml\n" +
                "# modules.xml\n" +
                "# .idea/misc.xml\n" +
                "# *.ipr\n" +
                "\n" +
                "# Sonarlint plugin\n" +
                "# https://plugins.jetbrains.com/plugin/7973-sonarlint\n" +
                ".idea/**/sonarlint/\n" +
                "\n" +
                "# SonarQube Plugin\n" +
                "# https://plugins.jetbrains.com/plugin/7238-sonarqube-community-plugin\n" +
                ".idea/**/sonarIssues.xml\n" +
                "\n" +
                "# Markdown Navigator plugin\n" +
                "# https://plugins.jetbrains.com/plugin/7896-markdown-navigator-enhanced\n" +
                ".idea/**/markdown-navigator.xml\n" +
                ".idea/**/markdown-navigator-enh.xml\n" +
                ".idea/**/markdown-navigator/\n" +
                "\n" +
                "# Cache file creation bug\n" +
                "# See https://youtrack.jetbrains.com/issue/JBR-2257\n" +
                ".idea/$CACHE_FILE$\n" +
                "\n" +
                "# CodeStream plugin\n" +
                "# https://plugins.jetbrains.com/plugin/12206-codestream\n" +
                ".idea/codestream.xml\n" +
                "\n" +
                "### Java ###\n" +
                "# Compiled class file\n" +
                "*.class\n" +
                "\n" +
                "# Log file\n" +
                "*.log\n" +
                "\n" +
                "# BlueJ files\n" +
                "*.ctxt\n" +
                "\n" +
                "# Mobile Tools for Java (J2ME)\n" +
                ".mtj.tmp/\n" +
                "\n" +
                "# Package Files #\n" +
                "*.jar\n" +
                "*.war\n" +
                "*.nar\n" +
                "*.ear\n" +
                "*.zip\n" +
                "*.tar.gz\n" +
                "*.rar\n" +
                "\n" +
                "# virtual machine crash logs, see http://www.java.com/en/download/help/error_hotspot.xml\n" +
                "hs_err_pid*\n" +
                "\n" +
                "### Windows ###\n" +
                "# Windows thumbnail cache files\n" +
                "Thumbs.db\n" +
                "Thumbs.db:encryptable\n" +
                "ehthumbs.db\n" +
                "ehthumbs_vista.db\n" +
                "\n" +
                "# Dump file\n" +
                "*.stackdump\n" +
                "\n" +
                "# Folder config file\n" +
                "[Dd]esktop.ini\n" +
                "\n" +
                "# Recycle Bin used on file shares\n" +
                "$RECYCLE.BIN/\n" +
                "\n" +
                "# Windows Installer files\n" +
                "*.cab\n" +
                "*.msi\n" +
                "*.msix\n" +
                "*.msm\n" +
                "*.msp\n" +
                "\n" +
                "# Windows shortcuts\n" +
                "*.lnk\n" +
                "\n" +
                "# End of https://www.toptal.com/developers/gitignore/api/intellij,java,windows\n";
        PageInterpreter pageInterpreter = new PageInterpreter("F:\\Facultate\\IP\\Web_Crawler\\robots.txt");
//        System.out.println(pageInterpreter.isPageCorrect(s));
        System.out.println(pageInterpreter.formatPages(s));
//        AbstractCommand command = CommandFactory.CreateCommand(args);
//        command.execute();


        //String fileToAppend = "C:\\Users\\Bogdan\\Documents\\GitHub\\sitemap.txt";
        //Create_File(fileToAppend); //fisierul de output
//
//        String firstdirpath = "C:\\Users\\Bogdan\\Documents\\GitHub\\"; //Calea pentru directorul principal
//        File firstdir = new File(firstdirpath);
//        try {
//            if (firstdir.exists() && firstdir.isDirectory()) {
//                File files[] = firstdir.listFiles();
//                usingFileOutputStream(firstdirpath, fileToAppend);
//                usingFileOutputStream("\r\n", fileToAppend);
//                createSitemap(files, 0, 0, fileToAppend);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    /**
     * interpretare fisier de configurare
     * se va respecta formatul "regula":"valoare" , urmand ca regulile din fisier sa fie salvate intr-un
     * obiect de forma HashMap <String, String>
     */

    public static void interpretConfig(String filename) throws FileNotFoundException {
        List<String> list = new ArrayList<String>();
        List<String> namesList = new ArrayList<String>();
        List<String> valsList = new ArrayList<String>();
        FileReader reader = new FileReader(filename);
        try (BufferedReader in = new BufferedReader(reader)) {
            String line;
            HashMap<String, String> rules = new HashMap<String, String>();
            while ((line = in.readLine()) != null) {
                String[] pair = line.split("=");
                rules.put(pair[0], pair[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
