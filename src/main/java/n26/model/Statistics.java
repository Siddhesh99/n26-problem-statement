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

    public Statistics(DoubleSummaryStatistics resultStatistics) {
        this.sum = resultStatistics.getSum();
        this.avg = resultStatistics.getAverage();
        this.max = resultStatistics.getMax();
        this.min = resultStatistics.getMin();
        this.count = resultStatistics.getCount();
    }

}
