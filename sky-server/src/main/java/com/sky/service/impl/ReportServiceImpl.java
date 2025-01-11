package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.exception.BaseException;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.service.WorkspaceService;
import com.sky.vo.*;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private WorkspaceService workspaceService;

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
        List<Double> turnoverList = new ArrayList<>();
        for (LocalDate date : dateList) {
            LocalDateTime queryBegin = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime queryEnd = LocalDateTime.of(date, LocalTime.MAX);
            Double turnover = orderMapper.getTurnover(queryBegin, queryEnd, Orders.COMPLETED);
            turnoverList.add(turnover != null ? turnover : 0);
        }

        return TurnoverReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .turnoverList(StringUtils.join(turnoverList, ","))
                .build();
    }

    /**
     * 根据给定的开始日期 begin 和结束日期 end 进行订单统计
     *
     * @param begin 开始日期
     * @param end 结束日期
     * @return 返回 OrderReportVO 的视图对象
     */
    @Override
    public OrderReportVO orderReport(LocalDate begin, LocalDate end) {
        // 生成 dateList
        List<LocalDate> dateList = generateDateList(begin, end);

        // 获取每日订单数
        List<Integer> orderCountList = new ArrayList<>();
        // 获取每日有效订单数
        List<Integer> validOrderCountList = new ArrayList<>();
        for (LocalDate date : dateList) {
            LocalDateTime queryBegin = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime queryEnd = LocalDateTime.of(date, LocalTime.MAX);
            Integer count = orderMapper.getCount(queryBegin, queryEnd);
            Integer validCount = orderMapper.getValidCount(queryBegin, queryEnd, Orders.COMPLETED);
            orderCountList.add(count);
            validOrderCountList.add(validCount);
        }

        // 获取订单总数
        Integer count = orderMapper.getCount(null, null);
        // 获取有效订单数
        Integer validCount = orderMapper.getValidCount(null, null, Orders.COMPLETED);
        // 订单完成率
        Double orderCompletionRate = Double.valueOf(validCount) / Double.valueOf(count);

        return OrderReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .orderCountList(StringUtils.join(orderCountList, ","))
                .validOrderCountList(StringUtils.join(validOrderCountList, ","))
                .totalOrderCount(count)
                .validOrderCount(validCount)
                .orderCompletionRate(orderCompletionRate)
                .build();
    }

    /**
     * 根据给定的开始日期 begin 和结束日期 end 进行销量排名前10统计
     *
     * @param begin 开始日期
     * @param end 结束日期
     * @return 返回 SalesTop10ReportVO 的视图对象
     */
    @Override
    public SalesTop10ReportVO salesTop10Report(LocalDate begin, LocalDate end) {
        // 获取销量 map
        LocalDateTime queryBegin = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime queryEnd = LocalDateTime.of(end, LocalTime.MAX);
        List<Map<String, Object>> top10Map = orderMapper.getTop10(queryBegin, queryEnd);
        List<String> nameList = top10Map.stream()
                .map(map -> (String) map.get("name"))
                .toList();

        List<Long> countList = top10Map.stream()
                .map(map -> (Long) map.get("count"))
                .toList();


        return SalesTop10ReportVO.builder()
                .nameList(StringUtils.join(nameList, ","))
                .numberList(StringUtils.join(countList, ","))
                .build();
    }

    /**
     * 导出 Excel 文件
     *
     * @param httpServletResponse 响应对象
     */
    @Override
    public void export(HttpServletResponse httpServletResponse) {
        LocalDate beginDate = LocalDate.now().minusDays(30);
        LocalDate endDate = LocalDate.now().minusDays(1);
        BusinessDataVO businessData = workspaceService.getBusinessData(LocalDateTime.of(beginDate, LocalTime.MIN), LocalDateTime.of(endDate, LocalTime.MAX));

        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("template/运营数据报表模板.xlsx");
        try {
            if (inputStream == null){
                throw new BaseException("模板文件未找到, 请联系管理员! ");
            }
            XSSFWorkbook excel = new XSSFWorkbook(inputStream);
            XSSFSheet sheet1 = excel.getSheet("sheet1");

            // 填入日期数据
            sheet1.getRow(1).getCell(1).setCellValue("时间: " + beginDate + "至" + endDate);

            // 填入概览数据
            // 概览数据第一行
            XSSFRow row = sheet1.getRow(3);
            row.getCell(2).setCellValue(businessData.getTurnover());
            row.getCell(4).setCellValue(businessData.getOrderCompletionRate());
            row.getCell(6).setCellValue(businessData.getNewUsers());
            // 概览数据第二行
            row = sheet1.getRow(4);
            row.getCell(2).setCellValue(businessData.getValidOrderCount());
            row.getCell(4).setCellValue(businessData.getUnitPrice());

            // 填入明细数据
            int i = 7;
            while (!beginDate.equals(endDate)){
                LocalDateTime begin = LocalDateTime.of(beginDate, LocalTime.MIN);
                LocalDateTime end = LocalDateTime.of(beginDate, LocalTime.MAX);

                BusinessDataVO business = workspaceService.getBusinessData(begin, end);
                row = sheet1.getRow(i);
                row.getCell(1).setCellValue(beginDate.toString());
                row.getCell(2).setCellValue(business.getTurnover());
                row.getCell(3).setCellValue(business.getValidOrderCount());
                row.getCell(4).setCellValue(business.getOrderCompletionRate());
                row.getCell(5).setCellValue(business.getUnitPrice());
                row.getCell(6).setCellValue(String.valueOf(business.getNewUsers()));

                beginDate = beginDate.plusDays(1);
                i++;
            }

            ServletOutputStream outputStream = httpServletResponse.getOutputStream();
            excel.write(outputStream);

            inputStream.close();
            outputStream.close();
            excel.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
