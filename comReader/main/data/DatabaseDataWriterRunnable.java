package data;

import java.net.UnknownHostException;
import java.util.concurrent.BlockingQueue;

import main.Application;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

public class DatabaseDataWriterRunnable implements Runnable {

	private static final int MILLIS_TO_SLEEP_IF_QUEUE_IS_EMPTY = 10;
	private Mongo mongo;
	private DB db;
	private DBCollection sampleData;
	
	public DatabaseDataWriterRunnable() {
		// TODO Auto-generated constructor stub
		
		try {
			mongo = new Mongo("127.0.0.1");
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
//        db = mongo.getDB("javaTest");
//        sampleData = db.getCollection("sampleData");
        db = mongo.getDB("javaTest");
        sampleData = db.getCollection("sampleData");
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		BlockingQueue<WatchPositionData> calculatedPositionsQueue = Application.getApplication().getController().getCalculatedPositionsQueue();
		
		while (true) { //always
			
			if(calculatedPositionsQueue.isEmpty()) { 
				
				try {
					Thread.sleep(MILLIS_TO_SLEEP_IF_QUEUE_IS_EMPTY); // try again in some time
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			} else { // queue is not empty, take WatchPositionData object and write it into the database
				
				
				// we take watchPositionData object from the queue by calling method 'poll()' on the queue
				WatchPositionData watchPositionData = calculatedPositionsQueue.poll();
				
				// WRITE HERE THE watchPositionData TO MONGODB
				System.out.println(watchPositionData);
				// TODO write watchPositionData object into the database
				
			    try {
		        	DBObject documentDetail = new BasicDBObject();
		        	documentDetail.put("x", watchPositionData.getPosition().getX());
		        	documentDetail.put("y", watchPositionData.getPosition().getY());
		        	documentDetail.put("insertedAt", watchPositionData.getTime());
		        	documentDetail.put("mapId", 1);
		        	documentDetail.put("watchId", Integer.toString(watchPositionData.getWatchId()));
//		        	System.out.println(documentDetail);
		        	sampleData.insert(documentDetail);

		        } catch (Exception e) {
		        	System.out.print(e);
		        }
				
			}
			
		}
	}

}
