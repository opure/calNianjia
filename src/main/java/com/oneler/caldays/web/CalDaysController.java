package com.oneler.caldays.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.concurrent.atomic.AtomicInteger;

@Controller
public class CalDaysController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    static AtomicInteger atomicInteger = new AtomicInteger();
    static AtomicInteger atomicIntegers = new AtomicInteger();

    static final double DEFAULTDAY = 5;

    @GetMapping(value = "/")
    public String index() {
        return "index";
    }

    @PostMapping(value = "/getResult")
    @ResponseBody
    public String caldays(String time) {
        logger.info("使用次数" + atomicInteger.incrementAndGet() +time);
        LocalDate year17 = LocalDate.of(2017, 12, 31);

        LocalDate year18 = LocalDate.of(2018, 12, 31);

        LocalDate start18 = LocalDate.of(2018, 1, 1);

        LocalDate ruzhi = null;
        try {
            ruzhi = LocalDate.parse(time);
        } catch (Exception e) {
            e.printStackTrace();
            return "对不起,你输入的入职时间格式有误,请重新输入!";
        }

        //2017新的构造函数

        LocalDate juli = null;
        try {
            juli = LocalDate.of(2017, ruzhi.getMonth(), ruzhi.getDayOfMonth());
        } catch (Exception e) {
            e.printStackTrace();
            return "对不起,你输入的入职时间格式有误,请重新输入!";
        }

        //2018新的构造函数

        LocalDate new18 = LocalDate.of(2018, ruzhi.getMonth(), ruzhi.getDayOfMonth());

        //2017年

        long day = year17.toEpochDay() - juli.toEpochDay() + 1;

        //距离2017年有多久
        Period period = Period.between(ruzhi, year17);

        int dayYears = period.getYears() - 1;

        if (dayYears < 0) {
            return "不好意思,你暂时没有年假可以休哦!";
        }

        long year1 = Math.round((DEFAULTDAY + dayYears) / 365 * day * 8);

        //第二阶段
        long day18 = new18.toEpochDay() - start18.toEpochDay() - 1;

        long day19 = year18.toEpochDay() - new18.toEpochDay();

        long step1 = Math.round((DEFAULTDAY + dayYears) / 365 * day18 * 8);

        long stem2 = Math.round((DEFAULTDAY + dayYears + 1) / 365 * 8 * day19);


        StringBuilder stringBuilder = new StringBuilder();

        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(2);

        stringBuilder.append("截止到2017年12月31日年假小时数为" + year1 + "小时,约为" + nf.format(year1 / 7.5) + "天|");
        stringBuilder.append("截止到2018年12月31日年假小时数为" + (step1 + stem2) + "小时,约为" + nf.format((step1 + stem2) / 7.5) + "天");

        logger.info("查询成功的次数" + atomicIntegers.incrementAndGet() +"查询时间为" + time);
        return stringBuilder.toString();
    }

}
