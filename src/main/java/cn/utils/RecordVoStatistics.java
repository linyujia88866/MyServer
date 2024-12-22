package cn.utils;

import cn.vo.RecordVo;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecordVoStatistics {

    public static Map<String, Double>  generateMonthlyReport(List<RecordVo> recordVoList, int year, int month) {
        // 使用Map来存储每个月的总花费，键为年份和月份的组合，值为该月的总花费
        Map<String, Double> monthlySpendMap = new HashMap<>();

        for (RecordVo recordVo : recordVoList) {
            Date date = recordVo.getDate();
            String monthDayKey = date.toString();
            double currentSpend = recordVo.getSpend();
            if (monthlySpendMap.containsKey(monthDayKey)) {
                double totalSpend = monthlySpendMap.get(monthDayKey);
                monthlySpendMap.put(monthDayKey, totalSpend + currentSpend);
            } else {
                monthlySpendMap.put(monthDayKey, currentSpend);
            }
        }
        return completeMapForMonth(monthlySpendMap, year, month);

        // 输出月度报表
//        System.out.println("月度报表：");
//        System.out.println("-------------------");
//        System.out.println("日期\t\t花费");
//        for (Map.Entry<String, Double> entry : monthlySpendMap.entrySet()) {
//            System.out.println(entry.getKey() + "\t\t" + entry.getValue());
//        }
    }
    public static Map<String, Double> completeMapForMonth(Map<String, Double> originalMap, int year, int month) {
        Map<String, Double> completedMap = new HashMap<>();

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = LocalDate.of(year, month, 30);

        LocalDate currentDate = startDate;
        while (currentDate.isBefore(endDate.plusDays(1))) {
            String dateKey = currentDate.toString();
            completedMap.put(dateKey, originalMap.getOrDefault(dateKey, 0.0));
            currentDate = currentDate.plusDays(1);
        }

        // 如果需要，可以将完成后的Map赋值回原Map，这里只是示例输出完成后的Map
//        System.out.println("补充完整后的Map:");
//        for (Map.Entry<String, Double> entry : completedMap.entrySet()) {
//            System.out.println(entry.getKey() + "=" + entry.getValue());
//        }
        return completedMap;
    }
//    public static void main(String[] args) {
//        // 示例数据，这里创建一个包含一些RecordVo对象的列表
//        List<RecordVo> recordVoList = new ArrayList<>();
//        System.out.println(LocalDate.now());
//        recordVoList.add(new RecordVo(200, 1832947713, "交通", "", Date.valueOf(LocalDate.now())));
//        // 可以继续添加更多的RecordVo对象到列表中
//
//        generateMonthlyReport(recordVoList);
//    }
public static void main(String[] args) {

}
}