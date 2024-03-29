package com.fengdu.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fengdu.entity.WxReplyNewsEntity;
import com.fengdu.service.WxReplyNewsService;
import com.fengdu.utils.PageUtils;
import com.fengdu.utils.Query;
import com.fengdu.utils.R;

/**
 * Controller
 *
 * @author william_w
 * @email 2366207000@qq.com
 * @date 2018-05-24 15:56:38
 */
@Controller
@RequestMapping("wxreplynews")
public class WxReplyNewsController {
    @Autowired
    private WxReplyNewsService wxReplyNewsService;

    /**
     * 查看列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("wxreplynews:list")
    @ResponseBody
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        List<WxReplyNewsEntity> wxReplyNewsList = wxReplyNewsService.queryList(query);
        int total = wxReplyNewsService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(wxReplyNewsList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 查看信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("wxreplynews:info")
    @ResponseBody
    public R info(@PathVariable("id") String id) {
        WxReplyNewsEntity wxReplyNews = wxReplyNewsService.queryObject(id);

        return R.ok().put("wxReplyNews", wxReplyNews);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("wxreplynews:save")
    @ResponseBody
    public R save(@RequestBody WxReplyNewsEntity wxReplyNews) {
        wxReplyNewsService.save(wxReplyNews);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("wxreplynews:update")
    @ResponseBody
    public R update(@RequestBody WxReplyNewsEntity wxReplyNews) {
        wxReplyNewsService.update(wxReplyNews);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("wxreplynews:delete")
    @ResponseBody
    public R delete(@RequestBody String[]ids) {
        wxReplyNewsService.deleteBatch(ids);

        return R.ok();
    }

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    @ResponseBody
    public R queryAll(@RequestParam Map<String, Object> params) {

        List<WxReplyNewsEntity> list = wxReplyNewsService.queryList(params);

        return R.ok().put("list", list);
    }
}
