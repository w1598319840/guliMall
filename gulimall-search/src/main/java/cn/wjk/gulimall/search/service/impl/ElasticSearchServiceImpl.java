package cn.wjk.gulimall.search.service.impl;

import cn.wjk.gulimall.common.domain.to.es.SkuEsModel;
import cn.wjk.gulimall.search.constrant.ESConstant;
import cn.wjk.gulimall.search.service.ElasticSearchService;
import com.alibaba.fastjson2.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * @Package: cn.wjk.gulimall.search.service.impl
 * @ClassName: ElasticSearchServiceImpl
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/12 下午7:46
 * @Description:
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ElasticSearchServiceImpl implements ElasticSearchService {
    private final RestHighLevelClient client;

    @Override
    public boolean up(List<SkuEsModel> skuEsModels) throws IOException {
        //1. 判断product索引是否已创建
        GetIndexRequest getIndexRequest = new GetIndexRequest(ESConstant.PRODUCT_INDEX);
        boolean isExist = client.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
        if (!isExist) {
            //2. 如果product索引库不存在，那么需要创建索引库
            CreateIndexRequest createIndexRequest = new CreateIndexRequest(ESConstant.PRODUCT_INDEX);
            BufferedInputStream bufferedInputStream = null;
            try {
                InputStream inputStream = this.getClass().getClassLoader().
                        getResourceAsStream("indexJson/productIndex.json");
                if (inputStream != null) {
                    bufferedInputStream = new BufferedInputStream(inputStream);
                    byte[] buffer = new byte[10 * 1024 * 1024];
                    int length;
                    StringBuilder sb = new StringBuilder();
                    if ((length = bufferedInputStream.read(buffer)) != -1) {
                        sb.append(new String(buffer, 0, length));
                    }
                    String productIndexJsonString = sb.toString();
                    createIndexRequest.source(productIndexJsonString, XContentType.JSON);
                    client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
                }
            } catch (IOException e) {
                log.error(e.getMessage());
            } finally {
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
            }
        }

        //3. 索引库已经存在，直接插入数据
        BulkRequest bulkRequest = new BulkRequest();
        for (SkuEsModel skuEsModel : skuEsModels) {
            IndexRequest indexRequest = new IndexRequest(ESConstant.PRODUCT_INDEX);
            indexRequest.id(skuEsModel.getSkuId().toString());
            indexRequest.source(JSON.toJSONString(skuEsModel), XContentType.JSON);
            bulkRequest.add(indexRequest);
        }
        BulkResponse bulk = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        List<String> itemIds = Arrays.stream(bulk.getItems()).map(BulkItemResponse::getId).toList();
        boolean hasFailures = bulk.hasFailures();
        if (hasFailures) {
            log.error("商品上架存在失败情况");
        }
        log.info("上架成功,{}", itemIds);
        return hasFailures;
    }
}
