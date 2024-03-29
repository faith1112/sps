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

import com.fengdu.entity.WxMenuEntity;
import com.fengdu.service.WxMenuService;
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
@RequestMapping("wxmenu")
public class WxMenuController {
    @Autowired
    private WxMenuService wxMenuService;

    /**
     * 查看列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("wxmenu:list")
    @ResponseBody
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        List<WxMenuEntity> wxMenuList = wxMenuService.queryList(query);
        int total = wxMenuService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(wxMenuList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 查看信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("wxmenu:info")
    @ResponseBody
    public R info(@PathVariable("id") String id) {
        WxMenuEntity wxMenu = wxMenuService.queryObject(id);

        return R.ok().put("wxMenu", wxMenu);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("wxmenu:save")
    @ResponseBody
    public R save(@RequestBody WxMenuEntity wxMenu) {
        wxMenuService.save(wxMenu);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("wxmenu:update")
    @ResponseBody
    public R update(@RequestBody WxMenuEntity wxMenu) {
        wxMenuService.update(wxMenu);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("wxmenu:delete")
    @ResponseBody
    public R delete(@RequestBody String[]ids) {
        wxMenuService.deleteBatch(ids);

        return R.ok();
    }

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    @ResponseBody
    public R queryAll(@RequestParam Map<String, Object> params) {

        List<WxMenuEntity> list = wxMenuService.queryList(params);

        return R.ok().put("list", list);
    }
}
