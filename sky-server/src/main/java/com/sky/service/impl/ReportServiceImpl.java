package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 根据给定的开始日期 begin 和结束日期 end 进行用户统计
     *
     * @param begin 开始日期
     * @param end 结束日期
     * @return 返回 UserReportVO 的视图对象
     */
    @Override
    public UserReportVO userReport(LocalDate begin, LocalDate end) {
        // 生成 dateList
        List<LocalDate> dateList = generateDateList(begin, end);

        // 获取 totalUserList
        List<Integer> totalUserList = new ArrayList<>();
        for (LocalDate date : dateList) {
            LocalDateTime queryBegin = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime queryEnd = LocalDateTime.of(date, LocalTime.MAX);
            Integer total = userMapper.getTotal(queryBegin, queryEnd);
            totalUserList.add(total);
        }

        // 获取 newUserList
        List<Integer> newUserList = new ArrayList<>();
        for (int i = 0; i < totalUserList.size(); i++) {
            if (i == 0)
                newUserList.add(totalUserList.get(i));
            else
                newUserList.add(Math.max(totalUserList.get(i) - totalUserList.get(i-1), 0));
        }

        return UserReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .totalUserList(StringUtils.join(totalUserList, ","))
                .newUserList(StringUtils.join(newUserList, ","))
                .build();
    }

    /**
     * 根据给定的开始日期 begin 和结束日期 end 进行营业额统计
     *
     * @param begin 开始日期
     * @param end 结束日期
     * @return 返回 TurnoverReportVO 的视图对象
     */
    @Override
    public TurnoverReportVO turnoverReport(LocalDate begin, LocalDate end) {
        // 生成 dateList
        List<LocalDate> dateList = generateDateList(begin, end);

        // 获取营业额 turnoverList
        List<Integer> turnoverList = new ArrayList<>();
        for (LocalDate date : dateList) {
            LocalDateTime queryBegin = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime queryEnd = LocalDateTime.of(date, LocalTime.MAX);
            Integer turnover = orderMapper.getTurnover(queryBegin, queryEnd, Orders.COMPLETED);
            turnoverList.add(turnover != null ? turnover : 0);
        }

        return TurnoverReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .turnoverList(StringUtils.join(turnoverList, ","))
                .build();
    }

    private List<LocalDate> generateDateList(LocalDate begin, LocalDate end){
        // 生成 dateList
        List<LocalDate> dateList = new ArrayList<>();
        while (!begin.equals(end)){
            dateList.add(begin);
            begin = begin.plusDays(1);
        }
        return dateList;
    }
}
