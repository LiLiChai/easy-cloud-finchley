package pers.fancy.cloud.search.core.repository;

import org.elasticsearch.index.reindex.BulkByScrollResponse;
import pers.fancy.cloud.search.core.enums.AggsType;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.filter.FiltersAggregator;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.metrics.Stats;

import java.util.List;
import java.util.Map;

/**
 * Elasticsearch基础功能组件
 *
 * @author LiLiChai
 */
public interface ElasticsearchTemplate<T, M> {

    /**
     * 通过Low Level REST Client 查询
     * https://www.elastic.co/guide/en/elasticsearch/client/java-rest/6.6/java-rest-low-usage-requests.html
     *
     * @param request
     * @return
     * @throws Exception
     */
    Response request(Request request) throws Exception;

    /**
     * 新增索引
     *
     * @param t
     * @return boolean
     * @throws Exception
     */
    boolean save(T t) throws Exception;


    /**
     * 新增索引集合
     *
     * @param list
     * @return BulkResponse
     * @throws Exception
     */
    BulkResponse save(List<T> list) throws Exception;


    /**
     * 更新索引集合
     *
     * @param list
     * @return
     * @throws Exception
     */
    BulkResponse bulkUpdate(List<T> list) throws Exception;


    /**
     * 按照有值字段更新索引
     *
     * @param t
     * @return boolean
     * @throws Exception
     */
    boolean update(T t) throws Exception;

    /**
     * 根据queryBuilder所查结果，按照有值字段更新索引
     *
     * @param queryBuilder
     * @param t
     * @param clazz
     * @param limitcount   更新字段不能超出limitcount
     * @param asyn         true异步处理  否则同步处理
     * @return
     * @throws Exception
     */
    BulkResponse batchUpdate(QueryBuilder queryBuilder, T t, Class clazz, int limitcount, boolean asyn) throws Exception;

    /**
     * 覆盖更新索引
     *
     * @param t
     * @return boolean
     * @throws Exception
     */
    boolean updateCover(T t) throws Exception;

    /**
     * 删除索引
     *
     * @param t
     * @return boolean
     * @throws Exception
     */
    boolean delete(T t) throws Exception;

    /**
     * 根据条件删除索引
     * https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high-document-delete-by-query.html#java-rest-high-document-delete-by-query-response
     *
     * @param queryBuilder
     * @param clazz
     * @return
     * @throws Exception
     */
    BulkByScrollResponse deleteByCondition(QueryBuilder queryBuilder, Class<T> clazz) throws Exception;

    /**
     * 删除索引
     *
     * @param id
     * @param clazz
     * @return boolean
     * @throws Exception
     */
    boolean deleteById(M id, Class<T> clazz) throws Exception;


    /**
     * 【最原始】查询
     *
     * @param searchRequest
     * @return
     * @throws Exception
     */
    SearchResponse search(SearchRequest searchRequest) throws Exception;

    /**
     * 非分页查询
     * 目前暂时传入类类型
     *
     * @param queryBuilder
     * @param clazz
     * @return
     * @throws Exception
     */
    List<T> search(QueryBuilder queryBuilder, Class<T> clazz) throws Exception;

    /**
     * 非分页查询(跨索引)
     * 目前暂时传入类类型
     *
     * @param queryBuilder
     * @param clazz
     * @param indexs
     * @return
     * @throws Exception
     */
    List<T> search(QueryBuilder queryBuilder, Class<T> clazz, String... indexs) throws Exception;


    /**
     * 非分页查询，指定最大返回条数
     * 目前暂时传入类类型
     *
     * @param queryBuilder
     * @param limitSize    最大返回条数
     * @param clazz
     * @return
     * @throws Exception
     */
    List<T> searchMore(QueryBuilder queryBuilder, int limitSize, Class<T> clazz) throws Exception;

    /**
     * 非分页查询(跨索引)，指定最大返回条数
     * 目前暂时传入类类型
     *
     * @param queryBuilder
     * @param limitSize    最大返回条数
     * @param clazz
     * @return
     * @throws Exception
     */
    List<T> searchMore(QueryBuilder queryBuilder, int limitSize, Class<T> clazz, String... indexs) throws Exception;


    /**
     * 查询数量
     *
     * @param queryBuilder
     * @param clazz
     * @return
     * @throws Exception
     */
    long count(QueryBuilder queryBuilder, Class<T> clazz) throws Exception;


    /**
     * 查询数量(跨索引)
     *
     * @param queryBuilder
     * @param clazz
     * @param indexs
     * @return
     * @throws Exception
     */
    long count(QueryBuilder queryBuilder, Class<T> clazz, String... indexs) throws Exception;

    /**
     * 支持分页、高亮、排序的查询
     *
     * @param queryBuilder
     * @param pageSortHighLight
     * @param clazz
     * @return
     * @throws Exception
     */
    PageList<T> search(QueryBuilder queryBuilder, PageSortHighLight pageSortHighLight, Class<T> clazz) throws Exception;


    /**
     * 支持分页、高亮、排序的查询（跨索引）
     *
     * @param queryBuilder
     * @param pageSortHighLight
     * @param clazz
     * @param indexs
     * @return
     * @throws Exception
     */
    PageList<T> search(QueryBuilder queryBuilder, PageSortHighLight pageSortHighLight, Class<T> clazz, String... indexs) throws Exception;


    /**
     * scroll方式查询(默认了保留时间为Constant.DEFAULT_SCROLL_TIME)
     *
     * @param queryBuilder
     * @param clazz
     * @return
     * @throws Exception
     */
    List<T> scroll(QueryBuilder queryBuilder, Class<T> clazz) throws Exception;

    /**
     * scroll方式查询
     *
     * @param queryBuilder
     * @param clazz
     * @param time
     * @param indexs
     * @return
     * @throws Exception
     */
    List<T> scroll(QueryBuilder queryBuilder, Class<T> clazz, long time, String... indexs) throws Exception;

    /**
     * Template方式搜索，Template已经保存在script目录下
     * look at https://www.elastic.co/guide/en/elasticsearch/client/java-api/6.6/java-search-template.html
     * 暂时无法使用该方法，原因为官方API SearchTemplateRequestBuilder仍保留对transportClient 的依赖，但Migration Guide 中描述需要把transportClient迁移为RestHighLevelClient
     *
     * @param template_params
     * @param templateName
     * @param clazz
     * @return
     */
    @Deprecated
    List<T> searchTemplate(Map<String, Object> template_params, String templateName, Class<T> clazz) throws Exception;

    /**
     * Template方式搜索，Template内容以参数方式传入
     * look at https://www.elastic.co/guide/en/elasticsearch/client/java-api/6.6/java-search-template.html
     * 暂时无法使用该方法，原因为官方API SearchTemplateRequestBuilder仍保留对transportClient 的依赖，但Migration Guide 中描述需要把transportClient迁移为RestHighLevelClient
     *
     * @param template_params
     * @param templateSource
     * @param clazz
     * @return
     */
    @Deprecated
    List<T> searchTemplateBySource(Map<String, Object> template_params, String templateSource, Class<T> clazz) throws Exception;

    /**
     * 保存Template
     * look at https://www.elastic.co/guide/en/elasticsearch/client/java-api/6.6/java-search-template.html
     * 暂时无法使用该方法，原因为官方API SearchTemplateRequestBuilder仍保留对transportClient 的依赖，但Migration Guide 中描述需要把transportClient迁移为RestHighLevelClient
     *
     * @param templateName
     * @param templateSource
     * @param clazz
     * @return
     */
    @Deprecated
    List<T> saveTemplate(String templateName, String templateSource, Class<T> clazz) throws Exception;

    /**
     * 搜索建议
     *
     * @param fieldName
     * @param fieldValue
     * @param clazz
     * @return
     * @throws Exception
     */
    List<String> completionSuggest(String fieldName, String fieldValue, Class<T> clazz) throws Exception;


    /**
     * 搜索建议
     *
     * @param fieldName
     * @param fieldValue
     * @param clazz
     * @param indexs
     * @return
     * @throws Exception
     */
    List<String> completionSuggest(String fieldName, String fieldValue, Class<T> clazz, String... indexs) throws Exception;

    /**
     * 根据ID查询
     *
     * @param id
     * @param clazz
     * @return
     * @throws Exception
     */
    T getById(M id, Class<T> clazz) throws Exception;

    /**
     * 根据ID列表批量查询
     *
     * @param ids
     * @param clazz
     * @return
     * @throws Exception
     */
    List<T> mgetById(M[] ids, Class<T> clazz) throws Exception;

    /**
     * id数据是否存在
     *
     * @param id
     * @param clazz
     * @return
     */
    boolean exists(M id, Class<T> clazz) throws Exception;

    /**
     * 普通聚合查询
     * 以bucket分组以aggstypes的方式metric度量
     *
     * @param bucketName
     * @param metricName
     * @param aggsType
     * @param clazz
     * @return
     */
    Map aggs(String metricName, AggsType aggsType, QueryBuilder queryBuilder, Class<T> clazz, String bucketName) throws Exception;


    /**
     * 普通聚合查询
     *
     * @param metricName
     * @param aggsType
     * @param queryBuilder
     * @param clazz
     * @param bucketName
     * @param indexs
     * @return
     * @throws Exception
     */
    Map aggs(String metricName, AggsType aggsType, QueryBuilder queryBuilder, Class<T> clazz, String bucketName, String... indexs) throws Exception;

    /**
     * 以aggstypes的方式metric度量
     *
     * @param metricName
     * @param aggsType
     * @param queryBuilder
     * @param clazz
     * @return
     * @throws Exception
     */
    double aggs(String metricName, AggsType aggsType, QueryBuilder queryBuilder, Class<T> clazz) throws Exception;

    /**
     * 以aggstypes的方式metric度量
     *
     * @param metricName
     * @param aggsType
     * @param queryBuilder
     * @param clazz
     * @param indexs
     * @return
     * @throws Exception
     */
    double aggs(String metricName, AggsType aggsType, QueryBuilder queryBuilder, Class<T> clazz, String... indexs) throws Exception;


    /**
     * 下钻聚合查询(无排序默认策略)
     * 以bucket分组以aggstypes的方式metric度量
     *
     * @param metricName
     * @param aggsType
     * @param queryBuilder
     * @param clazz
     * @param bucketNames
     * @return
     * @throws Exception
     */
    List<Down> aggswith2level(String metricName, AggsType aggsType, QueryBuilder queryBuilder, Class<T> clazz, String[] bucketNames) throws Exception;


    /**
     * 下钻聚合查询(无排序默认策略)
     *
     * @param metricName
     * @param aggsType
     * @param queryBuilder
     * @param clazz
     * @param bucketNames
     * @param indexs
     * @return
     * @throws Exception
     */
    List<Down> aggswith2level(String metricName, AggsType aggsType, QueryBuilder queryBuilder, Class<T> clazz, String[] bucketNames, String... indexs) throws Exception;


    /**
     * 统计聚合metric度量
     *
     * @param metricName
     * @param queryBuilder
     * @param clazz
     * @return
     * @throws Exception
     */
    Stats statsAggs(String metricName, QueryBuilder queryBuilder, Class<T> clazz) throws Exception;

    /**
     * 统计聚合metric度量
     *
     * @param metricName
     * @param queryBuilder
     * @param clazz
     * @param indexs
     * @return
     * @throws Exception
     */
    Stats statsAggs(String metricName, QueryBuilder queryBuilder, Class<T> clazz, String... indexs) throws Exception;

    /**
     * 以bucket分组，统计聚合metric度量
     *
     * @param bucketName
     * @param metricName
     * @param queryBuilder
     * @param clazz
     * @return
     * @throws Exception
     */
    Map<String, Stats> statsAggs(String metricName, QueryBuilder queryBuilder, Class<T> clazz, String bucketName) throws Exception;

    /**
     * 以bucket分组，统计聚合metric度量
     *
     * @param metricName
     * @param queryBuilder
     * @param clazz
     * @param bucketName
     * @param indexs
     * @return
     * @throws Exception
     */
    Map<String, Stats> statsAggs(String metricName, QueryBuilder queryBuilder, Class<T> clazz, String bucketName, String... indexs) throws Exception;


    /**
     * 通用（定制）聚合基础方法
     *
     * @param aggregationBuilder
     * @param queryBuilder
     * @param clazz
     * @return
     * @throws Exception
     */
    Aggregations aggs(AggregationBuilder aggregationBuilder, QueryBuilder queryBuilder, Class<T> clazz) throws Exception;

    /**
     * 通用（定制）聚合基础方法
     *
     * @param aggregationBuilder
     * @param queryBuilder
     * @param clazz
     * @param indexs
     * @return
     * @throws Exception
     */
    Aggregations aggs(AggregationBuilder aggregationBuilder, QueryBuilder queryBuilder, Class<T> clazz, String... indexs) throws Exception;


    /**
     * 基数查询
     *
     * @param metricName
     * @param queryBuilder
     * @param clazz
     * @return
     * @throws Exception
     */
    long cardinality(String metricName, QueryBuilder queryBuilder, Class<T> clazz) throws Exception;

    /**
     * 基数查询
     *
     * @param metricName
     * @param queryBuilder
     * @param clazz
     * @param indexs
     * @return
     * @throws Exception
     */
    long cardinality(String metricName, QueryBuilder queryBuilder, Class<T> clazz, String... indexs) throws Exception;

    /**
     * 百分比聚合 默认聚合见Constant.DEFAULT_PERCSEGMENT
     *
     * @param metricName
     * @param queryBuilder
     * @param clazz
     * @return
     * @throws Exception
     */
    Map percentilesAggs(String metricName, QueryBuilder queryBuilder, Class<T> clazz) throws Exception;

    /**
     * 以百分比聚合
     *
     * @param metricName
     * @param queryBuilder
     * @param clazz
     * @param customSegment
     * @param indexs
     * @return
     * @throws Exception
     */
    Map percentilesAggs(String metricName, QueryBuilder queryBuilder, Class<T> clazz, double[] customSegment, String... indexs) throws Exception;


    /**
     * 以百分等级聚合 (统计在多少数值之内占比多少)
     *
     * @param metricName
     * @param queryBuilder
     * @param clazz
     * @param customSegment
     * @return
     * @throws Exception
     */
    Map percentileRanksAggs(String metricName, QueryBuilder queryBuilder, Class<T> clazz, double[] customSegment) throws Exception;

    /**
     * 以百分等级聚合 (统计在多少数值之内占比多少)
     *
     * @param metricName
     * @param queryBuilder
     * @param clazz
     * @param customSegment
     * @param indexs
     * @return
     * @throws Exception
     */
    Map percentileRanksAggs(String metricName, QueryBuilder queryBuilder, Class<T> clazz, double[] customSegment, String... indexs) throws Exception;


    /**
     * 过滤器聚合
     * new FiltersAggregator.KeyedFilter("men", QueryBuilders.termQuery("gender", "male"))
     *
     * @param metricName
     * @param aggsType
     * @param clazz
     * @param queryBuilder
     * @param filters
     * @return
     * @throws Exception
     */
    Map filterAggs(String metricName, AggsType aggsType, QueryBuilder queryBuilder, Class<T> clazz, FiltersAggregator.KeyedFilter[] filters) throws Exception;

    /**
     * 过滤器聚合
     * new FiltersAggregator.KeyedFilter("men", QueryBuilders.termQuery("gender", "male"))
     *
     * @param metricName
     * @param aggsType
     * @param clazz
     * @param queryBuilder
     * @param filters
     * @return
     * @throws Exception
     */
    Map filterAggs(String metricName, AggsType aggsType, QueryBuilder queryBuilder, Class<T> clazz, FiltersAggregator.KeyedFilter[] filters, String... indexs) throws Exception;


    /**
     * 直方图聚合
     *
     * @param metricName
     * @param aggsType
     * @param queryBuilder
     * @param clazz
     * @param bucketName
     * @param interval
     * @return
     * @throws Exception
     */
    Map histogramAggs(String metricName, AggsType aggsType, QueryBuilder queryBuilder, Class<T> clazz, String bucketName, double interval) throws Exception;


    /**
     * 直方图聚合
     *
     * @param metricName
     * @param aggsType
     * @param queryBuilder
     * @param clazz
     * @param bucketName
     * @param interval
     * @param indexs
     * @return
     * @throws Exception
     */
    Map histogramAggs(String metricName, AggsType aggsType, QueryBuilder queryBuilder, Class<T> clazz, String bucketName, double interval, String... indexs) throws Exception;


    /**
     * 日期直方图聚合
     *
     * @param metricName
     * @param aggsType
     * @param queryBuilder
     * @param clazz
     * @param bucketName
     * @param interval
     * @return
     * @throws Exception
     */
    Map dateHistogramAggs(String metricName, AggsType aggsType, QueryBuilder queryBuilder, Class<T> clazz, String bucketName, DateHistogramInterval interval) throws Exception;

    /**
     * 日期直方图聚合
     *
     * @param metricName
     * @param aggsType
     * @param queryBuilder
     * @param clazz
     * @param bucketName
     * @param interval
     * @param indexs
     * @return
     * @throws Exception
     */
    Map dateHistogramAggs(String metricName, AggsType aggsType, QueryBuilder queryBuilder, Class<T> clazz, String bucketName, DateHistogramInterval interval, String... indexs) throws Exception;
}
