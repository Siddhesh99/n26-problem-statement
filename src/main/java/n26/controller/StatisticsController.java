package n26.controller;

import n26.model.Statistics;
import n26.service.StatisticsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatisticsController {
    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    /**
     * @return statistics
     * Returns already calculated statistics in O(1)
     */

    @RequestMapping(value = "/statistics", method = RequestMethod.GET)
    public Statistics get() {
        return statisticsService.getStatistics();
    }
}
