package com.freshdirect.cms.search;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.draft.service.DraftService;
import com.freshdirect.cms.core.CmsDaoFactory;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.cms.merge.MergeResult;
import com.freshdirect.cms.merge.MergeTask;
import com.freshdirect.cms.merge.ValidationResult;
import com.freshdirect.cms.publish.EnumPublishStatus;
import com.freshdirect.cms.publish.Publish;
import com.freshdirect.cms.publish.PublishDao;
import com.freshdirect.cms.publish.PublishMessage;
import com.freshdirect.cms.publish.PublishTask;
import com.freshdirect.cms.publish.PublishX;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentSearch;
import com.freshdirect.framework.util.log.LoggerFactory;

public class BackgroundProcessorImpl implements IBackgroundProcessor {
    
    private static abstract class CallableWithNotifications<X> implements Callable<X> {
    	private final static Logger LOG = LoggerFactory.getInstance(CallableWithNotifications.class);

        BackgroundStatus status;

        public CallableWithNotifications(BackgroundStatus status) {
            this.status = status;
        }

        abstract X doCall() throws Exception;
        
        abstract void callFinished(X result);
        
        @Override
        public final X call() throws Exception {
            try {
            	LOG.info("started background process");
                status.notifyStart();
                X result = doCall();
                callFinished(result);
            	LOG.info("completed background process");
                status.notifiyFinished();
                return result;
            } catch (Exception e) {
                status.notifiyError(e);
                LOG.error("background process failure" , e);
                throw e;
            }
        }
    }
    
    private final static Logger LOG = LoggerFactory.getInstance(BackgroundProcessorImpl.class);

    private ExecutorService reindexer;
    private ContentSearchServiceI searchService;
    private BackgroundStatus procStatus = new BackgroundStatus();
    private List<PublishTask> publishTasks;
    private MergeTask mergeTask;
    
    public BackgroundProcessorImpl () {
    }

    
    public void setSearchService(ContentSearchServiceI searchService) {
        this.searchService = searchService;
    }
    
    
    public ContentSearchServiceI getSearchService() {
        return searchService;
    }

    @Override
    public void setPublishTasks(List<PublishTask> publishTasks) {
        this.publishTasks = publishTasks;
    }

    @Override
    public List<PublishTask> getPublishTasks() {
            return publishTasks;
    }

    @Override
    public void setMergeTask(MergeTask mergeTask) {
        this.mergeTask = mergeTask;
    }

    PublishDao getPublishDao() {
        return CmsDaoFactory.getInstance().getPublishDao();
    }
    

    @Override
    public Future<Integer> backgroundReindex(final Collection<String> terms) {
        return initThreads().submit(new CallableWithNotifications<Integer>(procStatus) {
            @Override
            public void callFinished(Integer result) {
                status.setElapsedTime(System.currentTimeMillis() - status.getStarted());
                String s = "finished reindexing by " +terms.size()+" terms, in "+ (status.getElapsedTime() / 1000) + " sec";
                status.setStatus(s);
                status.setLastReindexResult(s);
            }

            @Override
            public Integer doCall() throws Exception {
                Set<ContentKey> keys = new HashSet<ContentKey>();
                for (String t : terms) {
                    status.setStatus("Search nodes matching '" + t + "'");
                    Collection<SearchHit> search = getSearchService().search(t, false, 10000);
                    for (SearchHit hit : search) {
                        keys.add(hit.getContentKey());
                    }
                }
                status.setStatus("Loading " + keys.size() + " nodes");
                Map<ContentKey, ContentNodeI> nodes = CmsManager.getInstance().getContentNodes(keys);
                final Collection<ContentNodeI> values = nodes.values();
                status.setStatus("Reindexing " + values.size() + " nodes");
                getSearchService().index(values, false);
                status.setStatus("Finished.");
                return values.size();
            }
        });
    }

    @Override
    public Future<Integer> backgroundReindex() {
        return initThreads().submit(new CallableWithNotifications<Integer>(procStatus) {
            
            @Override
            public void callFinished(Integer result) {
                status.setElapsedTime(System.currentTimeMillis() - status.getStarted());
                status.setStatus("finished in " + (status.getElapsedTime() / 1000) + " sec");
                status.setLastReindexResult("indexed " + result + " nodes in " + (status.getElapsedTime() / 1000) + " sec");
            }
            
            @Override
            public Integer doCall() throws Exception {
                Set<ContentKey> keys = new HashSet<ContentKey>();
                CmsManager instance = CmsManager.getInstance();
                for (Iterator<ContentType> i = getSearchService().getIndexedTypes().iterator(); i.hasNext();) {
                    ContentType type = i.next();
                    keys.addAll(instance.getContentKeysByType(type));
                    status.setStatus("loading " + keys.size() + " keys");
                }

                status.setStatus("loading " + keys.size() + " nodes");
                Map<ContentKey, ContentNodeI> nodes = instance.getContentNodes(keys);
                status.setStatus("setting up synonym dictionary");
                status.setStatus("indexing " + nodes.values().size() + " nodes");
                getSearchService().index(nodes.values(), true);
                status.setStatus("indexing spelling of " + nodes.values().size() + " nodes");
                getSearchService().indexSpelling(nodes.values());
                status.setStatus("optimizing index");
                getSearchService().optimize();
                status.setStatus("refreshing relevancy scores. ");

                ContentSearch.getInstance().refreshRelevancyScores();
                status.setStatus("refresh complete.");
                return nodes.values().size();
            }

        });
    }    
    
    @Override
    public Future<Integer> rebuildWineIndex() {
        return initThreads().submit(new CallableWithNotifications<Integer>(procStatus) {
            @Override
            public void callFinished(Integer result) {
                status.setElapsedTime(System.currentTimeMillis() - status.getStarted());
                status.setStatus("completed wine index rebuild");
                status.setLastReindexResult("indexed " + result + " wine products in " + (status.getElapsedTime() / 1000) + " sec");
            }
            
            @Override
            Integer doCall() throws Exception {
                status.setStatus("wine index rebuild");
                ContentFactory.getInstance().refreshWineIndex(true);
                
                return ContentFactory.getInstance().getAllWineProductKeys().size();
            }
        });
    }
    
    @Override
    public void executePublish(final Publish publish) {
        initThreads().submit(new CallableWithNotifications<Publish>(procStatus) {
            @Override
            Publish doCall() {
                PublishDao publishDao = getPublishDao();
                try {

                	if (publishTasks != null) {
                		// Go through each Stores
                		Collection<ContentKey> storeKeys = CmsManager.getInstance().getContentKeysByType(FDContentTypes.STORE);
                		final int n = storeKeys.size();
                		int k=0;
                		for (final ContentKey storeKey : storeKeys) {
                			k++;
                			publish.setStoreId(storeKey.getId());
                			publish.setPath(publish.getBasePath() + "/" + publish.getStoreId());
                			LOG.info("=== Start publish for store: " + publish.getStoreId() + "  ("+k+"/"+n+") ===");
		                    for (PublishTask task : publishTasks) {
		                        status.setStatus("Publish step :" + task.getComment());
		                        publishDao.beginTransaction();
		                        if(publish instanceof PublishX){
		                        	//No messages for feed publish now.
		                        	//publish.getMessages().add(new PublishMessage(PublishMessage.INFO, task.getComment()));
		                        } else {
		                        	publish.getMessages().add(new PublishMessage(PublishMessage.INFO, task.getComment()));
		                        }
		                        publish.setLastModified(new Date());
		                        publishDao.savePublish(publish);
		                        publishDao.commitTransaction();
		                        task.execute(publish);
		                    }
                		}
                	} else {
                		LOG.warn("NOTE, that there are no publish tasks defined. Publish may become CORRUPT !!!!!");
                	}
                    status.setStatus("Finalizing publish");
                    publishDao.beginTransaction();
                    publish.setStatus(EnumPublishStatus.COMPLETE);
                    publish.setLastModified(new Date());
                    publishDao.savePublish(publish);

                } catch (Throwable e) {
                    LOG.error("Exception occured during publish", e);
                    publishDao.beginTransaction();
                    publish.setStatus(EnumPublishStatus.FAILED);
                    publish.setLastModified(new Date());
                    publish.getMessages().add(new PublishMessage(PublishMessage.ERROR, e.toString()));
                    publishDao.savePublish(publish);
                    status.notifiyError(e);
                    status.setStatus("Error during publish :"+e.getMessage());
                    status.setLastReindexResult("Error during publish :"+e.getMessage());
                    
                } finally {
                    // one has to commit the transation, as transaction are
                    // thread-specific
                    // and the publish status page is polling in a different
                    // thread
                    publishDao.commitTransaction();
                    publishDao.closeSession();
                }
                return publish;
            }

            @Override
            void callFinished(Publish result) {
                status.setElapsedTime(System.currentTimeMillis() - status.getStarted());
                status.setStatus("Publish " + result.getId() + " succeeded");
                status.setLastReindexResult("Publish succeeded in " + (status.getElapsedTime() / 1000) + " ms to '" + publish.getPath() + "'");
            }
        });
    }

    @Override
    public BackgroundStatus getStatus() {
        return procStatus.clone();
    }

    private synchronized ExecutorService initThreads() {
        if (reindexer == null) {
            reindexer = Executors.newSingleThreadExecutor();
        }
        return reindexer;
    }


    @Override
    public Future<ValidationResult> validateDraft(final ValidationResult merge) {
        return initThreads().submit(new CallableWithNotifications<ValidationResult>(procStatus) {
            @Override
            ValidationResult doCall() throws Exception {
                status.setStatus(MessageFormat.format("start validating {0} draft.", merge.getCmsRequest().getDraftContext().getDraftName()));
                mergeTask.execute(merge);
                return merge;
            }

            @Override
            public void callFinished(ValidationResult result) {
                status.setElapsedTime(System.currentTimeMillis() - status.getStarted());
                status.setStatus(MessageFormat.format("validate of {0} draft was {1}.", merge.getCmsRequest().getDraftContext().getDraftName(), result.isSuccess() ? "successed" : "failed"));
                status.setLastReindexResult("validate has been finished in " + (status.getElapsedTime() / 1000) + " ms.");
            }

        });
    }

    @Override
    public Future<MergeResult> mergeDraft(final MergeResult merge) {
        return initThreads().submit(new CallableWithNotifications<MergeResult>(procStatus) {
            @Override
            MergeResult doCall() throws Exception {
                status.setStatus(MessageFormat.format("start merging {0} draft.", merge.getCmsRequest().getDraftContext().getDraftName()));
                mergeTask.execute(merge);
                if (merge.isSuccess()){
                   DraftService.defaultService().updateDraftStatusForDraft(merge.getCmsRequest().getDraftContext().getDraftId(),"MERGED");
                }
                else
                {
                   DraftService.defaultService().updateDraftStatusForDraft(merge.getCmsRequest().getDraftContext().getDraftId(),"FAILED");
                }
                return merge;
            }

            @Override
            public void callFinished(MergeResult result) {
                status.setElapsedTime(System.currentTimeMillis() - status.getStarted());
                status.setStatus(MessageFormat.format("merge of {0} draft was {1}.", merge.getCmsRequest().getDraftContext().getDraftName(), result.isSuccess() ? "successed" : "failed"));
                status.setLastReindexResult("merge has been finished in " + (status.getElapsedTime() / 1000) + " ms.");
            }
            
        });
    }

}