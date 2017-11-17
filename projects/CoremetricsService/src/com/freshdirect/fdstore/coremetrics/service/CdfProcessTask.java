package com.freshdirect.fdstore.coremetrics.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;

import com.freshdirect.affiliate.ExternalAgency;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.coremetrics.CmContext;
import com.freshdirect.fdstore.coremetrics.CmFacade;
import com.freshdirect.fdstore.coremetrics.CmInstance;
import com.freshdirect.fdstore.coremetrics.builder.PageViewTagModelBuilder;
import com.freshdirect.fdstore.coremetrics.builder.PageViewTagModelBuilder.CustomCategory;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.framework.event.EnumEventSource;
import com.freshdirect.framework.util.RuntimeServiceUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.ContentNodeI;
import com.freshdirect.storeapi.application.CmsManager;
import com.freshdirect.storeapi.content.CategoryModel;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.ContentNodeModel;
import com.freshdirect.storeapi.content.DepartmentModel;
import com.freshdirect.storeapi.content.ProductContainer;
import com.freshdirect.storeapi.content.SuperDepartmentModel;
import com.freshdirect.storeapi.fdstore.FDContentTypes;
import com.freshdirect.storeapi.node.ContentNodeUtil;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class CdfProcessTask {

    private static final Logger LOGGER = LoggerFactory.getInstance(CdfProcessTask.class);

    private List<CdfRowModel> cdfRowModels = new ArrayList<CdfRowModel>();
    private String cdfFilePath;
    private String cdfFileName;

    // New Hack for fixing the Parent Category ID
    private Map<String, String> catToParentCatMapping = new HashMap<String, String>();

    private final CmContext context;
    private final String clientId;

    public CdfProcessTask() {
        this(null);
    }

    public CdfProcessTask(CmContext ctx) {
        this.context = ctx != null ? ctx : CmContext.getContext();

        this.clientId = this.context.getClientId();
    }

    public CdfProcessResult process() {
        LOGGER.info("CDF process started with context: " + context.getInstance());

        mapCatToParentCat();
        generateCdfModel();
        prefixCategoryIds();
        try {
            saveCdfFile();
            uploadFile();

        } catch (FDResourceException e) {
            LOGGER.error("CdfProcessTask failed", e);
            return new CdfProcessResult(false, e.getMessage());
        }

        LOGGER.info("CdfProcessTask complete");
        return new CdfProcessResult(true, null);
    }

	private void mapCatToParentCat() {
        catToParentCatMapping.put(EnumEventSource.QUICKSHOP.toString(), EnumEventSource.REORDER.toString());
        catToParentCatMapping.put(EnumEventSource.qs_pastOrders.toString(), EnumEventSource.REORDER.toString());
        catToParentCatMapping.put(EnumEventSource.qs_customerLists.toString(), EnumEventSource.REORDER.toString());
        catToParentCatMapping.put(EnumEventSource.qs_fdLists.toString(), EnumEventSource.REORDER.toString());
        catToParentCatMapping.put(EnumEventSource.qs_ymal.toString(), EnumEventSource.REORDER.toString());
        catToParentCatMapping.put(EnumEventSource.qs_tabbedRecommender.toString(), EnumEventSource.REORDER.toString());
    }

    private CdfRowModel buildModel(String categoryId, String categoryName, String parentCategoryId) {
        return new CdfRowModel(this.clientId, categoryId, categoryName, parentCategoryId);
    }

    private void generateCdfModel() {

        Set<String> categoryKeys = new HashSet<String>();

        // [APPDEV-2907] Add mobile keys
        addMobileKeys();

        for (String catIdDir : FDStoreProperties.getCoremetricsCatIdDirs().split(",")) {
            addCmPageViewTagCategory(catIdDir, categoryKeys);
        }

        addCmPageViewTagCategory(FDStoreProperties.getCoremetricsCatIdBlog(), categoryKeys);
        addCmPageViewTagCategory(FDStoreProperties.getCoremetricsCatIdOtherPage(), categoryKeys);

        for (CustomCategory category : PageViewTagModelBuilder.CustomCategory.values()) {
            addCmPageViewTagCategory(category.toString(), categoryKeys);
        }

        // event source used in shop tags as category
        for (EnumEventSource eventEnum : EnumEventSource.values()) {
            addCmPageViewTagCategory(eventEnum.toString(), categoryKeys);
        }

        // [APPDEV-2907] site feature used in shop tags as category
        addSiteFeatureKeys(EnumSiteFeature.getEnumList());

        // external agency, e.g. Foodily
        for (ExternalAgency externalAgency : ExternalAgency.values()) {
            addCmPageViewTagCategory(externalAgency.toString(), categoryKeys);
        }

        // HomepageRedesign block
        addNewHomepageModuleKeys();

        // CMS block

        final CmsManager svc = CmsManager.getInstance();

        if (CmInstance.GLOBAL == context.getInstance()) {

            // Get available stores
            final Set<ContentKey> storeKeys = svc.getContentKeysByType(ContentType.Store);

            for (final ContentKey theStoreKey : storeKeys) {
                Set<ContentKey> processedDeptKeys = new HashSet<ContentKey>();

                ContentNodeI store = svc.getContentNode(theStoreKey);
                if (store == null)
                    continue;

                List<ContentKey> superDeptKeys = (List<ContentKey>) store.getAttributeValue("superDepartments");
                if (superDeptKeys != null) {
                    for (final ContentKey superDeptKey : superDeptKeys) {
                        ContentNodeI superDept = svc.getContentNode(superDeptKey);

                        // add superdept
                        cdfRowModels.add(buildModel(superDeptKey.getId(), (String) superDept.getAttributeValue("FULL_NAME"), null));

                        // process depts reachable through superdepts
                        List<ContentKey> deptKeys = (List<ContentKey>) superDept.getAttributeValue("departments");
                        if (deptKeys != null) {
                            for (final ContentKey deptKey : deptKeys) {
                                if (processedDeptKeys.add(deptKey)) {
                                    processCmsNode(deptKey, superDeptKey);
                                }
                            }
                        }

                        // process the rest
                        List<ContentKey> deptKeysRem = (List<ContentKey>) store.getAttributeValue("departments");
                        deptKeysRem.removeAll(processedDeptKeys);
                        for (final ContentKey deptKey : deptKeysRem) {
                            processCmsNode(deptKey, null);
                        }
                    }
                } else {
                    // no super departmens, just walk down the old path
                    List<ContentKey> deptKeysRem = (List<ContentKey>) store.getAttributeValue("departments");
                    for (final ContentKey deptKey : deptKeysRem) {
                        processCmsNode(deptKey, null);
                    }
                }

            }

        } else {
            // super department flow
            Set<DepartmentModel> processedDepartments = new HashSet<DepartmentModel>();

            for (ContentKey superDeptKey : svc.getContentKeysByType(ContentType.SuperDepartment)) {
                ContentNodeModel superDeptNode = ContentFactory.getInstance().getContentNodeByKey(superDeptKey);

                if (superDeptNode instanceof SuperDepartmentModel) {
                    SuperDepartmentModel superDept = (SuperDepartmentModel) superDeptNode;
                    String superDeptId = superDept.getContentName();
                    cdfRowModels.add(buildModel(superDeptId, superDept.getFullName(), null));

                    for (DepartmentModel dept : superDept.getDepartments()) {
                        if (processedDepartments.add(dept)) {
                            processCmsCategory(dept, superDeptId);
                        }
                    }
                }
            }

            // remaining departments
            for (DepartmentModel dept : ContentFactory.getInstance().getStore().getDepartments()) {
                if (!processedDepartments.contains(dept)) {
                    processCmsCategory(dept, null);
                }
            }
        }
    }

    private void addNewHomepageModuleKeys() {
        final CmsManager cmsManager = CmsManager.getInstance();

        if (CmInstance.GLOBAL == context.getInstance() || CmInstance.FDW == context.getInstance()) {
            // Add first level category
            cdfRowModels.add(buildModel("NEW HOME PAGE CAROUSELS", "NEW HOME PAGE CAROUSELS", null));

            // Get Homepage active moduleContainerContentKeys
            List<String> homepageModuleContainerContentKeys = new ArrayList<String>();
            String newUserModuleContainer = FDStoreProperties.getHomepageRedesignNewUserContainerContentKey();
            String currentUserModuleContainer = FDStoreProperties.getHomepageRedesignCurrentUserContainerContentKey();

            if (newUserModuleContainer != currentUserModuleContainer) {
                homepageModuleContainerContentKeys.add(newUserModuleContainer);
                homepageModuleContainerContentKeys.add(currentUserModuleContainer);
            } else {
                homepageModuleContainerContentKeys.add(newUserModuleContainer);
            }

            // Generate CDF row models for ModuleContainers
            for (String moduleContainerContentKey : homepageModuleContainerContentKeys) {
                ContentNodeI moduleContainer = cmsManager.getContentNode(ContentKeyFactory.get(moduleContainerContentKey));
                String moduleContainerName = ContentNodeUtil.getStringAttribute(moduleContainer, "name");

                List<ContentKey> moduleContentKeysWithPossibleDuplicates = generateModuleContentKeys(moduleContainer);
                int moduleCount = moduleContentKeysWithPossibleDuplicates.size();
                // Generate cdfRowModels with positions and original length and moduleContainer id
                createCDFRowModelFromModuleContentKeys(moduleContentKeysWithPossibleDuplicates, moduleContainerContentKey.split(":")[1], moduleContainerName, moduleCount);
            }

        }

    }

    private List<ContentKey> generateModuleContentKeys(ContentNodeI moduleContainer) {
        final CmsManager cmsManager = CmsManager.getInstance();
        List<ContentKey> contentKeys = new ArrayList<ContentKey>();

        if (moduleContainer != null) {
            List<ContentKey> modulesAndGroups = (List<ContentKey>) moduleContainer.getAttributeValue("modulesAndGroups");
            for (ContentKey contentKey : modulesAndGroups) {
                if (ContentType.Module == contentKey.type) {
                    contentKeys.add(contentKey);
                } else if (ContentType.ModuleGroup == contentKey.type) {
                    ContentNodeI moduleGroup = cmsManager.getContentNode(contentKey);
                    List<ContentKey> moduleContentKeys = (List<ContentKey>) moduleGroup.getAttributeValue("modules");
                    for (ContentKey moduleContentKey : moduleContentKeys) {
                        contentKeys.add(moduleContentKey);
                    }
                }
            }
        }

        return contentKeys;
    }

    private void createCDFRowModelFromModuleContentKeys(List<ContentKey> contentkeys, String moduleContainerId, String moduleContainerName, int originalModuleCount) {
        final CmsManager cmsManager = CmsManager.getInstance();

        Set<ContentKey> moduleContentKeysWithoutDuplication = new HashSet<ContentKey>();

        for (ContentKey contentKey : contentkeys) {
            if (moduleContentKeysWithoutDuplication.add(contentKey)) {
                ContentNodeI module = cmsManager.getContentNode(contentKey);
                String moduleName = ContentNodeUtil.getStringAttribute(module, "name");
                String moduleContentKeyId = contentKey.getId();
                for (int i = 1; i < originalModuleCount + 1; i++) {
                    String moduleContentKeyWithPosition = moduleContainerId + ":POSITION " + i + ":" + moduleContentKeyId;
                    String moduleNameWithPosition = moduleContainerName + ":POSITION " + i + ":" + moduleName;
                    cdfRowModels.add(buildModel(moduleContentKeyWithPosition, moduleNameWithPosition, "NEW HOME PAGE CAROUSELS"));
                }
            }
        }
    }

    private void addCmPageViewTagCategory(String catId, Set<String> categoryKeys) {

        if (categoryKeys.add(catId.toUpperCase())) {
            cdfRowModels.add(buildModel(catId, "Category: " + catId, catToParentCatMapping.containsKey(catId) ? catToParentCatMapping.get(catId) : null));
        }
    }

    private void addSiteFeatureKeys(Collection<EnumSiteFeature> siteFeatures) {
        for (EnumSiteFeature f : siteFeatures) {
            final String key = f.getName();
            cdfRowModels.add(buildModel(key, "SmartStore: " + key, null));
        }
    }

    private void addMobileKeys() {
        final CmInstance inst = context.getInstance();

        if (inst != CmInstance.GLOBAL && !inst.getFacade().isMobile()) {
            // Nothing to see here ... leave the scene now!
            return;
        }

        // [id],MOBILE,"MOBILE", <---- there is no parent MOBILE category today
        // [id],MOBILE_OTHER,"MOBILE OTHER",MOBILE
        // [id],MOBILE_ALL,"MOBILE ALL",MOBILE

        cdfRowModels.add(buildModel("MOBILE", "Category: MOBILE", null));
        cdfRowModels.add(buildModel("MOBILE_OTHER", "Category: MOBILE_OTHER", "MOBILE"));
        cdfRowModels.add(buildModel("MOBILE_ALL", "Category: MOBILE ALL", "MOBILE"));

        // APPDEV-4008 CDF File Updates for Coremetrics with New Ipad App
        if (inst == CmInstance.GLOBAL || CmFacade.TABLET == inst.getFacade()) {
            cdfRowModels.add(buildModel("ME", "ME", null));
            cdfRowModels.add(buildModel("MY_ITEMS", "MY ITEMS", "ME"));
            cdfRowModels.add(buildModel("MY_ORDERS", "MY ORDERS", "ME"));
            cdfRowModels.add(buildModel("MY_SHOPPING_LISTS", "MY LISTS", "ME"));

            cdfRowModels.add(buildModel("IDEAS", "IDEAS", null));
            cdfRowModels.add(buildModel("IDEAS_SHOPPINGSHORTCUTS", "SHOPPING SHORTCUTS", "IDEAS"));
            cdfRowModels.add(buildModel("IDEAS_PRODUCERS", "PRODUCERS", "IDEAS"));
            cdfRowModels.add(buildModel("IDEAS_DISHES", "DELICIOUS DISHES", "IDEAS"));
        }
    }

    private void processCmsNode(ContentKey aKey, ContentKey parentKey) {
        final CmsManager svc = CmsManager.getInstance();

        ContentNodeI node = svc.getContentNode(aKey);
        if (node == null)
            return;

        // process current node
        cdfRowModels.add(buildModel(aKey.getId(), (String) node.getAttributeValue("FULL_NAME"), parentKey != null ? parentKey.getId() : null));

        // descend
        final ContentType t = aKey.getType();
        List<ContentKey> subKeys = null;
        if (FDContentTypes.DEPARTMENT.equals(t)) {
            subKeys = (List<ContentKey>) node.getAttributeValue("categories");
        } else if (FDContentTypes.CATEGORY.equals(t)) {
            subKeys = (List<ContentKey>) node.getAttributeValue("subcategories");
        }

        if (subKeys != null) {
            for (ContentKey subKey : subKeys) {
                processCmsNode(subKey, aKey);
            }
        }

    }

    private void processCmsCategory(ProductContainer cat, String parentCatId) {

        String catId = cat.getContentKey().getId();
        cdfRowModels.add(buildModel(catId, cat.getFullName(), parentCatId));

        List<CategoryModel> subCats = cat.getSubcategories();
        if (subCats != null) {
            for (ProductContainer subCat : subCats) {
                processCmsCategory(subCat, catId);
            }
        }
    }

    private void prefixCategoryIds() {
        // Category IDs are not prefixed in global context
        if (CmInstance.GLOBAL == context.getInstance()) {
            return;
        }

        final String prefix = context.getInstance().name();
        for (CdfRowModel model : cdfRowModels) {
            model.prefixCategoryId(prefix);
        }
    }

    private void saveCdfFile() throws FDResourceException {
        String rootDirectory = RuntimeServiceUtil.getInstance().getRootDirectory();
        cdfFileName = "CDF_" + clientId + ".csv";
        cdfFilePath = rootDirectory + File.separator + cdfFileName;
        LOGGER.info("saving Coremetrics CDF to " + cdfFilePath);

        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new FileWriter(cdfFilePath));

            for (CdfRowModel cdfRowModel : cdfRowModels) {
                writer.println(cdfRowModel.toString());
            }

            writer.close();
        } catch (IOException e) {
            throw new FDResourceException("saveCdfFile failed: " + e.getMessage(), e);

        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    private void uploadFile() throws FDResourceException {

        String ftpUrl = FDStoreProperties.getCoremetricsFtpUrl();
        String ftpUser = clientId + "-import";
        String ftpPassword = FDStoreProperties.getCoremetricsFtpPassword();
        int sftpPort = FDStoreProperties.getCoremetricsFtpSftpPort();
        boolean secure = FDStoreProperties.isCoremetricsFtpSecure();

        LOGGER.info("uploading Coremetrics CDF to " + ftpUser + "@" + ftpUrl + " (via " + (secure ? "sftp on port " + sftpPort : "ftp") + ")");

        if (secure) {

            ChannelSftp sftp = null;
            Session session = null;

            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");

            JSch jsch = new JSch();
            try {
                session = jsch.getSession(ftpUser, ftpUrl, sftpPort);
                session.setPassword(ftpPassword);
                session.setConfig(config);

                session.connect();
                sftp = (ChannelSftp) session.openChannel("sftp");

                LOGGER.debug("SFTP: Connecting..");
                sftp.connect();

                sftp.put(cdfFilePath, cdfFileName);

                sftp.disconnect();
                session.disconnect();

            } catch (JSchException e) {
                throw new FDResourceException("sftp uploadFile: " + e.getMessage(), e);

            } catch (SftpException e) {
                throw new FDResourceException("sftp uploadFile: " + e.getMessage(), e);

            } finally {
                if (null != sftp && sftp.isConnected()) {
                    LOGGER.debug("SFTP: disconnecting");
                    sftp.disconnect();
                }

                if (null != session && session.isConnected()) {
                    LOGGER.debug("SFTP: disconnecting");
                    session.disconnect();
                }
            }

        } else {

            FTPClient client = new FTPClient();
            client.setDefaultTimeout(600000);
            client.setDataTimeout(600000);

            FileInputStream fis = null;

            try {
                client.connect(ftpUrl);

                if (!client.login(ftpUser, ftpPassword)) {
                    throw new FDResourceException("ftp login failed");
                }
                client.enterLocalPassiveMode();

                fis = new FileInputStream(cdfFilePath);
                if (!client.storeFile(cdfFileName, fis)) {
                    throw new FDResourceException("ftp file store failed");
                }

                client.logout();

            } catch (IOException e) {
                throw new FDResourceException("ftp uploadFile: " + e.getMessage(), e);

            } finally {
                try {
                    if (fis != null) {
                        fis.close();
                    }
                    client.disconnect();
                } catch (IOException e) {
                }
            }
        }
    }
}
