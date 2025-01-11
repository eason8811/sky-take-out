package com.sky.service;

import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

import javax.servlet.http.HttpServletResponse;
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

    /**
     * 根据给定的开始日期 begin 和结束日期 end 进行订单统计
     *
     * @param begin 开始日期
     * @param end 结束日期
     * @return 返回 OrderReportVO 的视图对象
     */
    OrderReportVO orderReport(LocalDate begin, LocalDate end);

    /**
     * 根据给定的开始日期 begin 和结束日期 end 进行销量排名前10统计
     *
     * @param begin 开始日期
     * @param end 结束日期
     * @return 返回 SalesTop10ReportVO 的视图对象
     */
    SalesTop10ReportVO salesTop10Report(LocalDate begin, LocalDate end);

    /**
     * 导出 Excel 文件
     *
     * @param httpServletResponse 响应对象
     */
    void export(HttpServletResponse httpServletResponse);
}
