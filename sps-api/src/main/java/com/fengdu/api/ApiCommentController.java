package com.fengdu.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fengdu.annotation.IgnoreAuth;
import com.fengdu.annotation.LoginUser;
import com.fengdu.entity.*;
import com.fengdu.service.*;
import com.fengdu.util.ApiBaseAction;
import com.fengdu.util.ApiPageUtils;
import com.fengdu.utils.Base64;
import com.fengdu.utils.CharUtil;
import com.fengdu.utils.Query;
import com.fengdu.utils.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @author wangzizhan  
 * @date 2018年5月31日  
 * @version V1.0
 */
@RestController
@RequestMapping("/api/comment")
public class ApiCommentController extends ApiBaseAction {
    @Autowired
    private ApiCommentService commentService;
    @Autowired
    private ApiUserService userService;
    @Autowired
    private ApiCommentPictureService commentPictureService;
    @Autowired
    private ApiCouponService apiCouponService;
    @Autowired
    private ApiUserCouponService apiUserCouponService;

    /**
     * 发表评论
     */
    @RequestMapping("post")
    public Object post(@LoginUser UserVo loginUser) {
        Map resultObj = new HashMap();
        //
        JSONObject jsonParam = getJsonRequest();
        Integer typeId = jsonParam.getInteger("typeId");
        Integer valueId = jsonParam.getInteger("valueId");
        String content = jsonParam.getString("content");
        JSONArray imagesList = jsonParam.getJSONArray("imagesList");
        CommentVo commentEntity = new CommentVo();
        commentEntity.setType_id(typeId);
        commentEntity.setValue_id(valueId);
        commentEntity.setContent(content);
        commentEntity.setStatus(0);
        //
        commentEntity.setAdd_time(System.currentTimeMillis() / 1000);
        commentEntity.setUser_id(loginUser.getUserId());
        commentEntity.setContent(Base64.encode(commentEntity.getContent()));
        Integer insertId = commentService.save(commentEntity);
        //
        if (insertId > 0 && null != imagesList && imagesList.size() > 0) {
            int i = 0;
            for (Object imgLink : imagesList) {
                i++;
                CommentPictureVo pictureVo = new CommentPictureVo();
                pictureVo.setComment_id(insertId);
                pictureVo.setPic_url(imgLink.toString());
                pictureVo.setSort_order(i);
                commentPictureService.save(pictureVo);
            }
        }
        // 是否领取优惠券
        if (insertId > 0 && typeId == 0) {
            // 当前评价的次数
            Map param = new HashMap();
            param.put("value_id", valueId);
            List<CommentVo> commentVos = commentService.queryList(param);
            boolean hasComment = false;
            for (CommentVo commentVo : commentVos) {
                if (commentVo.getUser_id().equals(loginUser.getUserId())
                        && !commentVo.getId().equals(insertId)) {
                    hasComment = true;
                }
            }
            if (!hasComment) {
                Map couponParam = new HashMap();
                couponParam.put("send_type", 6);
                CouponVo newCouponConfig = apiCouponService.queryMaxUserEnableCoupon(couponParam);
                if (null != newCouponConfig
                        && newCouponConfig.getMin_transmit_num() >= commentVos.size()) {
                    UserCouponVo userCouponVo = new UserCouponVo();
                    userCouponVo.setAdd_time(new Date());
                    userCouponVo.setCoupon_id(newCouponConfig.getId());
                    userCouponVo.setCoupon_number(CharUtil.getRandomString(12));
                    userCouponVo.setUser_id(loginUser.getUserId());
                    apiUserCouponService.save(userCouponVo);
                    resultObj.put("coupon", newCouponConfig);
                }
            }
        }
        if (insertId > 0) {
            return toResponsObject(0, "评论添加成功", resultObj);
        } else {
            return toResponsFail("评论保存失败");
        }
    }

    /**
     */
    @RequestMapping("count")
    public Object count(@LoginUser UserVo loginUser, Integer typeId, Integer valueId) {
        Map<String, Object> resultObj = new HashMap();
        //
        Map param = new HashMap();
        param.put("type_id", typeId);
        param.put("value_id", valueId);
        Integer allCount = commentService.queryTotal(param);
        Integer hasPicCount = commentService.queryhasPicTotal(param);
        //
        resultObj.put("allCount", allCount);
        resultObj.put("hasPicCount", hasPicCount);
        return toResponsSuccess(resultObj);
    }

    /**
     * @param typeId
     * @param valueId
     * @param showType 选择评论的类型 0 全部， 1 只显示图片
     * @param page
     * @param size
     * @return
     */
    @IgnoreAuth
    @RequestMapping("list")
    public Object list(Integer typeId, Integer valueId, Integer showType,
                       @RequestParam(value = "page", defaultValue = "1") Integer page, @RequestParam(value = "size", defaultValue = "10") Integer size,
                       String sort, String order) {
        Map<String, Object> resultObj = new HashMap();
        List<CommentVo> commentList = new ArrayList();
        Map param = new HashMap();
        param.put("type_id", typeId);
        param.put("value_id", valueId);
        param.put("page", page);
        param.put("limit", size);
        if (StringUtils.isNullOrEmpty(sort)) {
            param.put("order", "desc");
        } else {
            param.put("order", sort);
        }
        if (StringUtils.isNullOrEmpty(order)) {
            param.put("sidx", "id");
        } else {
            param.put("sidx", order);
        }
        if (null != showType && showType == 1) {
            param.put("hasPic", 1);
        }
        //查询列表数据
        Query query = new Query(param);
        commentList = commentService.queryList(query);
        int total = commentService.queryTotal(query);
        ApiPageUtils pageUtil = new ApiPageUtils(commentList, total, query.getLimit(), query.getPage());
        //
        for (CommentVo commentItem : commentList) {
            commentItem.setContent(Base64.decode(commentItem.getContent()));
            commentItem.setUser_info(userService.queryObject(commentItem.getUser_id()));

            Map paramPicture = new HashMap();
            paramPicture.put("comment_id", commentItem.getId());
            List<CommentPictureVo> commentPictureEntities = commentPictureService.queryList(paramPicture);
            commentItem.setPic_list(commentPictureEntities);
        }
        return toResponsSuccess(pageUtil);
    }
}