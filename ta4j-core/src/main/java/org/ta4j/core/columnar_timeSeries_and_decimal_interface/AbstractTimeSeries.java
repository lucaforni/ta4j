package org.ta4j.core.columnar_timeSeries_and_decimal_interface;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * A time series that stores the bar data following a columnar store approach
 * @param <D> the data type for OHLC data, volume and amount
 *           see {@link Value}
 *           see {@link TimeSeries}
 */
public abstract class AbstractTimeSeries<D extends Value> implements TimeSeries<D>, Iterable<Bar<D>> {

    protected String name;

    protected Value[] openPrice;
    protected Value[] minPrice;
    protected Value[] maxPrice;
    protected Value[] closePrice;
    protected Value[] volume;
    protected int[] trades;
    protected Value[] amount;
    protected Duration[] timePeriod; // remove this by enum of period for the whole time series?
    protected ZonedDateTime[] beginTime; // work with long as timestamp?
    protected ZonedDateTime[] endTime;

    protected int position = -1;
    protected int capacity;
    protected int removed=0;
    protected boolean isWindow = false;

    protected NumOperationsFactory<D> numOperationsFactory;


    public AbstractTimeSeries(NumOperationsFactory<D> numOperationsFactory){
        this(false, numOperationsFactory);
    }

    public AbstractTimeSeries(int capacity, NumOperationsFactory<D> numOperationsFactory){
        this("unnamed",capacity,false,numOperationsFactory);
    }

    public AbstractTimeSeries( boolean isWindow, NumOperationsFactory<D> numOperationsFactory){
        this( 1000,isWindow, numOperationsFactory);
    }

    public AbstractTimeSeries(int capacity, boolean isWindow, NumOperationsFactory<D> numOperationsFactory){
        this("unnamed", capacity,isWindow, numOperationsFactory);
    }

    public AbstractTimeSeries(String name, int capacity, boolean isWindow, NumOperationsFactory<D> numOperationsFactory){
        this.name = name;
        this.capacity = capacity;
        this.isWindow = isWindow;
        this.numOperationsFactory = numOperationsFactory;
        openPrice = new Value[capacity]; //https://stackoverflow.com/questions/529085/how-to-create-a-generic-array-in-java
        minPrice = new Value[capacity];
        maxPrice = new Value[capacity];
        closePrice = new Value[capacity];
        volume = new Value[capacity];
        amount = new Value[capacity];
        timePeriod = new Duration[capacity];
        beginTime = new ZonedDateTime[capacity];
        endTime = new ZonedDateTime[capacity];
    }

    @Override
    public String getName(){
        return name;
    }

    @Override
    public NumOperationsFactory<D> getNumOperationsFactory(){
        return numOperationsFactory;
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

    public Stream<Value> getOpenPrices(){
        return Arrays.stream(openPrice);
    }

    public Stream<Value> getMinPrices(){
        return Arrays.stream(minPrice);
    }

    public Stream<Value> getMaxPrices(){
        return Arrays.stream(maxPrice);
    }

    public Stream<Value> getClosePrices(){
        return Arrays.stream(closePrice);
    }

    public Stream<Value> getVolumes(){
        return Arrays.stream(volume);
    }

    public Stream<Value> getAmounts(){
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
            return (D) AbstractTimeSeries.this.openPrice[index];
        }

        protected void setOpenPrice(D openPrice){ // i think it is faster not to use setter, but indices
            AbstractTimeSeries.this.openPrice[index] = openPrice;
        }

        @Override
        public D getMinPrice() {
            return (D) AbstractTimeSeries.this.minPrice[index];
        }

        protected void setMinPrice(D minPrice){ // i think it is faster not to use setter, but indices
            AbstractTimeSeries.this.minPrice[index] = minPrice;
        }

        @Override
        public D getMaxPrice() {
            return (D) AbstractTimeSeries.this.maxPrice[index];
        }

        protected void setMaxPrice(D maxPrice){ // i think it is faster not to use setter, but indices
            AbstractTimeSeries.this.maxPrice[index] = maxPrice;
        }

        @Override
        public D getClosePrice() {
            return (D) AbstractTimeSeries.this.closePrice[index];
        }


        protected void setClosePrice(D closePrice){ // i think it is faster not to use setter, but indices
            AbstractTimeSeries.this.closePrice[index] = closePrice;
        }

        @Override
        public D getVolume() {
            return (D) AbstractTimeSeries.this.volume[index];
        }

        protected void setVolumen(D volume){
            AbstractTimeSeries.this.volume[index] = volume;
        }

        @Override
        public int getTrades() {
            return AbstractTimeSeries.this.trades[index];
        }

        protected void setTrades(int trades){
            AbstractTimeSeries.this.trades[index] = trades;
        }

        @Override
        public D getAmount() {
            return (D) AbstractTimeSeries.this.amount[index];
        }            //TODO: implement

        protected void setAmount(D amount){
            AbstractTimeSeries.this.amount[index] = amount;
        }

        @Override
        public Duration getTimePeriod() {
            return AbstractTimeSeries.this.timePeriod[index];
        }

        @Override
        public ZonedDateTime getBeginTime() {
            return AbstractTimeSeries.this.beginTime[index];
        }

        @Override
        public ZonedDateTime getEndTime() {
            return AbstractTimeSeries.this.endTime[index];
        }

        @Override
        public void addTrade(double tradeVolume, double tradePrice) {
            //TODO: implement
        }

        @Override
        public void addTrade(String tradeVolume, String tradePrice) {
            //TODO: implement
        }

        @Override
        public void addTrade(D tradeVolume, D tradePrice) {

            if (openPrice == null) {
                AbstractTimeSeries.this.openPrice[index] = tradePrice;
            }
            setClosePrice(tradePrice);

            if (maxPrice == null) {
                AbstractTimeSeries.this.maxPrice[index] = tradePrice;
            } else {
                if(getMaxPrice().isLessThan(tradePrice)){
                    AbstractTimeSeries.this.maxPrice[index] = tradePrice;;
                }
            }
            if (minPrice == null) {
                AbstractTimeSeries.this.minPrice[index] = tradePrice;
            } else {
                if(getMinPrice().isGreaterThan(tradePrice)){
                    AbstractTimeSeries.this.minPrice[index] = tradePrice;
                }
            }
            AbstractTimeSeries.this.volume[index] = (D) AbstractTimeSeries.this.volume[index].plus(tradeVolume);
            AbstractTimeSeries.this.amount[index] = (D) AbstractTimeSeries.this.amount[index].plus(tradePrice);
            AbstractTimeSeries.this.trades[index] += 1;
        }

    }


    public class BarIterator implements Bar<D>, Iterator<Bar<D>> {

        private int currentIndex;
        private final int end;


        public BarIterator(){
            this.currentIndex = 1; // why not 0?
            this.end = AbstractTimeSeries.this.position;
        }

        public BarIterator(int currentIndex, int end){
            this.currentIndex = currentIndex-1;
            this.end = end;
        }

        @Override
        public D getOpenPrice() {
            return (D) AbstractTimeSeries.this.openPrice[currentIndex];
        }

        @Override
        public D getMinPrice() {
            return (D) AbstractTimeSeries.this.minPrice[currentIndex];
        }

        @Override
        public D getMaxPrice() {
            return (D) AbstractTimeSeries.this.maxPrice[currentIndex];
        }

        @Override
        public D getClosePrice() {
            return (D) AbstractTimeSeries.this.closePrice[currentIndex];
        }

        @Override
        public D getVolume() {
            return (D) AbstractTimeSeries.this.volume[currentIndex];
        }

        @Override
        public int getTrades() {
            return AbstractTimeSeries.this.trades[currentIndex];
        }

        @Override
        public D getAmount() {
            return (D) AbstractTimeSeries.this.amount[currentIndex];
        }

        @Override
        public Duration getTimePeriod() {
            return AbstractTimeSeries.this.timePeriod[currentIndex];
        }

        @Override
        public ZonedDateTime getBeginTime() {
            return AbstractTimeSeries.this.beginTime[currentIndex];
        }

        @Override
        public ZonedDateTime getEndTime() {
            return AbstractTimeSeries.this.endTime[currentIndex];
        }

        @Override
        public void addTrade(double tradeVolume, double tradePrice) {
            //TODO: implement
        }

        @Override
        public void addTrade(String tradeVolume, String tradePrice) {
            //TODO: implement
        }

        @Override
        public void addTrade(Value tradeVolume, Value tradePrice) {
            //TODO: implement
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
