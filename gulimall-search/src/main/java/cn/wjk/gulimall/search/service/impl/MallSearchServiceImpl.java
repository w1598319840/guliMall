package cn.wjk.gulimall.search.service.impl;

import cn.wjk.gulimall.common.domain.to.es.SkuEsModel;
import cn.wjk.gulimall.common.domain.vo.SearchVO;
import cn.wjk.gulimall.search.constrant.ESConstant;
import cn.wjk.gulimall.search.domain.dto.SearchDTO;
import cn.wjk.gulimall.search.service.MallSearchService;
import com.alibaba.fastjson2.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Package: cn.wjk.gulimall.search.service.impl
 * @ClassName: MallSearchServiceImpl
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/15 下午6:57
 * @Description:
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MallSearchServiceImpl implements MallSearchService {
    private final RestHighLevelClient restHighLevelClient;

    @Override
    public SearchVO search(SearchDTO searchDTO) {
        SearchRequest searchRequest = new SearchRequest(ESConstant.PRODUCT_INDEX);

        buildCondition(searchDTO, searchRequest.source());
        log.info("DLS = {}", searchRequest.source().toString());

        SearchResponse response = null;
        try {
            response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return parseResponse(response, searchDTO.getPageNum());
    }

    /**
     * 解析返回结果
     */
    private SearchVO parseResponse(SearchResponse response, Integer pageNum) {
        if (response == null) {
            return null;
        }
        //====以下数据在hits中
        SearchHits searchHits = response.getHits();
        SearchVO searchVO = new SearchVO();
        searchVO.setProducts(parseProductVOs(searchHits.getHits()));
        searchVO.setPageNum(pageNum);
        long total = searchHits.getTotalHits().value;
        searchVO.setTotal(total);
        searchVO.setTotalPages((int) (total + ESConstant.PRODUCT_PAGE_SIZE - 1) / ESConstant.PRODUCT_PAGE_SIZE);
        //====以下数据在aggregation中====
        Aggregations aggregations = response.getAggregations();
        searchVO.setAttrs(parseAttrVOs(aggregations));
        searchVO.setBrands(parseBrandVOs(aggregations));
        searchVO.setCatalogs(parseCatalogVOs(aggregations));

        return searchVO;
    }

    /**
     * 解析productVO
     */
    private List<SkuEsModel> parseProductVOs(SearchHit[] hits) {
        return Arrays.stream(hits).map(hit -> {
            //替换highlight部分
            SkuEsModel skuEsModel = JSON.parseObject(hit.getSourceAsString(), SkuEsModel.class);
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            if (highlightFields != null && !highlightFields.isEmpty()) {
                HighlightField highlightField = highlightFields.get("skuTitle");
                StringBuilder stringBuilder = new StringBuilder();
                for (Text fragment : highlightField.getFragments()) {
                    stringBuilder.append(fragment);
                }
                skuEsModel.setSkuTitle(stringBuilder.toString());
            }
            return skuEsModel;
        }).toList();
    }

    /**
     * 解析CatalogVO
     */
    private List<SearchVO.CatalogVO> parseCatalogVOs(Aggregations aggregations) {
        ParsedLongTerms catalogAgg = aggregations.get("catalog_agg");
        return catalogAgg.getBuckets().stream().map(bucket -> {
            SearchVO.CatalogVO catalogVO = new SearchVO.CatalogVO();
            catalogVO.setCatalogId(bucket.getKeyAsNumber().longValue());
            ParsedStringTerms catalogNameAgg = bucket.getAggregations().get("catalog_name_agg");
            catalogVO.setCatalogName(catalogNameAgg.getBuckets().getFirst().getKeyAsString());
            return catalogVO;
        }).toList();
    }

    /**
     * 解析brandVO
     */
    private List<SearchVO.BrandVO> parseBrandVOs(Aggregations aggregations) {
        ParsedLongTerms brandAgg = aggregations.get("brand_agg");
        return brandAgg.getBuckets().stream().map(bucket -> {
            SearchVO.BrandVO brandVO = new SearchVO.BrandVO();
            brandVO.setBrandId(bucket.getKeyAsNumber().longValue());
            Aggregations subAgg = bucket.getAggregations();
            ParsedStringTerms brandNameAgg = subAgg.get("brand_name_agg");
            brandVO.setBrandName(brandNameAgg.getBuckets().getFirst().getKeyAsString());
            ParsedStringTerms brandImgAgg = subAgg.get("brand_img_agg");
            brandVO.setBrandImg(brandImgAgg.getBuckets().getFirst().getKeyAsString());
            return brandVO;
        }).toList();
    }

    /**
     * 获取AttrVO
     */
    private List<SearchVO.AttrVO> parseAttrVOs(Aggregations aggregations) {
        ParsedNested attrAgg = aggregations.get("attr_agg");
        ParsedLongTerms attrIdAgg = attrAgg.getAggregations().get("attr_id_agg");
        return attrIdAgg.getBuckets().stream().map(bucket -> {
            SearchVO.AttrVO attrVO = new SearchVO.AttrVO();
            attrVO.setAttrId(bucket.getKeyAsNumber().longValue());
            Aggregations subAgg = bucket.getAggregations();
            ParsedStringTerms attrNameAgg = subAgg.get("attr_name_agg");
            attrVO.setAttrName(attrNameAgg.getBuckets().getFirst().getKeyAsString());
            ParsedStringTerms attrValueAgg = subAgg.get("attr_value_agg");
            attrVO.setAttrValue(attrValueAgg.getBuckets().stream().map(MultiBucketsAggregation.Bucket::getKeyAsString)
                    .toList());
            return attrVO;
        }).toList();
    }

    /**
     * 构建所有条件
     */
    private void buildCondition(SearchDTO searchDTO, SearchSourceBuilder source) {
        buildQueryCondition(searchDTO, source);
        buildSortCondition(searchDTO.getSort(), source);
        buildPageCondition(searchDTO.getPageNum(), source);
        buildHighLightCondition(searchDTO.getKeyword(), source);
        buildAggregationCondition(source);
    }

    /**
     * 构建聚合条件
     */
    private void buildAggregationCondition(SearchSourceBuilder source) {
        //聚合brand
        AggregationBuilder brandAggregationBuilder = AggregationBuilders.terms("brand_agg")
                .field("brandId").size(100);
        brandAggregationBuilder.subAggregation(AggregationBuilders.terms("brand_name_agg")
                .field("brandName").size(1));
        brandAggregationBuilder.subAggregation(AggregationBuilders.terms("brand_img_agg")
                .field("brandImg").size(1));
        source.aggregation(brandAggregationBuilder);

        //聚合catalog
        AggregationBuilder catalogAggregationBuilder = AggregationBuilders.terms("catalog_agg")
                .field("catalogId").size(20).
                subAggregation(AggregationBuilders.terms("catalog_name_agg").field("catalogName").size(1));
        source.aggregation(catalogAggregationBuilder);

        //聚合attrs
        TermsAggregationBuilder attrsIdAggregationBuilder = AggregationBuilders.terms("attr_id_agg")
                .field("attrs.attrId").size(100);
        attrsIdAggregationBuilder.subAggregation(AggregationBuilders.terms("attr_name_agg")
                .field("attrs.attrName").size(1));
        attrsIdAggregationBuilder.subAggregation(AggregationBuilders.terms("attr_value_agg")
                .field("attrs.attrValue").size(50));
        source.aggregation(AggregationBuilders
                .nested("attr_agg", "attrs").subAggregation(attrsIdAggregationBuilder));
    }

    /**
     * 构建高亮条件
     */
    private void buildHighLightCondition(String keyword, SearchSourceBuilder source) {
        if (StringUtils.isEmpty(keyword)) {
            return;
        }
        source.highlighter(new HighlightBuilder()
                .field("skuTitle")
                .preTags("<b style='color:red'>")
                .postTags("</b>"));
    }

    /**
     * 构建分页条件
     */
    private void buildPageCondition(Integer pageNum, SearchSourceBuilder source) {
        source.from((pageNum - 1) * ESConstant.PRODUCT_PAGE_SIZE)
                .size(ESConstant.PRODUCT_PAGE_SIZE);
    }

    /**
     * 构建排序条件
     */
    private void buildSortCondition(String sort, SearchSourceBuilder source) {
        if (StringUtils.isEmpty(sort)) {
            return;
        }
        //sort=saleCount_asc
        String[] nameAndOrderArray = sort.split("_");
        String name = nameAndOrderArray[0];
        SortOrder order = SortOrder.ASC.toString().equalsIgnoreCase(nameAndOrderArray[1]) ?
                SortOrder.ASC : SortOrder.DESC;
        source.sort(name, order);
    }

    /**
     * 构建查询条件
     */
    private void buildQueryCondition(SearchDTO searchDTO, SearchSourceBuilder source) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        String keyword = searchDTO.getKeyword();
        if (StringUtils.isNotEmpty(keyword)) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("skuTitle", keyword));
        }
        Long catalog3Id = searchDTO.getCatalog3Id();
        if (catalog3Id != null) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("catalogId", catalog3Id));
        }
        List<Long> brandIds = searchDTO.getBrandId();
        if (brandIds != null && !brandIds.isEmpty()) {
            boolQueryBuilder.filter(QueryBuilders.termsQuery("brandId", brandIds));
        }
        List<String> attrs = searchDTO.getAttrs();
        if (attrs != null && !attrs.isEmpty()) {
            for (String attr : attrs) {
                //每一个属性我们都需要构建一个nested查询条件
                BoolQueryBuilder nestedBoolQueryBuild = QueryBuilders.boolQuery();
                //attrs=1_其他:安卓&attrs=2_5寸:6寸 (一号属性选了其他和安装两个选项)
                String[] attrIdAndValuesArray = attr.split("_");
                int attrId = Integer.parseInt(attrIdAndValuesArray[0]);
                String[] attrValues = attrIdAndValuesArray[1].split(":");
                nestedBoolQueryBuild.filter(QueryBuilders.termQuery("attrs.attrId", attrId))
                        .filter(QueryBuilders.termsQuery("attrs.attrValue", attrValues));
                //第三个参数: 以什么样的方式参与评分
                boolQueryBuilder.filter(QueryBuilders.nestedQuery("attrs", nestedBoolQueryBuild, ScoreMode.None));
            }
        }
        Integer hasStock = searchDTO.getHasStock();
        if (hasStock != null) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("hasStock", hasStock == 1));
        }
        String skuPriceString = searchDTO.getSkuPrice();
        if (StringUtils.isNotEmpty(skuPriceString)) {
            int index = skuPriceString.indexOf('_');
            String[] minAndMaxSkuPrice = skuPriceString.split("_");
            int maxSkuPrice = -1;
            int minSkuPrice = -1;
            if (index == 0) {
                //_500
                maxSkuPrice = Integer.parseInt(minAndMaxSkuPrice[1]);
            } else if (index == skuPriceString.length() - 1) {
                //600_
                minSkuPrice = Integer.parseInt(minAndMaxSkuPrice[0]);
            } else {
                //500_600
                minSkuPrice = Integer.parseInt(minAndMaxSkuPrice[0]);
                maxSkuPrice = Integer.parseInt(minAndMaxSkuPrice[1]);
            }
            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("skuPrice");
            if (minSkuPrice != -1) {
                rangeQueryBuilder.gte(minSkuPrice);
            }
            if (maxSkuPrice != -1) {
                rangeQueryBuilder.lte(maxSkuPrice);
            }
            boolQueryBuilder.filter(rangeQueryBuilder);
        }

        //构建请求
        source.query(boolQueryBuilder);
    }
}
