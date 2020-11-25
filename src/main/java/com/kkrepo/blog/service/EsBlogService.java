package com.kkrepo.blog.service;

import com.github.pagehelper.PageInfo;
import com.kkrepo.blog.common.Constant;
import com.kkrepo.blog.common.util.JsonMapper;
import com.kkrepo.blog.document.BlogDocument;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author WangRuofei
 * @date 2020-11-22 4:11 下午
 * @copyright (c) 2020, kkrepo.com All Rights Reserved
 */
@Slf4j
@Service
public class EsBlogService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    public PageInfo<BlogDocument> searchByPage(String keyword, int pageNum) {
        PageInfo<BlogDocument> pageInfo = new PageInfo<>();
        SearchRequest searchRequest = new SearchRequest(Constant.ELASTIC_SEARCH_INDEX);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.from((pageNum - 1) * Constant.DEFAULT_SEARCH_PAGE_LIMIT);
        searchSourceBuilder.size(Constant.DEFAULT_SEARCH_PAGE_LIMIT);
        // 建立 bool 查询
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.should(QueryBuilders.matchQuery("title", keyword));
        boolQueryBuilder.should(QueryBuilders.matchQuery("description", keyword));
        boolQueryBuilder.should(QueryBuilders.matchQuery("author", keyword));
        boolQueryBuilder.should(QueryBuilders.matchQuery("tags", keyword));
        boolQueryBuilder.should(QueryBuilders.matchQuery("category", keyword));
        boolQueryBuilder.should(QueryBuilders.matchQuery("content", keyword));
        searchSourceBuilder.query(boolQueryBuilder);
        // 高亮显示
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.requireFieldMatch(false);
        highlightBuilder.field("title");
        highlightBuilder.preTags("<font color='red'>");
        highlightBuilder.postTags("</font>");
        searchSourceBuilder.highlighter(highlightBuilder);
        // 执行搜索
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = null;
        try {
            searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("EsBlogService searchByPage error! keyword:{} | pageNum:{} | e:{}", keyword, pageNum, e);
            return pageInfo;
        }
        if (!RestStatus.OK.equals(searchResponse.status())) {
            log.error("EsBlogService searchByPage error! keyword:{} | pageNum:{} | searchResponse:{}", keyword, pageNum, JsonMapper.toJsonNotNull(searchResponse));
            return pageInfo;
        }
        SearchHit[] searchHits = searchResponse.getHits().getHits();
        pageInfo.setPageNum(pageNum);
        pageInfo.setPageSize(searchHits.length);
        long total = searchResponse.getHits().getTotalHits().value;
        pageInfo.setTotal(total);
        pageInfo.setPages((int) Math.ceil((double) total / Constant.DEFAULT_SEARCH_PAGE_LIMIT));
        pageInfo.setList(Arrays.stream(searchHits).map(x -> coverToDocument(x)).collect(Collectors.toList()));
        return pageInfo;
    }

    /**
     * 将 SearchHit 转为 BlogDocument
     * 这儿展示搜索结果,暂时不返回content内容
     * @param x
     * @return
     */
    private BlogDocument coverToDocument(SearchHit x) {
        Map<String, Object> sourceMap = x.getSourceAsMap();
        return BlogDocument.builder()
            .id(x.getId())
            .title((String) sourceMap.get("title"))
            .description((String) sourceMap.get("description"))
            .author((String) sourceMap.get("author"))
            .tags((List<String>) sourceMap.get("tags"))
            .category((String) sourceMap.get("category"))
            .pv((Integer) sourceMap.get("pv"))
            .createDate((Long) sourceMap.get("createDate"))
            .comments(Long.valueOf(((Integer) sourceMap.get("comments")).longValue()))
            .build();
    }


    /**
     * 判断索引是否存在
     * @return
     */
    public boolean existsIndex() {
        GetIndexRequest getIndexRequest = new GetIndexRequest(Constant.ELASTIC_SEARCH_INDEX);
        try {
            return restHighLevelClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("EsBlogService createBlogIndex error! e:{}", e);
            return false;
        }
    }

    /**
     * 创建索引
     * @return
     */
    public boolean createBlogIndex() {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(Constant.ELASTIC_SEARCH_INDEX);
        createIndexRequest.settings(Settings.builder()
            .put("index.number_of_shards", 1)
            .put("index.number_of_replicas", 0)
        );
        createIndexRequest.mapping("{\n"
            + "    \"properties\": {\n"
            + "        \"id\": {\n"
            + "            \"type\": \"keyword\"\n"
            + "        },\n"
            + "        \"title\": {\n"
            + "            \"type\": \"text\",\n"
            + "            \"analyzer\": \"ik_max_word\"\n"
            + "        },\n"
            + "        \"description\": {\n"
            + "            \"type\": \"text\",\n"
            + "            \"analyzer\": \"ik_max_word\"\n"
            + "        },\n"
            + "        \"author\": {\n"
            + "            \"type\": \"text\",\n"
            + "            \"analyzer\": \"ik_max_word\"\n"
            + "        },\n"
            + "        \"tags\": {\n"
            + "            \"type\": \"keyword\"\n"
            + "        },\n"
            + "        \"category\": {\n"
            + "            \"type\": \"text\",\n"
            + "            \"analyzer\": \"ik_max_word\"\n"
            + "        },\n"
            + "        \"content\": {\n"
            + "            \"type\": \"text\",\n"
            + "            \"analyzer\": \"ik_max_word\"\n"
            + "        },\n"
            + "        \"pv\": {\n"
            + "            \"type\": \"integer\"\n"
            + "        },\n"
            + "        \"comments\": {\n"
            + "            \"type\": \"long\"\n"
            + "        },\n"
            + "        \"createTime\": {\n"
            + "            \"type\": \"date\"\n"
            + "        }\n"
            + "    }\n"
            + "}", XContentType.JSON);
        CreateIndexResponse createIndexResponse = null;
        try {
            createIndexResponse = restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("EsBlogService createBlogIndex error! e:{}", e);
            return false;
        }
        return createIndexResponse.isAcknowledged();
    }

    /**
     * 创建文档
     * @param blogDocument
     * @return
     */
    public boolean createBlogDocument(BlogDocument blogDocument) {
        IndexRequest indexRequest = new IndexRequest(Constant.ELASTIC_SEARCH_INDEX)
            .id(blogDocument.getId())
            .source(JsonMapper.toJsonNotNull(blogDocument), XContentType.JSON);
        IndexResponse indexResponse = null;
        try {
            indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("EsBlogService createBlogDocument error! blogDocument:{} | e:{}", JsonMapper.toJsonNotNull(blogDocument), e);
            return false;
        }
        return RestStatus.OK.equals(indexResponse.status());
    }

    /**
     * 批量创建文档
     * @param documentList
     * @return
     */
    public boolean bulkCreateBlogDocument(List<BlogDocument> documentList) {
        BulkRequest bulkRequest = new BulkRequest();
        for (BlogDocument blogDocument : documentList) {
            bulkRequest.add(new IndexRequest(Constant.ELASTIC_SEARCH_INDEX)
                .id(blogDocument.getId())
                .source(JsonMapper.toJsonNotNull(blogDocument), XContentType.JSON)
            );
        }
        BulkResponse bulkResponse = null;
        try {
            bulkResponse = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("EsBlogService bulkCreateBlogDocument error! blogDocumentList:{} | e:{}", JsonMapper.toJsonNotNull(documentList), e);
            return false;
        }
        return RestStatus.OK.equals(bulkResponse.status());
    }

    /**
     * 根据id,获取文档
     * @param id
     * @return
     */
    public BlogDocument getBlogDocumentById(String id) {
        GetRequest getRequest = new GetRequest(Constant.ELASTIC_SEARCH_INDEX, id);
        GetResponse getResponse = null;
        try {
            getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("EsBlogService getBlogDocumentById error! id:{} | e:{}", id, e);
            return null;
        }
        if (getResponse.isExists()) {
            return JsonMapper.nonDefaultMapper().fromJson(getResponse.getSourceAsString(), BlogDocument.class);
        }
        return null;
    }

    /**
     * 更新/创建 文档
     * @param blogDocument
     * @return
     */
    public boolean upsertBlogDocument(BlogDocument blogDocument) {
        BlogDocument resultDocument = getBlogDocumentById(blogDocument.getId());
        if (resultDocument == null) {
            return createBlogDocument(blogDocument);
        }
        UpdateRequest updateRequest = new UpdateRequest(Constant.ELASTIC_SEARCH_INDEX, blogDocument.getId());
        updateRequest.doc(JsonMapper.toJsonNotNull(blogDocument), XContentType.JSON);
        UpdateResponse updateResponse = null;
        try {
            updateResponse = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("EsBlogService updateBlogDocument error! blogDocument:{} | e:{}", JsonMapper.toJsonNotNull(blogDocument), e);
            return false;
        }
        return RestStatus.OK.equals(updateResponse.status());
    }

    /**
     * 批量更新文档
     * @param documentList
     * @return
     */
    public boolean batchUpdateBlogDocument(List<BlogDocument> documentList) {
        for (BlogDocument blogDocument : documentList) {
            if (!upsertBlogDocument(blogDocument)) {
                log.error("EsBlogService updateBlogDocument error! blogDocument:{}", JsonMapper.toJsonNotNull(blogDocument));
                return false;
            }
        }
        return true;
    }


    /**
     * 删除文档
     * @param id
     * @return
     */
    public String deleteBlogDocument(String id) {
        DeleteRequest deleteRequest = new DeleteRequest(Constant.ELASTIC_SEARCH_INDEX, id);
        DeleteResponse deleteResponse = null;
        try {
            deleteResponse = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("EsBlogService deleteBlogDocument error! id:{} | e:{}", id, e);
            return "";
        }
        return deleteResponse.getResult().name();
    }
}
