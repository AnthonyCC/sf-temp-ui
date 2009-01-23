package com.freshdirect.cms.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.ContentTypeDefI;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.ContentTypeServiceI;
import com.freshdirect.cms.application.service.CompositeTypeService;
import com.freshdirect.cms.application.service.xml.ContentNodeSerializer;
import com.freshdirect.cms.application.service.xml.FlexContentHandler;
import com.freshdirect.cms.application.service.xml.XmlContentService;
import com.freshdirect.cms.application.service.xml.XmlTypeService;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.StoreModel;

public class ContentFilterTool {

    private static final Logger LOG = Logger.getLogger(ContentFilterTool.class);
    
    private static ContentServiceI service;
    private static ContentTypeServiceI typeService;
    private static boolean includeFeaturedProducts = true;
    
    public static ContentServiceI createContentService() throws IOException {
        if (service == null) {
            LOG.info("content service init.");
            ContentTypeServiceI typeService = createTypeService();

            LOG.info("type service inited.");
            
            File currentDirectory = getCurrentDirectory();
            File destFile = new File(currentDirectory.getParent(), "CMS/data/Store.xml.gz");

            LOG.info("content service init from "+destFile.toURL().toString());
            
            service = new XmlContentService(typeService, new FlexContentHandler(), destFile.toURL().toString());

            LOG.info("content service inited.");

            CmsManager.setInstance(new CmsManager(service, null));

        }
        return service;
    }

    public static ContentTypeServiceI createTypeService() {
        if (typeService==null) {
            List list = new ArrayList();
            list.add(new XmlTypeService("classpath:/com/freshdirect/cms/resource/CMSStoreDef.xml"));
            // list.add(new
            // XmlTypeService("classpath:/com/freshdirect/cms/fdstore/ErpDef.xml"));
            typeService = new CompositeTypeService(list);
        }
        return typeService;
    }

    private static File getCurrentDirectory() {
        String currentDirectory = System.getProperty("user.dir");
        File file = new File(currentDirectory);
        return file;
    }

    public static void writeNodes(File file, final Set contentKeys) throws IOException {
        ContentServiceI contentService = createContentService();

        LOG.info("write content keys:"+contentKeys.size());
        Map nodes = contentService.getContentNodes(contentKeys);

        ContentNodeSerializer serializer = new ContentNodeSerializer() {
            protected boolean filter(ContentTypeDefI typeDef, String name) {
                ContentType type = typeDef.getType();
                if ("FULL_NAME".equals(name)) {
                    return true;
                }
                if (FDContentTypes.PRODUCT.equals(type)) {
                    if ("PRIMARY_HOME".equals(name)) {
                        return true;
                    }
                    if ("brands".equals(name)) {
                        return true;
                    }
                    if ("skus".equals(name)) {
                        return true;
                    }
                    return false;
                }
                if (FDContentTypes.CATEGORY.equals(type)) {
                    if ("subcategories".equals(name)) {
                        return true;
                    }
                    if (includeFeaturedProducts && "FEATURED_PRODUCTS".equals(name)) {
                        return true;
                    }
                    if (includeFeaturedProducts && "products".equals(name)) {
                        return true;
                    }
                    return false;
                }
                if (FDContentTypes.DEPARTMENT.equals(type)) {
                    if ("categories".equals(name)) {
                        return true;
                    }
                    return false;
                }
                if (FDContentTypes.STORE.equals(type)) {
                    if ("departments".equals(name)) {
                        return true;
                    }
                    return false;
                }
                return false;
            }
            protected boolean filterRelationTo(ContentKey key) {
                return contentKeys.contains(key);
            }
            
        };
        List l = new ArrayList(nodes.values());
        Document doc = serializer.visitNodes(l);

        createParentDirectory(file);

        LOG.info("writing out doc to " + file.getPath());
        Writer writer;
        if (file.getName().endsWith(".gz")) {
            writer = new OutputStreamWriter(new GZIPOutputStream(new FileOutputStream(file)));
        } else {
            writer = new FileWriter(file);
        }

        OutputFormat format = new OutputFormat();
        format.setEncoding("UTF-8");
        format.setIndent("\t");
        format.setNewlines(true);

        XMLWriter xw = new XMLWriter(writer, format);
        xw.write(doc);
        xw.flush();
        xw.close();

        LOG.info("finished.");
    }

    private static void createParentDirectory(File file) {
        File directory = file.getParentFile();
        if (directory != null && !directory.exists()) {
            directory.mkdirs();
        }

    }

    private static void addContentKeys(Set contentKeys, ContentType ctype, String keys) {
        String[] strings = StringUtils.split(keys, ',');
        for (int i = 0; i < strings.length; i++) {
            ContentKey ck = new ContentKey(ctype, strings[i].trim());
            //contentKeys.add();
            addContentKeys(contentKeys, ContentFactory.getInstance().getContentNodeByKey(ck) );
        }
    }

    private static void addContentKeys(Set otherKeys, ContentNodeModel nodeModel) {
        if (nodeModel!=null) {
            if (otherKeys.contains(nodeModel.getContentKey())) {
                // ALREADY in the list
                return;
            }
            otherKeys.add(nodeModel.getContentKey());
        } else {
            return;
        }
        if (nodeModel instanceof ProductModel) {
            ProductModel pm = (ProductModel) nodeModel;
            addContentKeys(otherKeys, pm.getParentNode());
            List b = pm.getBrands();
            if (b!=null) {
                for (int i=0;i<b.size();i++) {
                    addContentKeys(otherKeys, (ContentNodeModel) b.get(i));
                }
            }
            List skus = pm.getSkus();
            if (skus!=null) {
                for (int i=0;i<skus.size();i++) {
                    addContentKeys(otherKeys, (ContentNodeModel) skus.get(i));
                }
            }
            if (includeFeaturedProducts) {
                addContentKeys(otherKeys, pm.getPrimaryHome());
            }
        }
        if (nodeModel instanceof CategoryModel) {
            addContentKeys(otherKeys, nodeModel.getParentNode());
            if (includeFeaturedProducts) {
                List featuredProducts = ((CategoryModel)nodeModel).getFeaturedProducts();
                for (int i=0;i<featuredProducts.size();i++) {
                    addContentKeys(otherKeys, (ContentNodeModel) featuredProducts.get(i));
                }
            }
        }
        if (nodeModel instanceof DepartmentModel) {
            addContentKeys(otherKeys, nodeModel.getParentNode());
        }
        if (nodeModel instanceof StoreModel) {
            addContentKeys(otherKeys, nodeModel.getParentNode());
        }
        
       
   }
    
    
    /**
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        createContentService();

        Set filteredContentKeys = new HashSet();
        
/*        addContentKeys(filteredContentKeys, FDContentTypes.PRODUCT, "dai_organi_2_milk_02, dai_organi_1_milk_01, dai_orgval_whlmilk_01, cfncndy_ash_mcrrd, dai_orgval_laclfmilkhlf,"
                        + "dai_orgval_laclfmilkqt, dai_farm_whl_milk_04, dai_nsqare_skim_hg, gro_carn_milk_01, dai_nestle_chocolat_01, dai_organi_nonfat_m_02, dai_nsqare_mlk_hg,"
                        + "gro_goya_coconut_01, dai_farm_wmilkpaper_01, spe_roland_orgcocomilk, dai_nsqare_chcmlk, hba_phillips_mom_01, spe_tasthai_cocmlk, dai_lactaid_oneper, "
                        + "dai_farm_1milkpaper_01, hba_phillips_mom_02, gro_parmalat_white_wh_01, dai_hers_ffchoc, dai_hers_twochcmlk_02, gro_parmalat_white_2_01, gro_parmalat_banan_01, "
                        + "gro_parmalat_straw_01, gro_parmalat_vanil_01, gro_carnat_evaporat_02, gro_parmalat_white_wh_02, gro_parmalat_white_sk_01, dai_farm_2milkpaper_01, "
                        + "dai_farm_fat_milk_01, dai_farm_0milkpaper_01, spe_honees_milk, gro_parmalat_chocolat_01, gro_parmalat_slamvan, dai_orgval_chochlf, gro_parmalat_white_1_01, "
                        + "gro_parmalat_white_2_02, dai_farm_special_1_02, dai_farm_skimplus_02, gro_parmalat_org_01, gro_eagle_borden_01, dai_farm_skimplus_01, gro_parmalat_slamstraw, "
                        + "gro_swiss_milk_cho_01, spe_feod_mlkhzl_01, spe_tasthai_ltcocmlk, gro_carnat_lowfat_e_01, dai_farm_special_1_01, gro_carnat_fat_free_01, dai_rich_lacfremlk, "
                        + "dai_rich_lacfreskm, gro_swiss_milk_cho_02, fro_dove_bar_vanilla_02, spe_feod_mlkalm_01, dai_polly_o_regular_01, dai_polly_o_mozzarel_01, dai_nsqare_rfmlk_hg, "
                        + "dai_polly_o_whole_mi_01, fro_godiva_chocolat_01, gro_milkbone_medium_r_01, dai_lactaid_100_lact_01, fro_david_mousse_01, spe_bahlsen_afrika_m_01, spe_ritter_msalm, "
                        + "gro_alprose_mlkchoc, gro_ovaltine_chocolat_01, spe_vivani_mlkchc, spe_guit_mlkchps, gro_tollho_nestle_03, gro_nestle_chocolat_01, spe_ritter_alp, "
                        + "spe_carrs_mchocteabis_01, ckibni_pecmilk, cfncndy_ash_mcprtz, cfncndy_ash_mcbrk, cfncndy_ash_mcchry, dai_total_whyog_01, spe_ritter_mcras, dai_lactaid_01, "
                        + "spe_ritter_mchaz, spe_bahlsen_fcmchc, spe_ritter_mcpra, spe_lu_ecmlkchc, dai_total_whyog_02, hba_viactiv_mlk_chc, gro_enfamil_powder_m_02, spe_clu_milkbar, "
                        + "spe_ritter_mccrnflk, spe_vivani_mlkchcpr, gro_mrsfields_chcchp, dai_total_greek_sh_01, cfncndy_ash_mcvmar, dai_polly_o_shredded_01, gro_kellogg_coco_cs, "
                        + "dai_lactaid_nonfat_m_01, spe_ritter_mcrum, cfncndy_ash_mcgrah, dai_catham_mapyog, dai_total_honyog, gro_carnat_good_sta_02, dai_catham_shepyog, "
                        + "gro_purina_cat_chow_01, fro_haagen_vanilla_11, gro_duncan_homestyl_02, gro_alprose_bitchoc, gro_kellogg_ffcinn_cs, gro_pfarm_saus, gro_carnat_good_sta_03, "
                        + "dai_vita_soy_plain_so_01, dai_vita_soy_chocolat_02, dai_vita_soy_original_01, dai_vita_soy_chocolat_01, dai_farm_buttermilk_01, gro_westsoy_rice_bev_01, "
                        + "gro_westsoy_westbrae_01, gro_westsoy_drink_va_01, gro_westsoy_lite_pla_01, gro_westsoy_drink_pl_01, gro_westsoy_plus_pla_01, gro_westsoy_lite_van_01, "
                        + "gro_westsoy_van_shak_01, gro_westsoy_cho_shk_01, gro_westsoy_west_soy_01, gro_westsoy_unsweete_01, gro_westsoy_nonfat_p_01, gro_westsoy_plus_van_01");
        
        
        
        writeNodes(new File(getCurrentDirectory().getParent(), "Tests/data/com/freshdirect/cms/fdstore/content/FilteredStore.xml"), filteredContentKeys);

        filteredContentKeys.clear();
        addContentKeys(filteredContentKeys, FDContentTypes.PRODUCT, "dai_organi_2_milk_02, dai_organi_1_milk_01, dai_orgval_whlmilk_01, cfncndy_ash_mcrrd, dai_orgval_laclfmilkhlf");
        writeNodes(new File(getCurrentDirectory().getParent(), "Tests/data/com/freshdirect/cms/fdstore/content/FilteredStore2.xml"), filteredContentKeys);*/

        addContentKeys(filteredContentKeys, FDContentTypes.CATEGORY, "spe_cooki_krcake");
        addContentKeys(filteredContentKeys, FDContentTypes.PRODUCT, "spe_bruces_chcrug,spe_bruces_rain");
        writeNodes(new File(getCurrentDirectory().getParent(), "Tests/data/com/freshdirect/cms/fdstore/content/FeaturedProducts.xml"), filteredContentKeys);        
        
    }


}
