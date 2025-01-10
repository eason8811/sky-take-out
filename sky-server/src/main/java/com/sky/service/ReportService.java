package com.sky.service;

import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

import java.time.LocalDate;

public interface ReportService {

    /**
     * 根据给定的开始日期 begin 和结束日期 end 进行用户统计
     *
     * @param begin 开始日期
     * @param end 结束日期
     * @return 返回 UserReportVO 的视图对象
     */
    UserReportVO userReport(LocalDate begin, LocalDate end);

    /**
     * 根据给定的开始日期 begin 和结束日期 end 进行营业额统计
     *
     * @param begin 开始日期
     * @param end 结束日期
     * @return 返回 TurnoverReportVO 的视图对象
     */
    TurnoverReportVO turnoverReport(LocalDate begin, LocalDate end);
}
