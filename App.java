package com.binance.learn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.BinanceApiWebSocketClient;
import com.binance.api.client.domain.event.CandlestickEvent;
import com.binance.api.client.domain.market.Candlestick;
import com.binance.api.client.domain.market.CandlestickInterval;

/**
 * Hello world!
 *
 */


public class App 
{
	
    public static void main( String[] args ) throws IOException
    {
    	MovingAverage ma = new MovingAverage();
    	ma.initAlgo();
    }
    
  
}
