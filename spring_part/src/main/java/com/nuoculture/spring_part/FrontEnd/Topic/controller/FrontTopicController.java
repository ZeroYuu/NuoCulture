package com.nuoculture.spring_part.FrontEnd.Topic.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nuoculture.spring_part.BackEnd.Active.Aduit.entity.ActiveJoinInfo;
import com.nuoculture.spring_part.BackEnd.Goods.Commdity.entity.GoodsInfo;
import com.nuoculture.spring_part.BackEnd.Topic.Content.entity.TopicInfo;
import com.nuoculture.spring_part.BackEnd.Topic.Content.service.ContentService;
import com.nuoculture.spring_part.BackEnd.Topic.Review.entity.TopicReviewInfo;
import com.nuoculture.spring_part.BackEnd.Topic.Review.service.ReviewService;
import com.nuoculture.spring_part.BackEnd.User.Account.entity.UserInfo;
import com.nuoculture.spring_part.FrontEnd.Topic.entity.TopicTypeInfo;
import com.nuoculture.spring_part.FrontEnd.Topic.entity.PageParam;
import com.nuoculture.spring_part.FrontEnd.Topic.service.FrontTopicService;
import com.nuoculture.spring_part.FrontEnd.UserInfo.entity.UserStarInfo;
import com.nuoculture.spring_part.utils.ResultVo;
import com.nuoculture.spring_part.utils.SensitiveWordUtil;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

//结合了vue所以不用controller
@RestController
@RequestMapping("/api/fronttopic")
public class FrontTopicController {

    @Autowired
    FrontTopicService frontTopicService;

    @Autowired
    ContentService contentService;


    @Value("${file.staticPath}")
    private String staticPath;


    //收藏
    @GetMapping("/StarTopic/{id}/{uid}/{star}")
    public ResultVo StarTopic(@PathVariable("id") int id, @PathVariable("uid") int uid,
                              @PathVariable("star") boolean star) {
        System.out.println(id + " " + uid);
        int temp = frontTopicService.StarTopic(id, uid, star);
//        System.out.println(getStarCul(uid));
        if (temp == 1) {
            return ResultVo.success("收藏成功");
        } else if (temp == 2) {
            return ResultVo.success("取消收藏");
        }
        return ResultVo.error("收藏失败");
    }

    @GetMapping("/getStarTopic/{id}")
    public ResultVo getStarTopic(@PathVariable("id") int id) {
        List<UserStarInfo> account = frontTopicService.getStarTopic(id);
        System.out.println(account);
        if (account.isEmpty()) {
            return ResultVo.success("", account);
        }

        Set<Integer> TopicIds = account.stream()
                .map(UserStarInfo::getTopic_id)
                .collect(Collectors.toSet());

        List<TopicInfo> infos = frontTopicService.listByIds(TopicIds);

        IntStream.range(0, infos.size()) // 假设infos和account列表长度相同
                .forEach(i -> {
                    TopicInfo info = infos.get(i); // 获取infos列表中的元素
                    UserStarInfo userStarInfo = account.get(i); // 获取account列表中的对应元素
                    info.setStar_id(userStarInfo.getStar_heart_id()); // 设置star_id
                });

        for (Integer topicId : TopicIds) {
            String url = frontTopicService.getImgurl(topicId);
            for (TopicInfo topicInfo : infos) {
                if (topicInfo.getTopic_id() == (topicId)) {  // 假设GoodsInfo有getGoodsId方法
                    topicInfo.setImg_path_url(url);
                    break;  // 找到匹配的goodsInfo后跳出内层循环
                }
            }
        }


        if (account != null) {
            return ResultVo.success("", infos);
        }
        return ResultVo.error("查询失败!");
    }

    @GetMapping("/getStar/{id}/{uid}")
    public ResultVo getStar(@PathVariable("id") int id, @PathVariable("uid") int uid) {
        Boolean account = frontTopicService.getStar(id, uid);
        System.out.println(account);

        return ResultVo.success("", account);

    }

    //增加话题

    @PostMapping("/addTopic")
    public ResultVo addTopic(@RequestBody TopicInfo topicInfo) {
        System.out.println(topicInfo);
        topicInfo.setTopic_view(0);
        topicInfo.setTopic_status(0);
        topicInfo.setTopic_time(new Date());
        boolean temp = contentService.save(topicInfo);

        int flag = 0;
        int id = contentService.getId();
//        System.out.println(id);
        for (int i = 0; i < topicInfo.getImg_path().length; i++) {
            flag = contentService.setImg(id, topicInfo.getImg_path()[i]);
        }


        if (temp && flag == 1) {
            return ResultVo.success("等待审核");
        }
        return ResultVo.error("发布失败");
    }

    //添加浏览量
    @PutMapping("/addView/{id}")
    public ResultVo addView(@PathVariable("id") int id) {

        UpdateWrapper<TopicInfo> updateWrapper = Wrappers.update();
        updateWrapper.eq("topic_id", id);
        updateWrapper.setSql("topic_view = topic_view + 1");
        boolean temp = frontTopicService.update(updateWrapper);


        if (temp) {
            return ResultVo.success("浏览量增加成功");
        }
        return ResultVo.error("浏览量增加失败");
    }


    @Autowired
    ReviewService reviewService;
    @Autowired
    private SensitiveWordUtil sensitiveWordUtil;

    //增加评论
    @PostMapping("/addReview")
    public ResultVo addReview(@RequestBody TopicReviewInfo topicReviewInfo) {
        System.out.println(topicReviewInfo);

        String oldContent = topicReviewInfo.getTopic_review_content();
        String result = sensitiveWordUtil.replace(topicReviewInfo.getTopic_review_content());


        if (!result.equals(oldContent) ) {
            System.out.println(oldContent+" "+result);
            return ResultVo.error("包含敏感词汇，请重新输入");
        } else {
            System.out.println(oldContent+" "+result);
            topicReviewInfo.setTopic_review_time(new Date());
            boolean temp = reviewService.save(topicReviewInfo);
            if (temp) {
                return ResultVo.success("评论成功");
            }
            return ResultVo.error("评论失败");
        }



    }

    //删除
    @DeleteMapping("/delReview/{id}")
    public ResultVo delReview(@PathVariable("id") int id) {
        boolean temp = reviewService.removeById(id);
        if (temp) {
            return ResultVo.success("删除成功");
        }
        return ResultVo.error("删除失败");
    }

    //删除
    @DeleteMapping("/delTopic/{id}")
    public ResultVo delTopic(@PathVariable("id") int id) {
        boolean temp = frontTopicService.removeById(id);
        if (temp) {
            return ResultVo.success("删除成功");
        }
        return ResultVo.error("删除失败");
    }

    //批量删除
    @DeleteMapping("/allDelTopic/{id}")
    public ResultVo allDelTopic(@PathVariable("id") int[] Id) {
        boolean temp = false;
        for (int i = 0; i < Id.length; i++) {
            System.out.println(Id[i]);
            temp = frontTopicService.removeById(Id[i]);
        }

        if (temp) {
            return ResultVo.success("批量删除成功");
        }
        return ResultVo.error("批量删除失败");
    }

    //修改
    @PutMapping
    public ResultVo edit(@RequestBody TopicInfo topicInfo) {
        System.out.println("useEdit");
        System.out.println(topicInfo);
        topicInfo.setTopic_status(0);
        topicInfo.setTopic_time(new Date());
        boolean temp = contentService.updateById(topicInfo);
        //修改图片
        int flag = 0;
        if (topicInfo.getImg_path() != null) {
            int id = topicInfo.getTopic_id();
//        System.out.println(id);
            for (int i = 0; i < topicInfo.getImg_path().length; i++) {
                flag = contentService.setImg(id, topicInfo.getImg_path()[i]);
            }
        } else {
            flag = 1;
        }

        if (temp && flag == 1) {
            return ResultVo.success("等待审核");
        }
        return ResultVo.error("发布失败");
    }


    //获取列表
    @GetMapping("/getList")
    public ResultVo getList(PageParam param) {
        System.out.println("param" + param);
        //构建分页对象

        IPage<TopicInfo> page = new Page<>();
        page.setCurrent(param.getCurPage());
        page.setSize(param.getPageSize());

        QueryWrapper<TopicInfo> wrapper = new QueryWrapper<>();

        if (StringUtils.isNotEmpty(param.getTopic_type_id())) {
            frontTopicService.getTypeTopic(page, param.getTopic_type_id(), param.getTopic_content());
        } else {
            frontTopicService.getTopic(page, param.getTopic_content());
        }

        System.out.println(page);
        if (page != null) {
            return ResultVo.success("查询成功", page);
        }
        return ResultVo.error("查询失败!");
    }


    @GetMapping("/getImages/{id}")
    public ResultVo getImages(@PathVariable("id") int id) {
//        System.out.println(id);
        List<String> list = frontTopicService.getImages(id);
//        System.out.println(list);
        if (list != null) {
            return ResultVo.success("", list);
        }
        return ResultVo.error("查询失败!");
    }

    //获取账号名称
    @GetMapping("/getDetail/{id}")
    public ResultVo getDetail(@PathVariable("id") int id) {
        List<TopicInfo> detail = frontTopicService.getDetailApi(id);
        System.out.println(detail);
        if (detail != null) {
            return ResultVo.success("", detail);
        }
        return ResultVo.error("查询失败!");
    }

    @GetMapping("/getReview/{id}")
    public ResultVo getReview(@PathVariable("id") int id) {
        List<TopicReviewInfo> review = frontTopicService.getReview(id);
        System.out.println(review);
        if (review != null) {
            return ResultVo.success("", review);
        }
        return ResultVo.error("查询失败!");
    }

    @GetMapping("/getAccount/{id}")
    public ResultVo getAccount(@PathVariable("id") int id) {
        List<UserInfo> account = frontTopicService.getAccount(id);
        System.out.println(account);
        if (account != null) {
            return ResultVo.success("", account);
        }
        return ResultVo.error("查询失败!");
    }


    //获取活动信息
    @GetMapping("/getTopicType")
    public ResultVo getTopicType() {
        List<TopicTypeInfo> type = frontTopicService.getType();
        System.out.println(type);
        if (type != null) {
            return ResultVo.success("", type);
        }
        return ResultVo.error("查询失败!");
    }


}
