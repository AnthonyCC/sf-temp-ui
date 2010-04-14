/*
 * PoiTest.java
 *
 * Created on March 26, 2002, 2:52 PM
 */

package com.freshdirect.dataloader.nutrition;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.freshdirect.content.nutrition.ErpNutritionModel;
import com.freshdirect.content.nutrition.ErpNutritionType;

/**
 *
 * @author  mrose
 * @version
 */
public class EshaSpreadsheetParser {
    
    public static void main(String[] args) {
        EshaSpreadsheetParser pt = new EshaSpreadsheetParser();
        pt.parseFile("c:/product_data/nutrition/2004_04_06.xls"); 
    }
    
    /** Creates new PoiTest */
    public EshaSpreadsheetParser() {
        nutrition = new ArrayList();
    }
    
    ArrayList nutrition = null;
    
    public ArrayList getNutrition() {
        return this.nutrition;
    }
    
	public void parseFile(String fileName) {
		try {
			FileInputStream fis = new FileInputStream(new File(fileName));
			doParse(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
    
    public void doParse(InputStream fis){
        HSSFWorkbook xls = null;
        try {
            xls = new HSSFWorkbook(fis);
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
            ioe.printStackTrace();
        }
        if (xls == null) {
            return;
        }
        
        int n = xls.getNumberOfSheets();
        for (int i=0; i<n; i++) {
            ErpNutritionModel enm = new ErpNutritionModel();
            ArrayList skuList = new ArrayList();
            HSSFSheet sheet = xls.getSheetAt(i);
            try {
            	boolean hasHeating = false;
                for (Iterator rIter = sheet.rowIterator(); rIter.hasNext(); ) {
                    HSSFRow row = (HSSFRow) rIter.next();
                    HSSFCell testCell = row.getCell((short)0);
                    if ((testCell == null) || (testCell.getCellType() != HSSFCell.CELL_TYPE_STRING)) continue;
                    String testString = testCell.getStringCellValue();
                    if ("".equals(testString.trim())) {
                        continue;
                    } else if (testString.startsWith(productNameKey)) {
                        doProductNameRow(row, enm);
                    } else if (testString.startsWith(servingSizeKey)) {
                        doServingSizeRow(row, enm);
                    } else if (testString.startsWith(servingSizeWeightKey)) {
                        doServingSizeWeightRow(row, enm);
                    } else if (testString.startsWith(servingsPerKey)) {
                        doNumServingsRow(row, enm);
                    } else if (testString.toUpperCase().startsWith(ingredientsKey)) {
                        doIngredientsRow(row, enm);
                    } else if (isNutrientName(testString)) {
                        doNutrientRow(row, enm);
                    } else if (isSkuCode(testString)) {
                        doSkuCode(testString, skuList);
                    } else if (testString.toUpperCase().startsWith(heatingInstrKey)) {
                        doHeatingInstrRow(row, enm);
                    } 
                }
                	
            } catch (RuntimeException re) {
                System.out.println("Unanticipated exception on sheet: " + sheet.getRow((short)1).getCell((short)0).getStringCellValue());
                throw re;
            }
            if (!skuList.isEmpty()) {
                //
                // add info about source of nutrition facts data
                //
                enm.setValueFor(ErpNutritionType.SOURCE, 0.0);
                enm.setUomFor(ErpNutritionType.SOURCE, "ESHA");
                
                for (Iterator sIter = skuList.iterator(); sIter.hasNext(); ) {
                    ErpNutritionModel model = new ErpNutritionModel();
                    model.setSkuCode((String) sIter.next());
                    model.setHeatingInstructions(enm.getHeatingInstructions());
                    model.setHiddenIngredients(enm.getHiddenIngredients());
                    model.setIngredients(enm.getIngredients());
                    for (Iterator nIter = enm.getKeyIterator(); nIter.hasNext(); ) {
                        String key = (String) nIter.next();
                        model.setValueFor(key, enm.getValueFor(key));
                        model.setUomFor(key, enm.getUomFor(key));
                    }
                    nutrition.add(model);
                }
                
            }
            
        }
        
    }
    
    private static String productNameKey = "For:";
    private static String servingSizeKey = "Serving Size ";
    private static String servingSizeWeightKey = "Serving Size:";
    private static String servingsPerKey = "Servings Per Container ";
    private static String ingredientsKey = "INGREDIENTS:";
    private static String heatingInstrKey = "HEATING INSTRUCTIONS:";
    
    protected void doProductNameRow(HSSFRow row, ErpNutritionModel nutrition) {
        String pName = row.getCell((short)0).getStringCellValue();
        pName = pName.substring(productNameKey.length()).trim();
        //System.out.println("\n\n"+pName);
    }
    
    protected void doServingSizeRow(HSSFRow row, ErpNutritionModel nutrition) {
        String sSize = row.getCell((short)0).getStringCellValue();
        sSize = sSize.substring(servingSizeKey.length()).trim();
        int parenPos = sSize.indexOf("(");
        if (parenPos > -1) {
            sSize = sSize.substring(0, parenPos).trim();
        }
        //System.out.println(sSize);
        String numPart = "0";
        String otherPart = "";
        int pos = sSize.indexOf(" ");
        if (pos > -1) {
            numPart = sSize.substring(0, pos);
            otherPart = sSize.substring(pos);
        }

        try {
            nutrition.setValueFor(ErpNutritionType.SERVING_SIZE, Double.parseDouble(numPart));
        } catch (NumberFormatException nfe) {
            int slashPos = numPart.indexOf("/");
            nutrition.setValueFor(ErpNutritionType.SERVING_SIZE, Double.parseDouble(numPart.substring(0, slashPos)) / Double.parseDouble(numPart.substring(slashPos+1, numPart.length())));
        }
        if (nutrition.getUomFor(ErpNutritionType.SERVING_SIZE) == null) {
            nutrition.setUomFor(ErpNutritionType.SERVING_SIZE, otherPart);
        } else {
            nutrition.setUomFor(ErpNutritionType.SERVING_SIZE, otherPart + " " + nutrition.getUomFor(ErpNutritionType.SERVING_SIZE));
        }
    }
    
    protected void doServingSizeWeightRow(HSSFRow row, ErpNutritionModel nutrition) {
        String ssW = row.getCell((short)1).getStringCellValue().trim();
        int parenPos = ssW.indexOf("(");
        if (parenPos > -1) {
            ssW = ssW.substring(0, parenPos);
        }
        ssW = ssW.trim();
        //System.out.println(ssW);
        int spPos = ssW.indexOf(" ");
        if (spPos < 0) return;
        String ssWval = ssW.substring(0, spPos);
        String ssWuom = ssW.substring(spPos+1);
        nutrition.setValueFor(ErpNutritionType.SERVING_WEIGHT, Double.parseDouble(ssWval));
        nutrition.setUomFor(ErpNutritionType.SERVING_WEIGHT, ssWuom);
    }
    
    protected void doNumServingsRow(HSSFRow row, ErpNutritionModel nutrition) {
        String numServ = row.getCell((short)0).getStringCellValue();
        numServ = numServ.substring(servingsPerKey.length()).trim();
        //System.out.println(numServ);
        try {
            nutrition.setValueFor(ErpNutritionType.NUMBER_OF_SERVINGS, Double.parseDouble(numServ));
        } catch (NumberFormatException nfe) {
            nutrition.setValueFor(ErpNutritionType.NUMBER_OF_SERVINGS, 0);
            nutrition.setUomFor(ErpNutritionType.NUMBER_OF_SERVINGS, numServ);
        }
    }
    
    protected void doIngredientsRow(HSSFRow row, ErpNutritionModel nutrition) {
        String ing = row.getCell((short)0).getStringCellValue();
        ing = ing.substring(ing.indexOf(":")+1).trim();
        //System.out.println("Ingredients are " + ing);
        nutrition.setIngredients(ing);
    }
    
    protected boolean isNutrientName(String name) {
        for (Iterator nIter = ErpNutritionType.getTypesIterator(); nIter.hasNext(); ) {
            String nName = (String) nIter.next();
            ErpNutritionType.Type entt = ErpNutritionType.getType(nName);
            if (entt.getDisplayName().toUpperCase().startsWith(name.toUpperCase().trim())) {
                return true;
            }
        }
        return false;
    }
    
    protected ErpNutritionType.Type getNutritionType(String name) {
        for (Iterator nIter = ErpNutritionType.getTypesIterator(); nIter.hasNext(); ) {
            String nName = (String) nIter.next();
            ErpNutritionType.Type entt = ErpNutritionType.getType(nName);
            if (entt.getDisplayName().toUpperCase().equals(name.toUpperCase().trim())) {
                return entt;
            }
        }
        return null;
    }
    
    protected void doNutrientRow(HSSFRow row, ErpNutritionModel nutrition) {
        HSSFCell nnameCell = row.getCell((short)0);
        HSSFCell quantCell = row.getCell((short)1);
        HSSFCell valueCell = row.getCell((short)2);
        //System.out.println(nnameCell.getStringCellValue());
        if (quantCell != null) {
            ErpNutritionType.Type nType = getNutritionType(nnameCell.getStringCellValue().trim() + " quantity");
            if (nType == null) nType = getNutritionType(nnameCell.getStringCellValue().trim());
            if (quantCell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                //System.out.println(quantCell.getNumericCellValue());
                nutrition.setValueFor(nType.getName(), quantCell.getNumericCellValue());
            } else if (quantCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                String quant = quantCell.getStringCellValue().trim();
                //System.out.println(quantCell.getStringCellValue());
                String quantVal = "";
                String quantUom = "";
                if ("".equals(quant)) {
                    return;
                } else if (!quant.toUpperCase().startsWith("LESS")) {
                    int pos = -1;
                    for (int i=0; i<quant.length(); i++) {
                        if (Character.isLetter(quant.charAt(i))) {
                            pos = i;
                            break;
                        }
                    }
                    try {
                        quantVal = quant.substring(0, pos);
                        quantUom = quant.substring(pos);
                    } catch (RuntimeException re) {
                        System.out.println("Error in data for: " + nType.getDisplayName() + " --> \"" + quant + "\"");
                        throw re;
                    }
                } else {
                    for (int i=0; i<quant.length(); i++) {
                        if (Character.isDigit(quant.charAt(i)) || '.' == quant.charAt(i)) {
                            quantVal += quant.charAt(i);
                        }
                    }
                    quantVal = "-" + quantVal;
                    quantUom = nType.getUom();
                }
                nutrition.setValueFor(nType.getName(), Double.parseDouble(quantVal));
                nutrition.setUomFor(nType.getName(), quantUom);
            }
        }
        if (valueCell != null) {
            //System.out.println("\"" + nnameCell.getStringCellValue().trim() + "\"");
            ErpNutritionType.Type nType = getNutritionType(nnameCell.getStringCellValue().trim() + " value");
            if (nType == null) nType = getNutritionType(nnameCell.getStringCellValue().trim());
            //System.out.println(nType);
            if (valueCell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                //System.out.println(valueCell.getNumericCellValue());
                nutrition.setValueFor(nType.getName(), 100. * valueCell.getNumericCellValue());
                nutrition.setUomFor(nType.getName(), "%");
            } else if (valueCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                //System.out.println(valueCell.getStringCellValue());
            }
        }
    }
    //private static String[] skuPrefixes = {"VEG", "FRU", "YEL", "MEA", "SEA", "DEL", "CAN", "COF", "TEA", "GRO", "FRO", "SPE", "HMR", "BAK", "PAS", "CHE", "DAI", "KOS", "WIN", "HBA", "VAR"};
    private static String[] skuPrefixes = {"BAK","CAN","CAT","CBL","CHE","COF","DAI","DEL","FDM","FRO","FRU","GRO","HBA","HMR","KOS","MEA","MKT","PAS","SEA","SMP","SPE","TEA","TST","VAR","VEG","YEL"};
    
    protected boolean isSkuCode(String skuCode) {
        for (int i=0; i<skuPrefixes.length; i++) {
            if (skuCode.startsWith(skuPrefixes[i]) && Character.isDigit(skuCode.charAt(skuPrefixes[i].length()))) {
                return true;
            }
        }
        return false;
    }
    
    protected void doSkuCode(String skuCodeList, ArrayList list) {
        StringTokenizer stoke = new StringTokenizer(skuCodeList, " ");
        while (stoke.hasMoreTokens()) {
            String code = stoke.nextToken();
            if (isSkuCode(code)) {
                list.add(code);
            }
        }
    }
    
    protected void doHeatingInstrRow(HSSFRow row, ErpNutritionModel nutrition) {
        String ins = row.getCell((short)0).getStringCellValue();
        ins = ins.substring(ins.indexOf(":")+1).trim();
        //System.out.println(ins);
        nutrition.setHeatingInstructions(ins);
    }
    
    
}
