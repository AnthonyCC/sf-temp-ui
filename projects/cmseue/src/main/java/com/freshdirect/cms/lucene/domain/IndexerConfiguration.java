package com.freshdirect.cms.lucene.domain;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.freshdirect.cms.core.service.ContextualContentProvider;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class IndexerConfiguration {

    @Value("${cms.index.batchsize:10000}")
    private String cmsIndexBatchSize;

    @Value("${fdstore.search.primaryHomeKeywordsEnabled:false}")
    private boolean primaryHomeKeywordsEnabled;

    @Value("${fdstore.search.recurseParentAttributesEnabled:false}")
    private boolean recurseParentAttributesEnabled;

    @Value("${cms.index.path}")
    private String indexPath;

    private boolean synonymsDisabled = false;

    private boolean keywordsDisabled = false;

    @Value("${cms.index.path}")
    private String indexDirectoryPath;

    private Analyzer analyzer = IndexingConstants.ANALYZER;

    private MaxFieldLength maxFieldLength = IndexingConstants.MAX_FIELD_LENGTH_1024;

    private boolean partialIndex = false;

    private int indexWriterRamBufferSize = IndexingConstants.DICTIONARY_WRITER_RAM_BUFFER_SIZE;

    private int indexWriterMergeFactor = IndexingConstants.DICTIONARY_WRITER_MERGE_FACTOR;

    @Autowired
    private ContextualContentProvider contentProviderService;

    public boolean isSynonymsDisabled() {
        return synonymsDisabled;
    }

    public void setSynonymsDisabled(boolean synonymsDisabled) {
        this.synonymsDisabled = synonymsDisabled;
    }

    public boolean isKeywordsDisabled() {
        return keywordsDisabled;
    }

    public void setKeywordsDisabled(boolean keywordsDisabled) {
        this.keywordsDisabled = keywordsDisabled;
    }

    public String getIndexDirectoryPath() {
        return indexDirectoryPath;
    }

    public void setIndexDirectoryPath(String indexDirectoryPath) {
        this.indexDirectoryPath = indexDirectoryPath;
    }

    public boolean isPrimaryHomeKeywordsEnabled() {
        return primaryHomeKeywordsEnabled;
    }

    public boolean isRecurseParentAttributesEnabled() {
        return recurseParentAttributesEnabled;
    }

    public int getDictionaryWriteoutBatchSize() {
        return Integer.parseInt(cmsIndexBatchSize);
    }

    public Analyzer getAnalyzer() {
        return analyzer;
    }

    public void setAnalyzer(Analyzer analyzer) {
        this.analyzer = analyzer;
    }

    public MaxFieldLength getMaxFieldLength() {
        return maxFieldLength;
    }

    public void setMaxFieldLength(MaxFieldLength maxFieldLength) {
        this.maxFieldLength = maxFieldLength;
    }

    // if not partial index >> full index
    public boolean isPartialIndex() {
        return partialIndex;
    }

    public void setPartialIndex(boolean partialIndex) {
        this.partialIndex = partialIndex;
    }

    public int getIndexWriterRamBufferSize() {
        return indexWriterRamBufferSize;
    }

    public void setIndexWriterRamBufferSize(int indexWriterRamBufferSize) {
        this.indexWriterRamBufferSize = indexWriterRamBufferSize;
    }

    public int getIndexWriterMergeFactor() {
        return indexWriterMergeFactor;
    }

    public void setIndexWriterMergeFactor(int indexWriterMergeFactor) {
        this.indexWriterMergeFactor = indexWriterMergeFactor;
    }

    public String getIndexPath() {
        return indexPath;
    }

    public void setIndexPath(String indexPath) {
        this.indexPath = indexPath;
    }

    public ContextualContentProvider getContentProviderService() {
        return contentProviderService;
    }

    public void setContentProviderService(ContextualContentProvider contentProviderService) {
        this.contentProviderService = contentProviderService;
    }
}
