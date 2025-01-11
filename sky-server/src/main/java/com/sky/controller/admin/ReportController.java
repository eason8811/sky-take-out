package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

@RestController
@RequestMapping("/admin/report")
@Slf4j
public class ReportController {

    @Autowired
    private ReportService reportService;

    /**
     * 根据给定的开始日期 begin 和结束日期 end 进行用户统计
     *
     * @param begin 开始日期
     * @param end   结束日期
     * @return 返回Result格式的对象
     */
    @GetMapping("/userStatistics")
    public Result<UserReportVO> userReport(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                           @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("正在进行用户统计, 开始日期为: {}, 结束日期为: {}", begin, end);
        UserReportVO userReportVO = reportService.userReport(begin, end);
        return Result.success(userReportVO);
    }

    /**
     * 根据给定的开始日期 begin 和结束日期 end 进行营业额统计
     *
     * @param begin 开始日期
     * @param end   结束日期
     * @return 返回Result格式的对象
     */
    @GetMapping("/turnoverStatistics")
    public Result<TurnoverReportVO> turnoverReport(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                                   @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("正在进行营业额统计, 开始日期为: {}, 结束日期为: {}", begin, end);
        TurnoverReportVO turnoverReportVO = reportService.turnoverReport(begin, end);
        return Result.success(turnoverReportVO);
    }

    /**
     * 根据给定的开始日期 begin 和结束日期 end 进行订单统计
     *
     * @param begin 开始日期
     * @param end   结束日期
     * @return 返回Result格式的对象
     */
    @GetMapping("/ordersStatistics")
    public Result<OrderReportVO> orderReport(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                             @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("正在进行订单统计, 开始日期为: {}, 结束日期为: {}", begin, end);
        OrderReportVO orderReportVO = reportService.orderReport(begin, end);
        return Result.success(orderReportVO);
    }

    /**
     * 根据给定的开始日期 begin 和结束日期 end 进行销量排名前10统计
     *
     * @param begin 开始日期
     * @param end   结束日期
     * @return 返回Result格式的对象
     */
    @GetMapping("/top10")
    public Result<SalesTop10ReportVO> salesTop10Report(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                                       @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("正在进行销量统计, 开始日期为: {}, 结束日期为: {}", begin, end);
        SalesTop10ReportVO salesTop10ReportVO = reportService.salesTop10Report(begin, end);
        return Result.success(salesTop10ReportVO);
    }

    /**
     * 导出 Excel 文件
     *
     * @param httpServletResponse 响应对象
     */
    @GetMapping("/export")
    public void export(HttpServletResponse httpServletResponse) {
        reportService.export(httpServletResponse);
    }
}
