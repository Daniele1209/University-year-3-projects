package Entities;

public final class Task implements Runnable {
    private final Polynomial pol1;
    private final Polynomial pol2;
    private final Polynomial result;
    private final int start;
    private final int end;

    public Task(int start, int end, Polynomial pol1, Polynomial pol2, Polynomial result) {
        this.start = start;
        this.end = end;
        this.result = result;
        this.pol1 = pol1;
        this.pol2 = pol2;
    }

    @Override
    public void run() {
        for (int idx = start; idx < end; idx++) {
            // Check if we still have elements to calculate
            if (idx > result.getLength()) {
                return;
            }
            // Find all the pairs that we add to obtain the value of a result coefficient
            for (int j = 0; j <= idx; j++) {
                if (j < pol1.getLength() && (idx - j) < pol2.getLength()) {
                    int value = pol1.getCoefficients().get(j) * pol2.getCoefficients().get(idx - j);
                    result.getCoefficients().set(idx, result.getCoefficients().get(idx) + value);
                }
            }
        }
    }
}
