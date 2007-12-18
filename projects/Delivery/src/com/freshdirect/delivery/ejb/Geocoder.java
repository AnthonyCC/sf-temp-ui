/*
 * Geocoder.java
 *
 * Created on February 11, 2002, 8:13 PM
 */

package com.freshdirect.delivery.ejb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.sql.*;

/**
 *
 * @author  mrose
 * @version
 */
public class Geocoder {
    
    public static void main(String args[]) {
        Geocoder g = new Geocoder();
        g.test(50000);
    }
    
    public Geocoder() {
        super();
    }
    
    private final static String streetSegQuery =
    "select ng.LINK_ID, ng.L_NREFADDR, ng.L_REFADDR, R_NREFADDR, ng.R_REFADDR, ng.L_POSTCODE, ng.R_POSTCODE, gg.COLUMN_VALUE as COORD " +
    "from dlv.navtech_geocode ng, TABLE(ng.geoloc.sdo_ordinates) gg " +
    "where ng.ST_NAME = ? and (ng.L_POSTCODE=? or ng.R_POSTCODE=?) " +
    "and ((ng.L_NREFADDR <= ? and ng.L_REFADDR >= ?) or (ng.R_NREFADDR <= ? and ng.R_REFADDR >= ?) " +
    "or   (ng.L_NREFADDR >= ? and ng.L_REFADDR <= ?) or (ng.R_NREFADDR >= ? and ng.R_REFADDR <= ?))";
    
    private final static String streetSeqQueryNoZip =
    "select ng.LINK_ID, ng.L_NREFADDR, ng.L_REFADDR, R_NREFADDR, ng.R_REFADDR, ng.L_POSTCODE, ng.R_POSTCODE, gg.COLUMN_VALUE as COORD " +
    "from dlv.navtech_geocode ng, TABLE(ng.geoloc.sdo_ordinates) gg " +
    "where ng.ST_NAME = ? and (ng.L_POSTCODE in (select zipcode from dlv.zipcode) or ng.R_POSTCODE in (select zipcode from dlv.zipcode)) " +
    "and ((ng.L_NREFADDR <= ? and ng.L_REFADDR >= ?) or (ng.R_NREFADDR <= ? and ng.R_REFADDR >= ?) " +
    "or (ng.L_NREFADDR >= ? and ng.L_REFADDR <= ?) or (ng.R_NREFADDR >= ? and ng.R_REFADDR <= ?))";
    
    private final static String streetSeqQueryTextPrefix =
    "select ng.LINK_ID, ng.L_NREFADDR, ng.L_REFADDR, R_NREFADDR, ng.R_REFADDR, ng.L_POSTCODE, ng.R_POSTCODE, gg.COLUMN_VALUE as COORD " +
    "from dlv.navtech_geocode ng, TABLE(ng.geoloc.sdo_ordinates) gg ";
    
    private final static String streetSeqQueryTextSuffix =
    "and (ng.L_POSTCODE in (select zipcode from dlv.zipcode) or ng.R_POSTCODE in (select zipcode from dlv.zipcode)) " +
    "and ((ng.L_NREFADDR <= ? and ng.L_REFADDR >= ?) or (ng.R_NREFADDR <= ? and ng.R_REFADDR >= ?) " +
    "or (ng.L_NREFADDR >= ? and ng.L_REFADDR <= ?) or (ng.R_NREFADDR >= ? and ng.R_REFADDR <= ?))";
    
    private final static double offsetFromStreet = 20; // meters
    private final static double XmetersToDeg = 1.0 / 84515.0;  // meters per degree longitude approx at 40.7 deg latitude
    private final static double YmetersToDeg = 1.0 / 111048.0; // meters per degree latitude approx at 40.7 deg latitude
    
    private final static String testQuery = "select address, zip_code from dlv.mn_lots " +
    "where address is not null and zip_code in (select zipcode from dlv.zipcode) and rownum < ?";
    
    public void test(int numAddresses) {
        
        int attempts = 0;
        int failed = 0;
        
        long start = System.currentTimeMillis();
        
        Connection conn = null;
        HashMap addresses = new HashMap();
        try {
            conn = getConnection();
            //
            // load addresses to geocode
            //
            PreparedStatement ps = conn.prepareStatement(testQuery);
            ps.setInt(1, numAddresses);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String address = rs.getString(1);
                String zipcode = rs.getString(2);
                // clean address ranges for lots
                int dash = address.indexOf("-");
                if (dash > -1) {
                    int nextSpace = address.indexOf(" ", dash);
                    address = address.substring(0, dash) + address.substring(nextSpace, address.length());
                }
                addresses.put(address, zipcode);
            }
            rs.close();
            ps.close();
            //
            // try and geocode a lot of addresses
            //
            for (Iterator keyIter = addresses.keySet().iterator(); keyIter.hasNext(); ) {
                attempts++;
                String address = (String) keyIter.next();
                String zipcode = (String) addresses.get(address);
                Point p = geocode(address, zipcode, conn);
                if (p == null) {
                    //System.out.println("Unable to geocode " + address + " " + zipcode);
                    failed++;
                } else {
                    //System.out.println("Geocoded " + address + " " + zipcode + " to " + p.x + ", " + p.y);
                }
                if (0 == (attempts % 200)) {
                    System.out.println("Successfully geocoded " + (attempts - failed) + " of " + attempts + " addresses");
                    System.out.println("Hitrate = " + 100.0 * (attempts - failed) / attempts);
                }
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException sqle2) {
                }
            }
        }
        
        System.out.println("Successfully geocoded " + (attempts - failed) + " of " + attempts + " addresses");
        System.out.println("Hitrate = " + 100.0 * (attempts - failed) / attempts);
        System.out.println("Elapsed time " + (int) ((System.currentTimeMillis() - start) / 1000.0) + " seconds");
        
    }
    
    public Point geocode(String address, String zipcode) {
        
        Connection conn = null;
        Point p = null;
        try {
            conn = getConnection();
            p = geocode(address, zipcode, conn);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException sqle2) {
                }
            }
        }
        return p;
        
    }
    
    public Point geocode(String address, String zipcode, Connection conn) {
        //
        // clean leading and trailing whitespace
        //
        address = address.trim().toUpperCase();
        //
        // separate street number and street name
        //
        int numBreak = address.indexOf(' ');
        if (numBreak == -1) {
            System.out.println("Invalid address " + address + " " + zipcode);
            return null;
        }
        String streetName = address.substring(numBreak+1, address.length());
        String bldgNumCand = address.substring(0, numBreak);
        int bldgNumber = -1;
        try {
            bldgNumber = Integer.parseInt(bldgNumCand);
        } catch (NumberFormatException nfe1) {
            //
            // remove everything after the first non-numeric character and try again
            //
            StringBuffer clean = new StringBuffer();
            char[] chars = bldgNumCand.toCharArray();
            for (int i=0; i<chars.length; i++) {
                if (Character.isDigit(chars[i])) {
                    clean.append(chars[i]);
                } else {
                    break;
                }
            }
            boolean isNumber = (clean.length() > 0);
            if (isNumber) {
                try {
                    bldgNumber = Integer.parseInt(clean.toString());
                } catch (NumberFormatException nfe2) {
                    //
                    // doesn't look like a number after at all
                    //
                    isNumber = false;
                }
            }
            if (!isNumber) {
                //
                // set bldgNumber to zero and use the whole address as a street name
                //
                bldgNumber = 0;
                streetName = address;
            }
        }
        //System.out.println("bldg number is " + bldgNumber);
        //System.out.println("street name is " + streetName);
        
        StreetSegment seg = new StreetSegment();
        StreetName scrubbedStreet = scrubStreet(streetName);
        
        //
        // form a compressed version of the name (no spaces)
        // for use in fast search methods
        //
        String tempStreet = scrubbedStreet.prefix + scrubbedStreet.base + scrubbedStreet.suffix + scrubbedStreet.type;
        StringBuffer streetBuffer = new StringBuffer();
        char[] chars = tempStreet.toCharArray();
        for (int i=0; i<chars.length; i++) {
            if (!Character.isSpaceChar(chars[i])) streetBuffer.append(chars[i]);
        }
        String compressedStreet = streetBuffer.toString();
        
        try {
            //
            // find row in db for street segment with a zip code
            //
            PreparedStatement ps = conn.prepareStatement(streetSegQuery);
            ps.setString(1, compressedStreet);
            ps.setString(2, zipcode);
            ps.setString(3, zipcode);
            ps.setInt(4, bldgNumber);
            ps.setInt(5, bldgNumber);
            ps.setInt(6, bldgNumber);
            ps.setInt(7, bldgNumber);
            ps.setInt(8, bldgNumber);
            ps.setInt(9, bldgNumber);
            ps.setInt(10, bldgNumber);
            ps.setInt(11, bldgNumber);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                seg.linkId = rs.getLong("LINK_ID");
                seg.leftNearNum = rs.getInt("L_NREFADDR");
                seg.leftFarNum = rs.getInt("L_REFADDR");
                seg.rightNearNum = rs.getInt("R_NREFADDR");
                seg.rightFarNum = rs.getInt("R_REFADDR");
                seg.leftZipcode = rs.getString("L_POSTCODE");
                seg.rightZipcode = rs.getString("R_POSTCODE");
                seg.nearX = rs.getDouble("COORD");
                if (rs.next())
                    seg.nearY = rs.getDouble("COORD");
                if (rs.next())
                    seg.farX = rs.getDouble("COORD");
                if (rs.next())
                    seg.farY = rs.getDouble("COORD");
            } else {
                //System.out.println("Address doesn't appear to be in supplied zipcode.  Widening search...");
            }
            rs.close();
            ps.close();
            if (seg.linkId != -1) {
                return findPoint(seg, bldgNumber);
            }
            //
            // didn't find a matching segment
            // try again, relaxing the zipcode constraint
            //
            ps = conn.prepareStatement(streetSeqQueryNoZip);
            ps.setString(1, compressedStreet);
            ps.setInt(2, bldgNumber);
            ps.setInt(3, bldgNumber);
            ps.setInt(4, bldgNumber);
            ps.setInt(5, bldgNumber);
            ps.setInt(6, bldgNumber);
            ps.setInt(7, bldgNumber);
            ps.setInt(8, bldgNumber);
            ps.setInt(9, bldgNumber);
            rs = ps.executeQuery();
            if (rs.next()) {
                seg.linkId = rs.getLong("LINK_ID");
                seg.leftNearNum = rs.getInt("L_NREFADDR");
                seg.leftFarNum = rs.getInt("L_REFADDR");
                seg.rightNearNum = rs.getInt("R_NREFADDR");
                seg.rightFarNum = rs.getInt("R_REFADDR");
                seg.leftZipcode = rs.getString("L_POSTCODE");
                seg.rightZipcode = rs.getString("R_POSTCODE");
                seg.nearX = rs.getDouble("COORD");
                if (rs.next())
                    seg.nearY = rs.getDouble("COORD");
                if (rs.next())
                    seg.farX = rs.getDouble("COORD");
                if (rs.next())
                    seg.farY = rs.getDouble("COORD");
            }
            if ((seg.leftNearNum != 0) && (seg.leftFarNum != 0) && (((seg.leftNearNum <= bldgNumber) && (seg.leftFarNum >= bldgNumber)) || ((seg.leftNearNum >= bldgNumber) && (seg.leftFarNum <= bldgNumber)))) {
                //System.out.println("Correct zipcode is " + seg.leftZipcode);
            } else {
                //System.out.println("Correct zipcode is " + seg.rightZipcode);
            }
            rs.close();
            ps.close();
            if (seg.linkId != -1) {
                return findPoint(seg, bldgNumber);
            }
            //
            // one last thing to try before doing any broader searches
            // try making substitutions in the compressed name with directions
            //
            compressedStreet = makeSubstitutions(compressedStreet, directions);
            ps = conn.prepareStatement(streetSeqQueryNoZip);
            ps.setString(1, compressedStreet);
            ps.setInt(2, bldgNumber);
            ps.setInt(3, bldgNumber);
            ps.setInt(4, bldgNumber);
            ps.setInt(5, bldgNumber);
            ps.setInt(6, bldgNumber);
            ps.setInt(7, bldgNumber);
            ps.setInt(8, bldgNumber);
            ps.setInt(9, bldgNumber);
            rs = ps.executeQuery();
            if (rs.next()) {
                seg.linkId = rs.getLong("LINK_ID");
                seg.leftNearNum = rs.getInt("L_NREFADDR");
                seg.leftFarNum = rs.getInt("L_REFADDR");
                seg.rightNearNum = rs.getInt("R_NREFADDR");
                seg.rightFarNum = rs.getInt("R_REFADDR");
                seg.leftZipcode = rs.getString("L_POSTCODE");
                seg.rightZipcode = rs.getString("R_POSTCODE");
                seg.nearX = rs.getDouble("COORD");
                if (rs.next())
                    seg.nearY = rs.getDouble("COORD");
                if (rs.next())
                    seg.farX = rs.getDouble("COORD");
                if (rs.next())
                    seg.farY = rs.getDouble("COORD");
            }
            if ((seg.leftNearNum != 0) && (seg.leftFarNum != 0) && (((seg.leftNearNum <= bldgNumber) && (seg.leftFarNum >= bldgNumber)) || ((seg.leftNearNum >= bldgNumber) && (seg.leftFarNum <= bldgNumber)))) {
                //System.out.println("Correct zipcode is " + seg.leftZipcode);
            } else {
                //System.out.println("Correct zipcode is " + seg.rightZipcode);
            }
            rs.close();
            ps.close();
            if (seg.linkId != -1) {
                return findPoint(seg, bldgNumber);
            }
            //
            // still didn't find a matching segment
            // maybe the street name is misspelled or abbreviated?
            // do a fuzzy search on only the base name of the street
            // only want to do this if the street name isn't a number
            // (e.g. not like W 12 Street)
            //
            boolean hasNumber = false;
            chars = scrubbedStreet.base.toCharArray();
            for (int i=0; i<chars.length; i++) {
                if (Character.isDigit(chars[i])) {
                    hasNumber = true;
                    break;
                }
            }
            if (hasNumber) {
                return null;
            }
            //
            // break the base into individual words
            //
            String[] baseWords; {
                StringTokenizer st=new StringTokenizer(scrubbedStreet.base, " ");
                baseWords = new String[st.countTokens()];
                for (int i=0; st.hasMoreTokens(); i++) {
                    baseWords[i] = st.nextToken();
                }
            }
            //
            // do a like search
            //
            StringBuffer likeBuffer = new StringBuffer();
            for (int i=0; i<baseWords.length; i++) {
                if (i == 0)
                    likeBuffer.append(" where ");
                else
                    likeBuffer.append(" and ");
                likeBuffer.append("ng.ST_NM_BASE like '%").append(baseWords[i]).append("%' ");
            }
            String likeQuery = streetSeqQueryTextPrefix + likeBuffer.toString() + streetSeqQueryTextSuffix;
            //System.out.println(likeQuery);
            ps = conn.prepareStatement(likeQuery);
            ps.setInt(1, bldgNumber);
            ps.setInt(2, bldgNumber);
            ps.setInt(3, bldgNumber);
            ps.setInt(4, bldgNumber);
            ps.setInt(5, bldgNumber);
            ps.setInt(6, bldgNumber);
            ps.setInt(7, bldgNumber);
            ps.setInt(8, bldgNumber);
            rs = ps.executeQuery();
            if (rs.next()) {
                seg.linkId = rs.getLong("LINK_ID");
                seg.leftNearNum = rs.getInt("L_NREFADDR");
                seg.leftFarNum = rs.getInt("L_REFADDR");
                seg.rightNearNum = rs.getInt("R_NREFADDR");
                seg.rightFarNum = rs.getInt("R_REFADDR");
                seg.leftZipcode = rs.getString("L_POSTCODE");
                seg.rightZipcode = rs.getString("R_POSTCODE");
                seg.nearX = rs.getDouble("COORD");
                if (rs.next())
                    seg.nearY = rs.getDouble("COORD");
                if (rs.next())
                    seg.farX = rs.getDouble("COORD");
                if (rs.next())
                    seg.farY = rs.getDouble("COORD");
                if ((seg.leftNearNum != 0) && (seg.leftFarNum != 0) && (((seg.leftNearNum <= bldgNumber) && (seg.leftFarNum >= bldgNumber)) || ((seg.leftNearNum >= bldgNumber) && (seg.leftFarNum <= bldgNumber)))) {
                    //System.out.println("Correct zipcode is " + seg.leftZipcode);
                } else {
                    //System.out.println("Correct zipcode is " + seg.rightZipcode);
                }
            } else {
                //System.out.println("Can't find street name using fuzzy search either.  Not a clue where this is");
            }
            rs.close();
            ps.close();
            if (seg.linkId != -1) {
                return findPoint(seg, bldgNumber);
            }
            //
            // do a fuzzy search
            //
            StringBuffer fuzzyBuffer = new StringBuffer();
            fuzzyBuffer.append(" where contains(ng.ST_NM_BASE, '");
            for (int i=0; i<baseWords.length; i++) {
                if (i!=0) {
                    fuzzyBuffer.append('&');
                }
                // use fuzzyness and word stemming
                fuzzyBuffer.append("?${").append(baseWords[i]).append('}');
            }
            fuzzyBuffer.append("') > 0 ");
            String fuzzyQuery = streetSeqQueryTextPrefix + fuzzyBuffer.toString() + streetSeqQueryTextSuffix;
            //System.out.println(fuzzyQuery);
            ps = conn.prepareStatement(fuzzyQuery);
            ps.setInt(1, bldgNumber);
            ps.setInt(2, bldgNumber);
            ps.setInt(3, bldgNumber);
            ps.setInt(4, bldgNumber);
            ps.setInt(5, bldgNumber);
            ps.setInt(6, bldgNumber);
            ps.setInt(7, bldgNumber);
            ps.setInt(8, bldgNumber);
            rs = ps.executeQuery();
            if (rs.next()) {
                seg.linkId = rs.getLong("LINK_ID");
                seg.leftNearNum = rs.getInt("L_NREFADDR");
                seg.leftFarNum = rs.getInt("L_REFADDR");
                seg.rightNearNum = rs.getInt("R_NREFADDR");
                seg.rightFarNum = rs.getInt("R_REFADDR");
                seg.leftZipcode = rs.getString("L_POSTCODE");
                seg.rightZipcode = rs.getString("R_POSTCODE");
                seg.nearX = rs.getDouble("COORD");
                if (rs.next())
                    seg.nearY = rs.getDouble("COORD");
                if (rs.next())
                    seg.farX = rs.getDouble("COORD");
                if (rs.next())
                    seg.farY = rs.getDouble("COORD");
                if ((seg.leftNearNum != 0) && (seg.leftFarNum != 0) && (((seg.leftNearNum <= bldgNumber) && (seg.leftFarNum >= bldgNumber)) || ((seg.leftNearNum >= bldgNumber) && (seg.leftFarNum <= bldgNumber)))) {
                    //System.out.println("Correct zipcode is " + seg.leftZipcode);
                } else {
                    //System.out.println("Correct zipcode is " + seg.rightZipcode);
                }
            } else {
                //System.out.println("Can't find street name using fuzzy search either.  Not a clue where this is");
            }
            rs.close();
            ps.close();
            if (seg.linkId != -1) {
                return findPoint(seg, bldgNumber);
            }
            
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        
        System.out.println("Unable to geocode " + address + " " + zipcode + " as " + scrubbedStreet);
        return null;
        
    }
    
    private Point findPoint(StreetSegment segment, int address) {
        //
        // find the unit vector for the street segment
        //
        double dX = segment.farX - segment.nearX;
        double dY = segment.farY - segment.nearY;
        double length = Math.sqrt(dX * dX + dY * dY);
        double dXunit = dX / length;
        double dYunit = dY / length;
        //
        // which side of the street?
        //
        boolean onLeft = false;
        if ( ((segment.leftNearNum != 0) && (segment.leftFarNum != 0)) && (((segment.leftNearNum % 2) == (address % 2)) || ((segment.leftFarNum % 2) == (address % 2))) ) {
            onLeft = true;
            //System.out.println("on left side of street");
        } else {
            //System.out.println("on right side of street");
        }
        //
        // how far along the segment is the street address?
        //
        double perc = 0.0;
        if (onLeft) {
            if (segment.leftNearNum < segment.leftFarNum) {
                perc = ((double)(address - segment.leftNearNum)) / Math.abs((double)(segment.leftFarNum - segment.leftNearNum));
            } else {
                perc = ((double)(address - segment.leftFarNum)) / Math.abs((double)(segment.leftFarNum - segment.leftNearNum));
            }
        } else {
            if (segment.rightNearNum < segment.rightFarNum) {
                perc = ((double)(address - segment.rightNearNum)) / Math.abs((double)(segment.rightFarNum - segment.rightNearNum));
            } else {
                perc = ((double)(address - segment.rightFarNum)) / Math.abs((double)(segment.rightFarNum - segment.rightNearNum));
            }
        }
        //System.out.println("perc is " + perc);
        //
        // move the endpoints of the street back from the intersection
        //
        segment.nearX += dXunit * offsetFromStreet * XmetersToDeg;
        segment.nearY += dYunit * offsetFromStreet * YmetersToDeg;
        segment.farX  -= dXunit * offsetFromStreet * XmetersToDeg;
        segment.farY  -= dYunit * offsetFromStreet * YmetersToDeg;
        //
        // find the X,Y point on the street
        //
        double pX = segment.nearX + perc * (segment.farX - segment.nearX);
        double pY = segment.nearY + perc * (segment.farY - segment.nearY);
        
        //System.out.println("geocoded point on street is X = " + pX + ", Y = " + pY);
        //
        // offset the X,Y point back from the street
        //
        Point retval = new Point();
        if (onLeft) {
            //
            // rotate unit vector counter-clockwise
            // find displacement by (-dY, dX)
            //
            retval.x = pX - dYunit * offsetFromStreet * XmetersToDeg;
            retval.y = pY + dXunit * offsetFromStreet * YmetersToDeg;
        } else {
            //
            // rotate unit vecotr clockwise
            // find displacement by (dY, -dX)
            //
            retval.x = pX + dYunit * offsetFromStreet * XmetersToDeg;
            retval.y = pY - dXunit * offsetFromStreet * YmetersToDeg;
        }
        
        //System.out.println("point offset from street is X = " + retval.x + ", Y = " + retval.y);
        
        return retval;
    }
    
    protected Connection getConnection() throws SQLException {
        Connection c = DriverManager.getConnection("jdbc:oracle:thin:@db1.dev.nyc1.freshdirect.com:1521:DBDEV01", "dlv_dev", "dlv_dev");
        return c;
    }
    
    static {
        //try {
        //    DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        //} catch (SQLException sqle) {
        //    sqle.printStackTrace();
        //}
    }
    
    public class Point {
        public double x;
        public double y;
        
        public Point() {
            super();
        }
        
        public Point(double nx, double ny) {
            this();
            this.x = nx;
            this.y = ny;
        }
        
        public String toString() {
            return "Point[ " + this.x + ", " + this.y + " ]";
        }
    }
    
    public class StreetSegment {
        
        public long linkId = -1;
        public String street;
        public String leftZipcode;
        public String rightZipcode;
        public int leftNearNum;
        public int leftFarNum;
        public int rightNearNum;
        public int rightFarNum;
        public double nearX;
        public double nearY;
        public double farX;
        public double farY;
        
    }
    
    private ArrayList tokenize(String thing) {
        ArrayList retval = new ArrayList();
        for (StringTokenizer stoke = new StringTokenizer(thing.toString(), " "); stoke.hasMoreTokens(); ) {
            retval.add(stoke.nextToken());
        }
        return retval;
    }
    
    private StreetName scrubStreet(String street) {
        //
        // remove any leading and trailing whitespace
        // upper case everything
        //
        street = street.trim().toUpperCase();
        //
        // if the street begins with 1/2, dump it
        //
        int half = street.indexOf("1/2");
        if (half != -1) {
            street = street.substring(half+3, street.length()).trim();
        }
        //
        // remove any punctuation
        //
        StringBuffer nopunct = new StringBuffer();
        char[] chars = street.toCharArray();
        for (int i=0; i<chars.length; i++) {
            if (Character.isLetterOrDigit(chars[i]) || Character.isWhitespace(chars[i])) {
                nopunct.append(chars[i]);
            }
        }
        street = nopunct.toString();
        //
        // tokenize street name
        //
        ArrayList nameParts = tokenize(nopunct.toString());
        String prefix = "";
        int prefixToken = -1;
        String suffix = "";
        int suffixToken = -1;
        String type = "";
        int typeToken = -1;
        String base = "";
        if (nameParts.size() == 1) {
            // its all base (e.g. Broadway)
        } else if (nameParts.size() == 2) {
            // three possibilities
            // - prefix and base (e.g. West Broadway)
            // - base and suffix (very, very rare, no good example)
            // - base and type (e.g. Broome Street)
            boolean firstIsPrefix = canMakeSubstitution((String) nameParts.get(0), directions, true);
            boolean secondIsSuffix = canMakeSubstitution((String) nameParts.get(1), directions, true);
            boolean secondIsType = canMakeSubstitution((String) nameParts.get(1), streetTypes, true);
            if (firstIsPrefix && !secondIsType) {
                prefixToken = 0;
            } else if (firstIsPrefix && secondIsType) {
                // e.g. South Street
                typeToken = 1;
            } else if (secondIsType) {
                typeToken = 1;
            } else if (secondIsSuffix) {
                suffixToken = 1;
            }
        } else if (nameParts.size() >= 3) {
            // three possibilities
            // - prefix, base, type (e.g. East 5th Street)
            // - base, type (e.g. Saint Nicholas Avenue, La Guardia Place)
            // - base, type, suffix (e.g. Central Park West, 7th Avenue South)
            boolean firstIsPrefix = canMakeSubstitution((String) nameParts.get(0), directions, true);
            boolean lastIsSuffix = canMakeSubstitution((String) nameParts.get(nameParts.size()-1), directions, true);
            boolean lastIsType = canMakeSubstitution((String) nameParts.get(nameParts.size()-1), streetTypes, true);
            if (firstIsPrefix) {
                prefixToken = 0;
                //
                // unless the next token matches certain values
                //
                if ("END".equals((String) nameParts.get(1))) {
                    prefixToken = -1;
                }
            }
            if (lastIsSuffix && !firstIsPrefix) {
                suffixToken = nameParts.size()-1;
                typeToken = nameParts.size()-2;
            } else if (lastIsType) {
                typeToken = nameParts.size()-1;
            }
        }
        
        if (prefixToken != -1) {
            prefix = (String) nameParts.get(prefixToken);
        }
        if (suffixToken != -1) {
            suffix = (String) nameParts.get(suffixToken);
        }
        if (typeToken != -1) {
            type = (String) nameParts.get(typeToken);
        }
        //
        // base is sum of all tokens that aren't used for prefix, suffix or type
        //
        StringBuffer baseBuffer = new StringBuffer();
        for (int i=0; i<nameParts.size(); i++) {
            if (!(((prefixToken != -1) && (prefixToken == i)) || ((suffixToken != -1) && (suffixToken == i)) || ((typeToken != -1) && (typeToken == i)))) {
                if (baseBuffer.length() != 0) baseBuffer.append(' ');
                baseBuffer.append((String) nameParts.get(i));
            }
        }
        base = baseBuffer.toString();
        
        //System.out.println("street name components are " + prefix + ":" + base + ":" + suffix + ":" + type);
        
        prefix = makeSubstitutions(prefix, directions, true);
        base   = makeSubstitutions(base, numberNames);
        base   = fixNumberSuffixes(base);
        suffix = makeSubstitutions(suffix, directions, true);
        type   = makeSubstitutions(type, streetTypes, true);
        
        //System.out.println("normalized street name is " + prefix + ":" + base + ":" + suffix + ":" + type);
        
        return new StreetName(prefix, base, suffix, type);
        
    }
    
    public class StreetName {
        public String prefix;
        public String base;
        public String suffix;
        public String type;
        
        public StreetName() {
            super();
        }
        
        public StreetName(String prefix, String base, String suffix, String type) {
            this();
            this.prefix = prefix;
            this.base = base;
            this.suffix = suffix;
            this.type = type;
        }
        
        public String toString() {
            return "[StreetName " + this.prefix + " " + this.base + " " + this.suffix + " " + this.type + "]";
        }
    }
    
    private boolean canMakeSubstitution(String orig, HashMap subs, boolean exact) {
        orig = orig.trim();
        for (Iterator keyIter = subs.keySet().iterator(); keyIter.hasNext(); ) {
            String key = (String) keyIter.next();
            //String value = (String) subs.get(key);
            if (exact) {
                if (key.equalsIgnoreCase(orig)) {
                    return true;
                }
            } else {
                int pos = orig.indexOf(key);
                if (pos > -1) {
                    return true;
                }
            }
        }
        for (Iterator valIter = subs.values().iterator(); valIter.hasNext(); ) {
            String value = (String) valIter.next();
            if (exact) {
                if (value.equalsIgnoreCase(orig)) {
                    return true;
                }
            } else {
                int pos = orig.indexOf(value);
                if (pos > -1) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private String makeSubstitutions(String orig, HashMap subs) {
        return makeSubstitutions(orig, subs, false);
    }
    
    private String makeSubstitutions(String orig, HashMap subs, boolean exact) {
        orig = orig.trim();
        for (Iterator keyIter = subs.keySet().iterator(); keyIter.hasNext(); ) {
            String key = (String) keyIter.next();
            String value = (String) subs.get(key);
            if (exact) {
                if (key.equalsIgnoreCase(orig)) {
                    return value;
                }
            } else {
                int pos = orig.indexOf(key);
                if (pos > -1) {
                    String pre = orig.substring(0, pos);
                    String post = orig.substring(pos+key.length(), orig.length());
                    return (pre + value + post);
                }
            }
        }
        return orig;
    }
    
    private String fixNumberSuffixes(String orig) {
        orig = orig.trim();
        for (Iterator keyIter = numberSuffixes.keySet().iterator(); keyIter.hasNext(); ) {
            String key = (String) keyIter.next();
            String value = (String) numberSuffixes.get(key);
            if (orig.endsWith(key)) {
                return (orig + value);
            }
        }
        return orig;
    }
    
    //
    // substitutions for street name scrubbing are here
    //
    
    //
    // used for prefix and suffix
    //
    private final static java.util.HashMap directions = new java.util.HashMap();
    static {
        directions.put("NORTH", "N");
        directions.put("SOUTH", "S");
        directions.put("EAST",  "E");
        directions.put("WEST",  "W");
        directions.put("NORTHEAST", "NE");
        directions.put("NORTHWEST", "NW");
        directions.put("SOUTHEAST", "SE");
        directions.put("SOUTHWEST", "SW");
    }
    
    private final static java.util.HashMap numberNames = new java.util.HashMap();
    static {
        numberNames.put("FIRST",    "1ST");
        numberNames.put("SECOND",   "2ND");
        numberNames.put("THIRD",    "3RD");
        numberNames.put("FOURTH",   "4TH");
        numberNames.put("FIFTH",    "5TH");
        numberNames.put("SIXTH",    "6TH");
        numberNames.put("SEVENTH",  "7TH");
        numberNames.put("EIGHTH",   "8TH");
        numberNames.put("NINTH",    "9TH");
        numberNames.put("TENTH",    "10TH");
    }
    
    private final static HashMap numberSuffixes = new HashMap();
    static {
        numberSuffixes.put("0", "TH");
        numberSuffixes.put("1", "ST");
        numberSuffixes.put("2", "ND");
        numberSuffixes.put("3", "RD");
        numberSuffixes.put("4", "TH");
        numberSuffixes.put("5", "TH");
        numberSuffixes.put("6", "TH");
        numberSuffixes.put("7", "TH");
        numberSuffixes.put("8", "TH");
        numberSuffixes.put("9", "TH");
        numberSuffixes.put("11", "TH");
        numberSuffixes.put("12", "TH");
        numberSuffixes.put("13", "TH");
    }
    
    private final static java.util.HashMap streetTypes = new java.util.HashMap();
    static {
        streetTypes.put("ALLEY",        "ALY");
        streetTypes.put("AVENUE",       "AVE");
        streetTypes.put("BOULEVARD",    "BLVD");
        streetTypes.put("BRIDGE",       "BR");
        streetTypes.put("BYPASS",       "BYP");
        streetTypes.put("CAUSEWAY",     "CSWY");
        streetTypes.put("CIRCLE",       "CIR");
        streetTypes.put("CENTER",       "CTR");
        streetTypes.put("COURT",        "CT");
        streetTypes.put("CROSSING",     "XING");
        streetTypes.put("DRIVE",        "DR");
        streetTypes.put("EXPRESSWAY",   "EXPY");
        streetTypes.put("EXTENSION",    "EXT");
        streetTypes.put("FREEWAY",      "FWY");
        streetTypes.put("HEIGHTS",      "HTS");
        streetTypes.put("HIGHWAY",      "HWY");
        streetTypes.put("HIWAY",        "HWY");
        streetTypes.put("INTERSTATE",   "I");
        streetTypes.put("LANE",         "LN");
        streetTypes.put("MOUNT",        "MT");
        streetTypes.put("OVAL",         "OVL");
        streetTypes.put("PARK",         "PARK");
        streetTypes.put("PARKWAY",      "PKWY");
        streetTypes.put("PLCE",         "PL");
        streetTypes.put("PLACE",        "PL");
        streetTypes.put("PLAZA",        "PLZ");
        streetTypes.put("POINT",        "PT");
        streetTypes.put("RAILROAD",     "RR");
        streetTypes.put("RIDGE",        "RDG");
        streetTypes.put("ROAD",         "RD");
        streetTypes.put("ROUTE",        "RT");
        streetTypes.put("SAINT",        "ST");
        streetTypes.put("SQUARE",       "SQ");
        streetTypes.put("STREET",       "ST");
        streetTypes.put("STR",          "ST");
        streetTypes.put("TERRACE",      "TER");
    }
    
    
}
