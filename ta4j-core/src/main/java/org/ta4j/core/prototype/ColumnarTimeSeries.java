package org.ta4j.core.prototype;

import org.ta4j.core.prototype.num.Num;
import org.ta4j.core.prototype.num.NumFactory;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * A time series that stores the bar data following a columnar store approach
 * @param <D> the data type for OHLC data, volume and amount
 *           see {@link Num}
 *           see {@link TimeSeries}
 */
public class ColumnarTimeSeries<D extends Num> implements TimeSeries<D>, Iterable<Bar<D>> {

    private String name;
    private int removedBars;
    private ArrayList<D> openPrice;
    private ArrayList<D> minPrice;
    private ArrayList<D> maxPrice;
    private ArrayList<D> closePrice;
    private ArrayList<D> volume;
    private ArrayList<Integer> trades;
    private ArrayList<D> amount;
    private ArrayList<Duration> timePeriod; // remove this by enum of period for the whole time series?
    private ArrayList<ZonedDateTime> beginTime; // work with long as timestamp?
    private ArrayList<ZonedDateTime> endTime;
    private int capacity;
    private NumFactory<D> numFactory;


    /**
     * Creates a ColumnarTimeSeries
     * @param numFactory the {@link NumFactory factory} for the underlying {@link Num Num} implementation
     */
    public ColumnarTimeSeries(NumFactory<D> numFactory){
        this("unnamed",Integer.MAX_VALUE, numFactory);
    }

    /**
     *
     * @param name the name of the time series
     * @param numFactory the {@link NumFactory factory} for the underlying {@link Num Num} implementation
     */
    public ColumnarTimeSeries(String name, NumFactory<D> numFactory){
        this(name, Integer.MAX_VALUE, numFactory);
    }


    /**
     *
     * @param capacity the capacity (default unlimited = Integer.MAX)
     * @param numFactory the {@link NumFactory factory} for the underlying {@link Num Num} implementation
     */
    public ColumnarTimeSeries(int capacity, NumFactory<D> numFactory){
        this("unnamed", capacity, numFactory);
    }

    /**
     *
     * @param name the name (e.g. ticker or symbol) of this TimeSeries
     * @param numFactory the {@link NumFactory factory} of the underlying {@Num num} implementation
     * @see {@link TimeSeriesFactory}
     */
    public ColumnarTimeSeries(String name, int capacity, NumFactory<D> numFactory){
        this.name = name;
        this.capacity = capacity;
        this.numFactory = numFactory;
        this.removedBars = 0;
        openPrice = new ArrayList<>();
        minPrice = new ArrayList<>();
        maxPrice = new ArrayList<>();
        trades = new ArrayList<>();
        closePrice = new ArrayList<>();
        volume = new ArrayList<>();
        amount = new ArrayList<>();
        timePeriod = new ArrayList<>();
        beginTime = new ArrayList<>();
        endTime = new ArrayList<>();
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
        return endTime.size();
    }

    @Override
    public NumFactory<D> getNumFactory(){
        return numFactory;
    }

    /**
     * Adds a bar to the time series.
     * <p/>
     * If the size of the time series is equal the capacity (default = Integer.MAX)
     * the first value will be removed (and the bars do a left shift by 1)
     *
     * @param startTime the start time of the bar
     * @param time the end time of the bar
     * @param open the open price
     * @param min the min (low) price
     * @param max the max (high) price
     * @param close the close price
     * @param vol the volume
     * @param trades the trades of the bar
     * @param amount the amount of the bar
     */
    public void addBar(ZonedDateTime startTime, ZonedDateTime time, D open, D min, D max, D close, D vol, int trades, D amount){

        if(capacity <= endTime.size()) {
            beginTime.remove(0);
            endTime.remove(0);
            openPrice.remove(0);
            minPrice.remove(0);
            maxPrice.remove(0);
            closePrice.remove(0);
            volume.remove(0);
            this.trades.remove(0);
            this.amount.remove(0);

        }

        beginTime.add(startTime);
        endTime.add(time);
        openPrice.add(open);
        minPrice.add(min);
        maxPrice.add(max);
        closePrice.add(close);
        volume.add(vol);
        this.trades.add(trades);
        this.amount.add(amount);

    }

    /**
     * Inserts (overrides) bar data to the index pos
     * @param pos the index
     * @param time endTime of the bar
     * @param open open price
     * @param min min (low) price
     * @param max max (high) price
     * @param close close (last) price
     * @param vol volume
     */
    public void insertBar(int pos, ZonedDateTime time, D open, D min, D max, D close, D vol){
        if(pos < 0 || pos >= capacity){
            throw new IndexOutOfBoundsException(
                    String.format("Column of time series '%s' not accesible. pos: %s, capacity: %s, isWindow: %s",
                            name, pos,capacity, isWindow()));
        }
        endTime.set(pos, time);
        openPrice.set(pos, open);
        minPrice.set(pos, min);
        maxPrice.set(pos, max);
        closePrice.set(pos, close);
        volume.set(pos, vol);
    }

    public void addBars(List<Bar<D>> bars){
        for(Bar b: bars){
            addBar(b);
        }
    }

    public int getWindowSize(){
        return capacity;
    }

    /**
     * Check if this time series object is a windows
     * @return true if the time series capacity is limited
     */
    @Override
    public boolean isWindow(){
        return capacity != Integer.MAX_VALUE;
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
        return endTime.size()-1; // highest index with data
    }

    @Override
    public int getRemovedBarCount() {
        return this.removedBars;
    }


    @Override
    public void addBar(Bar<D> bar) {
        addBar(bar.getBeginTime(), bar.getEndTime(),bar.getOpenPrice(),bar.getMinPrice(), bar.getMaxPrice(),
                bar.getClosePrice(),bar.getVolume(),bar.getTrades(), bar.getAmount());
    }

    public D getOpenPrice(int index){
        return (D) openPrice.get(index);
    }

    public D getMinPrice(int index){
        return (D) minPrice.get(index);
    }

    public D getMaxPrice(int index){
        return (D)  maxPrice.get(index);
    }

    public D getClosePrice(int index){
        return (D) closePrice.get(index);
    }

    public D getAmount(int index){
        return (D) amount.get(index);
    }

    public D getVolume(int index){
        return (D) volume.get(index);
    }

    public int getTrades(int index){
        return trades.get(index);
    }

    public Duration getDuration(int index){
        return timePeriod.get(index);
    }

    public ZonedDateTime getBeginTime(int index){
        return beginTime.get(index);
    }

    public ZonedDateTime getEndTime(int index){
        return endTime.get(index);
    }

    public Stream<D> getOpenPrices(){
        return openPrice.stream();
    }

    public Stream<D> getMinPrices(){
        return minPrice.stream();
    }

    public Stream<D> getMaxPrices(){
        return maxPrice.stream();
    }

    public Stream<D> getClosePrices(){
        return closePrice.stream();
    }

    public Stream<D> getVolumes(){
        return volume.stream();
    }

    public Stream<D> getAmounts(){
        return amount.stream();
    }

    public Stream<Integer> getTrades_stream(){
        return trades.stream();
    }

    public Stream<Duration> getTimePeriods(){
        return timePeriod.stream();
    }

    public Stream<ZonedDateTime> getBeginTimes(){
        return beginTime.stream();
    }

    public Stream<ZonedDateTime> getEndTimes(){
        return endTime.stream();
    }

    public Stream<Bar<D>> stream(int start, int end) {
        final Iterator<Bar<D>> iterator = iterator(start, end);
        final Spliterator<Bar<D>> spliterator
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
            return openPrice.get(index);
        }

        protected void setOpenPrice(D openPrice){ // i think it is faster not to use setter, but indices
            ColumnarTimeSeries.this.openPrice.set(index, openPrice);
        }

        @Override
        public D getMinPrice() {
            return minPrice.get(index);
        }

        protected void setMinPrice(D minPrice){ // i think it is faster not to use setter, but indices
            ColumnarTimeSeries.this.minPrice.set(index, minPrice);
        }

        @Override
        public D getMaxPrice() {
            return maxPrice.get(index);
        }

        protected void setMaxPrice(D maxPrice){ // i think it is faster not to use setter, but indices
            ColumnarTimeSeries.this.maxPrice.set(index, maxPrice);
        }

        @Override
        public D getClosePrice() {
            return ColumnarTimeSeries.this.closePrice.get(index);
        }


        protected void setClosePrice(D closePrice){ // i think it is faster not to use setter, but indices
            ColumnarTimeSeries.this.closePrice.set(index, closePrice);
        }

        @Override
        public D getVolume() {
            return ColumnarTimeSeries.this.getVolume(index);
        }

        protected void setVolumen(D volume){
            ColumnarTimeSeries.this.volume.set(index, volume);
        }

        @Override
        public int getTrades() {
            return ColumnarTimeSeries.this.trades.get(index);
        }

        protected void setTrades(int trades){
            ColumnarTimeSeries.this.trades.set(index, trades);
        }

        @Override
        public D getAmount() {
            return (D) ColumnarTimeSeries.this.amount.get(index);
        }

        protected void setAmount(D amount){
            ColumnarTimeSeries.this.amount.set(index, amount);
        }

        @Override
        public Duration getTimePeriod() {
            return ColumnarTimeSeries.this.timePeriod.get(index);
        }

        @Override
        public ZonedDateTime getBeginTime() {
            return ColumnarTimeSeries.this.beginTime.get(index);
        }

        @Override
        public ZonedDateTime getEndTime() {
            return ColumnarTimeSeries.this.endTime.get(index);
        }

        @Override
        public void addTrade(D tradeVolume, D tradePrice) {
            if (openPrice.get(index) == null) {
                openPrice.set(index, tradePrice);
            }
            closePrice.set(index, tradePrice);

            if (maxPrice.get(index) == null) {
                maxPrice.set(index, tradePrice);
            } else {
                maxPrice.set(index, maxPrice.get(index).isLessThan(tradePrice) ? tradePrice : maxPrice.get(index));
            }
            if (minPrice.get(index) == null) {
                minPrice.set(index, tradePrice);
            } else {
                minPrice.set(index, minPrice.get(index).isGreaterThan(tradePrice) ? tradePrice : minPrice.get(index));
            }

            volume.set(index, (D) tradeVolume.plus(volume.get(index)));
            amount.set(index, (D) amount.get(index).plus(tradeVolume.multipliedBy(tradePrice)));
            trades.set(index, trades.get(index)+1);
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
            this.end = ColumnarTimeSeries.this.getEndIndex();
        }

        public BarIterator(int currentIndex, int end){
            this.currentIndex = currentIndex-1;
            this.end = end;
        }

        @Override
        public D getOpenPrice() {
            return openPrice.get(currentIndex);
        }

        @Override
        public D getMinPrice() {
            return (D) ColumnarTimeSeries.this.minPrice.get(currentIndex);
        }

        @Override
        public D getMaxPrice() {
            return (D) ColumnarTimeSeries.this.maxPrice.get(currentIndex);
        }

        @Override
        public D getClosePrice() {
            return (D) ColumnarTimeSeries.this.closePrice.get(currentIndex);
        }

        @Override
        public D getVolume() {
            return (D) ColumnarTimeSeries.this.volume.get(currentIndex);
        }

        @Override
        public int getTrades() {
            return ColumnarTimeSeries.this.trades.get(currentIndex);
        }

        @Override
        public D getAmount() {
            return (D) ColumnarTimeSeries.this.amount.get(currentIndex);
        }

        @Override
        public Duration getTimePeriod() {
            return ColumnarTimeSeries.this.timePeriod.get(currentIndex);
        }

        @Override
        public ZonedDateTime getBeginTime() {
            return ColumnarTimeSeries.this.beginTime.get(currentIndex);
        }

        @Override
        public ZonedDateTime getEndTime() {
            return ColumnarTimeSeries.this.endTime.get(currentIndex);
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
            if (openPrice.get(currentIndex) == null) {
                openPrice.set(currentIndex, tradePrice);
            }
            closePrice.set(currentIndex, tradePrice);

            if (maxPrice.get(currentIndex) == null) {
                maxPrice.set(currentIndex, tradePrice);
            } else {
                maxPrice.set(currentIndex, maxPrice.get(currentIndex).isLessThan(tradePrice) ? tradePrice : maxPrice.get(currentIndex));
            }
            if (minPrice.get(currentIndex) == null) {
                minPrice.set(currentIndex, tradePrice);
            } else {
                minPrice.set(currentIndex, minPrice.get(currentIndex).isGreaterThan(tradePrice) ? tradePrice : minPrice.get(currentIndex));
            }

            volume.set(currentIndex, (D) tradeVolume.plus(volume.get(currentIndex)));
            amount.set(currentIndex, (D) amount.get(currentIndex).plus(tradeVolume.multipliedBy(tradePrice)));
            trades.set(currentIndex, trades.get(currentIndex)+1);
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
