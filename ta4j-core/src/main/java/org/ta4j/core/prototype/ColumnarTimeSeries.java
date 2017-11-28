package org.ta4j.core.prototype;

import org.ta4j.core.prototype.num.Num;
import org.ta4j.core.prototype.num.NumFactory;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * A time series that stores the bar data following a columnar store approach
 * @param <D> the data type for OHLC data, volume and amount
 *           see {@link num}
 *           see {@link TimeSeries}
 */
public class ColumnarTimeSeries<D extends Num> implements TimeSeries<D>, Iterable<Bar<D>> {

    protected String name;

    private Num[] openPrice;
    private Num[] minPrice;
    private Num[] maxPrice;
    private Num[] closePrice;
    private Num[] volume;
    private int[] trades;
    private Num[] amount;
    protected Duration[] timePeriod; // remove this by enum of period for the whole time series?
    protected ZonedDateTime[] beginTime; // work with long as timestamp?
    protected ZonedDateTime[] endTime;

    protected int position = -1;
    protected int capacity;
    protected int removed=0;
    protected boolean isWindow = false;

    protected NumFactory<D> numFactory;


    /**
     * Creates a ColumnarTimeSeries with default bar capacity of 10000
     * @param numFactory
     */
    public ColumnarTimeSeries(NumFactory<D> numFactory){
        this(10000, numFactory);
    }

    public ColumnarTimeSeries(int capacity, NumFactory<D> numFactory){
        this("unnamed",capacity,false, numFactory);
    }


    public ColumnarTimeSeries(int capacity, boolean isWindow, NumFactory<D> numFactory){
        this("unnamed", capacity,isWindow, numFactory);
    }

    /**
     *
     * @param name the name (e.g. ticker or symbol) of this TimeSeries
     * @param capacity the initial capacity (will be automatically increased if isWindow = false
     * @param isWindow if true the capacity will stay fix. If the TimeSeries is full the first entries will be shifted out
     * @param numFactory the {@link NumFactory factory} of the underlying {@Num num} implementation
     * @see {@link TimeSeriesFactory}
     */
    public ColumnarTimeSeries(String name, int capacity, boolean isWindow, NumFactory<D> numFactory){
        this.name = name;
        this.capacity = capacity;
        this.isWindow = isWindow;
        this.numFactory = numFactory;
        openPrice = new Num[capacity]; //https://stackoverflow.com/questions/529085/how-to-create-a-generic-array-in-java
        minPrice = new Num[capacity];
        maxPrice = new Num[capacity];
        trades = new int[capacity];
        closePrice = new Num[capacity];
        volume = new Num[capacity];
        amount = new Num[capacity];
        timePeriod = new Duration[capacity];
        beginTime = new ZonedDateTime[capacity];
        endTime = new ZonedDateTime[capacity];
    }

    @Override
    public String getName(){
        return name;
    }

    @Override
    public Bar<D> getBar(int i) {
        return new BarAccessCursor(i);
    }

    @Override
    public int getBarCount() {
        return position+1;
    }

    @Override
    public NumFactory<D> getNumFactory(){
        return numFactory;
    }

    public void addBar(ZonedDateTime time, D open, D min, D max, D close, D vol){
        if(capacity <= position+1) {
            throw new IndexOutOfBoundsException(
                    String.format("Columns of time series '%s' are full. index: %s, capacity: %s, isWindow: %s",
                            name, position,capacity, isWindow));
        }
        position++;
        endTime[position] = time;
        openPrice[position] = open;
        minPrice[position] = min;
        maxPrice[position] = max;
        closePrice[position] = close;
        volume[position] = vol;
        increaseArraySize();
    }

    /**
     * Inserts (overrides) bar data to the index pos
     * @param pos the index
     * @param time endTime of the bar
     * @param open open price
     * @param min min/low price
     * @param max max/high price
     * @param close close/last price
     * @param vol volume
     */
    public void insertBar(int pos, ZonedDateTime time, D open, D min, D max, D close, D vol){
        if(pos < 0 || pos >= capacity){
            throw new IndexOutOfBoundsException(
                    String.format("Column of time series '%s' not accesible. pos: %s, capacity: %s, isWindow: %s",
                            name, pos,capacity, isWindow));
        }
        endTime[pos] = time;
        openPrice[pos] = open;
        minPrice[pos] = min;
        maxPrice[pos] = max;
        closePrice[pos] = close;
        volume[pos] = vol;
    }

    public void addBars(List<Bar<D>> bars){
        for(Bar b: bars){
            addBar(b);
        }
    }

    public int getWindowSize(){
        if(!isWindow){
            return Integer.MAX_VALUE;
        }
        return capacity;
    }

    @Override
    public boolean isWindow(){
        return isWindow;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public List<Bar<D>> getBarData() {
        return null;
    }

    @Override
    public int getBeginIndex() {
        return 0; // always zero
    }

    @Override
    public int getEndIndex() {
        return position; // highest index with data
    }

    @Override
    public int getRemovedBarCount() {
        return 0;
    }

    @Override
    public void addBar(Bar<D> bar) {
        addBar(bar.getEndTime(),bar.getOpenPrice(),bar.getMinPrice(), bar.getMaxPrice(), bar.getClosePrice(),bar.getVolume());
    }

    public D getOpenPrice(int index){
        return (D) openPrice[index];
    }

    public D getMinPrice(int index){
        return (D) minPrice[index];
    }

    public D getMaxPrice(int index){
        return (D)  maxPrice[index];
    }

    public D getClosePrice(int index){
        return (D) closePrice[index];
    }

    public D getAmount(int index){
        return (D) amount[index];
    }

    public D getVolume(int index){
        return (D) volume[index];
    }

    public int getTrades(int index){
        return trades[index];
    }

    public Duration getDuration(int index){
        return timePeriod[index];
    }

    public ZonedDateTime getBeginTime(int index){
        return beginTime[index];
    }

    public ZonedDateTime getEndTime(int index){
        return endTime[index];
    }

    public Stream<Num> getOpenPrices(){
        return Arrays.stream(openPrice);
    }

    public Stream<Num> getMinPrices(){
        return Arrays.stream(minPrice);
    }

    public Stream<Num> getMaxPrices(){
        return Arrays.stream(maxPrice);
    }

    public Stream<Num> getClosePrices(){
        return Arrays.stream(closePrice);
    }

    public Stream<Num> getVolumes(){
        return Arrays.stream(volume);
    }

    public Stream<Num> getAmounts(){
        return Arrays.stream(amount);
    }

    public IntStream getTrades_stream(){
        return Arrays.stream(trades);
    }

    public Stream<Duration> getTimePeriods(){
        return Arrays.stream(timePeriod);
    }

    public Stream<ZonedDateTime> getBeginTimes(){
        return Arrays.stream(beginTime);
    }

    public Stream<ZonedDateTime> getEndTimes(){
        return Arrays.stream(endTime);
    }

    public Stream<Bar> stream(int start, int end) {
        final Iterator<Bar<D>> iterator = iterator(start, end);
        final Spliterator<Bar> spliterator
                = Spliterators.spliteratorUnknownSize(iterator, 0);
        return StreamSupport.stream(spliterator, false);
    }


    @Override
    public Iterator<Bar<D>> iterator() {
        return new BarIterator();
    }

    public BarIterator iterator(int start, int end){
        return new BarIterator(start, end);
    }


    /**
     * Get data of the columns at index represented as a {@link BarAccessCursor Cursor} that implements the {@link Bar Bar} interface
     * @param index the index
     * @return a {@link BarAccessCursor Cursor} that implements the {@link Bar Bar} interface
     */
    public BarAccessCursor get(int index){
        return new BarAccessCursor(index);
    }


    private void increaseArraySize(){
        if(isWindow){
            return;
        }
        if(capacity <= position+1){
            //Todo: implement array expansion logic
        }
    }

    private int findUpperTimestampBoundBegin(ZonedDateTime max) {
        int maxNdx = Arrays.binarySearch(beginTime, max);
        if (maxNdx < 0)
            maxNdx = (-maxNdx) - 1;
        while (maxNdx < (beginTime.length - 1) && beginTime[maxNdx] == beginTime[maxNdx + 1])
            maxNdx++;
        return maxNdx;
    }

    private int findLowerTimestampBoundBegin(ZonedDateTime min) {
        int minNdx = Arrays.binarySearch(beginTime, min);
        if (minNdx < 0)
            minNdx = (-minNdx) - 1;
        while (minNdx > 0 && beginTime[minNdx] == beginTime[minNdx - 1])
            minNdx--;
        return minNdx;
    }

    private int findUpperTimestampBoundEnd(ZonedDateTime max) {
        int maxNdx = Arrays.binarySearch(endTime, max);
        if (maxNdx < 0)
            maxNdx = (-maxNdx) - 1;
        while (maxNdx < (endTime.length - 1) && endTime[maxNdx] == endTime[maxNdx + 1])
            maxNdx++;
        return maxNdx;
    }

    private int findLowerTimestampBoundEnd(ZonedDateTime min) {
        int minNdx = Arrays.binarySearch(endTime, min);
        if (minNdx < 0)
            minNdx = (-minNdx) - 1;
        while (minNdx > 0 && endTime[minNdx] == endTime[minNdx - 1])
            minNdx--;
        return minNdx;
    }




    public class BarAccessCursor implements Bar<D>{
        private int index;

        protected BarAccessCursor(int index){
            this.index = index;
        }

        public BarAccessCursor at(int index){
            this.index = index;
            return this;
        }


        @Override
        public D getOpenPrice() {
            return (D) ColumnarTimeSeries.this.openPrice[index];
        }

        protected void setOpenPrice(D openPrice){ // i think it is faster not to use setter, but indices
            ColumnarTimeSeries.this.openPrice[index] = openPrice;
        }

        @Override
        public D getMinPrice() {
            return (D) ColumnarTimeSeries.this.minPrice[index];
        }

        protected void setMinPrice(D minPrice){ // i think it is faster not to use setter, but indices
            ColumnarTimeSeries.this.minPrice[index] = minPrice;
        }

        @Override
        public D getMaxPrice() {
            return (D) ColumnarTimeSeries.this.maxPrice[index];
        }

        protected void setMaxPrice(D maxPrice){ // i think it is faster not to use setter, but indices
            ColumnarTimeSeries.this.maxPrice[index] = maxPrice;
        }

        @Override
        public D getClosePrice() {
            return (D) ColumnarTimeSeries.this.closePrice[index];
        }


        protected void setClosePrice(D closePrice){ // i think it is faster not to use setter, but indices
            ColumnarTimeSeries.this.closePrice[index] = closePrice;
        }

        @Override
        public D getVolume() {
            return (D) ColumnarTimeSeries.this.volume[index];
        }

        protected void setVolumen(D volume){
            ColumnarTimeSeries.this.volume[index] = volume;
        }

        @Override
        public int getTrades() {
            return ColumnarTimeSeries.this.trades[index];
        }

        protected void setTrades(int trades){
            ColumnarTimeSeries.this.trades[index] = trades;
        }

        @Override
        public D getAmount() {
            return (D) ColumnarTimeSeries.this.amount[index];
        }

        protected void setAmount(D amount){
            ColumnarTimeSeries.this.amount[index] = amount;
        }

        @Override
        public Duration getTimePeriod() {
            return ColumnarTimeSeries.this.timePeriod[index];
        }

        @Override
        public ZonedDateTime getBeginTime() {
            return ColumnarTimeSeries.this.beginTime[index];
        }

        @Override
        public ZonedDateTime getEndTime() {
            return ColumnarTimeSeries.this.endTime[index];
        }

        @Override
        public void addTrade(D tradeVolume, D tradePrice) {
            if (openPrice[index] == null) {
                openPrice[index] = tradePrice;
            }
            closePrice[index] = tradePrice;

            if (maxPrice[index] == null) {
                maxPrice[index] = tradePrice;
            } else {
                maxPrice[index] = maxPrice[index].isLessThan(tradePrice) ? tradePrice : maxPrice[index];
            }
            if (minPrice[index] == null) {
                minPrice[index] = tradePrice;
            } else {
                minPrice[index] = minPrice[index].isGreaterThan(tradePrice) ? tradePrice : minPrice[index];
            }
            volume[index] = volume[index].plus(tradeVolume);
            amount[index] = amount[index].plus(tradeVolume.multipliedBy(tradePrice));
            trades[index]++;
        }

        @Override
        public void addTrade(String tradeVolume, String tradePrice) {
            NumFactory<D> n = getNumFactory();
            addTrade(n.valueOf(tradeVolume), n.valueOf(tradePrice));
        }

        @Override
        public void addTrade(double tradeVolume, double tradePrice) {
            NumFactory<D> n = getNumFactory();
            addTrade(n.valueOf(tradeVolume), n.valueOf(tradePrice));
        }

    }


    public class BarIterator implements Bar<D>, Iterator<Bar<D>> {

        private int currentIndex;
        private final int end;


        public BarIterator(){
            this.currentIndex = 1; // why not 0?
            this.end = ColumnarTimeSeries.this.position;
        }

        public BarIterator(int currentIndex, int end){
            this.currentIndex = currentIndex-1;
            this.end = end;
        }

        @Override
        public D getOpenPrice() {
            return (D) ColumnarTimeSeries.this.openPrice[currentIndex];
        }

        @Override
        public D getMinPrice() {
            return (D) ColumnarTimeSeries.this.minPrice[currentIndex];
        }

        @Override
        public D getMaxPrice() {
            return (D) ColumnarTimeSeries.this.maxPrice[currentIndex];
        }

        @Override
        public D getClosePrice() {
            return (D) ColumnarTimeSeries.this.closePrice[currentIndex];
        }

        @Override
        public D getVolume() {
            return (D) ColumnarTimeSeries.this.volume[currentIndex];
        }

        @Override
        public int getTrades() {
            return ColumnarTimeSeries.this.trades[currentIndex];
        }

        @Override
        public D getAmount() {
            return (D) ColumnarTimeSeries.this.amount[currentIndex];
        }

        @Override
        public Duration getTimePeriod() {
            return ColumnarTimeSeries.this.timePeriod[currentIndex];
        }

        @Override
        public ZonedDateTime getBeginTime() {
            return ColumnarTimeSeries.this.beginTime[currentIndex];
        }

        @Override
        public ZonedDateTime getEndTime() {
            return ColumnarTimeSeries.this.endTime[currentIndex];
        }

        @Override
        public void addTrade(double tradeVolume, double tradePrice) {
            NumFactory<D> n = getNumFactory();
            addTrade(n.valueOf(tradeVolume), n.valueOf(tradePrice));
        }

        @Override
        public void addTrade(String tradeVolume, String tradePrice) {
            NumFactory<D> n = getNumFactory();
            addTrade(n.valueOf(tradeVolume), n.valueOf(tradePrice));
        }

        @Override
        public void addTrade(D tradeVolume, D tradePrice) {
            if (openPrice[currentIndex] == null) {
                openPrice[currentIndex] = tradePrice;
            }
            closePrice[currentIndex] = tradePrice;

            if (maxPrice[currentIndex] == null) {
                maxPrice[currentIndex] = tradePrice;
            } else {
                maxPrice[currentIndex] = maxPrice[currentIndex].isLessThan(tradePrice) ? tradePrice : maxPrice[currentIndex];
            }
            if (minPrice[currentIndex] == null) {
                minPrice[currentIndex] = tradePrice;
            } else {
                minPrice[currentIndex] = minPrice[currentIndex].isGreaterThan(tradePrice) ? tradePrice : minPrice[currentIndex];
            }
            volume[currentIndex] = volume[currentIndex].plus(tradeVolume);
            amount[currentIndex] = amount[currentIndex].plus(tradeVolume.multipliedBy(tradePrice));
            trades[currentIndex]++;
        }

        @Override
        public boolean hasNext() {
            return currentIndex < end;
        }

        @Override
        public BarIterator next() {
            currentIndex++;
            return this;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Cannot remove from array base");
        }

    }

}
