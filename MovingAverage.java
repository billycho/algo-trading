package com.binance.learn;

import java.util.ArrayList;
import java.util.List;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.BinanceApiWebSocketClient;
import com.binance.api.client.domain.account.Account;
import com.binance.api.client.domain.account.NewOrderResponse;
import com.binance.api.client.domain.account.Trade;
import com.binance.api.client.domain.event.CandlestickEvent;
import com.binance.api.client.domain.market.CandlestickInterval;

import static com.binance.api.client.domain.account.NewOrder.limitBuy;
import static com.binance.api.client.domain.account.NewOrder.marketBuy;
import static com.binance.api.client.domain.account.NewOrder.marketSell;

public class MovingAverage {
	String apikey = "123";
	String secret = "123";
	List<CandlestickEvent> candleList;
	
	String symbol = "ethbidr";
	Double buyPrice = 30000000.00;
	Double sl = 0.2;
	Double tp = 0.2;
	//period
	int limit = 5;
	double minutes = 0;

	public MovingAverage() {
		candleList = new ArrayList<CandlestickEvent>();
	}

	public void initAlgo() {
		System.out.println("Algo Start");
		BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance(apikey, secret);
		BinanceApiWebSocketClient webClient = factory.newWebSocketClient();
		BinanceApiRestClient client = factory.newRestClient();
		
      Account account = client.getAccount();
      System.out.println(account.getAssetBalance("BIDR"));
      System.out.println(account.getAssetBalance("BIDR").getFree());
      
      List<Trade> myTrades = client.getMyTrades("BUSDBIDR");
      System.out.println(myTrades);
      
      
      //NewOrderResponse newOrderResponse = client.newOrder(marketBuy("BUSDBIDR", "1.5"));
      //NewOrderResponse newOrderResponse = client.newOrder(marketSell("BUSDBIDR", "1.4"));
      //System.out.println(newOrderResponse);
		webClient.onCandlestickEvent(symbol, CandlestickInterval.ONE_MINUTE, response -> {
			
			System.out.println(response.getClose());
		    	
			candleList.add(response);

			if (candleList.size() % limit == 0) {
				System.out.println("ma");
				double total = 0, movingAverage = 0, percentage = 0.2;
				int i = candleList.size() - 1 , counter = 0;
				double currentPrice = Double.parseDouble(candleList.get(i).getClose());
				while(true)
				{
					System.out.println(candleList.get(i).getClose());
					total += Double.parseDouble(candleList.get(i).getClose());

					i--;
					counter++;

					if (counter >= limit) {
						break;
					}
				}

				movingAverage = Math.round(total / limit);
				System.out.println("Moving Average = " + movingAverage);
				System.out.println("Current Price = " + currentPrice);
				
				//get asset
			      
				
				//check if asset went up 20% or went down 20%
				double currentAssetValue = Double.parseDouble(account.getAssetBalance("BIDR").getFree())/ currentPrice;
				double originalAssetValue = Double.parseDouble(account.getAssetBalance("BIDR").getFree())/ buyPrice;
				
				if( originalAssetValue+ (originalAssetValue * tp) >= currentAssetValue ) 
				{
					System.out.println("take profit " + tp*100 + "%");
					 //client.newOrderTest(marketSell(symbol, Double.toString(originalAssetValue * tp)));
					//take profit
				}
				else if(originalAssetValue - (originalAssetValue * sl) <= currentAssetValue)
				{
					System.out.println("take profit " + sl*100 + "%");
					 //client.newOrderTest(marketSell(symbol, Double.toString(originalAssetValue * sl)));
					//stop lose
				}
			
				
				if (currentPrice < movingAverage) {
					System.out.println("buy indicator");
				    //client.newOrderTest(marketBuy(symbol, "1.5"));

					//buy 1,5 usd
				    //client.newOrderTest(marketSell(symbol, "0.0007"));
					// sell indicator -> stop loss
				} else if (currentPrice > movingAverage) {
					System.out.println("sell indicator");
					//client.newOrderTest(marketSell(symbol, "1.5"));

					//sell 1.5 usd
					// client.newOrderTest(marketBuy(symbol, "0.0007"));
					// buy indicator
				}
			}
		});
	}
}
