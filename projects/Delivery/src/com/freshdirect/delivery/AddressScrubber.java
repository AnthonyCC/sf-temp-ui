/*
 * Geocoder.java
 *
 * Created on February 11, 2002, 8:13 PM
 */

package com.freshdirect.delivery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import com.freshdirect.fdstore.FDStoreProperties;

/**
 *
 * @author  mrose
 * @version
 */
public class AddressScrubber {
    
    public AddressScrubber() {
        super();
    }
    
    public static String standardizeForUSPS(String streetAddress) throws InvalidAddressException {
        return standardize(streetAddress, false);
    }
    
    public static String standardizeForGeocode(String streetAddress) throws InvalidAddressException {
        return standardize(streetAddress, true);
    }

    private static String standardize(String address, boolean forGeocode) throws InvalidAddressException {
        //
        // clean leading and trailing whitespace
        //
        String streetAddress = address.trim().toUpperCase();
        //
        // separate street number and street name
        //
        int numBreak = streetAddress.indexOf(' ');
        if (numBreak == -1) {
            throw new InvalidAddressException("Invalid address :" + streetAddress);
        }
        String streetName = streetAddress.substring(numBreak+1, streetAddress.length());
        String bldgNumCand = streetAddress.substring(0, numBreak);
        int bldgNumber = -1;
        //
        // change spelled out numbers to digits
        //
        if (canMakeSubstitution(bldgNumCand, digitNames, true)) {
            bldgNumCand = makeSubstitutions(bldgNumCand, digitNames, true);
        }
        try {
            bldgNumber = Integer.parseInt(bldgNumCand);
        } catch (NumberFormatException nfe1) {
            //
            // remove any non-numeric characters and try again
            //
            StringBuffer clean = new StringBuffer();
            char[] chars = bldgNumCand.toCharArray();
            for (int i=0; i<chars.length; i++) {
                if (Character.isDigit(chars[i])) {
                    clean.append(chars[i]);
                } else {
                	// Fix is done to remove the new building no format which is changed
                	//(Ex: 23-30 is stored as 2330 instead of 23)
                    if (forGeocode && !isNewGeocodeFormat()) {
                        break;
                    }
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
                // couldn't find a bldg number at all
                //
                throw new InvalidAddressException("Unable to find a building number in : " + address);
            }
        }
       return String.valueOf(bldgNumber) + " " + standardizeStreet(streetName, forGeocode).getAddress();
    }

    private static StreetName standardizeStreet(String street, boolean forGeocode) {
        //
        // remove any leading and trailing whitespace
        // upper case everything
        //
        street = street.trim().toUpperCase();
        //
        // if the street begins with a fraction, dump it
        //
        int half = street.indexOf("1/");
        if (half != -1) {
            street = street.substring(street.indexOf(' ', half), street.length()).trim();
        }
        //
        // remove any punctuation
        //
        StringBuffer nopunct = new StringBuffer();
        char[] chars = street.toCharArray();
        for (int i=0; i<chars.length; i++) {
            if (Character.isLetterOrDigit(chars[i]) || Character.isSpaceChar(chars[i])) {
                nopunct.append(chars[i]);
            }
        }
        street = nopunct.toString();
        //
        // tokenize street name
        //
        ArrayList nameParts = tokenize(nopunct.toString());
        String preDir = "";
        int preDirToken = -1;
        String suffix = "";
        int suffixToken = -1;
        String postDir = "";
        int postDirToken = -1;
        String name = "";
        if (nameParts.size() == 1) {
            // its all base (e.g. Broadway)
        } else if (nameParts.size() == 2) {
            // three possibilities
            // - prefix and base (e.g. West Broadway)
            // - base and suffix (very, very rare, no good example)
            // - base and type (e.g. Broome Street)
            boolean firstIsPreDir = canMakeSubstitution((String) nameParts.get(0), directions, true);
            boolean secondIsPostDir = canMakeSubstitution((String) nameParts.get(1), directions, true);
            boolean secondIsSuffix = canMakeSubstitution((String) nameParts.get(1), streetTypes, true);
            if (firstIsPreDir && !secondIsSuffix) {
                preDirToken = 0;
            } else if (firstIsPreDir && secondIsSuffix) {
                // e.g. South Street
                suffixToken = 1;
            } else if (secondIsSuffix) {
                suffixToken = 1;
            } else if (secondIsPostDir) {
                postDirToken = 1;
            }
        } else if (nameParts.size() >= 3) {
            // three possibilities
            // - predir, name, suffix (e.g. East 5th Street)
            // - name, suffix (e.g. Saint Nicholas Avenue, La Guardia Place)
            // - name, suffix, postdir (e.g. Central Park West, 7th Avenue South)
            boolean firstIsPreDir = canMakeSubstitution((String) nameParts.get(0), directions, true);
            boolean lastIsPostDir = canMakeSubstitution((String) nameParts.get(nameParts.size()-1), directions, true);
            boolean lastIsSuffix = canMakeSubstitution((String) nameParts.get(nameParts.size()-1), streetTypes, true);
            if (firstIsPreDir) {
                preDirToken = 0;
            }
            if (lastIsPostDir && !firstIsPreDir) {
                postDirToken = nameParts.size()-1;
                suffixToken = nameParts.size()-2;
            } else if (lastIsSuffix) {
                suffixToken = nameParts.size()-1;
            }
        }

        //StringBuffer normalized = new StringBuffer();
        if (preDirToken != -1) {
            preDir = (String) nameParts.get(preDirToken);
        }
        if (postDirToken != -1) {
            postDir = (String) nameParts.get(postDirToken);
        }
        if (suffixToken != -1) {
            suffix = (String) nameParts.get(suffixToken);
        }
        //
        // base is sum of all tokens that aren't used for preDir, postDir or suffix
        //
        StringBuffer nameBuffer = new StringBuffer();
        for (int i=0; i<nameParts.size(); i++) {
            if (!(((preDirToken != -1) && (preDirToken == i)) || ((postDirToken != -1) && (postDirToken == i)) || ((suffixToken != -1) && (suffixToken == i)))) {
                if (nameBuffer.length() != 0) nameBuffer.append(' ');
                nameBuffer.append((String) nameParts.get(i));
            }
        }
        name = nameBuffer.toString();

        preDir = makeSubstitutions(preDir, directions, true);
        name   = makeSubstitutions(name, numberNames);
        name   = fixNumberSuffixes(name);
        postDir = makeSubstitutions(postDir, directions, true);
        suffix   = makeSubstitutions(suffix, streetTypes, true);
        
        if(forGeocode){
        	int spacePos = name.indexOf(' ');
        	if(spacePos > -1){
        		name = makeSubstitutions(name.substring(0, spacePos), geoDifferences, true) 
					   + " " + name.substring(spacePos + 1);
        	}
        	suffix = makeSubstitutions(suffix, geoDifferences, true);
        }

        return new StreetName(preDir, name, suffix, postDir);
    }

    private static class StreetName {
        public String preDir;
        public String name;
        public String suffix;
        public String postDir;

        public StreetName() {
            super();
        }

        public StreetName(String pre, String n, String s, String post) {
            this();
            this.preDir = pre;
            this.name = n;
            this.suffix = s;
            this.postDir = post;
        }

        public String getNormalForm() {
            StringBuffer buff = new StringBuffer();
            String nws = buff.append(this.preDir).append(this.name).append(this.suffix).append(this.postDir).toString();
            buff.delete(0, buff.length());
            for (int i=0; i<nws.length(); i++) {
                char c = nws.charAt(i);
                if (!Character.isWhitespace(c)) buff.append(c);
            }
            return buff.toString();
        }

        public String getAddress() {
            StringBuffer buff = new StringBuffer();
            if (preDir != null) {
                buff.append(preDir);
            }
            if (name != null) {
                if ((buff.length() > 0) && (buff.charAt(buff.length()-1) != ' ')) buff.append(" ");
                buff.append(name);
            }
            if (suffix != null) {
                if ((buff.length() > 0) && (buff.charAt(buff.length()-1) != ' ')) buff.append(" ");
                buff.append(suffix);
            }
            if (postDir != null) {
                if ((buff.length() > 0) && (buff.charAt(buff.length()-1) != ' ')) buff.append(" ");
                buff.append(postDir);
            }
            return buff.toString().trim();
        }

        public String toString() {
            return "[StreetName\n  preDir:  " + this.preDir + "\n  name:    " + this.name + "\n  suffix:  " + this.suffix + "\n  postDir: "
            + this.postDir + "\n  normal:  " + this.getNormalForm() + "\n]";
        }
    }
    
    private static ArrayList tokenize(String thing) {
        ArrayList retval = new ArrayList();
        for (StringTokenizer stoke = new StringTokenizer(thing.toString(), " "); stoke.hasMoreTokens(); ) {
            retval.add(stoke.nextToken());
        }
        return retval;
    }


    private static boolean canMakeSubstitution(String orig, HashMap subs, boolean exact) {
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

    private static String makeSubstitutions(String orig, HashMap subs) {
        return makeSubstitutions(orig, subs, false);
    }

    private static String makeSubstitutions(String orig, HashMap subs, boolean exact) {
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

    private static String fixNumberSuffixes(String orig) {
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
    
    private final static java.util.HashMap digitNames = new java.util.HashMap();
    static {
        digitNames.put("ONE",   "1");
        digitNames.put("TWO",   "2");
        digitNames.put("THREE", "3");
        digitNames.put("FOUR",  "4");
        digitNames.put("FIVE",  "5");
        digitNames.put("SIX",   "6");
        digitNames.put("SEVEN", "7");
        digitNames.put("EIGHT", "8");
        digitNames.put("NINE",  "9");
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
    	streetTypes.put("ALLEE",	"ALY");
    	streetTypes.put("ALLEY",	"ALY");
    	streetTypes.put("ALLY",		"ALY");
    	streetTypes.put("ANEX",		"ANX");
    	streetTypes.put("ANNEX",	"ANX");
    	streetTypes.put("ANNX",		"ANX");
    	streetTypes.put("ARCADE",	"ARC");
    	streetTypes.put("AV",		"AVE");
    	streetTypes.put("AVEN",		"AVE");
    	streetTypes.put("AVENU",	"AVE");
    	streetTypes.put("AVENUE",	"AVE");
    	streetTypes.put("AVN",		"AVE");
    	streetTypes.put("AVNUE",	"AVE");
    	streetTypes.put("BAYOO",	"BYU");
    	streetTypes.put("BAYOU",	"BYU");
    	streetTypes.put("BEACH",	"BCH");
    	streetTypes.put("BEND",		"BND");
    	streetTypes.put("BLUF",		"BLF");
    	streetTypes.put("BLUFF",	"BLF");
    	streetTypes.put("BLUFFS",	"BLFS");
    	streetTypes.put("BOT",		"BTM");
    	streetTypes.put("BOTTM",	"BTM");
    	streetTypes.put("BOTTOM",	"BTM");
    	streetTypes.put("BOUL",		"BLVD");
    	streetTypes.put("BOULEVARD",	"BLVD");
    	streetTypes.put("BOULV",	"BLVD");
    	streetTypes.put("BRANCH",	"BR");
    	streetTypes.put("BRDGE",	"BRG");
    	streetTypes.put("BRIDGE",	"BRG");
    	streetTypes.put("BRNCH",	"BR");
    	streetTypes.put("BROOK",	"BRK");
    	streetTypes.put("BROOKS",	"BRKS");
    	streetTypes.put("BURG",		"BG");
    	streetTypes.put("BURGS",	"BGS");
    	streetTypes.put("BYPA",		"BYP");
    	streetTypes.put("BYPAS",	"BYP");
    	streetTypes.put("BYPASS",	"BYP");
    	streetTypes.put("BYPS",		"BYP");
    	streetTypes.put("CAMP",		"CP");
    	streetTypes.put("CANYN",	"CYN");
    	streetTypes.put("CANYON",	"CYN");
    	streetTypes.put("CAPE",		"CPE");
    	streetTypes.put("CAUSEWAY",	"CSWY");
    	streetTypes.put("CAUSWAY",	"CSWY");
    	streetTypes.put("CEN",		"CTR");
    	streetTypes.put("CENT",		"CTR");
    	streetTypes.put("CENTER",	"CTR");
    	streetTypes.put("CENTERS",	"CTRS");
    	streetTypes.put("CENTR",	"CTR");
    	streetTypes.put("CENTRE",	"CTR");
    	streetTypes.put("CIRC",		"CIR");
    	streetTypes.put("CIRCL",	"CIR");
    	streetTypes.put("CIRCLE",	"CIR");
    	streetTypes.put("CIRCLES",	"CIRS");
    	streetTypes.put("CK",		"CRK");
    	streetTypes.put("CLIFF",	"CLF");
    	streetTypes.put("CLIFFS",	"CLFS");
    	streetTypes.put("CLUB",		"CLB");
    	streetTypes.put("CMP",		"CP");
    	streetTypes.put("CNTER",	"CTR");
    	streetTypes.put("CNTR",		"CTR");
    	streetTypes.put("CNYN",		"CYN");
    	streetTypes.put("COMMON",	"CMN");
    	streetTypes.put("COMMONS",	"CMNS");
    	streetTypes.put("CORNER",	"COR");
    	streetTypes.put("CORNERS",	"CORS");
    	streetTypes.put("COURSE",	"CRSE");
    	streetTypes.put("COURT",	"CT");
    	streetTypes.put("COURTS",	"CTS");
    	streetTypes.put("COVE",		"CV");
    	streetTypes.put("COVES",	"CVS");
    	streetTypes.put("CR",		"CRK");
    	streetTypes.put("CRCL",		"CIR");
    	streetTypes.put("CRCLE",	"CIR");
    	streetTypes.put("CRECENT",	"CRES");
    	streetTypes.put("CREEK",	"CRK");
    	streetTypes.put("CRESCENT",	"CRES");
    	streetTypes.put("CRESENT",	"CRES");
    	streetTypes.put("CREST",	"CRST");
    	streetTypes.put("CROSSING",	"XING");
    	streetTypes.put("CROSSROAD",	"XRD");
    	streetTypes.put("CRSCNT",	"CRES");
    	streetTypes.put("CRSENT",	"CRES");
    	streetTypes.put("CRSNT",	"CRES");
    	streetTypes.put("CRSSING",	"XING");
    	streetTypes.put("CRSSNG",	"XING");
    	streetTypes.put("CRT",		"CT");
    	streetTypes.put("CURVE",	"CURV");
    	streetTypes.put("DALE",		"DL");
    	streetTypes.put("DAM",		"DM");
    	streetTypes.put("DIV",		"DV");
    	streetTypes.put("DIVIDE",	"DV");
    	streetTypes.put("DRIV",		"DR");
    	streetTypes.put("DRIVE",	"DR");
    	streetTypes.put("DRIVES",	"DRS");
    	streetTypes.put("DRV",		"DR");
    	streetTypes.put("DVD",		"DV");
    	streetTypes.put("ESTATE",	"EST");
    	streetTypes.put("ESTATES",	"ESTS");
    	streetTypes.put("EXP",		"EXPY");
    	streetTypes.put("EXPR",		"EXPY");
    	streetTypes.put("EXPRESS",	"EXPY");
    	streetTypes.put("EXPRESSWAY",	"EXPY");
    	streetTypes.put("EXPW",		"EXPY");
    	streetTypes.put("EXTENSION",	"EXT");
    	streetTypes.put("EXTENSIONS",	"EXTS");
    	streetTypes.put("EXTN",		"EXT");
    	streetTypes.put("EXTNSN",	"EXT");
    	streetTypes.put("FALLS",	"FLS");
    	streetTypes.put("FERRY",	"FRY");
    	streetTypes.put("FIELD",	"FLD");
    	streetTypes.put("FIELDS",	"FLDS");
    	streetTypes.put("FLAT",		"FLT");
    	streetTypes.put("FLATS",	"FLTS");
    	streetTypes.put("FORD",		"FRD");
    	streetTypes.put("FORDS",	"FRDS");
    	streetTypes.put("FOREST",	"FRST");
    	streetTypes.put("FORESTS",	"FRST");
    	streetTypes.put("FORG",		"FRG");
    	streetTypes.put("FORGE",	"FRG");
    	streetTypes.put("FORGES",	"FRGS");
    	streetTypes.put("FORK",		"FRK");
    	streetTypes.put("FORKS",	"FRKS");
    	streetTypes.put("FORT",		"FT");
    	streetTypes.put("FREEWAY",	"FWY");
    	streetTypes.put("FREEWY",	"FWY");
    	streetTypes.put("FRRY",		"FRY");
    	streetTypes.put("FRT",		"FT");
    	streetTypes.put("FRWAY",	"FWY");
    	streetTypes.put("FRWY",		"FWY");
    	streetTypes.put("GARDEN",	"GDN");
    	streetTypes.put("GARDENS",	"GDNS");
    	streetTypes.put("GARDN",	"GDN");
    	streetTypes.put("GATEWAY",	"GTWY");
    	streetTypes.put("GATEWY",	"GTWY");
    	streetTypes.put("GATWAY",	"GTWY");
    	streetTypes.put("GLEN",		"GLN");
    	streetTypes.put("GLENS",	"GLNS");
    	streetTypes.put("GRDEN",	"GDN");
    	streetTypes.put("GRDN",		"GDN");
    	streetTypes.put("GRDNS",	"GDNS");
    	streetTypes.put("GREEN",	"GRN");
    	streetTypes.put("GREENS",	"GRNS");
    	streetTypes.put("GROV",		"GRV");
    	streetTypes.put("GROVE",	"GRV");
    	streetTypes.put("GROVES",	"GRVS");
    	streetTypes.put("GTWAY",	"GTWY");
    	streetTypes.put("HARB",		"HBR");
    	streetTypes.put("HARBOR",	"HBR");
    	streetTypes.put("HARBORS",	"HBRS");
    	streetTypes.put("HARBR",	"HBR");
    	streetTypes.put("HAVEN",	"HVN");
    	streetTypes.put("HAVN",		"HVN");
    	streetTypes.put("HEIGHT",	"HTS");
    	streetTypes.put("HEIGHTS",	"HTS");
    	streetTypes.put("HGTS",		"HTS");
    	streetTypes.put("HIGHWAY",	"HWY");
    	streetTypes.put("HIGHWY",	"HWY");
    	streetTypes.put("HILL",		"HL");
    	streetTypes.put("HILLS",	"HLS");
    	streetTypes.put("HIWAY",	"HWY");
    	streetTypes.put("HIWY",		"HWY");
    	streetTypes.put("HLLW",		"HOLW");
    	streetTypes.put("HOLLOW",	"HOLW");
    	streetTypes.put("HOLLOWS",	"HOLW");
    	streetTypes.put("HOLWS",	"HOLW");
    	streetTypes.put("HRBOR",	"HBR");
    	streetTypes.put("HT",		"HTS");
    	streetTypes.put("HWAY",		"HWY");
    	streetTypes.put("INLET",	"INLT");
    	streetTypes.put("INTERSTATE",	"I");
    	streetTypes.put("ISLAND",	"IS");
    	streetTypes.put("ISLANDS",	"ISS");
    	streetTypes.put("ISLES",	"ISLE");
    	streetTypes.put("ISLND",	"IS");
    	streetTypes.put("ISLNDS",	"ISS");
    	streetTypes.put("JCTION",	"JCT");
    	streetTypes.put("JCTN",		"JCT");
    	streetTypes.put("JCTNS",	"JCTS");
    	streetTypes.put("JUNCTION",	"JCT");
    	streetTypes.put("JUNCTIONS",	"JCTS");
    	streetTypes.put("JUNCTN",	"JCT");
    	streetTypes.put("JUNCTON",	"JCT");
    	streetTypes.put("KEY",		"KY");
    	streetTypes.put("KEYS",		"KYS");
    	streetTypes.put("KNOL",		"KNL");
    	streetTypes.put("KNOLL",	"KNL");
    	streetTypes.put("KNOLLS",	"KNLS");
    	streetTypes.put("LA",		"LN");
    	streetTypes.put("LAKE",		"LK");
    	streetTypes.put("LAKES",	"LKS");
    	streetTypes.put("LANDING",	"LNDG");
    	streetTypes.put("LANE",		"LN");
    	streetTypes.put("LANES",	"LN");
    	streetTypes.put("LDGE",		"LDG");
    	streetTypes.put("LIGHT",	"LGT");
    	streetTypes.put("LIGHTS",	"LGTS");
    	streetTypes.put("LNDNG",	"LNDG");
    	streetTypes.put("LOAF",		"LF");
    	streetTypes.put("LOCK",		"LCK");
    	streetTypes.put("LOCKS",	"LCKS");
    	streetTypes.put("LODG",		"LDG");
    	streetTypes.put("LODGE",	"LDG");
    	streetTypes.put("LOOPS",	"LOOP");
    	streetTypes.put("MANOR",	"MNR");
    	streetTypes.put("MANORS",	"MNRS");
    	streetTypes.put("MEADOW",	"MDW");
    	streetTypes.put("MEADOWS",	"MDWS");
    	streetTypes.put("MEDOWS",	"MDWS");
    	streetTypes.put("MILL",		"ML");
    	streetTypes.put("MILLS",	"MLS");
    	streetTypes.put("MISSION",	"MSN");
    	streetTypes.put("MISSN",	"MSN");
    	streetTypes.put("MNT",		"MT");
    	streetTypes.put("MNTAIN",	"MTN");
    	streetTypes.put("MNTN",		"MTN");
    	streetTypes.put("MNTNS",	"MTNS");
    	streetTypes.put("MOTORWAY",	"MTWY");
    	streetTypes.put("MOUNT",	"MT");
    	streetTypes.put("MOUNTAIN",	"MTN");
    	streetTypes.put("MOUNTAINS",	"MTNS");
    	streetTypes.put("MOUNTIN",	"MTN");
    	streetTypes.put("MSSN",		"MSN");
    	streetTypes.put("MTIN",		"MTN");
    	streetTypes.put("NECK",		"NCK");
    	streetTypes.put("ORCHARD",	"ORCH");
    	streetTypes.put("ORCHRD",	"ORCH");
    	streetTypes.put("OVERPASS",	"OPAS");
    	streetTypes.put("OVL",		"OVAL");
    	streetTypes.put("PARKS",	"PARK");
    	streetTypes.put("PARKWAY",	"PKWY");
    	streetTypes.put("PARKWAYS",	"PKWY");
    	streetTypes.put("PARKWY",	"PKWY");
    	streetTypes.put("PASSAGE",	"PSGE");
    	streetTypes.put("PATHS",	"PATH");
    	streetTypes.put("PIKES",	"PIKE");
    	streetTypes.put("PINE",		"PNE");
    	streetTypes.put("PINES",	"PNES");
    	streetTypes.put("PK",		"PARK");
    	streetTypes.put("PKWAY",	"PKWY");
    	streetTypes.put("PKWYS",	"PKWY");
    	streetTypes.put("PKY",		"PKWY");
    	streetTypes.put("PLACE",	"PL");
    	streetTypes.put("PLAIN",	"PLN");
    	streetTypes.put("PLAINES",	"PLNS");
    	streetTypes.put("PLAINS",	"PLNS");
    	streetTypes.put("PLAZA",	"PLZ");
    	streetTypes.put("PLCE",		"PL");
    	streetTypes.put("PLZA",		"PLZ");
    	streetTypes.put("POINT",	"PT");
    	streetTypes.put("POINTS",	"PTS");
    	streetTypes.put("PORT",		"PRT");
    	streetTypes.put("PORTS",	"PRTS");
    	streetTypes.put("PRAIRIE",	"PR");
    	streetTypes.put("PRARIE",	"PR");
    	streetTypes.put("PRK",		"PARK");
    	streetTypes.put("PRR",		"PR");
    	streetTypes.put("RAD",		"RADL");
    	streetTypes.put("RADIAL",	"RADL");
    	streetTypes.put("RADIEL",	"RADL");
    	streetTypes.put("RAILROAD",	"RR");
    	streetTypes.put("RANCH",	"RNCH");
    	streetTypes.put("RANCHES",	"RNCH");
    	streetTypes.put("RAPID",	"RPD");
    	streetTypes.put("RAPIDS",	"RPDS");
    	streetTypes.put("RDGE",		"RDG");
    	streetTypes.put("REST",		"RST");
    	streetTypes.put("RIDGE",	"RDG");
    	streetTypes.put("RIDGES",	"RDGS");
    	streetTypes.put("RIVER",	"RIV");
    	streetTypes.put("RIVR",		"RIV");
    	streetTypes.put("RNCHS",	"RNCH");
    	streetTypes.put("ROAD",		"RD");
    	streetTypes.put("ROADS",	"RDS");
    	streetTypes.put("ROUTE",	"RT");
    	streetTypes.put("RVR",		"RIV");
    	streetTypes.put("SHOAL",	"SHL");
    	streetTypes.put("SHOALS",	"SHLS");
    	streetTypes.put("SHOAR",	"SHR");
    	streetTypes.put("SHOARS",	"SHRS");
    	streetTypes.put("SHORE",	"SHR");
    	streetTypes.put("SHORES",	"SHRS");
    	streetTypes.put("SKYWAY",	"SKWY");
    	streetTypes.put("SPNG",		"SPG");
    	streetTypes.put("SPNGS",	"SPGS");
    	streetTypes.put("SPRING",	"SPG");
    	streetTypes.put("SPRINGS",	"SPGS");
    	streetTypes.put("SPRNG",	"SPG");
    	streetTypes.put("SPRNGS",	"SPGS");
    	streetTypes.put("SPURS",	"SPUR");
    	streetTypes.put("SQR",		"SQ");
    	streetTypes.put("SQRE",		"SQ");
    	streetTypes.put("SQRS",		"SQS");
    	streetTypes.put("SQU",		"SQ");
    	streetTypes.put("SQUARE",	"SQ");
    	streetTypes.put("SQUARES",	"SQS");
    	streetTypes.put("STATION",	"STA");
    	streetTypes.put("STATN",	"STA");
    	streetTypes.put("STN",		"STA");
    	streetTypes.put("STR",		"ST");
    	streetTypes.put("STRAV",	"STRA");
    	streetTypes.put("STRAVE",	"STRA");
    	streetTypes.put("STRAVEN",	"STRA");
    	streetTypes.put("STRAVENUE",	"STRA");
    	streetTypes.put("STRAVN",	"STRA");
    	streetTypes.put("STREAM",	"STRM");
    	streetTypes.put("STREET",	"ST");
    	streetTypes.put("STREETS",	"STS");
    	streetTypes.put("STREME",	"STRM");
    	streetTypes.put("STRT",		"ST");
    	streetTypes.put("STRVN",	"STRA");
    	streetTypes.put("STRVNUE",	"STRA");
    	streetTypes.put("SUMIT",	"SMT");
    	streetTypes.put("SUMITT",	"SMT");
    	streetTypes.put("SUMMIT",	"SMT");
    	streetTypes.put("TERR",		"TER");
    	streetTypes.put("TERRACE",	"TER");
    	streetTypes.put("THROUGHWAY",	"TRWY");
    	streetTypes.put("TPK",		"TPKE");
    	streetTypes.put("TR",		"TRL");
    	streetTypes.put("TRACE",	"TRCE");
    	streetTypes.put("TRACES",	"TRCE");
    	streetTypes.put("TRACK",	"TRAK");
    	streetTypes.put("TRACKS",	"TRAK");
    	streetTypes.put("TRAFFICWAY",	"TRFY");
    	streetTypes.put("TRAIL",	"TRL");
    	streetTypes.put("TRAILS",	"TRL");
    	streetTypes.put("TRK",		"TRAK");
    	streetTypes.put("TRKS",		"TRAK");
    	streetTypes.put("TRLS",		"TRL");
    	streetTypes.put("TRNPK",	"TPKE");
    	streetTypes.put("TRPK",		"TPKE");
    	streetTypes.put("TUNEL",	"TUNL");
    	streetTypes.put("TUNLS",	"TUNL");
    	streetTypes.put("TUNNEL",	"TUNL");
    	streetTypes.put("TUNNELS",	"TUNL");
    	streetTypes.put("TUNNL",	"TUNL");
    	streetTypes.put("TURNPIKE",	"TPKE");
    	streetTypes.put("TURNPK",	"TPKE");
    	streetTypes.put("UNDERPASS",	"UPAS");
    	streetTypes.put("UNION",	"UN");
    	streetTypes.put("UNIONS",	"UNS");
    	streetTypes.put("VALLEY",	"VLY");
    	streetTypes.put("VALLEYS",	"VLYS");
    	streetTypes.put("VALLY",	"VLY");
    	streetTypes.put("VDCT",		"VIA");
    	streetTypes.put("VIADCT",	"VIA");
    	streetTypes.put("VIADUCT",	"VIA");
    	streetTypes.put("VIEW",		"VW");
    	streetTypes.put("VIEWS",	"VWS");
    	streetTypes.put("VILL",		"VLG");
    	streetTypes.put("VILLAG",	"VLG");
    	streetTypes.put("VILLAGE",	"VLG");
    	streetTypes.put("VILLAGES",	"VLGS");
    	streetTypes.put("VILLE",	"VL");
    	streetTypes.put("VILLG",	"VLG");
    	streetTypes.put("VILLIAGE",	"VLG");
    	streetTypes.put("VIST",		"VIS");
    	streetTypes.put("VISTA",	"VIS");
    	streetTypes.put("VLLY",		"VLY");
    	streetTypes.put("VST",		"VIS");
    	streetTypes.put("VSTA",		"VIS");
    	streetTypes.put("WALKS",	"WALK");
    	streetTypes.put("WELL",		"WL");
    	streetTypes.put("WELLS",	"WLS");
    	streetTypes.put("WY",		"WAY");
    }
    
    private final static java.util.HashMap geoDifferences = new java.util.HashMap();
    static {
    	geoDifferences.put("SAINT",        "ST");
    	geoDifferences.put("PKWY",        "PKY");
    	geoDifferences.put("OVAL",        "OVL");
    	geoDifferences.put("TRWY",		"THWY");
    	geoDifferences.put("MOUNT",		"MT");
    } 
    
    private static boolean isNewGeocodeFormat() {
    	
    	return FDStoreProperties.isNewGeocodeFormat();
    }


}
