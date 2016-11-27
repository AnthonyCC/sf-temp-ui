package com.freshdirect.tools.mediasync;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

public class MediaDbExport {

    final static Logger LOG = Logger.getLogger(MediaDbExport.class.getName());

    static class FileRec {
        String id;
        String uri;
        String type;
        boolean found = false;
    }

    private Connection connection;
    private Properties p;
    private PrintWriter out;
    private BufferedReader in;
    String svn;

    public MediaDbExport(Properties p) throws ClassNotFoundException, SQLException, IOException {
        this.p = p;
        if (p.getProperty("driver") != null) {
            Class.forName(p.getProperty("driver"));
        }
        svn = p.getProperty("svn", "svn");
    }

    public synchronized Connection getConnection() throws SQLException {
        if (connection == null) {
            connection = DriverManager.getConnection(p.getProperty("url"), p);
        }
        return connection;
    }

    public synchronized void closeConnection() throws SQLException {
        if (connection != null) {
            connection.close();
            connection = null;
        }
    }

    public PrintWriter getOut() throws IOException {
        if (out == null) {
            if (p.getProperty("out") != null) {
                out = createOutput(p.getProperty("out"));
            }
        }
        return out;
    }
    
    PrintWriter createOutput(String filename) throws IOException {
        return new PrintWriter(new BufferedWriter(new FileWriter(filename)));
    }

    /**
     * @return
     * @throws FileNotFoundException
     */
    private BufferedReader createInput(String path) throws FileNotFoundException {
        return new BufferedReader(new FileReader(path));
    }

    public void closeOut() {
        if (out != null) {
            out.flush();
            out.close();
            out = null;
        }
    }

    void log(String msg) {
        if (out != null) {
            out.println(msg);
        }
    }

    /**
     * @param args
     * @throws IOException
     * @throws FileNotFoundException
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException, SQLException {
        if (args.length >= 2) {
            Properties p = new Properties();
            p.load(new FileReader(args[0]));
            MediaDbExport m = new MediaDbExport(p);
            if ("loadFromDb".equals(args[1])) {
                LOG.info("loading from database...");
                Set<String> files = m.loadFilesFromDatabase();
                m.closeOut();
            } else if ("checkPath".equals(args[1])) {
                Map<String, FileRec> paths = m.loadPaths();

            } else if ("checkExport".equals(args[1])) {
                Set<String> paths = m.collectPaths();
            } else if ("compare".equals(args[1])) {
                m.compare();
            }

        } else {
            System.out.println("MediaDbExport <properties-file> <loadFromDb | compare>!");
            System.exit(1);
        }
    }

    private void compare() throws IOException {
        Map<String, FileRec> records = loadPaths();
        Set<String> filePaths = collectPaths();
        String prefix = p.getProperty("prefix", "test"); 
        
        final String fixScript = prefix+ ".fix.sh";
        LOG.info("writing log file to "+fixScript);
        List<String> fix = new ArrayList();
        List<String> sqlFix = new ArrayList();
        List<String> fileMoves = new ArrayList();
        List<String> svnMove1 = new ArrayList();
        List<String> svnMove2 = new ArrayList();
        List<String> svnMove3 = new ArrayList();
        List<String> otherMissing = new ArrayList();
        final String logFile = p.getProperty("log", "log.txt");
        LOG.info("writing log file to "+logFile);
        PrintWriter o = createOutput(logFile);
        o.println("missing from db :");
        int thumbCounter = 0;
        int correctibleError = 0;
        int count = 0;
        for (String path : filePaths) {
            FileRec fileRec = records.get(path);
            if (fileRec != null) {
                fileRec.found = true;
            } else {
                if (path.endsWith("Thumbs.db:encryptable")) {
                    thumbCounter ++;
                    fix.add(svn + " rm \"." + path + "\"");
                } else {
                    if (path.endsWith("_1.0")) {
                        String goodPath = path.substring(0, path.length() - 4);
                        
                        if (filePaths.contains(goodPath)) {
                            fix.add(svn + " rm \"." + path + "\"");
                        } else {
                            fileMoves.add("mv \"." + path + "\" \"." + goodPath + "\"");
                            svnMove1.add(svn + " rm \"." + path + "\"");
                            svnMove2.add(svn+" add \"." + goodPath + "\"");
                            svnMove3.add(svn+" commit -m \"re-add files "+count+"\" \"." + goodPath + "\"");
                            count++;
                        }
                        
                        FileRec fileRec2 = records.get(goodPath);
                        if (fileRec2 != null) {
                            o.println("correcting path "+path);
                            fileRec2.found = true;
                            correctibleError++;
                            continue;
                        }
                    } else {
                        otherMissing.add(svn + " rm \"." + path + "\"");
                    }
                    //o.println(path);
                }
            }
        }
        o.println();
        o.println("Thumbs.db:encryptable missing: " +thumbCounter);
        o.println("Incorrect ending: " +correctibleError);
        final String sqlScript = prefix+ ".fix.sql";
        LOG.info("writing sql file to "+sqlScript);
        
        o.println("not found in file system:");
        Set<FileRec> missing = new TreeSet<FileRec>(new Comparator<FileRec>() {
           @Override
            public int compare(FileRec o1, FileRec o2) {
               int res = o1.type.compareTo(o2.type);
               if (res == 0) {
                   res = o1.uri.compareTo(o2.uri);
               }
               return res;
            } 
        });
        for (FileRec f : records.values()) {
            if (!f.found) {
                o.println("id:"+f.id + "\t\t\t,uri:"+f.uri+"\t\t\t,type:"+f.type);
                missing.add(f);
                
            }
        }
        for (FileRec f : missing) {
            sqlFix.add("delete from CMS.MEDIA where ID = '"+f.id+"' AND URI = '"+f.uri+"' AND TYPE = '"+f.type+"';");
        }
        
        o.close();
        writeFile(sqlScript, sqlFix);
        writeFile(fixScript, fix);
        LOG.info("miscelaneus svn cleanup : " + prefix + ".otherMissingSvn.sh");
        writeFile(prefix+".otherMissingSvn.sh", otherMissing);
        LOG.info("svn 4 step moves: file moves in  " + prefix + ".moves.1.sh");
        writeFile(prefix+".moves.1.sh", fileMoves);
        LOG.info("svn 4 step moves: svn deletes in  " + prefix + ".moves.2.sh");
        writeFile(prefix+".moves.2.sh", svnMove1);
        LOG.info("svn 4 step moves: svn add in  " + prefix + ".moves.3.sh");
        writeFile(prefix+".moves.3.sh", svnMove2);
        LOG.info("svn 4 step moves: svn commits  " + prefix + ".moves.4.sh");
        writeFile(prefix+".moves.4.sh", svnMove3);
        
        writeFile(prefix+".all.sh", Arrays.asList(new String[] {
                "#!/bin/bash",
                "echo disable svn precommit hook!",
                "read",
                "./"+fixScript,
                svn + " commit -m sync_step_1",
                "./"+prefix+".otherMissingSvn.sh",
                svn + " commit -m sync_step_2",
                "./"+prefix+".moves.1.sh",
                "./"+prefix+".moves.2.sh",
                svn + " commit -m sync_step_3",
                "./"+prefix+".moves.3.sh",
                "echo enable svn precommit hook!",
                "read",
                "./"+prefix+".moves.4.sh"
        }));
        
        
        
    }
    
    
    private void writeFile(String name, List<String> lines) throws IOException {
        PrintWriter out = createOutput(name);
        for (String line : lines) {
            out.println(line);
        }
        out.close();
    }

    private Set<String> loadFilesFromDatabase() throws SQLException, IOException {
        try {
            Connection c = getConnection();
            long time = System.currentTimeMillis();
            PreparedStatement st = c.prepareStatement("SELECT ID,URI,TYPE FROM CMS.MEDIA ORDER BY ID");
            ResultSet resultSet = st.executeQuery();
            PrintWriter o = getOut();
            Set<String> files = new HashSet();
            while (resultSet.next()) {
                String id = resultSet.getString("ID");
                String uri = resultSet.getString("URI");
                String type = resultSet.getString("TYPE");
                log("" + id + ',' + uri + ',' + type);

                files.add(uri);
                if (files.size() % 1000 == 0) {
                    LOG.info("loaded " + files.size() + " in " + (System.currentTimeMillis() - time) + " ms");
                }
            }
            resultSet.close();
            st.close();
            LOG.info("loaded :" + files.size() + " uris in " + (System.currentTimeMillis() - time) + " ms");
            return files;

        } finally {
            closeOut();
            closeConnection();
        }
    }

    private Map<String, FileRec> loadPaths() throws IOException {
        String inp = p.getProperty("input", p.getProperty("out"));
        LOG.info("loading paths from " + inp);
        BufferedReader in = createInput(inp);
        String line = in.readLine();
        Map<String, FileRec> result = new HashMap();
        while (line != null) {
            String[] values = line.split(",");
            FileRec f = new FileRec();
            f.id = values[0];
            f.uri = values[1];
            f.type = values[2].intern();

            result.put(f.uri, f);
            line = in.readLine();
        }
        in.close();
        LOG.info("records: " + result.size());
        return result;
    }

    private Set<String> collectPaths() throws IOException {
        File f = new File(p.getProperty("export"));
        LOG.info("collecting from " + f.getCanonicalPath());
        Set<String> paths = new HashSet();
        visit(f, paths, f.getCanonicalPath());
        LOG.info("found " + paths.size() + " paths");
        return paths;
    }

    private void visit(File f, Set<String> paths, String root) throws IOException {
        String cp = f.getCanonicalPath();
        if (cp.startsWith(root)) {
            String x  = cp.substring(root.length());
            paths.add(x.length() == 0 ? "/" : x);
        } else {
            paths.add(cp);
        }
        File[] files = f.listFiles();
        if (files != null) {
            for (File child : files) {
                if (!".svn".equals(child.getName()) && !".git".equals(child.getName())) {
                    visit(child, paths, root);
                }
            }
        }
    }

}
