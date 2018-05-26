package n26.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.DoubleSummaryStatistics;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class Statistics {
    private double sum;
    private double avg;
    private double max;
    private double min;
    private long count;

    public Statistics(DoubleSummaryStatistics totalStatistics) {
        this.sum = totalStatistics.getSum();
        this.avg = totalStatistics.getAverage();
        this.max = totalStatistics.getMax();
        this.min = totalStatistics.getMin();
        this.count = totalStatistics.getCount();
    }

}
